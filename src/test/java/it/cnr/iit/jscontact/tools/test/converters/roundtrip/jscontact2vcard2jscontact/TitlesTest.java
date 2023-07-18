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

public class TitlesTest extends RoundtripTest {

    @Test
    public void testTitles1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359h\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"name\": \"Research Scientist\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                   "\"it\" : { " +
                      "\"titles/TITLE-1\": { \"@type\":\"Title\",\"name\": \"Ricercatore\" } " +
                   "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testTitles1 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testTitles2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"name\": \"Research Scientist\""  +
                    "}," +
                    "\"TITLE-2\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"name\": \"IETF Area Director\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                    "\"it\" : { " +
                        "\"titles/TITLE-1\": { \"@type\":\"Title\",\"name\": \"Ricercatore\" } " +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testTitles2 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testTitles3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359g\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"name\": \"Research Scientist\"" +
                    "}," +
                    "\"TITLE-2\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"name\": \"IETF Area Director\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                    "\"it\" : { " +
                        "\"titles/TITLE-1\": { \"@type\":\"Title\",\"name\": \"Ricercatore\" }, " +
                        "\"titles/TITLE-2\": { \"@type\":\"Title\",\"name\": \"Direttore Area IETF\" } " +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testTitles3 - 1", jscard2, Card.toJSCard(jscard));
    }

}
