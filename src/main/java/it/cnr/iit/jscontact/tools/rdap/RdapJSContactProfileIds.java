package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContactProfileIds;

public class RdapJSContactProfileIds {

    public static JSContactProfileIds getInstance() {

        return JSContactProfileIds.builder()
                .id(JSContactProfileIds.JSContactId.organizationsId("org"))
                .id(JSContactProfileIds.JSContactId.emailsId("email")) // 1st email
                .id(JSContactProfileIds.JSContactId.phonesId("voice"))  // 1st jCard phone number
                .id(JSContactProfileIds.JSContactId.phonesId("fax"))    // 2nd jCard phone number
                .id(JSContactProfileIds.JSContactId.addressesId("addr")) // 1st jCard address
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.linksId("url")))  // 1st jCard url
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.contactsId("contact-uri")))    // 1st jCard contact uri
                .build();

    }
}
