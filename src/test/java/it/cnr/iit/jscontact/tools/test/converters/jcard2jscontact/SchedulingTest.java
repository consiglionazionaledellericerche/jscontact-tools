package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchedulingTest extends JCard2JSContactTest {

    @Test
    public void testScheduling() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caladruri\", {\"pref\": 1}, \"uri\", \"mailto:janedoe@example.com\"], " +
                "[\"caladruri\", {}, \"uri\", \"http://example.com/calendar/jdoe\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testScheduling - 1", 2, jsCard.getScheduling().size());
        assertEquals("testScheduling - 2", 1, jsCard.getScheduling().get("CALADRURI-1").getSendTo().size());
        assertTrue("testScheduling - 3", jsCard.getScheduling().get("CALADRURI-1").getSendTo().containsValue("mailto:janedoe@example.com"));
        assertEquals("testScheduling - 4", 1, (int) jsCard.getScheduling().get("CALADRURI-1").getPref());
        assertEquals("testScheduling - 5", 1, jsCard.getScheduling().get("CALADRURI-2").getSendTo().size());
        assertTrue("testScheduling - 6", jsCard.getScheduling().get("CALADRURI-2").getSendTo().containsValue("http://example.com/calendar/jdoe"));
        assertNull("testScheduling - 7", jsCard.getScheduling().get("CALADRURI-2").getPref());
    }

}
