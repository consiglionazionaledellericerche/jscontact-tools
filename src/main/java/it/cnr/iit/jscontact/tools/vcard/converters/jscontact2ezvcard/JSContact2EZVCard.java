package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.*;
import ezvcard.property.*;
import ezvcard.property.Kind;
import ezvcard.util.GeoUri;
import ezvcard.util.PartialDate;
import ezvcard.util.TelUri;
import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Anniversary;
import it.cnr.iit.jscontact.tools.dto.Organization;
import it.cnr.iit.jscontact.tools.dto.Title;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.JCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.NoteUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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

    private static void fillMembers(VCard vcard, JSCardGroup jsCardGroup) {

        if (jsCardGroup.getMembers() == null)
            return;

        for (String key : jsCardGroup.getMembers().keySet())
            vcard.addMember(new Member(key));

    }


    private static void addNameComponent(StringJoiner joiner, NameComponent[] name, NameComponentType type) {

        for (NameComponent component : name)
            if (component.getType() == type)
                joiner.add(component.getValue());
    }

    private static FormattedName getFormattedName(NameComponent[] name) {

        StringJoiner joiner = new StringJoiner(SPACE_ARRAY_DELIMITER);
        addNameComponent(joiner, name,NameComponentType.PREFIX);
        addNameComponent(joiner, name,NameComponentType.PERSONAL);
        addNameComponent(joiner, name,NameComponentType.SURNAME);
        addNameComponent(joiner, name,NameComponentType.ADDITIONAL);
        addNameComponent(joiner, name,NameComponentType.SUFFIX);
        return new FormattedName(joiner.toString());
    }

    private static void fillFormattedNames(VCard vcard, JSContact jsContact) {

        if (jsContact.getFullName() == null || jsContact.getFullName().getValue().isEmpty()) {
            if (jsContact.getName() != null)
                vcard.setFormattedName(getFormattedName(jsContact.getName()));
            else
                vcard.setFormattedName(jsContact.getUid());
            return;
        }

        FormattedName fn = new FormattedName(jsContact.getFullName().getValue());
        fn.setLanguage(jsContact.getFullName().getLanguage());
        if (jsContact.getFullName().getLocalizations() != null) {
            fn.setAltId("1");
            vcard.addFormattedName(fn);
            for (Map.Entry<String,String> localization : jsContact.getFullName().getLocalizations().entrySet()) {
                fn = new FormattedName(localization.getValue());
                fn.setLanguage(localization.getKey());
                fn.setAltId("1");
                vcard.addFormattedName(fn);
            }
        } else
            vcard.addFormattedName(fn);

    }

    private static void fillNames(VCard vcard, JSContact jsContact) {

        if (jsContact.getName() == null)
            return;

        StructuredName name = new StructuredName();
        for (NameComponent component : jsContact.getName()) {
            switch(component.getType()) {
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
        vcard.setStructuredName(name);
    }

    private static void fillNickNames(VCard vcard, JSContact jsContact) {

        if (jsContact.getNickNames() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (LocalizedString localized : jsContact.getNickNames()) {
            if (localized.getLocalizations() == null)
                vcard.getNicknames().add(getTextListProperty(new Nickname(), COMMA_ARRAY_DELIMITER, localized.getValue(), localized.getLanguage()));
            else {
                vcard.getNicknames().add(getTextListProperty(new Nickname(), COMMA_ARRAY_DELIMITER, localized.getValue(), localized.getLanguage(), altId));
                for (Map.Entry<String,String> localization : localized.getLocalizations().entrySet())
                    vcard.getNicknames().add(getTextListProperty(new Nickname(), COMMA_ARRAY_DELIMITER, localization.getValue(), localization.getKey(), altId));
                altId ++;
            }
        }
    }

    private static boolean isStructuredAddress(Address address) {

        return (address.getCountry() !=null ||
                address.getCountryCode() !=null ||
                address.getRegion() != null ||
                address.getLocality() != null ||
                address.getStreetDetails() != null ||
                address.getPostOfficeBox() != null ||
                address.getPostcode() != null ||
                address.getStreetExtensions() != null);
    }

    private static String getFullAddressFromStructuredAddress(Address addr) {

        StringJoiner joiner = new StringJoiner(AUTO_FULL_ADDRESS_DELIMITER);
        if (StringUtils.isNotEmpty(addr.getPostOfficeBox())) joiner.add(addr.getPostOfficeBox());
        if (StringUtils.isNotEmpty(addr.getStreetExtensions())) joiner.add(addr.getStreetExtensions());
        if (StringUtils.isNotEmpty(addr.getStreetDetails())) joiner.add(addr.getStreetDetails());
        if (StringUtils.isNotEmpty(addr.getLocality())) joiner.add(addr.getLocality());
        if (StringUtils.isNotEmpty(addr.getRegion())) joiner.add(addr.getRegion());
        if (StringUtils.isNotEmpty(addr.getPostcode())) joiner.add(addr.getPostcode());
        if (StringUtils.isNotEmpty(addr.getCountry())) joiner.add(addr.getCountry());
        return joiner.toString();
    }

    private static <E extends Enum<E> & JCardTypeDerivedEnum> StringJoiner getVCardTypeStringJoiner(Class<E> enumType, Collection<E> enumValues) {
        StringJoiner joiner = new StringJoiner(COMMA_ARRAY_DELIMITER);
        for (E value : enumValues) {
            try {
                String typeItem = (String) enumType.getDeclaredMethod("getVCardType", enumType).invoke(null, value);
                if (typeItem != null)
                    joiner.add(typeItem);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return joiner;
    }

    private static <E extends Enum<E> & JCardTypeDerivedEnum> StringJoiner getVCardTypeStringJoiner(Class<E> enumType, String[] stringValues) {
        StringJoiner joiner = new StringJoiner(COMMA_ARRAY_DELIMITER);
        for (String value : stringValues) {
            try {
                String typeItem = (String) enumType.getDeclaredMethod("getVCardType", String.class).invoke(null, value);
                if (typeItem != null)
                    joiner.add(typeItem);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return joiner;
    }

    private static List<ezvcard.property.Address> getAddress(Address address, Integer altId) {

        if (address == null)
            return null;

        if (!isStructuredAddress(address) && address.getFullAddress() == null)
            return null;

        List<ezvcard.property.Address> addrs = new ArrayList<>();
        ezvcard.property.Address addr = new ezvcard.property.Address();
        if (isStructuredAddress(address)) {
            addr.setLabel(getFullAddressFromStructuredAddress(address));
            addr.setCountry(address.getCountry());
            addr.setRegion(address.getRegion());
            addr.setLocality(address.getLocality());
            addr.setStreetAddress(address.getStreetDetails());
            addr.setExtendedAddress(address.getStreetExtensions());
            addr.setPoBox(address.getPostOfficeBox());
            addr.setPostalCode(address.getPostcode());
            if (address.getTimeZone()!=null)
                addr.setTimezone(address.getTimeZone());
            if (address.getCoordinates()!=null)
                addr.setGeo(GeoUri.parse(address.getCoordinates()));
            if (address.getCountryCode()!=null)
                addr.setParameter("CC", address.getCountryCode());
            if (address.getContexts()!=null) {
                String vCardType = getVCardTypeStringJoiner(AddressContext.class, address.getContexts().keySet()).toString();
                if (StringUtils.isNotEmpty(vCardType))
                    addr.setParameter("TYPE", vCardType);
            }
        }
        if (address.getFullAddress()!=null) {
            addr.setLabel(address.getFullAddress().getValue());
            addr.setLanguage(address.getFullAddress().getLanguage());
            addr.setAltId((altId != null) ? altId.toString() : null);
        }
        addrs.add(addr);
        if (address.getFullAddress()!=null && address.getFullAddress().getLocalizations()!=null) {
            for (Map.Entry<String,String> localization : address.getFullAddress().getLocalizations().entrySet()) {
                ezvcard.property.Address localAddr = new ezvcard.property.Address();
                localAddr.setLabel(localization.getValue());
                localAddr.setLanguage(localization.getKey());
                localAddr.setAltId((altId != null) ? altId.toString() : null);
                addrs.add(localAddr);
            }
        }

        return addrs;
    }

    private static void fillAddresses(VCard vcard, JSContact jsContact) {

        if (jsContact.getAddresses() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (Address address : jsContact.getAddresses().values()) {
            boolean altIdToBeAdded = (jsContact.getAddresses().size() > 1) &&
                                     (
                                             (address.getFullAddress()!=null && address.getFullAddress().getLocalizations()!=null)
                                     );
            vcard.getAddresses().addAll(getAddress(address, altIdToBeAdded ? altId : null));
        }
    }

    private static <T extends PlaceProperty> T getPlaceProperty(Class<T> classs, Anniversary anniversary) {

        if (anniversary.getPlace() == null)
            return null;

        try {
            Constructor<T> constructor;
            if (anniversary.getPlace().getFullAddress() != null) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(anniversary.getPlace().getFullAddress().getValue());
            }

            if (isStructuredAddress(anniversary.getPlace())) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(getFullAddressFromStructuredAddress(anniversary.getPlace()));
            }

            if (anniversary.getPlace().getCoordinates() != null) {
                GeoUri geoUri = GeoUri.parse(anniversary.getPlace().getCoordinates());
                constructor = classs.getDeclaredConstructor(double.class, double.class);
                return constructor.newInstance(geoUri.getCoordA(), geoUri.getCoordB());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static void fillAnniversaries(VCard vcard, JSContact jsContact) {

        if (jsContact.getAnniversaries() == null)
            return;

        for (Anniversary anniversary : jsContact.getAnniversaries()) {

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
                    if (anniversary.getLabel().equals(ANNIVERSAY_MARRIAGE_LABEL)) {
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

    private static void fillPersonalInfos(VCard vcard, JSContact jsContact) {

        if (jsContact.getPersonalInfo() == null)
            return;

        for (PersonalInformation pi : jsContact.getPersonalInfo()) {
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
        language.setType(Context.getVCardType(cl.getContext()));
        language.setPref(cl.getPref());
        return language;
    }

    private static void fillContactLanguages(VCard vcard, JSContact jsContact) {

        if (jsContact.getPreferredContactLanguages() == null)
            return;

        for (Map.Entry<String,ContactLanguage[]> clArray : jsContact.getPreferredContactLanguages().entrySet()) {
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

        StringJoiner joiner = new StringJoiner(COMMA_ARRAY_DELIMITER);
        joiner = joiner.merge(getVCardTypeStringJoiner(Context.class, phone.getContexts().keySet()));
        joiner = joiner.merge(getVCardTypeStringJoiner(PhoneFeature.class, phone.getFeatures().keySet()));
        if (phone.getLabel()!=null)
            joiner = joiner.merge(getVCardTypeStringJoiner(PhoneFeature.class, phone.getLabel().split(COMMA_ARRAY_DELIMITER)));

        if (StringUtils.isNotEmpty(joiner.toString()))
            tel.setParameter("TYPE", joiner.toString());

        return tel;
    }

    private static void fillPhones(VCard vcard, JSContact jsContact) {

        if (jsContact.getPhones() == null)
            return;

        for (Phone phone : jsContact.getPhones().values())
            vcard.getTelephoneNumbers().add(getTelephone(phone));
    }

    private static Email getEmail(EmailAddress emailAddress) {

        Email email = new Email(emailAddress.getEmail());
        email.setPref(emailAddress.getPref());
        if (emailAddress.getContexts()!=null) {
            String vCardType = getVCardTypeStringJoiner(Context.class, emailAddress.getContexts().keySet()).toString();
            if (StringUtils.isNotEmpty(vCardType))
                email.setParameter("TYPE", vCardType);
        }
        return email;
    }

    private static void fillEmails(VCard vcard, JSContact jsContact) {

        if (jsContact.getEmails() == null)
            return;

        for (EmailAddress email : jsContact.getEmails().values())
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
            String vCardType = getVCardTypeStringJoiner(Context.class, resource.getContexts().keySet()).toString();
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
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static <T extends BinaryProperty> T getBinaryProperty(Class<T> classs, Resource resource) {

        try {
            ImageType it = getImageType(resource.getMediaType());
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class, ImageType.class);
            T object = constructor.newInstance(resource.getResource(), it);
            fillVCardProperty(object,resource);
            return object;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    private static void fillPhotos(VCard vcard, JSContact jsContact) {

        if (jsContact.getPhotos() == null)
            return;

        for(File file : jsContact.getPhotos().values()) {
            Photo photo = getPhoto(file);
            if (photo == null) continue;
            vcard.getPhotos().add(photo);
        }
    }


    private static void fillOnlines(VCard vcard, JSContact jsContact) {

        if (jsContact.getOnline() == null)
            return;

        for (Resource resource : jsContact.getOnline().values()) {

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

    private static <E extends TextProperty > E getTextProperty(E property, String language, Integer altId) {

        if (altId != null) property.getParameters().setAltId(altId.toString());
        if (language != null) property.getParameters().setLanguage(language);
        return property;
    }

    private static <E extends TextProperty > E getTextProperty(E property, String language) {
        return getTextProperty(property, language, null);
    }

    private static <E extends TextListProperty> E getTextListProperty(E property, String delimiter, String text, String language, Integer altId) {

        for (String item : text.split(delimiter))
            property.getValues().add(item);
        if (altId != null) property.getParameters().setAltId(altId.toString());
        if (language != null) property.getParameters().setLanguage(language);
        return property;
    }

    private static <E extends TextListProperty > E getTextListProperty(E property, String delimiter, String text, String language) {
        return getTextListProperty(property, delimiter, text, language, null);
    }


    private static void fillTitles(VCard vcard, JSContact jsContact) {

        if (jsContact.getTitles() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (Title title : jsContact.getTitles().values()) {
            if (title.getTitle().getLocalizations() == null)
                vcard.getTitles().add(getTextProperty(new ezvcard.property.Title(title.getTitle().getValue()), title.getTitle().getLanguage()));
            else {
                vcard.getTitles().add(getTextProperty(new ezvcard.property.Title(title.getTitle().getValue()), title.getTitle().getLanguage(), altId));
                for (Map.Entry<String,String> localization : title.getTitle().getLocalizations().entrySet())
                    vcard.getTitles().add(getTextProperty(new ezvcard.property.Title(localization.getValue()), localization.getKey(), altId));
                altId ++;
            }
        }
    }


    private static void fillCategories(VCard vcard, JSContact jsContact) {

        if (jsContact.getCategories() == null)
            return;

        vcard.setCategories(jsContact.getCategories().keySet().toArray(new String[jsContact.getCategories().size()]));
    }

    private static List<String> getOrganizationItems(LocalizedString organization, LocalizedString[] units, String language) {

        List<String> organizationItems = new ArrayList<>();
        if (language == null) {
            organizationItems.add(organization.getValue());
            if (units != null) {
                for (LocalizedString unit : units)
                    organizationItems.add(unit.getValue());
            }
        }
        else {
            organizationItems.add(organization.getLocalizations().get(language));
            if (units != null) {
                for (LocalizedString unit : units)
                    organizationItems.add(unit.getLocalizations().get(language));
            }
        }

        return organizationItems;
    }

    private static List<String> getOrganizationItems(LocalizedString organization, LocalizedString[] units) {
        return getOrganizationItems(organization, units, null);
    }

    private static void fillOrganizations(VCard vcard, JSContact jsContact) {

        if (jsContact.getOrganizations() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (Organization organization : jsContact.getOrganizations().values()) {
            List<String> organizationItems = getOrganizationItems(organization.getName(), organization.getUnits());
            if (organization.getName().getLocalizations() == null) {
                vcard.getOrganizations().add(getTextListProperty(new ezvcard.property.Organization(), SEMICOMMA_ARRAY_DELIMITER, String.join(SEMICOMMA_ARRAY_DELIMITER,organizationItems), organization.getName().getLanguage()));
            }
            else {
                vcard.getOrganizations().add(getTextListProperty(new ezvcard.property.Organization(), SEMICOMMA_ARRAY_DELIMITER, String.join(SEMICOMMA_ARRAY_DELIMITER,organizationItems), organization.getName().getLanguage(), altId));
                for (String language : organization.getName().getLocalizations().keySet()) {
                    organizationItems = getOrganizationItems(organization.getName(), organization.getUnits(), language);
                    vcard.getOrganizations().add(getTextListProperty(new ezvcard.property.Organization(), SEMICOMMA_ARRAY_DELIMITER, String.join(SEMICOMMA_ARRAY_DELIMITER,organizationItems), language, altId));
                }
                altId++;
            }
        }
    }

    private static void fillNotes(VCard vcard, JSContact jsContact) {

        if (jsContact.getNotes() == null)
            return;

        LocalizedString localized = jsContact.getNotes();

        if (localized.getLocalizations() == null) {
            for (String note : localized.getValue().split(NoteUtils.NOTE_DELIMITER))
                vcard.getNotes().add(getTextProperty(new Note(note), localized.getLanguage()));
        }
        else {
            Integer altId = Integer.parseInt("1");
            for (String note : localized.getValue().split(NoteUtils.NOTE_DELIMITER))
                vcard.getNotes().add(getTextProperty(new Note(note), localized.getLanguage(), altId++));
            altId = Integer.parseInt("1");
            for (Map.Entry<String,String> localization : localized.getLocalizations().entrySet())
                for (String note : localization.getValue().split(NoteUtils.NOTE_DELIMITER))
                    vcard.getNotes().add(getTextProperty(new Note(note), localization.getKey(), altId++));
        }
    }

    private static Related getRelated(String uriOrText, List<RelationType> types) {

        Related related = getRelated(uriOrText);
        for(RelationType type : types)
            related.getTypes().add(RelatedType.get(type.getValue()));

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

    private static void fillRelations(VCard vcard, JSContact jsContact) {

        if (jsContact.getRelatedTo() == null)
            return;

        for (String key : jsContact.getRelatedTo().keySet()) {
            if (jsContact.getRelatedTo().get(key).getRelation() == null)
                vcard.addRelated(getRelated(key));
            else
                vcard.addRelated(getRelated(key, new ArrayList<>(jsContact.getRelatedTo().get(key).getRelation().keySet())));
        }
    }

    private static ClientPidMap getCliendPidMap(String key, String value) {

        Integer pid = Integer.parseInt(key.replace(getUnmatchedPropertyName(VCARD_CLIENTPIDMAP_TAG)+"/",""));
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
                                   .replace(UNMATCHED_PROPERTY_PREFIX,"")
                                   .replace("/" + parameterName,"");
        return propertyPlusIndex.split("/");
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

    private void fillExtensions(VCard vcard, JSContact jsContact) {

        if (jsContact.getExtensions() == null)
            return;

        for (Map.Entry<String,String> extension : jsContact.getExtensions().entrySet()) {
            if (extension.getKey().equals(getUnmatchedPropertyName(VCARD_GENDER_TAG)))
                vcard.setGender(new Gender(extension.getValue()));
            else if (extension.getKey().startsWith(getUnmatchedPropertyName(VCARD_CLIENTPIDMAP_TAG)))
                vcard.addClientPidMap(getCliendPidMap(extension.getKey(), extension.getValue()));
            else if ((extension.getKey().startsWith(getUnmatchedPropertyName(VCARD_XML_TAG))))
                try {
                    vcard.getXmls().add(new Xml(extension.getValue()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            else if (extension.getKey().equals(getUnmatchedParamName("N", "SORT-AS")))
                vcard.getStructuredName().setParameter("SORT-AS", extension.getValue());
            else if (extension.getKey().equals(getUnmatchedParamName("ANNIVERSARY", "CALSCALE")))
                vcard.getAnniversary().setParameter("CALSCALE", extension.getValue());
            else if (extension.getKey().equals(getUnmatchedParamName("BDAY", "CALSCALE")))
                vcard.getBirthday().setParameter("CALSCALE", extension.getValue());
            else if (extension.getKey().equals(getUnmatchedParamName("DEATHDATE", "CALSCALE")))
                vcard.getDeathdate().setParameter("CALSCALE", extension.getValue());
            else if (extension.getKey().startsWith(UNMATCHED_PROPERTY_PREFIX) && extension.getKey().endsWith("/GROUP"))
                fillVCardUnmatchedParameter(vcard,extension.getKey(),"GROUP", extension.getValue());
            else if (extension.getKey().startsWith(UNMATCHED_PROPERTY_PREFIX) && extension.getKey().endsWith("/PID"))
                fillVCardUnmatchedParameter(vcard,extension.getKey(),"PID", extension.getValue());
            else
                vcard.getExtendedProperties().add(new RawProperty(extension.getKey().replace(config.getExtensionsPrefix(), ""), extension.getValue()));
        }
    }

    private static void fillUnmatchedElments(VCard vCard, JSContact jsContact) {

        if (jsContact.getCreated() != null) {
            vCard.addExtendedProperty("X-JSCONTACT-CREATED", VCardDateFormat.UTC_DATE_TIME_BASIC.format(jsContact.getCreated().getTime()));
        }

        if (jsContact.getPreferredContactMethod() != null) {
            vCard.addExtendedProperty("X-JSCONTACT-PREFERREDCONTACTMETHOD", jsContact.getPreferredContactMethod().getValue());
        }

    }

    /**
     * Converts a JSContact object into a basic vCard v4.0 [RFC6350].
     * JSContact objects are defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param jsContact a JSContact object (JSCard or JSCardGroup)
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
        vCard.setKind(getKind(jsContact.getKind()));
        vCard.setProductId(jsContact.getProdId());
        vCard.setRevision(getRevision(jsContact.getUpdated()));
        if (jsContact instanceof JSCardGroup)
            fillMembers(vCard, (JSCardGroup) jsContact);
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
        fillOrganizations(vCard, jsContact);
        fillCategories(vCard, jsContact);
        fillNotes(vCard, jsContact);
        fillRelations(vCard, jsContact);
        fillExtensions(vCard, jsContact);
        fillUnmatchedElments(vCard, jsContact);

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
    protected List<VCard> convert(List<JSContact> jsContacts) throws CardException {

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
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    public List<VCard> convert(String json) throws CardException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        objectMapper.registerModule(module);
        JsonNode jsonNode = objectMapper.readTree(json);
        JSContact[] jsContacts;
        if (jsonNode.isArray())
            jsContacts = objectMapper.treeToValue(jsonNode, JSContact[].class);
        else
            jsContacts = new JSContact[] { objectMapper.treeToValue(jsonNode, JSContact.class)};

        return convert(Arrays.asList(jsContacts));
    }

}
