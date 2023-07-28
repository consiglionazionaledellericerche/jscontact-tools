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

public class NameTest extends RoundtripTest {

    @Test
    public void testName1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Public, Esq.\"," +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"kind\": \"title\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"kind\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"kind\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"kind\": \"given2\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"kind\": \"credential\" }" +
                    "] " +
                "}, " +
                "\"nicknames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"Nickname\",\"name\": \"Johnny\" }, " +
                    "\"NICK-2\" : {  \"@type\":\"Nickname\",\"name\": \"Joe\" } " +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName1 - 1", jscard2, Card.toJSCard(jscard));
    }


    @Test
    public void testName2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"language\": \"en\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Public, Esq.\"," +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"kind\": \"title\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"kind\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"kind\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"kind\": \"given2\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"kind\": \"credential\" }" +
                    "] " +
                "}, " +
                "\"nicknames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"Nickname\",\"name\": \"Johnny\" }, " +
                    "\"NICK-2\" : {  \"@type\":\"Nickname\",\"name\": \"Joe\" } " +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                            "\"nicknames/NICK-1\" : {  \"@type\":\"Nickname\",\"name\": \"Giovannino\" }, " +
                            "\"nicknames/NICK-2\" : {  \"@type\":\"Nickname\",\"name\": \"Giò\" } " +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName2 - 1", jscard2, Card.toJSCard(jscard));
    }


    @Test
    public void testFullName3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"language\": \"jp\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"正仁\", \"kind\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"大久保\", \"kind\": \"surname\" }" +
                    "] " +
                "}, " +
                "\"localizations\" : {" +
                    "\"en\": {" +
                        "\"name/components\":[ " +
                            "{ \"@type\":\"NameComponent\", \"value\":\"Masahito\", \"kind\": \"given\" }," +
                            "{ \"@type\":\"NameComponent\", \"value\":\"Okubo\", \"kind\": \"surname\" }" +
                        "]" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName3 - 1", jscard2, Card.toJSCard(jscard));
    }

}
