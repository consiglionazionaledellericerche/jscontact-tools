package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContactIdsProfile;

public class RdapJSContactIdsProfile {

    public static JSContactIdsProfile getInstance() {

        return JSContactIdsProfile.builder()
                .id(JSContactIdsProfile.JSContactId.organizationsId("org"))
                .id(JSContactIdsProfile.JSContactId.emailsId("email"))
                .id(JSContactIdsProfile.JSContactId.phonesId("voice"))  // 1st jCard phone number
                .id(JSContactIdsProfile.JSContactId.phonesId("fax"))    // 2nd jCard phone number
                .id(JSContactIdsProfile.JSContactId.addressesId("addr")) // 1st jCard address
                .build();

    }
}
