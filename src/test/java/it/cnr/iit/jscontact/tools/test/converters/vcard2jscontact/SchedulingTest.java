package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class SchedulingTest extends VCard2JSContactTest {

    @Test
    public void testSchedulingValid() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALADRURI;PREF=1:mailto:janedoe@example.com\n" +
                "CALADRURI:http://example.com/calendar/jdoe\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSchedulingValid - 1",jsCard.getScheduling().size() == 2);
        assertTrue("testSchedulingValid - 2",jsCard.getScheduling().get("CALADRURI-1").getSendTo().size() == 1);
        assertTrue("testSchedulingValid - 3",jsCard.getScheduling().get("CALADRURI-1").getSendTo().values().contains("mailto:janedoe@example.com"));
        assertTrue("testSchedulingValid - 4",jsCard.getScheduling().get("CALADRURI-1").getPref() == 1);
        assertTrue("testSchedulingValid - 5",jsCard.getScheduling().get("CALADRURI-2").getSendTo().size() == 1);
        assertTrue("testSchedulingValid - 6",jsCard.getScheduling().get("CALADRURI-2").getSendTo().values().contains("http://example.com/calendar/jdoe"));
        assertTrue("testSchedulingValid - 7",jsCard.getScheduling().get("CALADRURI-2").getPref() == null);
    }


}
