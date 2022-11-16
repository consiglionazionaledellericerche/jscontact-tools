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
        assertEquals("testSchedulingAddress - 2", "mailto:janedoe@example.com", jsCard.getSchedulingAddresses().get("SCHEDULING-1").getUri());
        assertEquals("testSchedulingAddress - 3", 1, (int) jsCard.getSchedulingAddresses().get("SCHEDULING-1").getPref());
        assertEquals("testSchedulingAddress - 4", "http://example.com/calendar/jdoe", jsCard.getSchedulingAddresses().get("SCHEDULING-2").getUri());
        assertNull("testSchedulingAddress - 5", jsCard.getSchedulingAddresses().get("SCHEDULING-2").getPref());
    }

}
