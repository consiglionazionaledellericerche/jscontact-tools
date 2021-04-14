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

import static org.junit.Assert.assertTrue;

public class EmailAddressTest extends JSContact2VCardTest {

    @Test
    public void testEmailAddressValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"emails\":{ \"EMAIL-1\": {\"contexts\": {\"work\": true},\"email\":\"jqpublic@xyz.example.com\"}}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testEmailAddressValid1 - 1",vcard.getEmails().size() == 1);
        assertTrue("testEmailAddressValid1 - 2",vcard.getEmails().get(0).getValue().equals("jqpublic@xyz.example.com"));
        assertTrue("testEmailAddressValid1 - 3",vcard.getEmails().get(0).getTypes().size() == 1);
        assertTrue("testEmailAddressValid1 - 4",vcard.getEmails().get(0).getTypes().get(0).getValue().equals("work"));
    }

    @Test
    public void testEmailAddressValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"emails\":{ " +
                     "\"EMAIL-1\": {\"contexts\":{\"work\": true},\"email\":\"jqpublic@xyz.example.com\"}," +
                     "\"EMAIL-2\": {\"contexts\":{\"private\": true},\"pref\":1,\"email\":\"jane_doe@example.com\"}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testEmailAddressValid2 - 1",vcard.getEmails().size() == 2);
        assertTrue("testEmailAddressValid2 - 2",vcard.getEmails().get(0).getValue().equals("jqpublic@xyz.example.com"));
        assertTrue("testEmailAddressValid2 - 3",vcard.getEmails().get(0).getTypes().size() == 1);
        assertTrue("testEmailAddressValid2 - 4",vcard.getEmails().get(0).getTypes().get(0).getValue().equals("work"));
        assertTrue("testEmailAddressValid2 - 5",vcard.getEmails().get(1).getValue().equals("jane_doe@example.com"));
        assertTrue("testEmailAddressValid2 - 6",vcard.getEmails().get(1).getTypes().size() == 1);
        assertTrue("testEmailAddressValid2 - 7",vcard.getEmails().get(1).getTypes().get(0).getValue().equals("home"));
        assertTrue("testEmailAddressValid2 - 8",vcard.getEmails().get(1).getPref() == 1);
    }

}
