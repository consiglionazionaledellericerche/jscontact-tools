/*
 *    Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package it.cnr.iit.jscontact.tools.vcard.converters.ezvcard2jscontact;

import com.fasterxml.jackson.databind.JsonNode;
import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.ValidationWarnings;
import ezvcard.parameter.RelatedType;
import ezvcard.property.*;
import ezvcard.property.Organization;
import ezvcard.property.Title;
import ezvcard.property.VCardProperty;
import ezvcard.util.DataUri;
import ezvcard.util.GeoUri;
import ezvcard.util.UtcOffset;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.Calendar;
import it.cnr.iit.jscontact.tools.dto.Nickname;
import it.cnr.iit.jscontact.tools.dto.Note;
import it.cnr.iit.jscontact.tools.dto.TimeZone;
import it.cnr.iit.jscontact.tools.dto.comparators.JSCardAddressesComparator;
import it.cnr.iit.jscontact.tools.dto.comparators.VCardPropertiesAltidComparator;
import it.cnr.iit.jscontact.tools.dto.comparators.VCardPropertiesPrefComparator;
import it.cnr.iit.jscontact.tools.dto.interfaces.VCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.*;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContactProfileIds;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedAddress;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedStructuredName;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for converting a vCard 4.0 [RFC6350] instance represented as an Ezvcard VCard object [ez-vcard] into a JSContact object [RFC9553].
 *
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC9553</a>
 * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard</a>
 * @author Mario Loffredo
 */
@NoArgsConstructor
@ToString
public abstract class EZVCard2JSContact extends AbstractConverter {

    private static final Pattern TIMEZONE_AS_UTC_OFFSET_PATTERN = Pattern.compile("[+-](\\d{2}):?(\\d{2})");

    private static final String CUSTOM_TIME_ZONE_ID_PREFIX = "TZ";
    public static final String CUSTOM_TIME_ZONE_RULE_START = "1900-01-01T00:00:00";

    private VCardPropertiesAltidComparator vCardPropertiesAltidComparator;
    private VCardPropertiesPrefComparator vCardPropertiesPrefComparator;

    protected VCard2JSContactConfig config;

    private int customTimeZoneCounter = 0;

    private final Map<String, TimeZone> customTimeZones = new HashMap<>();

    private static final Map<String, PhoneFeatureEnum> phoneFeatureAliases = new HashMap<>() {{ put ("cell", PhoneFeatureEnum.MOBILE);}};

    private static final Map<String, String> vCardSpecificPropertyVsClassMap = new HashMap<>() {{
            put("ExtendedStructuredName", "N");
            put("ExtendedAddress", "ADR");
            put("FormattedName", "FN");
            put("Birthday", "BDAY");
            put("Telephone", "TEL");
            put("Language", "LANG");
            put("Timezone", "TZ");
            put("Organization", "ORG");
            put("ProductId", "PRODID");
            put("Revision", "REV");
            put("FreeBusyUrl", "FBURL");
            put("CalendarRequestUri", "CALADRURI");
            put("CalendarUri", "CALURI");
            put("OrgDirectory", "ORG-DIRECTORY");
            put("ContactUri", "CONTACT-URI");
        }};

    private static String getVCardPropertyNameFromClassName(String className) {

        className = className.substring(className.lastIndexOf('.')+1);
        String vCardPropertyName = vCardSpecificPropertyVsClassMap.get(className);
        if (vCardPropertyName == null)
            vCardPropertyName = className;
        return vCardPropertyName.toLowerCase();
    }

    private static void addVCardUnconvertedParams(Card jsCard, String jsContactPath, String vCardPropertyName, Map<String, VCardParam> unconvertedParams ) {

        if (unconvertedParams != null)
            jsCard.addVCardConvertedProp(jsContactPath,
                    it.cnr.iit.jscontact.tools.dto.VCardProperty.builder()
                            .name(vCardPropertyName)
                            .parameters(unconvertedParams)
                            .build());

    }

    private String getJSCardId(JSContactProfileIds.IdType idType, int index, String id, String jsid, Object... args) {

        if (config.isUseJsds() && jsid != null)
            return jsid;

        if (config.getProfileIdsToUse() == null || config.getProfileIdsToUse().getIds() == null || config.getProfileIdsToUse().getIds().size() == 0)
            return id;

        return config.getProfileIdsToUse().getMapId(idType, index, args);
    }

    private boolean isProfielIdsToUsePhoneFeatureSpecific() {

        if (config.getProfileIdsToUse() == null || config.getProfileIdsToUse().getIds() == null || config.getProfileIdsToUse().getIds().size() == 0)
            return false;

        return config.getProfileIdsToUse().isPhoneFeatureSpecific();
    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> E getJSCardEnumFromVCardTypeParam(Class<E> enumType, String vcardTypeParam, List<String> exclude, Map<String,E> aliases) {

        if (vcardTypeParam == null)
            return null;

        String[] items = vcardTypeParam.split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        for (String item : items) {
            if (exclude!=null && exclude.contains(item))
                continue;
            try {
                return EnumUtils.getEnum(enumType, item, aliases);
            } catch (IllegalArgumentException ignored) {
            }
            try {
                return EnumUtils.getEnum(enumType, item.toLowerCase(), aliases);
            } catch (IllegalArgumentException ignored) {
            }
        }

        return null;
    }


    private static <E extends Enum<E> & VCardTypeDerivedEnum> List<E> getJSCardEnumValues(Class<E> enumType, String vcardTypeParam, List<String> exclude, Map<String, E> aliases) {

        if (vcardTypeParam == null)
            return null;

        List<E> enumValues = new ArrayList<>();
        String[] typeItems = vcardTypeParam.split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        for (String typeItem : typeItems) {
            try {
                E enumInstance = getJSCardEnumFromVCardTypeParam(enumType, typeItem, exclude, aliases);
                if (enumInstance != null)
                    enumValues.add(enumInstance);
            } catch (Exception e) {
                throw new InternalErrorException(e.getMessage());
            }
        }

        return (enumValues.size() > 0) ? enumValues : null;
    }

    private static Map<Context,Boolean> toJSCardContexts(String vcardTypeParam) {
        List<ContextEnum> enumValues = getJSCardEnumValues(ContextEnum.class,vcardTypeParam, null, ContextEnum.getAliases());
        if (enumValues == null) return null;

        Map<Context,Boolean> contexts = new HashMap<>();
        for (ContextEnum enumValue : enumValues) {
            contexts.put(Context.rfc(enumValue), Boolean.TRUE);
        }

        return contexts;
    }

    private static Map<AddressContext,Boolean> toJSCardAddressContexts(String vcardTypeParam) {
        List<AddressContextEnum> enumValues =  getJSCardEnumValues(AddressContextEnum.class,vcardTypeParam, null, AddressContextEnum.getAliases());
        if (enumValues == null) return null;

        Map<AddressContext,Boolean> contexts = new HashMap<>();
        for (AddressContextEnum enumValue : enumValues) {
            contexts.put(AddressContext.rfc(enumValue), Boolean.TRUE);
        }

        return contexts;
    }

    private static Map<PhoneFeature,Boolean> toJSCardPhoneFeatures(String vcardTypeParam) {
        List<PhoneFeatureEnum> enumValues = getJSCardEnumValues(PhoneFeatureEnum.class,vcardTypeParam, Arrays.asList("home", "work"), phoneFeatureAliases);

        Map<PhoneFeature,Boolean> phoneTypes = new HashMap<>();
        if (enumValues != null) {
            for (PhoneFeatureEnum enumValue : enumValues) {
                phoneTypes.put(PhoneFeature.rfc(enumValue), Boolean.TRUE);
            }
        }

        if (phoneTypes.size() == 0)
            return null;

        return phoneTypes;
    }

    private static Integer toJSCardPref(String vcardPref) {
        return (vcardPref != null) ? Integer.parseInt(vcardPref) : null;
    }

    private static String toJSCardAutoFulllAddress(ExtendedAddress addr) {

        StringJoiner joiner = new StringJoiner(DelimiterUtils.NEWLINE_DELIMITER);
        if (StringUtils.isNotEmpty(addr.getPoBox())) joiner.add(addr.getPoBox());
        if (StringUtils.isNotEmpty(addr.getExtendedAddressFull())) joiner.add(addr.getExtendedAddressFull());
        if (StringUtils.isNotEmpty(addr.getStreetAddressFull())) joiner.add(addr.getStreetAddressFull());
        if (StringUtils.isNotEmpty(addr.getLocality())) joiner.add(addr.getLocality());
        if (StringUtils.isNotEmpty(addr.getRegion())) joiner.add(addr.getRegion());
        if (StringUtils.isNotEmpty(addr.getPostalCode())) joiner.add(addr.getPostalCode());
        if (StringUtils.isNotEmpty(addr.getCountry())) joiner.add(addr.getCountry());
        String autoFullAddress = joiner.toString();
        return StringUtils.isNotEmpty(autoFullAddress) ? autoFullAddress : null;
    }

    private String toJSCardFulllAddress(String addressLabel, String autoFullAddress) {

        String fullAddress = addressLabel;
        if (fullAddress == null && config.isSetAutoFullAddress()) {
            fullAddress = autoFullAddress;
        }

        return fullAddress;
    }

    private String toJSCardLabel(VCardProperty vcardProperty, List<RawProperty> vcardExtendedProperties) {

        String group = vcardProperty.getGroup();

        if (group == null)
            return null;

        for (RawProperty rawProperty : vcardExtendedProperties) {
            if (rawProperty.getPropertyName().equalsIgnoreCase(VCardPropEnum.X_ABLABEL.getValue()) && rawProperty.getGroup().equals(group))
                return rawProperty.getValue();
        }

        return null;
    }

    private Resource toJSCardResource(VCardProperty property) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        String vcardType = VCardUtils.getVCardParamValue(property.getParameters(), VCardParamEnum.TYPE);
        Map<Context,Boolean> contexts = toJSCardContexts(vcardType);

        return  Resource.builder()
                .jsid(getJSIDParameter(property))
                .uri(value)
                .contexts(contexts)
                .mediaType(toJSCardMediaType(VCardUtils.getVCardParamValue(property.getParameters(), VCardParamEnum.MEDIATYPE), value))
                .pref(toJSCardPref(VCardUtils.getVCardParamValue(property.getParameters(), VCardParamEnum.PREF)))
                .build();
    }

