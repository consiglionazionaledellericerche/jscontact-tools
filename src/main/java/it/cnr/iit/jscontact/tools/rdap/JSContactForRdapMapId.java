package it.cnr.iit.jscontact.tools.rdap;

import lombok.Getter;

public enum JSContactForRdapMapId {
    ADDRESS_ID("addr"),
    ORG_ID("org"),
    VOICE_ID("voice"),
    FAX_ID("fax"),
    EMAIL_ID("email"),
    URL_ID("url"),
    CONTACT_URI_ID("contact-uri"),
    ORG_LOCALIZATION_ID("organizations"),
    NAME_LOCALIZATION_ID("name"),
    ADDRESS_LOCALIZATION_ID("addresses"),
    EMAIL_LOCALIZATION_ID("emails");

    @Getter
    private final String value;

    JSContactForRdapMapId(String value) {
        this.value = value;
    }
}

