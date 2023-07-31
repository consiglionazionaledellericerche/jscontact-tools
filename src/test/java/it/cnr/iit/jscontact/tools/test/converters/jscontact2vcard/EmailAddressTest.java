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
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EmailAddressTest extends JSContact2VCardTest {

    @Test
    public void testEmailAddress1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"emails\":{ \"EMAIL-1\": {\"@type\":\"EmailAddress\",\"contexts\": {\"work\": true},\"address\":\"jqpublic@xyz.example.com\"}}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testEmailAddress1 - 1", 1, vcard.getEmails().size());
        assertEquals("testEmailAddress1 - 2", "jqpublic@xyz.example.com", vcard.getEmails().get(0).getValue());
        assertEquals("testEmailAddress1 - 3", 1, vcard.getEmails().get(0).getTypes().size());
        assertEquals("testEmailAddress1 - 4", "work", vcard.getEmails().get(0).getTypes().get(0).getValue());
        assertEquals("testEmailAddress1 - 5", "EMAIL-1", vcard.getEmails().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testEmailAddress2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"emails\":{ " +
                     "\"EMAIL-1\": {\"@type\":\"EmailAddress\",\"contexts\":{\"work\": true},\"address\":\"jqpublic@xyz.example.com\"}," +
                     "\"EMAIL-2\": {\"@type\":\"EmailAddress\",\"contexts\":{\"private\": true},\"pref\":1,\"address\":\"jane_doe@example.com\"}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testEmailAddress2 - 1", 2, vcard.getEmails().size());
        assertEquals("testEmailAddress2 - 2", "jqpublic@xyz.example.com", vcard.getEmails().get(0).getValue());
        assertEquals("testEmailAddress2 - 3", 1, vcard.getEmails().get(0).getTypes().size());
        assertEquals("testEmailAddress2 - 4", "work", vcard.getEmails().get(0).getTypes().get(0).getValue());
        assertEquals("testEmailAddress2 - 5", "jane_doe@example.com", vcard.getEmails().get(1).getValue());
        assertEquals("testEmailAddress2 - 6", 1, vcard.getEmails().get(1).getTypes().size());
        assertEquals("testEmailAddress2 - 7", "home", vcard.getEmails().get(1).getTypes().get(0).getValue());
        assertEquals("testEmailAddress2 - 8", 1, (int) vcard.getEmails().get(1).getPref());
        assertEquals("testEmailAddress2 - 9", "EMAIL-1", vcard.getEmails().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testEmailAddress2 - 10", "EMAIL-2", vcard.getEmails().get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

}
