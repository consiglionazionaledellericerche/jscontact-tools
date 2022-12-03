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
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
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

    private VCardPropertiesComparator vCardPropertiesComparator;

    protected VCard2JSContactConfig config;

    private int customTimeZoneCounter = 0;

    private final Map<String, TimeZone> customTimeZones = new HashMap<>();

    private static boolean isDefaultLanguage(String language, String defaultLanguage) {

        if (StringUtils.isEmpty(language))
            return false;

        return defaultLanguage != null && StringUtils.equals(language, defaultLanguage);

    }


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
        List<PhoneFeatureEnum> enumValues = getJSCardEnumValues(PhoneFeatureEnum.class,vcardTypeParam, Arrays.asList("home", "work"), null);

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

    private static HasAltid getAlternative(List<? extends HasAltid> list, String altid) {

        if (altid == null)
            return null;

        for (HasAltid al : list) {
            if (al.getAltid() != null && al.getAltid().equals(altid))
                return al;
        }

        return null;
    }

    private static Integer toJSCardPref(String vcardPref) {
        return (vcardPref != null) ? Integer.parseInt(vcardPref) : null;
    }

    private static String toJSCardAutoFulllAddress(ezvcard.property.Address addr) {

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

    private void addJSCardMediaResource(VCardProperty vcardProperty, Card jsCard, MediaResourceType type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addMediaResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue()),  ResourceType.valueOf(type.getRfcValue().name())),
                                    MediaResource.builder()
                                     .type(type)
                                    .propId(resource.getPropId())
                                    .uri(resource.getUri())
                                    .contexts(resource.getContexts())
                                    .mediaType(resource.getMediaType())
                                    .pref(resource.getPref())
                                    .label(toJSCardLabel(vcardProperty,vcardExtendedProperties))
                                    .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardProperty, VCardParamEnum.PID, VCardParamEnum.GROUP))
                                    .build()
                           );
    }

    private DirectoryResource toJSCardDirectoryResource(VCardProperty vcardProperty, DirectoryResourceType type, List<RawProperty> vcardExtendedProperties) {

        String index = vcardProperty.getParameter(VCardParamEnum.INDEX.getValue());
        Resource resource = toJSCardResource(vcardProperty);
        return   DirectoryResource.builder()
                .type(type)
                .propId(resource.getPropId())
                .uri(resource.getUri())
                .contexts(resource.getContexts())
                .mediaType(resource.getMediaType())
                .pref(resource.getPref())
                .index((index!=null) ? Integer.parseInt(index) : null) //used only for DirectoryResource objects whose "type" is "directory"
                .label(toJSCardLabel(vcardProperty,vcardExtendedProperties))
                .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardProperty, VCardParamEnum.PID, VCardParamEnum.INDEX, VCardParamEnum.GROUP))
                .build();
    }

    private void addJSCardDirectoryResource(VCardProperty property, Card jsCard, DirectoryResourceType type, int index, List<RawProperty> vcardExtendedProperties) {

        jsCard.addDirectoryResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), property.getParameter(VCardParamEnum.PROP_ID.getValue()), type),
                                    toJSCardDirectoryResource(property,type, vcardExtendedProperties));
    }

    private void addJSCardDirectoryResource(Card jsCard, DirectoryResource resource, int index) {

        jsCard.addDirectoryResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",resource.getType().getRfcValue().name(),index), resource.getPropId(), ResourceType.valueOf(resource.getType().getRfcValue().name())),
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
                        .label(toJSCardLabel(vcardProperty,vcardExtendedProperties))
                        .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardProperty, VCardParamEnum.PID, VCardParamEnum.GROUP))
                        .build()
        );
    }

    private void addJSCardCalendarResource(VCardProperty vcardProperty, Card jsCard, CalendarResourceType type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addCalendarResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",type.getRfcValue().name(),index), vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue()), ResourceType.valueOf(type.getRfcValue().name())),
                CalendarResource.builder()
                        .type(type)
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty,vcardExtendedProperties))
                        .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardProperty, VCardParamEnum.PID, VCardParamEnum.GROUP))
                        .build()
        );
    }

    private void addJSCardLinkResource(VCardProperty vcardProperty, Card jsCard, LinkResourceType type, int index, List<RawProperty> vcardExtendedProperties) {

        Resource resource = toJSCardResource(vcardProperty);

        jsCard.addLinkResource(getJSCardId(VCard2JSContactIdsProfile.IdType.RESOURCE, index, String.format("%s-%s",(type!=null) ? type.getRfcValue().name() : "LINK",index), vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue()), (type!=null) ? ResourceType.valueOf(type.getRfcValue().name()) : ResourceType.LINK),
                LinkResource.builder()
                        .type(type)
                        .propId(resource.getPropId())
                        .uri(resource.getUri())
                        .contexts(resource.getContexts())
                        .mediaType(resource.getMediaType())
                        .pref(resource.getPref())
                        .label(toJSCardLabel(vcardProperty,vcardExtendedProperties))
                        .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardProperty, VCardParamEnum.PID, VCardParamEnum.GROUP))
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
                    .fullAddress(property.getText())
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

        if (vCard.getExtendedProperty(VCardPropEnum.GRAMMATICAL_GENDER.getValue()) != null)
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
            jsCard.setSpeakToAs(SpeakToAs.builder().grammaticalGender(GrammaticalGenderType.getEnum(vCard.getExtendedProperty(VCardPropEnum.GRAMMATICAL_GENDER.getValue()).getValue().toLowerCase())).build());
        }
    }

    private void fillJSCardFullName(VCard vcard, Card jsCard) {

        if (vcard.getFormattedNames() == null || vcard.getFormattedNames().isEmpty())
            return;

        List<FormattedName> fns = vcard.getFormattedNames();
        Collections.sort(fns, vCardPropertiesComparator);
        String lastAltid = null;
        for (FormattedName fn : fns) {
            if (fn.getAltId() == null || lastAltid == null || !fn.getAltId().equals(lastAltid)) {
                jsCard.setFullName(fn.getValue());
                lastAltid = fn.getAltId();
            } else {
                jsCard.addLocalization(fn.getLanguage(), "fullName", JsonNodeUtils.textNode(fn.getValue()));
            }
        }
    }

    private static void fillJSCardMembers(VCard vcard, Card jsCard) {

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
            jsCard.addMember(wrapper.getValue());

    }


    private static Map<String,String> toJSCardNameSortAs(List<String> vcardSortAs, StructuredName sn) {

        if (vcardSortAs == null || vcardSortAs.isEmpty())
            return null;

        Map<String,String> sortAs = new HashMap<>();
        NameComponentEnum[] nameComponentEnumValues = NameComponentEnum.values();
        int i = 0;
        for (String vcardSortAsItem : vcardSortAs) {
            String[] vcardSortAsItemSubs = vcardSortAsItem.split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
            for (String vcardSortAsItemSub : vcardSortAsItemSubs)
                sortAs.put(nameComponentEnumValues[i++].getValue(), vcardSortAsItemSub);
        }
        return sortAs;
    }

    private static Integer toJSCardRank(String[] ranks, int componentIndex, int subIndex) throws CardException {

        if (ranks == null)
            return null;

        if (componentIndex > ranks.length)
            return null;

        if (ranks[componentIndex-1].trim().isEmpty())
            return null;

        String[] subranks = ranks[componentIndex-1].split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        if (subIndex > subranks.length)
            return null;

        try {
            if (subranks[subIndex - 1].trim().isEmpty())
                return null;

            return Integer.parseInt(subranks[subIndex - 1].trim());
        } catch(Exception e) {
            throw new CardException(String.format("Invalid value in RANKS parameter %s",String.join(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER,ranks)));
        }
    }

    private static void fillJSCardNames(VCard vcard, Card jsCard) throws CardException {

        List<StructuredName> sns = vcard.getStructuredNames();
        if (sns.size() > 0) {
            NameComponent[] components = null;
            for (StructuredName sn : sns) {
                String[] ranks = null;
                if (sn.getParameter(VCardParamEnum.RANKS.getValue())!=null)
                    ranks = sn.getParameter(VCardParamEnum.RANKS.getValue()).split(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER);
                int i = 1;
                for (String px : sn.getPrefixes())
                    components = Name.addComponent(components, NameComponent.prefix(px, toJSCardRank(ranks,4, i++)));
                if (sn.getGiven() != null) {
                    String[] names = sn.getGiven().split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                    i = 1;
                    for (String name : names)
                        components = Name.addComponent(components, NameComponent.given(name, toJSCardRank(ranks,2, i++)));
                }
                if (sn.getFamily() != null) {
                    String[] surnames = sn.getFamily().split(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                    i = 1;
                    for (String surname : surnames)
                        components = Name.addComponent(components, NameComponent.surname(surname, toJSCardRank(ranks, 1, i++)));
                }
                i = 1;
                for (String an : sn.getAdditionalNames())
                    components = Name.addComponent(components,NameComponent.middle(an, toJSCardRank(ranks,3, i++)));
                i = 1;
                for (String sx : sn.getSuffixes())
                    components = Name.addComponent(components,NameComponent.suffix(sx, toJSCardRank(ranks,5, i++)));
                jsCard.setName(Name.builder()
                                   .components(components)
                                   .sortAs(toJSCardNameSortAs(sn.getSortAs(),sn))
                                   .vCardParams(VCardUtils.getVCardUnmatchedParams(sn, VCardParamEnum.PID, VCardParamEnum.GROUP))
                                   .build());
            }
        }
    }


    private NickName toJSCardNickName(String name, ezvcard.property.Nickname vcardNickname, VCard vcard) {

        return NickName.builder()
                .name(name)
                .pref(vcardNickname.getPref())
                .contexts(toJSCardContexts(vcardNickname.getType()))
                .label(toJSCardLabel(vcardNickname,vcard.getExtendedProperties()))
                .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardNickname, VCardParamEnum.PID, VCardParamEnum.GROUP))
                .build();
    }


    private void fillJSCardNickNames(VCard vcard, Card jsCard) {

        if (vcard.getNicknames() == null || vcard.getNicknames().isEmpty())
            return;

        List<ezvcard.property.Nickname> vcardNickNames = vcard.getNicknames();
        Collections.sort(vcardNickNames, vCardPropertiesComparator);
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
                    jsCard.addLocalization(vcardNickName.getLanguage(), "nickNames/" + lastMapId, mapper.convertValue(toJSCardNickName(name, vcardNickName, vcard), JsonNode.class));
                }
            }
        }
    }

    private static it.cnr.iit.jscontact.tools.dto.Address getAddressAltrenative(List<it.cnr.iit.jscontact.tools.dto.Address> addresses, String altid) {

        return (it.cnr.iit.jscontact.tools.dto.Address) getAlternative(addresses, altid);
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

    private void fillJSCardAddresses(VCard vcard, Card jsCard) {

        List<it.cnr.iit.jscontact.tools.dto.Address> addresses = new ArrayList<>();

        String tz;
        String geo;
        for (ezvcard.property.Address addr : vcard.getAddresses()) {

            String vcardType = VCardUtils.getVCardParamValue(addr.getParameters(), VCardParamEnum.TYPE);
            tz = toJSCardTimezoneName(addr.getTimezone());
            geo = getValue(addr.getGeo());
            String cc = addr.getParameter(VCardParamEnum.CC.getValue());

            List<StreetComponent> streetDetailPairs = new ArrayList<>();
            if (StringUtils.isNotEmpty(addr.getPoBox()))
                streetDetailPairs.add(StreetComponent.postOfficeBox(addr.getPoBox()));
            if (StringUtils.isNotEmpty(addr.getExtendedAddressFull()))
                streetDetailPairs.add(StreetComponent.extension(addr.getExtendedAddressFull()));
            if (StringUtils.isNotEmpty(addr.getStreetAddressFull()))
                streetDetailPairs.add(StreetComponent.name(addr.getStreetAddressFull()));

            String autoFullAddress = toJSCardAutoFulllAddress(addr);
            addresses.add(it.cnr.iit.jscontact.tools.dto.Address.builder()
                                                                 .hash(autoFullAddress)
                                                                 .propId(addr.getParameter(VCardParamEnum.PROP_ID.getValue()))
                                                                 .contexts(toJSCardAddressContexts(vcardType))
                                                                 .fullAddress(toJSCardFulllAddress(addr.getLabel(),autoFullAddress))
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
                                                                 .vCardParams(VCardUtils.getVCardUnmatchedParams(addr, VCardParamEnum.PID, VCardParamEnum.GROUP))
                                                                 .build()
            );
        }

        if (vcard.getTimezone() != null) {
            tz = getValue(vcard.getTimezone());
            it.cnr.iit.jscontact.tools.dto.Address address = getAddressAltrenative(addresses, vcard.getTimezone().getAltId());
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
            it.cnr.iit.jscontact.tools.dto.Address address = getAddressAltrenative(addresses, vcard.getGeo().getAltId());
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

        if (addresses.size()==0)
            return;

        Collections.sort(addresses); //sort based on altid

        String id;
        String altId = null;
        int i = 0;
        for (Address address: addresses) {
            if (altId!=null && address.getAltid()!=null && altId.equals(address.getAltid())) {
                id = getJSCardId(VCard2JSContactIdsProfile.IdType.ADDRESS, i, "ADR-" + i, address.getPropId() );
                jsCard.addLocalization(address.getLanguage(),"addresses/" + id,  mapper.convertValue(address, JsonNode.class));
            }
            else {
                i++;
                id = getJSCardId(VCard2JSContactIdsProfile.IdType.ADDRESS, i, "ADR-" + i, address.getPropId());
                jsCard.addAddress(id, address);
                altId = address.getAltid();
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
          jsCard.addAnniversary(getJSCardId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i ++), vcard.getBirthday().getParameter(VCardParamEnum.PROP_ID.getValue())),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.birth())
                                                                             .date(toJSCardAnniversaryDate(vcard.getBirthday()))
                                                                             .place(getValue(vcard.getBirthplace()))
                                                                             .vCardParams(VCardUtils.getVCardUnmatchedParams(vcard.getBirthday(), VCardParamEnum.GROUP))
                                                                             .build()
                                  );
      }

      if (vcard.getDeathdate() != null) {
          jsCard.addAnniversary(getJSCardId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + (i ++), vcard.getDeathdate().getParameter(VCardParamEnum.PROP_ID.getValue())),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.death())
                                                                             .date(toJSCardAnniversaryDate(vcard.getDeathdate()))
                                                                             .place(getValue(vcard.getDeathplace()))
                                                                             .vCardParams(VCardUtils.getVCardUnmatchedParams(vcard.getDeathdate(), VCardParamEnum.GROUP))
                                                                             .build()
                                  );
      }

      if (vcard.getAnniversary() != null) {
          jsCard.addAnniversary(getJSCardId(VCard2JSContactIdsProfile.IdType.ANNIVERSARY, i, "ANNIVERSARY-" + i, vcard.getAnniversary().getParameter(VCardParamEnum.PROP_ID.getValue())),it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                              .type(AnniversaryType.wedding())
                                                                              .date(toJSCardAnniversaryDate(vcard.getAnniversary()))
                                                                              .vCardParams(VCardUtils.getVCardUnmatchedParams(vcard.getAnniversary(), VCardParamEnum.GROUP))
                                                                              .build()
                                   );
      }

    }

    private static PersonalInformationLevelType toJSCardLevel(String vcardLevelParam) throws CardException {

        try {
            return PersonalInformationLevelType.builder().rfcValue(PersonalInformationLevelEnum.getEnum(vcardLevelParam)).build();
        } catch (IllegalArgumentException e) {
            throw new CardException("Unknown LEVEL value " + vcardLevelParam);
        }
    }

    private void fillJSCardPersonalInfos(VCard vcard, Card jsCard) throws CardException {

        List<PersonalInformation> hobbies = new ArrayList<>();
        List<PersonalInformation> interests = new ArrayList<>();
        List<PersonalInformation> expertizes = new ArrayList<>();

        for (Hobby hobby : vcard.getHobbies()) {
            hobbies.add(PersonalInformation.builder()
                                            .propId(hobby.getParameter(VCardParamEnum.PROP_ID.getValue()))
                                            .type(PersonalInformationType.builder().rfcValue(PersonalInformationEnum.HOBBY).build())
                                            .value(getValue(hobby))
                                            .level((hobby.getLevel() != null) ? toJSCardLevel(hobby.getLevel().getValue()) : null)
                                            .index(hobby.getIndex())
                                            .label(toJSCardLabel(hobby,vcard.getExtendedProperties()))
                                            .vCardParams(VCardUtils.getVCardUnmatchedParams(hobby, VCardParamEnum.INDEX, VCardParamEnum.GROUP))
                                            .build()
                       );
        }

        int j = 0;
        if (hobbies.size() > 0) {
            Collections.sort(hobbies); //sorted based on index
            int i = 1;
            for (PersonalInformation pi : hobbies)
                jsCard.addPersonalInfo(getJSCardId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "HOBBY-" + (i++), pi.getPropId(), PersonalInformationEnum.HOBBY), pi);
        }

        for (Interest interest : vcard.getInterests()) {
            interests.add(PersonalInformation.builder()
                                             .propId(interest.getParameter(VCardParamEnum.PROP_ID.getValue()))
                                             .type(PersonalInformationType.builder().rfcValue(PersonalInformationEnum.INTEREST).build())
                                             .value(getValue(interest))
                                             .level((interest.getLevel() != null) ? toJSCardLevel(interest.getLevel().getValue()) : null)
                                             .index(interest.getIndex())
                                             .label(toJSCardLabel(interest,vcard.getExtendedProperties()))
                                             .vCardParams(VCardUtils.getVCardUnmatchedParams(interest, VCardParamEnum.INDEX, VCardParamEnum.GROUP))
                                             .build()
                          );
        }

        if (interests.size() > 0) {
            Collections.sort(interests); //sorted based on index
            int i = 1;
            for (PersonalInformation pi : interests)
                jsCard.addPersonalInfo(getJSCardId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "INTEREST-" + (i++), pi.getPropId(), PersonalInformationEnum.INTEREST), pi);
        }

        for (Expertise expertise : vcard.getExpertise()) {
            expertizes.add(PersonalInformation.builder()
                                              .propId(expertise.getParameter(VCardParamEnum.PROP_ID.getValue()))
                                              .type(PersonalInformationType.builder().rfcValue(PersonalInformationEnum.EXPERTISE).build())
                                              .value(getValue(expertise))
                                              .level((expertise.getLevel() != null) ? toJSCardLevel(expertise.getLevel().getValue()) : null)
                                              .index(expertise.getIndex())
                                              .label(toJSCardLabel(expertise,vcard.getExtendedProperties()))
                                              .vCardParams(VCardUtils.getVCardUnmatchedParams(expertise, VCardParamEnum.INDEX, VCardParamEnum.GROUP))
                                              .build()
                           );
        }

        if (expertizes.size() > 0) {
            Collections.sort(expertizes); //sorted based on index
            int i = 1;
            for (PersonalInformation pi : expertizes)
                jsCard.addPersonalInfo(getJSCardId(VCard2JSContactIdsProfile.IdType.PERSONAL_INFO, j++, "EXPERTISE-" + (i++), pi.getPropId(), PersonalInformationEnum.EXPERTISE), pi);
        }

    }

    private static void fillJSCardPreferredLanguages(VCard vcard, Card jsCard) {

        for (Language lang : vcard.getLanguages()) {
            String vcardType = VCardUtils.getVCardParamValue(lang.getParameters(), VCardParamEnum.TYPE);
            if (vcardType!=null || lang.getPref()!=null)
                jsCard.addLanguagePreference(getValue(lang),
                                            LanguagePreference.builder()
                                                           .contexts(toJSCardContexts(vcardType))
                                                            .pref(lang.getPref())
                                                           .vCardParams(VCardUtils.getVCardUnmatchedParams(lang, VCardParamEnum.PID, VCardParamEnum.GROUP))
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
                                       .label(toJSCardLabel(tel,vcard.getExtendedProperties()))
                                       .vCardParams(VCardUtils.getVCardUnmatchedParams(tel, VCardParamEnum.PID, VCardParamEnum.GROUP))
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
                jsCard.addEmail(getJSCardId(VCard2JSContactIdsProfile.IdType.EMAIL, i, "EMAIL-" + (i++), email.getParameter(VCardParamEnum.PROP_ID.getValue())), EmailAddress.builder()
                        .address(emailAddress)
                        .contexts(toJSCardContexts(vcardType))
                        .pref(email.getPref())
                        .label(toJSCardLabel(email,vcard.getExtendedProperties()))
                        .vCardParams(VCardUtils.getVCardUnmatchedParams(email, VCardParamEnum.PID, VCardParamEnum.GROUP))
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
                    .user(getValue(impp))
                    .type(OnlineServiceType.impp())
                    .contexts(contexts)
                    .pref(impp.getPref())
                    .label(toJSCardLabel(impp,vcard.getExtendedProperties()))
                    .vCardParams(VCardUtils.getVCardUnmatchedParams(impp, VCardParamEnum.PID, VCardParamEnum.GROUP))
                    .build()
            );
        }

    }

    private void fillJSCardMedia(VCard vcard, Card jsCard) {

        int i = 1;
        for (Photo photo : vcard.getPhotos())
            addJSCardMediaResource(photo, jsCard, MediaResourceType.photo(), i++, vcard.getExtendedProperties());
        i = 1;
        for (Sound sound : vcard.getSounds())
            addJSCardMediaResource(sound, jsCard, MediaResourceType.sound(), i++, vcard.getExtendedProperties());

        i = 1;
        for (Logo logo : vcard.getLogos())
            addJSCardMediaResource(logo, jsCard, MediaResourceType.logo(), i++, vcard.getExtendedProperties());

    }

    private void fillJSCardCryptoKeys(VCard vcard, Card jsCard) {

        int i = 1;
        for (Key key : vcard.getKeys())
            addJSCardCryptoResource(key, jsCard, i++, vcard.getExtendedProperties());

    }

    private void fillJSCardDirectories(VCard vcard, Card jsCard) {

        int i = 1;
        for (Source source : vcard.getSources())
            addJSCardDirectoryResource(source, jsCard, DirectoryResourceType.entry(), i++, vcard.getExtendedProperties());

        List<DirectoryResource> orgDirectories = new ArrayList<>();
        for (OrgDirectory od : vcard.getOrgDirectories()) {
            orgDirectories.add(toJSCardDirectoryResource(od, DirectoryResourceType.directory(), vcard.getExtendedProperties()));
        }
        if (orgDirectories.size() > 0) {
            Collections.sort(orgDirectories);  //sorted based on index
            i = 1;
            for (DirectoryResource ds : orgDirectories)
                addJSCardDirectoryResource(jsCard,ds,i++);
        }
    }

    private void fillJSCardCalendars(VCard vcard, Card jsCard) {

        int i = 1;
        for (CalendarUri calendarUri : vcard.getCalendarUris())
            addJSCardCalendarResource(calendarUri, jsCard, CalendarResourceType.calendar(), i++, vcard.getExtendedProperties());

        i = 1;
        for (FreeBusyUrl fburl : vcard.getFbUrls())
            addJSCardCalendarResource(fburl, jsCard, CalendarResourceType.freeBusy(), i++, vcard.getExtendedProperties());

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
            addJSCardLinkResource(uriProperty, jsCard, LinkResourceType.contact(), i++, vcard.getExtendedProperties());
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
                             .label(toJSCardLabel(calendarRequestUri,vcard.getExtendedProperties()))
                             .vCardParams(VCardUtils.getVCardUnmatchedParams(calendarRequestUri, VCardParamEnum.PID, VCardParamEnum.GROUP))
                             .build());
        }
    }


    private static String getJSCardTitleOrganization(Map<String,it.cnr.iit.jscontact.tools.dto.Organization> jscardOrganizations, String vcardTitleGroup) {

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
                .type(TitleType.title())
                .organization(getJSCardTitleOrganization(jsCard.getOrganizations(), vcardTitle.getGroup()))
                .pref(vcardTitle.getPref())
                .contexts(toJSCardContexts(vcardTitle.getType()))
                .label(toJSCardLabel(vcardTitle,vcard.getExtendedProperties()))
                .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardTitle, VCardParamEnum.PID, VCardParamEnum.GROUP))
                .build();
    }

    private it.cnr.iit.jscontact.tools.dto.Title toJSCardTitle(Role vcardRole, VCard vcard, Card jsCard) {

        return it.cnr.iit.jscontact.tools.dto.Title.builder()
                .name(vcardRole.getValue())
                .type(TitleType.role())
                .organization(getJSCardTitleOrganization(jsCard.getOrganizations(), vcardRole.getGroup()))
                .pref(vcardRole.getPref())
                .contexts(toJSCardContexts(vcardRole.getType()))
                .label(toJSCardLabel(vcardRole,vcard.getExtendedProperties()))
                .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardRole, VCardParamEnum.PID, VCardParamEnum.GROUP))
                .build();
    }


    private void fillJSCardTitlesfromVCardTitles(VCard vcard, Card jsCard) {

        if (vcard.getTitles() == null || vcard.getTitles().isEmpty())
            return;

        List<ezvcard.property.Title> titles = vcard.getTitles();
        Collections.sort(titles,vCardPropertiesComparator);
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
        Collections.sort(roles,vCardPropertiesComparator);
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

        List<String> units;
        String name;
        if (vcardOrg.getValues().size() > 1 ) {
            name = vcardOrg.getValues().get(0);
            units = vcardOrg.getValues().subList(1,vcardOrg.getValues().size());
        } else { // ezvcard put all the organization components separated by semicolon into vcardOrg.getValues().get(0) !!
           String[] subItems = vcardOrg.getValues().get(0).split(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER);
           name =   subItems[0];
           units =  Arrays.asList(subItems).subList(1,subItems.length);
        }

        return it.cnr.iit.jscontact.tools.dto.Organization.builder()
                .name((!name.isEmpty()) ? name : null)
                .units((units!=null) ? units.toArray(new String[0]) : null)
                .pref(vcardOrg.getPref())
                .contexts(toJSCardContexts(vcardOrg.getType()))
                .label(toJSCardLabel(vcardOrg,vcard.getExtendedProperties()))
                .sortAs((vcardOrg.getSortAs()!=null && !vcardOrg.getSortAs().isEmpty()) ? vcardOrg.getSortAs().toArray(new String[0]) : null)
                .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardOrg, VCardParamEnum.PID, VCardParamEnum.GROUP))
                .build();
    }


    private void fillJSCardOrganizations(VCard vcard, Card jsCard) {

        if (vcard.getOrganizations() == null || vcard.getOrganizations().isEmpty())
            return;

        List<ezvcard.property.Organization> vcardOrgs = vcard.getOrganizations();
        Collections.sort(vcardOrgs, vCardPropertiesComparator);
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
                .author((authorName!=null || authorUri!=null) ?  Author.builder().name(authorName).uri(authorUri).build() : null)
                .created((created!=null) ? DateUtils.toCalendar(created) : null)
                .pref(vcardNote.getPref())
                .contexts(toJSCardContexts(vcardNote.getType()))
                .label(toJSCardLabel(vcardNote,vcard.getExtendedProperties()))
                .vCardParams(VCardUtils.getVCardUnmatchedParams(vcardNote, VCardParamEnum.PID, VCardParamEnum.GROUP))
                .build();
    }

    private void fillJSCardNotes(VCard vcard, Card jsCard) {

        if (vcard.getNotes() == null || vcard.getNotes().isEmpty())
            return;

        List<ezvcard.property.Note> vcardNotes = vcard.getNotes();
        Collections.sort(vcardNotes, vCardPropertiesComparator);
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

    private static void fillJSCardKeywords(VCard vcard, Card jsCard) {

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

    //TODO: replace XXXX with RFC number after draft-ietf-calext-vcard-jscontact-extensions
    private void fillJSCardPropsFromVCardRFCXXXXProps(VCard vcard, Card jsCard) {

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

            if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.LOCALE.getValue()))
                continue; // has already been set
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.CREATED.getValue()))
                jsCard.setCreated(DateUtils.toCalendar(extension.getValue()));
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.GRAMMATICAL_GENDER.getValue())) {
                GrammaticalGenderType gender = GrammaticalGenderType.getEnum(extension.getValue().toLowerCase());
                if (jsCard.getSpeakToAs() != null)
                    jsCard.getSpeakToAs().setGrammaticalGender(gender);
                else
                    jsCard.setSpeakToAs(SpeakToAs.builder().grammaticalGender(gender).build());
            }
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.PRONOUNS.getValue())) {
                String id = getJSCardId(VCard2JSContactIdsProfile.IdType.PRONOUNS, i,"PRONOUNS-" + (i++), extension.getParameter(VCardParamEnum.PROP_ID.getValue()));
                Pronouns pronouns = Pronouns.builder().pronouns(extension.getValue()).contexts(contexts).pref(pref).label(toJSCardLabel(extension,vcard.getExtendedProperties())).build();
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
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.CONTACT_CHANNEL_PREF.getValue())) {
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
            else if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.SOCIALSERVICE.getValue())) {
                i = (jsCard.getOnlineServices() != null) ? jsCard.getOnlineServices().size() + 1 : 1;
                vcardType = VCardUtils.getVCardParamValue(extension.getParameters(), VCardParamEnum.TYPE);
                contexts = toJSCardContexts(vcardType);
                jsCard.addOnlineService(getJSCardId(VCard2JSContactIdsProfile.IdType.ONLINE_SERVICE, i,"OS-" + (i++), extension.getParameter(VCardParamEnum.PROP_ID.getValue())), OnlineService.builder()
                        .user(extension.getValue())
                        .type((extension.getDataType() == null || extension.getDataType() == VCardDataType.URI) ? OnlineServiceType.uri() : OnlineServiceType.username())
                        .service(extension.getParameter(VCardParamEnum.SERVICE_TYPE.getValue()))
                        .contexts(contexts)
                        .pref(toJSCardPref(extension.getParameter(VCardParamEnum.PREF.getValue())))
                        .label(toJSCardLabel(extension,vcard.getExtendedProperties()))
                        .vCardParams(VCardUtils.getVCardUnmatchedParams(extension, VCardParamEnum.PID, VCardParamEnum.GROUP))
                        .build()
                );
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
        Object value = null;
        String extensionName = null;
        try {
            for (RawProperty extension : vcard.getExtendedProperties()) {
                if (extension.getPropertyName().equalsIgnoreCase(VCardPropEnum.JSCONTACT_PROP.getValue())) {
                    path = extension.getParameter(VCardParamEnum.JSPTR.getValue());
                    value = JSContactPropUtils.toJsonValue(extension.getValue());
                    if (!path.contains(DelimiterUtils.SLASH_DELIMITER)) {
                        jsCard.addExtension(path,value);
                    }
                    else {
                        String[] pathItems = path.split(DelimiterUtils.SLASH_DELIMITER);
                        extensionName = pathItems[pathItems.length-1];
                        List list = Arrays.asList(pathItems);
                        jsCard.addExtension(list.subList(0, pathItems.length-1),extensionName.replaceAll(DelimiterUtils.SLASH_DELIMITER_IN_JSON_POINTER,DelimiterUtils.SLASH_DELIMITER), value);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new CardException(String.format("Unable to convert X-RFC0000-PROP property with path: %s", path));
        }
    }

    private static void fillVCardUnmatchedProps(VCard vcard, Card jsCard) {

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
        RawProperty locale = vCard.getExtendedProperty(VCardPropEnum.LOCALE.getValue());
        jsCard.setLocale((locale!=null) ? locale.getValue() : config.getDefaultLanguage());
        vCardPropertiesComparator = VCardPropertiesComparator.builder().defaultLanguage(jsCard.getLocale()).build();
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
        fillJSCardPropsFromVCardRFCXXXXProps(vCard,jsCard);
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
