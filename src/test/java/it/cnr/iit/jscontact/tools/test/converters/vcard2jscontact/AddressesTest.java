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
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.ezvcard2jscontact.EZVCard2JSContact;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddressesTest extends VCard2JSContactTest {

    @Test
    public void testAddresses1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses1 - 1", jsCard.getAddresses());
        assertEquals("testAddresses1 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses1 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses1 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses1 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses1 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses1 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses1 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses1 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());

    }

    @Test
    public void testAddresses2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;LABEL=54321 Oak St Reston USA:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses2 - 1", jsCard.getAddresses());
        assertEquals("testAddresses2 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses2 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses2 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses2 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses2 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses2 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses2 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses2 - 9", "54321 Oak St Reston USA", jsCard.getAddresses().get("ADR-1").getFull());

    }

    @Test
    public void testAddresses3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses3 - 1", jsCard.getAddresses());
        assertEquals("testAddresses3 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses3 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses3 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses3 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses3 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses3 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses3 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses3 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses3 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddresses3 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddresses3 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddresses3 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddresses3 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddresses3 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testAddresses3 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFull());

    }


    @Test
    public void testAddresses4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GROUP1.ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GROUP1.GEO:geo:46.772673,-71.282945\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses4 - 1", jsCard.getAddresses());
        assertEquals("testAddresses4 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses4 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses4 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses4 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses4 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses4 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses4 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses4 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses4 - 10", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());

    }

    @Test
    public void testAddresses5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GROUP1.ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GROUP1.GEO:geo:46.772673,-71.282945\n" +
                "GROUP1.TZ:-0500\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses5 - 1", jsCard.getAddresses());
        assertEquals("testAddresses5 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses5 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses5 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses5 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses5 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses5 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses5 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses5 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses5 - 10", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
    }

    @Test
    public void testAddresses6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GROUP1.ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GROUP1.GEO:geo:46.772673,-71.282945\n" +
                "GROUP1.TZ:America/New_York\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses6 - 1", jsCard.getAddresses());
        assertEquals("testAddresses6 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses6 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses6 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses6 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses6 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses6 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses6 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses6 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses6 - 10", "America/New_York", jsCard.getAddresses().get("ADR-1").getTimeZone());

    }

    @Test
    public void testAddresses7() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=America/New_York;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses7 - 1", jsCard.getAddresses());
        assertEquals("testAddresses7 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses7 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses7 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses7 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses7 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses7 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses7 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses7 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses7 - 10", "America/New_York", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testAddresses7 - 11", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());

    }


    @Test
    public void testAddresses8() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GROUP1.ADR;CC=US:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "GROUP1.TZ:America/New_York\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses8 - 1", jsCard.getAddresses());
        assertEquals("testAddresses8 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses8 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses8 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses8 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses8 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses8 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses8 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses8 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses8 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddresses8 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddresses8 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddresses8 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddresses8 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddresses8 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testAddresses8 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFull());
        assertEquals("testAddresses8 - 17", "America/New_York", jsCard.getAddresses().get("ADR-2").getTimeZone());

    }

    @Test
    public void testAddresses9() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=-0500;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses9 - 1", jsCard.getAddresses());
        assertEquals("testAddresses9 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses9 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses9 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses9 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses9 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses9 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses9 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses9 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses9 - 10", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testAddresses9 - 11", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());

    }

    @Test
    public void testAddresses10() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GROUP1.ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GROUP1.GEO:geo:46.772673,-71.282945\n" +
                "GROUP1.TZ:-0530\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses10 - 1", jsCard.getAddresses());
        assertEquals("testAddresses10 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses10 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses10 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses10 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses10 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses10 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses10 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses10 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses10 - 10", "tz1", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testAddresses10 - 10", "tz1", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testAddresses10 - 11", 1, jsCard.getCustomTimeZones().size());
        assertEquals("testAddresses10 - 13", "TZ-0530", jsCard.getCustomTimeZones().get("tz1").getTzId());
        assertNotNull("testAddresses10 - 14", jsCard.getCustomTimeZones().get("tz1").getUpdated());
        assertEquals("testAddresses10 - 15", 1, jsCard.getCustomTimeZones().get("tz1").getStandard().size());
        assertEquals("testAddresses10 - 17", 0, jsCard.getCustomTimeZones().get("tz1").getStandard().get(0).getStart().compareTo(DateUtils.toCalendar(EZVCard2JSContact.CUSTOM_TIME_ZONE_RULE_START)));
        assertEquals("testAddresses10 - 18", "-0530", jsCard.getCustomTimeZones().get("tz1").getStandard().get(0).getOffsetFrom());
        assertEquals("testAddresses10 - 19", "-0530", jsCard.getCustomTimeZones().get("tz1").getStandard().get(0).getOffsetTo());
    }


    @Test
    public void testAddresses11() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses11 - 1", jsCard.getAddresses());
        assertEquals("testAddresses11 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses11 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses11 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses11 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses11 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses11 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses11 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses11 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses11 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddresses11 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddresses11 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddresses11 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddresses11 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddresses11 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testAddresses11 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFull());
        assertNotNull("testAddresses11 - 17", jsCard.getLocalization("it", "addresses/ADR-2"));

    }


    @Test
    public void testAddresses12() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("en").build()).build();
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses12 - 1", jsCard.getAddresses());
        assertEquals("testAddresses12 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses12 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses12 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses12 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses12 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses12 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses12 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses12 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses12 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddresses12 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddresses12 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddresses12 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddresses12 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddresses12 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testAddresses12 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFull());
        assertNotNull("testAddresses12 - 17", jsCard.getLocalization("it", "addresses/ADR-2"));

    }


    @Test
    public void testAddresses13() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("it").build()).build();
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses13 - 1", jsCard.getAddresses());
        assertEquals("testAddresses13 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses13 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses13 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses13 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses13 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses13 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses13 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAddresses13 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAddresses13 - 10", "IT", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddresses13 - 11", "Italy", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddresses13 - 12", "56124", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddresses13 - 13", "Pisa", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddresses13 - 15", "Via Moruzzi,1", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testAddresses13 - 16", "Via Moruzzi,1\nPisa\n56124\nItaly", jsCard.getAddresses().get("ADR-2").getFull());
        assertNotNull("testAddresses13 - 17", jsCard.getLocalization("en", "addresses/ADR-2"));

    }


    @Test
    public void testAddresses14() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;JSCOMPS=\"s,\\,;11;s, ;10;3\";LANGUAGE=en;PROP-ID=ADR-1:;;54321 Oak St;Reston;;;;;;;54321;Oak St;;;;;;\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses14 - 1", jsCard.getAddresses());
        assertEquals("testAddresses14 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses14 - 3", Boolean.TRUE, jsCard.getAddresses().get("ADR-1").getIsOrdered());
        assertEquals("testAddresses14 - 4", DelimiterUtils.COMMA_ARRAY_DELIMITER, jsCard.getAddresses().get("ADR-1").getDefaultSeparator());
        assertEquals("testAddresses14 - 5", 4, jsCard.getAddresses().get("ADR-1").getComponents().length);
        assertTrue("testAddresses14 - 6", jsCard.getAddresses().get("ADR-1").getComponents()[2].isNumber());
        assertEquals("testAddresses14 - 7", "54321", jsCard.getAddresses().get("ADR-1").getComponents()[2].getValue());
        assertTrue("testAddresses14 - 8", jsCard.getAddresses().get("ADR-1").getComponents()[1].isSeparator());
        assertEquals("testAddresses14 - 9", DelimiterUtils.SPACE_DELIMITER, jsCard.getAddresses().get("ADR-1").getComponents()[1].getValue());
        assertTrue("testAddresses14 - 10", jsCard.getAddresses().get("ADR-1").getComponents()[0].isName());
        assertEquals("testAddresses14 - 11", "Oak St", jsCard.getAddresses().get("ADR-1").getComponents()[0].getValue());
        assertTrue("testAddresses14 - 12", jsCard.getAddresses().get("ADR-1").getComponents()[3].isLocality());
        assertEquals("testAddresses14 - 13", "Reston", jsCard.getAddresses().get("ADR-1").getComponents()[3].getValue());

    }


    @Test
    public void testAddresses15() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;JSCOMPS=\"s,\\, ;13;s,-;10;s, ;15;3;4;s, ;5\";LABEL=\"2-7-2 Marunouchi, Chiyoda-ku, Tokyo 100-8994\";PROP-ID=k26;ALTID=1:;;2,2-7,Marunouchi;Chiyoda-ku;Tokyo;100-8994;;;;;2;;;2-7;;Marunouchi;;\n" +
                "ADR;JSCOMPS=\"s,;4;3;15;13;s,-;10;5\";LABEL=〒100-8994東京都千代田区丸ノ内2-7-2;LANGUAGE=jp;PROP-ID=k26;ALTID=1:;;2,2-7,丸ノ内;千代田区;東京都;〒100-8994;;;;;2;;;2-7;;丸ノ内;;\n" +
                "ADR;JSCOMPS=\"s,\\,;11;s, ;10;3\";LANGUAGE=en;PROP-ID=ADR-1:;;54321 Oak St;Reston;;;;;;;54321;Oak St;;;;;;\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses15 - 1", jsCard.getAddresses());
        assertEquals("testAddresses15 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses15 - 3", Boolean.TRUE, jsCard.getAddresses().get("ADR-1").getIsOrdered());
        assertEquals("testAddresses15 - 4", DelimiterUtils.COMMA_ARRAY_DELIMITER, jsCard.getAddresses().get("ADR-1").getDefaultSeparator());
        assertEquals("testAddresses15 - 5", 4, jsCard.getAddresses().get("ADR-1").getComponents().length);
        assertTrue("testAddresses15 - 6", jsCard.getAddresses().get("ADR-1").getComponents()[0].isName());
        assertEquals("testAddresses15 - 7", "Oak St", jsCard.getAddresses().get("ADR-1").getComponents()[0].getValue());
        assertTrue("testAddresses15 - 8", jsCard.getAddresses().get("ADR-1").getComponents()[1].isSeparator());
        assertEquals("testAddresses15 - 9", DelimiterUtils.SPACE_DELIMITER, jsCard.getAddresses().get("ADR-1").getComponents()[1].getValue());
        assertTrue("testAddresses15 - 10", jsCard.getAddresses().get("ADR-1").getComponents()[2].isNumber());
        assertEquals("testAddresses15 - 11", "54321", jsCard.getAddresses().get("ADR-1").getComponents()[2].getValue());
        assertTrue("testAddresses15 - 12", jsCard.getAddresses().get("ADR-1").getComponents()[3].isLocality());
        assertEquals("testAddresses15 - 13", "Reston", jsCard.getAddresses().get("ADR-1").getComponents()[3].getValue());
        assertEquals("testAddresses15 - 14", Boolean.TRUE, jsCard.getAddresses().get("k26").getIsOrdered());
        assertEquals("testAddresses15 - 15", ", ", jsCard.getAddresses().get("k26").getDefaultSeparator());
        assertEquals("testAddresses15 - 16", "2-7-2 Marunouchi, Chiyoda-ku, Tokyo 100-8994", jsCard.getAddresses().get("k26").getFull());
        assertEquals("testAddresses15 - 17", 9, jsCard.getAddresses().get("k26").getComponents().length);
        assertTrue("testAddresses15 - 18", jsCard.getAddresses().get("k26").getComponents()[0].isBlock());
        assertEquals("testAddresses15 - 19", "2-7", jsCard.getAddresses().get("k26").getComponents()[0].getValue());
        assertTrue("testAddresses15 - 20", jsCard.getAddresses().get("k26").getComponents()[1].isSeparator());
        assertEquals("testAddresses15 - 21", "-", jsCard.getAddresses().get("k26").getComponents()[1].getValue());
        assertTrue("testAddresses15 - 22", jsCard.getAddresses().get("k26").getComponents()[2].isNumber());
        assertEquals("testAddresses15 - 23", "2", jsCard.getAddresses().get("k26").getComponents()[2].getValue());
        assertTrue("testAddresses15 - 24", jsCard.getAddresses().get("k26").getComponents()[3].isSeparator());
        assertEquals("testAddresses15 - 25", DelimiterUtils.SPACE_DELIMITER, jsCard.getAddresses().get("k26").getComponents()[3].getValue());
        assertTrue("testAddresses15 - 26", jsCard.getAddresses().get("k26").getComponents()[4].isDistrict());
        assertEquals("testAddresses15 - 27", "Marunouchi", jsCard.getAddresses().get("k26").getComponents()[4].getValue());
        assertTrue("testAddresses15 - 28", jsCard.getAddresses().get("k26").getComponents()[5].isLocality());
        assertEquals("testAddresses15 - 29", "Chiyoda-ku", jsCard.getAddresses().get("k26").getComponents()[5].getValue());
        assertTrue("testAddresses15 - 30", jsCard.getAddresses().get("k26").getComponents()[6].isRegion());
        assertEquals("testAddresses15 - 31", "Tokyo", jsCard.getAddresses().get("k26").getComponents()[6].getValue());
        assertTrue("testAddresses15 - 32", jsCard.getAddresses().get("k26").getComponents()[7].isSeparator());
        assertEquals("testAddresses15 - 33", DelimiterUtils.SPACE_DELIMITER, jsCard.getAddresses().get("k26").getComponents()[7].getValue());
        assertTrue("testAddresses15 - 34", jsCard.getAddresses().get("k26").getComponents()[8].isPostcode());
        assertEquals("testAddresses15 - 35", "100-8994", jsCard.getAddresses().get("k26").getComponents()[8].getValue());
        assertNotNull("testAddresses15 - 36",  jsCard.getLocalization("jp","addresses/k26"));
        Address addressLocalization = (Address) JsonNodeUtils.toObject(jsCard.getLocalization("jp","addresses/k26"), Address.class);
        assertEquals("testAddresses15 - 37", Boolean.TRUE, addressLocalization.getIsOrdered());
        assertEquals("testAddresses15 - 38", StringUtils.EMPTY, addressLocalization.getDefaultSeparator());
        assertEquals("testAddresses15 - 39", "〒100-8994東京都千代田区丸ノ内2-7-2", addressLocalization.getFull());
        assertEquals("testAddresses15 - 40", 7, addressLocalization.getComponents().length);
        assertTrue("testAddresses15 - 41", addressLocalization.getComponents()[0].isRegion());
        assertEquals("testAddresses15 - 42", "東京都", addressLocalization.getComponents()[0].getValue());
        assertTrue("testAddresses15 - 43", addressLocalization.getComponents()[1].isLocality());
        assertEquals("testAddresses15 - 44", "千代田区", addressLocalization.getComponents()[1].getValue());
        assertTrue("testAddresses15 - 45", addressLocalization.getComponents()[2].isDistrict());
        assertEquals("testAddresses15 - 46", "丸ノ内", addressLocalization.getComponents()[2].getValue());
        assertTrue("testAddresses15 - 47", addressLocalization.getComponents()[3].isBlock());
        assertEquals("testAddresses15 - 48", "2-7", addressLocalization.getComponents()[3].getValue());
        assertTrue("testAddresses15 - 49", addressLocalization.getComponents()[4].isSeparator());
        assertEquals("testAddresses15 - 50", "-", addressLocalization.getComponents()[4].getValue());
        assertTrue("testAddresses15 - 51", addressLocalization.getComponents()[5].isNumber());
        assertEquals("testAddresses15 - 52", "2", addressLocalization.getComponents()[5].getValue());
        assertTrue("testAddresses15 - 53", addressLocalization.getComponents()[6].isPostcode());
        assertEquals("testAddresses15 - 54", "〒100-8994", addressLocalization.getComponents()[6].getValue());

    }
}
