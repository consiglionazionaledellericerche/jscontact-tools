package it.cnr.iit.jscontact.tools.rdap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.utils.builders.PhoneFeaturesBuilder;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class JSContactForRdapBuilder {

    private Card jsCard;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JSContactForRdapBuilder builder() {
        return new JSContactForRdapBuilder(Card.builder().uid(UUID.randomUUID().toString()).build());
    }

    public JSContactForRdapBuilder uid(String uid) {
        this.jsCard.setUid(uid);
        return this;
    }

    public JSContactForRdapBuilder name(Name name) {
        this.jsCard.setName(name);
        return this;
    }

    public JSContactForRdapBuilder org(String org) {
        this.jsCard.addOrganization(JSContactForRdapMapId.ORG_ID.getValue(),Organization.builder().name(org).build());
        return this;
    }

    public JSContactForRdapBuilder email(String email) {
        this.jsCard.addEmailAddress(JSContactForRdapMapId.EMAIL_ID.getValue(), EmailAddress.builder().address(email).build());
        return this;
    }

    public JSContactForRdapBuilder voice(String voice) {
        this.jsCard.addPhone(JSContactForRdapMapId.VOICE_ID.getValue(), Phone.builder().number(voice).features(PhoneFeaturesBuilder.builder().voice().build()).build());
        return this;
    }

    public JSContactForRdapBuilder fax(String fax) {
        this.jsCard.addPhone(JSContactForRdapMapId.FAX_ID.getValue(), Phone.builder().number(fax).features(PhoneFeaturesBuilder.builder().fax().build()).build());
        return this;
    }

    public JSContactForRdapBuilder url(String url) {
        this.jsCard.addLinkResource(JSContactForRdapMapId.URL_ID.getValue(), Link.builder().uri(url).build());
        return this;
    }

    public JSContactForRdapBuilder address(Address address) {
        this.jsCard.addAddress(JSContactForRdapMapId.ADDRESS_ID.getValue(), address);
        return this;
    }

    public JSContactForRdapBuilder nameLoc(String language, Name name) {
        this.jsCard.addLocalization(language, JSContactForRdapMapId.NAME_LOCALIZATION_ID.getValue(), mapper.convertValue(name, JsonNode.class));
        return this;
    }

    public JSContactForRdapBuilder orgLoc(String language, String org) {
        this.jsCard.addLocalization(language, JSContactForRdapMapId.ORG_LOCALIZATION_ID.getValue(), mapper.convertValue(Organization.builder().name(org).build(), JsonNode.class));
        return this;
    }

    public JSContactForRdapBuilder addrLoc(String language, Address address) {
        this.jsCard.addLocalization(language, JSContactForRdapMapId.ADDRESS_LOCALIZATION_ID.getValue(), mapper.convertValue(address, JsonNode.class));
        return this;
    }

    public JSContactForRdapBuilder emailLoc(String language, String email) {
        this.jsCard.addLocalization(language, JSContactForRdapMapId.EMAIL_LOCALIZATION_ID.getValue(), mapper.convertValue(EmailAddress.builder().address(email).build(), JsonNode.class));
        return this;
    }

    public Card build() throws MissingFieldException, CardException {

        if (jsCard.getName() == null &&
            jsCard.getOrganizations() == null &&
            jsCard.getAddresses() == null &&
            jsCard.getPhones() == null &&
            jsCard.getEmails() == null &&
            jsCard.getLinks() == null)
            throw new MissingFieldException("At least one between name, organizations, addresses, phones, emails and links must be set in JSCard");

        if (!jsCard.isValid())
            throw new CardException(jsCard.getValidationMessage());

        return jsCard;
    }
}
