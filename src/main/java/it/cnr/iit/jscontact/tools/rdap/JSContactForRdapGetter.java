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

    public static JSContactForRdapGetter of(Card jsCard) throws MissingFieldException {
        if (jsCard == null)
            throw new MissingFieldException("A Card object is required");
        return new JSContactForRdapGetter(jsCard);
    }

    public String uid() {
        return jsCard.getUid();
    }

    public Name name() {
        return jsCard.getName();
    }

    public String org() {
        return (jsCard.getOrganizations()!=null && jsCard.getOrganizations().get(JSContactForRdapMapId.ORG_ID.getValue())!=null) ? jsCard.getOrganizations().get(JSContactForRdapMapId.ORG_ID.getValue()).getName() : null;
    }

    public String email() {
        return (jsCard.getEmails()!=null && jsCard.getEmails().get(JSContactForRdapMapId.EMAIL_ID.getValue())!=null) ? jsCard.getEmails().get(JSContactForRdapMapId.EMAIL_ID.getValue()).getAddress() : null;
    }

    public String voice() {
        return (jsCard.getPhones()!=null && jsCard.getPhones().get(JSContactForRdapMapId.VOICE_ID.getValue())!=null) ? jsCard.getPhones().get(JSContactForRdapMapId.VOICE_ID.getValue()).getNumber() : null;
    }

    public String fax() {
        return (jsCard.getPhones()!=null && jsCard.getPhones().get(JSContactForRdapMapId.FAX_ID.getValue())!=null) ? jsCard.getPhones().get(JSContactForRdapMapId.FAX_ID.getValue()).getNumber() : null;
    }

    public String url() {
        return (jsCard.getLinks()!=null && jsCard.getLinks().get(JSContactForRdapMapId.URL_ID.getValue())!=null) ? jsCard.getLinks().get(JSContactForRdapMapId.URL_ID.getValue()).getUri() : null;
    }

    public Address address() {
        return (jsCard.getAddresses()!=null) ? jsCard.getAddresses().get(JSContactForRdapMapId.ADDRESS_ID.getValue()) : null;
    }

    public Name nameLoc(String language) throws InternalErrorException {
        try {
            return (jsCard.getLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue())!=null) ? mapper.treeToValue(jsCard.getLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue()), Name.class) : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Name object");
        }
    }

    public String orgLoc(String language) {
        return (jsCard.getLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue())!=null) ? jsCard.getLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue()).asText() : null;
    }

    public Address addressLoc(String language) throws InternalErrorException {
        try {
            return (jsCard.getLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue())!=null) ? mapper.treeToValue(jsCard.getLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue()), Address.class) : null;
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Unable to cast localization to JSContact Address object");
        }
    }

    public String emailLoc(String language) {
        return (jsCard.getLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue())!=null) ? jsCard.getLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue()).asText() : null;
    }

}
