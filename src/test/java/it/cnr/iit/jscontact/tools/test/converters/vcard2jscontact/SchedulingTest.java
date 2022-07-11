package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchedulingTest extends VCard2JSContactTest {

    @Test
    public void testSchedulingValid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALADRURI;PREF=1:mailto:janedoe@example.com\n" +
                "CALADRURI:http://example.com/calendar/jdoe\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSchedulingValid - 1", 2, jsCard.getScheduling().size());
        assertEquals("testSchedulingValid - 2", 1, jsCard.getScheduling().get("CALADRURI-1").getSendTo().size());
        assertTrue("testSchedulingValid - 3", jsCard.getScheduling().get("CALADRURI-1").getSendTo().containsValue("mailto:janedoe@example.com"));
        assertEquals("testSchedulingValid - 4", 1, (int) jsCard.getScheduling().get("CALADRURI-1").getPref());
        assertEquals("testSchedulingValid - 5", 1, jsCard.getScheduling().get("CALADRURI-2").getSendTo().size());
        assertTrue("testSchedulingValid - 6", jsCard.getScheduling().get("CALADRURI-2").getSendTo().containsValue("http://example.com/calendar/jdoe"));
        assertNull("testSchedulingValid - 7", jsCard.getScheduling().get("CALADRURI-2").getPref());
    }


}
