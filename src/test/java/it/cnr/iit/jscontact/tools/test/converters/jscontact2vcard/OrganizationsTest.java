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
                        "\"units\":[ \"North American Division\", \"Marketing\" ]" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"/organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"ABC, Spa.\"," +
                            "\"units\":[ \"Divisione Nord America\", \"Marketing\" ]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOrganizations1 - 1",vcard.getOrganizations().size() == 2);
        assertTrue("testOrganizations1 - 2",vcard.getOrganizations().get(0).getValues().size() == 3);
        assertTrue("testOrganizations1 - 3",vcard.getOrganizations().get(0).getValues().get(0).equals("ABC, Inc."));
        assertTrue("testOrganizations1 - 4",vcard.getOrganizations().get(0).getValues().get(1).equals("North American Division"));
        assertTrue("testOrganizations1 - 5",vcard.getOrganizations().get(0).getValues().get(2).equals("Marketing"));
        assertTrue("testOrganizations1 - 6",vcard.getOrganizations().get(0).getLanguage() == null);
        assertTrue("testOrganizations1 - 7",vcard.getOrganizations().get(0).getAltId().equals("1"));
        assertTrue("testOrganizations1 - 8",vcard.getOrganizations().get(1).getValues().size() == 3);
        assertTrue("testOrganizations1 - 9",vcard.getOrganizations().get(1).getValues().get(0).equals("ABC, Spa."));
        assertTrue("testOrganizations1 - 10",vcard.getOrganizations().get(1).getValues().get(1).equals("Divisione Nord America"));
        assertTrue("testOrganizations1 - 11",vcard.getOrganizations().get(1).getValues().get(2).equals("Marketing"));
        assertTrue("testOrganizations1 - 12",vcard.getOrganizations().get(1).getLanguage().equals("it"));
        assertTrue("testOrganizations1 - 13",vcard.getOrganizations().get(1).getAltId().equals("1"));
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
                        "\"/organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"ABC, Spa.\"," +
                            "\"units\":[ \"Divisione Nord America\", \"Marketing\" ]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOrganizations2 - 1",vcard.getOrganizations().size() == 3);
        assertTrue("testOrganizations2 - 2",vcard.getOrganizations().get(0).getValues().size() == 3);
        assertTrue("testOrganizations2 - 3",vcard.getOrganizations().get(0).getValues().get(0).equals("ABC, Inc."));
        assertTrue("testOrganizations2 - 4",vcard.getOrganizations().get(0).getValues().get(1).equals("North American Division"));
        assertTrue("testOrganizations2 - 5",vcard.getOrganizations().get(0).getValues().get(2).equals("Marketing"));
        assertTrue("testOrganizations2 - 6",vcard.getOrganizations().get(0).getLanguage() == null);
        assertTrue("testOrganizations2 - 7",vcard.getOrganizations().get(0).getAltId().equals("1"));
        assertTrue("testOrganizations2 - 8",vcard.getOrganizations().get(1).getValues().size() == 3);
        assertTrue("testOrganizations2 - 9",vcard.getOrganizations().get(1).getValues().get(0).equals("ABC, Spa."));
        assertTrue("testOrganizations2 - 10",vcard.getOrganizations().get(1).getValues().get(1).equals("Divisione Nord America"));
        assertTrue("testOrganizations2 - 11",vcard.getOrganizations().get(1).getValues().get(2).equals("Marketing"));
        assertTrue("testOrganizations2 - 12",vcard.getOrganizations().get(1).getLanguage().equals("it"));
        assertTrue("testOrganizations2 - 13",vcard.getOrganizations().get(1).getAltId().equals("1"));
        assertTrue("testOrganizations2 - 14",vcard.getOrganizations().get(2).getValues().size() == 1);
        assertTrue("testOrganizations2 - 15",vcard.getOrganizations().get(2).getValues().get(0).equals("University of North America"));
        assertTrue("testOrganizations2 - 16",vcard.getOrganizations().get(2).getLanguage() == null);
        assertTrue("testOrganizations2 - 17",vcard.getOrganizations().get(2).getAltId() == null);
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
                        "\"/organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"ABC, Spa.\"," +
                            "\"units\":[ \"Divisione Nord America\", \"Marketing\" ]" +
                        "}," +
                        "\"/organizations/ORG-2\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"Università del Nord America\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOrganizations3 - 1",vcard.getOrganizations().size() == 4);
        assertTrue("testOrganizations3 - 2",vcard.getOrganizations().get(0).getValues().size() == 3);
        assertTrue("testOrganizations3 - 3",vcard.getOrganizations().get(0).getValues().get(0).equals("ABC, Inc."));
        assertTrue("testOrganizations3 - 4",vcard.getOrganizations().get(0).getValues().get(1).equals("North American Division"));
        assertTrue("testOrganizations3 - 5",vcard.getOrganizations().get(0).getValues().get(2).equals("Marketing"));
        assertTrue("testOrganizations3 - 6",vcard.getOrganizations().get(0).getLanguage() == null);
        assertTrue("testOrganizations3 - 7",vcard.getOrganizations().get(0).getAltId().equals("1"));
        assertTrue("testOrganizations3 - 8",vcard.getOrganizations().get(1).getValues().size() == 3);
        assertTrue("testOrganizations3 - 9",vcard.getOrganizations().get(1).getValues().get(0).equals("ABC, Spa."));
        assertTrue("testOrganizations3 - 10",vcard.getOrganizations().get(1).getValues().get(1).equals("Divisione Nord America"));
        assertTrue("testOrganizations3 - 11",vcard.getOrganizations().get(1).getValues().get(2).equals("Marketing"));
        assertTrue("testOrganizations3 - 12",vcard.getOrganizations().get(1).getLanguage().equals("it"));
        assertTrue("testOrganizations3 - 13",vcard.getOrganizations().get(1).getAltId().equals("1"));
        assertTrue("testOrganizations3 - 14",vcard.getOrganizations().get(2).getValues().size() == 1);
        assertTrue("testOrganizations3 - 15",vcard.getOrganizations().get(2).getValues().get(0).equals("University of North America"));
        assertTrue("testOrganizations3 - 16",vcard.getOrganizations().get(2).getLanguage() == null);
        assertTrue("testOrganizations3 - 17",vcard.getOrganizations().get(2).getAltId().equals("2"));
        assertTrue("testOrganizations3 - 18",vcard.getOrganizations().get(3).getValues().size() == 1);
        assertTrue("testOrganizations3 - 19",vcard.getOrganizations().get(3).getValues().get(0).equals("Università del Nord America"));
        assertTrue("testOrganizations3 - 20",vcard.getOrganizations().get(3).getLanguage().equals("it"));
        assertTrue("testOrganizations3 - 21",vcard.getOrganizations().get(3).getAltId().equals("2"));
    }

}
