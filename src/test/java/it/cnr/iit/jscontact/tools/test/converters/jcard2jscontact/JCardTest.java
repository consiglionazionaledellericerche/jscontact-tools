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

import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class JCardTest extends JCard2JSContactTest {

    //jCard is not an array
    @Test(expected = CardException.class)
    public void testJCardInvalid1() throws CardException {

        String jcard="{}";
        jCard2JSContact.convert(jcard);
    }

    //jCard is an empty array
    @Test(expected = CardException.class)
    public void testJCardInvalid2() throws CardException {

        String jcard="[]";
        jCard2JSContact.convert(jcard);
    }

    //jCard array includes only one item
    @Test(expected = RuntimeException.class)
    public void testJCardInvalid3() throws CardException {

        String jcard="[\"vcard\"]";
        jCard2JSContact.convert(jcard);
    }

        //jCard array 2nd item is empty
        @Test(expected = CardException.class)
        public void testJCardInvalid4() throws CardException {

            String jcard="[\"vcard\",[]]";
            jCard2JSContact.convert(jcard);
        }

    //jCard does not include fn
    @Test(expected = CardException.class)
    public void testJCardInvalid5() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }


    //version param object appears as null instead of empty object
    @Test(expected = RuntimeException.class)
    public void testJCardInvalid6() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", null, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }


    //jCard array 1st is not vcard
    @Test(expected = CardException.class)
    public void testJCardInvalid7() throws CardException {

        String jcard="[\"jcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }

    //a jCard property includes the value parameter
    @Test(expected = CardException.class)
    public void testJCardInvalid8() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", { \"value\": \"a value\" }, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }

    //pref must be between 1 and 100
    @Test(expected = CardException.class)
    public void testJCardInvalid9() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {\"pref\": 0}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }

    @Test
    public void testJCard() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testJCard - 1", jsCard);
        assertTrue("testJCard - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertEquals("testJCard - 3", "test", jsCard.getFullName());

    }

    @Test
    public void testExtendedJCard() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"], [\"myext\", {}, \"text\", \"extvalue\"]]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testExtendedJCard - 1", jsCard);
        assertTrue("testExtendedJCard - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertEquals("testExtendedJCard - 3", "test", jsCard.getFullName());
        assertEquals("testExtendedJCard - 4", 1, jsCard.getJCardExtensions().length);
        assertEquals("testExtendedJCard - 5", "myext", jsCard.getJCardExtensions()[0].getName());
        assertEquals("testExtendedJCard - 6", VCardDataType.TEXT, jsCard.getJCardExtensions()[0].getType());
        assertEquals("testExtendedJCard - 7", "extvalue", jsCard.getJCardExtensions()[0].getValue());
    }


    @Test
    public void testCompleteJCard1() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7483.json"), StandardCharsets.UTF_8);
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertEquals("testCompleteJCard1 - 1", "Joe User", jsCard.getFullName());
        assertTrue("testCompleteJCard1 - 2", jsCard.getKind().isIndividual());
        assertEquals("testCompleteJCard1 - 3", 4, jsCard.getName().getComponents().length);
        assertTrue("testCompleteJCard1 - 4", jsCard.getName().getComponents()[0].isPersonal());
        assertEquals("testCompleteJCard1 - 5", "Joe", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testCompleteJCard1 - 6", jsCard.getName().getComponents()[1].isSurname());
        assertEquals("testCompleteJCard1 - 7", "User", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testCompleteJCard1 - 8", jsCard.getName().getComponents()[2].isSuffix());
        assertEquals("testCompleteJCard1 - 9", "ing. jr", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testCompleteJCard1 - 10", jsCard.getName().getComponents()[3].isSuffix());
        assertEquals("testCompleteJCard1 - 11", "M.Sc.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testCompleteJCard1 - 12", 2, jsCard.getPreferredContactLanguages().size());
        assertEquals("testCompleteJCard1 - 13", 1, (int) jsCard.getPreferredContactLanguages().get("fr")[0].getPref());
        assertEquals("testCompleteJCard1 - 14", 2, (int) jsCard.getPreferredContactLanguages().get("en")[0].getPref());
        assertEquals("testCompleteJCard1 - 15", "Example", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteJCard1 - 16", "Research Scientist", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testCompleteJCard1 - 17", "Project Lead", jsCard.getTitles().get("TITLE-2").getTitle());
        assertEquals("testCompleteJCard1 - 18", 1, jsCard.getAddresses().size());
        assertEquals("testCompleteJCard1 - 19", "Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testCompleteJCard1 - 20", "Suite 1234", jsCard.getAddresses().get("ADR-1").getStreetExtensions());
        assertEquals("testCompleteJCard1 - 21", "4321 Rue Somewhere", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testCompleteJCard1 - 22", "Quebec", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteJCard1 - 23", "QC", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteJCard1 - 24", "Canada", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteJCard1 - 25", "G1V 2M2", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testCompleteJCard1 - 26", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());
        assertEquals("testCompleteJCard1 - 27", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testCompleteJCard1 - 29", 1, jsCard.getEmails().size());
        assertTrue("testCompleteJCard1 - 30", jsCard.getEmails().get("EMAIL-1").asWork());
        assertEquals("testCompleteJCard1 - 31", "joe.user@example.com", jsCard.getEmails().get("EMAIL-1").getEmail());
        assertEquals("testCompleteJCard1 - 32", 2, jsCard.getPhones().size());
        assertTrue("testCompleteJCard1 - 33", jsCard.getPhones().get("PHONE-1").asVoice());
        assertEquals("testCompleteJCard1 - 34", "tel:+1-555-555-1234;ext=102", jsCard.getPhones().get("PHONE-1").getPhone());
        assertEquals("testCompleteJCard1 - 35", 1, (int) jsCard.getPhones().get("PHONE-1").getPref());
        assertTrue("testCompleteJCard1 - 36", jsCard.getPhones().get("PHONE-1").asWork());
        assertNull("testCompleteJCard1 - 37", jsCard.getPhones().get("PHONE-1").getLabel());
        assertTrue("testCompleteJCard1 - 38", jsCard.getPhones().get("PHONE-2").asVoice());
        assertEquals("testCompleteJCard1 - 39", "tel:+1-555-555-4321", jsCard.getPhones().get("PHONE-2").getPhone());
        assertNull("testCompleteJCard1 - 40", jsCard.getPhones().get("PHONE-2").getPref());
        assertTrue("testCompleteJCard1 - 41", jsCard.getPhones().get("PHONE-2").asWork());
        assertTrue("testCompleteJCard1 - 42", jsCard.getPhones().get("PHONE-2").asCell());
        assertTrue("testCompleteJCard1 - 43", jsCard.getPhones().get("PHONE-2").asVideo());
        assertTrue("testCompleteJCard1 - 44", jsCard.getPhones().get("PHONE-2").asText());
        assertEquals("testCompleteJCard1 - 45", 1, jsCard.getCryptoKeys().size());
        assertEquals("testCompleteJCard1 - 46", "http://www.example.com/joe.user/joe.asc", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertNull("testCompleteJCard1 - 47", jsCard.getCryptoKeys().get("KEY-1").getPref());
        assertTrue("testCompleteJCard1 - 48", jsCard.getCryptoKeys().get("KEY-1").asWork());
        assertEquals("testCompleteJCard1 - 49", 1, jsCard.getLinks().size());
        assertEquals("testCompleteJCard1 - 50", "http://example.org", jsCard.getLinks().get("LINK-1").getUri());
        assertNull("testCompleteJCard1 - 51", jsCard.getLinks().get("LINK-1").getPref());
        assertTrue("testCompleteJCard1 - 52", jsCard.getLinks().get("LINK-1").asPrivate());
        assertTrue("testCompleteJCard1 - 54", StringUtils.isNotEmpty(jsCard.getUid()));

    }

    @Test
    public void testCompleteJCard2() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Multilingual.json"), StandardCharsets.UTF_8);
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertEquals("testCompleteJCard2 - 1", "大久保 正仁", jsCard.getFullName());
        assertEquals("testCompleteJCard2 - 3", "Okubo Masahito", jsCard.getLocalizations().get("en").get("fullName").asText());
        assertTrue("testCompleteJCard2 - 4", jsCard.getKind().isIndividual());
        assertEquals("testCompleteJCard2 - 5", 1, jsCard.getTitles().size());
        assertEquals("testCompleteJCard2 - 6", "事務局長", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testCompleteJCard2 - 8", "Secretary General", jsCard.getLocalization("en", "titles/TITLE-1").get("title").asText());
        assertTrue("testCompleteJCard2 - 9", jsCard.getKind().isIndividual());
        assertEquals("testCompleteJCard2 - 10", 2, jsCard.getPreferredContactLanguages().size());
        assertEquals("testCompleteJCard2 - 11", 1, (int) jsCard.getPreferredContactLanguages().get("jp")[0].getPref());
        assertEquals("testCompleteJCard2 - 12", 2, (int) jsCard.getPreferredContactLanguages().get("en")[0].getPref());
        assertTrue("testCompleteJCard2 - 13", StringUtils.isNotEmpty(jsCard.getUid()));
    }

    @Test
    public void testCompleteJCard3() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Unstructured.json"), StandardCharsets.UTF_8);
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertEquals("testCompleteJCard3 - 1", "台灣固網股份有限公司", jsCard.getFullName());
        assertEquals("testCompleteJCard3 - 3", "Taiwan Fixed Network CO.,LTD.", jsCard.getLocalizations().get("en").get("fullName").asText());
        assertTrue("testCompleteJCard3 - 4", jsCard.getKind().isOrg());
        assertEquals("testCompleteJCard3 - 5", 1, jsCard.getAddresses().size());
        assertEquals("testCompleteJCard3 - 6", "8F., No.172-1, Sec.2, Ji-Lung Rd,", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertNull("testCompleteJCard3 - 8", jsCard.getEmails());
        assertEquals("testCompleteJCard3 - 10", 2, jsCard.getPhones().size());
        assertTrue("testCompleteJCard3 - 11", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteJCard3 - 12", jsCard.getPhones().get("PHONE-1").hasNoContext());
        assertTrue("testCompleteJCard3 - 13", jsCard.getPhones().get("PHONE-1").getPhone().isEmpty());
        assertTrue("testCompleteJCard3 - 14", jsCard.getPhones().get("PHONE-2").asFax());
        assertTrue("testCompleteJCard3 - 15", jsCard.getPhones().get("PHONE-2").hasNoContext());
        assertTrue("testCompleteJCard3 - 16", jsCard.getPhones().get("PHONE-2").getPhone().isEmpty());
        assertTrue("testCompleteJCard3 - 17", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteJCard4() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7095.json"), StandardCharsets.UTF_8);
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertEquals("testCompleteJCard4 - 1", "Simon Perreault", jsCard.getFullName());
        assertNull("testCompleteJCard4 - 2", jsCard.getKind());
        assertEquals("testCompleteJCard4 - 3", 4, jsCard.getName().getComponents().length);
        assertTrue("testCompleteJCard4 - 4", jsCard.getName().getComponents()[0].isPersonal());
        assertEquals("testCompleteJCard4 - 5", "Simon", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testCompleteJCard4 - 6", jsCard.getName().getComponents()[1].isSurname());
        assertEquals("testCompleteJCard4 - 7", "Perreault", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testCompleteJCard4 - 8", jsCard.getName().getComponents()[2].isSuffix());
        assertEquals("testCompleteJCard4 - 9", "ing. jr", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testCompleteJCard4 - 10", jsCard.getName().getComponents()[3].isSuffix());
        assertEquals("testCompleteJCard4 - 11", "M.Sc.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testCompleteJCard4 - 12", 2, jsCard.getAnniversaries().size());
        assertTrue("testCompleteJCard4 - 13", jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertTrue("testCompleteJCard4 - 14", jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("0000-02-03"));
        assertTrue("testCompleteJCard4 - 15", jsCard.getAnniversaries().get("ANNIVERSARY-2").isOtherAnniversary());
        assertEquals("testCompleteJCard4 - 16", "marriage date", jsCard.getAnniversaries().get("ANNIVERSARY-2").getLabel());
        assertTrue("testCompleteJCard4 - 17", jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("2009-08-08T14:30:00-05:00"));
        assertEquals("testCompleteJCard4 - 18", 2, jsCard.getPreferredContactLanguages().size());
        assertEquals("testCompleteJCard4 - 19", 1, (int) jsCard.getPreferredContactLanguages().get("fr")[0].getPref());
        assertEquals("testCompleteJCard4 - 20", 2, (int) jsCard.getPreferredContactLanguages().get("en")[0].getPref());
        assertEquals("testCompleteJCard4 - 21", "Viagenie", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteJCard4 - 22", 1, jsCard.getAddresses().size());
        assertEquals("testCompleteJCard4 - 23", "Suite D2-630\n2875 Laurier\nQuebec\nQC\nG1V 2M2\nCanada", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testCompleteJCard4 - 24", "Suite D2-630", jsCard.getAddresses().get("ADR-1").getStreetExtensions());
        assertEquals("testCompleteJCard4 - 25", "2875 Laurier", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testCompleteJCard4 - 26", "Quebec", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteJCard4 - 27", "QC", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteJCard4 - 28", "Canada", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteJCard4 - 29", "G1V 2M2", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testCompleteJCard4 - 30", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());
        assertEquals("testCompleteJCard4 - 31", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testCompleteJCard4 - 32", 2, jsCard.getPhones().size());
        assertTrue("testCompleteJCard4 - 33", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteJCard4 - 34", jsCard.getPhones().get("PHONE-1").asWork());
        assertEquals("testCompleteJCard4 - 35", "tel:+1-418-656-9254;ext=102", jsCard.getPhones().get("PHONE-1").getPhone());
        assertEquals("testCompleteJCard4 - 36", 1, (int) jsCard.getPhones().get("PHONE-1").getPref());
        assertTrue("testCompleteJCard4 - 37", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteJCard4 - 38", jsCard.getPhones().get("PHONE-2").asWork());
        assertEquals("testCompleteJCard4 - 39", "tel:+1-418-262-6501", jsCard.getPhones().get("PHONE-2").getPhone());
        assertTrue("testCompleteJCard4 - 40", jsCard.getPhones().get("PHONE-2").asCell());
        assertTrue("testCompleteJCard4 - 41", jsCard.getPhones().get("PHONE-2").asVideo());
        assertTrue("testCompleteJCard4 - 42", jsCard.getPhones().get("PHONE-2").asText());
        assertNull("testCompleteJCard4 - 43", jsCard.getPhones().get("PHONE-2").getLabel());
        assertEquals("testCompleteJCard4 - 44", 1, jsCard.getEmails().size());
        assertTrue("testCompleteJCard4 - 45", jsCard.getEmails().get("EMAIL-1").asWork());
        assertEquals("testCompleteJCard4 - 46", "simon.perreault@viagenie.ca", jsCard.getEmails().get("EMAIL-1").getEmail());
        assertEquals("testCompleteJCard4 - 47", 1, jsCard.getCryptoKeys().size());
        assertTrue("testCompleteJCard4 - 48", jsCard.getCryptoKeys().get("KEY-1").asWork());
        assertEquals("testCompleteJCard4 - 49", "http://www.viagenie.ca/simon.perreault/simon.asc", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertEquals("testCompleteJCard4 - 50", 1, jsCard.getLinks().size());
        assertTrue("testCompleteJCard4 - 51", jsCard.getLinks().get("LINK-1").asPrivate());
        assertEquals("testCompleteJCard4 - 52", "http://nomis80.org", jsCard.getLinks().get("LINK-1").getUri());
        assertTrue("testCompleteJCard4 - 53", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteJCard5() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Wikipedia.json"), StandardCharsets.UTF_8);
        Card jsCard = (Card) jCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteJCard5 - 1", "Forrest Gump", jsCard.getFullName());
        assertNull("testCompleteJCard5 - 2", jsCard.getKind());
        assertEquals("testCompleteJCard5 - 3", 3, jsCard.getName().getComponents().length);
        assertTrue("testCompleteJCard5 - 4", jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testCompleteJCard5 - 5", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testCompleteJCard5 - 6", jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testCompleteJCard5 - 7", "Forrest", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testCompleteJCard5 - 8", jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testCompleteJCard5 - 9", "Gump", jsCard.getName().getComponents()[2].getValue());
        assertEquals("testCompleteJCard5 - 10", "Bubba Gump Shrimp Co.", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteJCard5 - 11", "Shrimp Man", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testCompleteJCard5 - 15", "http://www.example.com/dir_photos/my_photo.gif", jsCard.getMedia().get("PHOTO-1").getUri());
        assertEquals("testCompleteJCard5 - 16", "image/gif", jsCard.getMedia().get("PHOTO-1").getMediaType());
        assertEquals("testCompleteJCard5 - 17", 2, jsCard.getPhones().size());
        assertTrue("testCompleteJCard5 - 18", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteJCard5 - 19", jsCard.getPhones().get("PHONE-1").asWork());
        assertEquals("testCompleteJCard5 - 20", "tel:+1-111-555-1212", jsCard.getPhones().get("PHONE-1").getPhone());
        assertNull("testCompleteJCard5 - 21", jsCard.getPhones().get("PHONE-1").getLabel());
        assertTrue("testCompleteJCard5 - 22", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteJCard5 - 23", jsCard.getPhones().get("PHONE-2").asPrivate());
        assertEquals("testCompleteJCard5 - 24", "tel:+1-404-555-1212", jsCard.getPhones().get("PHONE-2").getPhone());
        assertNull("testCompleteJCard5 - 25", jsCard.getPhones().get("PHONE-2").getLabel());

        assertEquals("testCompleteJCard5 - 26", 2, jsCard.getAddresses().size());
        assertTrue("testCompleteJCard5 - 27", jsCard.getAddresses().get("ADR-1").asWork());
        assertEquals("testCompleteJCard5 - 28", 1, (int) jsCard.getAddresses().get("ADR-1").getPref());
        assertEquals("testCompleteJCard5 - 29", "100 Waters Edge\nBaytown, LA 30314\nUnited States of America", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testCompleteJCard5 - 30", "100 Waters Edge", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testCompleteJCard5 - 31", "Baytown", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteJCard5 - 32", "LA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteJCard5 - 33", "United States of America", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteJCard5 - 34", "30314", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertTrue("testCompleteJCard5 - 35", jsCard.getAddresses().get("ADR-2").asPrivate());
        assertEquals("testCompleteJCard5 - 36", "42 Plantation St.\nBaytown, LA 30314\nUnited States of America", jsCard.getAddresses().get("ADR-2").getFullAddress());
        assertEquals("testCompleteJCard5 - 37", "42 Plantation St.", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testCompleteJCard5 - 38", "Baytown", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testCompleteJCard5 - 39", "LA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testCompleteJCard5 - 40", "United States of America", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testCompleteJCard5 - 41", "30314", jsCard.getAddresses().get("ADR-2").getPostcode());

        assertEquals("testCompleteJCard5 - 42", 1, jsCard.getEmails().size());
        assertEquals("testCompleteJCard5 - 43", "forrestgump@example.com", jsCard.getEmails().get("EMAIL-1").getEmail());
        assertEquals("testCompleteJCard5 - 44", 0, jsCard.getUpdated().compareTo(DateUtils.toCalendar("2008-04-24T19:52:43Z")));
        assertTrue("testCompleteJCard5 - 45", StringUtils.isNotEmpty(jsCard.getUid()));
    }

}
