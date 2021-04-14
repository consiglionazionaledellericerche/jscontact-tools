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
import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.JCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import it.cnr.iit.jscontact.tools.dto.wrappers.CategoryWrapper;
import it.cnr.iit.jscontact.tools.dto.wrappers.MemberWrapper;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@NoArgsConstructor
public class EZVCard2JSContact extends AbstractConverter {

    private static final Pattern TIMEZONE_AS_UTC_OFFSET_PATTERN = Pattern.compile("[+-](\\d{2}):?(\\d{2})");

    private static final List<String> fakeExtensions = Collections.singletonList("contact-uri");

    protected VCard2JSContactConfig config;

    private static <E extends Enum<E> & JCardTypeDerivedEnum> E getEnumFromJCardType(Class<E> enumType, String jcardTypeParam, List<String> exclude, Map<String,E> aliases) {

        if (jcardTypeParam == null)
            return null;

        String[] items = jcardTypeParam.split(COMMA_ARRAY_DELIMITER);
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
            if (extension.getPropertyName().equals(propertyName) ||
                    extension.getPropertyName().equals(propertyName.toUpperCase())) {
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

    private static <E extends Enum<E> & JCardTypeDerivedEnum> Map<E,Boolean> getEnumMap(Class<E> enumType, String jcardTypeParam, List<String> exclude, Map<String, E> aliases) {

        if (jcardTypeParam == null)
            return null;

        Map<E,Boolean> enumMap = new HashMap<>();
        String[] typeItems = jcardTypeParam.split(COMMA_ARRAY_DELIMITER);
        for (String typeItem : typeItems) {
            try {
                E enumInstance = getEnumFromJCardType(enumType, typeItem, exclude, aliases);
                if (enumInstance != null)
                    enumMap.put(enumInstance, Boolean.TRUE);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return (enumMap.size() > 0) ? enumMap : null;
    }

    private static Map<Context,Boolean> getContexts(String jcardTypeParam) {
        return getEnumMap(Context.class,jcardTypeParam, null, Context.getAliases());
    }

    private static Map<AddressContext,Boolean> getAddressContexts(String jcardTypeParam) {
        return getEnumMap(AddressContext.class,jcardTypeParam, null, AddressContext.getAliases());
    }

    private static Map<PhoneFeature,Boolean> getPhoneFeatures(String jcardTypeParam) {
        Map<PhoneFeature,Boolean> phoneTypes = getEnumMap(PhoneFeature.class,jcardTypeParam, Arrays.asList("home", "work"), null);

        if (phoneTypes == null)
            phoneTypes = new HashMap<>();

        if (phoneTypes.size() == 0)
            phoneTypes.put(PhoneFeature.OTHER, Boolean.TRUE);

        return phoneTypes;
    }

    private static Map<PhoneFeature,Boolean> getDefaultPhoneFeatures() {

        return new HashMap<PhoneFeature,Boolean>(){{ put(PhoneFeature.VOICE, Boolean.TRUE);}};
    }

    private static String getLabel(String jcardTypeParam, String[] exclude, String[] include) {

        List<String> list = new ArrayList<>();

        if (include != null)
            list.addAll(Arrays.asList(include));

        if (jcardTypeParam == null)
            return (list.size()> 0) ? String.join(COMMA_ARRAY_DELIMITER,list) : null;

        String[] items = jcardTypeParam.split(COMMA_ARRAY_DELIMITER);
        for (String item : items) {
            if (item.toLowerCase().equals("home")) item = "private";

            if (exclude != null && Arrays.asList(exclude).contains(item))
                continue;

            list.add(item.toLowerCase());
        }

        return (list.size()> 0) ? String.join(COMMA_ARRAY_DELIMITER,list) : null;
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

    private static void addLocalizedString(List<LocalizedString> list, LocalizedString localized) {

        if (list.size() == 0)
            list.add(localized);
        else {
            LocalizedString ls = (LocalizedString) getAlternative(list, localized.getAltid());
            if (ls == null)
                list.add(localized);
            else {
                ls.addLocalization(localized.getLanguage(), localized.getValue());
                list.set(list.indexOf(ls), ls);
            }
        }
    }

    private static Integer getPreference(String jcardPref) {
        return (jcardPref != null) ? Integer.parseInt(jcardPref) : null;
    }

    private static String getJcardParam(VCardParameters parameters, String param) {
        try {
            List<String> values = parameters.get(param);
            if (values.size()==0) return null;
            return String.join(COMMA_ARRAY_DELIMITER, values);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private static LocalizedString getFulllAddress(ezvcard.property.Address addr) {

        String fullAddress = addr.getLabel();
        if (fullAddress == null) {
            StringJoiner joiner = new StringJoiner(AUTO_FULL_ADDRESS_DELIMITER);
            if (StringUtils.isNotEmpty(addr.getPoBox())) joiner.add(addr.getPoBox());
            if (StringUtils.isNotEmpty(addr.getExtendedAddressFull())) joiner.add(addr.getExtendedAddressFull());
            if (StringUtils.isNotEmpty(addr.getStreetAddressFull())) joiner.add(addr.getStreetAddressFull());
            if (StringUtils.isNotEmpty(addr.getLocality())) joiner.add(addr.getLocality());
            if (StringUtils.isNotEmpty(addr.getRegion())) joiner.add(addr.getRegion());
            if (StringUtils.isNotEmpty(addr.getPostalCode())) joiner.add(addr.getPostalCode());
            if (StringUtils.isNotEmpty(addr.getCountry())) joiner.add(addr.getCountry());
            fullAddress = joiner.toString();
        }
        return StringUtils.isNotEmpty(fullAddress) ? LocalizedString.builder().value(fullAddress).language(addr.getLanguage()).build() : null;
    }

    private static void addOnline(VCardProperty property, JSContact jsContact, OnlineLabelKey labelKey, int index) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        String jcardType;
        Context rcontext;
        String label;

        jcardType = getJcardParam(property.getParameters(), "TYPE");
        Map<Context,Boolean> contexts = getContexts(jcardType);
        label = getLabel(jcardType, (contexts != null) ? EnumUtils.toArrayOfStrings(contexts.keySet()) : null, new String[]{labelKey.getValue()});
        jsContact.addOnline(String.format("%s-%s",labelKey.getValue().toUpperCase(),index), Resource.builder()
                                    .resource(value)
                                    .type(ResourceType.URI)
                                    .label(label)
                                    .contexts(contexts)
                                    .mediaType(getJcardParam(property.getParameters(), "MEDIATYPE"))
                                    .pref(getPreference(getJcardParam(property.getParameters(), "PREF")))
                                    .build()
                           );

    }

    private static void addFile(String id, VCardProperty property, JSContact jsContact) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        jsContact.addPhoto(id, File.builder()
                                .href(value)
                                .mediaType(getJcardParam(property.getParameters(), "MEDIATYPE"))
                                .pref(getPreference(getJcardParam(property.getParameters(), "PREF")))
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
                    .fullAddress(LocalizedString.builder()
                                                .value(property.getText())
                                                .language(property.getLanguage())
                                                .build()
                                )
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

    private static String getValue(Timezone property) {

        if (property.getText()!= null)
            return property.getText();
        else {
            String offset = property.getOffset().toString();
            String sign = offset.substring(0,1);
            String hours = offset.substring(1,3);
            String minutes = offset.substring(3,5);
            return String.format("Etc/GMT%s%s%s",
                                           (hours.equals("00") && minutes.equals("00")) ? "" : (sign.equals("+") ? "-" : "+") ,
                                           (hours.equals("00") && minutes.equals("00")) ? "" : String.valueOf(Integer.parseInt(hours)),
                                           (minutes.equals("00") ? "" : ":" + minutes));
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

        return StringUtils.join(property.getValues(), SEMICOMMA_ARRAY_DELIMITER);
    }

    private static String getValue(Impp property) {

        return property.getUri().toString();
    }

    private static void fillFormattedNames(VCard vcard, JSContact jsContact) {

        List<FormattedName> fns = vcard.getFormattedNames();
        List<LocalizedString> fullNames = new ArrayList<>();
        for (FormattedName fn : fns) {
            fullNames.add(LocalizedString.builder()
                                         .value(getValue(fn))
                                         .language(fn.getLanguage())
                                         .preference(fn.getPref())
                                         .build()
                         );
        }
        Collections.sort(fullNames);
        for (LocalizedString ls : fullNames)
            jsContact.addFullName(ls.getValue(), ls.getLanguage());
    }

    private static void fillMembers(VCard vcard, JSCardGroup jsCardGroup) {

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

    private static void fillNames(VCard vcard, JSContact jsContact) {

        List<StructuredName> sns = vcard.getStructuredNames();
        for (StructuredName sn : sns) {
            for (String px : sn.getPrefixes())
                jsContact.addName(NameComponent.builder()
                        .value(px)
                        .type(NameComponentType.PREFIX)
                        .build()
                );
            if (sn.getGiven() != null)
                jsContact.addName(NameComponent.builder()
                        .value(sn.getGiven())
                        .type(NameComponentType.PERSONAL)
                        .build()
                );
            if (sn.getFamily() != null)
                jsContact.addName(NameComponent.builder()
                                               .value(sn.getFamily())
                                               .type(NameComponentType.SURNAME)
                                               .build()
                                 );
            for (String an : sn.getAdditionalNames())
                jsContact.addName(NameComponent.builder()
                                               .value(an)
                                               .type(NameComponentType.ADDITIONAL)
                                               .build()
                                 );
            for (String sx : sn.getSuffixes())
                jsContact.addName(NameComponent.builder()
                                               .value(sx)
                                               .type(NameComponentType.SUFFIX)
                                               .build()
                                 );
            addUnmatchedParams(sn, "N", null, new String[]{"SORT-AS"}, jsContact);
        }

    }

    private static void fillNickNames(VCard vcard, JSContact jsContact) {

        List<Nickname> nicknames = vcard.getNicknames();
        List<LocalizedString> nicks = new ArrayList<>();
        for (Nickname nickname : nicknames) {
            for (String value : nickname.getValues())
                nicks.add(LocalizedString.builder()
                                         .value(value)
                                         .preference(nickname.getPref())
                                         .build()
                         );
        }
        Collections.sort(nicks);
        for (LocalizedString nick : nicks)
            jsContact.addNickName(nick);
    }

    private static it.cnr.iit.jscontact.tools.dto.Address getAddressAltrenative(List<it.cnr.iit.jscontact.tools.dto.Address> addresses, String altid) {

        it.cnr.iit.jscontact.tools.dto.Address address = (it.cnr.iit.jscontact.tools.dto.Address) getAlternative(addresses, altid);
        int ind = (address != null) ? addresses.indexOf(address) : 0;
        return addresses.get(ind);
    }

    private static String getTimezoneName(String jcardTzParam) {

        if (jcardTzParam == null)
            return null;

        Matcher m = TIMEZONE_AS_UTC_OFFSET_PATTERN.matcher(jcardTzParam);
        if (m.find())
            return getValue(new Timezone(UtcOffset.parse(jcardTzParam.replace(":", ""))));
        else
            return jcardTzParam;
    }

    private static String getGeoUri(GeoUri jcardGeoParam) {

        return  (jcardGeoParam != null) ? jcardGeoParam.toUri().toString() : null;
    }

    private static void fillAddresses(VCard vcard, JSContact jsContact) {

        List<it.cnr.iit.jscontact.tools.dto.Address> addresses = new ArrayList<>();

        String tz;
        String geo;
        for (ezvcard.property.Address addr : vcard.getAddresses()) {

            String jcardType = getJcardParam(addr.getParameters(), "TYPE");
            tz = getTimezoneName(addr.getTimezone());
            geo = getGeoUri(addr.getGeo());
            String cc = addr.getParameter("CC");

            addresses.add(it.cnr.iit.jscontact.tools.dto.Address.builder()
                                                                 .contexts(getAddressContexts(jcardType))
                                                                 .fullAddress(getFulllAddress(addr))
                                                                 .pref(addr.getPref())
                                                                 .coordinates(geo)
                                                                 .timeZone(tz)
                                                                 .countryCode(cc)
                                                                 .postOfficeBox(StringUtils.defaultIfEmpty(addr.getPoBox(), null))
                                                                 .extension(StringUtils.defaultIfEmpty(addr.getExtendedAddressFull(), null))
                                                                 .street(StringUtils.defaultIfEmpty(addr.getStreetAddressFull(), null))
                                                                 .locality(StringUtils.defaultIfEmpty(addr.getLocality(), null))
                                                                 .region(StringUtils.defaultIfEmpty(addr.getRegion(), null))
                                                                 .postcode(StringUtils.defaultIfEmpty(addr.getPostalCode(), null))
                                                                 .country(StringUtils.defaultIfEmpty(addr.getCountry(), null))
                                                                 .altid(addr.getAltId())
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

        int i = 1;
        for (it.cnr.iit.jscontact.tools.dto.Address address : addresses)
            jsContact.addAddress("ADR-" + (i++), address);

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
            System.out.println(e.getMessage());
        }

        return null;
    }


    private static void fillAnniversaries(VCard vcard, JSContact jsContact) {

      if (vcard.getBirthday() != null) {
          jsContact.addAnniversary(it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.BIRTH)
                                                                             .date(getAnniversaryDate(vcard.getBirthday()))
                                                                             .place(getValue(vcard.getBirthplace()))
                                                                             .build()
                                  );
          if (vcard.getBirthday().getCalscale()!= null && !vcard.getBirthday().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getBirthday(), "BDAY", null, new String[]{"CALSCALE"}, jsContact);
      }

      if (vcard.getDeathdate() != null) {
          jsContact.addAnniversary(it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.DEATH)
                                                                             .date(getAnniversaryDate(vcard.getDeathdate()))
                                                                             .place(getValue(vcard.getDeathplace()))
                                                                             .build()
                                  );
          if (vcard.getDeathdate().getCalscale()!= null && !vcard.getDeathdate().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getDeathdate(), "DEATHDATE", null, new String[]{"CALSCALE"}, jsContact);
      }

      if (vcard.getAnniversary() != null) {
          jsContact.addAnniversary(it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                              .type(AnniversaryType.OTHER)
                                                                              .date(getAnniversaryDate(vcard.getAnniversary()))
                                                                              .label(ANNIVERSAY_MARRIAGE_LABEL)
                                                                              .build()
                                   );
          if (vcard.getAnniversary().getCalscale()!= null && !vcard.getAnniversary().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getAnniversary(), "ANNIVERSARY", null, new String[]{"CALSCALE"}, jsContact);
      }
    }

    private static PersonalInformationLevel getLevel(String jcardLevelParam) throws CardException {
        try {
            return PersonalInformationLevel.getEnum(jcardLevelParam);
        } catch (IllegalArgumentException e) {
            throw new CardException("Unknown LEVEL value " + jcardLevelParam);
        }
    }

    private static void fillPersonalInfos(VCard vcard, JSContact jsContact) throws CardException {

        List<PersonalInformation> hobbies = new ArrayList<>();
        List<PersonalInformation> interests = new ArrayList<>();
        List<PersonalInformation> expertizes = new ArrayList<>();

        for (Hobby hobby : vcard.getHobbies()) {
            hobbies.add(PersonalInformation.builder()
                                            .type(PersonalInformationType.HOBBY)
                                            .value(getValue(hobby))
                                            .level((hobby.getLevel() != null) ? getLevel(hobby.getLevel().getValue()) : null)
                                            .index(hobby.getIndex())
                                            .build()
                       );
        }

        if (hobbies.size() > 0) {
            Collections.sort(hobbies);
            for (PersonalInformation pi : hobbies)
                jsContact.addPersonalInfo(pi);
        }

        for (Interest interest : vcard.getInterests()) {
            interests.add(PersonalInformation.builder()
                                             .type(PersonalInformationType.INTEREST)
                                             .value(getValue(interest))
                                             .level((interest.getLevel() != null) ? getLevel(interest.getLevel().getValue()) : null)
                                             .index(interest.getIndex())
                                             .build()
                          );
        }

        if (interests.size() > 0) {
            Collections.sort(interests);
            for (PersonalInformation pi : interests)
                jsContact.addPersonalInfo(pi);
        }

        for (Expertise expertise : vcard.getExpertise()) {
            expertizes.add(PersonalInformation.builder()
                                              .type(PersonalInformationType.EXPERTISE)
                                              .value(getValue(expertise))
                                              .level((expertise.getLevel() != null) ? getLevel(expertise.getLevel().getValue()) : null)
                                              .index(expertise.getIndex())
                                              .build()
                           );
        }

        if (expertizes.size() > 0) {
            Collections.sort(expertizes);
            for (PersonalInformation pi : expertizes)
                jsContact.addPersonalInfo(pi);
        }

    }

    private static void fillContactLanguages(VCard vcard, JSContact jsContact) {

        for (Language lang : vcard.getLanguages()) {
            jsContact.addContactLanguage(getValue(lang),
                                        ContactLanguage.builder()
                                                       .context((lang.getType() != null) ? Context.getEnum(lang.getType()) : null)
                                                       .pref(lang.getPref())
                                                       .build()
                                        );
        }
    }


    private static boolean labelIncludesTelTypes(String label) {

        if (label == null)
            return false;

        for (String labelItems : label.split(COMMA_ARRAY_DELIMITER)) {
            if (!label.equals("private") && !label.equals("work"))
                return true;
        }

        return false;
    }

    private static void fillPhones(VCard vcard, JSContact jsContact) {

        int i = 1;
        for (Telephone tel : vcard.getTelephoneNumbers()) {
            String jcardType = getJcardParam(tel.getParameters(), "TYPE");
            Map<Context,Boolean> contexts = getContexts(jcardType);
            Map<PhoneFeature,Boolean> phoneFeatures = getPhoneFeatures(jcardType);
            String[] exclude = null;
            if (contexts != null) exclude = ArrayUtils.addAll(null, EnumUtils.toArrayOfStrings(contexts.keySet()));
            if (!phoneFeatures.containsKey(PhoneFeature.OTHER)) exclude = ArrayUtils.addAll(exclude, EnumUtils.toArrayOfStrings(phoneFeatures.keySet()));
            String label = getLabel(jcardType, exclude, null);
            jsContact.addPhone("PHONE-" + (i++), Phone.builder()
                                       .phone(getValue(tel))
                                       .features((phoneFeatures.containsKey(PhoneFeature.OTHER) && !labelIncludesTelTypes(label)) ? getDefaultPhoneFeatures() : phoneFeatures)
                                       .contexts(contexts)
                                       .label(label)
                                       .pref(tel.getPref())
                                       .build()
                              );
        }
    }

    private static void fillEmails(VCard vcard, JSContact jsContact) {

        int i = 1;
        for (Email email : vcard.getEmails()) {
            String emailAddress = getValue(email);
            if (StringUtils.isNotEmpty(emailAddress)) {
                String jcardType = getJcardParam(email.getParameters(), "TYPE");
                jsContact.addEmail("EMAIL-" + (i++), EmailAddress.builder()
                        .email(emailAddress)
                        .contexts(getContexts(jcardType))
                        .pref(email.getPref())
                        .build()
                );
            }
        }
    }

    private static void fillPhotos(VCard vcard, JSContact jsContact) {

        int i = 1;
        for (Photo photo : vcard.getPhotos())
            addFile("PHOTO-" + (i++), photo, jsContact);

    }

    private static void fillOnlines(VCard vcard, JSContact jsContact) {

        String jcardType;
        Map<Context,Boolean> contexts;
        List<Resource> orgDirectories = new ArrayList<>();

        int i = 1;
        for (Source source : vcard.getSources())
            addOnline(source, jsContact, OnlineLabelKey.SOURCE, i++);

        i = 1;
        for (Impp impp : vcard.getImpps()) {
            jcardType = getJcardParam(impp.getParameters(), "TYPE");
            contexts = getContexts(jcardType);
            jsContact.addOnline("XMPP-" + (i++), Resource.builder()
                                        .resource(getValue(impp))
                                        .type(ResourceType.USERNAME)
                                        .contexts(contexts)
                                        .label(getLabel(jcardType, (contexts != null) ? EnumUtils.toArrayOfStrings(contexts.keySet()) : null, new String[]{OnlineLabelKey.IMPP.getValue()}))
                                        .pref(impp.getPref())
                                        .mediaType(impp.getMediaType())
                                        .build()
                               );
        }

        i = 1;
        for (Logo logo : vcard.getLogos())
            addOnline(logo, jsContact, OnlineLabelKey.LOGO, i++);

        i = 1;
        for (Sound sound : vcard.getSounds())
            addOnline(sound, jsContact, OnlineLabelKey.SOUND, i++);

        i = 1;
        for (Url url : vcard.getUrls())
            addOnline(url, jsContact, OnlineLabelKey.URL, i++);

        i = 1;
        for (Key key : vcard.getKeys())
            addOnline(key, jsContact, OnlineLabelKey.KEY, i++);

        i = 1;
        for (FreeBusyUrl fburl : vcard.getFbUrls())
            addOnline(fburl, jsContact, OnlineLabelKey.FBURL, i++);

        i = 1;
        for (CalendarRequestUri calendarRequestUri : vcard.getCalendarRequestUris())
            addOnline(calendarRequestUri, jsContact, OnlineLabelKey.CALADRURI, i++);

        i = 1;
        for (CalendarUri calendarUri : vcard.getCalendarUris())
            addOnline(calendarUri, jsContact, OnlineLabelKey.CALURI, i++);

        for (OrgDirectory od : vcard.getOrgDirectories()) {
            jcardType = od.getType();
            contexts = getContexts(jcardType);
            orgDirectories.add(Resource.builder()
                                       .resource(getValue(od))
                                       .type(ResourceType.URI)
                                       .label(getLabel(jcardType, (contexts != null) ? EnumUtils.toArrayOfStrings(contexts.keySet()) : null, new String[]{OnlineLabelKey.ORG_DIRECTORY.getValue()}))
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
                jsContact.addOnline("ORG-DIRECTORY-" + (i++), ol);
        }

        List<RawProperty> contactUris = getRawProperties(vcard, OnlineLabelKey.CONTACT_URI.getValue());
        i = 1;
        for (RawProperty contactUri : contactUris) {
            UriProperty uriProperty = new UriProperty(getValue(contactUri));
            uriProperty.setParameters(contactUri.getParameters());
            addOnline(uriProperty, jsContact, OnlineLabelKey.CONTACT_URI, i++);
        }
    }

    private static void fillTitles(VCard vcard, JSContact jsContact) {

        List<LocalizedString> titles = new ArrayList<>();
        for (Title title : vcard.getTitles())
            addLocalizedString(titles, LocalizedString.builder()
                                                     .value(getValue(title))
                                                     .language(title.getLanguage())
                                                     .altid(title.getAltId())
                                                     .preference(title.getPref())
                                                     .build()
                              );
        Collections.sort(titles);

        int i = 1;
        for (LocalizedString title : titles)
            jsContact.addTitle("TITLE-" + (i++), title);
    }

    private static void fillRoles(VCard vcard, JSContact jsContact) {

        List<LocalizedString> roles = new ArrayList<>();
        for (Role role : vcard.getRoles()) {
            addLocalizedString(roles, LocalizedString.builder()
                                                     .value(getValue(role))
                                                     .language(role.getLanguage())
                                                     .altid(role.getAltId())
                                                     .preference(role.getPref())
                                                     .build()
                              );
        }
        Collections.sort(roles);

        int i = (jsContact.getTitles() != null) ? jsContact.getTitles().size() + 1 : 1;
        for (LocalizedString role : roles)
            jsContact.addTitle("TITLE-" + (i++), role);
    }

    private static Map<String,String> getOrganizationItemLocalizations(LocalizedString organization, int organizationItemIndex) {

        Map<String, String> localizations = new HashMap<>();
        for (Map.Entry<String, String> unitLocalization : organization.getLocalizations().entrySet()) {
            String[] localizationItems = unitLocalization.getValue().split(SEMICOMMA_ARRAY_DELIMITER);
            localizations.put(unitLocalization.getKey(), localizationItems[organizationItemIndex]);
        }
        return localizations;
    }

    private static void fillOrganizations(VCard vcard, JSContact jsContact) {

        List<LocalizedString> organizations = new ArrayList<>();
        for (Organization org : vcard.getOrganizations()) {
            addLocalizedString(organizations, LocalizedString.builder()
                                                             .value(getValue(org))
                                                             .language(org.getLanguage())
                                                             .altid(org.getAltId())
                                                             .preference(org.getPref())
                                                             .build()
                              );
        }
        Collections.sort(organizations);

        List<it.cnr.iit.jscontact.tools.dto.Organization> jsContactOrganizations = new ArrayList<>();
        for (LocalizedString organization : organizations) {
            String[] nameItems = organization.getValue().split(SEMICOMMA_ARRAY_DELIMITER);
            LocalizedString name = LocalizedString.builder()
                                                  .value(nameItems[0])
                                                  .language(organization.getLanguage())
                                                  .build();

            if (organization.getLocalizations() != null)
                name.setLocalizations(getOrganizationItemLocalizations(organization,0));

            if (nameItems.length > 0) {
                List<LocalizedString> units = new ArrayList<>();
                for (int j = 1; j < nameItems.length; j++) {
                    if (nameItems[j].isEmpty())
                        continue;
                    if (organization.getLocalizations() != null)
                        units.add(LocalizedString.builder()
                                                 .value(nameItems[j])
                                                 .language(organization.getLanguage())
                                                 .localizations(getOrganizationItemLocalizations(organization, j))
                                                 .build()
                                 );
                    else
                        units.add(LocalizedString.builder()
                                                 .value(nameItems[j])
                                                 .language(organization.getLanguage())
                                                 .build()
                                 );
                }
                jsContactOrganizations.add(it.cnr.iit.jscontact.tools.dto.Organization.builder()
                                                                                      .name(name)
                                                                                      .units(units.toArray(new LocalizedString[units.size()]))
                                                                                      .build()
                                          );
            }
            else
                jsContactOrganizations.add(it.cnr.iit.jscontact.tools.dto.Organization.builder()
                                                                                       .name(name)
                                                                                       .build()
                                          );
        }

        int i = 1;
        for (it.cnr.iit.jscontact.tools.dto.Organization jsContactOrganization : jsContactOrganizations)
            jsContact.addOrganization("ORG-" + (i++), jsContactOrganization);
    }

    private static void fillNotes(VCard vcard, JSContact jsContact) {

        List<LocalizedString> notes = new ArrayList<>();
        for (Note note : vcard.getNotes()) {
            addLocalizedString(notes, LocalizedString.builder()
                                                     .value(getValue(note))
                                                     .language(note.getLanguage())
                                                     .altid(note.getAltId())
                                                     .preference(note.getPref())
                                                     .build()
                              );
        }

        for (LocalizedString note : notes) {
            jsContact.addNote(note.getValue(), note.getLanguage());
            if (note.getLocalizations()!=null) {
                for (Map.Entry<String,String> localization : note.getLocalizations().entrySet())
                    jsContact.addNote(localization.getValue(), localization.getKey());
            }
        }
    }

    private static void fillCategories(VCard vcard, JSContact jsContact) {

        List<CategoryWrapper> wrappers = new ArrayList<>();
        for (Categories categories : vcard.getCategoriesList()) {
            wrappers.add(CategoryWrapper.builder()
                                        .values(categories.getValues())
                                        .preference(categories.getPref())
                                        .build()
                        );
        }
        Collections.sort(wrappers);
        for (CategoryWrapper wrapper : wrappers)
            jsContact.addCategories(wrapper.getValues().toArray(new String[wrapper.getValues().size()]));
    }

    private static String getValue(List<RelatedType> list) {
        StringJoiner joiner = new StringJoiner(COMMA_ARRAY_DELIMITER);
        for (RelatedType el : list)
            joiner.add(el.getValue());
        return joiner.toString();
    }

    private static void fillRelations(VCard vcard, JSContact jsContact) {

        for (Related related : vcard.getRelations()) {
            if (getJcardParam(related.getParameters(), "TYPE") == null)
                jsContact.addRelation(getValue(related), null);
            else {
                for (RelatedType type : related.getTypes())
                    jsContact.addRelation(getValue(related), RelationType.getEnum(type.getValue()));
            }
        }
    }

    private String getExtPropertyName(String propertyName) {

        return (config.getExtensionsPrefix() != null) ? config.getExtensionsPrefix() + propertyName : propertyName;
    }

    private String getExtParamName(String propertyName, String paramName) {

        return getExtPropertyName(propertyName) + "/" + paramName;
    }

    private void fillExtensions(VCard vcard, JSContact jsContact) {

        for (RawProperty extension : vcard.getExtendedProperties()) {
            if (!fakeExtensions.contains(extension.getPropertyName()) &&
                    !fakeExtensions.contains(extension.getPropertyName().toUpperCase())) {
                jsContact.addExtension(getExtPropertyName(extension.getPropertyName()), getValue(extension));
            }
        }
    }

    private static void addUnmatchedParams(VCardProperty property, String propertyName, Integer index, String[] unmatchedParams, JSContact jsContact) {

        if (unmatchedParams == null)
            return;

        if (property.getParameters() == null)
            return;

        if (property.getGroup() != null) {
            jsContact.addExtension(getUnmatchedParamName(propertyName,"GROUP"),
                                   property.getGroup()
                                  );
        }

        if (property.getParameters().getPids() != null && property.getParameters().getPids().size()>0) {
            StringJoiner joiner = new StringJoiner(COMMA_ARRAY_DELIMITER);
            for (Pid pid : property.getParameters().getPids())
                joiner.add(pid.getLocalId().toString());
            jsContact.addExtension(getUnmatchedParamName(propertyName,"PID"),
                                   joiner.toString()
                                  );
        }

        for (String param : unmatchedParams) {
            if (property.getParameter(param) != null)
                jsContact.addExtension(getUnmatchedParamName(propertyName,param),
                                       property.getParameter(param)
                                      );
        }
    }

    private static void fillUnmatchedElments(VCard vcard, JSContact jsContact) {

        if (vcard.getGender()!=null) {
            if (vcard.getGender().getGender() != null)
                jsContact.addExtension(getUnmatchedPropertyName(VCARD_GENDER_TAG), vcard.getGender().getGender());
            else
                jsContact.addExtension(getUnmatchedPropertyName(VCARD_GENDER_TAG), vcard.getGender().getText());
        }

        if (vcard.getClientPidMaps()!=null) {
            for (ClientPidMap pidmap : vcard.getClientPidMaps())
                jsContact.addExtension(getUnmatchedPropertyName(VCARD_CLIENTPIDMAP_TAG, pidmap.getPid()), pidmap.getUri());
        }

        if (vcard.getXmls()!=null) {
            if (vcard.getXmls().size() == 1)
                jsContact.addExtension(getUnmatchedPropertyName(VCARD_XML_TAG), vcard.getXmls().get(0).getValue().getTextContent());
            else {
                int i = 0;
                for (Xml xml : vcard.getXmls())
                    jsContact.addExtension(getUnmatchedPropertyName(String.format("XML/%d", i++)), xml.getValue().getTextContent());
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
            return KindType.builder().rfcValue(it.cnr.iit.jscontact.tools.dto.Kind.getEnum(getValue(kind))).build();
        } catch (IllegalArgumentException e) {
            return KindType.builder().extValue(getValue(kind)).build();
        }
    }

    private JSContact convert(VCard vCard) throws CardException {

        JSContact jsContact;
        if (vCard.getMembers() != null && vCard.getMembers().size() != 0) {
            JSCardGroup jsCardGroup = JSCardGroup.builder().uid(UUID.randomUUID().toString()).build();
            fillMembers(vCard, jsCardGroup);
            jsContact = jsCardGroup;
        } else {
            jsContact = JSCard.builder().uid(UUID.randomUUID().toString()).build();
        }
        jsContact.setKind(getKind(vCard.getKind()));
        jsContact.setProdId(getValue(vCard.getProductId()));
        jsContact.setUpdated(getUpdated(vCard.getRevision()));
        if (vCard.getUid()!=null)
            jsContact.setUid(vCard.getUid().getValue());
        fillFormattedNames(vCard, jsContact);
        fillNames(vCard, jsContact);
        fillNickNames(vCard, jsContact);
        fillAddresses(vCard, jsContact);
        fillAnniversaries(vCard, jsContact);
        fillPersonalInfos(vCard, jsContact);
        fillContactLanguages(vCard, jsContact);
        fillPhones(vCard, jsContact);
        fillEmails(vCard, jsContact);
        fillPhotos(vCard, jsContact);
        fillOnlines(vCard, jsContact);
        fillTitles(vCard, jsContact);
        fillRoles(vCard, jsContact);
        fillOrganizations(vCard, jsContact);
        fillCategories(vCard, jsContact);
        fillNotes(vCard, jsContact);
        fillRelations(vCard, jsContact);
        fillExtensions(vCard, jsContact);
        fillUnmatchedElments(vCard, jsContact);

        return jsContact;
    }

    /**
     * Converts a list of vCard v4.0 instances [RFC6350] into a list of JSContact objects.
     * JSContact is defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param vCards a list of instances of the ez-vcard library VCard class
     * @return a list of JSContact objects
     * @throws CardException if one of the vCard instances is not v4.0 compliant
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    public List<JSContact> convert(List<VCard> vCards) throws CardException {

        List<JSContact> jsContacts = new ArrayList<>();

        for (VCard vCard : vCards) {
            if (config.isCardToValidate()) {
                ValidationWarnings warnings = vCard.validate(VCardVersion.V4_0);
                if (!warnings.isEmpty())
                    throw new CardException(warnings.toString());
            }
            jsContacts.add(convert(vCard));
        }

        return jsContacts;
    }

}
