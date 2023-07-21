package it.cnr.iit.jscontact.tools.test.converters.roundtrip;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard.JSContact2VCard;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedAddress;

public class RoundtripTest {

    protected final JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                                                                                                            .setAutoAddrLabel(false)
                                                                                                            .setPropIdParam(false)
                                                                                                             .convertTimezoneToOffset(true)
                                                                                                             .build())
                                                                               .build();

    protected final VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder()
                                                                                                            .setAutoFullAddress(false)
                                                                                                            .setVoiceAsDefaultPhoneFeature(false)
                                                                                                            .build())
                                                                               .build();

    protected void pruneVCard(VCard vcard) {

        if (vcard == null)
            return;

        vcard.setUid(null);
        vcard.getExtendedProperties().clear();
        if (vcard.getProperty(ExtendedAddress.class) != null) {

            for (ExtendedAddress ea : vcard.getProperties(ExtendedAddress.class)) {

                ea.getStreetNames().clear();
                ea.getStreetNumbers().clear();
                ea.getDirections().clear();
                ea.getDistricts().clear();
                ea.getSubDistricts().clear();
                ea.getBlocks().clear();
                ea.getLandmarks().clear();
            }

        }

    }

}
