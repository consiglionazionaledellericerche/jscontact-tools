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
package it.cnr.iit.jscontact.tools.test.converters.xcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.*;

public class XCardTest extends XCard2JSContactTest {

    @Test
    public void testCompleteXCard1() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("xcard/xCard-RFC6351.xml")), StandardCharsets.UTF_8);
        Card jsCard = xCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteXCard1 - 1", "Simon Perreault", jsCard.getName().getFull());
        assertNull("testCompleteXCard1 - 2", jsCard.getKind());
        assertEquals("testCompleteXCard1 - 3", 4, jsCard.getName().getComponents().length);
        assertTrue("testCompleteXCard1 - 4", jsCard.getName().getComponents()[0].isGiven());
        assertEquals("testCompleteXCard1 - 5", "Simon", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testCompleteXCard1 - 6", jsCard.getName().getComponents()[1].isSurname());
        assertEquals("testCompleteXCard1 - 7", "Perreault", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testCompleteXCard1 - 8", jsCard.getName().getComponents()[2].isCredential());
        assertEquals("testCompleteXCard1 - 9", "ing. jr", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testCompleteXCard1 - 10", jsCard.getName().getComponents()[3].isCredential());
        assertEquals("testCompleteXCard1 - 11", "M.Sc.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testCompleteXCard1 - 12", 2, jsCard.getAnniversaries().size());
        assertTrue("testCompleteXCard1 - 13", jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertTrue("testCompleteXCard1 - 14", jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("0000-02-03"));
        assertTrue("testCompleteXCard1 - 15", jsCard.getAnniversaries().get("ANNIVERSARY-2").isWedding());
        assertTrue("testCompleteXCard1 - 17", jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("2009-08-08T14:30:00-05:00"));
        assertEquals("testCompleteXCard1 - 18", 2, jsCard.getPreferredLanguages().size());
        assertEquals("testCompleteXCard1 - 19", 1, (int) jsCard.getPreferredLanguages().get("fr")[0].getPref());
        assertEquals("testCompleteXCard1 - 20", 2, (int) jsCard.getPreferredLanguages().get("en")[0].getPref());
        assertEquals("testCompleteXCard1 - 21", "Viagenie", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteXCard1 - 22", 1, jsCard.getAddresses().size());
        assertEquals("testCompleteXCard1 - 23", "Simon Perreault 2875 boul. Laurier, suite D2-630 Quebec, QC, Canada G1V 2M2", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testCompleteXCard1 - 25", "2875 boul. Laurier, suite D2-630", jsCard.getAddresses().get("ADR-1").getStreetAddress());
        assertEquals("testCompleteXCard1 - 26", "Quebec", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteXCard1 - 27", "QC", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteXCard1 - 28", "Canada", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteXCard1 - 29", "G1V 2M2", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testCompleteXCard1 - 30", "geo:46.766336,-71.28955", jsCard.getAddresses().get("ADR-1").getCoordinates());
        assertEquals("testCompleteXCard1 - 31", "America/Montreal", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testCompleteXCard1 - 32", 2, jsCard.getPhones().size());
        assertTrue("testCompleteXCard1 - 33", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteXCard1 - 34", jsCard.getPhones().get("PHONE-1").asWork());
        assertEquals("testCompleteXCard1 - 35", "tel:+1-418-656-9254;ext=102", jsCard.getPhones().get("PHONE-1").getNumber());
        assertNull("testCompleteXCard1 - 36", jsCard.getPhones().get("PHONE-1").getPref());
        assertTrue("testCompleteXCard1 - 37", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteXCard1 - 38", jsCard.getPhones().get("PHONE-2").asWork());
        assertEquals("testCompleteXCard1 - 39", "tel:+1-418-262-6501", jsCard.getPhones().get("PHONE-2").getNumber());
        assertTrue("testCompleteXCard1 - 40", jsCard.getPhones().get("PHONE-2").asMobile());
        assertTrue("testCompleteXCard1 - 41", jsCard.getPhones().get("PHONE-2").asVideo());
        assertTrue("testCompleteXCard1 - 42", jsCard.getPhones().get("PHONE-2").asText());
        assertNull("testCompleteXCard1 - 43", jsCard.getPhones().get("PHONE-2").getLabel());
        assertEquals("testCompleteXCard1 - 44", 1, jsCard.getEmails().size());
        assertTrue("testCompleteXCard1 - 45", jsCard.getEmails().get("EMAIL-1").asWork());
        assertEquals("testCompleteXCard1 - 46", "simon.perreault@viagenie.ca", jsCard.getEmails().get("EMAIL-1").getAddress());
        assertEquals("testCompleteXCard1 - 47", 1, jsCard.getCryptoKeys().size());
        assertTrue("testCompleteXCard1 - 48", jsCard.getCryptoKeys().get("KEY-1").asWork());
        assertEquals("testCompleteXCard1 - 49", "http://www.viagenie.ca/simon.perreault/simon.asc", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertEquals("testCompleteXCard1 - 50", 1, jsCard.getLinks().size());
        assertTrue("testCompleteXCard1 - 51", jsCard.getLinks().get("LINK-1").asPrivate());
        assertTrue("testCompleteXCard1 - 52", jsCard.getLinks().get("LINK-1").isGenericLink());
        assertEquals("testCompleteXCard1 - 53", "http://nomis80.org", jsCard.getLinks().get("LINK-1").getUri());
        assertTrue("testCompleteXCard1 - 54", StringUtils.isNotEmpty(jsCard.getUid()));

    }

    @Test
    public void testCompleteXCard2() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("xcard/xCard-Wikipedia.xml")), StandardCharsets.UTF_8);
        Card jsCard = xCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteXCard2 - 1", "Forrest Gump", jsCard.getName().getFull());
        assertNull("testCompleteXCard2 - 2", jsCard.getKind());
        assertEquals("testCompleteXCard2 - 3", 3, jsCard.getName().getComponents().length);
        assertTrue("testCompleteXCard2 - 4", jsCard.getName().getComponents()[0].isTitle());
        assertEquals("testCompleteXCard2 - 5", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testCompleteXCard2 - 6", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testCompleteXCard2 - 7", "Forrest", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testCompleteXCard2 - 8", jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testCompleteXCard2 - 9", "Gump", jsCard.getName().getComponents()[2].getValue());
        assertEquals("testCompleteXCard2 - 10", "Bubba Gump Shrimp Co.", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteXCard2 - 11", "Shrimp Man", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testCompleteXCard2 - 15", "http://www.example.com/dir_photos/my_photo.gif", jsCard.getMedia().get("PHOTO-1").getUri());
        assertEquals("testCompleteXCard2 - 16", "image/gif", jsCard.getMedia().get("PHOTO-1").getMediaType());
        assertEquals("testCompleteXCard2 - 17", 2, jsCard.getPhones().size());
        assertTrue("testCompleteXCard2 - 18", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteXCard2 - 19", jsCard.getPhones().get("PHONE-1").asWork());
        assertEquals("testCompleteXCard2 - 20", "tel:+1-111-555-1212", jsCard.getPhones().get("PHONE-1").getNumber());
        assertNull("testCompleteXCard2 - 21", jsCard.getPhones().get("PHONE-1").getLabel());
        assertTrue("testCompleteXCard2 - 22", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteXCard2 - 23", jsCard.getPhones().get("PHONE-2").asPrivate());
        assertEquals("testCompleteXCard2 - 24", "tel:+1-404-555-1212", jsCard.getPhones().get("PHONE-2").getNumber());
        assertNull("testCompleteXCard2 - 25", jsCard.getPhones().get("PHONE-2").getLabel());

        assertEquals("testCompleteXCard2 - 26", 2, jsCard.getAddresses().size());
        assertTrue("testCompleteXCard2 - 27", jsCard.getAddresses().get("ADR-1").asWork());
        assertEquals("testCompleteXCard2 - 28", 1, (int) jsCard.getAddresses().get("ADR-1").getPref());
//        assertTrue("testCompleteXCard2 - 29", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("100 Waters Edge\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertEquals("testCompleteXCard2 - 30", "100 Waters Edge", jsCard.getAddresses().get("ADR-1").getStreetAddress());
        assertEquals("testCompleteXCard2 - 31", "Baytown", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteXCard2 - 32", "LA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteXCard2 - 33", "United States of America", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteXCard2 - 34", "30314", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertTrue("testCompleteXCard2 - 35", jsCard.getAddresses().get("ADR-2").asPrivate());
//        assertTrue("testCompleteXCard2 - 36", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("42 Plantation St.\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertEquals("testCompleteXCard2 - 37", "42 Plantation St.", jsCard.getAddresses().get("ADR-2").getStreetAddress());
        assertEquals("testCompleteXCard2 - 38", "Baytown", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testCompleteXCard2 - 39", "LA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testCompleteXCard2 - 40", "United States of America", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testCompleteXCard2 - 41", "30314", jsCard.getAddresses().get("ADR-2").getPostcode());

        assertEquals("testCompleteXCard2 - 42", 1, jsCard.getEmails().size());
        assertEquals("testCompleteXCard2 - 43", "forrestgump@example.com", jsCard.getEmails().get("EMAIL-1").getAddress());
        assertEquals("testCompleteXCard2 - 44", 0, jsCard.getUpdated().compareTo(DateUtils.toCalendar("2008-04-24T19:52:43Z")));
        assertTrue("testCompleteXCard2 - 45", StringUtils.isNotEmpty(jsCard.getUid()));
    }

}
