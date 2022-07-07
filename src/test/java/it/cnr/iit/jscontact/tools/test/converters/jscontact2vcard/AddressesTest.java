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
import ezvcard.util.GeoUri;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class AddressesTest extends JSContact2VCardTest {

    @Test
    public void testAddressesValid1() throws IOException, CardException {

        String jscard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                        "\"fullName\":\"test\"," +
                        "\"addresses\":{" +
                            "\"ADR-1\": {" +
                                "\"@type\":\"Address\"," +
                                "\"fullAddress\":\"54321 Oak St Reston VA 20190 USA\"," +
                                "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                                "\"locality\":\"Reston\"," +
                                "\"region\":\"VA\"," +
                                "\"country\":\"USA\"," +
                                "\"postcode\":\"20190\"," +
                                "\"countryCode\":\"US\"" +
                            "}" +
                        "}" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid1 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid1 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid1 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid1 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid1 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid1 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid1 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid1 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St Reston VA 20190 USA"));
    }

    @Test
    public void testAddressesValid2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid2 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid2 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid2 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid2 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid2 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid2 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid2 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid2 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
    }

    @Test
    public void testAddressesValid3() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}," +
                    "\"ADR-2\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"12345 Elm St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid3 - 1",vcard.getAddresses().size() == 2);
        assertTrue("testAddressesValid3 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid3 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid3 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid3 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid3 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid3 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid3 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid3 - 9",vcard.getAddresses().get(1).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid3 - 10",vcard.getAddresses().get(1).getCountry().equals("USA"));
        assertTrue("testAddressesValid3 - 11",vcard.getAddresses().get(1).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid3 - 12",vcard.getAddresses().get(1).getLocality().equals("Reston"));
        assertTrue("testAddressesValid3 - 13",vcard.getAddresses().get(1).getRegion().equals("VA"));
        assertTrue("testAddressesValid3 - 14",vcard.getAddresses().get(1).getStreetAddress().equals("12345 Elm St"));
        assertTrue("testAddressesValid3 - 15", vcard.getAddresses().get(1).getLabel().equals("12345 Elm St\nReston\nVA\n20190\nUSA"));
    }

    @Test
    public void testAddressesValid4() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid4 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid4 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid4 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid4 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid4 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid4 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid4 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid4 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid4 - 8", vcard.getAddresses().get(0).getGeo().equals(GeoUri.parse("geo:46.772673,-71.282945")));
    }

    @Test
    public void testAddressesValid5() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"," +
                        "\"timeZone\":\"Etc/GMT+5\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid5 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid5 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid5 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid5 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid5 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid5 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid5 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid5 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid5 - 9", vcard.getAddresses().get(0).getGeo().equals(GeoUri.parse("geo:46.772673,-71.282945")));
        assertTrue("testAddressesValid5 - 10", vcard.getAddresses().get(0).getTimezone().equals("Etc/GMT+5"));
    }

    @Test
    public void testAddressesValid6() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"locale\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"fullAddress\":\"54321 Oak St Reston VA 20190 USA\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                   "\"it\":{" +
                       "\"addresses/ADR-1/fullAddress\":\"Via Moruzzi,1 Pisa 56124 Italia\"" +
                   "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid6 - 1",vcard.getAddresses().size() == 2);
        assertTrue("testAddressesValid6 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid6 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid6 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid6 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid6 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid6 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid6 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St Reston VA 20190 USA"));
        assertTrue("testAddressesValid6 - 9", vcard.getAddresses().get(0).getLanguage().equals("en"));
        assertTrue("testAddressesValid6 - 10", vcard.getAddresses().get(1).getLabel().equals("Via Moruzzi,1 Pisa 56124 Italia"));
        assertTrue("testAddressesValid6 - 11", vcard.getAddresses().get(1).getLanguage().equals("it"));
    }

    @Test
    public void testAddressesValid7() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"Oak St\"}, {\"type\":\"number\", \"value\":\"54321\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                        "}" +
                    "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid7 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid7 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid7 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid7 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid7 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid7 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid7 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("Oak St 54321"));
        assertTrue("testAddressesValid7 - 8", vcard.getAddresses().get(0).getLabel().equals("Oak St 54321\nReston\nVA\n20190\nUSA"));
    }

    @Test
    public void testAddressesValid8() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[" +
                                     "{\"type\":\"name\", \"value\":\"Oak St\"}," +
                                     "{\"type\":\"number\", \"value\":\"54321\"}," +
                                     "{\"type\":\"floor\", \"value\":\"5\"}," +
                                     "{\"type\":\"room\", \"value\":\"100\"}" +
                                    "]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid8 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid8 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid8 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid8 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid8 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid8 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid8 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("Oak St 54321"));
        assertTrue("testAddressesValid8 - 8",vcard.getAddresses().get(0).getExtendedAddress().equals("Floor: 5 Room: 100"));
        assertTrue("testAddressesValid8 - 9", vcard.getAddresses().get(0).getLabel().equals("Floor: 5 Room: 100\nOak St 54321\nReston\nVA\n20190\nUSA"));
    }


    @Test
    public void testAddressesValid9() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"," +
                        "\"timeZone\":\"tz1\"" +
                    "}" +
                "}," +
                "\"timeZones\": {" +
                    "\"tz1\": {" +
                        "\"@type\": \"TimeZone\"," +
                        "\"tzId\": \"TZ-0530\"," +
                        "\"updated\": \"2021-06-07T14:24:45Z\"," +
                        "\"standard\": [{" +
                            "\"@type\": \"TimeZoneRule\"," +
                            "\"offsetFrom\": \"-0530\"," +
                            "\"offsetTo\": \"-0530\"," +
                            "\"start\": \"1601-01-01T00:00:00\"" +
                        "}]" +
                    "}" +
                "}" +
        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid9 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid9 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid9 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid9 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid9 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid9 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid9 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid9 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid9 - 9", vcard.getAddresses().get(0).getGeo().equals(GeoUri.parse("geo:46.772673,-71.282945")));
        assertTrue("testAddressesValid9 - 10", vcard.getAddresses().get(0).getTimezone().equals("-0530"));
    }


    @Test
    public void testAddressesValid10() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"locale\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"type\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"it\":{" +
                        "\"addresses/ADR-1\":{" +
                            "\"street\":[{\"type\":\"name\", \"value\":\"Via Moruzzi,1\"}]," +
                            "\"locality\":\"Pisa\"," +
                            "\"country\":\"Italia\"," +
                            "\"postcode\":\"56124\"," +
                            "\"countryCode\":\"IT\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testAddressesValid10 - 1",vcard.getAddresses().size() == 2);
        assertTrue("testAddressesValid10 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid10 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid10 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid10 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid10 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid10 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid10 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid10 - 9", vcard.getAddresses().get(0).getLanguage().equals("en"));
        assertTrue("testAddressesValid10 - 10",vcard.getAddresses().get(1).getParameter("CC").equals("IT"));
        assertTrue("testAddressesValid10 - 11",vcard.getAddresses().get(1).getCountry().equals("Italia"));
        assertTrue("testAddressesValid10 - 12",vcard.getAddresses().get(1).getPostalCode().equals("56124"));
        assertTrue("testAddressesValid10 - 13",vcard.getAddresses().get(1).getLocality().equals("Pisa"));
        assertTrue("testAddressesValid10 - 14",vcard.getAddresses().get(1).getStreetAddress().equals("Via Moruzzi,1"));
        assertTrue("testAddressesValid10 - 15", vcard.getAddresses().get(1).getLabel().equals("Via Moruzzi,1\nPisa\n56124\nItalia"));
        assertTrue("testAddressesValid10 - 16", vcard.getAddresses().get(1).getLanguage().equals("it"));
    }

}
