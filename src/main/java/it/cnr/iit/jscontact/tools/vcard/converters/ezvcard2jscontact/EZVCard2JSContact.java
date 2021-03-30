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
import ezvcard.util.UtcOffset;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.JCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.dto.wrappers.CategoryWrapper;
import it.cnr.iit.jscontact.tools.dto.wrappers.MemberWrapper;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
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

        List<RawProperty> rawProperties = new ArrayList<RawProperty>();
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

    private static ResourceContext getResourceContext(String jcardTypeParam) {

         return getEnumFromJCardType(ResourceContext.class, jcardTypeParam, null, ResourceContext.getAliases());
    }

    private static AddressContext getAddressContext(String jcardTypeParam) {

        return getEnumFromJCardType(AddressContext.class, jcardTypeParam, null, AddressContext.getAliases());
    }

    private static PhoneResourceType getPhoneResourceType(String jcardTypeParam) {

        PhoneResourceType phoneType = getEnumFromJCardType(PhoneResourceType.class, jcardTypeParam, new ArrayList<>(Arrays.asList("home", "work")), null);
        return (phoneType != null) ? phoneType : PhoneResourceType.OTHER;
    }

    private static Map<String, Boolean> getLabels(String jcardTypeParam, String[] exclude, String[] include) {

        Map<String, Boolean> map = new HashMap<String, Boolean>();

        if (include != null) {
            for (String in : include)
                map.put(in, Boolean.TRUE);
        }

        if (jcardTypeParam == null)
            return map;

        String[] items = jcardTypeParam.split(COMMA_ARRAY_DELIMITER);
        for (String item : items) {
            if (item.toLowerCase().equals("home")) item = "private";

            if (exclude != null && Arrays.asList(exclude).contains(item))
                continue;

            map.put(item.toLowerCase(), Boolean.TRUE);
        }

        return (map.size()!=0) ? map : null;
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

    private static Boolean getPreference(String jcardPref) {
        return (jcardPref != null && jcardPref.equals("1")) ? Boolean.TRUE : null;
    }

    private static Boolean getPreference(Integer jcardPref) {
        return (jcardPref != null && jcardPref == 1) ? Boolean.TRUE : null;
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

    private static void addOnline(VCardProperty property, JSContact jsContact, LabelKey labelKey) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        String jcardType;
        ResourceContext rcontext;
        Map<String, Boolean> labels = new HashMap<String, Boolean>();

        jcardType = getJcardParam(property.getParameters(), "TYPE");
        rcontext = getResourceContext(jcardType);
        labels = getLabels(jcardType, (rcontext != null) ? new String[]{rcontext.getValue()} : null, new String[]{labelKey.getValue()});
        jsContact.addOnline(Resource.builder()
                                    .value(value)
                                    .type(OnlineResourceType.URI.getValue())
                                    .labels(labels)
                                    .context(rcontext)
                                    .mediaType(getJcardParam(property.getParameters(), "MEDIATYPE"))
                                    .isPreferred(getPreference(getJcardParam(property.getParameters(), "PREF")))
                                    .build()
                           );

    }

    private static void addFile(VCardProperty property, JSContact jsContact) {

        String value;
        if (property instanceof UriProperty)
            value = getValue((UriProperty) property);
        else
            value = getValue((BinaryProperty) property);

        jsContact.addPhoto(File.builder()
                                .href(value)
                                .mediaType(getJcardParam(property.getParameters(), "MEDIATYPE"))
                                .isPreferred(getPreference(getJcardParam(property.getParameters(), "PREF")))
                                .build()
                          );

    }

    private static String getValue(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.of(cal.getTimeZone().getID()));
        ZonedDateTime zdt = ZonedDateTime.of(ldt,ZoneId.of(cal.getTimeZone().getID()));
        return DateTimeFormatter.ISO_INSTANT.format(zdt);

    }

    private static boolean hasTime(Calendar cal) {

        if (cal.get(Calendar.HOUR_OF_DAY) > 0)
            return true;

        if (cal.get(Calendar.MINUTE) > 0)
            return true;

        if (cal.get(Calendar.SECOND) > 0)
            return true;

        return cal.get(Calendar.MILLISECOND) > 0;

    }

    private static ZoneOffset getZoneOffset(Calendar cal) {

        int gmtOffset = cal.get(Calendar.ZONE_OFFSET);
        int hours = gmtOffset / (60 * 60 * 1000);
        int minutes = Math.abs((gmtOffset % hours) / (60 * 1000));
        return ZoneOffset.ofHoursMinutes(hours, minutes);
    }

    private static String getValue(Calendar cal) {

        if (cal == null)
            return null;

        if (hasTime(cal)) {
            int offset = cal.get(Calendar.ZONE_OFFSET);
            if (offset == 0) {
                return LocalDateTime.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND)
                ).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+"Z";
            } else {
                return OffsetDateTime.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND),
                        0,
                        getZoneOffset(cal)
                ).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
        } else {
            return LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            ).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
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
            return getValue(property.getCalendar());
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
        List<LocalizedString> fullNames = new ArrayList<LocalizedString>();
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

        List<MemberWrapper> wrappers = new ArrayList<MemberWrapper>();
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
            if (sn.getFamily() != null)
                jsContact.addName(NameComponent.builder()
                                               .value(sn.getFamily())
                                               .type(NameComponentType.SURNAME)
                                               .build()
                                 );

            if (sn.getGiven() != null)
                jsContact.addName(NameComponent.builder()
                                               .value(sn.getGiven())
                                               .type(NameComponentType.PERSONAL)
                                               .build()
                                 );

            for (String an : sn.getAdditionalNames())
                jsContact.addName(NameComponent.builder()
                                               .value(an)
                                               .type(NameComponentType.ADDITIONAL)
                                               .build()
                                 );

            for (String px : sn.getPrefixes())
                jsContact.addName(NameComponent.builder()
                                               .value(px)
                                               .type(NameComponentType.PREFIX)
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
        List<LocalizedString> nicks = new ArrayList<LocalizedString>();
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

        List<it.cnr.iit.jscontact.tools.dto.Address> addresses = new ArrayList<it.cnr.iit.jscontact.tools.dto.Address>();

        String tz;
        String geo;
        for (ezvcard.property.Address addr : vcard.getAddresses()) {

            String jcardType = getJcardParam(addr.getParameters(), "TYPE");
            tz = getTimezoneName(addr.getTimezone());
            geo = getGeoUri(addr.getGeo());
            String cc = addr.getParameter("CC");

            addresses.add(it.cnr.iit.jscontact.tools.dto.Address.builder()
                                                                 .context(getAddressContext(jcardType))
                                                                 .fullAddress(getFulllAddress(addr))
                                                                 .isPreferred(getPreference(addr.getPref()))
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

        for (it.cnr.iit.jscontact.tools.dto.Address address : addresses)
            jsContact.addAddress(address);

    }


    private static void fillAnniversaries(VCard vcard, JSContact jsContact) {

      if (vcard.getBirthday() != null) {
          jsContact.addAnniversary(it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.BIRTH)
                                                                             .date(getValue(vcard.getBirthday()))
                                                                             .place(getValue(vcard.getBirthplace()))
                                                                             .build()
                                  );
          if (vcard.getBirthday().getCalscale()!= null && !vcard.getBirthday().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getBirthday(), "BDAY", null, new String[]{"CALSCALE"}, jsContact);
      }

      if (vcard.getDeathdate() != null) {
          jsContact.addAnniversary(it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                             .type(AnniversaryType.DEATH)
                                                                             .date(getValue(vcard.getDeathdate()))
                                                                             .place(getValue(vcard.getDeathplace()))
                                                                             .build()
                                  );
          if (vcard.getDeathdate().getCalscale()!= null && !vcard.getDeathdate().getCalscale().getValue().equals(DEFAULT_CALSCALE))
              addUnmatchedParams(vcard.getDeathdate(), "DEATHDATE", null, new String[]{"CALSCALE"}, jsContact);
      }

      if (vcard.getAnniversary() != null) {
          jsContact.addAnniversary(it.cnr.iit.jscontact.tools.dto.Anniversary.builder()
                                                                              .type(AnniversaryType.OTHER)
                                                                              .date(getValue(vcard.getAnniversary()))
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

        List<PersonalInformation> hobbies = new ArrayList<PersonalInformation>();
        List<PersonalInformation> interests = new ArrayList<PersonalInformation>();
        List<PersonalInformation> expertizes = new ArrayList<PersonalInformation>();

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
                                                       .type(lang.getType())
                                                       .preference(lang.getPref())
                                                       .build()
                                        );
        }
    }


    private static boolean labelsIncludesTelTypes(Map<String, Boolean> labels) {

        if (labels == null)
            return false;

        for (String label : labels.keySet()) {
            if (!label.equals("private") && !label.equals("work"))
                return true;
        }

        return false;
    }

    private static void fillPhones(VCard vcard, JSContact jsContact) {

        for (Telephone tel : vcard.getTelephoneNumbers()) {
            String jcardType = getJcardParam(tel.getParameters(), "TYPE");
            ResourceContext rcontext = getResourceContext(jcardType);
            PhoneResourceType telType = getPhoneResourceType(jcardType);
            String[] exclude = null;
            if (rcontext != null) exclude = ArrayUtils.add(exclude, rcontext.getValue());
            if (telType != PhoneResourceType.OTHER) exclude = ArrayUtils.add(exclude, telType.getValue());
            Map<String, Boolean> labels = getLabels(jcardType, exclude, null);
            jsContact.addPhone(Resource.builder()
                                       .value(getValue(tel))
                                       .type((telType == PhoneResourceType.OTHER && !labelsIncludesTelTypes(labels)) ? PhoneResourceType.VOICE.getValue() : telType.getValue())
                                       .context(rcontext)
                                       .labels(labels)
                                       .isPreferred(getPreference(tel.getPref()))
                                       .build()
                              );
        }
    }

    private static void fillEmails(VCard vcard, JSContact jsContact) {

        for (Email email : vcard.getEmails()) {
            String jcardType = getJcardParam(email.getParameters(), "TYPE");
            ResourceContext rcontext = getResourceContext(jcardType);
            jsContact.addEmail(Resource.builder()
                                       .value(getValue(email))
                                       .context(rcontext)
                                       .labels(getLabels(jcardType, (rcontext != null) ? new String[]{rcontext.getValue()} : null, null))
                                       .isPreferred(getPreference(email.getPref()))
                                       .build()
                              );
        }
    }

    private static void fillOnlines(VCard vcard, JSContact jsContact) {

        String jcardType;
        ResourceContext rcontext;
        List<Resource> orgDirectories = new ArrayList<Resource>();

        for (Source source : vcard.getSources())
            addOnline(source, jsContact, LabelKey.SOURCE);

        for (Photo photo : vcard.getPhotos())
            addFile(photo, jsContact);

        for (Impp impp : vcard.getImpps()) {
            jcardType = getJcardParam(impp.getParameters(), "TYPE");
            rcontext = getResourceContext(jcardType);
            jsContact.addOnline(Resource.builder()
                                        .value(getValue(impp))
                                        .type(OnlineResourceType.USERNAME.getValue())
                                        .context(rcontext)
                                        .labels(getLabels(jcardType, (rcontext != null) ? new String[]{rcontext.getValue()} : null, new String[]{LabelKey.IMPP.getValue()}))
                                        .isPreferred(getPreference(impp.getPref()))
                                        .mediaType(impp.getMediaType())
                                        .build()
                               );
        }

        for (Logo logo : vcard.getLogos())
            addOnline(logo, jsContact, LabelKey.LOGO);

        for (Sound sound : vcard.getSounds())
            addOnline(sound, jsContact, LabelKey.SOUND);

        for (Url url : vcard.getUrls())
            addOnline(url, jsContact, LabelKey.URL);

        for (Key key : vcard.getKeys())
            addOnline(key, jsContact, LabelKey.KEY);

        for (FreeBusyUrl fburl : vcard.getFbUrls())
            addOnline(fburl, jsContact, LabelKey.FBURL);

        for (CalendarRequestUri calendarRequestUri : vcard.getCalendarRequestUris())
            addOnline(calendarRequestUri, jsContact, LabelKey.CALADRURI);

        for (CalendarUri calendarUri : vcard.getCalendarUris())
            addOnline(calendarUri, jsContact, LabelKey.CALURI);

        for (OrgDirectory od : vcard.getOrgDirectories()) {
            jcardType = od.getType();
            rcontext = getResourceContext(jcardType);
            orgDirectories.add(Resource.builder()
                                       .value(getValue(od))
                                       .type(OnlineResourceType.URI.getValue())
                                       .labels(getLabels(jcardType, (rcontext != null) ? new String[]{rcontext.getValue()} : null, new String[]{LabelKey.ORG_DIRECTORY.getValue()}))
                                       .context(rcontext)
                                       .isPreferred(getPreference(od.getPref()))
                                       .index(od.getIndex())
                                       .build()
                              );
        }

        if (orgDirectories.size() > 0) {
            Collections.sort(orgDirectories);
            for (Resource ol : orgDirectories)
                jsContact.addOnline(ol);
        }

        List<RawProperty> contactUris = getRawProperties(vcard, LabelKey.CONTACT_URI.getValue());
        for (RawProperty contactUri : contactUris) {
            UriProperty uriProperty = new UriProperty(getValue(contactUri));
            uriProperty.setParameters(contactUri.getParameters());
            addOnline(uriProperty, jsContact, LabelKey.CONTACT_URI);
        }
    }

    private static void fillTitles(VCard vcard, JSContact jsContact) {

        List<LocalizedString> titles = new ArrayList<LocalizedString>();
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

        List<LocalizedString> roles = new ArrayList<LocalizedString>();
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

        for (LocalizedString role : roles)
            jsContact.addRole(role);
    }

    private static Map getOrganizationItemLocalizations(LocalizedString organization, int organizationItemIndex) {

        Map<String, String> localizations = new HashMap<String, String>();
        for (Map.Entry<String, String> unitLocalization : organization.getLocalizations().entrySet()) {
            String[] localizationItems = unitLocalization.getValue().split(SEMICOMMA_ARRAY_DELIMITER);
            localizations.put(unitLocalization.getKey(), localizationItems[organizationItemIndex]);
        }
        return localizations;
    }

    private static void fillOrganizations(VCard vcard, JSContact jsContact) {

        List<LocalizedString> organizations = new ArrayList<LocalizedString>();
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

        List<it.cnr.iit.jscontact.tools.dto.Organization> jsContactOrganizations = new ArrayList<it.cnr.iit.jscontact.tools.dto.Organization>();
        int i = 1;
        for (LocalizedString organization : organizations) {
            String[] nameItems = organization.getValue().split(SEMICOMMA_ARRAY_DELIMITER);
            LocalizedString name = LocalizedString.builder()
                                                  .value(nameItems[0])
                                                  .language(organization.getLanguage())
                                                  .build();

            if (organization.getLocalizations() != null)
                name.setLocalizations(getOrganizationItemLocalizations(organization,0));

            if (nameItems.length > 0) {
                List<LocalizedString> units = new ArrayList<LocalizedString>();
                for (int j = 1; j < nameItems.length; j++) {
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

        i = 1;
        for (it.cnr.iit.jscontact.tools.dto.Organization jsContactOrganization : jsContactOrganizations)
            jsContact.addOrganization("ORG-" + (i++), jsContactOrganization);
    }

    private static void fillNotes(VCard vcard, JSContact jsContact) {

        List<LocalizedString> notes = new ArrayList<LocalizedString>();
        for (Note note : vcard.getNotes()) {
            addLocalizedString(notes, LocalizedString.builder()
                                                     .value(getValue(note))
                                                     .language(note.getLanguage())
                                                     .altid(note.getAltId())
                                                     .preference(note.getPref())
                                                     .build()
                              );
        }
        Collections.sort(notes);
        for (LocalizedString note : notes)
            jsContact.addNote(note);
    }

    private static void fillCategories(VCard vcard, JSContact jsContact) {

        List<CategoryWrapper> wrappers = new ArrayList<CategoryWrapper>();
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

        if (vcard.getAgent()!=null)
            jsContact.addExtension(getUnmatchedPropertyName(VCARD_AGENT_TAG), vcard.getAgent().getUrl());

        if (vcard.getClassification()!=null)
            jsContact.addExtension(getUnmatchedPropertyName(VCARD_CLASSIFICATION_TAG), vcard.getClassification().getValue());

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

    /**
     * Converts a basic vCard v4.0 [RFC6350] into a JSContact object.
     * JSContact objects are defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param vCard a vCard as an instance of the ez-vcard library VCard class
     * @return a JSContact object (JSCard or JSCardGroup)
     * @throws CardException if the vCard is not v4.0 compliant
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    public JSContact convert(VCard vCard) throws CardException {

        JSContact jsContact;
        if (vCard.getMembers() != null && vCard.getMembers().size() != 0) {
            JSCardGroup jsCardGroup = JSCardGroup.builder().uid(UUID.randomUUID().toString()).build();
            fillMembers(vCard, jsCardGroup);
            jsContact = jsCardGroup;
        } else {
            JSCard jsCard = JSCard.builder().uid(UUID.randomUUID().toString()).build();
            jsCard = JSCard.builder().uid(UUID.randomUUID().toString()).build();
            jsContact = jsCard;
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

        List<JSContact> jsContacts = new ArrayList<JSContact>();

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
