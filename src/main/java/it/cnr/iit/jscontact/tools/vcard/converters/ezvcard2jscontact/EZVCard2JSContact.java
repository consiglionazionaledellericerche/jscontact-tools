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
import ezvcard.VCardVersion;
import ezvcard.ValidationWarnings;
import ezvcard.parameter.Pid;
import ezvcard.parameter.RelatedType;
import ezvcard.parameter.VCardParameters;
import ezvcard.property.*;
import ezvcard.property.Organization;
import ezvcard.property.Title;
import ezvcard.util.GeoUri;
import ezvcard.util.PartialDate;
import ezvcard.util.UtcOffset;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.Anniversary;
import it.cnr.iit.jscontact.tools.dto.Note;
import it.cnr.iit.jscontact.tools.dto.TimeZone;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.VCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.*;
import it.cnr.iit.jscontact.tools.dto.wrappers.CategoryWrapper;
import it.cnr.iit.jscontact.tools.dto.wrappers.MemberWrapper;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactIdsProfile;
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
    private static final Integer HIGHEST_PREFERENCE = 0;

    protected VCard2JSContactConfig config;

    private int customTimeZoneCounter = 0;

    private final Map<String, TimeZone> timeZones = new HashMap<>();

    private static boolean isDefaultLanguage(String language, String defaultLanguage) {

        if (StringUtils.isEmpty(language))
            return false;

        return defaultLanguage != null && StringUtils.equals(language, defaultLanguage);

    }

    private List<String> getProfileIds(VCard2JSContactIdsProfile.IdType idType, Object... args) {

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
                        PersonalInformationType piType = (PersonalInformationType) args[0];
                        if (piId.getType() == piType)
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

    private String getId(VCard2JSContactIdsProfile.IdType idType, int index, String id, String propId, Object... args) {

        if (config.isSetUsePropIds() && propId != null)
            return propId;

        if (config.isSetAutoIdsProfile() || config.getIdsProfileToUse() == null || config.getIdsProfileToUse().getIds() == null || config.getIdsProfileToUse().getIds().size() == 0)
            return id;

        List<String> ids = (idType == VCard2JSContactIdsProfile.IdType.RESOURCE || idType == VCard2JSContactIdsProfile.IdType.PERSONAL_INFO) ? getProfileIds(idType,args[0]) : getProfileIds(idType);

        if (ids.size() == 0)
            return id;

        return (ids.get(index-1) == null) ? id : ids.get(index-1);

    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> E getEnumFromVCardType(Class<E> enumType, String vcardTypeParam, List<String> exclude, Map<String,E> aliases) {

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

    private static List<RawProperty> getRawProperties(VCard vcard, String propertyName) {

        List<RawProperty> rawProperties = new ArrayList<>();
        for (RawProperty extension : vcard.getExtendedProperties()) {
            if (extension.getPropertyName().equalsIgnoreCase(propertyName)) {
                rawProperties.add(extension);
            }
        }

        return rawProperties;
    }

    private static RawProperty getRawProperty(VCard vcard, String propertyName) {

        List<RawProperty> rawProperties = getRawProperties(vcard,propertyName);
        if (rawProperties.size()==0) return null;
        return rawProperties.get(0);
    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> List<E> getEnumValues(Class<E> enumType, String vcardTypeParam, List<String> exclude, Map<String, E> aliases) {

        if (vcardTypeParam == null)
            return null;

        List<E> enumValues = new ArrayList<>();
        String[] typeItems = vcardTypeParam.split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        for (String typeItem : typeItems) {
            try {
                E enumInstance = getEnumFromVCardType(enumType, typeItem, exclude, aliases);
                if (enumInstance != null)
                    enumValues.add(enumInstance);
            } catch (Exception e) {
                throw new InternalErrorException(e.getMessage());
            }
        }

        return (enumValues.size() > 0) ? enumValues : null;
    }

    private static Map<Context,Boolean> getContexts(String vcardTypeParam) {
        List<ContextEnum> enumValues = getEnumValues(ContextEnum.class,vcardTypeParam, null, ContextEnum.getAliases());
        if (enumValues == null) return null;

        Map<Context,Boolean> contexts = new HashMap<>();
        for (ContextEnum enumValue : enumValues) {
            contexts.put(Context.rfc(enumValue), Boolean.TRUE);
        }

        return contexts;
    }

    private static Map<AddressContext,Boolean> getAddressContexts(String vcardTypeParam) {
        List<AddressContextEnum> enumValues =  getEnumValues(AddressContextEnum.class,vcardTypeParam, null, AddressContextEnum.getAliases());
        if (enumValues == null) return null;

        Map<AddressContext,Boolean> contexts = new HashMap<>();
        for (AddressContextEnum enumValue : enumValues) {
            contexts.put(AddressContext.rfc(enumValue), Boolean.TRUE);
        }

        return contexts;
    }

    private static Map<PhoneFeature,Boolean> getPhoneFeatures(String vcardTypeParam) {
        List<PhoneFeatureEnum> enumValues = getEnumValues(PhoneFeatureEnum.class,vcardTypeParam, Arrays.asList("home", "work"), null);

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

    private Map<PhoneFeature,Boolean> getDefaultPhoneFeatures() {

        if (config.isSetVoiceAsDefaultPhoneFeature())
            return new HashMap<PhoneFeature,Boolean>(){{ put(PhoneFeature.voice(), Boolean.TRUE);}};

        return null;
    }

    private static String getLabel(String vcardTypeParam, String[] exclude, String[] include) {

        List<String> list = new ArrayList<>();

        if (include != null)
            list.addAll(Arrays.asList(include));

        if (vcardTypeParam == null)
            return (list.size()> 0) ? String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,list) : null;

        String[] items = vcardTypeParam.split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        for (String item : items) {
            if (item.equalsIgnoreCase("home")) item = "private";

            if (exclude != null && Arrays.asList(exclude).contains(item))
                continue;

            list.add(item.toLowerCase());
        }

        return (list.size()> 0) ? String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,list) : null;
    }

    private static HasAltid getAlternative(List<? extends HasAltid> list, String altid) {

        if (altid == null)
            return null;

        for (HasAltid al : list) {
            if (al.getAltid() != null && al.getAltid().equals(altid))
                return al;
        }

        return null;
    }

    private static void addLocalizedText(List<LocalizedText> list, LocalizedText localized) {

        if (list.size() == 0)
            list.add(localized);
        else {
            LocalizedText ls = (LocalizedText) getAlternative(list, localized.getAltid());
            if (ls == null)
                list.add(localized);
            else {
                ls.addLocalization(localized.getLanguage(), localized.getValue());
                list.set(list.indexOf(ls), ls);
            }
        }
    }

    private static Integer getPreference(String vcardPref) {
        return (vcardPref != null) ? Integer.parseInt(vcardPref) : null;
    }

    private static String getVCardParam(VCardParameters parameters, String param) {
        try {
            List<String> values = parameters.get(param);
            if (values.size()==0) return null;
            return String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, values);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private static String getAutoFulllAddress(ezvcard.property.Address addr) {

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



    private String getFulllAddress(String addressLabel, String autoFullAddress) {

        String fullAddress = addressLabel;
        if (fullAddress == null && config.isSetAutoFullAddress()) {
            fullAddress = autoFullAddress;
        }

        return fullAddress;
    }


    private void addResource(VCardProperty property, Card jsCard, ResourceType type, int index) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        String vcardType;
        String label;

        vcardType = getVCardParam(property.getParameters(), "TYPE");
        Map<Context,Boolean> contexts = getContexts(vcardType);
        label = getLabel(vcardType, (contexts != null) ? EnumUtils.toStrings(Context.toEnumValues(contexts.keySet())) : null, null);
        jsCard.addResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getMapTag(),index), property.getParameter(PROP_ID_PARAM), type),
                                    Resource.builder()
                                    .group(property.getGroup())
                                    .uri(value)
                                    .type(type)
                                    .label(label)
                                    .contexts(contexts)
                                    .mediaType(getMediaType(getVCardParam(property.getParameters(), "MEDIATYPE"), value))
                                    .pref(getPreference(getVCardParam(property.getParameters(), "PREF")))
                                    .build()
                           );

    }

    private static void addFile(String id, VCardProperty property, Card jsCard) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        jsCard.addPhoto(id, File.builder()
                                .group(property.getGroup())
                                .href(value)
                                .mediaType(getMediaType(getVCardParam(property.getParameters(), "MEDIATYPE"), value))
                                .pref(getPreference(getVCardParam(property.getParameters(), "PREF")))
                                .build()
                          );

    }


    private static String getValue(GeoUri geoUri) {
        return geoUri.toUri().toString();
    }

    private static it.cnr.iit.jscontact.tools.dto.Address getValue(PlaceProperty property) {

        if (property == null)
            return null;

        if (property.getText() != null && property.getGeoUri() == null)
            return it.cnr.iit.jscontact.tools.dto.Address.builder()
                    .fullAddress(property.getText())
                    .build();
        else if (property.getGeoUri() != null)
            return it.cnr.iit.jscontact.tools.dto.Address.builder()
                                                         .coordinates(getValue(property.getGeoUri()))
                                                         .build();
        else
            return null;
    }


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
                timeZones.put(timeZoneName,TimeZone.builder()
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

    private static String getValue(TextListProperty property) {

        return StringUtils.join(property.getValues(), DelimiterUtils.SEMICOMMA_ARRAY_DELIMITER);
    }

    private static String getValue(Impp property) {

        return property.getUri().toString();
    }

    private void addPropertyGroups(Map<String, ? extends GroupableObject> map, String keyPrefix, Card jsCard) {

        if (map == null)
            return;

        for (Map.Entry<String,? extends GroupableObject> entry : map.entrySet()) {
            if (entry.getValue().getGroup() != null)
                jsCard.addPropertyGroup(entry.getValue().getGroup(),keyPrefix + entry.getKey());
        }
    }

    private void fillSpeakToAsWithGender(VCard vCard, Card jsCard) {

        if (vCard.getGender() == null)
            return;

        if (vCard.getExtendedProperty("GRAMMATICAL-GENDER") != null)
            return;

        if (!config.isConvertGenderToSpeakToAs())
            return;

        if (vCard.getGender() != null) {
            if (vCard.getGender().isMale())
                jsCard.setSpeakToAs(SpeakToAs.male());
            else if (vCard.getGender().isFemale())
                jsCard.setSpeakToAs(SpeakToAs.female());
            else if (vCard.getGender().isOther())
                jsCard.setSpeakToAs(SpeakToAs.animate());
            else if (vCard.getGender().isNone())
                jsCard.setSpeakToAs(SpeakToAs.neuter());
            else if (vCard.getGender().isUnknown())
                return;

            if (vCard.getGender().getText() != null)
                jsCard.addExtension(getUnmatchedPropertyName(VCARD_GENDER_TAG), vCard.getGender().getText());
        } else {
            jsCard.setSpeakToAs(SpeakToAs.builder().grammaticalGender(GrammaticalGenderType.getEnum(vCard.getExtendedProperty("GRAMMATICAL-GENDER").getValue().toLowerCase())).build());
        }
    }

    private static void fillFormattedNames(VCard vcard, Card jsCard) {

        List<FormattedName> fns = vcard.getFormattedNames();
        List<LocalizedText> fullNames = new ArrayList<>();
        for (FormattedName fn : fns) {
            fullNames.add(LocalizedText.builder()
                                         .value(getValue(fn))
                                         .language(fn.getLanguage())
                                         .preference(isDefaultLanguage(fn.getLanguage(), jsCard.getLocale()) ? HIGHEST_PREFERENCE : fn.getPref())
                                         .build()
                         );
        }
        Collections.sort(fullNames);
        for (LocalizedText ls : fullNames) {
            if (fullNames.indexOf(ls) == 0)
                jsCard.setFullName(ls.getValue());
            else
                jsCard.addLocalization(ls.getLanguage(), "fullName", JsonNodeUtils.textNode(ls.getValue()));
        }
    }

    private static void fillMembers(VCard vcard, CardGroup jsCardGroup) {

        List<MemberWrapper> wrappers = new ArrayList<>();
        for (Member member : vcard.getMembers()) {
            wrappers.add(MemberWrapper.builder()
                                      .value(getValue(member))
                                      .preference(member.getPref())
                                      .build()
                        );
        }
        Collections.sort(wrappers);
        for (MemberWrapper wrapper : wrappers)
            jsCardGroup.addMember(wrapper.getValue());

    }

    private static void fillNames(VCard vcard, Card jsCard) {

        List<StructuredName> sns = vcard.getStructuredNames();
        if (sns.size() > 0) {
            NameComponent[] components = null;
            for (StructuredName sn : sns) {
                for (String px : sn.getPrefixes())
                    components = Name.addComponent(components, NameComponent.prefix(px));
                if (sn.getGiven() != null)
                    components = Name.addComponent(components,NameComponent.personal(sn.getGiven()));
                if (sn.getFamily() != null)
                    components = Name.addComponent(components,NameComponent.surname(sn.getFamily()));
                for (String an : sn.getAdditionalNames())
                    components = Name.addComponent(components,NameComponent.additional(an));
                for (String sx : sn.getSuffixes())
                    components = Name.addComponent(components,NameComponent.suffix(sx));
                jsCard.setName(Name.builder().group(sn.getGroup()).components(components).build());
                addUnmatchedParams(sn, "N", null, new String[]{"SORT-AS"}, jsCard);

                if (sn.getGroup() != null)
                    jsCard.addPropertyGroup(sn.getGroup(),"name");
            }
        }
    }

    private void fillNickNames(VCard vcard, Card jsCard) {

        List<Nickname> nicknames = vcard.getNicknames();
        List<LocalizedText> nicks = new ArrayList<>();
        for (Nickname nickname : nicknames) {
            String vcardType = getVCardParam(nickname.getParameters(), "TYPE");
            Map<Context, Boolean> contexts = getContexts(vcardType);
            addLocalizedText(nicks, LocalizedText.builder()
                    .value(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,nickname.getValues()))
                    .language(nickname.getLanguage())
                    .altid(nickname.getAltId())
                    .preference(nickname.getPref())
                    .contexts(contexts)
                    .build());
        }
        int i = 1;
        for (LocalizedText nick : nicks) {
            String id = getId(VCard2JSContactIdsProfile.IdType.NICKNAME, i, "NICK-" + (i ++), nick.getPropId());
            NickName nickName = NickName.builder().name(nick.getValue()).pref(nick.getPreference()).contexts(nick.getContexts()).build();
            jsCard.addNickName(id, nickName);
            if (nick.getLocalizations() != null) {
                for (Map.Entry<String, String> localization : nick.getLocalizations().entrySet()) {
                    jsCard.addLocalization(localization.getKey(), "nickNames/" + id, mapper.convertValue(it.cnr.iit.jscontact.tools.dto.NickName.builder().name(localization.getValue()).build(), JsonNode.class));
                }
            }
        }
        addPropertyGroups(jsCard.getNickNames(), "nickNames/", jsCard);
    }

    private static it.cnr.iit.jscontact.tools.dto.Address getAddressAltrenative(List<it.cnr.iit.jscontact.tools.dto.Address> addresses, String altid) {

        it.cnr.iit.jscontact.tools.dto.Address address = (it.cnr.iit.jscontact.tools.dto.Address) getAlternative(addresses, altid);
        return (address != null) ? address : addresses.get(0);
    }

    private String getTimezoneName(String vcardTzParam) {

        if (vcardTzParam == null)
            return null;

        Matcher m = TIMEZONE_AS_UTC_OFFSET_PATTERN.matcher(vcardTzParam);
        if (m.find())
            return getValue(new Timezone(UtcOffset.parse(vcardTzParam.replace(":", StringUtils.EMPTY))));
        else
            return vcardTzParam;
    }

    private static String getGeoUri(GeoUri vcardGeoParam) {

        return  (vcardGeoParam != null) ? vcardGeoParam.toUri().toString() : null;
    }

    private void fillAddresses(VCard vcard, Card jsCard) {

        List<it.cnr.iit.jscontact.tools.dto.Address> addresses = new ArrayList<>();

        String tz;
        String geo;
        for (ezvcard.property.Address addr : vcard.getAddresses()) {

            String vcardType = getVCardParam(addr.getParameters(), "TYPE");
            tz = getTimezoneName(addr.getTimezone());
            geo = getGeoUri(addr.getGeo());
            String cc = addr.getParameter("CC");

            List<StreetComponent> streetDetailPairs = new ArrayList<>();
            if (StringUtils.isNotEmpty(addr.getPoBox()))
                streetDetailPairs.add(StreetComponent.postOfficeBox(addr.getPoBox()));
            if (StringUtils.isNotEmpty(addr.getExtendedAddressFull()))
                streetDetailPairs.add(StreetComponent.extension(addr.getExtendedAddressFull()));
            if (StringUtils.isNotEmpty(addr.getStreetAddressFull()))
                streetDetailPairs.add(StreetComponent.name(addr.getStreetAddressFull()));

            String autoFullAddress = getAutoFulllAddress(addr);
            addresses.add(it.cnr.iit.jscontact.tools.dto.Address.builder()
                                                                 .hash(autoFullAddress)
                                                                 .group(addr.getGroup())
                                                                 .propId(addr.getParameter(PROP_ID_PARAM))
                                                                 .contexts(getAddressContexts(vcardType))
                                                                 .fullAddress(getFulllAddress(addr.getLabel(),autoFullAddress))
                                                                 .pref(addr.getPref())
                                                                 .coordinates(geo)
                                                                 .timeZone(tz)
                                                                 .countryCode(cc)
                                                                 .street((streetDetailPairs.size() > 0) ? streetDetailPairs.toArray(new StreetComponent[streetDetailPairs.size()]) : null)
                                                                 .locality(StringUtils.defaultIfEmpty(addr.getLocality(), null))
                                                                 .region(StringUtils.defaultIfEmpty(addr.getRegion(), null))
                                                                 .postcode(StringUtils.defaultIfEmpty(addr.getPostalCode(), null))
                                                                 .country(StringUtils.defaultIfEmpty(addr.getCountry(), null))
                                                                 .altid(addr.getAltId())
                                                                 .language(addr.getLanguage())
                                                                 .isDefaultLanguage(isDefaultLanguage(addr.getLanguage(), jsCard.getLocale()))
                                                                 .build()
            );
        }

        if (vcard.getTimezone() != null) {
            tz = getValue(vcard.getTimezone());
            //update the timezone of first address
            it.cnr.iit.jscontact.tools.dto.Address address = getAddressAltrenative(addresses, vcard.getTimezone().getAltId());
            address.setTimeZone(tz);
            addresses.set(addresses.indexOf(address), address);
        }

        if (vcard.getGeo() != null) {
            geo = getValue(vcard.getGeo().getGeoUri());
            //update the coordinates of first address
            it.cnr.iit.jscontact.tools.dto.Address address = getAddressAltrenative(addresses, vcard.getGeo().getAltId());
            address.setCoordinates(geo);
            addresses.set(addresses.indexOf(address), address);
        }

        if (addresses.size()==0)
            return;

        Collections.sort(addresses);

        String id;
        String altId = null;
        int i = 0;
        for (Address address: addresses) {
            if (altId!=null && address.getAltid()!=null && altId.equals(address.getAltid())) {
                id = getId(VCard2JSContactIdsProfile.IdType.ADDRESS, i, "ADR-" + i, address.getPropId() );
                jsCard.addLocalization(address.getLanguage(),"addresses/" + id,  mapper.convertValue(address, JsonNode.class));
            }
            else {
                i++;
                id = getId(VCard2JSContactIdsProfile.IdType.ADDRESS, i, "ADR-" + i, address.getPropId());
                jsCard.addAddress(id, address);
                altId = address.getAltid();
            }
        }

        addPropertyGroups(jsCard.getAddresses(), "addresses/", jsCard);

    }

    private static  <T extends DateOrTimeProperty> AnniversaryDate getAnniversaryDate(T date) {

        try {
            if (date.getDate() != null)
                return AnniversaryDate.builder().date(date.getCalendar()).build();
            if (date.getPartialDate() != null) {
                PartialDate pd;
                if (date.getPartialDate().hasTimeComponent())
                    pd = PartialDate.builder().year(date.getPartialDate().getYear())
                                                          .month(date.getPartialDate().getMonth())
                                                          .date(date.getPartialDate().getDate())
                                                          .build();
                else
                    pd = date.getPartialDate();
                return AnniversaryDate.builder().partialDate(pd).build();
            }
            if (date.getText() != null)
                return null;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

        return null;
    }


    private void fillAnniversaries(VCard vcard, Card jsCard) {

        int i = 1;
      if (vcard.getBirthday() != null) {
          jsCard.addAnniversary(getId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i ++), vcard.getBirthday().getParameter(PROP_ID_PARAM)),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .group(vcard.getBirthday().getGroup())
                                                                             .type(AnniversaryType.birth())
                                                                             .date(getAnniversaryDate(vcard.getBirthday()))
                                                                             .place(getValue(vcard.getBirthplace()))
                                                                             .build()
                                  );
          if (vcard.getBirthday().getCalscale()!= null && !vcard.getBirthday().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getBirthday(), "BDAY", null, new String[]{"CALSCALE"}, jsCard);
      }

      if (vcard.getDeathdate() != null) {
          jsCard.addAnniversary(getId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i ++), vcard.getDeathdate().getParameter(PROP_ID_PARAM)),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .group(vcard.getDeathdate().getGroup())
                                                                             .type(AnniversaryType.death())
                                                                             .date(getAnniversaryDate(vcard.getDeathdate()))
                                                                             .place(getValue(vcard.getDeathplace()))
                                                                             .build()
                                  );
          if (vcard.getDeathdate().getCalscale()!= null && !vcard.getDeathdate().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getDeathdate(), "DEATHDATE", null, new String[]{"CALSCALE"}, jsCard);
      }

      if (vcard.getAnniversary() != null) {
          jsCard.addAnniversary(getId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + i, vcard.getAnniversary().getParameter(PROP_ID_PARAM)),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                              .group(vcard.getAnniversary().getGroup())
                                                                              .date(getAnniversaryDate(vcard.getAnniversary()))
                                                                              .label(Anniversary.ANNIVERSAY_MARRIAGE_LABEL)
                                                                              .build()
                                   );
          if (vcard.getAnniversary().getCalscale()!= null && !vcard.getAnniversary().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getAnniversary(), "ANNIVERSARY", null, new String[]{"CALSCALE"}, jsCard);
      }

      addPropertyGroups(jsCard.getAnniversaries(), "anniversaries/", jsCard);

    }

    private static PersonalInformationLevel getLevel(String vcardLevelParam) throws CardException {
        try {
            return PersonalInformationLevel.getEnum(vcardLevelParam);
        } catch (IllegalArgumentException e) {
            throw new CardException("Unknown LEVEL value " + vcardLevelParam);
        }
    }

    private void fillPersonalInfos(VCard vcard, Card jsCard) throws CardException {

        List<PersonalInformation> hobbies = new ArrayList<>();
        List<PersonalInformation> interests = new ArrayList<>();
        List<PersonalInformation> expertizes = new ArrayList<>();

        for (Hobby hobby : vcard.getHobbies()) {
            hobbies.add(PersonalInformation.builder()
                                            .group(hobby.getGroup())
                                            .propId(hobby.getParameter(PROP_ID_PARAM))
                                            .type(PersonalInformationType.HOBBY)
                                            .value(getValue(hobby))
                                            .level((hobby.getLevel() != null) ? getLevel(hobby.getLevel().getValue()) : null)
                                            .index(hobby.getIndex())
                                            .build()
                       );
        }

        int j = 0;
        if (hobbies.size() > 0) {
            Collections.sort(hobbies);
            int i = 1;
            for (PersonalInformation pi : hobbies)
                jsCard.addPersonalInfo(getId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "HOBBY-" + (i++), pi.getPropId(), PersonalInformationType.HOBBY), pi);
        }

        for (Interest interest : vcard.getInterests()) {
            interests.add(PersonalInformation.builder()
                                             .group(interest.getGroup())
                                             .propId(interest.getParameter(PROP_ID_PARAM))
                                             .type(PersonalInformationType.INTEREST)
                                             .value(getValue(interest))
                                             .level((interest.getLevel() != null) ? getLevel(interest.getLevel().getValue()) : null)
                                             .index(interest.getIndex())
                                             .build()
                          );
        }

        if (interests.size() > 0) {
            Collections.sort(interests);
            int i = 1;
            for (PersonalInformation pi : interests)
                jsCard.addPersonalInfo(getId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "INTEREST-" + (i++), pi.getPropId(), PersonalInformationType.INTEREST), pi);
        }

        for (Expertise expertise : vcard.getExpertise()) {
            expertizes.add(PersonalInformation.builder()
                                              .group(expertise.getGroup())
                                              .propId(expertise.getParameter(PROP_ID_PARAM))
                                              .type(PersonalInformationType.EXPERTISE)
                                              .value(getValue(expertise))
                                              .level((expertise.getLevel() != null) ? getLevel(expertise.getLevel().getValue()) : null)
                                              .index(expertise.getIndex())
                                              .build()
                           );
        }

        if (expertizes.size() > 0) {
            Collections.sort(expertizes);
            int i = 1;
            for (PersonalInformation pi : expertizes)
                jsCard.addPersonalInfo(getId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "EXPERTISE-" + (i++), pi.getPropId(), PersonalInformationType.EXPERTISE), pi);
        }

        addPropertyGroups(jsCard.getPersonalInfo(), "personalInfo/", jsCard);

    }

    private static void fillContactLanguages(VCard vcard, Card jsCard) {

        for (Language lang : vcard.getLanguages()) {
            String vcardType = getVCardParam(lang.getParameters(), "TYPE");
            if (vcardType!=null || lang.getPref()!=null)
                jsCard.addContactLanguage(getValue(lang),
                                            ContactLanguage.builder()
                                                           .group(lang.getGroup())
                                                           .contexts(getContexts(vcardType))
                                                            .pref(lang.getPref())
                                                           .build()
                                            );
            else
                jsCard.addContactLanguage(getValue(lang),null);
        }

        if (jsCard.getPreferredContactLanguages() == null)
            return;

        for (Map.Entry<String,ContactLanguage[]> entry : jsCard.getPreferredContactLanguages().entrySet()) {
            int i = 0;
            for (ContactLanguage language : entry.getValue()) {
                if (language.getGroup() != null)
                    jsCard.addPropertyGroup(language.getGroup(), "preferredContactLanguages/" + entry.getKey() + "/" + i);
                i++;
            }
        }

    }


    private static boolean labelIncludesTelTypes(String label) {

        return LabelUtils.labelIncludesAnyItem(label, Arrays.asList("voice", "textphone", "fax", "pager", "cell", "text", "video"));
    }

    private static String getResourceExt(String resource) {

        int index = resource.lastIndexOf('.');
        return (index > 0) ? resource.substring(index + 1) : null;
    }

    private static String getMediaType(String mediaTypeParamValue, String resource) {

        if (mediaTypeParamValue != null)
            return mediaTypeParamValue;

        String ext = getResourceExt(resource);
        return (ext != null) ? MimeTypeUtils.lookupMimeType(ext) : null;
    }

    private void fillPhones(VCard vcard, Card jsCard) {

        int i = 1;
        for (Telephone tel : vcard.getTelephoneNumbers()) {
            String vcardType = getVCardParam(tel.getParameters(), "TYPE");
            Map<Context,Boolean> contexts = getContexts(vcardType);
            Map<PhoneFeature,Boolean> phoneFeatures = getPhoneFeatures(vcardType);
            String[] exclude = null;
            if (contexts != null) exclude = ArrayUtils.addAll(null, EnumUtils.toStrings(Context.toEnumValues(contexts.keySet())));
            if (phoneFeatures != null) exclude = ArrayUtils.addAll(exclude, EnumUtils.toStrings(PhoneFeature.toEnumValues(phoneFeatures.keySet())));
            String label = getLabel(vcardType, exclude, null);
            jsCard.addPhone(getId(VCard2JSContactIdsProfile.IdType.PHONE, i,"PHONE-" + (i++), tel.getParameter(PROP_ID_PARAM)), Phone.builder()
                                       .group(tel.getGroup())
                                       .phone(getValue(tel))
                                       .features((phoneFeatures == null && !labelIncludesTelTypes(label)) ? getDefaultPhoneFeatures() : phoneFeatures)
                                       .contexts(contexts)
                                       .label(label)
                                       .pref(tel.getPref())
                                       .build()
                              );
        }

        addPropertyGroups(jsCard.getPhones(), "phones/", jsCard);

    }

    private void fillEmails(VCard vcard, Card jsCard) {

        int i = 1;
        for (Email email : vcard.getEmails()) {
            String emailAddress = getValue(email);
            if (StringUtils.isNotEmpty(emailAddress)) {
                String vcardType = getVCardParam(email.getParameters(), "TYPE");
                jsCard.addEmail(getId(VCard2JSContactIdsProfile.IdType.EMAIL, i, "EMAIL-" + (i++), email.getParameter(PROP_ID_PARAM)), EmailAddress.builder()
                         .group(email.getGroup())
                        .email(emailAddress)
                        .contexts(getContexts(vcardType))
                        .pref(email.getPref())
                        .build()
                );
            }
        }

        addPropertyGroups(jsCard.getEmails(), "emails/", jsCard);

    }

    private void fillPhotos(VCard vcard, Card jsCard) {

        int i = 1;
        for (Photo photo : vcard.getPhotos())
            addFile(getId(VCard2JSContactIdsProfile.IdType.PHOTO, i, "PHOTO-" + (i++), photo.getParameter(PROP_ID_PARAM)), photo, jsCard);

        addPropertyGroups(jsCard.getPhotos(), "photos/", jsCard);

    }

    private void fillOnlineServices(VCard vcard, Card jsCard) {

        String vcardType;
        Map<Context,Boolean> contexts;

        int i = 1;
        for (Impp impp : vcard.getImpps()) {
            vcardType = getVCardParam(impp.getParameters(), "TYPE");
            contexts = getContexts(vcardType);
            jsCard.addOnlineService(getId(VCard2JSContactIdsProfile.IdType.ONLINE_SERVICE, i,"OS-" + (i++), impp.getParameter(PROP_ID_PARAM)), OnlineService.builder()
                    .uri(getValue(impp))
                    .contexts(contexts)
                    .label(getLabel(vcardType, (contexts != null) ? EnumUtils.toStrings(Context.toEnumValues(contexts.keySet())) : null, null))
                    .pref(impp.getPref())
                    .build()
            );
        }

        addPropertyGroups(jsCard.getOnlineServices(), "onlineServices/", jsCard);

    }


    private void fillResources(VCard vcard, Card jsCard) {

        String vcardType;
        Map<Context,Boolean> contexts;

        int i = 1;
        for (Source source : vcard.getSources())
            addResource(source, jsCard, ResourceType.SOURCE, i++);

        i = 1;
        for (Logo logo : vcard.getLogos())
            addResource(logo, jsCard, ResourceType.LOGO, i++);

        i = 1;
        for (Sound sound : vcard.getSounds())
            addResource(sound, jsCard, ResourceType.SOUND, i++);

        i = 1;
        for (Url url : vcard.getUrls())
            addResource(url, jsCard, ResourceType.URI, i++);

        i = 1;
        for (Key key : vcard.getKeys())
            addResource(key, jsCard, ResourceType.KEY, i++);

        i = 1;
        for (FreeBusyUrl fburl : vcard.getFbUrls())
            addResource(fburl, jsCard, ResourceType.FBURL, i++);

        for (CalendarRequestUri calendarRequestUri : vcard.getCalendarRequestUris()) {
            Map<String,String> sendTo = new HashMap<String, String>() {{ put("imip", calendarRequestUri.getValue()); }};
            jsCard.addScheduling(getId(VCard2JSContactIdsProfile.IdType.SCHEDULING, i, "CALADRURI-" + (i++), calendarRequestUri.getParameter(PROP_ID_PARAM)), Scheduling.builder().group(calendarRequestUri.getGroup()).sendTo(sendTo).pref(calendarRequestUri.getPref()).build());
        }

        i = 1;
        for (CalendarUri calendarUri : vcard.getCalendarUris())
            addResource(calendarUri, jsCard, ResourceType.CALURI, i++);

        List<Resource> orgDirectories = new ArrayList<>();
        for (OrgDirectory od : vcard.getOrgDirectories()) {
            vcardType = od.getType();
            contexts = getContexts(vcardType);
            orgDirectories.add(Resource.builder()
                                       .group(od.getGroup())
                                       .propId(od.getParameter(PROP_ID_PARAM))
                                       .uri(getValue(od))
                                       .type(ResourceType.ORG_DIRECTORY)
                                       .label(getLabel(vcardType, (contexts != null) ? EnumUtils.toStrings(Context.toEnumValues(contexts.keySet())) : null, null))
                                       .contexts(contexts)
                                       .pref(od.getPref())
                                       .index(od.getIndex())
                                       .build()
                              );
        }

        if (orgDirectories.size() > 0) {
            Collections.sort(orgDirectories);
            i = 1;
            for (Resource ol : orgDirectories)
                jsCard.addResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, i, "ORG-DIRECTORY-" + (i++), ol.getPropId(), ResourceType.ORG_DIRECTORY), ol);
        }

        List<RawProperty> contactUris = getRawProperties(vcard, "CONTACT-URI");
        i = 1;
        for (RawProperty contactUri : contactUris) {
            UriProperty uriProperty = new UriProperty(getValue(contactUri));
            uriProperty.setParameters(contactUri.getParameters());
            addResource(uriProperty, jsCard, ResourceType.CONTACT_URI, i++);
        }

        addPropertyGroups(jsCard.getScheduling(), "scheduling/", jsCard);
        addPropertyGroups(jsCard.getResources(), "resources/", jsCard);
    }

    private void fillTitles(List<LocalizedText> localizedStrings, Card jsCard, TitleType type, int i) {

        for (LocalizedText localizedString : localizedStrings) {
            String id = getId(VCard2JSContactIdsProfile.IdType.TITLE, i, "TITLE-" + (i ++), localizedString.getPropId());
            jsCard.addTitle(id, it.cnr.iit.jscontact.tools.dto.Title.builder().title(localizedString.getValue()).type(type).build());
            if (localizedString.getLocalizations()!=null) {
                for (Map.Entry<String,String> localization : localizedString.getLocalizations().entrySet())
                    jsCard.addLocalization(localization.getKey(), "titles/" + id, mapper.convertValue(it.cnr.iit.jscontact.tools.dto.Title.builder().title(localization.getValue()).type(type).build(), JsonNode.class));
            }
        }
    }

    private void fillTitles(VCard vcard, Card jsCard) {

        List<LocalizedText> titles = new ArrayList<>();
        for (Title title : vcard.getTitles())
            addLocalizedText(titles, LocalizedText.builder()
                                                     .value(getValue(title))
                                                     .language(title.getLanguage())
                                                     .altid(title.getAltId())
                                                     .preference(title.getPref())
                                                     .build()
                              );
        Collections.sort(titles);
        fillTitles(titles, jsCard, TitleType.title(), 1);
    }

    private void fillRoles(VCard vcard, Card jsCard) {

        List<LocalizedText> roles = new ArrayList<>();
        for (Role role : vcard.getRoles()) {
            addLocalizedText(roles, LocalizedText.builder()
                                                     .value(getValue(role))
                                                     .language(role.getLanguage())
                                                     .altid(role.getAltId())
                                                     .preference(role.getPref())
                                                     .build()
                              );
        }
        Collections.sort(roles);
        fillTitles(roles, jsCard, TitleType.role(), (jsCard.getTitles() != null) ? jsCard.getTitles().size() + 1 : 1);

        addPropertyGroups(jsCard.getTitles(), "titles/", jsCard);
    }

    private void fillOrganizations(VCard vcard, Card jsCard) {

        List<LocalizedText> organizations = new ArrayList<>();
        for (Organization org : vcard.getOrganizations()) {
            addLocalizedText(organizations, LocalizedText.builder()
                                                             .group(org.getGroup())
                                                             .propId(org.getParameter(PROP_ID_PARAM))
                                                             .value(getValue(org))
                                                             .language(org.getLanguage())
                                                             .altid(org.getAltId())
                                                             .preference(org.getPref())
                                                             .build()
                              );
        }
        Collections.sort(organizations);

        int i = 1;
        for (LocalizedText organization : organizations) {
            String[] nameItems = organization.getValue().split(DelimiterUtils.SEMICOMMA_ARRAY_DELIMITER);
            String id = getId(VCard2JSContactIdsProfile.IdType.ORGANIZATION, i, "ORG-" + (i ++), organization.getPropId());
            List<String> units = (nameItems.length > 1 ) ? Arrays.asList(nameItems).subList(1,nameItems.length) : null;
            jsCard.addOrganization(id, it.cnr.iit.jscontact.tools.dto.Organization.builder().group(organization.getGroup()).name((!nameItems[0].isEmpty()) ? nameItems[0] : null).units((units!=null)? units.toArray(new String[0]) : null).build());
            if (organization.getLocalizations()!=null) {
                for (Map.Entry<String,String> localization : organization.getLocalizations().entrySet()) {
                    String[] localizedNameItems =  localization.getValue().split(DelimiterUtils.SEMICOMMA_ARRAY_DELIMITER);
                    List<String> localizedUnits = (localizedNameItems.length > 1 ) ? Arrays.asList(localizedNameItems).subList(1,localizedNameItems.length) : null;
                    jsCard.addLocalization(localization.getKey(), "organizations/" + id, mapper.convertValue(it.cnr.iit.jscontact.tools.dto.Organization.builder().name((!localizedNameItems[0].isEmpty()) ? localizedNameItems[0] : null).units((localizedUnits!=null)? localizedUnits.toArray(new String[0]) : null).build(), JsonNode.class));
                }
            }
        }

        addPropertyGroups(jsCard.getOrganizations(), "organizations/", jsCard);

    }

    private static void fillNotes(VCard vcard, Card jsCard) {

        List<LocalizedText> notes = new ArrayList<>();
        for (ezvcard.property.Note note : vcard.getNotes())
            jsCard.addNote(Note.builder().note(note.getValue()).language(note.getLanguage()).build());
    }

    private static void fillCategories(VCard vcard, Card jsCard) {

        List<CategoryWrapper> wrappers = new ArrayList<>();
        for (Categories categories : vcard.getCategoriesList()) {
            wrappers.add(CategoryWrapper.builder()
                                        .values(categories.getValues())
                                        .preference(categories.getPref())
                                        .build()
                        );
        }
        for (CategoryWrapper wrapper : wrappers)
            jsCard.addKeywords(wrapper.getValues().toArray(new String[wrapper.getValues().size()]));
    }

    private static String getValue(List<RelatedType> list) {
        StringJoiner joiner = new StringJoiner(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        for (RelatedType el : list)
            joiner.add(el.getValue());
        return joiner.toString();
    }

    private static void fillRelations(VCard vcard, Card jsCard) {

        for (Related related : vcard.getRelations()) {
            if (related.getTypes().isEmpty())
                jsCard.addRelation(getValue(related), null);
            else {
                for (RelatedType type : related.getTypes())
                    jsCard.addRelation(getValue(related), RelationType.rfcRelation(RelationEnum.getEnum(type.getValue())));
            }
        }
    }

    //TODO: replace XXXX with RFC number after draft-ietf-calext-vcard-jscontact-extensions
    private void fillRFCXXXXProperties(VCard vcard, Card jsCard) {

        int i = 1;
        for (RawProperty extension : vcard.getExtendedProperties()) {

            String language = extension.getParameter("LANGUAGE");
            String vcardPref = extension.getParameter("PREF");
            Integer pref;
            try {
                pref = Integer.parseInt(vcardPref);
            } catch (Exception e) {
                pref = null;
            }
            String vcardType = extension.getParameter("TYPE");
            Map<Context,Boolean> contexts = getContexts(vcardType);
            String jsonPointer = fakeExtensionsMapping.get(extension.getPropertyName().toLowerCase());

            if (extension.getPropertyName().equalsIgnoreCase("LOCALE"))
                jsCard.setLocale(extension.getValue());
            else if (extension.getPropertyName().equalsIgnoreCase("CREATED"))
                jsCard.setCreated(DateUtils.toCalendar(extension.getValue()));
            else if (extension.getPropertyName().equalsIgnoreCase("GRAMMATICAL-GENDER")) {
                GrammaticalGenderType gender = GrammaticalGenderType.getEnum(extension.getValue().toLowerCase());
                if (jsCard.getSpeakToAs() != null)
                    jsCard.getSpeakToAs().setGrammaticalGender(gender);
                else
                    jsCard.setSpeakToAs(SpeakToAs.builder().grammaticalGender(gender).build());
            }
            else if (extension.getPropertyName().equalsIgnoreCase("PRONOUNS")) {
                String id = getId(VCard2JSContactIdsProfile.IdType.PRONOUNS, i,"PRONOUNS-" + (i++), extension.getParameter(PROP_ID_PARAM));
                Pronouns pronouns = Pronouns.builder().pronouns(extension.getValue()).contexts(contexts).pref(pref).build();
                jsonPointer=String.format("%s/%s", jsonPointer, id);
                if (language==null || config.getDefaultLanguage().equalsIgnoreCase(language)) {
                    if (jsCard.getSpeakToAs() != null) {
                        jsCard.getSpeakToAs().addPronouns(id, pronouns);
                    }
                    else {
                        jsCard.setSpeakToAs(SpeakToAs.builder().pronouns(new HashMap<String,Pronouns>(){{ put(id,pronouns); }}).build());
                    }
                } else {
                    jsCard.addLocalization(language,jsonPointer, mapper.convertValue(pronouns, JsonNode.class));
                }
            }
            else if (extension.getPropertyName().equalsIgnoreCase("CONTACT-CHANNEL-PREF")) {
                if (!extension.getValue().isEmpty()) {
                    ChannelType channelType;
                    try {
                        channelType = ChannelType.rfc(ChannelEnum.getEnum(extension.getValue().toLowerCase()));
                    } catch (Exception e) {
                        channelType = ChannelType.ext(extension.getValue().toLowerCase());
                    }
                    if (contexts!=null || pref != null)
                        jsCard.addContactChannel(channelType, ContactChannelPreference.builder().contexts(contexts).pref(pref).build());
                    else
                        jsCard.addContactChannel(channelType, null);
                }
            }

            if (extension.getGroup() != null)
                jsCard.addPropertyGroup(extension.getGroup(),jsonPointer);
        }
    }

    private String getExtPropertyName(String propertyName) {

        return (config.getExtensionsPrefix() != null) ? config.getExtensionsPrefix() + propertyName : propertyName;
    }

    private String getExtParamName(String propertyName, String paramName) {

        return getExtPropertyName(propertyName) + "/" + paramName;
    }

    private void fillExtensions(VCard vcard, Card jsCard) {

        for (RawProperty extension : vcard.getExtendedProperties()) {
            if (!fakeExtensionsMapping.containsKey(extension.getPropertyName()) &&
                    !fakeExtensionsMapping.containsKey(extension.getPropertyName().toLowerCase())) {
                String propertyName = getExtPropertyName(extension.getPropertyName());
                jsCard.addExtension(propertyName, getValue(extension));
                if (extension.getGroup() != null)
                    jsCard.addPropertyGroup(extension.getGroup(),propertyName);
            }
        }
    }


    private static void addUnmatchedParams(VCardProperty property, String propertyName, Integer index, String[] unmatchedParams, Card jsCard) {

        if (unmatchedParams == null)
            return;

        if (property.getParameters() == null)
            return;

        if (property.getParameters().getPids() != null && property.getParameters().getPids().size()>0) {
            StringJoiner joiner = new StringJoiner(DelimiterUtils.COMMA_ARRAY_DELIMITER);
            for (Pid pid : property.getParameters().getPids())
                joiner.add(pid.getLocalId().toString());
            jsCard.addExtension(getUnmatchedParamName(propertyName,"PID"),
                                   joiner.toString()
                                  );
        }

        for (String param : unmatchedParams) {
            if (property.getParameter(param) != null)
                jsCard.addExtension(getUnmatchedParamName(propertyName,param),
                                       property.getParameter(param)
                                      );
        }
    }

    private static void fillUnmatchedElments(VCard vcard, Card jsCard) {

        if (vcard.getClientPidMaps()!=null) {
            for (ClientPidMap pidmap : vcard.getClientPidMaps())
                jsCard.addExtension(getUnmatchedPropertyName(VCARD_CLIENTPIDMAP_TAG, pidmap.getPid()), pidmap.getUri());
        }

        if (vcard.getXmls()!=null) {
            if (vcard.getXmls().size() == 1)
                jsCard.addExtension(getUnmatchedPropertyName(VCARD_XML_TAG), vcard.getXmls().get(0).getValue().getTextContent());
            else {
                int i = 0;
                for (Xml xml : vcard.getXmls())
                    jsCard.addExtension(getUnmatchedPropertyName(String.format("XML/%d", i++)), xml.getValue().getTextContent());
            }
        }

    }

    private static Calendar getUpdated(Revision rev) {

        if (rev == null)
            return null;

        return rev.getCalendar();
    }

    private static KindType getKind(ezvcard.property.Kind kind) {

        if (kind == null)
            return null;

        try {
            return KindType.builder().rfcValue(KindEnum.getEnum(getValue(kind))).build();
        } catch (IllegalArgumentException e) {
            return KindType.builder().extValue(getValue(kind)).build();
        }
    }

    private static boolean containsCardProperties(VCard vCard) {

        for (VCardProperty property : vCard.getProperties()) {
            if (
                !property.getClass().getName().equals(Kind.class.getName()) &&
                !property.getClass().getName().equals(Uid.class.getName()) &&
                !property.getClass().getName().equals(Member.class.getName())
               )
                return true;
        }

        return false;
    }

    private JSContact convert(VCard vCard) throws CardException {

        Card jsCard;
        CardGroup jsCardGroup = null;
        String uid;
        if (vCard.getUid()!=null)
            uid = vCard.getUid().getValue();
        else
            uid = UUID.randomUUID().toString();

        if (vCard.getMembers() != null && vCard.getMembers().size() != 0) {
            jsCardGroup = CardGroup.builder().uid(uid).build();
            fillMembers(vCard, jsCardGroup);
            jsCardGroup.setUid(uid);
            if (containsCardProperties(vCard)) {
                jsCardGroup.setCard(Card.builder().uid(uid).build());
                jsCard = jsCardGroup.getCard();
            }
            else
                return jsCardGroup;
        } else {
            jsCard = Card.builder().uid(uid).build();
        }

        jsCard.setUid(uid);
        jsCard.setKind(getKind(vCard.getKind()));
        jsCard.setProdId(getValue(vCard.getProductId()));
        jsCard.setUpdated(getUpdated(vCard.getRevision()));
        jsCard.setLocale(config.getDefaultLanguage());
        fillSpeakToAsWithGender(vCard, jsCard);
        fillFormattedNames(vCard, jsCard);
        fillNames(vCard, jsCard);
        fillNickNames(vCard, jsCard);
        fillAddresses(vCard, jsCard);
        fillAnniversaries(vCard, jsCard);
        fillPersonalInfos(vCard, jsCard);
        fillContactLanguages(vCard, jsCard);
        fillPhones(vCard, jsCard);
        fillEmails(vCard, jsCard);
        fillPhotos(vCard, jsCard);
        fillOnlineServices(vCard, jsCard);
        fillResources(vCard, jsCard);
        fillTitles(vCard, jsCard);
        fillRoles(vCard, jsCard);
        fillOrganizations(vCard, jsCard);
        fillCategories(vCard, jsCard);
        fillNotes(vCard, jsCard);
        fillRelations(vCard, jsCard);
        if (timeZones.size() > 0)
            jsCard.setTimeZones(timeZones);
        fillRFCXXXXProperties(vCard,jsCard);
        fillExtensions(vCard, jsCard);
        fillUnmatchedElments(vCard, jsCard);

        if (jsCardGroup != null)
            return jsCardGroup;
        else
            return jsCard;
    }

    /**
     * Converts a list of vCard v4.0 instances [RFC6350] into a list of JSContact objects.
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     *
     * @param vCards a list of instances of the ez-vcard library VCard class
     * @return a list of JSContact objects
     * @throws CardException if one of the vCard instances is not v4.0 compliant
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<JSContact> convert(VCard... vCards) throws CardException {

        List<JSContact> jsContacts = new ArrayList<>();

        for (VCard vCard : vCards) {
            if (config.isSetCardMustBeValidated()) {
                ValidationWarnings warnings = vCard.validate(VCardVersion.V4_0);
                if (!warnings.isEmpty())
                    throw new CardException(warnings.toString());
            }
            jsContacts.add(convert(vCard));
        }

        return jsContacts;
    }

}
