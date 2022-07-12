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
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OnlineServiceTest extends JSContact2VCardTest {

    @Test
    public void testOnlineServiceValid1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"onlineServices\": {"+
                    "\"OS-1\": {" +
                        "\"@type\":\"OnlineService\"," +
                        "\"contexts\": {\"private\": true}," +
                        "\"pref\": 1, " +
                        "\"uri\": \"xmpp:alice@example.com\"" +
                    "}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOnlineServiceValid1 - 1", 1, vcard.getImpps().size());
        assertEquals("testOnlineServiceValid1 - 2", "alice@example.com", vcard.getImpps().get(0).getHandle());
        assertEquals("testOnlineServiceValid1 - 3", "home", vcard.getImpps().get(0).getParameter("TYPE"));
        assertEquals("testOnlineServiceValid1 - 4", 1, (int) vcard.getImpps().get(0).getPref());
        assertEquals("testOnlineServiceValid1 - 5", "OS-1", vcard.getImpps().get(0).getParameter(PROP_ID_PARAM));
    }
}
