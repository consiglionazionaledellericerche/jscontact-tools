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
import ezvcard.util.GeoUri;
import ezvcard.util.UtcOffset;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Address;
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
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactIdsProfile;
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
 * Utility class for converting a vCard 4.0 [RFC6350] instance represented as an Ezvcard VCard object into a JSContact object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
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

    private static final Map<String, PhoneFeatureEnum> phoneFeatureAliases = new HashMap<String, PhoneFeatureEnum>() {{ put ("cell", PhoneFeatureEnum.MOBILE);}};

    private List<String> getVCard2JSContactProfileIds(VCard2JSContactIdsProfile.IdType idType, Object... args) {

        List<String> ids = new ArrayList<>();
        for (VCard2JSContactIdsProfile.JSContactId jsContactId : config.getIdsProfileToUse().getIds()) {

            if (jsContactId.getIdType() == idType) {
                switch (idType) {
                    case RESOURCE:
                        VCard2JSContactIdsProfile.ResourceId resourceId = (VCard2JSContactIdsProfile.ResourceId) jsContactId.getId();
                        ResourceType type = (ResourceType) args[0];
                        if (resourceId.getType() == type)
                            ids.add(resourceId.getId());
                        break;
                    case PERSONAL_INFO:
                        VCard2JSContactIdsProfile.PersonalInfoId piId = (VCard2JSContactIdsProfile.PersonalInfoId) jsContactId.getId();
                        PersonalInfoEnum piType = (PersonalInfoEnum) args[0];
                        if (piId.getPersonalInfoEnum() == piType)
                            ids.add(piId.getId());
                        break;
                    default:
                        ids.add((String) jsContactId.getId());
                        break;
                }
            }
        }

        return ids;
    }

    private String getJSCardId(VCard2JSContactIdsProfile.IdType idType, int index, String id, String propId, Object... args) {

        if (config.isSetUsePropIds() && propId != null)
            return propId;

        if (config.isSetAutoIdsProfile() || config.getIdsProfileToUse() == null || config.getIdsProfileToUse().getIds() == null || config.getIdsProfileToUse().getIds().size() == 0)
            return id;

        List<String> ids = (idType == VCard2JSContactIdsProfile.IdType.RESOURCE || idType == VCard2JSContactIdsProfile.IdType.PERSONAL_INFO) ? getVCard2JSContactProfileIds(idType,args[0]) : getVCard2JSContactProfileIds(idType);

        if (ids.size() == 0)
            return id;

        return (ids.get(index-1) == null) ? id : ids.get(index-1);

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

    private Map<PhoneFeature,Boolean> getJSCardDefaultPhoneFeatures() {

        if (config.isSetVoiceAsDefaultPhoneFeature())
            return new HashMap<PhoneFeature,Boolean>(){{ put(PhoneFeature.voice(), Boolean.TRUE);}};

        return null;
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
                .propId(property.getParameter(VCardParamEnum.PROP_ID.getValue()))
                .uri(value)
                .contexts(contexts)
                .mediaType(toJSCardMediaType(VCardUtils.getVCardParamValue(property.getParameters(), VCardParamEnum.MEDIATYPE), value))
                .pref(toJSCardPref(VCardUtils.getVCardParamValue(property.getParameters(), VCardParamEnum.PREF)))
                .build();
    }

    private void addJSCardMediaResource(VCardProperty vcardProperty, Card jsCard, MediaResourceKind type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addMediaResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue()),  ResourceType.valueOf(type.getRfcValue().name())),
                                    MediaResource.builder()
                                            .kind(type)
                                            .propId(resource.getPropId())
                                            .uri(resource.getUri())
                                            .contexts(resource.getContexts())
                                            .mediaType(resource.getMediaType())
                                            .pref(resource.getPref())
                                            .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                                            .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
                                    .build()
                           );
    }

    private DirectoryResource toJSCardDirectoryResource(VCardProperty vcardProperty, DirectoryResourceKind type, List<RawProperty> vcardExtendedProperties) {

        String index = vcardProperty.getParameter(VCardParamEnum.INDEX.getValue());
        Resource resource = toJSCardResource(vcardProperty);
        return DirectoryResource.builder()
                .kind(type)
                .propId(resource.getPropId())
                .uri(resource.getUri())
                .contexts(resource.getContexts())
                .mediaType(resource.getMediaType())
                .pref(resource.getPref())
                .listAs((index != null) ? Integer.parseInt(index) : null) //used only for DirectoryResource objects whose "kind" is "directory"
                .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE, VCardParamEnum.INDEX))
                .build();
    }

    private void addJSCardDirectoryResource(VCardProperty property, Card jsCard, DirectoryResourceKind type, int index, List<RawProperty> vcardExtendedProperties) {

        jsCard.addDirectoryResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), property.getParameter(VCardParamEnum.PROP_ID.getValue()), type),
                                    toJSCardDirectoryResource(property,type, vcardExtendedProperties));
    }

    private void addJSCardDirectoryResource(Card jsCard, DirectoryResource resource, int index) {

        jsCard.addDirectoryResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",resource.getKind().getRfcValue().name(),index), resource.getPropId(), ResourceType.valueOf(resource.getKind().getRfcValue().name())),
                                   resource);
    }

    private void addJSCardCryptoResource(VCardProperty vcardProperty, Card jsCard, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addCryptoResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("KEY-%s",index), vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue()), ResourceType.KEY),
                CryptoResource.builder()
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                        .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
                        .build()
        );
    }

    private void addJSCardCalendarResource(VCardProperty vcardProperty, Card jsCard, CalendarResourceKind type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addCalendarResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue()), ResourceType.valueOf(type.getRfcValue().name())),
                CalendarResource.builder()
                        .kind(type)
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                        .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
                        .build()
        );
    }

    private void addJSCardLinkResource(VCardProperty vcardProperty, Card jsCard, LinkResourceKind type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addLinkResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",(type!=null) ? type.getRfcValue().name() : "LINK",index), vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue()), (type!=null) ? ResourceType.valueOf(type.getRfcValue().name()) : ResourceType.LINK),
                LinkResource.builder()
                        .kind(type)
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty, vcardExtendedProperties))
                        .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardProperty, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.MEDIATYPE))
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
                                               (hours.equals("00") && minutes.equals("00")) ? StringUtils.EMPTY : (sign.equals("+") ? "-" : "+") ,
                                               (hours.equals("00") && minutes.equals("00")) ? StringUtils.EMPTY : String.valueOf(Integer.parseInt(hours)),
                                               (minutes.equals("00") ? StringUtils.EMPTY : ":" + minutes));
            else {
                String timeZoneName = String.format("%s%d", config.getCustomTimeZonesPrefix(), ++customTimeZoneCounter);
                customTimeZones.put(timeZoneName, TimeZone.builder()
                        .tzId(String.format("%s%s%s%s",CUSTOM_TIME_ZONE_ID_PREFIX,sign,hours,minutes))
                        .updated(Calendar.getInstance())
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

        return property.getUrl();
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
            jsCard.addVCardProp(VCardProp.builder()
                                         .name(V_Extension.toV_Extension(VCardPropEnum.GENDER.getValue().toLowerCase()))
                                         .parameters(VCardUtils.getVCardPropParams(vCard.getGender().getParameters()))
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

    private void fillJSCardFullName(VCard vcard, Card jsCard) {

        if (vcard.getFormattedNames() == null || vcard.getFormattedNames().isEmpty())
            return;

        List<FormattedName> fns = vcard.getFormattedNames();
        fns.sort(vCardPropertiesAltidComparator);
        String lastAltid = null;
        for (FormattedName fn : fns) {
            if (fn.getAltId() == null || lastAltid == null || !fn.getAltId().equals(lastAltid)) {
                if (jsCard.getName() == null) //no name exists
                    jsCard.setName(Name.builder().full(fn.getValue()).build());
                else
                    jsCard.getName().setFull(fn.getValue());
                lastAltid = fn.getAltId();
            } else {
                jsCard.addLocalization(fn.getLanguage(), "name", mapper.convertValue(Name.builder().full(fn.getValue()).build(), JsonNode.class));
            }
        }
    }

    private void fillJSCardMembers(VCard vcard, Card jsCard) {

        List<Member> members = vcard.getMembers();
        members.sort(vCardPropertiesPrefComparator);
        for (Member member : members)
            jsCard.addMember(member.getValue());
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

    private Name toJSCardName(ExtendedStructuredName vcardName) throws CardException {

        NameComponent[] components = null;

        String[] jscomps = (vcardName.getParameter(VCardParamEnum.JSCOMPS.getValue())!=null) ? vcardName.getParameter(VCardParamEnum.JSCOMPS.getValue()).split(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER) : null;

        boolean isPhonetic = (vcardName.getParameter(VCardParamEnum.PHONETIC.getValue())!=null) || (vcardName.getParameter(VCardParamEnum.SCRIPT.getValue())!=null);

        if (jscomps == null || jscomps.length < 2) {

            if (vcardName.getFamily() != null) {
                String[] surnames = vcardName.getFamily().split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                for (String surname : surnames) {
                    if (vcardName.getSurname2().contains(surname))
                        continue;
                    components = Name.addComponent(components, NameComponent.surname(surname, (isPhonetic) ? surname : null));
                }
            }

            if (vcardName.getGiven() != null) {
                String[] names = vcardName.getGiven().split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                for (String name : names)
                    components = Name.addComponent(components, NameComponent.given(name, (isPhonetic) ? name : null));
            }

            for (String an : vcardName.getAdditionalNames())
                components = Name.addComponent(components, NameComponent.given2(an, (isPhonetic) ? an : null));

            for (String px : vcardName.getPrefixes())
                components = Name.addComponent(components, NameComponent.title(px, (isPhonetic) ? px : null));

            for (String sx : vcardName.getSuffixes()) {
                if (vcardName.getGeneration().contains(sx))
                    continue;
                components = Name.addComponent(components, NameComponent.credential(sx, (isPhonetic) ? sx : null));
            }

            for (String sx : vcardName.getSurname2())
                components = Name.addComponent(components, NameComponent.surname2(sx, (isPhonetic) ? sx : null));

            for (String sx : vcardName.getGeneration())
                components = Name.addComponent(components, NameComponent.generation(sx, (isPhonetic) ? sx : null));
        }
        else {

            for (int i = 1; i < jscomps.length; i++) {

                if (jscomps[i].startsWith(DelimiterUtils.SEPARATOR_ID)) {
                    components = Name.addComponent(components, NameComponent.separator(jscomps[i].replace(DelimiterUtils.SEPARATOR_ID, StringUtils.EMPTY)));
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
                        String[] surnames = vcardName.getFamily().split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                        value = surnames[index2];
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

                components = Name.addComponent(components, NameComponent.builder().kind(kind).value(value).phonetic((isPhonetic) ? value : null).build());
            }
        }

        PhoneticSystem phoneticSystem = null;

        if (vcardName.getParameter(VCardParamEnum.PHONETIC.getValue())!=null) {
            try {
                phoneticSystem = PhoneticSystem.rfc(PhoneticSystemEnum.getEnum(vcardName.getParameter(VCardParamEnum.PHONETIC.getValue())));
            } catch (IllegalArgumentException e) {
                phoneticSystem = PhoneticSystem.ext(vcardName.getParameter(VCardParamEnum.PHONETIC.getValue()));
            }
        }

        return Name.builder()
                .components(components)
                .sortAs(toJSCardNameSortAs(vcardName.getSortAs(), vcardName))
                .defaultSeparator((ArrayUtils.isNotEmpty(jscomps) && jscomps[0].startsWith(DelimiterUtils.SEPARATOR_ID)) ? jscomps[0].replace(DelimiterUtils.SEPARATOR_ID,StringUtils.EMPTY) : null)
                .isOrdered((jscomps!=null) ? Boolean.TRUE : null)
                .phoneticSystem(phoneticSystem)
                .phoneticScript(vcardName.getParameter(VCardParamEnum.SCRIPT.getValue()))
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardName, VCardParamEnum.LANGUAGE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS))
                .build();
    }


    private String getFullNamePerLanguage(VCard vcard, String language) {

        if (vcard.getFormattedNames() == null)
            return null;

        for (FormattedName fn : vcard.getFormattedNames()) {
            if (fn.getLanguage().equals(language))
                return fn.getValue();
        }

        return null;
    }

    private void fillJSCardNames(VCard vcard, Card jsCard) throws CardException {

        if (vcard.getProperties(ExtendedStructuredName.class) == null || vcard.getProperties(ExtendedStructuredName.class).isEmpty())
            return;

        List<ExtendedStructuredName> vcardNames = vcard.getProperties(ExtendedStructuredName.class);
        vcardNames.sort(vCardPropertiesAltidComparator);

        if (jsCard.getName() == null) // no full name exists
            jsCard.setName(toJSCardName(vcardNames.get(0))); //the first N property is the name, all the others name localization
        else {
            // full name already been set,
            String fullName = jsCard.getName().getFull();
            Name name = toJSCardName(vcardNames.get(0));
            name.setFull(fullName);
            jsCard.setName(name);
        }
        if (jsCard.getLanguage() == null && vcardNames.get(0).getLanguage() != null)
            jsCard.setLanguage(vcardNames.get(0).getLanguage());

        for (int i = 1; i < vcardNames.size(); i++) {
            String language = vcardNames.get(i).getLanguage();

            Name name = toJSCardName(vcardNames.get(i));

            if (name.getPhoneticSystem() != null || name.getPhoneticScript() != null) {
                if (name.getPhoneticSystem()!= null)
                    jsCard.addLocalization(language, "name/phoneticSystem", JsonNodeUtils.textNode(name.getPhoneticSystem().toJson()));
                if (name.getPhoneticScript()!= null)
                    jsCard.addLocalization(language, "name/phoneticScript", JsonNodeUtils.textNode(name.getPhoneticScript()));

                if (name.getComponents() != null) {
                    int j = 0;
                    for (NameComponent component : name.getComponents()) {
                        if (component.getValue().equals(component.getPhonetic()))
                            jsCard.addLocalization(language, "name/components/" + j++ + "/phonetic", JsonNodeUtils.textNode(component.getPhonetic()));
                    }
                }
            }
            else if (jsCard.getLocalization(language, "name") == null)
                jsCard.addLocalization(language, "name", mapper.convertValue(name, JsonNode.class));
            else {
                name.setFull(getFullNamePerLanguage(vcard, language));
                jsCard.addLocalization(language, "name", mapper.convertValue(name, JsonNode.class));
            }
        }
    }

    private Nickname toJSCardNickName(String name, ezvcard.property.Nickname vcardNickname, VCard vcard) {

        return Nickname.builder()
                .name(name)
                .pref(vcardNickname.getPref())
                .contexts(toJSCardContexts(vcardNickname.getType()))
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardNickname, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.ALTID))
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
                    String propId = vcardNickName.getParameter(VCardParamEnum.PROP_ID.getValue());
                    String id = getJSCardId(VCard2JSContactIdsProfile.IdType.NICKNAME, i, "NICK-" + (i++), propId);
                    jsCard.addNickName(id, toJSCardNickName(name, vcardNickName, vcard));
                    lastAltid = vcardNickName.getAltId();
                    lastMapId = id;
                }
                else {
                    jsCard.addLocalization(vcardNickName.getLanguage(), "nicknames/" + lastMapId, mapper.convertValue(toJSCardNickName(name, vcardNickName, vcard), JsonNode.class));
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

    private Address toJSCardAddress(ExtendedAddress vcardAddr, VCard vcard) {

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
            if (StringUtils.isNotEmpty(vcardAddr.getStreetName()))
                streetDetailPairs.add(AddressComponent.name(vcardAddr.getStreetName(), (isPhonetic) ? vcardAddr.getStreetName() : null));
            if (StringUtils.isNotEmpty(vcardAddr.getStreetNumber()))
                streetDetailPairs.add(AddressComponent.number(vcardAddr.getStreetNumber()));
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

                String[] items = jscomps[i].split(DelimiterUtils.COMMA_ARRAY_DELIMITER);

                if (items[i].startsWith(DelimiterUtils.SEPARATOR_ID)) {
                    streetDetailPairs.add(AddressComponent.separator(items[i].replace(DelimiterUtils.SEPARATOR_ID, StringUtils.EMPTY)));
                    continue;
                }

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
                        streetDetailPairs.add(AddressComponent.name(vcardAddr.getStreetNames().get(index2), (isPhonetic) ? vcardAddr.getStreetNames().get(index2) : null));
                        break;
                    case 11:
                        streetDetailPairs.add(AddressComponent.number(vcardAddr.getStreetNumbers().get(index2)));
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

        if (vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue())!=null) {
            try {
                phoneticSystem = PhoneticSystem.rfc(PhoneticSystemEnum.getEnum(vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue())));
            } catch (IllegalArgumentException e) {
                phoneticSystem = PhoneticSystem.ext(vcardAddr.getParameter(VCardParamEnum.PHONETIC.getValue()));
            }
        }

        return it.cnr.iit.jscontact.tools.dto.Address.builder()
                .hash(autoFullAddress)
                .propId(vcardAddr.getParameter(VCardParamEnum.PROP_ID.getValue()))
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
                .defaultSeparator((ArrayUtils.isNotEmpty(jscomps) && jscomps[0].startsWith(DelimiterUtils.SEPARATOR_ID)) ? jscomps[0].replace(DelimiterUtils.SEPARATOR_ID,StringUtils.EMPTY) : null)
                .isOrdered((jscomps!=null) ? Boolean.TRUE : null)
                .propId(vcardAddr.getParameter(VCardParamEnum.PROP_ID.getValue()))
                .phoneticSystem(phoneticSystem)
                .phoneticScript(vcardAddr.getParameter(VCardParamEnum.SCRIPT.getValue()))
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardAddr, VCardParamEnum.PROP_ID, VCardParamEnum.LANGUAGE, VCardParamEnum.LABEL, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.CC, VCardParamEnum.TZ, VCardParamEnum.GEO, VCardParamEnum.DERIVED, VCardParamEnum.ALTID, VCardParamEnum.JSCOMPS))
                .build();
    }

    private void fillJSCardAddresses(VCard vcard, Card jsCard) {

        if (vcard.getProperties(ExtendedAddress.class) == null || vcard.getProperties(ExtendedAddress.class).isEmpty())
            return;

        List<it.cnr.iit.jscontact.tools.dto.Address> addresses = new ArrayList<>();
        String tz;
        String geo;
        for (ExtendedAddress addr : vcard.getProperties(ExtendedAddress.class))
            addresses.add(toJSCardAddress(addr, vcard));

        if (vcard.getTimezone() != null) {
            tz = getValue(vcard.getTimezone());
            it.cnr.iit.jscontact.tools.dto.Address address = findJSCardAddressByGroup(addresses, vcard.getTimezone().getGroup());
            if (address != null) {
                address.setTimeZone(tz);
            } else {
                jsCard.addVCardProp(VCardProp.builder()
                        .name(V_Extension.toV_Extension(VCardPropEnum.TZ.getValue().toLowerCase()))
                        .parameters(VCardUtils.getVCardPropParams(vcard.getTimezone().getParameters()))
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
                jsCard.addVCardProp(VCardProp.builder()
                        .name(V_Extension.toV_Extension(VCardPropEnum.GEO.getValue().toLowerCase()))
                        .parameters(VCardUtils.getVCardPropParams(vcard.getGeo().getParameters()))
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
                String id = getJSCardId(VCard2JSContactIdsProfile.IdType.ADDRESS, i, "ADR-" + (i++), address.getPropId() );
                jsCard.addAddress(id, address);
                lastAltid = address.getAltid();
                lastMapId = id;
            } else {
                if (address.getPhoneticSystem()!= null || address.getPhoneticScript() != null) {
                    if (address.getPhoneticSystem()!= null)
                        jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId+ "/phoneticSystem", JsonNodeUtils.textNode(address.getPhoneticSystem().toJson()));
                    if (address.getPhoneticScript()!= null)
                        jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId+ "/phoneticScript", JsonNodeUtils.textNode(address.getPhoneticScript()));

                    if (address.getComponents() != null) {
                        for (AddressComponent component : address.getComponents()) {
                            if (component.getValue().equals(component.getPhonetic()))
                                jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId+ "/components/" + i + "/phonetic", JsonNodeUtils.textNode(component.getPhonetic()));
                        }
                    }
                }
                else
                    jsCard.addLocalization(address.getLanguage(), "addresses/" + lastMapId, mapper.convertValue(address, JsonNode.class));
            }
        }
    }

    private static  <T extends DateOrTimeProperty> AnniversaryDate toJSCardAnniversaryDate(T date) {

        try {
            if (date.getDate() != null)
                return AnniversaryDate.builder().date(Timestamp.builder().utc(date.getCalendar()).build()).build();
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
                  .vCardParams(VCardUtils.getVCardParamsOtherThan(vcard.getBirthday(), VCardParamEnum.PROP_ID, VCardParamEnum.CALSCALE))
                  .build();
          jsCard.addAnniversary(getJSCardId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i++), vcard.getBirthday().getParameter(VCardParamEnum.PROP_ID.getValue())), anniversary);
      }

      if (vcard.getDeathdate() != null) {
          it.cnr.iit.jscontact.tools.dto.Anniversary anniversary = it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                  .kind(AnniversaryKind.death())
                  .date(toJSCardAnniversaryDate(vcard.getDeathdate()))
                  .place(getValue(vcard.getDeathplace()))
                  .vCardParams(VCardUtils.getVCardParamsOtherThan(vcard.getDeathdate(), VCardParamEnum.PROP_ID, VCardParamEnum.CALSCALE))
                  .build();
          jsCard.addAnniversary(getJSCardId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i++), vcard.getDeathdate().getParameter(VCardParamEnum.PROP_ID.getValue())), anniversary);
      }

      if (vcard.getAnniversary() != null) {
          it.cnr.iit.jscontact.tools.dto.Anniversary anniversary = it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                  .kind(AnniversaryKind.wedding())
                  .date(toJSCardAnniversaryDate(vcard.getAnniversary()))
                  .vCardParams(VCardUtils.getVCardParamsOtherThan(vcard.getAnniversary(), VCardParamEnum.PROP_ID, VCardParamEnum.CALSCALE))
                  .build();
          jsCard.addAnniversary(getJSCardId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i++), vcard.getAnniversary().getParameter(VCardParamEnum.PROP_ID.getValue())), anniversary);
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
            jsCard.addPersonalInfo(getJSCardId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "HOBBY-" + (i++), hobby.getParameter(VCardParamEnum.PROP_ID.getValue()), PersonalInfoEnum.HOBBY),
                    PersonalInfo.builder()
                            .kind(PersonalInfoKind.builder().rfcValue(PersonalInfoEnum.HOBBY).build())
                            .value(getValue(hobby))
                            .level((hobby.getLevel() != null) ? toJSCardLevel(hobby.getLevel().getValue()) : null)
                            .listAs(hobby.getIndex())
                            .label(toJSCardLabel(hobby, vcard.getExtendedProperties()))
                            .vCardParams(VCardUtils.getVCardParamsOtherThan(hobby, VCardParamEnum.PROP_ID, VCardParamEnum.LEVEL, VCardParamEnum.INDEX))
                            .build()
            );
        }

        i = 1;
        for (Interest interest : vcard.getInterests()) {
            jsCard.addPersonalInfo(getJSCardId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "INTEREST-" + (i++), interest.getParameter(VCardParamEnum.PROP_ID.getValue()), PersonalInfoEnum.INTEREST),
                    PersonalInfo.builder()
                            .kind(PersonalInfoKind.builder().rfcValue(PersonalInfoEnum.INTEREST).build())
                            .value(getValue(interest))
                            .level((interest.getLevel() != null) ? toJSCardLevel(interest.getLevel().getValue()) : null)
                            .listAs(interest.getIndex())
                            .label(toJSCardLabel(interest, vcard.getExtendedProperties()))
                            .vCardParams(VCardUtils.getVCardParamsOtherThan(interest, VCardParamEnum.PROP_ID, VCardParamEnum.LEVEL, VCardParamEnum.INDEX))
                            .build()
            );
        }

        i = 1;
        for (Expertise expertise : vcard.getExpertise()) {
            jsCard.addPersonalInfo(getJSCardId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "EXPERTISE-" + (i++), expertise.getParameter(VCardParamEnum.PROP_ID.getValue()), PersonalInfoEnum.INTEREST),
                    PersonalInfo.builder()
                            .propId(expertise.getParameter(VCardParamEnum.PROP_ID.getValue()))
                            .kind(PersonalInfoKind.builder().rfcValue(PersonalInfoEnum.EXPERTISE).build())
                            .value(getValue(expertise))
                            .level((expertise.getLevel() != null) ? toJSCardLevel(expertise.getLevel().getValue()) : null)
                            .listAs(expertise.getIndex())
                            .label(toJSCardLabel(expertise, vcard.getExtendedProperties()))
                            .vCardParams(VCardUtils.getVCardParamsOtherThan(expertise, VCardParamEnum.PROP_ID, VCardParamEnum.LEVEL, VCardParamEnum.INDEX))
                            .build()
            );
        }

    }

    private void fillJSCardPreferredLanguages(VCard vcard, Card jsCard) {

        int i = 1;
        for (Language lang : vcard.getLanguages()) {
            String vcardType = VCardUtils.getVCardParamValue(lang.getParameters(), VCardParamEnum.TYPE);
            jsCard.addLanguagePref(getJSCardId(VCard2JSContactIdsProfile.IdType.LANGUAGE, i,"LANG-" + (i++), lang.getParameter(VCardParamEnum.PROP_ID.getValue())),
                    LanguagePref.builder()
                            .contexts(toJSCardContexts(vcardType))
                            .pref(lang.getPref())
                            .language(getValue(lang))
                            .vCardParams(VCardUtils.getVCardParamsOtherThan(lang, VCardParamEnum.TYPE, VCardParamEnum.PREF))
                                                       .build()
                                        );

        }
    }


    private static String getResourceExt(String resource) {

        int index = resource.lastIndexOf('.');
        return (index > 0) ? resource.substring(index + 1) : null;
    }

    private static String toJSCardMediaType(String mediaTypeParamValue, String resource) {

        if (mediaTypeParamValue != null)
            return mediaTypeParamValue;

        String ext = getResourceExt(resource);
        return (ext != null) ? MimeTypeUtils.lookupMimeType(ext) : null;
    }

    private void fillJSCardPhones(VCard vcard, Card jsCard) {

        int i = 1;
        for (Telephone tel : vcard.getTelephoneNumbers()) {
            String vcardType = VCardUtils.getVCardParamValue(tel.getParameters(), VCardParamEnum.TYPE);
            Map<Context,Boolean> contexts = toJSCardContexts(vcardType);
            Map<PhoneFeature,Boolean> phoneFeatures = toJSCardPhoneFeatures(vcardType);
            String[] exclude = null;
            if (contexts != null) exclude = ArrayUtils.addAll(null, EnumUtils.toStrings(Context.toEnumValues(contexts.keySet())));
            if (phoneFeatures != null) exclude = ArrayUtils.addAll(exclude, EnumUtils.toStrings(PhoneFeature.toEnumValues(phoneFeatures.keySet())));
            jsCard.addPhone(getJSCardId(VCard2JSContactIdsProfile.IdType.PHONE, i,"PHONE-" + (i++), tel.getParameter(VCardParamEnum.PROP_ID.getValue())), Phone.builder()
                            .number(getValue(tel))
                            .features((phoneFeatures == null) ? getJSCardDefaultPhoneFeatures() : phoneFeatures)
                            .contexts(contexts)
                            .pref(tel.getPref())
                            .label(toJSCardLabel(tel, vcard.getExtendedProperties()))
                            .vCardParams(VCardUtils.getVCardParamsOtherThan(tel, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF))
                                       .build()
                              );
        }

    }

    private void fillJSCardEmails(VCard vcard, Card jsCard) {

        int i = 1;
        for (Email email : vcard.getEmails()) {
            String emailAddress = getValue(email);
            if (StringUtils.isNotEmpty(emailAddress)) {
                String vcardType = VCardUtils.getVCardParamValue(email.getParameters(), VCardParamEnum.TYPE);
                jsCard.addEmailAddress(getJSCardId(VCard2JSContactIdsProfile.IdType.EMAIL, i, "EMAIL-" + (i++), email.getParameter(VCardParamEnum.PROP_ID.getValue())), EmailAddress.builder()
                        .address(emailAddress)
                        .contexts(toJSCardContexts(vcardType))
                        .pref(email.getPref())
                        .label(toJSCardLabel(email, vcard.getExtendedProperties()))
                        .vCardParams(VCardUtils.getVCardParamsOtherThan(email, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF))
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
            jsCard.addOnlineService(getJSCardId(VCard2JSContactIdsProfile.IdType.ONLINE_SERVICE, i,"OS-" + (i++), impp.getParameter(VCardParamEnum.PROP_ID.getValue())), OnlineService.builder()
                    .uri(getValue(impp))
                    .vCardName(VCardPropEnum.IMPP.getValue().toLowerCase())
                    .contexts(contexts)
                    .pref(impp.getPref())
                    .user(VCardUtils.getVCardParamValue(impp.getParameters(), VCardParamEnum.USERNAME))
                    .service(VCardUtils.getVCardParamValue(impp.getParameters(), VCardParamEnum.SERVICE_TYPE))
                    .label(toJSCardLabel(impp, vcard.getExtendedProperties()))
                    .vCardParams(VCardUtils.getVCardParamsOtherThan(impp, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.SERVICE_TYPE, VCardParamEnum.USERNAME))
                    .build()
            );
        }

    }

    private void fillJSCardMedia(VCard vcard, Card jsCard) {

        int i = 1;
        for (Photo photo : vcard.getPhotos())
            addJSCardMediaResource(photo, jsCard, MediaResourceKind.photo(), i++, vcard.getExtendedProperties());
        i = 1;
        for (Sound sound : vcard.getSounds())
            addJSCardMediaResource(sound, jsCard, MediaResourceKind.sound(), i++, vcard.getExtendedProperties());

        i = 1;
        for (Logo logo : vcard.getLogos())
            addJSCardMediaResource(logo, jsCard, MediaResourceKind.logo(), i++, vcard.getExtendedProperties());

    }

    private void fillJSCardCryptoKeys(VCard vcard, Card jsCard) {

        int i = 1;
        for (Key key : vcard.getKeys())
            addJSCardCryptoResource(key, jsCard, i++, vcard.getExtendedProperties());

    }

    private void fillJSCardDirectories(VCard vcard, Card jsCard) {

        int i = 1;
        for (Source source : vcard.getSources())
            addJSCardDirectoryResource(source, jsCard, DirectoryResourceKind.entry(), i++, vcard.getExtendedProperties());

        i = 1;
        for (OrgDirectory od : vcard.getOrgDirectories())
            addJSCardDirectoryResource(jsCard, toJSCardDirectoryResource(od, DirectoryResourceKind.directory(), vcard.getExtendedProperties()), i++);
    }

    private void fillJSCardCalendars(VCard vcard, Card jsCard) {

        int i = 1;
        for (CalendarUri calendarUri : vcard.getCalendarUris())
            addJSCardCalendarResource(calendarUri, jsCard, CalendarResourceKind.calendar(), i++, vcard.getExtendedProperties());

        i = 1;
        for (FreeBusyUrl fburl : vcard.getFbUrls())
            addJSCardCalendarResource(fburl, jsCard, CalendarResourceKind.freeBusy(), i++, vcard.getExtendedProperties());

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
            addJSCardLinkResource(uriProperty, jsCard, LinkResourceKind.contact(), i++, vcard.getExtendedProperties());
        }

    }

    private void fillJSCardSchedulingAddresses(VCard vcard, Card jsCard) {

        int i = 1;
        for (CalendarRequestUri calendarRequestUri : vcard.getCalendarRequestUris()) {
            String vcardType = VCardUtils.getVCardParamValue(calendarRequestUri.getParameters(), VCardParamEnum.TYPE);
            Map<Context,Boolean> contexts = toJSCardContexts(vcardType);
            jsCard.addSchedulingAddress(getJSCardId(VCard2JSContactIdsProfile.IdType.SCHEDULING, i, "SCHEDULING-" + (i++), calendarRequestUri.getParameter(VCardParamEnum.PROP_ID.getValue())),
                    SchedulingAddress.builder()
                            .propId(calendarRequestUri.getParameter(VCardParamEnum.PROP_ID.getValue()))
                            .uri(calendarRequestUri.getValue())
                            .contexts(contexts)
                            .pref(calendarRequestUri.getPref())
                            .label(toJSCardLabel(calendarRequestUri, vcard.getExtendedProperties()))
                            .vCardParams(VCardUtils.getVCardParamsOtherThan(calendarRequestUri, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF))
                             .build());
        }
    }


    private static String findJSCardOrganizationIdByGroup(Map<String,it.cnr.iit.jscontact.tools.dto.Organization> jscardOrganizations, String vcardTitleGroup) {

        if (vcardTitleGroup == null)
            return null;

        if (jscardOrganizations == null || jscardOrganizations.isEmpty())
            return null;

        for (Map.Entry<String,it.cnr.iit.jscontact.tools.dto.Organization> entry : jscardOrganizations.entrySet()) {
            if (entry.getValue().getGroup().equals(vcardTitleGroup))
                return entry.getKey();
        }

        return null;
    }

    private it.cnr.iit.jscontact.tools.dto.Title toJSCardTitle(Title vcardTitle, VCard vcard, Card jsCard) {

        return it.cnr.iit.jscontact.tools.dto.Title.builder()
                .name(vcardTitle.getValue())
                .kind(TitleKind.title())
                .organization(findJSCardOrganizationIdByGroup(jsCard.getOrganizations(), vcardTitle.getGroup()))
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardTitle, VCardParamEnum.PROP_ID))
                .build();
    }

    private it.cnr.iit.jscontact.tools.dto.Title toJSCardTitle(Role vcardRole, VCard vcard, Card jsCard) {

        return it.cnr.iit.jscontact.tools.dto.Title.builder()
                .name(vcardRole.getValue())
                .kind(TitleKind.role())
                .organization(findJSCardOrganizationIdByGroup(jsCard.getOrganizations(), vcardRole.getGroup()))
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardRole, VCardParamEnum.PROP_ID, VCardParamEnum.ALTID))
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
                String propId = vcardTitle.getParameter(VCardParamEnum.PROP_ID.getValue());
                String id = getJSCardId(VCard2JSContactIdsProfile.IdType.TITLE, i, "TITLE-" + (i ++), propId);
                jsCard.addTitle(id, toJSCardTitle(vcardTitle, vcard, jsCard));
                lastAltid = vcardTitle.getAltId();
                lastMapId = id;
            } else {
                jsCard.addLocalization(vcardTitle.getLanguage(), "titles/" + lastMapId, mapper.convertValue(toJSCardTitle(vcardTitle, vcard, jsCard), JsonNode.class));
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
                String propId = vcardRole.getParameter(VCardParamEnum.PROP_ID.getValue());
                String id = getJSCardId(VCard2JSContactIdsProfile.IdType.TITLE, i, "TITLE-" + (i ++), propId);
                jsCard.addTitle(id, toJSCardTitle(vcardRole, vcard, jsCard));
                lastAltid = vcardRole.getAltId();
                lastMapId = id;
            } else {
                jsCard.addLocalization(vcardRole.getLanguage(), "titles/" + lastMapId, mapper.convertValue(toJSCardTitle(vcardRole, vcard, jsCard), JsonNode.class));
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
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardOrg, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.SORT_AS, VCardParamEnum.ALTID))
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
                String propId = vcardOrg.getParameter(VCardParamEnum.PROP_ID.getValue());
                String id = getJSCardId(VCard2JSContactIdsProfile.IdType.ORGANIZATION, i, "ORG-" + (i++), propId);
                jsCard.addOrganization(id, toJSCardOrganization(vcardOrg, vcard));
                lastAltid = vcardOrg.getAltId();
                lastMapId = id;
            } else {
                jsCard.addLocalization(vcardOrg.getLanguage(), "organizations/" + lastMapId, mapper.convertValue(toJSCardOrganization(vcardOrg, vcard), JsonNode.class));
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
                .vCardParams(VCardUtils.getVCardParamsOtherThan(vcardNote, VCardParamEnum.PROP_ID, VCardParamEnum.ALTID, VCardParamEnum.AUTHOR, VCardParamEnum.AUTHOR_NAME, VCardParamEnum.CREATED))
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
                String propId = vcardNote.getParameter(VCardParamEnum.PROP_ID.getValue());
                String id = getJSCardId(VCard2JSContactIdsProfile.IdType.NOTE, i, "NOTE-" + (i++), propId);
                jsCard.addNote(id, toJSCardNote(vcardNote, vcard));
                lastAltid = vcardNote.getAltId();
                lastMapId = id;
            } else {
                jsCard.addLocalization(vcardNote.getLanguage(), "notes/" + lastMapId, mapper.convertValue(toJSCardNote(vcardNote, vcard), JsonNode.class));
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
        }
    }

    //Fill JSContact properties mapping the VCard properties defined in draft-ietf-calext-vcard-jscontact-extensions
    private void fillJSCardPropsFromVCardJSContactExtensions(VCard vcard, Card jsCard) {

        int i = 1;
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
                continue; //vcard extension already treated elsewhere

            if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.LANGUAGE.getValue()))
                continue; // has already been set
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.CREATED.getValue()))
                jsCard.setCreated(DateUtils.toCalendar(extension.getValue()));
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.GRAMGENDER.getValue())) {
                GrammaticalGenderType gender = GrammaticalGenderType.getEnum(extension.getValue().toLowerCase());
                if (jsCard.getSpeakToAs() != null)
                    jsCard.getSpeakToAs().setGrammaticalGender(gender);
                else
                    jsCard.setSpeakToAs(SpeakToAs.builder().grammaticalGender(gender).build());
            }
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.PRONOUNS.getValue())) {
                String id = getJSCardId(VCard2JSContactIdsProfile.IdType.PRONOUNS, i, "PRONOUNS-" + (i++), extension.getParameter(VCardParamEnum.PROP_ID.getValue()));
                Pronouns pronouns = Pronouns.builder()
                        .pronouns(extension.getValue())
                        .contexts(contexts)
                        .pref(pref)
                        .vCardParams(VCardUtils.getVCardParamsOtherThan(extension, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.ALTID))
                        .build();
                jsonPointer = String.format("%s/%s", jsonPointer, id);
                if (language == null || config.getDefaultLanguage().equalsIgnoreCase(language)) {
                    if (jsCard.getSpeakToAs() != null) {
                        jsCard.getSpeakToAs().addPronouns(id, pronouns);
                    } else {
                        jsCard.setSpeakToAs(SpeakToAs.builder().pronouns(new HashMap<String, Pronouns>() {{
                            put(id, pronouns);
                        }}).build());
                    }
                } else {
                    jsCard.addLocalization(language, jsonPointer, mapper.convertValue(pronouns, JsonNode.class));
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
                        .vCardParams(VCardUtils.getVCardParamsOtherThan(extension, VCardParamEnum.PROP_ID, VCardParamEnum.TYPE, VCardParamEnum.PREF, VCardParamEnum.SERVICE_TYPE))
                        .build();

                if ((extension.getDataType() == null || extension.getDataType() == VCardDataType.URI) && extension.getParameter(VCardParamEnum.USERNAME.getValue()) != null)
                    ol.setUser(extension.getParameter(VCardParamEnum.USERNAME.getValue()));

                jsCard.addOnlineService(getJSCardId(VCard2JSContactIdsProfile.IdType.ONLINE_SERVICE, i,"OS-" + (i++), extension.getParameter(VCardParamEnum.PROP_ID.getValue())), ol);
            }
        }
    }


    private void fillJSCardPropsFromVCardExtensions(VCard vcard, Card jsCard) {

        for (RawProperty extension : vcard.getExtendedProperties()) {
            if (!fakeExtensionsMapping.containsKey(extension.getPropertyName()) &&
                    !fakeExtensionsMapping.containsKey(extension.getPropertyName().toLowerCase())) {
                jsCard.addVCardProp(VCardProp.builder()
                                             .name(V_Extension.toV_Extension(extension.getPropertyName()))
                                             .parameters(VCardUtils.getVCardPropParams(extension.getParameters()))                                            .type(extension.getDataType())
                                             .value(extension.getValue())
                                             .build());
            }
        }
    }

    private void fillJSCardPropsFromJSCardExtensionsInVCard(VCard vcard, Card jsCard) throws CardException {

        String path = null;
        Object value;
        String extensionName;
        try {
            for (RawProperty extension : vcard.getExtendedProperties()) {
                if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.JSPROP.getValue())) {
                    path = extension.getParameter(VCardParamEnum.JSPTR.getValue());
                    value = JSContactPropUtils.toJsonValue(extension.getValue());
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

    private static void fillVCardUnmatchedProps(VCard vcard, Card jsCard) {

        jsCard.addVCardProp(VCardProp.builder()
                .name(V_Extension.toV_Extension(VCardPropEnum.VERSION.getValue().toLowerCase()))
                .type(VCardDataType.TEXT)
                .value(vcard.getVersion().getVersion())
                .build());

        if (vcard.getClientPidMaps()!=null) {
            for (ClientPidMap pidmap : vcard.getClientPidMaps())
                jsCard.addVCardProp(VCardProp.builder()
                                             .name(V_Extension.toV_Extension(VCardPropEnum.CLIENTPIDMAP.getValue().toLowerCase()))
                                             .parameters(VCardUtils.getVCardPropParams(pidmap.getParameters()))
                                             .type(VCardDataType.TEXT)
                                             .value(String.format("%d,%s",pidmap.getPid(), pidmap.getUri()))
                                             .build());
        }

        if (vcard.getXmls()!=null) {
                for (Xml xml : vcard.getXmls())
                    jsCard.addVCardProp(VCardProp.builder()
                                                 .name(V_Extension.toV_Extension(VCardPropEnum.XML.getValue().toLowerCase()))
                                                 .parameters(VCardUtils.getVCardPropParams(xml.getParameters()))
                                                 .type(VCardDataType.TEXT)
                                                 .value(xml.getValue().getTextContent())
                                                 .build());
        }

    }

    private static Calendar toJSCardUpdated(Revision rev) {

        if (rev == null)
            return null;

        return rev.getCalendar();
    }

    private static KindType toJSCardKind(ezvcard.property.Kind kind) {

        if (kind == null)
            return null;

        try {
            return KindType.builder().rfcValue(KindEnum.getEnum(getValue(kind))).build();
        } catch (IllegalArgumentException e) {
            return KindType.builder().extValue(V_Extension.toV_Extension(getValue(kind))).build();
        }
    }

    private Card convert(VCard vCard) throws CardException {

        Card jsCard;
        String uid;
        if (vCard.getUid()!=null)
            uid = vCard.getUid().getValue();
        else
            uid = UUID.randomUUID().toString();

        jsCard = Card.builder().uid(uid).build();
        jsCard.setKind(toJSCardKind(vCard.getKind()));
        jsCard.setProdId(getValue(vCard.getProductId()));
        jsCard.setUpdated(toJSCardUpdated(vCard.getRevision()));
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
        fillVCardUnmatchedProps(vCard, jsCard);
        fillJSCardPropsFromVCardExtensions(vCard, jsCard);
        fillJSCardPropsFromJSCardExtensionsInVCard(vCard, jsCard);

       return jsCard;
    }

    /**
     * Converts a list of vCard v4.0 instances [RFC6350] into a list of Card objects.
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     *
     * @param vCards a list of instances of the ez-vcard library VCard class
     * @return a list of Card objects
     * @throws CardException if one of the vCard instances is not v4.0 compliant
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<Card> convert(VCard... vCards) throws CardException {

        List<Card> jsCards = new ArrayList<>();

        for (VCard vCard : vCards) {
            if (config.isSetCardMustBeValidated()) {
                ValidationWarnings warnings = vCard.validate(VCardVersion.V4_0);
                if (!warnings.isEmpty())
                    throw new CardException(warnings.toString());
            }
            jsCards.add(convert(vCard));
        }

        return jsCards;
    }

}
