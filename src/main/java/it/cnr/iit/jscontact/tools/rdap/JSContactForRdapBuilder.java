package it.cnr.iit.jscontact.tools.rdap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.constraints.groups.profiles.Profile_RDAP;
import it.cnr.iit.jscontact.tools.constraints.groups.Version_2_0;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.utils.VersionUtils;
import it.cnr.iit.jscontact.tools.dto.utils.builders.PhoneFeaturesBuilder;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class JSContactForRdapBuilder {

    private Card jsCard;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Returns a JSContactForRdapBuilder object used to build a JSContact Card object.
     *
     * @return the JSContactForRdapBuilder object
     */
    public static JSContactForRdapBuilder builder() {
        return new JSContactForRdapBuilder(Card.builder().version(VersionUtils.getDefaultVersionForRdap()).build());
    }

    /**
     * Sets the uid and returns this JSContactForRdapBuilder object updated.
     *
     * @param uid the uid value to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder uid(String uid) {
        if (uid == null) return this;
        this.jsCard.setUid(uid);
        return this;
    }

    /**
     * Sets the version and returns this JSContactForRdapBuilder object updated.
     *
     * @param version the version value to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder version(VersionUtils.VersionEnum version) {
        if (version == null) return this;
        this.jsCard.setVersion(version.getValue());
        return this;
    }

    /**
     * Sets the language and returns this JSContactForRdapBuilder object updated.
     *
     * @param language the language value to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder language(String language) {
        if (language == null) return this;
        this.jsCard.setLanguage(language);
        return this;
    }

    /**
     * Sets the kind and returns this JSContactForRdapBuilder object updated.
     *
     * @param kind the kind value to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder kind(KindType kind) {
        if (kind == null) return this;
        this.jsCard.setKind(kind);
        return this;
    }

    /**
     * Sets the name as a JSContact Name object and returns this JSContactForRdapBuilder object updated.
     *
     * @param name the Name object to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder name(Name name) {
        if (name == null) return this;
        this.jsCard.setName(name);
        return this;
    }

    /**
     * Sets the organization and returns this JSContactForRdapBuilder object updated.
     *
     * @param org the organization name to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder org(String org) {
        if (org == null) return this;
        this.jsCard.addOrganization(JSContactForRdapMapId.ORG_ID.getValue(),Organization.builder().name(org).build());
        return this;
    }

    /**
     * Sets the email address and returns this JSContactForRdapBuilder object updated.
     *
     * @param email the email address to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder email(String email) {
        if (email == null) return this;
        this.jsCard.addEmailAddress(JSContactForRdapMapId.EMAIL_ID.getValue(), EmailAddress.builder().address(email).build());
        return this;
    }

    /**
     * Sets the voice number and returns this JSContactForRdapBuilder object updated.
     *
     * @param voice the voice number to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder voice(String voice) {
        if (voice == null) return this;
        this.jsCard.addPhone(JSContactForRdapMapId.VOICE_ID.getValue(), Phone.builder().number(voice).features(PhoneFeaturesBuilder.builder().voice().build()).build());
        return this;
    }

    /**
     * Sets the fax number and returns this JSContactForRdapBuilder object updated.
     *
     * @param fax the fax number to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder fax(String fax) {
        if (fax == null) return this;
        this.jsCard.addPhone(JSContactForRdapMapId.FAX_ID.getValue(), Phone.builder().number(fax).features(PhoneFeaturesBuilder.builder().fax().build()).build());
        return this;
    }

    /**
     * Sets the url and returns this JSContactForRdapBuilder object updated.
     *
     * @param url the url to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder url(String url) {
        if (url == null) return this;
        this.jsCard.addLinkResource(JSContactForRdapMapId.URL_ID.getValue(), Link.builder().uri(url).build());
        return this;
    }

    /**
     * Sets the contact uri and returns this JSContactForRdapBuilder object updated.
     *
     * @param contactUri the contact uri to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder contactUri(String contactUri) {
        if (contactUri == null) return this;
        this.jsCard.addLinkResource(JSContactForRdapMapId.CONTACT_URI_ID.getValue(), Link.builder().uri(contactUri).build());
        return this;
    }

    /**
     * Sets the name as a JSContact Address object and returns this JSContactForRdapBuilder object updated.
     *
     * @param address the Address object to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder addr(Address address) {
        if (address == null) return this;
        this.jsCard.addAddress(JSContactForRdapMapId.ADDRESS_ID.getValue(), address);
        return this;
    }

    /**
     * Sets a name localization as a JSContact Name object and returns this JSContactForRdapBuilder object updated.
     *
     * @param language the localization language
     * @param name the Name object in the given language to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder nameLoc(String language, Name name) {
        if (language == null || name == null) return this;
        this.jsCard.addLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue(), mapper.convertValue(name, JsonNode.class));
        return this;
    }

    /**
     * Sets an organization localization and returns this JSContactForRdapBuilder object updated.
     *
     * @param language the localization language
     * @param org the organization name in the given language to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder orgLoc(String language, String org) {
        if (language == null || org == null) return this;
        Map<String,Organization> organizations =  new HashMap<String,Organization>() {{ put(JSContactForRdapMapId.ORG_ID.getValue(), Organization.builder().name(org).build()); }};
        this.jsCard.addLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue(), mapper.convertValue(organizations, JsonNode.class));
        return this;
    }

    /**
     * Sets an address localization as a JSContact Address object and returns this JSContactForRdapBuilder object updated.
     *
     * @param language the localization language
     * @param address the Address object in the given language to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder addrLoc(String language, Address address) {
        if (language == null || address == null) return this;
        Map<String,Address> addresses =  new HashMap<String,Address>() {{ put(JSContactForRdapMapId.ADDRESS_ID.getValue(), address); }};
        this.jsCard.addLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue(), mapper.convertValue(addresses, JsonNode.class));
        return this;
    }

    /**
     * Sets an email address localization and returns this JSContactForRdapBuilder object updated.
     *
     * @param language the localization language
     * @param email the email address in the given language to be assigned
     * @return this JSContactForRdapBuilder object updated
     */
    public JSContactForRdapBuilder emailLoc(String language, String email) {
        if (language == null || email == null) return this;
        Map<String,EmailAddress> emails =  new HashMap<String,EmailAddress>() {{ put(JSContactForRdapMapId.EMAIL_ID.getValue(), EmailAddress.builder().address(email).build()); }};
        this.jsCard.addLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue(), mapper.convertValue(emails, JsonNode.class));
        return this;
    }

    /**
     * Returns the JSContact Address object with the given properties set
     *
     * @throws MissingFieldException if no property has been set
     * @throws CardException if the JSContact object to build is not valid
     * @return the JSContact Address object
     */
    public Card build() throws MissingFieldException, CardException {

        if (jsCard.getName() == null &&
            jsCard.getOrganizations() == null &&
            jsCard.getAddresses() == null &&
            jsCard.getPhones() == null &&
            jsCard.getEmails() == null &&
            jsCard.getLinks() == null)
            throw new MissingFieldException("At least one between name, organizations, addresses, phones, emails and links must be set in JSCard");

        if (!jsCard.isValid(Version_2_0.class, Profile_RDAP.class))
            throw new CardException(jsCard.getValidationMessage());

        return jsCard;
    }
}
