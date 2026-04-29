package it.cnr.iit.jscontact.tools.rdap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.utils.VersionUtils;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JSContactForRdapGetter {

    private Card jsCard;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Returns a JSContactForRdapGetter object initialized with an JSContact Card object.
     *
     * @param jsCard the JSContact Card object
     * @return a JSContactForRdapGetter object
     * @throws MissingFieldException if jsCard is null
     */
    public static JSContactForRdapGetter of(Card jsCard) throws MissingFieldException {
        if (jsCard == null)
            throw new MissingFieldException("A Card object is required");
        return new JSContactForRdapGetter(jsCard);
    }

    /**
     * Returns the uid of this JSContactForRdapGetter object
     *
     * @return the uid
     */
    public String uid() {
        return jsCard.getUid();
    }

    /**
     * Returns the language of this JSContactForRdapGetter object
     *
     * @return the language
     */
    public String language() {
        return jsCard.getLanguage();
    }

    /**
     * Returns the kind of this JSContactForRdapGetter object
     *
     * @return the kind
     */
    public KindType kind() {
        return jsCard.getKind();
    }

    /**
     * Returns the version of this JSContactForRdapGetter object
     *
     * @return the version
     */
    public VersionUtils.VersionEnum version() {
        return VersionUtils.VersionEnum.getEnum(jsCard.getVersion());
    }

    /**
     * Returns the name as JSContact Name object of this JSContactForRdapGetter object
     *
     * @return the name as JSContact Name object
     */
    public Name name() {
        return jsCard.getName();
    }

    /**
     * Returns the organization of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the organization
     */
    public String org() {
        return (jsCard.getOrganizations()!=null && jsCard.getOrganizations().get(JSContactForRdapMapId.ORG_ID.getValue())!=null) ? jsCard.getOrganizations().get(JSContactForRdapMapId.ORG_ID.getValue()).getName() : null;
    }

    /**
     * Returns the email address of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @param key the key identifying the email address to return
     * @return the email address identified by the key or the primary email address if key is null
     */
    public String email(String key) {
        if (key == null) key = JSContactForRdapMapId.EMAIL_ID.getValue();
        return (jsCard.getEmails()!=null && jsCard.getEmails().get(key)!=null) ? jsCard.getEmails().get(key).getAddress() : null;
    }

    /**
     * Returns the primary email address of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the primary email address
     */
    public String email() {
        return email(null);
    }

    /**
     * Returns the voice number of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @param key the key identifying the voice number to return
     * @return the voice number identified by the key or the primary voice number if key is null
     */
    public String voice(String key) {
        if (key == null) key = JSContactForRdapMapId.VOICE_ID.getValue();
        return (jsCard.getPhones()!=null && jsCard.getPhones().get(key)!=null) ? jsCard.getPhones().get(key).getNumber() : null;
    }

    /**
     * Returns the primary voice number of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the primary voice number
     */
    public String voice() {
        return voice(null);
    }

    /**
     * Returns the fax number of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @param key the key identifying the fax number to return
     * @return the fax number identified by the key or the primary fax number if key is null
     */
    public String fax(String key) {
        if (key == null) key = JSContactForRdapMapId.FAX_ID.getValue();
        return (jsCard.getPhones()!=null && jsCard.getPhones().get(key)!=null) ? jsCard.getPhones().get(key).getNumber() : null;
    }

    /**
     * Returns the primary fax number of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the primary fax number
     */
    public String fax() {
        return fax(null);
    }

    /**
     * Returns the url value of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @param key the key identifying the url to return
     * @return the url identified by the key or the primary url if key is null
     */
    public String url(String key) {
        if (key == null) key = JSContactForRdapMapId.URL_ID.getValue();
        return (jsCard.getLinks()!=null && jsCard.getLinks().get(key)!=null) ? jsCard.getLinks().get(key).getUri() : null;
    }


    /**
     * Returns the primary url value of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the primary url value
     */
    public String url() {
        return url(null);
    }

    /**
     * Returns the contact uri value of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @param key the key identifying the contact uri to return
     * @return the contact uri identified by the key or the primary contact uri if key is null
     */
    public String contactUri(String key) {
        if (key == null) key = JSContactForRdapMapId.CONTACT_URI_ID.getValue();
        return (jsCard.getLinks()!=null && jsCard.getLinks().get(key)!=null) ? jsCard.getLinks().get(key).getUri() : null;
    }

    /**
     * Returns the primary contact uri value of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the primary contact uri value
     */
    public String contactUri() {
        return contactUri(null);
    }

    /**
     * Returns the address as JSContact Address object of this JSContactForRdapGetter object
     *
     * @param key the key identifying the address to return
     * @return the address identified by the key or the primary address if key is null
     */
    public Address addr(String key) {
        if (key == null) key = JSContactForRdapMapId.ADDRESS_ID.getValue();
        return (jsCard.getAddresses()!=null) ? jsCard.getAddresses().get(key) : null;
    }


    /**
     * Returns the primary address as JSContact Address object of this JSContactForRdapGetter object
     *
     * @return the primary address as JSContact Address object
     */
    public Address addr() {
        return addr(null);
    }


    /**
     * Returns a name localization as JSContact Name object of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the name localization as JSContact Name object if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be cast to a JSContact Name object
     */
    public Name nameLoc(String language) throws InternalErrorException {
        try {
            return (jsCard.getLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue())!=null) ? mapper.treeToValue(jsCard.getLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue()), Name.class) : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Name object");
        }
    }

    /**
     * Returns an organization localization of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the organization localization if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be cast to a JSContact Organization object
     */
    public String orgLoc(String language) {
        try {
            JsonNode node = (jsCard.getLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue())!=null) ? jsCard.getLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue()) : null;
            return (node!=null) ? mapper.treeToValue(node.get(JSContactForRdapMapId.ORG_ID.getValue()), Organization.class).getName() : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Organization object");
        }
    }


    /**
     * Returns an address localization as JSContact Address object of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @param key the key identifying the address localization to return
     * @return the address localization identified by the key or the primary address if key is null, null otherwise
     * @throws InternalErrorException if the localization cannot be cast to a JSContact Address object
     */
    public Address addrLoc(String language, String key) throws InternalErrorException {
        try {
            JsonNode node = (jsCard.getLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue())!=null) ? jsCard.getLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue()) : null;
            return (node!=null) ? mapper.treeToValue(node.get((key == null) ? JSContactForRdapMapId.ADDRESS_ID.getValue() : key),Address.class) : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Address object");
        }
    }

    /**
     * Returns the primary address localization as JSContact Address object of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the primary address localization as JSContact Address object if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be cast to a JSContact Address object
     */
    public Address addrLoc(String language) throws InternalErrorException {
        return addrLoc(language, null);
    }

    /**
     * Returns an email address localization of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @param key the key identifying the email address localization to return
     * @return the email address localization identified by the key or the primary email address if key is null, null otherwise
     * @throws InternalErrorException if the localization cannot be cast to a JSContact EmailAddress object
     */
    public String emailLoc(String language, String key) {
        try {
            JsonNode node = (jsCard.getLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue())!=null) ? jsCard.getLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue()) : null;
            return (node !=null) ? mapper.treeToValue(node.get((key == null) ? JSContactForRdapMapId.EMAIL_ID.getValue() : key), EmailAddress.class).getAddress() : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Address object");
        }
    }

    /**
     * Returns the primary email address localization of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the primary email address localization if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be cast to a JSContact EmailAddress object
     */
    public String emailLoc(String language) {
        return emailLoc(language, null);
    }

}
