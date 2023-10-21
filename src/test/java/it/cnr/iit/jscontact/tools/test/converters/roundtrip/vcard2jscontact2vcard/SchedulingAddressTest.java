package it.cnr.iit.jscontact.tools.test.converters.roundtrip.vcard2jscontact2vcard;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchedulingAddressTest extends RoundtripTest {

    @Test
    public void testSchedulingAddress() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALADRURI;PREF=1:mailto:janedoe@example.com\n" +
                "CALADRURI:http://example.com/calendar/jdoe\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testSchedulingAddress - 1", vcard2, VCardParser.parse(vcard).get(0));
    }


}
