package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContactProfileIds;

public class RdapJSContactProfileIds {

    public static JSContactProfileIds getInstance() {

        return JSContactProfileIds.builder()
                .id(JSContactProfileIds.JSContactId.organizationsId("org"))
                .id(JSContactProfileIds.JSContactId.emailsId("email")) // 1st email
                .id(JSContactProfileIds.JSContactId.emailsId("email-1")) // 2nd email
                .id(JSContactProfileIds.JSContactId.emailsId("email-2")) // 3rd email
                .id(JSContactProfileIds.JSContactId.emailsId("email-3")) // 4th email
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.voicesId("voice")))  // 1st jCard voice number
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.voicesId("voice-1")))  // 2nd jCard voice number
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.voicesId("voice-2")))  // 3rd jCard voice number
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.voicesId("voice-3")))  // 4th jCard voice number
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.faxesId("fax")))  // 1st jCard fax number
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.faxesId("fax-1")))  // 2nd jCard fax number
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.faxesId("fax-2")))  // 3rd jCard fax number
                .id(JSContactProfileIds.JSContactId.phonesId(JSContactProfileIds.PhoneId.faxesId("fax-3")))  // 4th jCard fax number
                .id(JSContactProfileIds.JSContactId.addressesId("addr")) // 1st jCard address
                .id(JSContactProfileIds.JSContactId.addressesId("addr-1")) // 2nd jCard address
                .id(JSContactProfileIds.JSContactId.addressesId("addr-2")) // 3rd jCard address
                .id(JSContactProfileIds.JSContactId.addressesId("addr-3")) // 4th jCard address
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.linksId("url")))  // 1st jCard url
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.linksId("url-1")))  // 2nd jCard url
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.linksId("url-2")))  // 3rd jCard url
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.linksId("url-3")))  // 4th jCard url
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.contactsId("contact-uri")))    // 1st jCard contact uri
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.contactsId("contact-uri-1")))    // 2nd jCard contact uri
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.contactsId("contact-uri-2")))    // 3rd jCard contact uri
                .id(JSContactProfileIds.JSContactId.resourcesId(JSContactProfileIds.ResourceId.contactsId("contact-uri-3")))    // 4th jCard contact uri
                .build();
    }


}
