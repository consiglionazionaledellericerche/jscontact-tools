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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.util.GeoUri;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedAddressScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedAddress;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AddressesTest extends JSContact2VCardTest {

    @Test
    public void testAddresses1() throws IOException, CardException {

        String jscard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                        "\"name\": { \"full\": \"test\"}," +
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
        assertEquals("testAddresses1 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses1 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses1 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses1 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses1 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses1 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses1 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses1 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses1 - 9", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses2 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses2 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses2 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses2 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses2 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses2 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses2 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses2 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses2 - 9", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses3() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}," +
                    "\"ADR-2\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"12345 Elm St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses3 - 1", 2, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses3 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses3 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses3 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses3 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses3 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses3 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses3 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses3 - 9", "US", vcard.getProperties(ExtendedAddress.class).get(1).getParameter("CC"));
        assertEquals("testAddresses3 - 10", "USA", vcard.getProperties(ExtendedAddress.class).get(1).getCountry());
        assertEquals("testAddresses3 - 11", "20190", vcard.getProperties(ExtendedAddress.class).get(1).getPostalCode());
        assertEquals("testAddresses3 - 12", "Reston", vcard.getProperties(ExtendedAddress.class).get(1).getLocality());
        assertEquals("testAddresses3 - 13", "VA", vcard.getProperties(ExtendedAddress.class).get(1).getRegion());
        assertEquals("testAddresses3 - 14", "12345 Elm St", vcard.getProperties(ExtendedAddress.class).get(1).getStreetAddress());
        assertEquals("testAddresses3 - 15", "12345 Elm St Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(1).getLabel());
        assertEquals("testAddresses3 - 16", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testAddresses3 - 17", "ADR-2", vcard.getProperties(ExtendedAddress.class).get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses4() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}" +
                        "]," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses4 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses4 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses4 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses4 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses4 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses4 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses4 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses4 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses4 - 9", vcard.getProperties(ExtendedAddress.class).get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddresses4 - 10", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses5() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}" +
                        "]," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"," +
                        "\"timeZone\":\"Etc/GMT+5\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses5 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses5 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses5 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses5 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses5 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses5 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses5 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses5 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses5 - 9", vcard.getProperties(ExtendedAddress.class).get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddresses5 - 10", "Etc/GMT+5", vcard.getProperties(ExtendedAddress.class).get(0).getTimezone());
        assertEquals("testAddresses5 - 11", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses6() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
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
        assertEquals("testAddresses6 - 1", 2, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses6 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses6 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses6 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses6 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses6 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses6 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses6 - 8", "54321 Oak St Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses6 - 9", "en", vcard.getProperties(ExtendedAddress.class).get(0).getLanguage());
        assertEquals("testAddresses6 - 10", "Via Moruzzi,1 Pisa 56124 Italia", vcard.getProperties(ExtendedAddress.class).get(1).getLabel());
        assertEquals("testAddresses6 - 11", "it", vcard.getProperties(ExtendedAddress.class).get(1).getLanguage());
        assertEquals("testAddresses6 - 12", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses7() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"Oak St\"}," +
                            "{\"kind\":\"number\",\"value\":\"54321\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                        "}" +
                    "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses7 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses7 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses7 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses7 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses7 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses7 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses7 - 7", "54321", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(0));
        assertEquals("testAddresses7 - 7", "Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(1));
        assertEquals("testAddresses7 - 8", "Oak St 54321 Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses7 - 9", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testAddresses8() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[" +
                                    "{\"kind\":\"floor\", \"value\":\"5\"}," +
                                    "{\"kind\":\"room\", \"value\":\"100\"}," +
                                     "{\"kind\":\"name\", \"value\":\"Oak St\"}," +
                                     "{\"kind\":\"number\", \"value\":\"54321\"}," +
                                     "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                                     "{\"kind\":\"region\",\"value\":\"VA\"}," +
                                     "{\"kind\":\"postcode\",\"value\":\"20190\"}," +
                                    "{\"kind\":\"country\",\"value\":\"USA\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses8 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses8 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses8 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses8 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses8 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses8 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses8 - 7", "54321", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(0));
        assertEquals("testAddresses8 - 7", "Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(1));
        assertEquals("testAddresses8 - 8", "100", vcard.getProperties(ExtendedAddress.class).get(0).getExtendedAddresses().get(0));
        assertEquals("testAddresses8 - 8", "5", vcard.getProperties(ExtendedAddress.class).get(0).getExtendedAddresses().get(1));
        assertEquals("testAddresses8 - 9", "5 100 Oak St 54321 Reston VA 20190 USA", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses8 - 10", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }


    @Test
    public void testAddresses9() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
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
                        "\"timeZone\":\"-0530\"" +
                    "}" +
                "}" +
        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses9 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses9 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses9 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses9 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses9 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses9 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses9 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses9 - 8", "54321 Oak St Reston VA USA 20190", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses9 - 9", vcard.getProperties(ExtendedAddress.class).get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));
        assertEquals("testAddresses9 - 10", "-0530", vcard.getProperties(ExtendedAddress.class).get(0).getTimezone());
        assertEquals("testAddresses9 - 11", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }


    @Test
    public void testAddresses10() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
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
                                "{\"kind\":\"postcode\",\"value\":\"56124\"}," +
                                "{\"kind\":\"country\",\"value\":\"Italia\"}" +
                            "]," +
                            "\"countryCode\":\"IT\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses10 - 1", 2, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses10 - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAddresses10 - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAddresses10 - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses10 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses10 - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses10 - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAddresses10 - 8", "54321 Oak St Reston VA USA 20190", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses10 - 9", "en", vcard.getProperties(ExtendedAddress.class).get(0).getLanguage());
        assertEquals("testAddresses10 - 10", "IT", vcard.getProperties(ExtendedAddress.class).get(1).getParameter("CC"));
        assertEquals("testAddresses10 - 11", "Italia", vcard.getProperties(ExtendedAddress.class).get(1).getCountry());
        assertEquals("testAddresses10 - 12", "56124", vcard.getProperties(ExtendedAddress.class).get(1).getPostalCode());
        assertEquals("testAddresses10 - 13", "Pisa", vcard.getProperties(ExtendedAddress.class).get(1).getLocality());
        assertEquals("testAddresses10 - 14", "Via Moruzzi,1", vcard.getProperties(ExtendedAddress.class).get(1).getStreetAddress());
        assertEquals("testAddresses10 - 15", "Via Moruzzi,1 Pisa 56124 Italia", vcard.getProperties(ExtendedAddress.class).get(1).getLabel());
        assertEquals("testAddresses10 - 16", "it", vcard.getProperties(ExtendedAddress.class).get(1).getLanguage());
        assertEquals("testAddresses10 - 17", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }


    @Test
    public void testAddresses11() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                            "\"components\":[ " +
                                "{\"kind\":\"number\",\"value\":\"54321\"}," +
                                "{\"kind\":\"separator\",\"value\":\" \"}," +
                                "{\"kind\":\"name\",\"value\":\"Oak St\"}," +
                                "{\"kind\":\"locality\",\"value\":\"Reston\"}" +
                            "]," +
                        "\"isOrdered\": true," +
                        "\"defaultSeparator\":\",\"" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses11 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses11 - 1", "54321 Oak St,Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses11 - 2", "en", vcard.getProperties(ExtendedAddress.class).get(0).getLanguage());
        assertEquals("testAddresses11 - 3", "s,\\,;10;s, ;11;3", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.JSCOMPS.getValue()));
        assertEquals("testAddresses11 - 4", "54321", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(0));
        assertEquals("testAddresses11 - 4", "Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(1));
        assertEquals("testAddresses11 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses11 - 6", "Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetName());
        assertEquals("testAddresses11 - 7", "54321", vcard.getProperties(ExtendedAddress.class).get(0).getStreetNumber());
    }


    @Test
    public void testAddresses12() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"addresses\":{" +
                    "\"k26\": {" +
                        "\"full\": \"2-7-2 Marunouchi, Chiyoda-ku, Tokyo 100-8994\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"block\",\"value\":\"2-7\"}," +
                            "{\"kind\":\"separator\",\"value\":\"-\"}," +
                            "{\"kind\":\"number\",\"value\":\"2\"}," +
                            "{\"kind\":\"separator\",\"value\":\" \"}," +
                            "{\"kind\":\"district\",\"value\":\"Marunouchi\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Chiyoda-ku\"}," +
                            "{\"kind\":\"region\",\"value\":\"Tokyo\"}," +
                            "{\"kind\":\"separator\",\"value\":\" \"}," +
                            "{\"kind\":\"postcode\",\"value\":\"100-8994\"}" +
                        "]," +
                        "\"isOrdered\": true," +
                        "\"defaultSeparator\":\", \"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"jp\":{" +
                        "\"addresses/k26\": {" +
                            "\"full\": \"\\u3012100-8994\\u6771\\u4eac\\u90fd\\u5343\\u4ee3\\u7530\\u533a\\u4e38\\u30ce\\u51852-7-2\"," +
                            "\"components\":[ " +
                                "{\"kind\":\"region\",\"value\":\"\\u6771\\u4eac\\u90fd\"}," +
                                "{\"kind\":\"locality\",\"value\":\"\\u5343\\u4ee3\\u7530\\u533a\"}," +
                                "{\"kind\":\"district\",\"value\":\"\\u4e38\\u30ce\\u5185\"}," +
                                "{\"kind\":\"block\",\"value\":\"2-7\"}," +
                                "{\"kind\":\"separator\",\"value\":\"-\"}," +
                                "{\"kind\":\"number\",\"value\":\"2\"}," +
                                "{\"kind\":\"postcode\",\"value\":\"\\u3012100-8994\"}" +
                            "]," +
                            "\"isOrdered\": true," +
                            "\"defaultSeparator\":\", \"" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses12 - 1", 2, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses12 - 2", "2-7-2 Marunouchi, Chiyoda-ku, Tokyo 100-8994", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses12 - 3", "s,\\, ;13;s,-;10;s, ;15;3;4;s, ;5", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.JSCOMPS.getValue()));
        assertEquals("testAddresses12 - 4", "2", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(0));
        assertEquals("testAddresses12 - 5", "2-7", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(1));
        assertEquals("testAddresses12 - 6", "Marunouchi", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(2));
        assertEquals("testAddresses12 - 7", "Chiyoda-ku", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses12 - 8", "Tokyo", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAddresses12 - 9", "100-8994", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAddresses12 - 10", "2", vcard.getProperties(ExtendedAddress.class).get(0).getStreetNumber());
        assertEquals("testAddresses12 - 11", "2-7", vcard.getProperties(ExtendedAddress.class).get(0).getBlock());
        assertEquals("testAddresses12 - 12", "Marunouchi", vcard.getProperties(ExtendedAddress.class).get(0).getDistrict());
        assertEquals("testAddresses12 - 13", "k26", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testAddresses12 - 14", "jp", vcard.getProperties(ExtendedAddress.class).get(1).getLanguage());
        assertEquals("testAddresses12 - 15", "〒100-8994東京都千代田区丸ノ内2-7-2", vcard.getProperties(ExtendedAddress.class).get(1).getLabel());
        assertEquals("testAddresses12 - 16", "s,\\, ;4;3;15;13;s,-;10;5", vcard.getProperties(ExtendedAddress.class).get(1).getParameter(VCardParamEnum.JSCOMPS.getValue()));
        assertEquals("testAddresses12 - 17", "2", vcard.getProperties(ExtendedAddress.class).get(1).getStreetAddresses().get(0));
        assertEquals("testAddresses12 - 18", "2-7", vcard.getProperties(ExtendedAddress.class).get(1).getStreetAddresses().get(1));
        assertEquals("testAddresses12 - 19", "丸ノ内", vcard.getProperties(ExtendedAddress.class).get(1).getStreetAddresses().get(2));
        assertEquals("testAddresses12 - 20", "千代田区", vcard.getProperties(ExtendedAddress.class).get(1).getLocality());
        assertEquals("testAddresses12 - 21", "東京都", vcard.getProperties(ExtendedAddress.class).get(1).getRegion());
        assertEquals("testAddresses12 - 22", "〒100-8994", vcard.getProperties(ExtendedAddress.class).get(1).getPostalCode());
        assertEquals("testAddresses12 - 23", "2", vcard.getProperties(ExtendedAddress.class).get(1).getStreetNumber());
        assertEquals("testAddresses12 - 24", "2-7", vcard.getProperties(ExtendedAddress.class).get(1).getBlock());
        assertEquals("testAddresses12 - 25", "丸ノ内", vcard.getProperties(ExtendedAddress.class).get(1).getDistrict());
        assertEquals("testAddresses12 - 26", "k26", vcard.getProperties(ExtendedAddress.class).get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));

    }

    @Test
    public void testAddresses13() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                "\"ADR-1\": {" +
                "\"@type\":\"Address\"," +
                "\"components\":[ " +
                "{\"kind\":\"number\",\"value\":\"54321\"}," +
                "{\"kind\":\"separator\",\"value\":\" \"}," +
                "{\"kind\":\"name\",\"value\":\"Oak St\"}," +
                "{\"kind\":\"locality\",\"value\":\"Reston\"}" +
                "]," +
                "\"isOrdered\": true," +
                "\"defaultSeparator\":\"\\n\"" +
                "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses13 - 1", 1, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAddresses13 - 1", "54321 Oak St\nReston", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAddresses13 - 2", "en", vcard.getProperties(ExtendedAddress.class).get(0).getLanguage());
        assertEquals("testAddresses13 - 3", "s,\n;10;s, ;11;3", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.JSCOMPS.getValue()));
        assertEquals("testAddresses13 - 4", "54321", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(0));
        assertEquals("testAddresses13 - 4", "Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddresses().get(1));
        assertEquals("testAddresses13 - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAddresses13 - 6", "Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetName());
        assertEquals("testAddresses13 - 7", "54321", vcard.getProperties(ExtendedAddress.class).get(0).getStreetNumber());
    }

}
