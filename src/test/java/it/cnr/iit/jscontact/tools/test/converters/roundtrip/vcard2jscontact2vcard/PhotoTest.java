package it.cnr.iit.jscontact.tools.test.converters.roundtrip.vcard2jscontact2vcard;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardReader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoTest extends RoundtripTest {

    @Test
    public void testPhoto() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PHOTO:http://www.example.com/pub/photos/jqpublic.gif\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhoto1 - 1", vcard2, VCardReader.parse(vcard).get(0));
    }

}
