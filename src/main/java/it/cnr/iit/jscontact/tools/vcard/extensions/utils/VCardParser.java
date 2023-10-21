package it.cnr.iit.jscontact.tools.vcard.extensions.utils;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedAddressScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class VCardParser {

    public static List<VCard> parse(String str) {

        return Ezvcard.parse(str)
                      .caretDecoding(true)
                      .register(new ExtendedAddressScribe())
                      .register(new ExtendedStructuredNameScribe())
                      .all();
    }

    public static List<VCard> parse(File file) throws IOException {

        return Ezvcard.parse(file)
                .caretDecoding(true)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }

    public static List<VCard> parse(Reader reader) throws IOException {

        return Ezvcard.parse(reader)
                .caretDecoding(true)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }


    public static List<VCard> parseJson(String str) {

        return Ezvcard.parseJson(str)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }

    public static List<VCard> parseJson(File file) throws IOException {

        return Ezvcard.parseJson(file)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }

    public static List<VCard> parseJson(Reader reader) throws IOException {

        return Ezvcard.parseJson(reader)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }


    public static List<VCard> parseXml(String str) {

        return Ezvcard.parseXml(str)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }

    public static List<VCard> parseXml(File file) throws IOException {

        return Ezvcard.parseXml(file)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }

    public static List<VCard> parseXml(Reader reader) throws IOException {

        return Ezvcard.parseXml(reader)
                .register(new ExtendedAddressScribe())
                .register(new ExtendedStructuredNameScribe())
                .all();
    }
}
