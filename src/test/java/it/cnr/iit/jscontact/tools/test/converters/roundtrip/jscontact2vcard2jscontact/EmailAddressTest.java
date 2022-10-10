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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.jscontact2vcard2jscontact;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EmailAddressTest extends RoundtripTest {

    @Test
    public void testEmailAddress1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"emails\":{ \"EMAIL-1\": {\"@type\":\"EmailAddress\",\"contexts\": {\"work\": true},\"email\":\"jqpublic@xyz.example.com\"}}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testEmailAddress1 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testEmailAddress2() throws IOException, CardException {

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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testEmailAddress2 - 1", jscard2, Card.toCard(jscard));
    }

}
