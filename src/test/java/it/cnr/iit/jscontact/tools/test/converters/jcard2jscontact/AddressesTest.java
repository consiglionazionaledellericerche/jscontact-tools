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
package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.ezvcard2jscontact.EZVCard2JSContact;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddressesTest extends JCard2JSContactTest {

    @Test
    public void testAddressesValid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid1 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid1 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid1 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid1 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid1 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid1 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid1 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid1 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid1 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());

    }

    @Test
    public void testAddressesValid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\", \"label\":\"54321 Oak St Reston USA\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid2 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid2 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid2 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid2 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid2 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid2 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid2 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid2 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid2 - 9", "54321 Oak St Reston USA", jsCard.getAddresses().get("ADR-1").getFullAddress());

    }

    @Test
    public void testAddressesValid3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"12345 Elm St\", \"Reston\", \"VA\", \"20190\", \"USA\"]]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid3 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid3 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddressesValid3 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid3 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid3 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid3 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid3 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid3 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid3 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid3 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddressesValid3 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddressesValid3 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddressesValid3 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddressesValid3 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddressesValid3 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testAddressesValid3 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFullAddress());

    }


    @Test
    public void testAddressesValid4() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"geo\", {}, \"uri\", \"geo:46.772673,-71.282945\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid4 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid4 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid4 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid4 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid4 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid4 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid4 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid4 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid4 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid4 - 10", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());

    }

    @Test
    public void testAddressesValid5() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"tz\", {}, \"utc-offset\", \"-05:00\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid5 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid5 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid5 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid5 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid5 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid5 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid5 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid5 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid5 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid5 - 10", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
    }


    @Test
    public void testAddressesValid6() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"tz\", {}, \"utc-offset\", \"+05:00\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid6 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid6 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid6 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid6 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid6 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid6 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid6 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid6 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid6 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid6 - 10", "Etc/GMT-5", jsCard.getAddresses().get("ADR-1").getTimeZone());
    }

    @Test
    public void testAddressesValid7() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"tz\", {}, \"utc-offset\", \"+00:00\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid7 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid7 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid7 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid7 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid7 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid7 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid7 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid7 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid7 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid7 - 10", "Etc/GMT", jsCard.getAddresses().get("ADR-1").getTimeZone());
    }

    @Test
    public void testAddressesValid8() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"tz\", {}, \"utc-offset\", \"+05:30\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid8 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid8 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid8 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid8 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid8 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid8 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid8 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid8 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid8 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid8 - 10", "tz1", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testAddressesValid8 - 11", 1, jsCard.getTimeZones().size());
        assertEquals("testAddressesValid8 - 12", "TimeZone", jsCard.getTimeZones().get("tz1").getType());
        assertEquals("testAddressesValid8 - 13", "TZ+0530", jsCard.getTimeZones().get("tz1").getTzId());
        assertNotNull("testAddressesValid8 - 14", jsCard.getTimeZones().get("tz1").getUpdated());
        assertEquals("testAddressesValid8 - 15", 1, jsCard.getTimeZones().get("tz1").getStandard().size());
        assertEquals("testAddressesValid8 - 16", "TimeZoneRule", jsCard.getTimeZones().get("tz1").getStandard().get(0).getType());
        assertEquals("testAddressesValid8 - 17", 0, jsCard.getTimeZones().get("tz1").getStandard().get(0).getStart().compareTo(DateUtils.toCalendar(EZVCard2JSContact.CUSTOM_TIME_ZONE_RULE_START)));
        assertEquals("testAddressesValid8 - 18", "+0530", jsCard.getTimeZones().get("tz1").getStandard().get(0).getOffsetFrom());
        assertEquals("testAddressesValid8 - 19", "+0530", jsCard.getTimeZones().get("tz1").getStandard().get(0).getOffsetTo());

    }

    @Test
    public void testAddressesValid9() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"tz\", {}, \"text\", \"America/New_York\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid9 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid9 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid9 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid9 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid9 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid9 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid9 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid9 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid9 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid9 - 10", "America/New_York", jsCard.getAddresses().get("ADR-1").getTimeZone());

    }

    @Test
    public void testAddressesValid10() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\", \"tz\": \"America/New_York\", \"geo\": \"geo:46.772673,-71.282945\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid10 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid10 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid10 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid10 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid10 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid10 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid10 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid10 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid10 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid10 - 10", "America/New_York", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testAddressesValid10 - 11", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());

    }

    @Test
    public void testAddressesValid11() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"adr\", {\"cc\": \"US\", \"altid\": \"1\"}, \"text\", [\"\", \"\", \"12345 Elm St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"tz\", {\"altid\": \"1\"}, \"text\", \"America/New_York\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid11 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid11 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddressesValid11 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid11 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid11 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid11 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid11 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid11 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid11 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid11 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddressesValid11 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddressesValid11 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddressesValid11 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddressesValid11 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddressesValid11 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testAddressesValid11 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFullAddress());
        assertEquals("testAddressesValid11 - 17", "America/New_York", jsCard.getAddresses().get("ADR-2").getTimeZone());

    }

    @Test
    public void testAddressesValid12() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\", \"tz\": \"-05:00\", \"geo\": \"geo:46.772673,-71.282945\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid12 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid12 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid12 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid12 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid12 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid12 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid12 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid12 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid12 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid12 - 10", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testAddressesValid12 - 11", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());

    }



    @Test
    public void testAddressesValid13() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"adr\", {\"cc\": \"US\",\"altid\": \"1\",\"language\": \"en\"}, \"text\", [\"\", \"\", \"12345 Elm St\", \"Reston\", \"VA\", \"20190\", \"USA\"]]," +
                "[\"adr\", {\"cc\": \"IT\",\"altid\": \"1\",\"language\": \"it\"}, \"text\", [\"\", \"\", \"Via Moruzzi,1\", \"Pisa\", \"\", \"56124\", \"Italy\"]]" +
                "]]";


        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid11 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid11 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddressesValid11 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid11 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid11 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid11 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid11 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid11 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid11 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid11 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddressesValid11 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddressesValid11 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddressesValid11 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddressesValid11 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddressesValid11 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testAddressesValid11 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFullAddress());
        assertNotNull("testAddressesValid11 - 17", jsCard.getLocalization("it", "addresses/ADR-2"));

    }


    @Test
    public void testAddressesValid14() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"adr\", {\"cc\": \"US\",\"altid\": \"1\",\"language\": \"en\"}, \"text\", [\"\", \"\", \"12345 Elm St\", \"Reston\", \"VA\", \"20190\", \"USA\"]]," +
                "[\"adr\", {\"cc\": \"IT\",\"altid\": \"1\",\"language\": \"it\"}, \"text\", [\"\", \"\", \"Via Moruzzi,1\", \"Pisa\", \"\", \"56124\", \"Italy\"]]" +
                "]]";

        JCard2JSContact jCard2JSContact = JCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("en").build()).build();
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid12 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid12 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddressesValid12 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid12 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid12 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid12 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid12 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid12 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid12 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid12 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddressesValid12 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddressesValid12 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddressesValid12 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddressesValid12 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddressesValid12 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testAddressesValid12 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFullAddress());
        assertNotNull("testAddressesValid12 - 17", jsCard.getLocalization("it", "addresses/ADR-2"));

    }


    @Test
    public void testAddressesValid15() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"adr\", {\"cc\": \"US\",\"altid\": \"1\",\"language\": \"en\"}, \"text\", [\"\", \"\", \"12345 Elm St\", \"Reston\", \"VA\", \"20190\", \"USA\"]]," +
                "[\"adr\", {\"cc\": \"IT\",\"altid\": \"1\",\"language\": \"it\"}, \"text\", [\"\", \"\", \"Via Moruzzi,1\", \"Pisa\", \"\", \"56124\", \"Italy\"]]" +
                "]]";

        JCard2JSContact jCard2JSContact = JCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("it").build()).build();
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testAddressesValid13 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid13 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddressesValid13 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddressesValid13 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddressesValid13 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddressesValid13 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddressesValid13 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddressesValid13 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddressesValid13 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddressesValid13 - 10", "IT", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddressesValid13 - 11", "Italy", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddressesValid13 - 12", "56124", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddressesValid13 - 13", "Pisa", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddressesValid13 - 15", "Via Moruzzi,1", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testAddressesValid13 - 16", "Via Moruzzi,1\nPisa\n56124\nItaly", jsCard.getAddresses().get("ADR-2").getFullAddress());
        assertNotNull("testAddressesValid13 - 17", jsCard.getLocalization("en", "addresses/ADR-2"));

    }

}
