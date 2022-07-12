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

import static org.junit.Assert.assertEquals;

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
        assertEquals("testAddressesValid1 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddressesValid1 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid1 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid1 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid1 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid1 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid1 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid1 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid1 - 9", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid2 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddressesValid2 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid2 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid2 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid2 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid2 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid2 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid2 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid2 - 9", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid3 - 1", 2, vcard.getAddresses().size());
        assertEquals("testAddressesValid3 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid3 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid3 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid3 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid3 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid3 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid3 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid3 - 9", "US", vcard.getAddresses().get(1).getParameter("CC"));
        assertEquals("testAddressesValid3 - 10", "USA", vcard.getAddresses().get(1).getCountry());
        assertEquals("testAddressesValid3 - 11", "20190", vcard.getAddresses().get(1).getPostalCode());
        assertEquals("testAddressesValid3 - 12", "Reston", vcard.getAddresses().get(1).getLocality());
        assertEquals("testAddressesValid3 - 13", "VA", vcard.getAddresses().get(1).getRegion());
        assertEquals("testAddressesValid3 - 14", "12345 Elm St", vcard.getAddresses().get(1).getStreetAddress());
        assertEquals("testAddressesValid3 - 15", "12345 Elm St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(1).getLabel());
        assertEquals("testAddressesValid3 - 16", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testAddressesValid3 - 17", "ADR-2", vcard.getAddresses().get(1).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid4 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddressesValid4 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid4 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid4 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid4 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid4 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid4 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid4 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid4 - 9", vcard.getAddresses().get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddressesValid4 - 10", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid5 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddressesValid5 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid5 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid5 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid5 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid5 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid5 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid5 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid5 - 9", vcard.getAddresses().get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddressesValid5 - 10", "Etc/GMT+5", vcard.getAddresses().get(0).getTimezone());
        assertEquals("testAddressesValid5 - 11", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid6 - 1", 2, vcard.getAddresses().size());
        assertEquals("testAddressesValid6 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid6 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid6 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid6 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid6 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid6 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid6 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid6 - 9", "en", vcard.getAddresses().get(0).getLanguage());
        assertEquals("testAddressesValid6 - 10", "Via Moruzzi,1 Pisa 56124 Italia", vcard.getAddresses().get(1).getLabel());
        assertEquals("testAddressesValid6 - 11", "it", vcard.getAddresses().get(1).getLanguage());
        assertEquals("testAddressesValid3 - 12", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid7 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddressesValid7 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid7 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid7 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid7 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid7 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid7 - 7", "Oak St 54321", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid7 - 8", "Oak St 54321\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid8 - 9", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid8 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddressesValid8 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid8 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid8 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid8 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid8 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid8 - 7", "Oak St 54321", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid8 - 8", "Floor: 5 Room: 100", vcard.getAddresses().get(0).getExtendedAddress());
        assertEquals("testAddressesValid8 - 9", "Floor: 5 Room: 100\nOak St 54321\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid9 - 10", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid9 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddressesValid9 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid9 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid9 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid9 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid9 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid9 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid9 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid9 - 9", vcard.getAddresses().get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddressesValid9 - 10", "-0530", vcard.getAddresses().get(0).getTimezone());
        assertEquals("testAddressesValid9 - 11", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testAddressesValid10 - 1", 2, vcard.getAddresses().size());
        assertEquals("testAddressesValid10 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddressesValid10 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddressesValid10 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddressesValid10 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddressesValid10 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddressesValid10 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddressesValid10 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddressesValid10 - 9", "en", vcard.getAddresses().get(0).getLanguage());
        assertEquals("testAddressesValid10 - 10", "IT", vcard.getAddresses().get(1).getParameter("CC"));
        assertEquals("testAddressesValid10 - 11", "Italia", vcard.getAddresses().get(1).getCountry());
        assertEquals("testAddressesValid10 - 12", "56124", vcard.getAddresses().get(1).getPostalCode());
        assertEquals("testAddressesValid10 - 13", "Pisa", vcard.getAddresses().get(1).getLocality());
        assertEquals("testAddressesValid10 - 14", "Via Moruzzi,1", vcard.getAddresses().get(1).getStreetAddress());
        assertEquals("testAddressesValid10 - 15", "Via Moruzzi,1\nPisa\n56124\nItalia", vcard.getAddresses().get(1).getLabel());
        assertEquals("testAddressesValid10 - 16", "it", vcard.getAddresses().get(1).getLanguage());
        assertEquals("testAddressesValid10 - 17", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
    }

}
