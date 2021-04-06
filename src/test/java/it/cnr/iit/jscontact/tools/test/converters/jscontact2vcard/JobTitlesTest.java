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

public class JobTitlesTest extends JSContact2VCardTest {

    @Test
    public void testTitles1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"jobTitles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"title\": {" +
                            "\"value\": \"Research Scientist\"," +
                            "\"localizations\": { \"it\":\"Ricercatore\" }" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testTitles1 - 1",vcard.getTitles().size() == 2);
        assertTrue("testTitles1 - 2",vcard.getTitles().get(0).getValue().equals("Research Scientist"));
        assertTrue("testTitles1 - 3",vcard.getTitles().get(0).getLanguage() == null);
        assertTrue("testTitles1 - 4",vcard.getTitles().get(0).getAltId().equals("1"));
        assertTrue("testTitles1 - 5",vcard.getTitles().get(1).getValue().equals("Ricercatore"));
        assertTrue("testTitles1 - 6",vcard.getTitles().get(1).getLanguage().equals("it"));
        assertTrue("testTitles1 - 7",vcard.getTitles().get(1).getAltId().equals("1"));
    }

    @Test
    public void testTitles2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"jobTitles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"title\": {" +
                            "\"value\": \"Research Scientist\"," +
                            "\"localizations\": { \"it\":\"Ricercatore\" }" +
                        "}" +
                    "}," +
                    "\"TITLE-2\" : {" +
                        "\"title\": {" +
                            "\"value\": \"IETF Area Director\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testTitles2 - 1",vcard.getTitles().size() == 3);
        assertTrue("testTitles2 - 2",vcard.getTitles().get(0).getValue().equals("Research Scientist"));
        assertTrue("testTitles2 - 3",vcard.getTitles().get(0).getLanguage() == null);
        assertTrue("testTitles2 - 4",vcard.getTitles().get(0).getAltId().equals("1"));
        assertTrue("testTitles2 - 5",vcard.getTitles().get(1).getValue().equals("Ricercatore"));
        assertTrue("testTitles2 - 6",vcard.getTitles().get(1).getLanguage().equals("it"));
        assertTrue("testTitles2 - 7",vcard.getTitles().get(1).getAltId().equals("1"));
        assertTrue("testTitles2 - 8",vcard.getTitles().get(2).getValue().equals("IETF Area Director"));
        assertTrue("testTitles2 - 9",vcard.getTitles().get(2).getLanguage() == null);
        assertTrue("testTitles2 - 10",vcard.getTitles().get(2).getAltId() == null);
    }

    @Test
    public void testTitles3() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"jobTitles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"title\": {" +
                            "\"value\": \"Research Scientist\"," +
                            "\"localizations\": { \"it\":\"Ricercatore\" }" +
                        "}" +
                    "}," +
                    "\"TITLE-2\" : {" +
                        "\"title\": {" +
                            "\"value\": \"IETF Area Director\"," +
                            "\"localizations\": { \"it\":\"Direttore Area IETF\" }" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testTitles3 - 1",vcard.getTitles().size() == 4);
        assertTrue("testTitles3 - 2",vcard.getTitles().get(0).getValue().equals("Research Scientist"));
        assertTrue("testTitles3 - 3",vcard.getTitles().get(0).getLanguage() == null);
        assertTrue("testTitles3 - 4",vcard.getTitles().get(0).getAltId().equals("1"));
        assertTrue("testTitles3 - 5",vcard.getTitles().get(1).getValue().equals("Ricercatore"));
        assertTrue("testTitles3 - 6",vcard.getTitles().get(1).getLanguage().equals("it"));
        assertTrue("testTitles3 - 7",vcard.getTitles().get(1).getAltId().equals("1"));
        assertTrue("testTitles3 - 8",vcard.getTitles().get(2).getValue().equals("IETF Area Director"));
        assertTrue("testTitles3 - 9",vcard.getTitles().get(2).getLanguage() == null);
        assertTrue("testTitles3 - 10",vcard.getTitles().get(2).getAltId().equals("2"));
        assertTrue("testTitles3 - 11",vcard.getTitles().get(3).getValue().equals("Direttore Area IETF"));
        assertTrue("testTitles3 - 12",vcard.getTitles().get(3).getLanguage().equals("it"));
        assertTrue("testTitles3 - 13",vcard.getTitles().get(3).getAltId().equals("2"));
    }

}
