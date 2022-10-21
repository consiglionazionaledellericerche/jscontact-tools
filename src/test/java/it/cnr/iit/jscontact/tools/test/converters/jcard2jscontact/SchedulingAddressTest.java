package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchedulingAddressTest extends JCard2JSContactTest {

    @Test
    public void testSchedulingAddress() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caladruri\", {\"pref\": 1}, \"uri\", \"mailto:janedoe@example.com\"], " +
                "[\"caladruri\", {}, \"uri\", \"http://example.com/calendar/jdoe\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testSchedulingAddress - 1", 2, jsCard.getSchedulingAddresses().size());
        assertEquals("testSchedulingAddress - 2", 1, jsCard.getSchedulingAddresses().get("CALADRURI-1").getSendTo().size());
        assertTrue("testSchedulingAddress - 3", jsCard.getSchedulingAddresses().get("CALADRURI-1").getSendTo().containsValue("mailto:janedoe@example.com"));
        assertEquals("testSchedulingAddress - 4", 1, (int) jsCard.getSchedulingAddresses().get("CALADRURI-1").getPref());
        assertEquals("testSchedulingAddress - 5", 1, jsCard.getSchedulingAddresses().get("CALADRURI-2").getSendTo().size());
        assertTrue("testSchedulingAddress - 6", jsCard.getSchedulingAddresses().get("CALADRURI-2").getSendTo().containsValue("http://example.com/calendar/jdoe"));
        assertNull("testSchedulingAddress - 7", jsCard.getSchedulingAddresses().get("CALADRURI-2").getPref());
    }

}
