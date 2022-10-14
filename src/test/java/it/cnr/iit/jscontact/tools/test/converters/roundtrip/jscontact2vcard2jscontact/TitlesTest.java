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
                "\"fullName\":\"test\"," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"title\": \"Research Scientist\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                   "\"it\" : { " +
                      "\"titles/TITLE-1\": { \"@type\":\"Title\",\"title\": \"Ricercatore\" } " +
                   "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testTitles1 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testTitles2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"title\": \"Research Scientist\""  +
                    "}," +
                    "\"TITLE-2\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"title\": \"IETF Area Director\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                    "\"it\" : { " +
                        "\"titles/TITLE-1\": { \"@type\":\"Title\",\"title\": \"Ricercatore\" } " +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testTitles2 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testTitles3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359g\"," +
                "\"fullName\":\"test\"," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"title\": \"Research Scientist\"" +
                    "}," +
                    "\"TITLE-2\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"title\": \"IETF Area Director\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                    "\"it\" : { " +
                        "\"titles/TITLE-1\": { \"@type\":\"Title\",\"title\": \"Ricercatore\" }, " +
                        "\"titles/TITLE-2\": { \"@type\":\"Title\",\"title\": \"Direttore Area IETF\" } " +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testTitles3 - 1", jscard2, Card.toCard(jscard));
    }

}
