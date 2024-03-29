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

public class AddressesTest extends RoundtripTest {

    @Test
    public void testAddresses1() throws IOException, CardException {

        String jscard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                        "\"name\":{\"full\":\"test\"}," +
                        "\"addresses\":{" +
                            "\"ADR-1\": {" +
                                "\"@type\":\"Address\"," +
                                "\"full\":\"54321 Oak St Reston VA 20190 USA\"," +
                                "\"components\":[ " +
                                    "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                                    "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                                    "{\"kind\":\"region\",\"value\":\"VA\"}," +
                                    "{\"kind\":\"country\",\"value\":\"USA\"}," +
                                    "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                                "]," +
                                "\"countryCode\":\"US\"" +
                            "}" +
                        "}" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses1 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAddresses2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses2 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAddresses3() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}," +
                    "\"ADR-2\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"12345 Elm St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses3 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAddresses4() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses4 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAddresses5() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"," +
                        "\"timeZone\":\"Etc/GMT+5\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses5 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAddresses6() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"full\":\"54321 Oak St Reston VA 20190 USA\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                   "\"it\":{" +
                       "\"addresses/ADR-1/full\":\"Via Moruzzi,1 Pisa 56124 Italia\"" +
                   "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses6 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAddresses7() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"Oak St\"}," +
                            "{\"kind\":\"number\",\"value\":\"54321\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                        "}" +
                    "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses7 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAddresses8() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[" +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"Oak St\"}," +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"number\", \"value\":\"54321\"}," +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"floor\", \"value\":\"5\"}," +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"room\", \"value\":\"100\"}," +
                                    "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                                    "{\"kind\":\"region\",\"value\":\"VA\"}," +
                                    "{\"kind\":\"country\",\"value\":\"USA\"}," +
                                    "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses8 - 1", jscard2, Card.toJSCard(jscard));
    }


    @Test
    public void testAddresses9() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"," +
                        "\"timeZone\":\"tz1\"" +
                    "}" +
                "}" +
        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses9 - 1", jscard2, Card.toJSCard(jscard));
    }


    @Test
    public void testAddresses10() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"it\":{" +
                        "\"addresses/ADR-1\":{" +
                            "\"@type\":\"Address\"," +
                            "\"components\":[ " +
                                "{\"kind\":\"name\",\"value\":\"Via Moruzzi,1\"}," +
                                "{\"kind\":\"locality\",\"value\":\"Pisa\"}," +
                                "{\"kind\":\"country\",\"value\":\"Italia\"}," +
                                "{\"kind\":\"postcode\",\"value\":\"56124\"}" +
                            "]," +
                            "\"countryCode\":\"IT\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAddresses10 - 1", jscard2, Card.toJSCard(jscard));
    }

}
