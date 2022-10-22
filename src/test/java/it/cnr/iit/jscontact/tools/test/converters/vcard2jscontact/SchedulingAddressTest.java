package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchedulingAddressTest extends VCard2JSContactTest {

    @Test
    public void testSchedulingAddress() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALADRURI;PREF=1:mailto:janedoe@example.com\n" +
                "CALADRURI:http://example.com/calendar/jdoe\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSchedulingAddress - 1", 2, jsCard.getSchedulingAddresses().size());
        assertEquals("testSchedulingAddress - 2", 1, jsCard.getSchedulingAddresses().get("SCHEDULING-1").getSendTo().size());
        assertTrue("testSchedulingAddress - 3", jsCard.getSchedulingAddresses().get("SCHEDULING-1").getSendTo().containsValue("mailto:janedoe@example.com"));
        assertEquals("testSchedulingAddress - 4", 1, (int) jsCard.getSchedulingAddresses().get("SCHEDULING-1").getPref());
        assertEquals("testSchedulingAddress - 5", 1, jsCard.getSchedulingAddresses().get("SCHEDULING-2").getSendTo().size());
        assertTrue("testSchedulingAddress - 6", jsCard.getSchedulingAddresses().get("SCHEDULING-2").getSendTo().containsValue("http://example.com/calendar/jdoe"));
        assertNull("testSchedulingAddress - 7", jsCard.getSchedulingAddresses().get("SCHEDULING-2").getPref());
    }


}
