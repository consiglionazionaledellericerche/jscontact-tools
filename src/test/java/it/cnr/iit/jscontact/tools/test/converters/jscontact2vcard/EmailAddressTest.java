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

public class EmailAddressTest extends JSContact2VCardTest {

    @Test
    public void testEmailAddressValid1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"emails\":{ \"EMAIL-1\": {\"@type\":\"EmailAddress\",\"contexts\": {\"work\": true},\"email\":\"jqpublic@xyz.example.com\"}}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testEmailAddressValid1 - 1", 1, vcard.getEmails().size());
        assertEquals("testEmailAddressValid1 - 2", "jqpublic@xyz.example.com", vcard.getEmails().get(0).getValue());
        assertEquals("testEmailAddressValid1 - 3", 1, vcard.getEmails().get(0).getTypes().size());
        assertEquals("testEmailAddressValid1 - 4", "work", vcard.getEmails().get(0).getTypes().get(0).getValue());
        assertEquals("testEmailAddressValid1 - 5", "EMAIL-1", vcard.getEmails().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testEmailAddressValid2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"emails\":{ " +
                     "\"EMAIL-1\": {\"@type\":\"EmailAddress\",\"contexts\":{\"work\": true},\"email\":\"jqpublic@xyz.example.com\"}," +
                     "\"EMAIL-2\": {\"@type\":\"EmailAddress\",\"contexts\":{\"private\": true},\"pref\":1,\"email\":\"jane_doe@example.com\"}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testEmailAddressValid2 - 1", 2, vcard.getEmails().size());
        assertEquals("testEmailAddressValid2 - 2", "jqpublic@xyz.example.com", vcard.getEmails().get(0).getValue());
        assertEquals("testEmailAddressValid2 - 3", 1, vcard.getEmails().get(0).getTypes().size());
        assertEquals("testEmailAddressValid2 - 4", "work", vcard.getEmails().get(0).getTypes().get(0).getValue());
        assertEquals("testEmailAddressValid2 - 5", "jane_doe@example.com", vcard.getEmails().get(1).getValue());
        assertEquals("testEmailAddressValid2 - 6", 1, vcard.getEmails().get(1).getTypes().size());
        assertEquals("testEmailAddressValid2 - 7", "home", vcard.getEmails().get(1).getTypes().get(0).getValue());
        assertEquals("testEmailAddressValid2 - 8", 1, (int) vcard.getEmails().get(1).getPref());
        assertEquals("testEmailAddressValid2 - 9", "EMAIL-1", vcard.getEmails().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testEmailAddressValid2 - 10", "EMAIL-2", vcard.getEmails().get(1).getParameter(PROP_ID_PARAM));
    }

}
