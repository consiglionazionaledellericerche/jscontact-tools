package it.cnr.iit.jscontact.tools.vcard.extensions.utils;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedAddressScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;

import java.util.Collection;

public class VCardWriter {

    public static String write(VCard... cards) {

        return Ezvcard.write(cards)
                      .register(new ExtendedAddressScribe())
                      .register(new ExtendedStructuredNameScribe())
                      .go();
    }

    public static String write(Collection<VCard> cards) {

        return Ezvcard.write(cards)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .go();
    }

    public static String writeJson(VCard... cards) {

        return Ezvcard.writeJson(cards)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .go();
    }

    public static String writeJson(Collection<VCard> cards) {

        return Ezvcard.writeJson(cards)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .go();
    }

    public static String writeXml(VCard... cards) {

        return Ezvcard.writeXml(cards)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .go();
    }

    public static String writeXml(Collection<VCard> cards) {

        return Ezvcard.writeXml(cards)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .go();
    }

}
