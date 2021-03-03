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
import ezvcard.util.TelUri;
import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.Anniversary;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringJoiner;

@NoArgsConstructor
public class JSContact2EZVCard extends AbstractConverter {

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


    private static Revision getRevision(String update) {

        if (update == null)
            return null;

        return new Revision(VCardDateFormat.parseAsCalendar(update));
    }

    private static void fillFormattedNames(VCard vcard, JSContact jsContact) {

        if (jsContact.getFullName() == null)
            return;

        FormattedName fn = new FormattedName(jsContact.getFullName().getValue());
        fn.setLanguage(jsContact.getFullName().getLanguage());
        if (jsContact.getFullName().getLocalizations() != null) {
            fn.setAltId("1");
            vcard.addFormattedName(fn);
            for (String key : jsContact.getFullName().getLocalizations().keySet()) {
                fn = new FormattedName(jsContact.getFullName().getLocalizations().get(key));
                fn.setLanguage(key);
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
        List<String> nicknames = new ArrayList<String>();
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
                case NICKNAME:
                    nicknames.add(component.getValue());
                    break;
            }
        }
        vcard.setStructuredName(name);
        if (nicknames.size() > 0)
            vcard.setNickname(nicknames.toArray(new String[nicknames.size()]));
    }

    private static boolean isComposedAddress(Address address) {

        return (address.getCountry() !=null ||
                address.getCountryCode() !=null ||
                address.getRegion() != null ||
                address.getLocality() != null ||
                address.getStreet() != null ||
                address.getPostOfficeBox() != null ||
                address.getPostcode() != null ||
                address.getExtension() != null);
    }

