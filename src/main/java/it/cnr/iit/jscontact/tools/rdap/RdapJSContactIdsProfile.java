package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContactIdsProfile;

public class RdapJSContactIdsProfile {

    public static JSContactIdsProfile getInstance() {

        return JSContactIdsProfile.builder()
                .id(JSContactIdsProfile.JSContactId.organizationsId("org"))
                .id(JSContactIdsProfile.JSContactId.emailsId("email")) // 1st email
                .id(JSContactIdsProfile.JSContactId.phonesId("voice"))  // 1st jCard phone number
                .id(JSContactIdsProfile.JSContactId.phonesId("fax"))    // 2nd jCard phone number
                .id(JSContactIdsProfile.JSContactId.addressesId("addr")) // 1st jCard address
                .id(JSContactIdsProfile.JSContactId.resourcesId(JSContactIdsProfile.ResourceId.linksId("url")))  // 1st jCard url
                .id(JSContactIdsProfile.JSContactId.resourcesId(JSContactIdsProfile.ResourceId.contactsId("contact-uri")))    // 1st jCard contact uri
                .build();

    }
}