    private void addJSCardMediaResource(VCardProperty vcardProperty, Card jsCard, MediaKind type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addMediaResource(getJSCardId(JSContactProfileIds.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), getJSIDParameter(vcardProperty),  ResourceType.valueOf(type.getRfcValue().name())),
                                    Media.builder()
                                            .kind(type)
                                            .jsid(resource.getJsid())
                                            .uri(resource.getUri())
                                            .contexts(resource.getContexts())
                                            .mediaType(resource.getMediaType())
                                            .pref(resource.getPref())
                                            .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                                            .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(vcardProperty.getClass().getName()))
                                            .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
                                    .build()
                           );
    }

    private Directory toJSCardDirectoryResource(VCardProperty vcardProperty, DirectoryKind type, List<RawProperty> vcardExtendedProperties) {

        String index = vcardProperty.getParameter(VCardParamEnum.INDEX.getValue());
        Resource resource = toJSCardResource(vcardProperty);
        return Directory.builder()
                .kind(type)
                .jsid(resource.getJsid())
                .uri(resource.getUri())
                .contexts(resource.getContexts())
                .mediaType(resource.getMediaType())
                .pref(resource.getPref())
                .listAs((index != null) ? Integer.parseInt(index) : null) //used only for Directory objects whose "kind" is "directory"
                .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(vcardProperty.getClass().getName()))
                .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE, VCardParamEnum.INDEX))
                .build();
    }

    private void addJSCardDirectoryResource(VCardProperty property, Card jsCard, DirectoryKind type, int index, List<RawProperty> vcardExtendedProperties) {

        jsCard.addDirectoryResource(getJSCardId(JSContactProfileIds.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), getJSIDParameter(property), type),
                                    toJSCardDirectoryResource(property,type, vcardExtendedProperties));
    }

    private void addJSCardDirectoryResource(Card jsCard, Directory resource, int index) {

        jsCard.addDirectoryResource(getJSCardId(JSContactProfileIds.IdType.RESOURCE, index, String.format("%s-%s",resource.getKind().getRfcValue().name(),index), resource.getJsid(), ResourceType.valueOf(resource.getKind().getRfcValue().name())),
                                   resource);
    }

    private void addJSCardCryptoResource(VCardProperty vcardProperty, Card jsCard, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addCryptoResource(getJSCardId(JSContactProfileIds.IdType.RESOURCE, index, String.format("KEY-%s",index), getJSIDParameter(vcardProperty), ResourceType.KEY),
                CryptoKey.builder()
                        .jsid(resource.getJsid())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                        .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(vcardProperty.getClass().getName()))
                        .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
                        .build()
        );
    }

    private void addJSCardCalendarResource(VCardProperty vcardProperty, Card jsCard, CalendarKind type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addCalendarResource(getJSCardId(JSContactProfileIds.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), getJSIDParameter(vcardProperty), ResourceType.valueOf(type.getRfcValue().name())),
                Calendar.builder()
                        .kind(type)
                        .jsid(resource.getJsid())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                        .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(vcardProperty.getClass().getName()))
                        .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
                        .build()
        );
    }

    private void addJSCardLinkResource(VCardProperty vcardProperty, Card jsCard, LinkKind type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        String className;
        if (vcardProperty instanceof Url)
            className = vcardProperty.getClass().getName();
        else
            className = ".ContactUri";

        jsCard.addLinkResource(getJSCardId(JSContactProfileIds.IdType.RESOURCE, index, String.format("%s-%s",(type!=null) ? type.getRfcValue().name() : "LINK",index), getJSIDParameter(vcardProperty), (type!=null) ? ResourceType.valueOf(type.getRfcValue().name()) : ResourceType.LINK),
                Link.builder()
                        .kind(type)
                        .jsid(resource.getJsid())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                        .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(className))
                        .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
                        .build()
        );
    }

    private static String getValue(GeoUri geoUri) {

        return (geoUri!=null) ? geoUri.toUri().toString() : null;
    }

    private static it.cnr.iit.jscontact.tools.dto.Address getValue(PlaceProperty property) {

        if (property == null)
            return null;

        if (property.getText() != null && property.getGeoUri() == null)
            return it.cnr.iit.jscontact.tools.dto.Address.builder()
                    .full(property.getText())
                    .build();
        else if (property.getGeoUri() != null)
            return it.cnr.iit.jscontact.tools.dto.Address.builder()
                                                         .coordinates(getValue(property.getGeoUri()))
                                                         .build();
        else
            return null;
    }


    @Deprecated
    private static String getValue(DateOrTimeProperty property) {

        if (property.getText() != null)
            return property.getText();
        else if (property.getPartialDate()!=null)
            return property.getPartialDate().toISO8601(true);
        else
            return DateUtils.toString(property.getCalendar());
    }

    private static String getValue(Telephone property) {

        if (property.getText() != null)
            return property.getText();
        else
            return property.getUri().toString();
    }

    private static String getValue(Related property) {

        if (property.getText() != null)
            return property.getText();
        else
            return property.getUri();
    }

    private String getValue(Timezone property) {

        if (property.getText()!= null)
            return property.getText();
        else {
            String offset = property.getOffset().toString();
            String sign = offset.substring(0,1);
            String hours = offset.substring(1,3);
            String minutes = offset.substring(3,5);
            if (minutes.equals("00"))
                return String.format("Etc/GMT%s%s%s",
                                               (hours.equals("00")) ? StringUtils.EMPTY : (sign.equals("+") ? "-" : "+") ,
                                               (hours.equals("00")) ? StringUtils.EMPTY : String.valueOf(Integer.parseInt(hours)),
                                               StringUtils.EMPTY);
            else {
                String timeZoneName = String.format("%s%d", config.getCustomTimeZonesPrefix(), ++customTimeZoneCounter);
                customTimeZones.put(timeZoneName, TimeZone.builder()
                        .tzId(String.format("%s%s%s%s",CUSTOM_TIME_ZONE_ID_PREFIX,sign,hours,minutes))
                        .updated(java.util.Calendar.getInstance())
                        .standardItem(TimeZoneRule.builder()
                                .offsetFrom(String.format("%s%s%s",sign,hours,minutes))
                                .offsetTo(String.format("%s%s%s",sign,hours,minutes))
                                .start(DateUtils.toCalendar(CUSTOM_TIME_ZONE_RULE_START))
                                .build()
                        )
                        .build()
                );
                return timeZoneName;
            }
        }
    }

    private static String getValue(BinaryProperty property) {

        if (property.getUrl()!=null)
            return property.getUrl();
        else
            return new DataUri(property.getContentType().getMediaType(), property.getData()).toString();
    }

    private static String getValue(TextProperty property) {

        if (property == null)
            return null;

        return property.getValue();
    }

    private static String getValue(Impp property) {

        return property.getUri().toString();
    }

    private void fillJSCardSpeakToAsOrGender(VCard vCard, Card jsCard) {

        if (vCard.getGender() == null)
            return;

        if (!config.isConvertGenderToSpeakToAs()) {
            jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                                         .name(V_Extension.toV_Extension(VCardPropEnum.GENDER.getValue().toLowerCase()))
                                         .parameters(VCardUtils.getVCardUnconvertedPropParams(vCard.getGender().getParameters()))
                                         .type(VCardDataType.TEXT)
                                         .value(String.format("%s%s", vCard.getGender().getGender(), (vCard.getGender().getText()==null) ? StringUtils.EMPTY : ";"+vCard.getGender().getText()))
                                         .build());
            return;
        }

        if (vCard.getExtendedProperty(VCardPropEnum.GRAMGENDER.getValue()) != null)
            return;

        if (vCard.getGender() != null) {
            if (vCard.getGender().isMale())
                jsCard.setSpeakToAs(SpeakToAs.masculine());
            else if (vCard.getGender().isFemale())
                jsCard.setSpeakToAs(SpeakToAs.feminine());
            else if (vCard.getGender().isOther())
                jsCard.setSpeakToAs(SpeakToAs.animate());
            else if (vCard.getGender().isNone())
                jsCard.setSpeakToAs(SpeakToAs.common());
        } else {
            jsCard.setSpeakToAs(SpeakToAs.builder().grammaticalGender(GrammaticalGenderType.getEnum(vCard.getExtendedProperty(VCardPropEnum.GRAMGENDER.getValue()).getValue().toLowerCase())).build());
        }
    }


    private static boolean structuredNameWithAltidExists(List<ExtendedStructuredName> extendedStructuredNames, String altid, String language) {

        if (extendedStructuredNames == null || extendedStructuredNames.isEmpty())
            return false;

        for (ExtendedStructuredName esn : extendedStructuredNames) {
            if (language != null) {
                if (esn.getAltId()!= null && esn.getAltId().equals(altid) && esn.getLanguage()!=null && esn.getLanguage().equals(language))
                    return true;
            }
            else if (esn.getAltId()!= null && esn.getAltId().equals(altid))
                return true;
        }

        return false;
    }

    private boolean isFNDerivedFromN(FormattedName fn, VCard vcard) {

        List<FormattedName> fns = vcard.getFormattedNames();
        if (fn.getParameter(VCardParamEnum.DERIVED.getValue()) != null && Boolean.parseBoolean(fn.getParameter(VCardParamEnum.DERIVED.getValue())) == Boolean.TRUE ) {
            if (fns.size() == 1) {
                if (vcard.getProperties(ExtendedStructuredName.class) != null && !vcard.getProperties(ExtendedStructuredName.class).isEmpty()) // structured name corresponding to full name exists
                    return true;
            } else {
                if (fn.getAltId()!=null && structuredNameWithAltidExists(vcard.getProperties(ExtendedStructuredName.class), fn.getAltId(), fn.getLanguage())) // structured name corresponding to full name exists
                    return true;
            }
        }
        return false;
    }

    private boolean isFNDerivedFromUid(FormattedName fn, VCard vcard) {

        if (vcard.getUid() != null && fn.getValue().equals(vcard.getUid().getValue()))
            return true;

        return false;
    }

    private void fillJSCardFullName(VCard vcard, Card jsCard) {

        if (vcard.getFormattedNames() == null || vcard.getFormattedNames().isEmpty())
            return;

        List<FormattedName> fns = vcard.getFormattedNames();
        fns.sort(vCardPropertiesAltidComparator);
        String lastAltid = null;

        FormattedName candidateFn = null;
        int candidatePref = 101;
        for (FormattedName fn : fns) {
            if (fn.getLanguage() == null) {
                if (candidateFn == null) {
                    candidateFn = fn;
                    if (fn.getPref()!=null)
                        candidatePref = fn.getPref();
                }
                else if (fn.getPref()!=null && (candidatePref == 101 || fn.getPref() < candidatePref)) {
                    candidateFn = fn;
                    candidatePref = candidateFn.getPref();
                } else if (candidatePref == 101 && fn.getParameters().size() < candidateFn.getParameters().size())
                    candidateFn = fn;
            }
        }

        for (FormattedName fn : fns) {

            if (isFNDerivedFromN(fn,vcard) || isFNDerivedFromUid(fn,vcard))
                continue;

            if (candidateFn != null && candidateFn.getValue().equals(fn.getValue())) {

                if (jsCard.getName() == null) //no N property exists
                    jsCard.setName(Name.builder().full(fn.getValue()).build());
                else
                    jsCard.getName().setFull(fn.getValue());

                addVCardUnconvertedParams(jsCard, "name/full", "fn", VCardUtils.getVCardParamsOtherThan(fn, VCardParamEnum.LANGUAGE, VCardParamEnum.ALTID));
                continue;
            }

            if (fn.getLanguage() == null) {
                jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                        .name(V_Extension.toV_Extension(VCardPropEnum.FN.getValue().toLowerCase()))
                        .parameters(VCardUtils.getVCardUnconvertedPropParams(fn.getParameters()))
                        .type(VCardDataType.TEXT)
                        .value(fn.getValue())
                        .build());
            } else {
                if (fn.getAltId() == null || lastAltid == null || !fn.getAltId().equals(lastAltid)) {
                    if (jsCard.getName() == null) //no N property exists
                        jsCard.setName(Name.builder().full(fn.getValue()).build());
                    else
                        jsCard.getName().setFull(fn.getValue());

                    lastAltid = fn.getAltId();
                    addVCardUnconvertedParams(jsCard, "name/full", "fn", VCardUtils.getVCardParamsOtherThan(fn, VCardParamEnum.LANGUAGE, VCardParamEnum.ALTID));
                } else {
                    jsCard.addLocalization(fn.getLanguage(), "name", mapper.convertValue(Name.builder().full(fn.getValue()).build(), JsonNode.class));
                    addVCardUnconvertedParams(jsCard, String.format("localizations/%s/name~1full", fn.getLanguage()), "fn", VCardUtils.getVCardParamsOtherThan(fn, VCardParamEnum.LANGUAGE, VCardParamEnum.ALTID));
                }
            }
        }
    }

    private void fillJSCardMembers(VCard vcard, Card jsCard) {

        List<Member> members = vcard.getMembers();
        members.sort(vCardPropertiesPrefComparator);
        for (Member member : members) {
            jsCard.addMember(member.getValue());
            addVCardUnconvertedParams(jsCard,"members/" + member.getValue(),"member", VCardUtils.getVCardParamsOtherThan(member));
        }
    }


    private static Map<NameComponentKind, String> toJSCardNameSortAs(List<String> vcardSortAs, ExtendedStructuredName sn) {

        if (vcardSortAs == null || vcardSortAs.isEmpty())
            return null;

        Map<NameComponentKind, String> sortAs = new HashMap<>();
        NameComponentEnum[] nameComponentEnumValues = NameComponentEnum.values();
        int i = 0;
        for (String vcardSortAsItem : vcardSortAs) {
            String[] vcardSortAsItemSubs = vcardSortAsItem.split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
            for (String vcardSortAsItemSub : vcardSortAsItemSubs)
                sortAs.put(NameComponentKind.rfc(nameComponentEnumValues[i++]), vcardSortAsItemSub);
        }
        return sortAs;
    }

    private Name toJSCardName(ExtendedStructuredName vcardName) {

        NameComponent[] components = null;

        String[] jscomps = (vcardName.getParameter(VCardParamEnum.JSCOMPS.getValue())!=null) ? vcardName.getParameter(VCardParamEnum.JSCOMPS.getValue()).split(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER) : null;

        boolean isPhonetic = (vcardName.getParameter(VCardParamEnum.PHONETIC.getValue())!=null) || (vcardName.getParameter(VCardParamEnum.SCRIPT.getValue())!=null);

        int indexPerKind=0;
        if (jscomps == null || jscomps.length < 2) {

            if (vcardName.getFamilyNames() != null) {
                for (String surname : vcardName.getFamilyNames()) {
                    if (vcardName.getSurname2().contains(surname))
                        continue;
                    components = Name.addComponent(components, NameComponent.builder().kind(NameComponentKind.surname()).value(surname).phonetic((isPhonetic) ? surname : null).indexPerKind(indexPerKind).build());
                }
            }

            if (vcardName.getGiven() != null) {
                String[] names = vcardName.getGiven().split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                for (String name : names)
                    components = Name.addComponent(components, NameComponent.builder().kind(NameComponentKind.given()).value(name).phonetic((isPhonetic) ? name : null).indexPerKind(indexPerKind).build());
            }

            for (String an : vcardName.getAdditionalNames())
                components = Name.addComponent(components, NameComponent.builder().kind(NameComponentKind.given2()).value(an).phonetic((isPhonetic) ? an : null).indexPerKind(indexPerKind).build());

            for (String px : vcardName.getPrefixes())
                components = Name.addComponent(components, NameComponent.builder().kind(NameComponentKind.title()).value(px).phonetic((isPhonetic) ? px : null).indexPerKind(indexPerKind).build());

            for (String sx : vcardName.getSuffixes()) {
                if (vcardName.getGeneration().contains(sx))
                    continue;
                components = Name.addComponent(components, NameComponent.builder().kind(NameComponentKind.credential()).value(sx).phonetic((isPhonetic) ? sx : null).indexPerKind(indexPerKind).build());
            }

            for (String sx : vcardName.getSurname2())
                components = Name.addComponent(components, NameComponent.builder().kind(NameComponentKind.surname2()).value(sx).phonetic((isPhonetic) ? sx : null).indexPerKind(indexPerKind).build());

            for (String sx : vcardName.getGeneration())
                components = Name.addComponent(components, NameComponent.builder().kind(NameComponentKind.generation()).value(sx).phonetic((isPhonetic) ? sx : null).indexPerKind(indexPerKind).build());
        }
        else {

            for (int i = 1; i < jscomps.length; i++) {

                if (jscomps[i].startsWith(DelimiterUtils.SEPARATOR_ID)) {
                    components = Name.addComponent(components, NameComponent.separator(jscomps[i].replace(DelimiterUtils.SEPARATOR_ID + "\\",StringUtils.EMPTY).replace(DelimiterUtils.SEPARATOR_ID, StringUtils.EMPTY)));
                    continue;
                }

                String[] items = jscomps[i].split(DelimiterUtils.COMMA_ARRAY_DELIMITER);

                int index1 = Integer.parseInt(items[0]);
                int index2 = (items.length == 1) ? 0 : Integer.parseInt(items[1]);
                NameComponentKind kind = null;
                String value = null;
                switch (index1) {
                    case 0:
                        kind = NameComponentKind.surname();
                        value = vcardName.getFamilyNames().get(index2);
                        break;
                    case 1:
                        kind = NameComponentKind.given();
                        String[] names = vcardName.getGiven().split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                        value = names[index2];
                        break;
                    case 2:
                        kind = NameComponentKind.given2();
                        value = vcardName.getAdditionalNames().get(index2);
                        break;
                    case 3:
                        kind = NameComponentKind.title();
                        value = vcardName.getPrefixes().get(index2);
                        break;
                    case 4:
                        kind = NameComponentKind.credential();
                        value = vcardName.getSuffixes().get(index2);
                        break;
                    case 5:
                        kind = NameComponentKind.surname2();
                        value = vcardName.getSurname2().get(index2);
                        break;
                    case 6:
                        kind = NameComponentKind.generation();
                        value = vcardName.getGeneration().get(index2);
                        break;
                }

                components = Name.addComponent(components, NameComponent.builder().kind(kind).value(value).phonetic((isPhonetic) ? value : null).indexPerKind(index2).build());
            }
        }

        PhoneticSystem phoneticSystem = null;

        if (vcardName.getParameter(VCardParamEnum.PHONETIC.getValue())!=null && !vcardName.getParameter(VCardParamEnum.PHONETIC.getValue()).equalsIgnoreCase(PhoneticSystemEnum.SCRIPT.getValue())) {
            try {
                phoneticSystem = PhoneticSystem.rfc(PhoneticSystemEnum.getEnum(vcardName.getParameter(VCardParamEnum.PHONETIC.getValue())));
            } catch (IllegalArgumentException e) {
                phoneticSystem = PhoneticSystem.ext(vcardName.getParameter(VCardParamEnum.PHONETIC.getValue()));
            }
        }

        return Name.builder()
                .components(components)
                .sortAs(toJSCardNameSortAs(vcardName.getSortAs(), vcardName))
                .defaultSeparator((ArrayUtils.isNotEmpty(jscomps) && jscomps[0].startsWith(DelimiterUtils.SEPARATOR_ID)) ? jscomps[0].replace(DelimiterUtils.SEPARATOR_ID + "\\",StringUtils.EMPTY).replace(DelimiterUtils.SEPARATOR_ID,StringUtils.EMPTY) : null)
                .isOrdered((jscomps!=null) ? Boolean.TRUE : null)
                .phoneticSystem(phoneticSystem)
                .phoneticScript(vcardName.getParameter(VCardParamEnum.SCRIPT.getValue()))
                .build();
    }


    private String getFullNamePerLanguage(VCard vcard, String language) {

        if (vcard.getFormattedNames() == null)
            return null;

        for (FormattedName fn : vcard.getFormattedNames()) {

            if (isFNDerivedFromN(fn, vcard)|| isFNDerivedFromUid(fn,vcard))
                continue;

            if (fn.getLanguage()!= null && fn.getLanguage().equals(language))
                return fn.getValue();
        }

        return null;
    }


    private Name getNamePlusPhonetic(Name name, Name phoneticName) {

        name.setPhoneticSystem(phoneticName.getPhoneticSystem());
        name.setPhoneticScript(phoneticName.getPhoneticScript());

        for (int i = 0; i < name.getComponents().length; i++) {
            for (int j=0; j < phoneticName.getComponents().length; j++) {

                if (name.getComponents()[i].getKind().equals(phoneticName.getComponents()[j].getKind()) &&
                    name.getComponents()[i].getIndexPerKind() == phoneticName.getComponents()[j].getIndexPerKind())
                    name.getComponents()[i].setPhonetic(phoneticName.getComponents()[j].getPhonetic());
            }
        }

        return name;
    }

    private void fillJSCardNames(VCard vcard, Card jsCard) {

        if (vcard.getProperties(ExtendedStructuredName.class) == null || vcard.getProperties(ExtendedStructuredName.class).isEmpty())
            return;

        List<ExtendedStructuredName> vcardNames = vcard.getProperties(ExtendedStructuredName.class);
        vcardNames.sort(vCardPropertiesAltidComparator);

        Name name = toJSCardName(vcardNames.get(0));
        if (jsCard.getName() != null) // full name already been set,
            name.setFull(jsCard.getName().getFull());
        addVCardUnconvertedParams(jsCard,"name/components","n", VCardUtils.getVCardParamsOtherThan(vcardNames.get(0), VCardParamEnum.LANGUAGE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS, VCardParamEnum.PHONETIC, VCardParamEnum.SCRIPT));
        jsCard.setName(name);

        if (vcardNames.size() == 2) {
            boolean isPhonetic = (vcardNames.get(1).getParameter(VCardParamEnum.PHONETIC.getValue())!=null) || (vcardNames.get(1).getParameter(VCardParamEnum.SCRIPT.getValue())!=null);
            if (isPhonetic) { // this is a phonetic
                jsCard.setName(getNamePlusPhonetic(jsCard.getName(), toJSCardName(vcardNames.get(1))));
            }
        }

        if (jsCard.getLanguage() == null && vcardNames.get(0).getLanguage() != null)
            jsCard.setLanguage(vcardNames.get(0).getLanguage());

        for (int i = 1; i < vcardNames.size(); i++) {
            String language = vcardNames.get(i).getLanguage();

            name = toJSCardName(vcardNames.get(i));

            if (name.getPhoneticSystem() != null || name.getPhoneticScript() != null) {
                if (name.getPhoneticSystem()!= null)
                    jsCard.addLocalization(language, "name/phoneticSystem", JsonNodeUtils.textNode(name.getPhoneticSystem().toJson()));
                if (name.getPhoneticScript()!= null)
                    jsCard.addLocalization(language, "name/phoneticScript", JsonNodeUtils.textNode(name.getPhoneticScript()));

                if (name.getPhoneticSystem()!=null)
                    addVCardUnconvertedParams(jsCard,String.format("localizations/%s/name~1phoneticSystem", language), "n", VCardUtils.getVCardParamsOtherThan(vcardNames.get(i), VCardParamEnum.LANGUAGE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS, VCardParamEnum.PHONETIC, VCardParamEnum.SCRIPT));
                else {
                    if (name.getPhoneticScript()!=null) {
                        addVCardUnconvertedParams(jsCard,String.format("localizations/%s/name~1phoneticScript", language), "n", VCardUtils.getVCardParamsOtherThan(vcardNames.get(i), VCardParamEnum.LANGUAGE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS, VCardParamEnum.PHONETIC, VCardParamEnum.SCRIPT));
                    }
                }

                if (name.getComponents() != null) {
                    int j = 0;
                    for (NameComponent component : name.getComponents()) {
                        if (component.getValue().equals(component.getPhonetic()))
                            jsCard.addLocalization(language, "name/components/" + j++ + "/phonetic", JsonNodeUtils.textNode(component.getPhonetic()));
                    }
                }
            }
            else if (jsCard.getLocalization(language, "name") == null) {
                jsCard.addLocalization(language, "name", mapper.convertValue(name, JsonNode.class));
                addVCardUnconvertedParams(jsCard, String.format("localizations/%s/name~1components", language), "n", VCardUtils.getVCardParamsOtherThan(vcardNames.get(i), VCardParamEnum.LANGUAGE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS, VCardParamEnum.PHONETIC, VCardParamEnum.SCRIPT));
            }
            else {
                name.setFull(getFullNamePerLanguage(vcard, language));
                jsCard.addLocalization(language, "name", mapper.convertValue(name, JsonNode.class));
                addVCardUnconvertedParams(jsCard, String.format("localizations/%s/name~1components", language), "n", VCardUtils.getVCardParamsOtherThan(vcardNames.get(i), VCardParamEnum.LANGUAGE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS, VCardParamEnum.PHONETIC, VCardParamEnum.SCRIPT));
            }
        }
    }

    private Nickname toJSCardNickName(String name, ezvcard.property.Nickname vcardNickname, VCard vcard) {

        return Nickname.builder()
                .name(name)
                .pref(vcardNickname.getPref())
                .contexts(toJSCardContexts(vcardNickname.getType()))
                .build();
    }

    private void fillJSCardNickNames(VCard vcard, Card jsCard) {

        if (vcard.getNicknames() == null || vcard.getNicknames().isEmpty())
            return;

        List<ezvcard.property.Nickname> vcardNickNames = vcard.getNicknames();
        vcardNickNames.sort(vCardPropertiesAltidComparator);
        int i = 1;
        String lastAltid = null;
        String lastMapId = null;
        for (ezvcard.property.Nickname vcardNickName : vcardNickNames) {
            for (String name : vcardNickName.getValues()) {
                if (vcardNickName.getAltId() == null || lastAltid == null || !vcardNickName.getAltId().equals(lastAltid)) {
                    String jsid = getJSIDParameter(vcardNickName);
                    String id = getJSCardId(JSContactProfileIds.IdType.NICKNAME, i, "NICK-" + (i++), jsid);
                    jsCard.addNickName(id, toJSCardNickName(name, vcardNickName, vcard));
                    lastAltid = vcardNickName.getAltId();
                    lastMapId = id;
                    addVCardUnconvertedParams(jsCard,"nicknames/"+ lastMapId,"nickname", VCardUtils.getVCardParamsOtherThan(vcardNickName, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.PREF, VCardParamEnum.ALTID));
                }
                else {
                    jsCard.addLocalization(vcardNickName.getLanguage(), "nicknames/" + lastMapId, mapper.convertValue(toJSCardNickName(name, vcardNickName, vcard), JsonNode.class));
                    addVCardUnconvertedParams(jsCard,String.format("localizations/%s/nicknames~1%s", vcardNickName.getLanguage(), lastMapId),"nickname", VCardUtils.getVCardParamsOtherThan(vcardNickName, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.PREF, VCardParamEnum.ALTID));
                }
            }
        }
    }

    private static it.cnr.iit.jscontact.tools.dto.Address findJSCardAddressByGroup(List<it.cnr.iit.jscontact.tools.dto.Address> addresses, String group) {

            if (group == null)
                return null;

            for (Address address : addresses) {
                if (address.getGroup()!=null && address.getGroup().equalsIgnoreCase(group))
                    return address;
            }

            return null;
    }

    private String toJSCardTimezoneName(String vcardTzParam) {

        if (vcardTzParam == null)
            return null;

        Matcher m = TIMEZONE_AS_UTC_OFFSET_PATTERN.matcher(vcardTzParam);
        if (m.find())
            return getValue(new Timezone(UtcOffset.parse(vcardTzParam.replace(":", StringUtils.EMPTY))));
        else
            return vcardTzParam;
    }

    private Address toJSCardAddress(ExtendedAddress vcardAddr) {

        List<AddressComponent> streetDetailPairs = new ArrayList<>();

        String[] jscomps = (vcardAddr.getParameter(VCardParamEnum.JSCOMPS.getValue())!=null) ? vcardAddr.getParameter(VCardParamEnum.JSCOMPS.getValue()).split(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER) : null;

        boolean isPhonetic = (vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue())!=null) || (vcardAddr.getParameter(VCardParamEnum.SCRIPT.getValue())!=null);

        if (jscomps == null || jscomps.length < 2) {

            if (StringUtils.isNotEmpty(vcardAddr.getPoBox()))
                streetDetailPairs.add(AddressComponent.postOfficeBox(vcardAddr.getPoBox()));
            if (StringUtils.isNotEmpty(vcardAddr.getLocality()))
                streetDetailPairs.add(AddressComponent.locality(vcardAddr.getLocality(), (isPhonetic) ? vcardAddr.getLocality() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getRegion()))
                streetDetailPairs.add(AddressComponent.region(vcardAddr.getRegion(), (isPhonetic) ? vcardAddr.getRegion() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getPostalCode()))
                streetDetailPairs.add(AddressComponent.postcode(vcardAddr.getPostalCode()));
            if (StringUtils.isNotEmpty(vcardAddr.getCountry()))
                streetDetailPairs.add(AddressComponent.country(vcardAddr.getCountry(), (isPhonetic) ? vcardAddr.getCountry() : null));

            if (StringUtils.isNotEmpty(vcardAddr.getRoom()))
                streetDetailPairs.add(AddressComponent.room(vcardAddr.getRoom()));
            if (!vcardAddr.isExtended() && StringUtils.isNotEmpty(vcardAddr.getExtendedAddress()))
                streetDetailPairs.add(AddressComponent.apartment(vcardAddr.getExtendedAddress()));
            if (StringUtils.isNotEmpty(vcardAddr.getApartment()))
                streetDetailPairs.add(AddressComponent.apartment(vcardAddr.getApartment()));
            if (StringUtils.isNotEmpty(vcardAddr.getFloor()))
                streetDetailPairs.add(AddressComponent.floor(vcardAddr.getFloor()));
            if (!vcardAddr.isExtended() && StringUtils.isNotEmpty(vcardAddr.getStreetAddressFull()))
                streetDetailPairs.add(AddressComponent.name(vcardAddr.getStreetAddressFull(), (isPhonetic) ? vcardAddr.getStreetAddressFull() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getStreetNumber()))
                streetDetailPairs.add(AddressComponent.number(vcardAddr.getStreetNumber()));
            if (StringUtils.isNotEmpty(vcardAddr.getStreetName()))
                streetDetailPairs.add(AddressComponent.name(vcardAddr.getStreetName(), (isPhonetic) ? vcardAddr.getStreetName() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getBuilding()))
                streetDetailPairs.add(AddressComponent.building(vcardAddr.getBuilding(), (isPhonetic) ? vcardAddr.getBuilding() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getBlock()))
                streetDetailPairs.add(AddressComponent.block(vcardAddr.getBlock(), (isPhonetic) ? vcardAddr.getBlock() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getSubDistrict()))
                streetDetailPairs.add(AddressComponent.subdistrict(vcardAddr.getSubDistrict(), (isPhonetic) ? vcardAddr.getSubDistrict() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getDistrict()))
                streetDetailPairs.add(AddressComponent.district(vcardAddr.getDistrict(), (isPhonetic) ? vcardAddr.getDistrict() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getLandmark()))
                streetDetailPairs.add(AddressComponent.landmark(vcardAddr.getLandmark(), (isPhonetic) ? vcardAddr.getLandmark() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getDirection()))
                streetDetailPairs.add(AddressComponent.direction(vcardAddr.getDirection()));

         } else {

            for (int i = 1; i < jscomps.length; i++) {

                if (jscomps[i].startsWith(DelimiterUtils.SEPARATOR_ID)) {
                    streetDetailPairs.add(AddressComponent.separator(jscomps[i].replace(DelimiterUtils.SEPARATOR_ID + "\\",StringUtils.EMPTY).replace(DelimiterUtils.SEPARATOR_ID, StringUtils.EMPTY)));
                    continue;
                }

                String[] items = jscomps[i].split(DelimiterUtils.COMMA_ARRAY_DELIMITER);

                int index1 = Integer.parseInt(items[0]);
                int index2 = (items.length == 1) ? 0 : Integer.parseInt(items[1]);
                switch (index1) {
                    case 0:
                        streetDetailPairs.add(AddressComponent.postOfficeBox(vcardAddr.getPoBoxes().get(index2)));
                        break;
                    case 1:
                        if (!vcardAddr.isExtended())
                            streetDetailPairs.add(AddressComponent.apartment(vcardAddr.getExtendedAddress()));
                        break;
                    case 2:
                        if (!vcardAddr.isExtended())
                            streetDetailPairs.add(AddressComponent.name(vcardAddr.getStreetAddresses().get(index2), (isPhonetic) ? vcardAddr.getStreetAddresses().get(index2) : null));
                        break;
                    case 3:
                        streetDetailPairs.add(AddressComponent.locality(vcardAddr.getLocalities().get(index2), (isPhonetic) ? vcardAddr.getLocalities().get(index2) : null));
                        break;
                    case 4:
                        streetDetailPairs.add(AddressComponent.region(vcardAddr.getRegions().get(index2), (isPhonetic) ? vcardAddr.getRegions().get(index2) : null));
                        break;
                    case 5:
                        streetDetailPairs.add(AddressComponent.postcode(vcardAddr.getPostalCodes().get(index2)));
                        break;
                    case 6:
                        streetDetailPairs.add(AddressComponent.country(vcardAddr.getCountries().get(index2), (isPhonetic) ? vcardAddr.getCountries().get(index2) : null));
                        break;
                    case 7:
                        streetDetailPairs.add(AddressComponent.room(vcardAddr.getRooms().get(index2)));
                        break;
                    case 8:
                        streetDetailPairs.add(AddressComponent.apartment(vcardAddr.getApartments().get(index2)));
                        break;
                    case 9:
                        streetDetailPairs.add(AddressComponent.floor(vcardAddr.getFloors().get(index2)));
                        break;
                    case 10:
                        streetDetailPairs.add(AddressComponent.number(vcardAddr.getStreetNumbers().get(index2)));
                        break;
                    case 11:
                        streetDetailPairs.add(AddressComponent.name(vcardAddr.getStreetNames().get(index2), (isPhonetic) ? vcardAddr.getStreetNames().get(index2) : null));
                        break;
                    case 12:
                        streetDetailPairs.add(AddressComponent.building(vcardAddr.getBuildings().get(index2), (isPhonetic) ? vcardAddr.getBuildings().get(index2) : null));
                        break;
                    case 13:
                        streetDetailPairs.add(AddressComponent.block(vcardAddr.getBlocks().get(index2), (isPhonetic) ? vcardAddr.getBlocks().get(index2) : null));
                        break;
                    case 14:
                        streetDetailPairs.add(AddressComponent.subdistrict(vcardAddr.getSubDistricts().get(index2), (isPhonetic) ? vcardAddr.getSubDistricts().get(index2) : null));
                        break;
                    case 15:
                        streetDetailPairs.add(AddressComponent.district(vcardAddr.getDistricts().get(index2), (isPhonetic) ? vcardAddr.getDistricts().get(index2) : null));
                        break;
                    case 16:
                        streetDetailPairs.add(AddressComponent.landmark(vcardAddr.getLandmarks().get(index2), (isPhonetic) ? vcardAddr.getLandmarks().get(index2) : null));
                        break;
                    case 17:
                        streetDetailPairs.add(AddressComponent.direction(vcardAddr.getDirections().get(index2)));
                        break;
                }
            }

        }

        String autoFullAddress = toJSCardAutoFulllAddress(vcardAddr);
        String vcardTypeParam = VCardUtils.getVCardParamValue(vcardAddr.getParameters(), VCardParamEnum.TYPE);

        PhoneticSystem phoneticSystem = null;
        if (vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue())!=null && !vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue()).equalsIgnoreCase(PhoneticSystemEnum.SCRIPT.getValue())) {
            try {
                phoneticSystem = PhoneticSystem.rfc(PhoneticSystemEnum.getEnum(vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue())));
            } catch (IllegalArgumentException e) {
                phoneticSystem = PhoneticSystem.ext(vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue()));
            }
        }

        return it.cnr.iit.jscontact.tools.dto.Address.builder()
                .hash(autoFullAddress)
                .jsid(getJSIDParameter(vcardAddr))
                .contexts(toJSCardAddressContexts(vcardTypeParam))
                .full(toJSCardFulllAddress(vcardAddr.getLabel(), autoFullAddress))
                .pref(vcardAddr.getPref())
                .coordinates(getValue(vcardAddr.getGeo()))
                .timeZone(toJSCardTimezoneName(vcardAddr.getTimezone()))
                .countryCode((VCardParamEnum.CC.getValue()!=null) ? vcardAddr.getParameter(VCardParamEnum.CC.getValue()) : vcardAddr.getParameter(VCardParamEnum.ISO_3166_1_ALPHA_2.getValue()))
                .components((streetDetailPairs.size() > 0) ? streetDetailPairs.toArray(new AddressComponent[0]) : null)
                .altid(vcardAddr.getAltId())
                .group(vcardAddr.getGroup())
                .language(vcardAddr.getLanguage())
                .defaultSeparator((ArrayUtils.isNotEmpty(jscomps) && jscomps[0].startsWith(DelimiterUtils.SEPARATOR_ID)) ? jscomps[0].replace(DelimiterUtils.SEPARATOR_ID + "\\",StringUtils.EMPTY).replace(DelimiterUtils.SEPARATOR_ID,StringUtils.EMPTY) : null)
                .isOrdered((jscomps!=null) ? Boolean.TRUE : null)
                .jsid(getJSIDParameter(vcardAddr))
                .phoneticSystem(phoneticSystem)
                .phoneticScript(vcardAddr.getParameter(VCardParamEnum.SCRIPT.getValue()))
                .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcardAddr, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.LABEL, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.CC, VCardParamEnum.TZ, VCardParamEnum.GEO, VCardParamEnum.DERIVED, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS, VCardParamEnum.PHONETIC, VCardParamEnum.SCRIPT))
                .build();
    }

    private Address getAddressPlusPhonetic(Address address, Address phoneticAddress) {

        address.setPhoneticSystem(phoneticAddress.getPhoneticSystem());
        address.setPhoneticScript(phoneticAddress.getPhoneticScript());

        for (int i = 0; i < address.getComponents().length; i++) {
            for (int j = 0; j < phoneticAddress.getComponents().length; j++) {

                if (address.getComponents()[i].getKind().equals(phoneticAddress.getComponents()[j].getKind()))
                    address.getComponents()[i].setPhonetic(phoneticAddress.getComponents()[j].getPhonetic());
            }
        }

        return address;
    }
    private void fillJSCardAddresses(VCard vcard, Card jsCard) {

        if (vcard.getProperties(ExtendedAddress.class) == null || vcard.getProperties(ExtendedAddress.class).isEmpty())
            return;

        List<it.cnr.iit.jscontact.tools.dto.Address> addresses = new ArrayList<>();
        String tz;
        String geo;
        for (ExtendedAddress addr : vcard.getProperties(ExtendedAddress.class))
            addresses.add(toJSCardAddress(addr));

        if (vcard.getTimezone() != null) {
            tz = getValue(vcard.getTimezone());
            it.cnr.iit.jscontact.tools.dto.Address address = findJSCardAddressByGroup(addresses, vcard.getTimezone().getGroup());
            if (address != null) {
                address.setTimeZone(tz);
            } else {
                jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                        .name(V_Extension.toV_Extension(VCardPropEnum.TZ.getValue().toLowerCase()))
                        .parameters(VCardUtils.getVCardUnconvertedPropParams(vcard.getTimezone().getParameters()))
                        .type(vcard.getTimezone().getParameters().getValue())
                        .value(tz)
                        .build());
            }
        }

        if (vcard.getGeo() != null) {
            geo = getValue(vcard.getGeo().getGeoUri());
            it.cnr.iit.jscontact.tools.dto.Address address = findJSCardAddressByGroup(addresses, vcard.getGeo().getGroup());
            if (address != null) {
                address.setCoordinates(geo);
            } else {
                jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                        .name(V_Extension.toV_Extension(VCardPropEnum.GEO.getValue().toLowerCase()))
                        .parameters(VCardUtils.getVCardUnconvertedPropParams(vcard.getGeo().getParameters()))
                        .type(vcard.getGeo().getParameters().getValue())
                        .value(geo)
                        .build());
            }
        }

        addresses.sort(JSCardAddressesComparator.builder().defaultLanguage(jsCard.getLanguage()).build()); //sort based on altid

        int i = 1;
        String lastAltid = null;
        String lastMapId = null;
        for (Address address : addresses) {

            if (address.getAltid() == null && jsCard.getLanguage() == null && address.getLanguage() != null)
                jsCard.setLanguage(address.getLanguage());

            if (address.getAltid() == null || !address.getAltid().equals(lastAltid)) {
                String id = getJSCardId(JSContactProfileIds.IdType.ADDRESS, i, "ADR-" + (i++), address.getJsid() );
                addVCardUnconvertedParams(jsCard,"addresses/"+id,"adr", address.getVCardUnconvertedParams());
                address.setVCardUnconvertedParams(null);
                jsCard.addAddress(id, address);
                lastAltid = address.getAltid();
                lastMapId = id;
            } else {
                if (address.getPhoneticSystem()!= null || address.getPhoneticScript() != null) {

                    if (address.getLanguage()==null) {
                        jsCard.getAddresses().replace(lastMapId, getAddressPlusPhonetic(jsCard.getAddresses().get(lastMapId), address));
                    } else {
                        if (address.getPhoneticSystem() != null)
                            jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId + "/phoneticSystem", JsonNodeUtils.textNode(address.getPhoneticSystem().toJson()));
                        if (address.getPhoneticScript() != null)
                            jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId + "/phoneticScript", JsonNodeUtils.textNode(address.getPhoneticScript()));

                        if (address.getPhoneticSystem()!=null)
                            addVCardUnconvertedParams(jsCard,String.format("localizations/%s/addresses~1%s~1phoneticSystem", address.getLanguage(), lastMapId), "adr", address.getVCardUnconvertedParams());
                        else {
                            if (address.getPhoneticScript()!=null) {
                                addVCardUnconvertedParams(jsCard,String.format("localizations/%s/addresses~1%s~1phoneticScript", address.getLanguage(), lastMapId), "adr", address.getVCardUnconvertedParams());
                            }
                        }

                        if (address.getComponents() != null) {
                            for (AddressComponent component : address.getComponents()) {
                                if (component.getValue().equals(component.getPhonetic()))
                                    jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId + "/components/" + i + "/phonetic", JsonNodeUtils.textNode(component.getPhonetic()));
                            }
                        }
                    }
                }
                else {
                    addVCardUnconvertedParams(jsCard, String.format("localizations/%s/addresses~1%s", address.getLanguage(), lastMapId), "adr", address.getVCardUnconvertedParams());
                    address.setVCardUnconvertedParams(null);
                    jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId, mapper.convertValue(address, JsonNode.class));
                }
            }
        }
    }

    private static  <T extends DateOrTimeProperty> AnniversaryDate toJSCardAnniversaryDate(T date) {

        try {
            if (date.getDate() != null) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(date.getDate());
                if (!DateUtils.hasTime(calendar)) { // date doesn't have time
                    PartialDate pd = PartialDate.builder().year(calendar.get(java.util.Calendar.YEAR))
                            .month(calendar.get(java.util.Calendar.MONTH)+1)
                            .day(calendar.get(java.util.Calendar.DAY_OF_MONTH))
                            .calendarScale((date.getCalscale()!=null) ? date.getCalscale().getValue() : null)
                            .build();
                    return AnniversaryDate.builder().partialDate(pd).build();
                }
                else
                    return AnniversaryDate.builder().date(Timestamp.builder().utc(date.getCalendar()).build()).build();
            }
            if (date.getPartialDate() != null) {
                    PartialDate pd = PartialDate.builder().year(date.getPartialDate().getYear())
                                                          .month(date.getPartialDate().getMonth())
                                                          .day(date.getPartialDate().getDate())
                                                          .calendarScale((date.getCalscale()!=null) ? date.getCalscale().getValue() : null)
                                                          .build();
                return AnniversaryDate.builder().partialDate(pd).build();
            }
            if (date.getText() != null)
                return null;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

        return null;
    }

    private void fillJSCardAnniversaries(VCard vcard, Card jsCard) {

        int i = 1;
      if (vcard.getBirthday() != null) {
          it.cnr.iit.jscontact.tools.dto.Anniversary anniversary = it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                  .kind(AnniversaryKind.birth())
                  .date(toJSCardAnniversaryDate(vcard.getBirthday()))
                  .place(getValue(vcard.getBirthplace()))
                  .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(vcard.getBirthday().getClass().getName()))
                  .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcard.getBirthday(), VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.CALSCALE))
                  .build();
          String id = getJSCardId(JSContactProfileIds.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i++), getJSIDParameter(vcard.getBirthday()));
          jsCard.addAnniversary(id, anniversary);
          addVCardUnconvertedParams(jsCard,String.format("anniversaries/%s/place",id),"birthplace", VCardUtils.getVCardParamsOtherThan(vcard.getBirthplace(), VCardParamEnum.JSID, VCardParamEnum.PROP_ID));
      }

      if (vcard.getDeathdate() != null) {

          it.cnr.iit.jscontact.tools.dto.Anniversary anniversary = it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                  .kind(AnniversaryKind.death())
                  .date(toJSCardAnniversaryDate(vcard.getDeathdate()))
                  .place(getValue(vcard.getDeathplace()))
                  .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(vcard.getDeathdate().getClass().getName()))
                  .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcard.getDeathdate(), VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.CALSCALE))
                  .build();
          String id = getJSCardId(JSContactProfileIds.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i++), getJSIDParameter(vcard.getDeathdate()));
          jsCard.addAnniversary(id, anniversary);
          addVCardUnconvertedParams(jsCard,String.format("anniversaries/%s/place",id),"deathplace", VCardUtils.getVCardParamsOtherThan(vcard.getDeathplace(), VCardParamEnum.JSID, VCardParamEnum.PROP_ID));
      }

      if (vcard.getAnniversary() != null) {
          it.cnr.iit.jscontact.tools.dto.Anniversary anniversary = it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                  .kind(AnniversaryKind.wedding())
                  .date(toJSCardAnniversaryDate(vcard.getAnniversary()))
                  .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(vcard.getAnniversary().getClass().getName()))
                  .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(vcard.getAnniversary(), VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.CALSCALE))
                  .build();
          jsCard.addAnniversary(getJSCardId(JSContactProfileIds.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i++), getJSIDParameter(vcard.getAnniversary())), anniversary);
      }
    }

    private static PersonalInfoLevelType toJSCardLevel(String vcardLevelParam) throws CardException {

        try {
            return PersonalInfoLevelType.builder().rfcValue(PersonalInfoLevelEnum.getEnum(vcardLevelParam)).build();
        } catch (IllegalArgumentException e) {
            throw new CardException("Unknown LEVEL value " + vcardLevelParam);
        }
    }

    private void fillJSCardPersonalInfos(VCard vcard, Card jsCard) throws CardException {

        int j = 0;
        int i = 1;
        for (Hobby hobby : vcard.getHobbies()) {
            jsCard.addPersonalInfo(getJSCardId(JSContactProfileIds.IdType.PERSONAL_INFO, j++, "HOBBY-" + (i++), getJSIDParameter(hobby), PersonalInfoEnum.HOBBY),
                    PersonalInfo.builder()
                            .kind(PersonalInfoKind.builder().rfcValue(PersonalInfoEnum.HOBBY).build())
                            .value(getValue(hobby))
                            .level((hobby.getLevel() != null) ? toJSCardLevel(hobby.getLevel().getValue()) : null)
                            .listAs(hobby.getIndex())
                            .label(toJSCardLabel(hobby, vcard.getExtendedProperties()))
                            .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(hobby.getClass().getName()))
                            .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(hobby, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LEVEL, VCardParamEnum.INDEX))
                            .build()
            );
        }

        i = 1;
        for (Interest interest : vcard.getInterests()) {
            jsCard.addPersonalInfo(getJSCardId(JSContactProfileIds.IdType.PERSONAL_INFO, j++, "INTEREST-" + (i++), getJSIDParameter(interest), PersonalInfoEnum.INTEREST),
                    PersonalInfo.builder()
                            .kind(PersonalInfoKind.builder().rfcValue(PersonalInfoEnum.INTEREST).build())
                            .value(getValue(interest))
                            .level((interest.getLevel() != null) ? toJSCardLevel(interest.getLevel().getValue()) : null)
                            .listAs(interest.getIndex())
                            .label(toJSCardLabel(interest, vcard.getExtendedProperties()))
                            .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(interest.getClass().getName()))
                            .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(interest, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LEVEL, VCardParamEnum.INDEX))
                            .build()
            );
        }

        i = 1;
        for (Expertise expertise : vcard.getExpertise()) {
            jsCard.addPersonalInfo(getJSCardId(JSContactProfileIds.IdType.PERSONAL_INFO, j++, "EXPERTISE-" + (i++), getJSIDParameter(expertise), PersonalInfoEnum.INTEREST),
                    PersonalInfo.builder()
                            .jsid(getJSIDParameter(expertise))
                            .kind(PersonalInfoKind.builder().rfcValue(PersonalInfoEnum.EXPERTISE).build())
                            .value(getValue(expertise))
                            .level((expertise.getLevel() != null) ? toJSCardLevel(expertise.getLevel().getValue()) : null)
                            .listAs(expertise.getIndex())
                            .label(toJSCardLabel(expertise, vcard.getExtendedProperties()))
                            .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(expertise.getClass().getName()))
                            .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(expertise, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LEVEL, VCardParamEnum.INDEX))
                            .build()
            );
        }

    }

    private void fillJSCardPreferredLanguages(VCard vcard, Card jsCard) {

        int i = 1;
        for (Language lang : vcard.getLanguages()) {
            String vcardType = VCardUtils.getVCardParamValue(lang.getParameters(), VCardParamEnum.TYPE);
            jsCard.addLanguagePref(getJSCardId(JSContactProfileIds.IdType.LANGUAGE, i,"LANG-" + (i++), getJSIDParameter(lang)),
                    LanguagePref.builder()
                            .contexts(toJSCardContexts(vcardType))
                            .pref(lang.getPref())
                            .language(getValue(lang))
                            .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(lang.getClass().getName()))
                            .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(lang, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF))
                                                       .build()
                                        );

        }
    }


    private static String getResourceExt(String resource) {

        int index = resource.lastIndexOf('.');
        return (index > 0) ? resource.substring(index + 1) : null;
    }

    private String toJSCardMediaType(String mediaTypeParamValue, String resource) {

        if (mediaTypeParamValue != null)
            return mediaTypeParamValue;

        if (config.isSetAutoMediaType()) {
            String ext = getResourceExt(resource);
            return (ext != null) ? MimeTypeUtils.lookupMimeType(ext) : null;
        }

        return null;
    }

    private Map<PhoneFeature,Integer> initIndexesPerPhoneFeaturesMap() {
        return new HashMap<>() {{
            put (PhoneFeature.voice(), Integer.valueOf(1));
            put(PhoneFeature.fax(), Integer.valueOf(1));
            put(PhoneFeature.mobile(), Integer.valueOf(1));
        }};
    }

    private static PhoneFeature getMainPhoneFeature(Map<PhoneFeature,Boolean> phoneFeatures) {
        if (phoneFeatures == null || phoneFeatures.isEmpty())
            return PhoneFeature.voice();
        if (phoneFeatures.containsKey(PhoneFeature.voice()))
            return PhoneFeature.voice();
        if (phoneFeatures.containsKey(PhoneFeature.fax()))
            return PhoneFeature.fax();
        if (phoneFeatures.containsKey(PhoneFeature.mobile()))
            return PhoneFeature.mobile();
        return null;
    }

    private void fillJSCardPhones(VCard vcard, Card jsCard) {

        int i = 1;
        Map<PhoneFeature,Integer> indexes = initIndexesPerPhoneFeaturesMap();
        for (Telephone tel : vcard.getTelephoneNumbers()) {
            String vcardType = VCardUtils.getVCardParamValue(tel.getParameters(), VCardParamEnum.TYPE);
            Map<Context,Boolean> contexts = toJSCardContexts(vcardType);
            Map<PhoneFeature,Boolean> phoneFeatures = toJSCardPhoneFeatures(vcardType);
            PhoneFeature mainPhoneFeature = getMainPhoneFeature(phoneFeatures);
            jsCard.addPhone(getJSCardId(JSContactProfileIds.IdType.PHONE, (mainPhoneFeature == null || !isProfielIdsToUsePhoneFeatureSpecific()) ? i : indexes.get(mainPhoneFeature),"PHONE-" + (i++), getJSIDParameter(tel), (mainPhoneFeature != null || isProfielIdsToUsePhoneFeatureSpecific()) ? mainPhoneFeature.getRfcValue() : null), Phone.builder()
                            .number(getValue(tel))
                            .features(phoneFeatures)
                            .contexts(contexts)
                            .pref(tel.getPref())
                            .label(toJSCardLabel(tel, vcard.getExtendedProperties()))
                            .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(tel.getClass().getName()))
                            .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(tel, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF))
                                       .build()
                              );
            if (mainPhoneFeature != null && isProfielIdsToUsePhoneFeatureSpecific()) indexes.replace(mainPhoneFeature,indexes.get(mainPhoneFeature)+1);
        }

    }

    private void fillJSCardEmails(VCard vcard, Card jsCard) {

        int i = 1;
        for (Email email : vcard.getEmails()) {
            String emailAddress = getValue(email);
            if (StringUtils.isNotEmpty(emailAddress)) {
                String vcardType = VCardUtils.getVCardParamValue(email.getParameters(), VCardParamEnum.TYPE);
                jsCard.addEmailAddress(getJSCardId(JSContactProfileIds.IdType.EMAIL, i, "EMAIL-" + (i++), getJSIDParameter(email)), EmailAddress.builder()
                        .address(emailAddress)
                        .contexts(toJSCardContexts(vcardType))
                        .pref(email.getPref())
                        .label(toJSCardLabel(email, vcard.getExtendedProperties()))
                        .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(email.getClass().getName()))
                        .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(email, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF))
                        .build()
                );
            }
        }
    }

    private void fillJSCardOnlineServices(VCard vcard, Card jsCard) {

        String vcardType;
        Map<Context,Boolean> contexts;

        int i = 1;
        for (Impp impp : vcard.getImpps()) {
            vcardType = VCardUtils.getVCardParamValue(impp.getParameters(), VCardParamEnum.TYPE);
            contexts = toJSCardContexts(vcardType);
            String onlineServiceId = getJSCardId(JSContactProfileIds.IdType.ONLINE_SERVICE, i,"OS-" + (i++), getJSIDParameter(impp));
            jsCard.addOnlineService(onlineServiceId, OnlineService.builder()
                    .uri(getValue(impp))
                    .contexts(contexts)
                    .pref(impp.getPref())
                    .user(VCardUtils.getVCardParamValue(impp.getParameters(), VCardParamEnum.USERNAME))
                    .service(VCardUtils.getVCardParamValue(impp.getParameters(), VCardParamEnum.SERVICE_TYPE))
                    .label(toJSCardLabel(impp, vcard.getExtendedProperties()))
                    .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(impp.getClass().getName()))
                    .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(impp, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.SERVICE_TYPE, VCardParamEnum.USERNAME))
                    .build()
            );
            jsCard.addVCardConvertedProp("onlineServices/"+onlineServiceId, it.cnr.iit.jscontact.tools.dto.VCardProperty.builder()
                                                                                         .name("impp")
                                                                                         .build());
        }

    }

    private void fillJSCardMedia(VCard vcard, Card jsCard) {

        int i = 1;
        for (Photo photo : vcard.getPhotos())
            addJSCardMediaResource(photo, jsCard, MediaKind.photo(), i++, vcard.getExtendedProperties());
        i = 1;
        for (Sound sound : vcard.getSounds())
            addJSCardMediaResource(sound, jsCard, MediaKind.sound(), i++, vcard.getExtendedProperties());

        i = 1;
        for (Logo logo : vcard.getLogos())
            addJSCardMediaResource(logo, jsCard, MediaKind.logo(), i++, vcard.getExtendedProperties());

    }

    private void fillJSCardCryptoKeys(VCard vcard, Card jsCard) {

        int i = 1;
        for (Key key : vcard.getKeys())
            addJSCardCryptoResource(key, jsCard, i++, vcard.getExtendedProperties());

    }

    private void fillJSCardDirectories(VCard vcard, Card jsCard) {

        int i = 1;
        for (Source source : vcard.getSources())
            addJSCardDirectoryResource(source, jsCard, DirectoryKind.entry(), i++, vcard.getExtendedProperties());

        i = 1;
        for (OrgDirectory od : vcard.getOrgDirectories())
            addJSCardDirectoryResource(jsCard, toJSCardDirectoryResource(od, DirectoryKind.directory(), vcard.getExtendedProperties()), i++);
    }

    private void fillJSCardCalendars(VCard vcard, Card jsCard) {

        int i = 1;
        for (CalendarUri calendarUri : vcard.getCalendarUris())
            addJSCardCalendarResource(calendarUri, jsCard, CalendarKind.calendar(), i++, vcard.getExtendedProperties());

        i = 1;
        for (FreeBusyUrl fburl : vcard.getFbUrls())
            addJSCardCalendarResource(fburl, jsCard, CalendarKind.freeBusy(), i++, vcard.getExtendedProperties());

    }


    private void fillJSCardLinks(VCard vcard, Card jsCard) {

        int i = 1;
        for (Url url : vcard.getUrls())
            addJSCardLinkResource(url, jsCard, null, i++, vcard.getExtendedProperties());

        List<RawProperty> contactUris = VCardUtils.getRawProperties(vcard, "CONTACT-URI");
        i = 1;
        for (RawProperty contactUri : contactUris) {
            UriProperty uriProperty = new UriProperty(getValue(contactUri));
            uriProperty.setParameters(contactUri.getParameters());
            addJSCardLinkResource(uriProperty, jsCard, LinkKind.contact(), i++, vcard.getExtendedProperties());
        }

    }

    private void fillJSCardSchedulingAddresses(VCard vcard, Card jsCard) {

        int i = 1;
        for (CalendarRequestUri calendarRequestUri : vcard.getCalendarRequestUris()) {
            String vcardType = VCardUtils.getVCardParamValue(calendarRequestUri.getParameters(), VCardParamEnum.TYPE);
            Map<Context,Boolean> contexts = toJSCardContexts(vcardType);
            jsCard.addSchedulingAddress(getJSCardId(JSContactProfileIds.IdType.SCHEDULING, i, "SCHEDULING-" + (i++), getJSIDParameter(calendarRequestUri)),
                    SchedulingAddress.builder()
                            .jsid(getJSIDParameter(calendarRequestUri))
                            .uri(calendarRequestUri.getValue())
                            .contexts(contexts)
                            .pref(calendarRequestUri.getPref())
                            .label(toJSCardLabel(calendarRequestUri, vcard.getExtendedProperties()))
                            .vCardPropNameOfUnconvertedParams(getVCardPropertyNameFromClassName(calendarRequestUri.getClass().getName()))
                            .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(calendarRequestUri, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF))
                             .build());
        }
    }


    private static String findJSCardOrganizationIdByGroup(Map<String,it.cnr.iit.jscontact.tools.dto.Organization> jscardOrganizations, String vcardTitleGroup) {

        if (vcardTitleGroup == null)
            return null;

        if (jscardOrganizations == null || jscardOrganizations.isEmpty())
            return null;

        for (Map.Entry<String,it.cnr.iit.jscontact.tools.dto.Organization> entry : jscardOrganizations.entrySet()) {
            if (entry.getValue().getGroup()!=null && entry.getValue().getGroup().equals(vcardTitleGroup))
                return entry.getKey();
        }

        return null;
    }

    private it.cnr.iit.jscontact.tools.dto.Title toJSCardTitle(Title vcardTitle, VCard vcard, Card jsCard) {

        return it.cnr.iit.jscontact.tools.dto.Title.builder()
                .name(vcardTitle.getValue())
                .kind(TitleKind.title())
                .organizationId(findJSCardOrganizationIdByGroup(jsCard.getOrganizations(), vcardTitle.getGroup()))
                .build();
    }

    private it.cnr.iit.jscontact.tools.dto.Title toJSCardTitle(Role vcardRole, VCard vcard, Card jsCard) {

        return it.cnr.iit.jscontact.tools.dto.Title.builder()
                .name(vcardRole.getValue())
                .kind(TitleKind.role())
                .organizationId(findJSCardOrganizationIdByGroup(jsCard.getOrganizations(), vcardRole.getGroup()))
                .build();
    }


    private void fillJSCardTitlesfromVCardTitles(VCard vcard, Card jsCard) {

        if (vcard.getTitles() == null || vcard.getTitles().isEmpty())
            return;

        List<ezvcard.property.Title> titles = vcard.getTitles();
        titles.sort(vCardPropertiesAltidComparator);
        int i = 1;
        String lastAltid = null;
        String lastMapId = null;
        for (Title vcardTitle : titles) {
            if (vcardTitle.getAltId() == null || lastAltid == null || !vcardTitle.getAltId().equals(lastAltid)) {
                String jsid = getJSIDParameter(vcardTitle);
                String id = getJSCardId(JSContactProfileIds.IdType.TITLE, i, "TITLE-" + (i ++), jsid);
                jsCard.addTitle(id, toJSCardTitle(vcardTitle, vcard, jsCard));
                lastAltid = vcardTitle.getAltId();
                lastMapId = id;
                addVCardUnconvertedParams(jsCard, "titles/"+ lastMapId,"title", VCardUtils.getVCardParamsOtherThan(vcardTitle, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.PREF, VCardParamEnum.ALTID));
            } else {
                jsCard.addLocalization(vcardTitle.getLanguage(), "titles/" + lastMapId, mapper.convertValue(toJSCardTitle(vcardTitle, vcard, jsCard), JsonNode.class));
                addVCardUnconvertedParams(jsCard, String.format("localizations/%s/titles~1%s", vcardTitle.getLanguage(), lastMapId),"title", VCardUtils.getVCardParamsOtherThan(vcardTitle, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.PREF, VCardParamEnum.ALTID));
            }
        }
    }

    private void fillJSCardTitlesfromVCardRoles(VCard vcard, Card jsCard) {

        if (vcard.getRoles() == null || vcard.getRoles().isEmpty())
            return;

        List<ezvcard.property.Role> roles = vcard.getRoles();
        roles.sort(vCardPropertiesAltidComparator);
        int i = (jsCard.getTitles() != null) ? jsCard.getTitles().size() + 1 : 1;
        String lastAltid = null;
        String lastMapId = null;
        for (Role vcardRole : roles) {
            if (vcardRole.getAltId() == null || lastAltid == null || !vcardRole.getAltId().equals(lastAltid)) {
                String jsid = getJSIDParameter(vcardRole);
                String id = getJSCardId(JSContactProfileIds.IdType.TITLE, i, "TITLE-" + (i ++), jsid);
                jsCard.addTitle(id, toJSCardTitle(vcardRole, vcard, jsCard));
                lastAltid = vcardRole.getAltId();
                lastMapId = id;
                addVCardUnconvertedParams(jsCard, "titles/"+ lastMapId,"role", VCardUtils.getVCardParamsOtherThan(vcardRole, VCardParamEnum.JSID, VCardParamEnum.LANGUAGE, VCardParamEnum.PREF, VCardParamEnum.ALTID));
            } else {
                jsCard.addLocalization(vcardRole.getLanguage(), "titles/" + lastMapId, mapper.convertValue(toJSCardTitle(vcardRole, vcard, jsCard), JsonNode.class));
                addVCardUnconvertedParams(jsCard, String.format("localizations/%s/titles~1%s", vcardRole.getLanguage(), lastMapId),"role", VCardUtils.getVCardParamsOtherThan(vcardRole, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.PREF, VCardParamEnum.ALTID));
            }
        }
    }

    private it.cnr.iit.jscontact.tools.dto.Organization toJSCardOrganization(Organization vcardOrg, VCard vcard) {

        List<String> unitNameList;
        List<String> unitSortAsList = null;
        String name;
        String orgSortAs = null;
        if (vcardOrg.getValues().size() > 1 ) {
            name = vcardOrg.getValues().get(0);
            unitNameList = vcardOrg.getValues().subList(1,vcardOrg.getValues().size());
            if (vcardOrg.getSortAs()!=null && !vcardOrg.getSortAs().isEmpty()) {
                orgSortAs = vcardOrg.getSortAs().get(0);
                unitSortAsList = vcardOrg.getSortAs().subList(1,vcardOrg.getSortAs().size());
            }
        } else { // ezvcard put all the organization components separated by semicolon into vcardOrg.getValues().get(0) !!
            String[] nameSubItems = vcardOrg.getValues().get(0).split(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER);
            name =   nameSubItems[0];
            unitNameList =  Arrays.asList(nameSubItems).subList(1,nameSubItems.length);
            if (vcardOrg.getSortAs()!=null && !vcardOrg.getSortAs().isEmpty()) {
                String[] sortAsSubItems = vcardOrg.getSortAs().get(0).split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                orgSortAs = sortAsSubItems[0];
                unitSortAsList = Arrays.asList(sortAsSubItems).subList(1,sortAsSubItems.length);
            }
        }

        OrgUnit[] orgUnits = null;
        if (unitNameList!=null && unitNameList.size()>0) {
            orgUnits = new OrgUnit[unitNameList.size()];
            for (int i=0; i < unitNameList.size(); i++) {
                String sortAs = (unitSortAsList!=null && unitSortAsList.size()>0) ? unitSortAsList.get(i) : null;
                orgUnits[i] = OrgUnit.builder().name(unitNameList.get(i)).sortAs(sortAs).build();
            }

        }

        return it.cnr.iit.jscontact.tools.dto.Organization.builder()
                .name((!name.isEmpty()) ? name : null)
                .units(orgUnits)
                .contexts(toJSCardContexts(vcardOrg.getType()))
                .sortAs(orgSortAs)
                .group(vcardOrg.getGroup())
                .build();
    }


    private void fillJSCardOrganizations(VCard vcard, Card jsCard) {

        if (vcard.getOrganizations() == null || vcard.getOrganizations().isEmpty())
            return;

        List<ezvcard.property.Organization> vcardOrgs = vcard.getOrganizations();
        vcardOrgs.sort(vCardPropertiesAltidComparator);
        int i = 1;
        String lastAltid = null;
        String lastMapId = null;
        for (ezvcard.property.Organization vcardOrg : vcardOrgs) {
            if (vcardOrg.getAltId() == null || lastAltid == null || !vcardOrg.getAltId().equals(lastAltid)) {
                String jsid = getJSIDParameter(vcardOrg);
                String id = getJSCardId(JSContactProfileIds.IdType.ORGANIZATION, i, "ORG-" + (i++), jsid);
                jsCard.addOrganization(id, toJSCardOrganization(vcardOrg, vcard));
                lastAltid = vcardOrg.getAltId();
                lastMapId = id;
                addVCardUnconvertedParams(jsCard,"organizations/"+id,"org", VCardUtils.getVCardParamsOtherThan(vcardOrg, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID));
            } else {
                jsCard.addLocalization(vcardOrg.getLanguage(), "organizations/" + lastMapId, mapper.convertValue(toJSCardOrganization(vcardOrg, vcard), JsonNode.class));
                addVCardUnconvertedParams(jsCard, String.format("localizations/%s/organizations~1%s", vcardOrg.getLanguage(), lastMapId),"org", VCardUtils.getVCardParamsOtherThan(vcardOrg, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID));
            }
        }
    }

    private Note toJSCardNote(ezvcard.property.Note vcardNote, VCard vcard) {

        String authorUri = vcardNote.getParameter(VCardParamEnum.AUTHOR.getValue());
        String authorName = vcardNote.getParameter(VCardParamEnum.AUTHOR_NAME.getValue());
        String created = vcardNote.getParameter(VCardParamEnum.CREATED.getValue());

        return Note.builder()
                .note(vcardNote.getValue())
                .author((authorName != null || authorUri != null) ? Author.builder().name(authorName).uri(authorUri).build() : null)
                .created((created != null) ? DateUtils.toCalendar(created) : null)
                .build();
    }

    private void fillJSCardNotes(VCard vcard, Card jsCard) {

        if (vcard.getNotes() == null || vcard.getNotes().isEmpty())
            return;

        List<ezvcard.property.Note> vcardNotes = vcard.getNotes();
        vcardNotes.sort(vCardPropertiesAltidComparator);
        int i = 1;
        String lastAltid = null;
        String lastMapId = null;
        for (ezvcard.property.Note vcardNote : vcardNotes) {
            if (vcardNote.getAltId() == null || lastAltid == null || !vcardNote.getAltId().equals(lastAltid)) {
                String jsid = getJSIDParameter(vcardNote);
                String id = getJSCardId(JSContactProfileIds.IdType.NOTE, i, "NOTE-" + (i++), jsid);
                jsCard.addNote(id, toJSCardNote(vcardNote, vcard));
                lastAltid = vcardNote.getAltId();
                lastMapId = id;
                addVCardUnconvertedParams(jsCard, "notes/"+ lastMapId,"note", VCardUtils.getVCardParamsOtherThan(vcardNote, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.ALTID, VCardParamEnum.CREATED, VCardParamEnum.AUTHOR, VCardParamEnum.AUTHOR_NAME));
            } else {
                jsCard.addLocalization(vcardNote.getLanguage(), "notes/" + lastMapId, mapper.convertValue(toJSCardNote(vcardNote, vcard), JsonNode.class));
                addVCardUnconvertedParams(jsCard, String.format("localizations/%s/notes~1%s", vcardNote.getLanguage(), lastMapId),"note", VCardUtils.getVCardParamsOtherThan(vcardNote, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.ALTID, VCardParamEnum.CREATED, VCardParamEnum.AUTHOR, VCardParamEnum.AUTHOR_NAME));
            }
        }
    }

    private void fillJSCardKeywords(VCard vcard, Card jsCard) {

        List<Categories> categoriesList = vcard.getCategoriesList();
        categoriesList.sort(vCardPropertiesPrefComparator);
        for (Categories categories : categoriesList)
            jsCard.addKeywords(categories.getValues().toArray(new String[0]));
    }

    private static void fillJSCardRelations(VCard vcard, Card jsCard) {

        for (Related related : vcard.getRelations()) {
            if (related.getTypes().isEmpty())
                jsCard.addRelation(getValue(related), null);
            else {
                for (RelatedType type : related.getTypes())
                    jsCard.addRelation(getValue(related), RelationType.rfcRelation(RelationEnum.getEnum(type.getValue())));
            }
            addVCardUnconvertedParams(jsCard,"relatedTo/" + getValue(related),"related", VCardUtils.getVCardParamsOtherThan(related, VCardParamEnum.TYPE));
        }
    }

    //Fill JSContact properties mapping the VCard properties defined in RFC9554
    private void fillJSCardPropsFromVCardJSContactExtensions(VCard vcard, Card jsCard) {

        int i = 1;
        String lastAltid = null;
        String lastMapid = null;
        for (RawProperty extension : vcard.getExtendedProperties()) {

            String language = extension.getParameter(VCardParamEnum.LANGUAGE.getValue());
            String vcardPref = extension.getParameter(VCardParamEnum.PREF.getValue());
            Integer pref;
            try {
                pref = Integer.parseInt(vcardPref);
            } catch (Exception e) {
                pref = null;
            }
            String vcardType = extension.getParameter(VCardParamEnum.TYPE.getValue());
            Map<Context,Boolean> contexts = toJSCardContexts(vcardType);
            String jsonPointer = fakeExtensionsMapping.get(extension.getPropertyName().toLowerCase());

            if (jsonPointer == null)
                continue; //vcard extension already handled elsewhere

            if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.LANGUAGE.getValue()))
                // property has already been set
                addVCardUnconvertedParams(jsCard,"language","language", VCardUtils.getVCardParamsOtherThan(extension));
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.CREATED.getValue())) {
                jsCard.setCreated(DateUtils.toCalendar(extension.getValue()));
                addVCardUnconvertedParams(jsCard,"created","created", VCardUtils.getVCardParamsOtherThan(extension));
            }
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.GRAMGENDER.getValue())) {
                GrammaticalGenderType gender = GrammaticalGenderType.getEnum(extension.getValue().toLowerCase());
                addVCardUnconvertedParams(jsCard,"speakToAs/grammaticalGender","gramgender", VCardUtils.getVCardParamsOtherThan(extension, VCardParamEnum.LANGUAGE));
                if (language == null ||
                        (jsCard.getLanguage() != null && jsCard.getLanguage().equalsIgnoreCase(language)) ||
                        (config.getDefaultLanguage() !=null && config.getDefaultLanguage().equalsIgnoreCase(language))
               ) {
                    if (jsCard.getSpeakToAs() != null)
                        jsCard.getSpeakToAs().setGrammaticalGender(gender);
                    else
                        jsCard.setSpeakToAs(SpeakToAs.builder().grammaticalGender(gender).build());
                } else {
                    jsCard.addLocalization(language, jsonPointer, JsonNodeUtils.textNode(gender.getValue()));
                    addVCardUnconvertedParams(jsCard,String.format("localizations/%s/speakToAs~1grammaticalGender", language),"gramgender", VCardUtils.getVCardParamsOtherThan(extension, VCardParamEnum.LANGUAGE));
                }
            }
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.PRONOUNS.getValue())) {
                Pronouns pronouns = Pronouns.builder()
                        .pronouns(extension.getValue())
                        .contexts(contexts)
                        .pref(pref)
                        .vCardPropNameOfUnconvertedParams(VCardPropEnum.PRONOUNS.getValue().toLowerCase())
                        .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(extension, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.ALTID, VCardParamEnum.LANGUAGE))
                        .build();

                String altid = extension.getParameter(VCardParamEnum.ALTID.getValue());
                if (altid == null || lastAltid == null || !altid.equals(lastAltid)) {
                    String jsid = getJSIDParameter(extension);
                    String id = getJSCardId(JSContactProfileIds.IdType.PRONOUNS, i, "PRONOUNS-" + (i++), jsid);
                    lastAltid = altid;
                    lastMapid = (jsid!=null) ? jsid : id;
                    if (language == null ||
                            (jsCard.getLanguage() != null && jsCard.getLanguage().equalsIgnoreCase(language)) ||
                            (config.getDefaultLanguage() != null && config.getDefaultLanguage().equalsIgnoreCase(language))
                    ) {
                        if (jsCard.getSpeakToAs() != null) {
                            jsCard.getSpeakToAs().addPronouns(id, pronouns);
                        } else {
                            jsCard.setSpeakToAs(SpeakToAs.builder().pronouns(new HashMap<String, Pronouns>() {{
                                put(id, pronouns);
                            }}).build());
                        }
                    }
                } else {
                    jsonPointer = String.format("%s/%s", jsonPointer, lastMapid);
                    jsCard.addLocalization(language, jsonPointer, mapper.convertValue(pronouns, JsonNode.class));
                    addVCardUnconvertedParams(jsCard,String.format("localizations/%s/speakToAs~1pronouns~1%s", language, lastMapid),"pronouns", VCardUtils.getVCardParamsOtherThan(extension, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.ALTID, VCardParamEnum.LANGUAGE));
                }
            }
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.SOCIALPROFILE.getValue())) {
                i = (jsCard.getOnlineServices() != null) ? jsCard.getOnlineServices().size() + 1 : 1;
                vcardType = VCardUtils.getVCardParamValue(extension.getParameters(), VCardParamEnum.TYPE);
                contexts = toJSCardContexts(vcardType);
                OnlineService ol = OnlineService.builder()
                        .user((extension.getDataType() == VCardDataType.TEXT) ? extension.getValue() : null)
                        .uri((extension.getDataType() == null || extension.getDataType() == VCardDataType.URI) ? extension.getValue() : null)
                        .service(extension.getParameter(VCardParamEnum.SERVICE_TYPE.getValue()))
                        .contexts(contexts)
                        .pref(toJSCardPref(extension.getParameter(VCardParamEnum.PREF.getValue())))
                        .label(toJSCardLabel(extension, vcard.getExtendedProperties()))
                        .vCardPropNameOfUnconvertedParams(VCardPropEnum.SOCIALPROFILE.getValue().toLowerCase())
                        .vCardUnconvertedParams(VCardUtils.getVCardParamsOtherThan(extension, VCardParamEnum.JSID, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.SERVICE_TYPE, VCardParamEnum.USERNAME))
                        .build();

                if ((extension.getDataType() == null || extension.getDataType() == VCardDataType.URI) && extension.getParameter(VCardParamEnum.USERNAME.getValue()) != null)
                    ol.setUser(extension.getParameter(VCardParamEnum.USERNAME.getValue()));

                String onlineServiceId = getJSCardId(JSContactProfileIds.IdType.ONLINE_SERVICE, i,"OS-" + (i++), getJSIDParameter(extension));
                jsCard.addOnlineService(onlineServiceId, ol);
            }
        }
    }


    private void fillJSCardPropsFromVCardExtensions(VCard vcard, Card jsCard) {

        for (RawProperty extension : vcard.getExtendedProperties()) {
            if (!fakeExtensionsMapping.containsKey(extension.getPropertyName()) &&
                    !fakeExtensionsMapping.containsKey(extension.getPropertyName().toLowerCase())) {
                Map<String,Object> parameters = new HashMap<>();
                if (extension.getGroup()!=null)
                    parameters.put(VCardParamEnum.GROUP.toString().toLowerCase(),extension.getGroup());
                parameters.putAll(VCardUtils.getVCardUnconvertedPropParams(extension.getParameters()));
                jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                                             .name(V_Extension.toV_Extension(extension.getPropertyName().toLowerCase()))
                                             .parameters(parameters)
                                             .type(extension.getDataType())
                                             .value(extension.getValue())
                                             .build());
            }
        }
    }

    private void fillJSPropsFromJSCardExtensionsInVCard(VCard vcard, Card jsCard) throws CardException {

        String path = null;
        Object value;
        String extensionName;
        try {
            for (RawProperty extension : vcard.getExtendedProperties()) {
                if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.JSPROP.getValue())) {
                    path = extension.getParameter(VCardParamEnum.JSPTR.getValue());
                    value = JSPropUtils.toJSContactObject(extension.getValue());
                    if (!path.contains(DelimiterUtils.SLASH_DELIMITER)) {
                        jsCard.addExtension(path, value);
                    } else {
                        String[] pathItems = path.split(DelimiterUtils.SLASH_DELIMITER);
                        extensionName = pathItems[pathItems.length-1];
                        List<String> list = Arrays.asList(pathItems);
                        jsCard.addExtension(list.subList(0, pathItems.length-1),extensionName.replaceAll(DelimiterUtils.SLASH_DELIMITER_IN_JSON_POINTER,DelimiterUtils.SLASH_DELIMITER), value);
                    }
                }
            }
        } catch(Exception e) {
            throw new CardException(String.format("Unable to convert JSPROP property with path: %s", path));
        }
    }


    private static void fillVCardUnconvertedProps(VCard vcard, Card jsCard) {

        jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                .name(V_Extension.toV_Extension(VCardPropEnum.VERSION.getValue().toLowerCase()))
                .type(VCardDataType.TEXT)
                .value(vcard.getVersion().getVersion())
                .build());

        if (vcard.getClientPidMaps()!=null) {
            for (ClientPidMap pidmap : vcard.getClientPidMaps())
                jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                                             .name(V_Extension.toV_Extension(VCardPropEnum.CLIENTPIDMAP.getValue().toLowerCase()))
                                             .parameters(VCardUtils.getVCardUnconvertedPropParams(pidmap.getParameters()))
                                             .type(VCardDataType.TEXT)
                                             .value(String.format("%d,%s",pidmap.getPid(), pidmap.getUri()))
                                             .build());
        }

        if (vcard.getXmls()!=null) {
                for (Xml xml : vcard.getXmls())
                    jsCard.addVCardUnconvertedProp(VCardUnconvertedProperty.builder()
                                                 .name(V_Extension.toV_Extension(VCardPropEnum.XML.getValue().toLowerCase()))
                                                 .parameters(VCardUtils.getVCardUnconvertedPropParams(xml.getParameters()))
                                                 .type(VCardDataType.TEXT)
                                                 .value(xml.getValue().getTextContent())
                                                 .build());
        }

    }

    private static String toJSCardProdId(VCard vCard, Card jsCard) {

        if (vCard.getProductId() == null)
            return null;

        addVCardUnconvertedParams(jsCard,"prodId","prodid", VCardUtils.getVCardParamsOtherThan(vCard.getProductId()));
        return getValue(vCard.getProductId());
    }

    private static java.util.Calendar toJSCardUpdated(VCard vCard, Card jsCard) {

        if (vCard.getRevision() == null)
            return null;

        addVCardUnconvertedParams(jsCard,"updated","rev", VCardUtils.getVCardParamsOtherThan(vCard.getRevision()));
        return vCard.getRevision().getCalendar();
    }

    private static KindType toJSCardKind(VCard vCard, Card jsCard) {

        if (vCard.getKind() == null)
            return null;

        addVCardUnconvertedParams(jsCard,"kind","kind", VCardUtils.getVCardParamsOtherThan(vCard.getKind()));
        try {
            return KindType.builder().rfcValue(KindEnum.getEnum(getValue(vCard.getKind()))).build();
        } catch (IllegalArgumentException e) {
            return KindType.builder().extValue(V_Extension.toV_Extension(getValue(vCard.getKind()))).build();
        }
    }

    private void fillVCardUnconvertedParamsOfConvertedProperties(Card jsCard) {

        Map<String, it.cnr.iit.jscontact.tools.dto.VCardProperty> allVCardUnconvertedParmsMap = new HashMap<>();
        jsCard.buildAllVCardUnconvertedParmsOfConvertedPropertiesMap(allVCardUnconvertedParmsMap,StringUtils.EMPTY);
        if (allVCardUnconvertedParmsMap.isEmpty())
            return;
        if (jsCard.getVCard() == null)
            jsCard.setVCard(VCardData.builder().build());
        if (jsCard.getVCard().getConvertedProperties()==null || jsCard.getVCard().getConvertedProperties().isEmpty())
            jsCard.getVCard().setConvertedProperties(allVCardUnconvertedParmsMap);
        else
            jsCard.getVCard().getConvertedProperties().putAll(allVCardUnconvertedParmsMap);
    }


    private Card convert(VCard vCard) throws CardException {

        Card jsCard;
        String uid;
        if (vCard.getUid()!=null)
            uid = vCard.getUid().getValue();
        else {
            if (VersionUtils.getDefaultVersionEnum() == VersionUtils.VersionEnum.VERSION_1_0)
                uid = UuidUtils.getRandomV4UuidPrefixedByNamespace();
            else
                uid = null;
        }

        jsCard = Card.builder().uid(uid).version(VersionUtils.getDefaultVersion()).build();
        if (vCard.getUid()!= null)
            addVCardUnconvertedParams(jsCard,"uid","uid", VCardUtils.getVCardParamsOtherThan(vCard.getUid()));
        jsCard.setKind(toJSCardKind(vCard, jsCard));
        jsCard.setProdId(toJSCardProdId(vCard, jsCard));
        jsCard.setUpdated(toJSCardUpdated(vCard, jsCard));

        RawProperty language = vCard.getExtendedProperty(VCardPropEnum.LANGUAGE.getValue());
        jsCard.setLanguage((language!=null) ? language.getValue() : config.getDefaultLanguage());
        vCardPropertiesAltidComparator = VCardPropertiesAltidComparator.builder().defaultLanguage(jsCard.getLanguage()).build();
        vCardPropertiesPrefComparator = VCardPropertiesPrefComparator.builder().build();
        fillJSCardSpeakToAsOrGender(vCard, jsCard);
        fillJSCardMembers(vCard, jsCard);
        fillJSCardFullName(vCard, jsCard);
        fillJSCardNames(vCard, jsCard);
        fillJSCardNickNames(vCard, jsCard);
        fillJSCardAddresses(vCard, jsCard);
        fillJSCardAnniversaries(vCard, jsCard);
        fillJSCardPersonalInfos(vCard, jsCard);
        fillJSCardPreferredLanguages(vCard, jsCard);
        fillJSCardPhones(vCard, jsCard);
        fillJSCardEmails(vCard, jsCard);
        fillJSCardSchedulingAddresses(vCard,jsCard);
        fillJSCardOnlineServices(vCard, jsCard);
        fillJSCardCalendars(vCard, jsCard);
        fillJSCardCryptoKeys(vCard, jsCard);
        fillJSCardLinks(vCard, jsCard);
        fillJSCardMedia(vCard, jsCard);
        fillJSCardDirectories(vCard, jsCard);
        fillJSCardOrganizations(vCard, jsCard);
        fillJSCardTitlesfromVCardTitles(vCard, jsCard);
        fillJSCardTitlesfromVCardRoles(vCard, jsCard);
        fillJSCardKeywords(vCard, jsCard);
        fillJSCardNotes(vCard, jsCard);
        fillJSCardRelations(vCard, jsCard);
        fillJSCardPropsFromVCardJSContactExtensions(vCard, jsCard);
        if (customTimeZones.size() > 0)
            jsCard.setCustomTimeZones(customTimeZones);
        fillVCardUnconvertedProps(vCard, jsCard);
        fillJSCardPropsFromVCardExtensions(vCard, jsCard);
        fillJSPropsFromJSCardExtensionsInVCard(vCard, jsCard);
        fillVCardUnconvertedParamsOfConvertedProperties(jsCard);

       return jsCard;
    }

    private boolean warningIsAboutNonPrintableCharacters(String warning) {

        if (warning.contains("Parameter values may contain printable characters, with the exception of: [ \\n \\r \" ]"))
            return true;

        return false;
    }

    /**
     * Converts a list of vCard v4.0 instances [RFC6350] into a list of Card objects.
     * JSContact is defined in [RFC9553].
     * JSContact extensions to vCard are defined in [RFC9554]
     * Conversion rules are defined in [RFC9555].
     *
     * @param vCards a list of instances of the ez-vcard library VCard class [ez-vcard]
     * @return a list of Card objects
     * @throws CardException if one of the vCard instances is not v4.0 compliant
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553/">RFC9553</a>
     * @see <a href="https://datatracker.ietf.org/doc/RFC9554/">RFC9554</a>
     * @see <a href="https://datatracker.ietf.org/doc/RFC9555/">RFC9555</a>
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard</a>
     */
    public List<Card> convert(VCard... vCards) throws CardException {

        List<Card> jsCards = new ArrayList<>();

        for (VCard vCard : vCards) {
            if (config.isValidateCard()) {
                ValidationWarnings warnings = vCard.validate(VCardVersion.V4_0);
                if (!warnings.isEmpty() && !warningIsAboutNonPrintableCharacters(warnings.toString()))
                    throw new CardException(warnings.toString());
            }
            jsCards.add(convert(vCard));
        }

        return jsCards;
    }

}