    private static String getFullAddress(Address addr) {

        StringJoiner joiner = new StringJoiner(AUTO_FULL_ADDRESS_DELIMITER);
        if (StringUtils.isNotEmpty(addr.getPostOfficeBox())) joiner.add(addr.getPostOfficeBox());
        if (StringUtils.isNotEmpty(addr.getExtension())) joiner.add(addr.getExtension());
        if (StringUtils.isNotEmpty(addr.getStreet())) joiner.add(addr.getStreet());
        if (StringUtils.isNotEmpty(addr.getLocality())) joiner.add(addr.getLocality());
        if (StringUtils.isNotEmpty(addr.getRegion())) joiner.add(addr.getRegion());
        if (StringUtils.isNotEmpty(addr.getPostcode())) joiner.add(addr.getPostcode());
        if (StringUtils.isNotEmpty(addr.getCountry())) joiner.add(addr.getCountry());
        if (StringUtils.isNotEmpty(addr.getCountryCode())) joiner.add(addr.getCountryCode());
        return joiner.toString();
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

            if (isComposedAddress(anniversary.getPlace())) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(getFullAddress(anniversary.getPlace()));
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
            Constructor<T> constructor = classs.getDeclaredConstructor(Calendar.class);
            return constructor.newInstance(VCardDateFormat.parseAsCalendar(anniversary.getDate()));
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
                    vcard.setBirthplace(getPlaceProperty(Birthplace.class, anniversary));
                    break;
                case DEATH:
                    vcard.setDeathdate(getDateOrTimeProperty(Deathdate.class, anniversary));
                    vcard.setDeathplace(getPlaceProperty(Deathplace.class, anniversary));
                    break;
                case OTHER:
                    if (anniversary.getLabel().equals(ANNIVERSAY_MARRIAGE_LABEL))
                        vcard.setAnniversary(getDateOrTimeProperty(ezvcard.property.Anniversary.class,anniversary));
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

    private static void fillPersonalInfos(VCard vcard, JSContact jsContact) throws CardException {

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
        language.setType(cl.getType());
        language.setPref(cl.getPreference());
        return language;
    }

    private static void fillContactLanguages(VCard vcard, JSContact jsContact) {

        if (jsContact.getPreferredContactLanguages() == null)
            return;

        for (String lang : jsContact.getPreferredContactLanguages().keySet()) {
            for(ContactLanguage cl : jsContact.getPreferredContactLanguages().get(lang))
                vcard.addLanguage(getLanguage(lang, cl));
        }
    }

    private static Telephone getTelephone(Resource resource) {

        Telephone tel;
        try {
            tel = new Telephone(TelUri.parse(resource.getValue()));
        } catch(Exception e) {
            tel = new Telephone(resource.getValue());
        }
        tel.setPref((resource.getIsPreferred() == Boolean.TRUE) ? 1 : null);
        StringJoiner joiner = new StringJoiner(COMMA_ARRAY_DELIMITER);
        String context = ResourceContext.getVCardType(resource.getContext());
        if (context!=null)
            joiner.add(context);
        PhoneResourceType telType = (resource.getType()!=null) ? PhoneResourceType.getEnum(resource.getType()): null;
        if (telType!=null && telType!=PhoneResourceType.OTHER)
            joiner.add(telType.getValue());
        if (resource.getLabels()!=null) {
            for (String key : resource.getLabels().keySet())
                joiner.add(ResourceContext.getVCardType(key));
        }
        tel.setParameter("TYPE", joiner.toString());

        return tel;
    }

    private static void fillPhones(VCard vcard, JSContact jsContact) {

        if (jsContact.getPhones() == null)
            return;

        for (Resource resource : jsContact.getPhones())
            vcard.getTelephoneNumbers().add(getTelephone(resource));
    }

    private static Email getEmail(Resource resource) {

        Email email = new Email(resource.getValue());
        email.setPref((resource.getIsPreferred() == Boolean.TRUE) ? 1 : null);
        StringJoiner joiner = new StringJoiner(COMMA_ARRAY_DELIMITER);

        String context = ResourceContext.getVCardType(resource.getContext());
        if (context!=null)
            joiner.add(context);
        if (resource.getLabels()!=null) {
            for (String key : resource.getLabels().keySet())
                joiner.add(ResourceContext.getVCardType(key));
        }
        email.setParameter("TYPE", joiner.toString());
        return email;
    }


    private static void fillEmails(VCard vcard, JSContact jsContact) {

        if (jsContact.getEmails() == null)
            return;

        for (Resource email : jsContact.getEmails())
            vcard.getEmails().add(getEmail(email));
    }

    private static ImageType getImageType(String mediaType) {

        for (ImageType it : ImageType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static Photo getPhoto(File file) {

        ImageType it = getImageType(file.getMediaType());
        if (it == null) return null;
        Photo photo = new Photo(file.getHref(), it);
        photo.setPref((file.getIsPreferred() == Boolean.TRUE) ? 1 : null);
        photo.setContentType(it);
        return photo;
    }

    private static <T extends VCardProperty> void fillVCardProperty(T property, Resource resource) {

        if (resource.getMediaType()!=null)
            property.setParameter("MEDIATYPE",resource.getMediaType());
        if (resource.getIsPreferred() == Boolean.TRUE)
            property.setParameter("PREF", "1");
        if (resource.getContext()!=null)
            property.setParameter("TYPE", ResourceContext.getVCardType(resource.getContext()));
        if (resource.getIndex()!=null)
            property.setParameter("INDEX", resource.getIndex().toString());

    }


    private static <T extends UriProperty> T getUriProperty(Class<T> classs, Resource resource) {

        try {
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class);
            T object = constructor.newInstance(resource.getValue());
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
            T object = constructor.newInstance(resource.getValue(), it);
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

        for(File file : jsContact.getPhotos()) {
            Photo photo = getPhoto(file);
            if (photo == null) continue;
            vcard.getPhotos().add(photo);
        }
    }


    private static void fillOnlines(VCard vcard, JSContact jsContact) {

        if (jsContact.getOnline() == null)
            return;

        for (Resource resource : jsContact.getOnline()) {
            switch(LabelKey.getLabelKey(new ArrayList<String>(resource.getLabels().keySet()))) {
                case SOUND:
                    vcard.getSounds().add(getBinaryProperty(Sound.class,resource));
                    break;
                case SOURCE:
                    vcard.getSources().add(getUriProperty(Source.class,resource));
                    break;
                case KEY:
                    vcard.getKeys().add(getBinaryProperty(Key.class,resource));
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

                    Impp impp = new Impp(resource.getValue());
                    fillVCardProperty(impp,resource);
                    vcard.getImpps().add(impp);
                    break;
                case CONTACT_URI:
                    RawProperty rp = new RawProperty("CONTACT-URI",resource.getValue());
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

        if (jsContact.getJobTitles() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (LocalizedString localized : jsContact.getJobTitles()) {
            if (localized.getLocalizations() == null)
                vcard.getTitles().add(getTextProperty(new Title(localized.getValue()), localized.getLanguage()));
            else {
                vcard.getTitles().add(getTextProperty(new Title(localized.getValue()), localized.getLanguage(), altId));
                for (String key : localized.getLocalizations().keySet())
                    vcard.getTitles().add(getTextProperty(new Title(localized.getLocalizations().get(key)), key, altId));
                altId ++;
            }
        }
    }


    private static void fillRoles(VCard vcard, JSContact jsContact) {

        if (jsContact.getRoles() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (LocalizedString localized : jsContact.getRoles()) {
            if (localized.getLocalizations() == null)
                vcard.getRoles().add(getTextProperty(new Role(localized.getValue()), localized.getLanguage()));
            else {
                vcard.getRoles().add(getTextProperty(new Role(localized.getValue()), localized.getLanguage(), altId));
                for (String key : localized.getLocalizations().keySet())
                    vcard.getRoles().add(getTextProperty(new Role(localized.getLocalizations().get(key)), key, altId));
                altId ++;
            }
        }
    }


    private static void fillCategories(VCard vcard, JSContact jsContact) {

        if (jsContact.getCategories() == null)
            return;

        vcard.setCategories(jsContact.getCategories().keySet().toArray(new String[jsContact.getCategories().size()]));
    }

    private static void fillOrganizations(VCard vcard, JSContact jsContact) {

        if (jsContact.getOrganizations() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (LocalizedString localized : jsContact.getOrganizations()) {
            if (localized.getLocalizations() == null)
                vcard.getOrganizations().add(getTextListProperty(new Organization(), SEMICOMMA_ARRAY_DELIMITER, localized.getValue(), localized.getLanguage()));
            else {
                vcard.getOrganizations().add(getTextListProperty(new Organization(), SEMICOMMA_ARRAY_DELIMITER, localized.getValue(), localized.getLanguage(), altId));
                for (String key : localized.getLocalizations().keySet())
                    vcard.getOrganizations().add(getTextListProperty(new Organization(), SEMICOMMA_ARRAY_DELIMITER, localized.getLocalizations().get(key), key, altId));
                altId ++;
            }
        }
    }


    private static void fillNotes(VCard vcard, JSContact jsContact) {

        if (jsContact.getNotes() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (LocalizedString localized : jsContact.getNotes()) {
            if (localized.getLocalizations() == null)
                vcard.getNotes().add(getTextProperty(new Note(localized.getValue()), localized.getLanguage()));
            else {
                vcard.getNotes().add(getTextProperty(new Note(localized.getValue()), localized.getLanguage(), altId));
                for (String key : localized.getLocalizations().keySet())
                    vcard.getNotes().add(getTextProperty(new Note(localized.getLocalizations().get(key)), key, altId));
                altId ++;
            }
        }
    }

    private static Related getRelated(String uriOrText, List<String> types) {

        Related related = getRelated(uriOrText);
        for(String type : types)
            related.getTypes().add(RelatedType.get(type));

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
                vcard.addRelated(getRelated(key, new ArrayList<String>(jsContact.getRelatedTo().get(key).getRelation().keySet())));
        }
    }

    private void fillExtensions(VCard vcard, JSContact jsContact) {

        if (jsContact.getExtensions() == null)
            return;

        for (String key : jsContact.getExtensions().keySet())
            vcard.getExtendedProperties().add(new RawProperty(key.replace(config.getExtensionsPrefix(),""),jsContact.getExtensions().get(key)));

    }

    /**
     * Converts a JSContact object into a basic vCard v4.0 [RFC6350].
     * JSContact objects are defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param jsContact a JSContact object (JSCard or JSCardGroup)
     * @return a vCard as an instance of the ez-vcard library VCard class
     * @throws CardException if the JSContact object is not valid
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    public VCard convert(JSContact jsContact) throws CardException {

        if (jsContact == null)
            return null;

        VCard vCard = new VCard(VCardVersion.V4_0);
        vCard.setUid(getUid(jsContact.getUid()));
        vCard.setKind(getKind(jsContact.getKind()));
        vCard.setProductId(jsContact.getProdId());
        vCard.setRevision(getRevision(jsContact.getUpdated()));
        fillFormattedNames(vCard, jsContact);
        fillNames(vCard, jsContact);
//        fillAddresses(vCard, jsContact);
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
    public List<VCard> convert(List<JSContact> jsContacts) throws CardException {

        List<VCard> vCards = new ArrayList<VCard>();

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
        List<VCard> vCards = new ArrayList<VCard>();

        if (jsonNode.isArray()) {
            JSContact[] jsContacts = objectMapper.treeToValue(jsonNode, JSContact[].class);
            for (JSContact jsContact : jsContacts) {
                if (config.isCardToValidate()) {
                    if (!jsContact.isValid())
                        throw new CardException(jsContact.getValidationMessage());
                }
                vCards.add(convert(jsContact));
            }
        }
        else {
            JSContact jsContact = objectMapper.treeToValue(jsonNode, JSContact.class);
            if (!jsContact.isValid())
                throw new CardException(jsContact.getValidationMessage());
            vCards.add(convert(jsContact));
        }

        return vCards;
    }



}
