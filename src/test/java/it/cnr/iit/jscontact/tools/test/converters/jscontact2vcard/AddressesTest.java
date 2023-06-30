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
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AddressesTest extends JSContact2VCardTest {

    @Test
    public void testAddresses1() throws IOException, CardException {

        String jscard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                        "\"fullName\":\"test\"," +
                        "\"addresses\":{" +
                            "\"ADR-1\": {" +
                                "\"@type\":\"Address\"," +
                                "\"fullAddress\":\"54321 Oak St Reston VA 20190 USA\"," +
                                "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
                                "\"locality\":\"Reston\"," +
                                "\"region\":\"VA\"," +
                                "\"country\":\"USA\"," +
                                "\"postcode\":\"20190\"," +
                                "\"countryCode\":\"US\"" +
                            "}" +
                        "}" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses1 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses1 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses1 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses1 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses1 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses1 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses1 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses1 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses1 - 9", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses2 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses2 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses2 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses2 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses2 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses2 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses2 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses2 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses2 - 9", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses3() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}," +
                    "\"ADR-2\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"12345 Elm St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses3 - 1", 2, vcard.getAddresses().size());
        assertEquals("testAddresses3 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses3 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses3 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses3 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses3 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses3 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses3 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses3 - 9", "US", vcard.getAddresses().get(1).getParameter("CC"));
        assertEquals("testAddresses3 - 10", "USA", vcard.getAddresses().get(1).getCountry());
        assertEquals("testAddresses3 - 11", "20190", vcard.getAddresses().get(1).getPostalCode());
        assertEquals("testAddresses3 - 12", "Reston", vcard.getAddresses().get(1).getLocality());
        assertEquals("testAddresses3 - 13", "VA", vcard.getAddresses().get(1).getRegion());
        assertEquals("testAddresses3 - 14", "12345 Elm St", vcard.getAddresses().get(1).getStreetAddress());
        assertEquals("testAddresses3 - 15", "12345 Elm St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(1).getLabel());
        assertEquals("testAddresses3 - 16", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testAddresses3 - 17", "ADR-2", vcard.getAddresses().get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses4() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
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
        assertEquals("testAddresses4 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses4 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses4 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses4 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses4 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses4 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses4 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses4 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses4 - 9", vcard.getAddresses().get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddresses4 - 10", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses5() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
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
        assertEquals("testAddresses5 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses5 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses5 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses5 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses5 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses5 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses5 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses5 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses5 - 9", vcard.getAddresses().get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddresses5 - 10", "Etc/GMT+5", vcard.getAddresses().get(0).getTimezone());
        assertEquals("testAddresses5 - 11", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses6() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"fullAddress\":\"54321 Oak St Reston VA 20190 USA\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
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
        assertEquals("testAddresses6 - 1", 2, vcard.getAddresses().size());
        assertEquals("testAddresses6 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses6 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses6 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses6 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses6 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses6 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses6 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses6 - 9", "en", vcard.getAddresses().get(0).getLanguage());
        assertEquals("testAddresses6 - 10", "Via Moruzzi,1 Pisa 56124 Italia", vcard.getAddresses().get(1).getLabel());
        assertEquals("testAddresses6 - 11", "it", vcard.getAddresses().get(1).getLanguage());
        assertEquals("testAddresses6 - 12", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses7() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"Oak St\"}, {\"@type\":\"AddressComponent\",\"kind\":\"number\", \"value\":\"54321\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"" +
                        "}" +
                    "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses7 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses7 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses7 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses7 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses7 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses7 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses7 - 7", "Oak St,54321", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses7 - 8", "Oak St,54321\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses7 - 9", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses8() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[" +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"Oak St\"}," +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"number\", \"value\":\"54321\"}," +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"floor\", \"value\":\"5\"}," +
                                     "{\"@type\":\"AddressComponent\",\"kind\":\"room\", \"value\":\"100\"}" +
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
        assertEquals("testAddresses8 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses8 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses8 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses8 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses8 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses8 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses8 - 7", "Oak St,54321", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses8 - 8", "5,100", vcard.getAddresses().get(0).getExtendedAddress());
        assertEquals("testAddresses8 - 9", "5,100\nOak St,54321\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses8 - 10", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }


    @Test
    public void testAddresses9() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"," +
                        "\"timeZone\":\"-0530\"" +
                    "}" +
                "}" +
        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses9 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses9 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses9 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses9 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses9 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses9 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses9 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses9 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses9 - 9", vcard.getAddresses().get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddresses9 - 10", "-0530", vcard.getAddresses().get(0).getTimezone());
        assertEquals("testAddresses9 - 11", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }


    @Test
    public void testAddresses10() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"54321 Oak St\"}]," +
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
                            "\"@type\":\"Address\"," +
                            "\"components\":[{\"@type\":\"AddressComponent\",\"kind\":\"name\", \"value\":\"Via Moruzzi,1\"}]," +
                            "\"locality\":\"Pisa\"," +
                            "\"country\":\"Italia\"," +
                            "\"postcode\":\"56124\"," +
                            "\"countryCode\":\"IT\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses10 - 1", 2, vcard.getAddresses().size());
        assertEquals("testAddresses10 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses10 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses10 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses10 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses10 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses10 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses10 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses10 - 9", "en", vcard.getAddresses().get(0).getLanguage());
        assertEquals("testAddresses10 - 10", "IT", vcard.getAddresses().get(1).getParameter("CC"));
        assertEquals("testAddresses10 - 11", "Italia", vcard.getAddresses().get(1).getCountry());
        assertEquals("testAddresses10 - 12", "56124", vcard.getAddresses().get(1).getPostalCode());
        assertEquals("testAddresses10 - 13", "Pisa", vcard.getAddresses().get(1).getLocality());
        assertEquals("testAddresses10 - 14", "Via Moruzzi,1", vcard.getAddresses().get(1).getStreetAddress());
        assertEquals("testAddresses10 - 15", "Via Moruzzi,1\nPisa\n56124\nItalia", vcard.getAddresses().get(1).getLabel());
        assertEquals("testAddresses10 - 16", "it", vcard.getAddresses().get(1).getLanguage());
        assertEquals("testAddresses10 - 17", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

}
