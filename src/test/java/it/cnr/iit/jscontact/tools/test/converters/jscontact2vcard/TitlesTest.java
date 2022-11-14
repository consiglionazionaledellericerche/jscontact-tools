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

import static org.junit.Assert.*;

public class TitlesTest extends JSContact2VCardTest {

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
        assertEquals("testTitles1 - 1", 2, vcard.getTitles().size());
        assertEquals("testTitles1 - 2", "Research Scientist", vcard.getTitles().get(0).getValue());
        assertNull("testTitles1 - 3", vcard.getTitles().get(0).getLanguage());
        assertEquals("testTitles1 - 4", "1", vcard.getTitles().get(0).getAltId());
        assertEquals("testTitles1 - 5", "Ricercatore", vcard.getTitles().get(1).getValue());
        assertEquals("testTitles1 - 6", "it", vcard.getTitles().get(1).getLanguage());
        assertEquals("testTitles1 - 7", "1", vcard.getTitles().get(1).getAltId());
        assertEquals("testTitles1 - 8", "TITLE-1", vcard.getTitles().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testTitles1 - 9", "TITLE-1", vcard.getTitles().get(1).getParameter(PROP_ID_PARAM));
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
        assertEquals("testTitles2 - 1", 3, vcard.getTitles().size());
        assertEquals("testTitles2 - 2", "Research Scientist", vcard.getTitles().get(0).getValue());
        assertNull("testTitles2 - 3", vcard.getTitles().get(0).getLanguage());
        assertEquals("testTitles2 - 4", "1", vcard.getTitles().get(0).getAltId());
        assertEquals("testTitles2 - 5", "Ricercatore", vcard.getTitles().get(1).getValue());
        assertEquals("testTitles2 - 6", "it", vcard.getTitles().get(1).getLanguage());
        assertEquals("testTitles2 - 7", "1", vcard.getTitles().get(1).getAltId());
        assertEquals("testTitles2 - 8", "IETF Area Director", vcard.getTitles().get(2).getValue());
        assertNull("testTitles2 - 9", vcard.getTitles().get(2).getLanguage());
        assertNull("testTitles2 - 10", vcard.getTitles().get(2).getAltId());
        assertEquals("testTitles2 - 11", "TITLE-1", vcard.getTitles().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testTitles2 - 12", "TITLE-1", vcard.getTitles().get(1).getParameter(PROP_ID_PARAM));
        assertEquals("testTitles2 - 13", "TITLE-2", vcard.getTitles().get(2).getParameter(PROP_ID_PARAM));
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
        assertEquals("testTitles3 - 1", 4, vcard.getTitles().size());
        assertEquals("testTitles3 - 2", "Research Scientist", vcard.getTitles().get(0).getValue());
        assertNull("testTitles3 - 3", vcard.getTitles().get(0).getLanguage());
        assertEquals("testTitles3 - 4", "1", vcard.getTitles().get(0).getAltId());
        assertEquals("testTitles3 - 5", "Ricercatore", vcard.getTitles().get(1).getValue());
        assertEquals("testTitles3 - 6", "it", vcard.getTitles().get(1).getLanguage());
        assertEquals("testTitles3 - 7", "1", vcard.getTitles().get(1).getAltId());
        assertEquals("testTitles3 - 8", "IETF Area Director", vcard.getTitles().get(2).getValue());
        assertNull("testTitles3 - 9", vcard.getTitles().get(2).getLanguage());
        assertEquals("testTitles3 - 10", "2", vcard.getTitles().get(2).getAltId());
        assertEquals("testTitles3 - 11", "Direttore Area IETF", vcard.getTitles().get(3).getValue());
        assertEquals("testTitles3 - 12", "it", vcard.getTitles().get(3).getLanguage());
        assertEquals("testTitles3 - 13", "2", vcard.getTitles().get(3).getAltId());
        assertEquals("testTitles3 - 14", "TITLE-1", vcard.getTitles().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testTitles3 - 15", "TITLE-1", vcard.getTitles().get(1).getParameter(PROP_ID_PARAM));
        assertEquals("testTitles3 - 16", "TITLE-2", vcard.getTitles().get(2).getParameter(PROP_ID_PARAM));
        assertEquals("testTitles3 - 17", "TITLE-2", vcard.getTitles().get(3).getParameter(PROP_ID_PARAM));
    }


    @Test
    public void testTitlesAndRoles() throws IOException, CardException {

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
                        "\"type\":\"role\"," +
                        "\"title\": \"IETF Area Director\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                    "\"it\" : { " +
                        "\"titles/TITLE-1\": { \"@type\":\"Title\",\"title\": \"Ricercatore\" }, " +
                        "\"titles/TITLE-2\": { \"@type\":\"Title\",\"type\":\"role\",\"title\": \"Direttore Area IETF\" } " +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testTitlesAndRoles - 1", 2, vcard.getTitles().size());
        assertEquals("testTitlesAndRoles - 2", 2, vcard.getRoles().size());
        assertEquals("testTitlesAndRoles - 3", "Research Scientist", vcard.getTitles().get(0).getValue());
        assertNull("testTitlesAndRoles - 4", vcard.getTitles().get(0).getLanguage());
        assertEquals("testTitlesAndRoles - 5", "1", vcard.getTitles().get(0).getAltId());
        assertEquals("testTitlesAndRoles - 6", "Ricercatore", vcard.getTitles().get(1).getValue());
        assertEquals("testTitlesAndRoles - 7", "it", vcard.getTitles().get(1).getLanguage());
        assertEquals("testTitlesAndRoles - 8", "1", vcard.getTitles().get(1).getAltId());
        assertEquals("testTitlesAndRoles - 9", "IETF Area Director", vcard.getRoles().get(0).getValue());
        assertNull("testTitlesAndRoles - 10", vcard.getRoles().get(0).getLanguage());
        assertEquals("testTitlesAndRoles - 11", "1", vcard.getRoles().get(0).getAltId());
        assertEquals("testTitlesAndRoles - 12", "Direttore Area IETF", vcard.getRoles().get(1).getValue());
        assertEquals("testTitlesAndRoles - 13", "it", vcard.getRoles().get(1).getLanguage());
        assertEquals("testTitlesAndRoles - 14", "1", vcard.getRoles().get(1).getAltId());
        assertEquals("testTitlesAndRoles - 15", "TITLE-1", vcard.getTitles().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testTitlesAndRoles - 16", "TITLE-1", vcard.getTitles().get(1).getParameter(PROP_ID_PARAM));
        assertEquals("testTitlesAndRoles - 17", "TITLE-2", vcard.getRoles().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testTitlesAndRoles - 18", "TITLE-2", vcard.getRoles().get(1).getParameter(PROP_ID_PARAM));
    }

}
