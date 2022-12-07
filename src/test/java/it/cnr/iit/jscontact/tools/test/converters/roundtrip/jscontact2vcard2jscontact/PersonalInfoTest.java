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

public class PersonalInfoTest extends RoundtripTest {

    @Test
    public void testPersonalInfo() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"personalInfo\":{ " +
                "\"PERSINFO-1\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"type\": \"expertise\"," +
                "\"value\": \"chemistry\"," +
                "\"level\": \"high\" " +
                "}," +
                "\"PERSINFO-2\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"type\": \"expertise\"," +
                "\"value\": \"chinese literature\"," +
                "\"level\": \"low\"" +
                "}," +
                "\"PERSINFO-3\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"type\": \"hobby\"," +
                "\"value\": \"reading\"," +
                "\"level\": \"high\"" +
                "}," +
                "\"PERSINFO-4\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"type\": \"hobby\"," +
                "\"value\": \"sewing\"," +
                "\"level\": \"high\"" +
                "}," +
                "\"PERSINFO-5\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"type\": \"interest\"," +
                "\"value\": \"r&b music\"," +
                "\"level\": \"medium\" " +
                "}," +
                "\"PERSINFO-6\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"type\": \"interest\"," +
                "\"value\": \"rock 'n' roll music\"," +
                "\"level\": \"high\" " +
                "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPersonalInfo - 1", jscard2, Card.toJSCard(jscard));
    }
}
