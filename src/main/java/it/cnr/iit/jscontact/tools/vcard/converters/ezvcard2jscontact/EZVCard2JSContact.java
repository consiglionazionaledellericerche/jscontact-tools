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

    private final Map<String, TimeZone> customTimeZones = new HashMap<>();

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
                        PersonalInformationEnum piType = (PersonalInformationEnum) args[0];
                        if (piId.getPersonalInformationEnum() == piType)
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


    private Resource getResource(VCardProperty property) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        String vcardType = VCardUtils.getVCardParameterValue(property.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
        Map<Context,Boolean> contexts = getContexts(vcardType);

        return  Resource.builder()
                .propId(property.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG))
                .uri(value)
                .contexts(contexts)
                .mediaType(getMediaType(VCardUtils.getVCardParameterValue(property.getParameters(), VCardUtils.VCARD_MEDIATYPE_PARAM_TAG), value))
                .pref(getPreference(VCardUtils.getVCardParameterValue(property.getParameters(), VCardUtils.VCARD_PREF_PARAM_TAG)))
                .build();
    }

    private void addMediaResource(VCardProperty property, Card jsCard, MediaResourceType type, int index) {

        Resource resource = getResource(property);

        jsCard.addMediaResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), property.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG),  ResourceType.valueOf(type.getRfcValue().name())),
                                    MediaResource.builder()
                                     .type(type)
                                    .propId(resource.getPropId())
                                    .uri(resource.getUri())
                                    .contexts(resource.getContexts())
                                    .mediaType(resource.getMediaType())
                                    .pref(resource.getPref())
                                    .jCardParams(VCardUtils.getVCardUnmatchedParameters(property,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                    .build()
                           );
    }

    private DirectoryResource getDirectoryResource(VCardProperty property, DirectoryResourceType type) {

        String index = property.getParameter(VCardUtils.VCARD_INDEX_PARAM_TAG);
        Resource resource = getResource(property);
        return   DirectoryResource.builder()
                .type(type)
                .propId(resource.getPropId())
                .uri(resource.getUri())
                .contexts(resource.getContexts())
                .mediaType(resource.getMediaType())
                .pref(resource.getPref())
                .index((index!=null) ? Integer.parseInt(index) : null) //used only for DirectoryResource objects whose "type" is "directory"
                .jCardParams(VCardUtils.getVCardUnmatchedParameters(property,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_INDEX_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                .build();
    }

    private void addDirectoryResource(VCardProperty property, Card jsCard, DirectoryResourceType type, int index) {

        jsCard.addDirectoryResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), property.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG), type),
                                    getDirectoryResource(property,type));
    }

    private void addDirectoryResource(Card jsCard, DirectoryResource resource, int index) {

        jsCard.addDirectoryResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",resource.getType().getRfcValue().name(),index), resource.getPropId(), ResourceType.valueOf(resource.getType().getRfcValue().name())),
                                   resource);
    }

    private void addCryptoResource(VCardProperty property, Card jsCard, int index) {

        Resource resource = getResource(property);

        jsCard.addCryptoResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("KEY-%s",index), property.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG), ResourceType.KEY),
                CryptoResource.builder()
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .jCardParams(VCardUtils.getVCardUnmatchedParameters(property,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                        .build()
        );
    }

    private void addCalendarResource(VCardProperty property, Card jsCard, CalendarResourceType type, int index) {

        Resource resource = getResource(property);

        jsCard.addCalendarResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), property.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG), ResourceType.valueOf(type.getRfcValue().name())),
                CalendarResource.builder()
                        .type(type)
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .jCardParams(VCardUtils.getVCardUnmatchedParameters(property,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                        .build()
        );
    }

    private void addLinkResource(VCardProperty property, Card jsCard, LinkResourceType type, int index) {

        Resource resource = getResource(property);

        jsCard.addLinkResource(getId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",(type!=null) ? type.getRfcValue().name() : "LINK",index), property.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG), (type!=null) ? ResourceType.valueOf(type.getRfcValue().name()) : ResourceType.LINK),
                LinkResource.builder()
                        .type(type)
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .jCardParams(VCardUtils.getVCardUnmatchedParameters(property,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
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

    private static String getValue(TextListProperty property) {

        return StringUtils.join(property.getValues(), DelimiterUtils.SEMICOMMA_ARRAY_DELIMITER);
    }

    private static String getValue(Impp property) {

        return property.getUri().toString();
    }

    private void fillSpeakToAsOrGender(VCard vCard, Card jsCard) {

        if (vCard.getGender() == null)
            return;

        if (!config.isConvertGenderToSpeakToAs()) {
            jsCard.addJCardProp(JCardProp.builder()
                                         .name(V_Extension.toV_Extension(VCardUtils.VCARD_GENDER_TAG.toLowerCase()))
                                         .parameters(VCardUtils.getJCardPropParameters(vCard.getGender().getParameters()))
                                         .type(VCardDataType.TEXT)
                                         .value(String.format("%s%s", vCard.getGender().getGender(), (vCard.getGender().getText()==null) ? "" : ";"+vCard.getGender().getText()))
                                         .build());
            return;
        }

        if (vCard.getExtendedProperty("GRAMMATICAL-GENDER") != null)
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
                                        .jCardParams(VCardUtils.getVCardUnmatchedParameters(fn,Arrays.asList(new String[]{VCardUtils.VCARD_DERIVED_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                         .build()
                         );
        }
        Collections.sort(fullNames); // sort based on preference
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
        Collections.sort(wrappers); // sort based on preference
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
                    components = Name.addComponent(components,NameComponent.given(sn.getGiven()));
                if (sn.getFamily() != null)
                    components = Name.addComponent(components,NameComponent.surname(sn.getFamily()));
                for (String an : sn.getAdditionalNames())
                    components = Name.addComponent(components,NameComponent.middle(an));
                for (String sx : sn.getSuffixes())
                    components = Name.addComponent(components,NameComponent.suffix(sx));
                jsCard.setName(Name.builder().components(components).jCardParams(VCardUtils.getVCardUnmatchedParameters(sn,Arrays.asList(new String[]{VCardUtils.VCARD_SORT_AS_PARAM_TAG, VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG}))).build());

            }
        }
    }

    private void fillNickNames(VCard vcard, Card jsCard) {

        List<Nickname> nicknames = vcard.getNicknames();
        List<LocalizedText> nicks = new ArrayList<>();
        for (Nickname nickname : nicknames) {
            String vcardType = VCardUtils.getVCardParameterValue(nickname.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
            Map<Context, Boolean> contexts = getContexts(vcardType);
            addLocalizedText(nicks, LocalizedText.builder()
                    .value(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,nickname.getValues()))
                    .language(nickname.getLanguage())
                    .altid(nickname.getAltId())
                    .preference(nickname.getPref())
                    .contexts(contexts)
                    .jCardParams(VCardUtils.getVCardUnmatchedParameters(nickname,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                    .build());
        }
        int i = 1;
        for (LocalizedText nick : nicks) {
            String id = getId(VCard2JSContactIdsProfile.IdType.NICKNAME, i, "NICK-" + (i ++), nick.getPropId());
            NickName nickName = NickName.builder().name(nick.getValue()).pref(nick.getPreference()).contexts(nick.getContexts()).jCardParams(nick.getJCardParams()).build();
            jsCard.addNickName(id, nickName);
            if (nick.getLocalizations() != null) {
                for (Map.Entry<String, String> localization : nick.getLocalizations().entrySet()) {
                    jsCard.addLocalization(localization.getKey(), "nickNames/" + id, mapper.convertValue(it.cnr.iit.jscontact.tools.dto.NickName.builder().name(localization.getValue()).build(), JsonNode.class));
                }
            }
        }
    }

    private static it.cnr.iit.jscontact.tools.dto.Address getAddressAltrenative(List<it.cnr.iit.jscontact.tools.dto.Address> addresses, String altid) {

        return (it.cnr.iit.jscontact.tools.dto.Address) getAlternative(addresses, altid);
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

            String vcardType = VCardUtils.getVCardParameterValue(addr.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
            tz = getTimezoneName(addr.getTimezone());
            geo = getGeoUri(addr.getGeo());
            String cc = addr.getParameter(VCardUtils.VCARD_CC_PARAM_TAG);

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
                                                                 .propId(addr.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG))
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
                                                                 .jCardParams(VCardUtils.getVCardUnmatchedParameters(addr,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                                 .build()
            );
        }

        if (vcard.getTimezone() != null) {
            tz = getValue(vcard.getTimezone());
            it.cnr.iit.jscontact.tools.dto.Address address = getAddressAltrenative(addresses, vcard.getTimezone().getAltId());
            if (address != null) {
                address.setTimeZone(tz);
            } else {
                jsCard.addJCardProp(JCardProp.builder()
                        .name(V_Extension.toV_Extension(VCardUtils.VCARD_TZ_TAG.toLowerCase()))
                        .parameters(VCardUtils.getJCardPropParameters(vcard.getTimezone().getParameters()))
                        .type(vcard.getTimezone().getParameters().getValue())
                        .value(tz)
                        .build());
            }
        }

        if (vcard.getGeo() != null) {
            geo = getValue(vcard.getGeo().getGeoUri());
            it.cnr.iit.jscontact.tools.dto.Address address = getAddressAltrenative(addresses, vcard.getGeo().getAltId());
            if (address != null) {
                address.setCoordinates(geo);
            } else {
                jsCard.addJCardProp(JCardProp.builder()
                        .name(V_Extension.toV_Extension(VCardUtils.VCARD_GEO_TAG.toLowerCase()))
                        .parameters(VCardUtils.getJCardPropParameters(vcard.getGeo().getParameters()))
                        .type(vcard.getGeo().getParameters().getValue())
                        .value(geo)
                        .build());
            }
        }

        if (addresses.size()==0)
            return;

        Collections.sort(addresses); //sort based on altid

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

    }

    private static  <T extends DateOrTimeProperty> AnniversaryDate getAnniversaryDate(T date) {

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


    private void fillAnniversaries(VCard vcard, Card jsCard) {

        int i = 1;
      if (vcard.getBirthday() != null) {
          jsCard.addAnniversary(getId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i ++), vcard.getBirthday().getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG)),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.birth())
                                                                             .date(getAnniversaryDate(vcard.getBirthday()))
                                                                             .place(getValue(vcard.getBirthplace()))
                                                                             .jCardParams(VCardUtils.getVCardUnmatchedParameters(vcard.getBirthday(),Arrays.asList(new String[]{VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                                             .build()
                                  );
      }

      if (vcard.getDeathdate() != null) {
          jsCard.addAnniversary(getId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i ++), vcard.getDeathdate().getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG)),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.death())
                                                                             .date(getAnniversaryDate(vcard.getDeathdate()))
                                                                             .place(getValue(vcard.getDeathplace()))
                                                                             .jCardParams(VCardUtils.getVCardUnmatchedParameters(vcard.getDeathdate(),Arrays.asList(new String[]{VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                                             .build()
                                  );
      }

      if (vcard.getAnniversary() != null) {
          jsCard.addAnniversary(getId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + i, vcard.getAnniversary().getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG)),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                              .type(AnniversaryType.marriage())
                                                                              .date(getAnniversaryDate(vcard.getAnniversary()))
                                                                              .jCardParams(VCardUtils.getVCardUnmatchedParameters(vcard.getAnniversary(),Arrays.asList(new String[]{VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                                              .build()
                                   );
      }

    }

    private static PersonalInformationLevelType getLevel(String vcardLevelParam) throws CardException {

        try {
            return PersonalInformationLevelType.builder().rfcValue(PersonalInformationLevelEnum.getEnum(vcardLevelParam)).build();
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
                                            .propId(hobby.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG))
                                            .type(PersonalInformationType.builder().rfcValue(PersonalInformationEnum.HOBBY).build())
                                            .value(getValue(hobby))
                                            .level((hobby.getLevel() != null) ? getLevel(hobby.getLevel().getValue()) : null)
                                            .index(hobby.getIndex())
                                            .jCardParams(VCardUtils.getVCardUnmatchedParameters(hobby,Arrays.asList(new String[]{VCardUtils.VCARD_INDEX_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                            .build()
                       );
        }

        int j = 0;
        if (hobbies.size() > 0) {
            Collections.sort(hobbies); //sorted based on index
            int i = 1;
            for (PersonalInformation pi : hobbies)
                jsCard.addPersonalInfo(getId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "HOBBY-" + (i++), pi.getPropId(), PersonalInformationEnum.HOBBY), pi);
        }

        for (Interest interest : vcard.getInterests()) {
            interests.add(PersonalInformation.builder()
                                             .propId(interest.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG))
                                             .type(PersonalInformationType.builder().rfcValue(PersonalInformationEnum.INTEREST).build())
                                             .value(getValue(interest))
                                             .level((interest.getLevel() != null) ? getLevel(interest.getLevel().getValue()) : null)
                                             .index(interest.getIndex())
                                             .jCardParams(VCardUtils.getVCardUnmatchedParameters(interest,Arrays.asList(new String[]{VCardUtils.VCARD_INDEX_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                             .build()
                          );
        }

        if (interests.size() > 0) {
            Collections.sort(interests); //sorted based on index
            int i = 1;
            for (PersonalInformation pi : interests)
                jsCard.addPersonalInfo(getId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "INTEREST-" + (i++), pi.getPropId(), PersonalInformationEnum.INTEREST), pi);
        }

        for (Expertise expertise : vcard.getExpertise()) {
            expertizes.add(PersonalInformation.builder()
                                              .propId(expertise.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG))
                                              .type(PersonalInformationType.builder().rfcValue(PersonalInformationEnum.EXPERTISE).build())
                                              .value(getValue(expertise))
                                              .level((expertise.getLevel() != null) ? getLevel(expertise.getLevel().getValue()) : null)
                                              .index(expertise.getIndex())
                                              .jCardParams(VCardUtils.getVCardUnmatchedParameters(expertise,Arrays.asList(new String[]{VCardUtils.VCARD_INDEX_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                              .build()
                           );
        }

        if (expertizes.size() > 0) {
            Collections.sort(expertizes); //sorted based on index
            int i = 1;
            for (PersonalInformation pi : expertizes)
                jsCard.addPersonalInfo(getId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "EXPERTISE-" + (i++), pi.getPropId(), PersonalInformationEnum.EXPERTISE), pi);
        }

    }

    private static void fillPreferredLanguages(VCard vcard, Card jsCard) {

        for (Language lang : vcard.getLanguages()) {
            String vcardType = VCardUtils.getVCardParameterValue(lang.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
            if (vcardType!=null || lang.getPref()!=null)
                jsCard.addLanguagePreference(getValue(lang),
                                            LanguagePreference.builder()
                                                           .contexts(getContexts(vcardType))
                                                            .pref(lang.getPref())
                                                           .jCardParams(VCardUtils.getVCardUnmatchedParameters(lang,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                           .build()
                                            );
            else
                jsCard.addLanguagePreference(getValue(lang),null);
        }

        if (jsCard.getPreferredLanguages() == null)
            return;
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
            String vcardType = VCardUtils.getVCardParameterValue(tel.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
            Map<Context,Boolean> contexts = getContexts(vcardType);
            Map<PhoneFeature,Boolean> phoneFeatures = getPhoneFeatures(vcardType);
            String[] exclude = null;
            if (contexts != null) exclude = ArrayUtils.addAll(null, EnumUtils.toStrings(Context.toEnumValues(contexts.keySet())));
            if (phoneFeatures != null) exclude = ArrayUtils.addAll(exclude, EnumUtils.toStrings(PhoneFeature.toEnumValues(phoneFeatures.keySet())));
            jsCard.addPhone(getId(VCard2JSContactIdsProfile.IdType.PHONE, i,"PHONE-" + (i++), tel.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG)), Phone.builder()
                                       .phone(getValue(tel))
                                       .features((phoneFeatures == null) ? getDefaultPhoneFeatures() : phoneFeatures)
                                       .contexts(contexts)
                                       .pref(tel.getPref())
                                       .jCardParams(VCardUtils.getVCardUnmatchedParameters(tel,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                       .build()
                              );
        }

    }

    private void fillEmails(VCard vcard, Card jsCard) {

        int i = 1;
        for (Email email : vcard.getEmails()) {
            String emailAddress = getValue(email);
            if (StringUtils.isNotEmpty(emailAddress)) {
                String vcardType = VCardUtils.getVCardParameterValue(email.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
                jsCard.addEmail(getId(VCard2JSContactIdsProfile.IdType.EMAIL, i, "EMAIL-" + (i++), email.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG)), EmailAddress.builder()
                        .email(emailAddress)
                        .contexts(getContexts(vcardType))
                        .pref(email.getPref())
                        .jCardParams(VCardUtils.getVCardUnmatchedParameters(email,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                        .build()
                );
            }
        }
    }

    private void fillOnlineServices(VCard vcard, Card jsCard) {

        String vcardType;
        Map<Context,Boolean> contexts;

        int i = 1;
        for (Impp impp : vcard.getImpps()) {
            vcardType = VCardUtils.getVCardParameterValue(impp.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
            contexts = getContexts(vcardType);
            jsCard.addOnlineService(getId(VCard2JSContactIdsProfile.IdType.ONLINE_SERVICE, i,"OS-" + (i++), impp.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG)), OnlineService.builder()
                    .uri(getValue(impp))
                    .contexts(contexts)
                    .pref(impp.getPref())
                    .jCardParams(VCardUtils.getVCardUnmatchedParameters(impp,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                    .build()
            );
        }

    }

    private void fillMedia(VCard vcard, Card jsCard) {

        int i = 1;
        for (Photo photo : vcard.getPhotos())
            addMediaResource(photo, jsCard, MediaResourceType.photo(), i++);
        i = 1;
        for (Sound sound : vcard.getSounds())
            addMediaResource(sound, jsCard, MediaResourceType.sound(), i++);

        i = 1;
        for (Logo logo : vcard.getLogos())
            addMediaResource(logo, jsCard, MediaResourceType.logo(), i++);

    }

    private void fillCryptoKeys(VCard vcard, Card jsCard) {

        int i = 1;
        for (Key key : vcard.getKeys())
            addCryptoResource(key, jsCard, i++);

    }

    private void fillDirectories(VCard vcard, Card jsCard) {

        int i = 1;
        for (Source source : vcard.getSources())
            addDirectoryResource(source, jsCard, DirectoryResourceType.entry(), i++);

        List<DirectoryResource> orgDirectories = new ArrayList<>();
        for (OrgDirectory od : vcard.getOrgDirectories()) {
            orgDirectories.add(getDirectoryResource(od, DirectoryResourceType.directory()));
        }
        if (orgDirectories.size() > 0) {
            Collections.sort(orgDirectories);  //sorted based on index
            i = 1;
            for (DirectoryResource ds : orgDirectories)
                addDirectoryResource(jsCard,ds,i++);
        }
    }

    private void fillCalendars(VCard vcard, Card jsCard) {

        int i = 1;
        for (CalendarUri calendarUri : vcard.getCalendarUris())
            addCalendarResource(calendarUri, jsCard, CalendarResourceType.calendar(), i++);

        i = 1;
        for (FreeBusyUrl fburl : vcard.getFbUrls())
            addCalendarResource(fburl, jsCard, CalendarResourceType.freeBusy(), i++);

    }


    private void fillLinks(VCard vcard, Card jsCard) {

        int i = 1;
        for (Url url : vcard.getUrls())
            addLinkResource(url, jsCard, null, i++);

        List<RawProperty> contactUris = VCardUtils.getRawProperties(vcard, "CONTACT-URI");
        i = 1;
        for (RawProperty contactUri : contactUris) {
            UriProperty uriProperty = new UriProperty(getValue(contactUri));
            uriProperty.setParameters(contactUri.getParameters());
            addLinkResource(uriProperty, jsCard, LinkResourceType.contact(), i++);
        }

    }

    private void fillSchedulingAddresses(VCard vcard, Card jsCard) {

        int i = 1;
        for (CalendarRequestUri calendarRequestUri : vcard.getCalendarRequestUris()) {
            String vcardType = VCardUtils.getVCardParameterValue(calendarRequestUri.getParameters(), VCardUtils.VCARD_TYPE_PARAM_TAG);
            Map<Context,Boolean> contexts = getContexts(vcardType);
            jsCard.addSchedulingAddress(getId(VCard2JSContactIdsProfile.IdType.SCHEDULING, i, "SCHEDULING-" + (i++), calendarRequestUri.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG)),
                     SchedulingAddress.builder()
                             .type((calendarRequestUri.getValue().startsWith("mailto:")) ? SchedulingAddressType.imip() : null)
                             .propId(calendarRequestUri.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG))
                             .uri(calendarRequestUri.getValue())
                             .contexts(contexts)
                             .pref(calendarRequestUri.getPref())
                             .jCardParams(VCardUtils.getVCardUnmatchedParameters(calendarRequestUri,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                             .build());
        }
    }

    private void fillTitles(List<LocalizedText> localizedStrings, Card jsCard, TitleType type, int i) {

        for (LocalizedText localizedString : localizedStrings) {
            String id = getId(VCard2JSContactIdsProfile.IdType.TITLE, i, "TITLE-" + (i ++), localizedString.getPropId());
            jsCard.addTitle(id, it.cnr.iit.jscontact.tools.dto.Title.builder().title(localizedString.getValue()).type(type).jCardParams(localizedString.getJCardParams()).build());
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
                                                     .jCardParams(VCardUtils.getVCardUnmatchedParameters(title,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                     .build()
                              );
        Collections.sort(titles); //sort based on preference
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
                                                     .jCardParams(VCardUtils.getVCardUnmatchedParameters(role,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                     .build()
                              );
        }
        Collections.sort(roles); //sort based on preference
        fillTitles(roles, jsCard, TitleType.role(), (jsCard.getTitles() != null) ? jsCard.getTitles().size() + 1 : 1);
    }

    private void fillOrganizations(VCard vcard, Card jsCard) {

        List<LocalizedText> organizations = new ArrayList<>();
        for (Organization org : vcard.getOrganizations()) {
            addLocalizedText(organizations, LocalizedText.builder()
                                                             .propId(org.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG))
                                                             .value(getValue(org))
                                                             .language(org.getLanguage())
                                                             .altid(org.getAltId())
                                                             .preference(org.getPref())
                                                             .jCardParams(VCardUtils.getVCardUnmatchedParameters(org,Arrays.asList(new String[]{VCardUtils.VCARD_SORT_AS_PARAM_TAG, VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                                                             .build()
                              );
        }
        Collections.sort(organizations); //sort based on preference

        int i = 1;
        for (LocalizedText organization : organizations) {
            String[] nameItems = organization.getValue().split(DelimiterUtils.SEMICOMMA_ARRAY_DELIMITER);
            String id = getId(VCard2JSContactIdsProfile.IdType.ORGANIZATION, i, "ORG-" + (i ++), organization.getPropId());
            List<String> units = (nameItems.length > 1 ) ? Arrays.asList(nameItems).subList(1,nameItems.length) : null;
            jsCard.addOrganization(id, it.cnr.iit.jscontact.tools.dto.Organization.builder().name((!nameItems[0].isEmpty()) ? nameItems[0] : null).units((units!=null)? units.toArray(new String[0]) : null).jCardParams(organization.getJCardParams()).build());
            if (organization.getLocalizations()!=null) {
                for (Map.Entry<String,String> localization : organization.getLocalizations().entrySet()) {
                    String[] localizedNameItems =  localization.getValue().split(DelimiterUtils.SEMICOMMA_ARRAY_DELIMITER);
                    List<String> localizedUnits = (localizedNameItems.length > 1 ) ? Arrays.asList(localizedNameItems).subList(1,localizedNameItems.length) : null;
                    jsCard.addLocalization(localization.getKey(), "organizations/" + id, mapper.convertValue(it.cnr.iit.jscontact.tools.dto.Organization.builder().name((!localizedNameItems[0].isEmpty()) ? localizedNameItems[0] : null).units((localizedUnits!=null)? localizedUnits.toArray(new String[0]) : null).build(), JsonNode.class));
                }
            }
        }

    }

    private static void fillNotes(VCard vcard, Card jsCard) {

        List<LocalizedText> notes = new ArrayList<>();
        for (ezvcard.property.Note note : vcard.getNotes())
            jsCard.addNote(Note.builder()
                               .note(note.getValue())
                               .language(note.getLanguage())
                               .jCardParams(VCardUtils.getVCardUnmatchedParameters(note,Arrays.asList(new String[]{VCardUtils.VCARD_PID_PARAM_TAG, VCardUtils.VCARD_GROUP_PARAM_TAG})))
                               .build());
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
            String vcardPref = extension.getParameter(VCardUtils.VCARD_PREF_PARAM_TAG);
            Integer pref;
            try {
                pref = Integer.parseInt(vcardPref);
            } catch (Exception e) {
                pref = null;
            }
            String vcardType = extension.getParameter(VCardUtils.VCARD_TYPE_PARAM_TAG);
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
                String id = getId(VCard2JSContactIdsProfile.IdType.PRONOUNS, i,"PRONOUNS-" + (i++), extension.getParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG));
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
                        jsCard.addContactChannelPreference(channelType, ContactChannelPreference.builder().contexts(contexts).pref(pref).build());
                    else
                        jsCard.addContactChannelPreference(channelType, null);
                }
            }
        }
    }


    private void fillVCardExtensions(VCard vcard, JSContact jsContact) {

        for (RawProperty extension : vcard.getExtendedProperties()) {
            if (!fakeExtensionsMapping.containsKey(extension.getPropertyName()) &&
                    !fakeExtensionsMapping.containsKey(extension.getPropertyName().toLowerCase())) {
                jsContact.addJCardProp(JCardProp.builder()
                                             .name(V_Extension.toV_Extension(extension.getPropertyName()))
                                             .parameters(VCardUtils.getJCardPropParameters(extension.getParameters()))                                            .type(extension.getDataType())
                                             .value(extension.getValue())
                                             .build());
            }
        }
    }

    private void fillJSContactExtensions(VCard vcard, JSContact jsContact) throws CardException {

        String path = null;
        Object value = null;
        String extensionName = null;
        try {
            for (RawProperty extension : vcard.getExtendedProperties()) {
                if (extension.getPropertyName().equalsIgnoreCase(VCardUtils.VCARD_X_RFC0000_JSPROP_TAG)) {
                    path = extension.getParameter(VCardUtils.VCARD_X_RFC0000_JSPATH_PARAM_TAG);
                    value = X_RFC0000_JSPROP_Utils.toJsonValue(extension.getValue());
                    if (!path.contains(DelimiterUtils.SLASH_DELIMITER)) {
                        jsContact.addExtension(path,value);
                    }
                    else {
                        String[] pathItems = path.split(DelimiterUtils.SLASH_DELIMITER);
                        extensionName = pathItems[pathItems.length-1];
                        List list = Arrays.asList(pathItems);
                        jsContact.addExtension(list.subList(0, pathItems.length-1),extensionName.replaceAll(DelimiterUtils.SLASH_DELIMITER_IN_JSON_POINTER,DelimiterUtils.SLASH_DELIMITER), value);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new CardException(String.format("Unable to convert X-RFC0000-PROP property with path: %s", path));
        }
    }

    private static void fillUnmatchedElments(VCard vcard, Card jsCard) {

        if (vcard.getClientPidMaps()!=null) {
            for (ClientPidMap pidmap : vcard.getClientPidMaps())
                jsCard.addJCardProp(JCardProp.builder()
                                             .name(V_Extension.toV_Extension(VCardUtils.VCARD_CLIENTPIDMAP_TAG.toLowerCase()))
                                             .parameters(VCardUtils.getJCardPropParameters(pidmap.getParameters()))
                                             .type(VCardDataType.TEXT)
                                             .value(String.format("%d,%s",pidmap.getPid(), pidmap.getUri()))
                                             .build());
        }

        if (vcard.getXmls()!=null) {
                for (Xml xml : vcard.getXmls())
                    jsCard.addJCardProp(JCardProp.builder()
                                                 .name(V_Extension.toV_Extension(VCardUtils.VCARD_XML_TAG.toLowerCase()))
                                                 .parameters(VCardUtils.getJCardPropParameters(xml.getParameters()))
                                                 .type(VCardDataType.TEXT)
                                                 .value(xml.getValue().getTextContent())
                                                 .build());
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
            return KindType.builder().extValue(V_Extension.toV_Extension(getValue(kind))).build();
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
            fillVCardExtensions(vCard, jsCardGroup);
            fillJSContactExtensions(vCard, jsCardGroup);
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
        fillSpeakToAsOrGender(vCard, jsCard);
        fillFormattedNames(vCard, jsCard);
        fillNames(vCard, jsCard);
        fillNickNames(vCard, jsCard);
        fillAddresses(vCard, jsCard);
        fillAnniversaries(vCard, jsCard);
        fillPersonalInfos(vCard, jsCard);
        fillPreferredLanguages(vCard, jsCard);
        fillPhones(vCard, jsCard);
        fillEmails(vCard, jsCard);
        fillSchedulingAddresses(vCard,jsCard);
        fillOnlineServices(vCard, jsCard);
        fillCalendars(vCard, jsCard);
        fillCryptoKeys(vCard, jsCard);
        fillLinks(vCard, jsCard);
        fillMedia(vCard, jsCard);
        fillDirectories(vCard, jsCard);
        fillTitles(vCard, jsCard);
        fillRoles(vCard, jsCard);
        fillOrganizations(vCard, jsCard);
        fillCategories(vCard, jsCard);
        fillNotes(vCard, jsCard);
        fillRelations(vCard, jsCard);
        fillRFCXXXXProperties(vCard,jsCard);
        if (customTimeZones.size() > 0)
            jsCard.setCustomTimeZones(customTimeZones);
        fillUnmatchedElments(vCard, jsCard);
        fillVCardExtensions(vCard, jsCard);
        fillJSContactExtensions(vCard, jsCard);

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
