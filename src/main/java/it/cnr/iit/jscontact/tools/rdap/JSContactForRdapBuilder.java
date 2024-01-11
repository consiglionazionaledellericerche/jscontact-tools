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

    private static final String ADDRESS_ID = "addr";
    private static final String ORG_ID = "org";
    private static final String VOICE_ID = "voice";
    private static final String FAX_ID = "fax";
    private static final String EMAIL_ID = "email";
    private static final String URL_ID = "url";
    private static final String ORG_LOCALIZATION_ID = "organizations/org";
    private static final String NAME_LOCALIZATION_ID = "name";
    private static final String ADDRESS_LOCALIZATION_ID = "addresses/addr";
    private static final String EMAIL_LOCALIZATION_ID = "emails/email";

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
        this.jsCard.addOrganization(ORG_ID,Organization.builder().name(org).build());
        return this;
    }

    public JSContactForRdapBuilder email(String email) {
        this.jsCard.addEmailAddress(EMAIL_ID, EmailAddress.builder().address(email).build());
        return this;
    }

    public JSContactForRdapBuilder voice(String voice) {
        this.jsCard.addPhone(VOICE_ID, Phone.builder().number(voice).features(PhoneFeaturesBuilder.builder().voice().build()).build());
        return this;
    }

    public JSContactForRdapBuilder fax(String fax) {
        this.jsCard.addPhone(FAX_ID, Phone.builder().number(fax).features(PhoneFeaturesBuilder.builder().fax().build()).build());
        return this;
    }

    public JSContactForRdapBuilder url(String url) {
        this.jsCard.addLinkResource(URL_ID, Link.builder().uri(url).build());
        return this;
    }

    public JSContactForRdapBuilder addr(Address address) {
        this.jsCard.addAddress(ADDRESS_ID, address);
        return this;
    }

    public JSContactForRdapBuilder nameLocalization(String language, Name name) {
        this.jsCard.addLocalization(language, NAME_LOCALIZATION_ID, mapper.convertValue(name, JsonNode.class));
        return this;
    }

    public JSContactForRdapBuilder orgLocalization(String language, String org) {
        this.jsCard.addLocalization(language, ORG_LOCALIZATION_ID, mapper.convertValue(Organization.builder().name(org).build(), JsonNode.class));
        return this;
    }

    public JSContactForRdapBuilder addrLocalization(String language, Address address) {
        this.jsCard.addLocalization(language, ADDRESS_LOCALIZATION_ID, mapper.convertValue(address, JsonNode.class));
        return this;
    }

    public JSContactForRdapBuilder emailLocalization(String language, String email) {
        this.jsCard.addLocalization(language, EMAIL_LOCALIZATION_ID, mapper.convertValue(EmailAddress.builder().address(email).build(), JsonNode.class));
        return this;
    }

    public Card build() throws MissingFieldsException, CardException {

        if (jsCard.getName() == null &&
            jsCard.getOrganizations() == null &&
            jsCard.getAddresses() == null &&
            jsCard.getPhones() == null &&
            jsCard.getEmails() == null &&
            jsCard.getLinks() == null)
            throw new MissingFieldsException("At least one between name, organizations, addresses, phones, emails and links must be set in JSCard");

        if (!jsCard.isValid())
            throw new CardException(jsCard.getValidationMessage());

        return jsCard;
    }
}
