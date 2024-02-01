package it.cnr.iit.jscontact.tools.rdap;

import lombok.Getter;

public enum JSContactForRdapMapId {
    ADDRESS_ID("addr"),
    ORG_ID("org"),
    VOICE_ID("voice"),
    FAX_ID("fax"),
    EMAIL_ID("email"),
    URL_ID("url"),
    ORG_LOCALIZATION_ID("organizations/org"),
    NAME_LOCALIZATION_ID("name"),
    ADDRESS_LOCALIZATION_ID("addresses/addr"),
    EMAIL_LOCALIZATION_ID("emails/email");

    @Getter
    private final String value;

    JSContactForRdapMapId(String value) {
        this.value = value;
    }
}

