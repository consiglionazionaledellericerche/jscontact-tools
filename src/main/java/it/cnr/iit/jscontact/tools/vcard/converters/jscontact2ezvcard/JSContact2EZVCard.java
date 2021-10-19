package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.*;
import ezvcard.property.*;
import ezvcard.util.GeoUri;
import ezvcard.util.PartialDate;
import ezvcard.util.TelUri;
import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Anniversary;
import it.cnr.iit.jscontact.tools.dto.Organization;
import it.cnr.iit.jscontact.tools.dto.TimeZone;
import it.cnr.iit.jscontact.tools.dto.Title;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.VCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Utility class for converting a JSContact object into a vCard 4.0 [RFC6350] instance represented as an Ezvcard VCard object.
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class JSContact2EZVCard extends AbstractConverter {

    private static final Map<String,String> ezclassesPerPropertiesMap = new HashMap<String,String>() {{
        put("Address", "ADR");
        put("Birthday","BDAY");
        put("CalendarUri","CALURI");
        put("CalendarRequestUri","CALADRURI");
        put("FormattedName", "FN");
        put("FreeBusyUrl","FBURL");
        put("Language","LANG");
        put("Organization","ORG");
        put("OrgDirectories","ORG-DIRECTORY");
        put("ProductId","PRODID");
        put("Revision","REV");
        put("StructuredName", "N");
        put("Telephone","TEL");
        put("Timezone","TZ");
    }};

    protected JSContact2VCardConfig config;

    private static Kind getKind(KindType kind) {

        if (kind == null)
            return null;

        if (kind.getExtValue() != null)
            return new Kind(kind.getExtValue());

        return new Kind(kind.getRfcValue().getValue());
    }

    private static Uid getUid(String uid) {

        if (uid == null)
            return null;

        return new Uid(uid);
    }

    private static Revision getRevision(Calendar update) {

        if (update == null)
            return null;

        return new Revision(update);
    }

    private static void fillMembers(VCard vcard, CardGroup jsCardGroup) {

        if (jsCardGroup.getMembers() == null)
            return;

        for (String key : jsCardGroup.getMembers().keySet())
            vcard.addMember(new Member(key));

    }


    private static String getNameComponent(NameComponent[] name, NameComponentEnum type) {

        for (NameComponent component : name)
            if (component.getType().getRfcValue()!=null &&  component.getType().getRfcValue() == type)
                return component.getValue();

        return null;
    }

    private static void addNameComponent(StringJoiner joiner, NameComponent[] name, NameComponentEnum type) {

        String component = getNameComponent(name, type);
        joiner.add(component);
    }

    private static void addNameComponent(StringJoiner joiner, List<String> values) {

        if (values != null && values.size() != 0 )
            joiner.add(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, values));
    }

    private static void addNameComponent(StringJoiner joiner, String value) {

        if (value != null)
            joiner.add(value);
    }

    private static FormattedName getFormattedName(NameComponent[] name) {

        String separator = getNameComponent(name, NameComponentEnum.SEPARATOR);
        StringJoiner joiner = new StringJoiner((separator!=null) ? separator : StringUtils.SPACE);
        addNameComponent(joiner, name, NameComponentEnum.PREFIX);
        addNameComponent(joiner, name, NameComponentEnum.PERSONAL);
        addNameComponent(joiner, name, NameComponentEnum.SURNAME);
        addNameComponent(joiner, name, NameComponentEnum.ADDITIONAL);
        addNameComponent(joiner, name, NameComponentEnum.SUFFIX);
        return new FormattedName(joiner.toString());
    }

    private static FormattedName getFormattedName(StructuredName sn, String separator) {

        StringJoiner joiner = new StringJoiner((separator!=null) ? separator : StringUtils.SPACE);
        addNameComponent(joiner, sn.getPrefixes());
        addNameComponent(joiner, sn.getGiven());
        addNameComponent(joiner, sn.getFamily());
        addNameComponent(joiner, sn.getAdditionalNames());
        addNameComponent(joiner, sn.getSuffixes());
        return new FormattedName(joiner.toString());
    }

    private static FormattedName getFormattedName(String name) {

        return new FormattedName(name);
    }

    private static void fillFormattedNames(VCard vcard, Card jsCard) {

        if (StringUtils.isEmpty(jsCard.getFullName())) {
            if (jsCard.getName() != null) {
                List<StructuredName> sns = getStructuredNames(jsCard);
                String separator = getNameComponent(jsCard.getName(), NameComponentEnum.SEPARATOR);
                if (sns.size() == 1) {
                    FormattedName fn = getFormattedName(sns.get(0), separator);
                    fn.setLanguage(jsCard.getLanguage());
                    vcard.setFormattedName(fn);
                }
                else {
                    List<FormattedName> fns = new ArrayList<>();
                    for (StructuredName sn : sns) {
                        FormattedName fn = getFormattedName(sn, separator);
                        fn.setLanguage(sn.getLanguage());
                        fns.add(fn);
                    }
                    vcard.setFormattedNameAlt(fns.toArray(new FormattedName[0]));
                }
            }
            else
                vcard.setFormattedName(jsCard.getUid());
            return;
        }

        if (jsCard.getLocalizationsPerPath("/fullName") != null) {
            List<FormattedName> fns = new ArrayList<>();
            FormattedName fn = getFormattedName(jsCard.getFullName());
            fn.setLanguage(jsCard.getLanguage());
            fns.add(fn);
            vcard.setFormattedName(fn);
            for (Map.Entry<String,JsonNode> localizations : jsCard.getLocalizationsPerPath("/fullName").entrySet()) {
                fn = getFormattedName(localizations.getValue().asText());
                fn.setLanguage(localizations.getKey());
                fns.add(fn);
            }
            vcard.setFormattedNameAlt(fns.toArray(new FormattedName[0]));
        } else
            vcard.setFormattedName(getFormattedName(jsCard.getFullName()));

    }

    private static StructuredName getStructuredName(NameComponent[] nameComponents) {

        StructuredName name = new StructuredName();
        for (NameComponent component : nameComponents) {
            if (component.getType().getRfcValue() == null)
                continue;
            switch(component.getType().getRfcValue()) {
                case PREFIX:
                    name.getPrefixes().add(component.getValue());
                    break;
                case PERSONAL:
                    name.setGiven(component.getValue());
                    break;
                case SURNAME:
                    name.setFamily(component.getValue());
                    break;
                case ADDITIONAL:
                    name.getAdditionalNames().add(component.getValue());
                    break;
                case SUFFIX:
                    name.getSuffixes().add(component.getValue());
                    break;
            }
        }

        return name;
    }


    private static NameComponent[] asNameComponentArray(JsonNode arrayNode) {

        if (!arrayNode.isArray())
            return null;
        List<NameComponent> ncs = new ArrayList<>();
        try {
            for (JsonNode node : arrayNode) {

                if (node.isObject()) {
                    NameComponent nc = mapper.convertValue(node, NameComponent.class);
                    ncs.add(nc);
                }
            }

            return (ncs.size() > 0) ? ncs.toArray(new NameComponent[0]) : null;
        } catch (Exception e) {
            return null;
        }

    }

    private static List<StructuredName> getStructuredNames(Card jsCard) {

        List<StructuredName> sns = new ArrayList<>();
        if (jsCard.getLocalizationsPerPath("/name") != null) {
            StructuredName sn = getStructuredName(jsCard.getName());
            sn.setLanguage(jsCard.getLanguage());
            sns.add(sn);
            for (Map.Entry<String, JsonNode> localizations : jsCard.getLocalizationsPerPath("/name").entrySet()) {
                sn = getStructuredName(asNameComponentArray(localizations.getValue()));
                sn.setLanguage(localizations.getKey());
                sns.add(sn);
            }
        }
        else
            sns.add(getStructuredName(jsCard.getName()));

        return sns;

    }


    private static void fillNames(VCard vcard, Card jsCard) {

        if (jsCard.getName() == null)
            return;

        List<StructuredName> sns = getStructuredNames(jsCard);
        if (sns.size() == 1)
            vcard.setStructuredName(sns.get(0));
        else
            vcard.setStructuredNameAlt(sns.toArray(new StructuredName[0]));
    }

    private static void fillNickNames(VCard vcard, Card jsCard) {

        if (jsCard.getNickNames() == null)
            return;

        String[] nickNames = jsCard.getNickNames();

        Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("/nickNames");
        if (localizations == null)
            vcard.setNickname(nickNames);
        else {
            List<Nickname> nicks = new ArrayList<>();
            nicks.add(getTextListProperty(new Nickname(), Arrays.asList(nickNames), jsCard.getLanguage()));
            for (Map.Entry<String, JsonNode> localization : localizations.entrySet())
                nicks.add(getTextListProperty(new Nickname(), Arrays.asList(JsonNodeUtils.asTextArray(localization.getValue())), localization.getKey()));

            vcard.setNicknameAlt(nicks.toArray(new Nickname[0]));
        }
    }

    private static boolean isNullStructuredAddress(Address address) {

        return (address.getCountry() ==null &&
                address.getCountryCode() ==null &&
                address.getRegion() == null &&
                address.getLocality() == null &&
                address.getStreetDetails() == null &&
                address.getPostOfficeBox() == null &&
                address.getPostcode() == null &&
                address.getStreetExtensions() == null);
    }

    private static String getFullAddressFromStructuredAddress(Address addr) {

        StringJoiner joiner = new StringJoiner(DelimiterUtils.NEWLINE_DELIMITER);
        if (StringUtils.isNotEmpty(addr.getPostOfficeBox())) joiner.add(addr.getPostOfficeBox());
        if (StringUtils.isNotEmpty(addr.getStreetExtensions())) joiner.add(addr.getStreetExtensions());
        if (StringUtils.isNotEmpty(addr.getStreetDetails())) joiner.add(addr.getStreetDetails());
        if (StringUtils.isNotEmpty(addr.getLocality())) joiner.add(addr.getLocality());
        if (StringUtils.isNotEmpty(addr.getRegion())) joiner.add(addr.getRegion());
        if (StringUtils.isNotEmpty(addr.getPostcode())) joiner.add(addr.getPostcode());
        if (StringUtils.isNotEmpty(addr.getCountry())) joiner.add(addr.getCountry());
        return joiner.toString();
    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> StringJoiner toVCardTypeStringJoiner(Class<E> enumType, Collection<E> enumValues) {
        StringJoiner joiner = new StringJoiner(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        for (E value : enumValues) {
            try {
                String typeItem = (String) enumType.getDeclaredMethod("toVCardType", enumType).invoke(null, value);
                if (typeItem != null)
                    joiner.add(typeItem);
            } catch (Exception e) {
                throw new InternalErrorException(e.getMessage());

            }
        }
        return joiner;
    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> StringJoiner toVCardTypeStringJoiner(Class<E> enumType, String[] stringValues) {
        StringJoiner joiner = new StringJoiner(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        for (String value : stringValues) {
            try {
                String typeItem = (String) enumType.getDeclaredMethod("toVCardType", String.class).invoke(null, value);
                if (typeItem != null)
                    joiner.add(typeItem);
            } catch (Exception e) {
                throw new InternalErrorException(e.getMessage());
            }
        }
        return joiner;
    }

    private static ezvcard.property.Address getAddress(Address address, Map<String,TimeZone> timeZones, String language) {

        ezvcard.property.Address addr = new ezvcard.property.Address();
        if (!isNullStructuredAddress(address)) {
            addr.setLabel(getFullAddressFromStructuredAddress(address));
            addr.setCountry(address.getCountry());
            addr.setRegion(address.getRegion());
            addr.setLocality(address.getLocality());
            addr.setStreetAddress(address.getStreetDetails());
            addr.setExtendedAddress(address.getStreetExtensions());
            addr.setPoBox(address.getPostOfficeBox());
            addr.setPostalCode(address.getPostcode());
            if (address.getTimeZone() != null) {
                TimeZone timeZone = null;
                if (timeZones != null)
                    timeZone = timeZones.get(address.getTimeZone());
                if (timeZone != null) {
                    if (timeZone.getStandard() != null && timeZone.getStandard().size() > 0)
                        addr.setTimezone(timeZone.getStandard().get(0).getOffsetFrom());
                } else
                    addr.setTimezone(address.getTimeZone());
            }
            if (address.getCoordinates() != null)
                addr.setGeo(GeoUri.parse(address.getCoordinates()));
            if (address.getCountryCode() != null)
                addr.setParameter("CC", address.getCountryCode());
            if (address.getContexts() != null) {
                String vCardType = toVCardTypeStringJoiner(AddressContextEnum.class, AddressContext.getAddressContextEnumValues(address.getContexts().keySet())).toString();
                if (StringUtils.isNotEmpty(vCardType))
                    addr.setParameter("TYPE", vCardType);
            }
        }

        if (address.getFullAddress() != null)
            addr.setLabel(address.getFullAddress());

        addr.setLanguage(language);

        return addr;
    }

    private static Address asAddress(JsonNode jsonNode) {

        if (!jsonNode.isObject())
            return null;
        try {
            return mapper.convertValue(jsonNode, Address.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static void fillAddresses(VCard vcard, Card jsCard) {

        if (jsCard.getAddresses() == null)
            return;

        for (Map.Entry<String,Address> entry : jsCard.getAddresses().entrySet()) {

            Address address = entry.getValue();
            if (isNullStructuredAddress(address) && address.getFullAddress() == null)
                continue;

            if (jsCard.getLocalizationsPerPath("/addresses/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("/addresses/"+entry.getKey()+"/fullAddress")==null) {
                ezvcard.property.Address addr = getAddress(address, jsCard.getTimeZones(), jsCard.getLanguage());
                vcard.addAddress(addr);
            }
            else {
                List<ezvcard.property.Address> addrs = new ArrayList<>();
                ezvcard.property.Address addr = getAddress(address, jsCard.getTimeZones(), jsCard.getLanguage());
                addrs.add(addr);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("/addresses/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet())
                        addrs.add(getAddress(asAddress(localization.getValue()), jsCard.getTimeZones(), localization.getKey()));
                }
                localizations = jsCard.getLocalizationsPerPath("/addresses/"+entry.getKey()+"/fullAddress");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet())
                        addrs.add(getAddress(Address.builder().fullAddress(localization.getValue().asText()).build(), jsCard.getTimeZones(), localization.getKey()));
                }
                vcard.addAddressAlt(addrs.toArray(new ezvcard.property.Address[0]));
            }
        }
    }

    private static <T extends PlaceProperty> T getPlaceProperty(Class<T> classs, Anniversary anniversary) {

        if (anniversary.getPlace() == null)
            return null;

        try {
            Constructor<T> constructor;
            if (anniversary.getPlace().getFullAddress() != null) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(anniversary.getPlace().getFullAddress());
            }

            if (!isNullStructuredAddress(anniversary.getPlace())) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(getFullAddressFromStructuredAddress(anniversary.getPlace()));
            }

            if (anniversary.getPlace().getCoordinates() != null) {
                GeoUri geoUri = GeoUri.parse(anniversary.getPlace().getCoordinates());
                constructor = classs.getDeclaredConstructor(double.class, double.class);
                return constructor.newInstance(geoUri.getCoordA(), geoUri.getCoordB());
            }
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

        return null;
    }


    private static <T extends DateOrTimeProperty> T getDateOrTimeProperty(Class<T> classs, Anniversary anniversary) {

        try {
            if (anniversary.getDate().getDate()!=null) {
                Constructor<T> constructor = classs.getDeclaredConstructor(Calendar.class);
                return constructor.newInstance(anniversary.getDate().getDate());
            }
            if (anniversary.getDate().getPartialDate()!=null) {
                Constructor<T> constructor = classs.getDeclaredConstructor(PartialDate.class);
                return constructor.newInstance(anniversary.getDate().getPartialDate());
            }
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

        return null;
    }

    private static void fillAnniversaries(VCard vcard, Card jsCard) {

        if (jsCard.getAnniversaries() == null)
            return;

        for (Anniversary anniversary : jsCard.getAnniversaries().values()) {

            switch(anniversary.getType()) {
                case BIRTH:
                    vcard.setBirthday(getDateOrTimeProperty(Birthday.class, anniversary));
                    vcard.getBirthday().setCalscale(Calscale.GREGORIAN);
                    vcard.setBirthplace(getPlaceProperty(Birthplace.class, anniversary));
                    break;
                case DEATH:
                    vcard.setDeathdate(getDateOrTimeProperty(Deathdate.class, anniversary));
                    vcard.getDeathdate().setCalscale(Calscale.GREGORIAN);
                    vcard.setDeathplace(getPlaceProperty(Deathplace.class, anniversary));
                    break;
                case OTHER:
                    if (anniversary.getLabel().equals(Anniversary.ANNIVERSAY_MARRIAGE_LABEL)) {
                        vcard.setAnniversary(getDateOrTimeProperty(ezvcard.property.Anniversary.class, anniversary));
                        vcard.getAnniversary().setCalscale(Calscale.GREGORIAN);
                    }
                    break;
            }
        }

    }

    private static Expertise getExpertise(PersonalInformation pi) {

        Expertise e = new Expertise(pi.getValue());
        e.setLevel(ExpertiseLevel.get(PersonalInformationLevel.getVCardExpertiseLevel(pi.getLevel())));
        return e;
    }

    private static Hobby getHobby(PersonalInformation pi) {

        Hobby h = new Hobby(pi.getValue());
        h.setLevel(HobbyLevel.get(pi.getLevel().getValue()));
        return h;
    }

    private static Interest getInterest(PersonalInformation pi) {

        Interest i = new Interest(pi.getValue());
        i.setLevel(InterestLevel.get(pi.getLevel().getValue()));
        return i;
    }

    private static void fillPersonalInfos(VCard vcard, Card jsCard) {

        if (jsCard.getPersonalInfo() == null)
            return;

        for (PersonalInformation pi : jsCard.getPersonalInfo().values()) {
            switch (pi.getType()) {
                case EXPERTISE:
                    vcard.getExpertise().add(getExpertise(pi));
                    break;
                case HOBBY:
                    vcard.getHobbies().add(getHobby(pi));
                    break;
                case INTEREST:
                    vcard.getInterests().add(getInterest(pi));
                    break;
            }
        }
    }

    private static Language getLanguage(String lang, ContactLanguage cl) {

        Language language = new Language(lang);
        if (cl.getContext()!=null && cl.getContext().isRfcValue())
            language.setType(ContextEnum.toVCardType(cl.getContext().getRfcValue()));
        language.setPref(cl.getPref());
        return language;
    }

    private static void fillContactLanguages(VCard vcard, Card jsCard) {

        if (jsCard.getPreferredContactLanguages() == null)
            return;

        for (Map.Entry<String,ContactLanguage[]> clArray : jsCard.getPreferredContactLanguages().entrySet()) {
            for(ContactLanguage cl : clArray.getValue())
                vcard.addLanguage(getLanguage(clArray.getKey(), cl));
        }
    }

    private static Telephone getTelephone(Phone phone) {

        Telephone tel;
        try {
            tel = new Telephone(TelUri.parse(phone.getPhone()));
        } catch(Exception e) {
            tel = new Telephone(phone.getPhone());
        }
        tel.setPref(phone.getPref());

        StringJoiner joiner = new StringJoiner(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        joiner = joiner.merge(toVCardTypeStringJoiner(ContextEnum.class, Context.getContextEnumValues(phone.getContexts().keySet())));
        joiner = joiner.merge(toVCardTypeStringJoiner(PhoneFeatureEnum.class, PhoneFeature.getFeatureEnumValues(phone.getFeatures().keySet())));

        if (StringUtils.isNotEmpty(joiner.toString()))
            tel.setParameter("TYPE", joiner.toString());

        return tel;
    }

    private static void fillPhones(VCard vcard, Card jsCard) {

        if (jsCard.getPhones() == null)
            return;

        for (Phone phone : jsCard.getPhones().values())
            vcard.getTelephoneNumbers().add(getTelephone(phone));
    }

    private static Email getEmail(EmailAddress emailAddress) {

        Email email = new Email(emailAddress.getEmail());
        email.setPref(emailAddress.getPref());
        if (emailAddress.getContexts()!=null) {
            String vCardType = toVCardTypeStringJoiner(ContextEnum.class, Context.getContextEnumValues(emailAddress.getContexts().keySet())).toString();
            if (StringUtils.isNotEmpty(vCardType))
                email.setParameter("TYPE", vCardType);
        }
        return email;
    }

    private static void fillEmails(VCard vcard, Card jsCard) {

        if (jsCard.getEmails() == null)
            return;

        for (EmailAddress email : jsCard.getEmails().values())
            vcard.getEmails().add(getEmail(email));
    }

    private static ImageType getImageType(String mediaType) {

        if (mediaType == null)
            return null;

        for (ImageType it : ImageType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static SoundType getSoundType(String mediaType) {

        if (mediaType == null)
            return null;

        for (SoundType it : SoundType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static KeyType getKeyType(String mediaType) {

        if (mediaType == null)
            return null;

        for (KeyType it : KeyType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }


    private static Photo getPhoto(File file) {

        ImageType it = getImageType(file.getMediaType());
        if (it == null) return null;
        Photo photo = new Photo(file.getHref(), it);
        photo.setPref(file.getPref());
        photo.setContentType(it);
        return photo;
    }

    private static <T extends VCardProperty> void fillVCardProperty(T property, Resource resource) {

        if (resource.getMediaType()!=null)
            property.setParameter("MEDIATYPE",resource.getMediaType());
        if (resource.getPref() != null)
            property.setParameter("PREF", resource.getPref().toString());
        if (resource.getContexts()!=null) {
            String vCardType = toVCardTypeStringJoiner(ContextEnum.class, Context.getContextEnumValues(resource.getContexts().keySet())).toString();
            if (StringUtils.isNotEmpty(vCardType))
                property.setParameter("TYPE", vCardType);
        }
    }

    private static <T extends UriProperty> T getUriProperty(Class<T> classs, Resource resource) {

        try {
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class);
            T object = constructor.newInstance(resource.getResource());
            fillVCardProperty(object,resource);
            return object;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    private static <T extends BinaryProperty> T getBinaryProperty(Class<T> classs, Resource resource) {

        try {
            ImageType it = getImageType(resource.getMediaType());
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class, ImageType.class);
            T object = constructor.newInstance(resource.getResource(), it);
            fillVCardProperty(object,resource);
            return object;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

    }


    private static void fillPhotos(VCard vcard, Card jsCard) {

        if (jsCard.getPhotos() == null)
            return;

        for(File file : jsCard.getPhotos().values()) {
            Photo photo = getPhoto(file);
            if (photo == null) continue;
            vcard.getPhotos().add(photo);
        }
    }


    private static void fillOnlines(VCard vcard, Card jsCard) {

        if (jsCard.getOnline() == null)
            return;

        for (Resource resource : jsCard.getOnline().values()) {

            if (resource.getLabel() == null)
                continue;

            switch(OnlineLabelKey.getLabelKey(resource.getLabel())) {
                case SOUND:
                    vcard.getSounds().add(new Sound(resource.getResource(), getSoundType(resource.getMediaType())));
                    break;
                case SOURCE:
                    vcard.getSources().add(getUriProperty(Source.class,resource));
                    break;
                case KEY:
                    vcard.getKeys().add(new Key(resource.getResource(), getKeyType(resource.getMediaType())));
                    break;
                case LOGO:
                    vcard.getLogos().add(getBinaryProperty(Logo.class,resource));
                    break;
                case URL:
                    vcard.getUrls().add(getUriProperty(Url.class,resource));
                    break;
                case FBURL:
                    vcard.getFbUrls().add(getUriProperty(FreeBusyUrl.class,resource));
                    break;
                case CALADRURI:
                    vcard.getCalendarRequestUris().add(getUriProperty(CalendarRequestUri.class,resource));
                    break;
                case CALURI:
                    vcard.getCalendarUris().add(getUriProperty(CalendarUri.class,resource));
                    break;
                case ORG_DIRECTORY:
                    vcard.getOrgDirectories().add(getUriProperty(OrgDirectory.class,resource));
                    break;
                case IMPP:
                    Impp impp = new Impp(resource.getResource());
                    fillVCardProperty(impp,resource);
                    vcard.getImpps().add(impp);
                    break;
                case CONTACT_URI:
                    RawProperty rp = new RawProperty("CONTACT-URI",resource.getResource());
                    fillVCardProperty(rp,resource);
                    vcard.getExtendedProperties().add(rp);
                    break;
            }
        }
    }

    private static <E extends TextProperty > E getTextProperty(E property, String language) {

        if (language != null) property.getParameters().setLanguage(language);
        return property;
    }

    private static <E extends TextListProperty> E getTextListProperty(E property, List<String> textList, String language) {

        property.getValues().addAll(textList);
        if (language != null) property.getParameters().setLanguage(language);
        return property;
    }

    private static void fillTitles(VCard vcard, Card jsCard) {

        if (jsCard.getTitles() == null)
            return;

        for (Map.Entry<String,Title> entry : jsCard.getTitles().entrySet()) {

            if (jsCard.getLocalizationsPerPath("/titles/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("/titles/"+entry.getKey()+"/title")==null)
                vcard.addTitle(getTextProperty(new ezvcard.property.Title(entry.getValue().getTitle()), jsCard.getLanguage()));
            else {
                List<ezvcard.property.Title> titles = new ArrayList<>();
                titles.add(getTextProperty(new ezvcard.property.Title(entry.getValue().getTitle()), jsCard.getLanguage()));

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("/titles/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet())
                        titles.add(getTextProperty(new ezvcard.property.Title(localization.getValue().get("title").asText()), localization.getKey()));
                }
                localizations = jsCard.getLocalizationsPerPath("/titles/"+entry.getKey()+"/title");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet())
                        titles.add(getTextProperty(new ezvcard.property.Title(localization.getValue().asText()), localization.getKey()));
                }
                vcard.addTitleAlt(titles.toArray(new ezvcard.property.Title[0]));
            }
        }
    }


    private static void fillCategories(VCard vcard, Card jsCard) {

        if (jsCard.getCategories() == null)
            return;

        vcard.setCategories(jsCard.getCategories().keySet().toArray(new String[jsCard.getCategories().size()]));
    }

    private static List<String> getOrganizationItems(String organization, String[] units) {

        List<String> organizationItems = new ArrayList<>();
        organizationItems.add(organization);
        if (units != null)
            organizationItems.addAll(Arrays.asList(units));
        return organizationItems;
    }

    private static void fillOrganizations(VCard vcard, Card jsCard) {

        if (jsCard.getOrganizations() == null)
            return;

        for (Map.Entry<String,Organization> entry : jsCard.getOrganizations().entrySet()) {

            if (jsCard.getLocalizationsPerPath("/organizations/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("/organizations/"+entry.getKey()+"/name")==null)

                vcard.getOrganizations().add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(entry.getValue().getName(), entry.getValue().getUnits()), jsCard.getLanguage()));

            else {
                List<ezvcard.property.Organization> organizations = new ArrayList<>();
                organizations.add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(entry.getValue().getName(), entry.getValue().getUnits()), jsCard.getLanguage()));

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("/organizations/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet())
                        organizations.add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(localization.getValue().get("name").asText(), JsonNodeUtils.asTextArray(localization.getValue().get("units"))), localization.getKey()));
                }
                localizations = jsCard.getLocalizationsPerPath("/organizations/"+entry.getKey()+"/name");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        JsonNode units = jsCard.getLocalization(localization.getKey(),"/organizations/"+entry.getKey()+"/units");
                        organizations.add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(localization.getValue().get("name").asText(), JsonNodeUtils.asTextArray(units)), localization.getKey()));
                    }
                }
                vcard.addOrganizationAlt(organizations.toArray(new ezvcard.property.Organization[0]));
            }
        }
    }

    private static void fillNotes(VCard vcard, Card jsCard) {

        if (jsCard.getNotes() == null)
            return;

        Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("/notes");
        if (localizations == null) {
            for (String note : jsCard.getNotes().split(DelimiterUtils.NEWLINE_DELIMITER))
                vcard.getNotes().add(getTextProperty(new Note(note), jsCard.getLanguage()));
        }
        else {
            int i = 0;
            for (String note : jsCard.getNotes().split(DelimiterUtils.NEWLINE_DELIMITER)) {
                List<ezvcard.property.Note> notes = new ArrayList<>();
                notes.add(getTextProperty(new Note(note), jsCard.getLanguage()));
                for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                    String[] localizedNotes = localization.getValue().asText().split(DelimiterUtils.NEWLINE_DELIMITER);
                    notes.add(getTextProperty(new Note(localizedNotes[i]), localization.getKey()));
                }
                vcard.addNoteAlt(notes.toArray(new ezvcard.property.Note[0]));
                i++;
            }
        }
    }

    private static Related getRelated(String uriOrText, List<RelationType> types) {

        Related related = getRelated(uriOrText);
        for(RelationType type : types) {
            if (type.getRfcValue() != null)
                related.getTypes().add(RelatedType.get(type.getRfcValue().getValue()));
        }

        return related;
    }


    private static Related getRelated(String uriOrText) {
        Related related = new Related();
        try {
            URI uri = new URI(uriOrText);
            if (uri.getScheme() == null)
                related.setText(uriOrText);
            else
                related.setUri(uriOrText);
        } catch (URISyntaxException e) {
            related.setText(uriOrText);
        }

        return related;
    }

    private static void fillRelations(VCard vcard, Card jsCard) {

        if (jsCard.getRelatedTo() == null)
            return;

        for (String key : jsCard.getRelatedTo().keySet()) {
            if (jsCard.getRelatedTo().get(key).getRelation() == null)
                vcard.addRelated(getRelated(key));
            else
                vcard.addRelated(getRelated(key, new ArrayList<>(jsCard.getRelatedTo().get(key).getRelation().keySet())));
        }
    }

    private static ClientPidMap getCliendPidMap(String key, String value) {

        Integer pid = Integer.parseInt(key.replace(getUnmatchedPropertyName(VCARD_CLIENTPIDMAP_TAG)+"/",StringUtils.EMPTY));
        return new ClientPidMap(pid, value);
    }


    private static String getPropertyNameFromClassName(String className) {

        for (Map.Entry<String,String> pair : ezclassesPerPropertiesMap.entrySet()) {

            if (pair.getKey().equals(className))
                return pair.getValue();
        }

        return className.toUpperCase();
    }

    private static String[] getPropertyNamePlusIndex(String extension, String parameterName) {
        String propertyPlusIndex = extension
                                   .replace(UNMATCHED_PROPERTY_PREFIX,StringUtils.EMPTY)
                                   .replace(":" + parameterName,StringUtils.EMPTY);
        return propertyPlusIndex.split(":");
    }

    private static String getPropertyNameFromExtension(String extension, String parameterName) {
        return getPropertyNamePlusIndex(extension, parameterName)[0];
    }

    private static Integer getPropertyIndexFromExtension(String extension, String parameterName) {
        String[] items = getPropertyNamePlusIndex(extension, parameterName);
        return (items.length == 2) ? Integer.parseInt(items[1]) : null;
    }

    private void fillVCardUnmatchedParameter(VCard vcard, String extension, String parameterName, String value) {

        String selectedPropertyName = getPropertyNameFromExtension(extension, parameterName);
        Integer index = getPropertyIndexFromExtension(extension, parameterName);
        for (VCardProperty property : vcard.getProperties()) {
            String propertyName = getPropertyNameFromClassName(property.getClass().getSimpleName());
            if (selectedPropertyName.equals(propertyName)) {
                if (parameterName.equals("GROUP"))
                    vcard.getProperties(property.getClass()).get((index==null) ? 0 : index).setGroup(value);
                else
                    vcard.getProperties(property.getClass()).get((index==null) ? 0 : index).setParameter(parameterName, value);
            }
        }
    }

    private void fillExtensions(VCard vcard, Card jsCard) {

        if (jsCard.getExtensions() == null)
            return;

        for (Map.Entry<String,String> extension : jsCard.getExtensions().entrySet()) {
            if (extension.getKey().equals(getUnmatchedPropertyName(VCARD_GENDER_TAG)))
                vcard.setGender(new Gender(extension.getValue()));
            else if (extension.getKey().startsWith(getUnmatchedPropertyName(VCARD_CLIENTPIDMAP_TAG)))
                vcard.addClientPidMap(getCliendPidMap(extension.getKey(), extension.getValue()));
            else if ((extension.getKey().startsWith(getUnmatchedPropertyName(VCARD_XML_TAG))))
                try {
                    vcard.getXmls().add(new Xml(extension.getValue()));
                } catch (Exception e) {
                    throw new InternalErrorException(e.getMessage());
                }
            else if (extension.getKey().equals(getUnmatchedParamName("N", "SORT-AS")))
                vcard.getStructuredName().setParameter("SORT-AS", extension.getValue());
            else if (extension.getKey().equals(getUnmatchedParamName("ANNIVERSARY", "CALSCALE")))
                vcard.getAnniversary().setParameter("CALSCALE", extension.getValue());
            else if (extension.getKey().equals(getUnmatchedParamName("BDAY", "CALSCALE")))
                vcard.getBirthday().setParameter("CALSCALE", extension.getValue());
            else if (extension.getKey().equals(getUnmatchedParamName("DEATHDATE", "CALSCALE")))
                vcard.getDeathdate().setParameter("CALSCALE", extension.getValue());
            else if (extension.getKey().startsWith(UNMATCHED_PROPERTY_PREFIX) && extension.getKey().endsWith(":GROUP"))
                fillVCardUnmatchedParameter(vcard,extension.getKey(),"GROUP", extension.getValue());
            else if (extension.getKey().startsWith(UNMATCHED_PROPERTY_PREFIX) && extension.getKey().endsWith(":PID"))
                fillVCardUnmatchedParameter(vcard,extension.getKey(),"PID", extension.getValue());
            else
                vcard.getExtendedProperties().add(new RawProperty(extension.getKey().replace(config.getExtensionsPrefix(), StringUtils.EMPTY), extension.getValue()));
        }
    }

    private static void fillUnmatchedElments(VCard vCard, Card jsCard) {

        if (jsCard.getCreated() != null) {
            vCard.addExtendedProperty("X-JSCONTACT-CREATED", VCardDateFormat.UTC_DATE_TIME_BASIC.format(jsCard.getCreated().getTime()));
        }

        if (jsCard.getPreferredContactMethod() != null) {
            vCard.addExtendedProperty("X-JSCONTACT-PREFERREDCONTACTMETHOD", jsCard.getPreferredContactMethod().getValue());
        }

    }

    /**
     * Converts a JSContact object into a basic vCard v4.0 [RFC6350].
     * JSContact objects are defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param jsContact a JSContact object (Card or CardGroup)
     * @return a vCard as an instance of the ez-vcard library VCard class
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    protected VCard convert(JSContact jsContact) {

        if (jsContact == null)
            return null;

        VCard vCard = new VCard(VCardVersion.V4_0);
        vCard.setUid(getUid(jsContact.getUid()));
        Card jsCard = null;
        if (jsContact instanceof CardGroup) {
            CardGroup jsCardGroup = (CardGroup) jsContact;
            fillMembers(vCard, jsCardGroup);
            if (jsCardGroup.getCard()!=null)
                jsCard = jsCardGroup.getCard();
        } else
            jsCard = (Card) jsContact;

        if (jsCard == null) return vCard;

        vCard.setKind(getKind(jsCard.getKind()));
        vCard.setProductId(jsCard.getProdId());
        vCard.setRevision(getRevision(jsCard.getUpdated()));
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
        fillOnlines(vCard, jsCard);
        fillTitles(vCard, jsCard);
        fillOrganizations(vCard, jsCard);
        fillCategories(vCard, jsCard);
        fillNotes(vCard, jsCard);
        fillRelations(vCard, jsCard);
        fillExtensions(vCard, jsCard);
        fillUnmatchedElments(vCard, jsCard);

        return vCard;
    }

    /**
     * Converts a list of JSContact objects into a a list of vCard v4.0 instances [RFC6350].
     * JSContact is defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param jsContacts a list of JSContact objects
     * @return a list of instances of the ez-vcard library VCard class
     * @throws CardException if one of JSContact objects is not valid
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    protected List<VCard> convert(JSContact... jsContacts) throws CardException {

        List<VCard> vCards = new ArrayList<>();

        for (JSContact jsContact : jsContacts) {
            if (config.isCardToValidate()) {
                if (!jsContact.isValid())
                    throw new CardException(jsContact.getValidationMessage());
            }
            vCards.add(convert(jsContact));
        }

        return vCards;
    }


    /**
     * Converts a JSON array of JSContact objects into a list of vCard v4.0 instances [RFC6350].
     * JSContact is defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param json a JSON array of JSContact objects
     * @return a list of instances of the ez-vcard library VCard class
     * @throws CardException if one of JSContact objects is not valid
     * @throws JsonProcessingException if json cannot be processed
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    public List<VCard> convert(String json) throws CardException, JsonProcessingException {

        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        mapper.registerModule(module);
        JsonNode jsonNode = mapper.readTree(json);
        JSContact[] jsContacts;
        if (jsonNode.isArray())
            jsContacts = mapper.treeToValue(jsonNode, JSContact[].class);
        else
            jsContacts = new JSContact[] { mapper.treeToValue(jsonNode, JSContact.class)};

        return convert(jsContacts);
    }

}
