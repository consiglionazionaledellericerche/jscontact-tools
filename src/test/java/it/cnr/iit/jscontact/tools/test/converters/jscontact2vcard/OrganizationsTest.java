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
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class OrganizationsTest extends JSContact2VCardTest {

    @Test
    public void testOrganizations1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"organizations\": {" +
                    "\"ORG-1\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"name\": \"ABC, Inc.\", " +
                        "\"units\":[ \"North American Division\", \"Marketing\" ]," +
                        "\"sortAs\": [\"ABC\"]" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"ABC, Spa.\"," +
                            "\"units\":[ \"Divisione Nord America\", \"Marketing\" ]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOrganizations1 - 1", 2, vcard.getOrganizations().size());
        assertEquals("testOrganizations1 - 2", 3, vcard.getOrganizations().get(0).getValues().size());
        assertEquals("testOrganizations1 - 3", "ABC, Inc.", vcard.getOrganizations().get(0).getValues().get(0));
        assertEquals("testOrganizations1 - 4", "North American Division", vcard.getOrganizations().get(0).getValues().get(1));
        assertEquals("testOrganizations1 - 5", "Marketing", vcard.getOrganizations().get(0).getValues().get(2));
        assertNull("testOrganizations1 - 6", vcard.getOrganizations().get(0).getLanguage());
        assertEquals("testOrganizations1 - 7", "ABC", String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, vcard.getOrganizations().get(0).getSortAs()));
        assertEquals("testOrganizations1 - 8", "1", vcard.getOrganizations().get(0).getAltId());
        assertEquals("testOrganizations1 - 9", 3, vcard.getOrganizations().get(1).getValues().size());
        assertEquals("testOrganizations1 - 10", "ABC, Spa.", vcard.getOrganizations().get(1).getValues().get(0));
        assertEquals("testOrganizations1 - 11", "Divisione Nord America", vcard.getOrganizations().get(1).getValues().get(1));
        assertEquals("testOrganizations1 - 12", "Marketing", vcard.getOrganizations().get(1).getValues().get(2));
        assertEquals("testOrganizations1 - 13", "it", vcard.getOrganizations().get(1).getLanguage());
        assertEquals("testOrganizations1 - 14", "1", vcard.getOrganizations().get(1).getAltId());
        assertEquals("testOrganizations1 - 15", "ORG-1", vcard.getOrganizations().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testOrganizations1 - 16", "ORG-1", vcard.getOrganizations().get(1).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testOrganizations2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"organizations\":{ " +
                    "\"ORG-1\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"name\":\"ABC, Inc.\"," +
                        "\"units\": [ \"North American Division\",\"Marketing\" ]" +
                    "}," +
                    "\"ORG-2\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"name\": \"University of North America\"" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"ABC, Spa.\"," +
                            "\"units\":[ \"Divisione Nord America\", \"Marketing\" ]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOrganizations2 - 1", 3, vcard.getOrganizations().size());
        assertEquals("testOrganizations2 - 2", 3, vcard.getOrganizations().get(0).getValues().size());
        assertEquals("testOrganizations2 - 3", "ABC, Inc.", vcard.getOrganizations().get(0).getValues().get(0));
        assertEquals("testOrganizations2 - 4", "North American Division", vcard.getOrganizations().get(0).getValues().get(1));
        assertEquals("testOrganizations2 - 5", "Marketing", vcard.getOrganizations().get(0).getValues().get(2));
        assertNull("testOrganizations2 - 6", vcard.getOrganizations().get(0).getLanguage());
        assertEquals("testOrganizations2 - 7", "1", vcard.getOrganizations().get(0).getAltId());
        assertEquals("testOrganizations2 - 8", 3, vcard.getOrganizations().get(1).getValues().size());
        assertEquals("testOrganizations2 - 9", "ABC, Spa.", vcard.getOrganizations().get(1).getValues().get(0));
        assertEquals("testOrganizations2 - 10", "Divisione Nord America", vcard.getOrganizations().get(1).getValues().get(1));
        assertEquals("testOrganizations2 - 11", "Marketing", vcard.getOrganizations().get(1).getValues().get(2));
        assertEquals("testOrganizations2 - 12", "it", vcard.getOrganizations().get(1).getLanguage());
        assertEquals("testOrganizations2 - 13", "1", vcard.getOrganizations().get(1).getAltId());
        assertEquals("testOrganizations2 - 14", 1, vcard.getOrganizations().get(2).getValues().size());
        assertEquals("testOrganizations2 - 15", "University of North America", vcard.getOrganizations().get(2).getValues().get(0));
        assertNull("testOrganizations2 - 16", vcard.getOrganizations().get(2).getLanguage());
        assertNull("testOrganizations2 - 17", vcard.getOrganizations().get(2).getAltId());
        assertEquals("testOrganizations2 - 18", "ORG-1", vcard.getOrganizations().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testOrganizations2 - 19", "ORG-1", vcard.getOrganizations().get(1).getParameter(PROP_ID_PARAM));
        assertEquals("testOrganizations2 - 20", "ORG-2", vcard.getOrganizations().get(2).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testOrganizations3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"organizations\": {" +
                    "\"ORG-1\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"name\":\"ABC, Inc.\"," +
                        "\"units\": [ \"North American Division\", \"Marketing\" ]" +
                    "}," +
                    "\"ORG-2\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"name\": \"University of North America\"" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"ABC, Spa.\"," +
                            "\"units\":[ \"Divisione Nord America\", \"Marketing\" ]" +
                        "}," +
                        "\"organizations/ORG-2\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"Università del Nord America\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOrganizations3 - 1", 4, vcard.getOrganizations().size());
        assertEquals("testOrganizations3 - 2", 3, vcard.getOrganizations().get(0).getValues().size());
        assertEquals("testOrganizations3 - 3", "ABC, Inc.", vcard.getOrganizations().get(0).getValues().get(0));
        assertEquals("testOrganizations3 - 4", "North American Division", vcard.getOrganizations().get(0).getValues().get(1));
        assertEquals("testOrganizations3 - 5", "Marketing", vcard.getOrganizations().get(0).getValues().get(2));
        assertNull("testOrganizations3 - 6", vcard.getOrganizations().get(0).getLanguage());
        assertEquals("testOrganizations3 - 7", "1", vcard.getOrganizations().get(0).getAltId());
        assertEquals("testOrganizations3 - 8", 3, vcard.getOrganizations().get(1).getValues().size());
        assertEquals("testOrganizations3 - 9", "ABC, Spa.", vcard.getOrganizations().get(1).getValues().get(0));
        assertEquals("testOrganizations3 - 10", "Divisione Nord America", vcard.getOrganizations().get(1).getValues().get(1));
        assertEquals("testOrganizations3 - 11", "Marketing", vcard.getOrganizations().get(1).getValues().get(2));
        assertEquals("testOrganizations3 - 12", "it", vcard.getOrganizations().get(1).getLanguage());
        assertEquals("testOrganizations3 - 13", "1", vcard.getOrganizations().get(1).getAltId());
        assertEquals("testOrganizations3 - 14", 1, vcard.getOrganizations().get(2).getValues().size());
        assertEquals("testOrganizations3 - 15", "University of North America", vcard.getOrganizations().get(2).getValues().get(0));
        assertNull("testOrganizations3 - 16", vcard.getOrganizations().get(2).getLanguage());
        assertEquals("testOrganizations3 - 17", "2", vcard.getOrganizations().get(2).getAltId());
        assertEquals("testOrganizations3 - 18", 1, vcard.getOrganizations().get(3).getValues().size());
        assertEquals("testOrganizations3 - 19", "Università del Nord America", vcard.getOrganizations().get(3).getValues().get(0));
        assertEquals("testOrganizations3 - 20", "it", vcard.getOrganizations().get(3).getLanguage());
        assertEquals("testOrganizations3 - 21", "2", vcard.getOrganizations().get(3).getAltId());
        assertEquals("testOrganizations3 - 22", "ORG-1", vcard.getOrganizations().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testOrganizations3 - 23", "ORG-1", vcard.getOrganizations().get(1).getParameter(PROP_ID_PARAM));
        assertEquals("testOrganizations3 - 24", "ORG-2", vcard.getOrganizations().get(2).getParameter(PROP_ID_PARAM));
        assertEquals("testOrganizations3 - 25", "ORG-2", vcard.getOrganizations().get(3).getParameter(PROP_ID_PARAM));
    }


    @Test
    public void testOrganizations5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"organizations\": {" +
                    "\"ORG-1\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"units\":[ \"North American Division\", \"Marketing\" ]" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"units\":[ \"Divisione Nord America\", \"Marketing\" ]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOrganizations5 - 1", 2, vcard.getOrganizations().size());
        assertEquals("testOrganizations5 - 2", 3, vcard.getOrganizations().get(0).getValues().size());
        assertTrue("testOrganizations5 - 3",  vcard.getOrganizations().get(0).getValues().get(0).isEmpty());
        assertEquals("testOrganizations5 - 4", "North American Division", vcard.getOrganizations().get(0).getValues().get(1));
        assertEquals("testOrganizations5 - 5", "Marketing", vcard.getOrganizations().get(0).getValues().get(2));
        assertNull("testOrganizations5 - 6", vcard.getOrganizations().get(0).getLanguage());
        assertEquals("testOrganizations5 - 7", "1", vcard.getOrganizations().get(0).getAltId());
        assertEquals("testOrganizations5 - 8", 3, vcard.getOrganizations().get(1).getValues().size());
        assertTrue("testOrganizations5 - 9", vcard.getOrganizations().get(1).getValues().get(0).isEmpty());
        assertEquals("testOrganizations5 - 10", "Divisione Nord America", vcard.getOrganizations().get(1).getValues().get(1));
        assertEquals("testOrganizations5 - 11", "Marketing", vcard.getOrganizations().get(1).getValues().get(2));
        assertEquals("testOrganizations5 - 12", "it", vcard.getOrganizations().get(1).getLanguage());
        assertEquals("testOrganizations5 - 13", "1", vcard.getOrganizations().get(1).getAltId());
        assertEquals("testOrganizations5 - 14", "ORG-1", vcard.getOrganizations().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testOrganizations5 - 15", "ORG-1", vcard.getOrganizations().get(1).getParameter(PROP_ID_PARAM));
    }

}
