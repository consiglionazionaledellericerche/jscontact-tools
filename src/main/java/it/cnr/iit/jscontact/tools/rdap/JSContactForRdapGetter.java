package it.cnr.iit.jscontact.tools.rdap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.*;
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
     * @return the email address
     */
    public String email() {
        return (jsCard.getEmails()!=null && jsCard.getEmails().get(JSContactForRdapMapId.EMAIL_ID.getValue())!=null) ? jsCard.getEmails().get(JSContactForRdapMapId.EMAIL_ID.getValue()).getAddress() : null;
    }

    /**
     * Returns the voice number of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the voice number
     */
    public String voice() {
        return (jsCard.getPhones()!=null && jsCard.getPhones().get(JSContactForRdapMapId.VOICE_ID.getValue())!=null) ? jsCard.getPhones().get(JSContactForRdapMapId.VOICE_ID.getValue()).getNumber() : null;
    }

    /**
     * Returns the fax number of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the fax number
     */
    public String fax() {
        return (jsCard.getPhones()!=null && jsCard.getPhones().get(JSContactForRdapMapId.FAX_ID.getValue())!=null) ? jsCard.getPhones().get(JSContactForRdapMapId.FAX_ID.getValue()).getNumber() : null;
    }

    /**
     * Returns the url value of this JSContactForRdapGetter object if it is set, null otherwise
     *
     * @return the url value
     */
    public String url() {
        return (jsCard.getLinks()!=null && jsCard.getLinks().get(JSContactForRdapMapId.URL_ID.getValue())!=null) ? jsCard.getLinks().get(JSContactForRdapMapId.URL_ID.getValue()).getUri() : null;
    }

    /**
     * Returns the address as JSContact Address object of this JSContactForRdapGetter object
     *
     * @return the address as JSContact Address object
     */
    public Address address() {
        return (jsCard.getAddresses()!=null) ? jsCard.getAddresses().get(JSContactForRdapMapId.ADDRESS_ID.getValue()) : null;
    }

    /**
     * Returns a name localization as JSContact Name object of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the name localization as JSContact Name object if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be casted to a JSContact Name object
     */
    public Name nameLoc(String language) throws InternalErrorException {
        try {
            return (jsCard.getLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue())!=null) ? mapper.treeToValue(jsCard.getLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue()), Name.class) : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Name object");
        }
    }

    /**
     * Returns a organization localization of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the organization localization if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be casted to a JSContact Organization object
     */
    public String orgLoc(String language) {
        try {
            return (jsCard.getLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue())!=null) ? mapper.treeToValue(jsCard.getLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue()), Organization.class).getName() : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Organization object");
        }
    }

    /**
     * Returns an address localization as JSContact Address object of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the address localization as JSContact Address object if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be casted to a JSContact Address object
     */
    public Address addressLoc(String language) throws InternalErrorException {
        try {
            return (jsCard.getLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue())!=null) ? mapper.treeToValue(jsCard.getLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue()), Address.class) : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Address object");
        }
    }

    /**
     * Returns an email address localization of this JSContactForRdapGetter object
     *
     * @param language the localization language
     * @return the email address localization if it is set, null otherwise
     * @throws InternalErrorException if the localization cannot be casted to a JSContact EmailAddress object
     */
    public String emailLoc(String language) {
        try {
            return (jsCard.getLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue())!=null) ? mapper.treeToValue(jsCard.getLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue()), EmailAddress.class).getAddress() : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Address object");
        }
    }

}
