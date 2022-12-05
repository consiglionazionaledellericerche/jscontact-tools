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

import static org.junit.Assert.*;

public class OrganizationsTest extends RoundtripTest {

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
                        "\"units\": [ " +
                            "{\"@type\":\"OrgUnit\", \"name\":\"North American Division\"}," +
                            "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\" }" +
                        "]" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"ABC, Spa.\"," +
                            "\"units\": [" +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Divisione Nord America\"}," +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\"}" +
                            "]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOrganizations1 - 1", jscard2, Card.toJSCard(jscard));
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
                        "\"units\": [ " +
                            "{\"@type\":\"OrgUnit\", \"name\":\"North American Division\"}," +
                            "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\" }" +
                        "]" +
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
                            "\"units\": [" +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Divisione Nord America\"}," +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\"}" +
                            "]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOrganizations2 - 1", jscard2, Card.toJSCard(jscard));
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
                        "\"units\": [ " +
                            "{\"@type\":\"OrgUnit\", \"name\":\"North American Division\"}," +
                            "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\" }" +
                        "]" +
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
                            "\"units\": [" +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Divisione Nord America\"}," +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\"}" +
                            "]" +
                        "}," +
                        "\"organizations/ORG-2\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"name\" :\"Università del Nord America\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOrganizations3 - 1", jscard2, Card.toJSCard(jscard));
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
                        "\"units\": [ " +
                            "{\"@type\":\"OrgUnit\", \"name\":\"North American Division\"}," +
                            "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\" }" +
                        "]" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"units\": [" +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Divisione Nord America\"}," +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\"}" +
                            "]" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOrganizations5 - 1", jscard2, Card.toJSCard(jscard));
    }

}
