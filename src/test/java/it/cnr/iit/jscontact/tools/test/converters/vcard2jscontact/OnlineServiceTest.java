/*
 *    Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OnlineServiceTest extends VCard2JSContactTest {

    @Test
    public void testOnlineService1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "IMPP;TYPE=home;PREF=1:xmpp:alice@example.com\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOnlineService1 - 1", 1, jsCard.getOnlineServices().size());
        assertEquals("testOnlineService1 - 2", "xmpp:alice@example.com", jsCard.getOnlineServices().get("OS-1").getUri());
        assertTrue("testOnlineService1 - 3",jsCard.getOnlineServices().get("OS-1").asPrivate());
        assertEquals("testOnlineService1 - 4", 1, (int) jsCard.getOnlineServices().get("OS-1").getPref());
        assertEquals("testOnlineService1 - 5", jsCard.getOnlineServices().get("OS-1").getVCardName(),"impp");
    }

    @Test
    public void testOnlineService2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOCIALPROFILE;SERVICE-TYPE=Twitter;TYPE=home;PREF=1:https://twitter.com/ietf\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOnlineService2 - 1", 1, jsCard.getOnlineServices().size());
        assertEquals("testOnlineService2 - 2", "https://twitter.com/ietf", jsCard.getOnlineServices().get("OS-1").getUri());
        assertTrue("testOnlineService2 - 3",jsCard.getOnlineServices().get("OS-1").asPrivate());
        assertEquals("testOnlineService2 - 4", 1, (int) jsCard.getOnlineServices().get("OS-1").getPref());
        assertEquals("testOnlineService2 - 5", "Twitter", jsCard.getOnlineServices().get("OS-1").getService());
    }

    @Test
    public void testOnlineService3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOCIALPROFILE;VALUE=text;SERVICE-TYPE=Twitter;TYPE=home;PREF=1:https://twitter.com/ietf\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOnlineService3 - 1", 1, jsCard.getOnlineServices().size());
        assertEquals("testOnlineService3 - 2", "https://twitter.com/ietf", jsCard.getOnlineServices().get("OS-1").getUser());
        assertTrue("testOnlineService3 - 3",jsCard.getOnlineServices().get("OS-1").asPrivate());
        assertEquals("testOnlineService3 - 4", 1, (int) jsCard.getOnlineServices().get("OS-1").getPref());
        assertEquals("testOnlineService3 - 5", "Twitter", jsCard.getOnlineServices().get("OS-1").getService());
    }

}
