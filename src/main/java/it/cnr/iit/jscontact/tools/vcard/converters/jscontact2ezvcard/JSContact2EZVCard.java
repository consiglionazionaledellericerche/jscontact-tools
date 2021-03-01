package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.RelatedType;
import ezvcard.property.*;
import ezvcard.property.Kind;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.JCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class JSContact2EZVCard {

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

    private static <E extends TextProperty > E getTextProperty(E property, String language, Integer altId) {

        if (altId != null) property.getParameters().setAltId(altId.toString());
        if (language != null) property.getParameters().setLanguage(language);
        return property;
    }

    private static <E extends TextProperty > E getTextProperty(E property, String language) {
        return getTextProperty(property, language, null);
    }

    private static <E extends TextListProperty> E getTextListProperty(E property, String text, String language, Integer altId) {

        for (String item : text.split(";"))
            property.getValues().add(item);
        if (altId != null) property.getParameters().setAltId(altId.toString());
        if (language != null) property.getParameters().setLanguage(language);
        return property;
    }

    private static <E extends TextListProperty > E getTextListProperty(E property, String text, String language) {
        return getTextListProperty(property, text, language, null);
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


    private static void fillOrganizations(VCard vcard, JSContact jsContact) {

        if (jsContact.getOrganizations() == null)
            return;

        Integer altId = Integer.parseInt("1");
        for (LocalizedString localized : jsContact.getOrganizations()) {
            if (localized.getLocalizations() == null)
                vcard.getOrganizations().add(getTextListProperty(new Organization(), localized.getValue(), localized.getLanguage()));
            else {
                vcard.getOrganizations().add(getTextListProperty(new Organization(), localized.getValue(), localized.getLanguage(), altId));
                for (String key : localized.getLocalizations().keySet())
                    vcard.getOrganizations().add(getTextListProperty(new Organization(), localized.getLocalizations().get(key), key, altId));
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
        fillFormattedNames(vCard, jsContact);
//        fillNames(vCard, jsContact);
//        fillAddresses(vCard, jsContact);
//        fillAnniversaries(vCard, jsContact);
//        fillPersonalInfos(vCard, jsContact);
//        fillContactLanguages(vCard, jsContact);
//        fillPhones(vCard, jsContact);
//        fillEmails(vCard, jsContact);
//        fillOnlines(vCard, jsContact);
        fillTitles(vCard, jsContact);
        fillRoles(vCard, jsContact);
        fillOrganizations(vCard, jsContact);
//        fillCategories(vCard, jsContact);
        fillNotes(vCard, jsContact);
        fillRelations(vCard, jsContact);
//        fillExtensions(vCard, jsContact);
//        fillUnmatchedElments(vCard, jsContact);

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
