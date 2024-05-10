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

public class VCardTest extends VCard2JSContactTest {

    //vCard does not include fn
    @Test(expected = CardException.class)
    public void testVCardInvalid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:test\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard);
    }

    //PREF must be between 1 and 100
    @Test(expected = CardException.class)
    public void testVCardInvalid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:test\n" +
                "FN;PREF=0:test\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard);
    }


    @Test
    public void testVCard() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testVCard - 1", jsCard);
        assertTrue("testVCard - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertEquals("testVCard - 3", "test", jsCard.getName().getFull());

    }

    @Test
    public void testExtendedVCard1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "myext:extvalue\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testExtendedVCard1 - 1", jsCard);
        assertTrue("testExtendedVCard1 - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertEquals("testExtendedVCard1 - 3", "test", jsCard.getName().getFull());
        assertEquals("testExtendedJCard1 - 4", 2, jsCard.getVCardProps().length); //including VERSION
        assertEquals("testExtendedJCard1 - 5", "myext", jsCard.getVCardProps()[1].getName().toString());
        assertNull("testExtendedJCard1 - 6", jsCard.getVCardProps()[1].getType());
        assertEquals("testExtendedJCard1 - 7", "extvalue", jsCard.getVCardProps()[1].getValue());
    }


    @Test
    public void testExtendedVCard2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "UID:0916d686-1c35-485d-8ccf-4dd3b510605b\n" +
                "FN;DERIVED=true:0916d686-1c35-485d-8ccf-4dd3b510605b\n" +
                "item2.X-FOO;PREF=1:bar\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testExtendedVCard2 - 1", jsCard);
        assertEquals("testExtendedJCard2 - 2", 2, jsCard.getVCardProps().length); //including VERSION
        assertEquals("testExtendedJCard2 - 3", "x-foo", jsCard.getVCardProps()[1].getName().toString());
        assertNull("testExtendedJCard2 - 4", jsCard.getVCardProps()[1].getType());
        assertEquals("testExtendedJCard2 - 5", 2, jsCard.getVCardProps()[1].getParameters().size());
        assertEquals("testExtendedJCard2 - 6", 1, jsCard.getVCardProps()[1].getParameters().get(VCardParamEnum.PREF.getValue().toLowerCase()));
        assertEquals("testExtendedJCard2 - 7", "item2", jsCard.getVCardProps()[1].getParameters().get(VCardParamEnum.GROUP.getValue().toLowerCase()));
        assertEquals("testExtendedJCard2 - 8", "bar", jsCard.getVCardProps()[1].getValue().toString());
    }

    @Test
    public void testCompleteVCard1() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-RFC7483.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteVCard1 - 1", "Joe User", jsCard.getName().getFull());
        assertTrue("testCompleteVCard1 - 2", jsCard.getKind().isIndividual());
        assertEquals("testCompleteVCard1 - 3", 4, jsCard.getName().getComponents().length);
        assertTrue("testCompleteVCard1 - 4", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testCompleteVCard1 - 5", "Joe", jsCard.getName().getGiven());
        assertTrue("testCompleteVCard1 - 6", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testCompleteVCard1 - 7", "User", jsCard.getName().getSurname());
        assertTrue("testCompleteVCard1 - 8", jsCard.getName().getComponents()[2].isCredential());
        assertEquals("testCompleteVCard1 - 9", "ing. jr", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testCompleteVCard1 - 10", jsCard.getName().getComponents()[3].isCredential());
        assertEquals("testCompleteVCard1 - 11", "M.Sc.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testCompleteVCard1 - 12", 2, jsCard.getPreferredLanguages().size());
        assertEquals("testCompleteVCard1 - 13", 1, (int) jsCard.getPreferredLanguages().get("LANG-1").getPref());
        assertEquals("testCompleteVCard1 - 14", 2, (int) jsCard.getPreferredLanguages().get("LANG-2").getPref());
        assertEquals("testCompleteVCard1 - 13", "fr", jsCard.getPreferredLanguages().get("LANG-1").getLanguage());
        assertEquals("testCompleteVCard1 - 14", "en", jsCard.getPreferredLanguages().get("LANG-2").getLanguage());
        assertEquals("testCompleteVCard1 - 15", "Example", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteVCard1 - 16", "Research Scientist", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testCompleteVCard1 - 17", "Project Lead", jsCard.getTitles().get("TITLE-2").getName());
        assertEquals("testCompleteVCard1 - 18", 2, jsCard.getAddresses().size());
        assertEquals("testCompleteVCard1 - 19", "Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testCompleteVCard1 - 20", "Suite 1234", jsCard.getAddresses().get("ADR-1").getStreetExtendedAddress());
        assertEquals("testCompleteVCard1 - 21", "4321 Rue Somewhere", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testCompleteVCard1 - 22", "Quebec", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteVCard1 - 23", "QC", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteVCard1 - 24", "Canada", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteVCard1 - 25", "G1V 2M2", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testCompleteVCard1 - 26", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());
        assertEquals("testCompleteVCard1 - 27", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testCompleteVCard1 - 28", "123 Maple Ave Suite 90001 Vancouver BC 1239", jsCard.getAddresses().get("ADR-2").getFull());
        assertEquals("testCompleteVCard1 - 29", 1, jsCard.getEmails().size());
        assertTrue("testCompleteVCard1 - 30", jsCard.getEmails().get("EMAIL-1").asWork());
        assertEquals("testCompleteVCard1 - 31", "joe.user@example.com", jsCard.getEmails().get("EMAIL-1").getAddress());
        assertEquals("testCompleteVCard1 - 32", 2, jsCard.getPhones().size());
        assertTrue("testCompleteVCard1 - 33", jsCard.getPhones().get("PHONE-1").asVoice());
        assertEquals("testCompleteVCard1 - 34", "tel:+1-555-555-1234;ext=102", jsCard.getPhones().get("PHONE-1").getNumber());
        assertEquals("testCompleteVCard1 - 35", 1, (int) jsCard.getPhones().get("PHONE-1").getPref());
        assertTrue("testCompleteVCard1 - 36", jsCard.getPhones().get("PHONE-1").asWork());
        assertNull("testCompleteVCard1 - 37", jsCard.getPhones().get("PHONE-1").getLabel());
        assertTrue("testCompleteVCard1 - 38", jsCard.getPhones().get("PHONE-2").asVoice());
        assertEquals("testCompleteVCard1 - 39", "tel:+1-555-555-4321", jsCard.getPhones().get("PHONE-2").getNumber());
        assertNull("testCompleteVCard1 - 40", jsCard.getPhones().get("PHONE-2").getPref());
        assertTrue("testCompleteVCard1 - 41", jsCard.getPhones().get("PHONE-2").asWork());
        assertTrue("testCompleteVCard1 - 42", jsCard.getPhones().get("PHONE-2").asMobile());
        assertTrue("testCompleteVCard1 - 43", jsCard.getPhones().get("PHONE-2").asVideo());
        assertTrue("testCompleteVCard1 - 44", jsCard.getPhones().get("PHONE-2").asText());
        assertEquals("testCompleteVCard1 - 46", 1, jsCard.getCryptoKeys().size());
        assertEquals("testCompleteVCard1 - 47", "http://www.example.com/joe.user/joe.asc", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertNull("testCompleteVCard1 - 48", jsCard.getCryptoKeys().get("KEY-1").getPref());
        assertTrue("testCompleteVCard1 - 49", jsCard.getCryptoKeys().get("KEY-1").asWork());
        assertEquals("testCompleteVCard1 - 50", 1, jsCard.getLinks().size());
        assertEquals("testCompleteVCard1 - 51", "http://example.org", jsCard.getLinks().get("LINK-1").getUri());
        assertNull("testCompleteVCard1 - 52", jsCard.getLinks().get("LINK-1").getPref());
        assertTrue("testCompleteVCard1 - 53", jsCard.getLinks().get("LINK-1").asPrivate());
        assertTrue("testCompleteVCard1 - 54", jsCard.getLinks().get("LINK-1").isGenericLink());
        assertTrue("testCompleteVCard1 - 55", StringUtils.isNotEmpty(jsCard.getUid()));

    }

    @Test
    public void testCompleteVCard2() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Multilingual.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteVCard2 - 1", "大久保 正仁", jsCard.getName().getFull());
        assertEquals("testCompleteVCard2 - 3", "Okubo Masahito", jsCard.getLocalizations().get("en").get("name").get("full").asText());
        assertTrue("testCompleteVCard2 - 4", jsCard.getKind().isIndividual());
        assertEquals("testCompleteVCard2 - 5", 1, jsCard.getTitles().size());
        assertEquals("testCompleteVCard2 - 6", "事務局長", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testCompleteVCard2 - 8", "Secretary General", jsCard.getLocalization("en", "titles/TITLE-1").get("name").asText());
        assertTrue("testCompleteVCard2 - 9", jsCard.getKind().isIndividual());
        assertEquals("testCompleteVCard2 - 10", 2, jsCard.getPreferredLanguages().size());
        assertEquals("testCompleteVCard2 - 11", 1, (int) jsCard.getPreferredLanguages().get("LANG-1").getPref());
        assertEquals("testCompleteVCard2 - 12", 2, (int) jsCard.getPreferredLanguages().get("LANG-2").getPref());
        assertEquals("testCompleteVCard2 - 11", "jp", jsCard.getPreferredLanguages().get("LANG-1").getLanguage());
        assertEquals("testCompleteVCard2 - 12", "en", jsCard.getPreferredLanguages().get("LANG-2").getLanguage());
        assertTrue("testCompleteVCard2 - 13", StringUtils.isNotEmpty(jsCard.getUid()));
    }

    @Test
    public void testCompleteVCard3() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Unstructured.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteVCard3 - 1", "台灣固網股份有限公司", jsCard.getName().getFull());
        assertEquals("testCompleteVCard3 - 3", "Taiwan Fixed Network CO.,LTD.", jsCard.getLocalizations().get("en").get("name").get("full").asText());
        assertTrue("testCompleteVCard3 - 4", jsCard.getKind().isOrg());
        assertEquals("testCompleteVCard3 - 5", 1, jsCard.getAddresses().size());
        assertEquals("testCompleteVCard3 - 6", "8F., No.172-1, Sec.2, Ji-Lung Rd,", jsCard.getAddresses().get("ADR-1").getFull());
        assertNull("testCompleteVCard3 - 7", jsCard.getEmails());
        assertEquals("testCompleteVCard3 - 10", 2, jsCard.getPhones().size());
        assertTrue("testCompleteVCard3 - 11", jsCard.getPhones().get("PHONE-1").asVoice());
        assertFalse("testCompleteVCard3 - 12", jsCard.getPhones().get("PHONE-1").hasContext());
        assertTrue("testCompleteVCard3 - 13", jsCard.getPhones().get("PHONE-1").getNumber().isEmpty());
        assertTrue("testCompleteVCard3 - 14", jsCard.getPhones().get("PHONE-2").asFax());
        assertFalse("testCompleteVCard3 - 15", jsCard.getPhones().get("PHONE-2").hasContext());
        assertTrue("testCompleteVCard3 - 16", jsCard.getPhones().get("PHONE-2").getNumber().isEmpty());
        assertTrue("testCompleteVCard3 - 17", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteVCard4() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-RFC7095.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteVCard4 - 1", "Simon Perreault", jsCard.getName().getFull());
        assertNull("testCompleteVCard4 - 2", jsCard.getKind());
        assertEquals("testCompleteVCard4 - 3", 4, jsCard.getName().getComponents().length);
        assertTrue("testCompleteVCard4 - 4", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testCompleteVCard4 - 5", "Simon", jsCard.getName().getGiven());
        assertTrue("testCompleteVCard4 - 6", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testCompleteVCard4 - 7", "Perreault", jsCard.getName().getSurname());
        assertTrue("testCompleteVCard4 - 8", jsCard.getName().getComponents()[2].isCredential());
        assertEquals("testCompleteVCard4 - 9", "ing. jr", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testCompleteVCard4 - 10", jsCard.getName().getComponents()[3].isCredential());
        assertEquals("testCompleteVCard4 - 11", "M.Sc.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testCompleteVCard4 - 12", 2, jsCard.getAnniversaries().size());
        assertTrue("testCompleteVCard4 - 13", jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertTrue("testCompleteVCard4 - 14", jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("0000-02-03"));
        assertTrue("testCompleteVCard4 - 15", jsCard.getAnniversaries().get("ANNIVERSARY-2").isWedding());
        assertTrue("testCompleteVCard4 - 17", jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("2009-08-08T14:30:00-05:00"));
        assertTrue("testCompleteVCard4 - 18", jsCard.getSpeakToAs().isMasculine());

        assertEquals("testCompleteVCard4 - 19", 2, jsCard.getPreferredLanguages().size());
        assertEquals("testCompleteVCard4 - 20", 1, (int) jsCard.getPreferredLanguages().get("LANG-1").getPref());
        assertEquals("testCompleteVCard4 - 21", 2, (int) jsCard.getPreferredLanguages().get("LANG-2").getPref());
        assertEquals("testCompleteVCard4 - 20", "fr", jsCard.getPreferredLanguages().get("LANG-1").getLanguage());
        assertEquals("testCompleteVCard4 - 21", "en", jsCard.getPreferredLanguages().get("LANG-2").getLanguage());
        assertEquals("testCompleteVCard4 - 22", "Viagenie", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteVCard4 - 23", 1, jsCard.getAddresses().size());
        assertEquals("testCompleteVCard4 - 24", "Suite D2-630\n2875 Laurier\nQuebec\nQC\nG1V 2M2\nCanada", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testCompleteVCard4 - 25", "Suite D2-630", jsCard.getAddresses().get("ADR-1").getStreetExtendedAddress());
        assertEquals("testCompleteVCard4 - 26", "2875 Laurier", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testCompleteVCard4 - 27", "Quebec", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteVCard4 - 28", "QC", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteVCard4 - 29", "Canada", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteVCard4 - 30", "G1V 2M2", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testCompleteVCard4 - 31", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());
        assertEquals("testCompleteVCard4 - 32", "Etc/GMT+5", jsCard.getAddresses().get("ADR-1").getTimeZone());
        assertEquals("testCompleteVCard4 - 33", 2, jsCard.getPhones().size());
        assertTrue("testCompleteVCard4 - 34", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteVCard4 - 35", jsCard.getPhones().get("PHONE-1").asWork());
        assertEquals("testCompleteVCard4 - 36", "tel:+1-418-656-9254;ext=102", jsCard.getPhones().get("PHONE-1").getNumber());
        assertEquals("testCompleteVCard4 - 37", 1, (int) jsCard.getPhones().get("PHONE-1").getPref());
        assertTrue("testCompleteVCard4 - 38", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteVCard4 - 39", jsCard.getPhones().get("PHONE-2").asWork());
        assertTrue("testCompleteVCard4 - 40", jsCard.getPhones().get("PHONE-2").asMobile());
        assertTrue("testCompleteVCard4 - 41", jsCard.getPhones().get("PHONE-2").asVideo());
        assertTrue("testCompleteVCard4 - 42", jsCard.getPhones().get("PHONE-2").asText());
        assertEquals("testCompleteVCard4 - 43", "tel:+1-418-262-6501", jsCard.getPhones().get("PHONE-2").getNumber());
        assertEquals("testCompleteVCard4 - 44", 1, jsCard.getEmails().size());
        assertTrue("testCompleteVCard4 - 45", jsCard.getEmails().get("EMAIL-1").asWork());
        assertEquals("testCompleteVCard4 - 46", "simon.perreault@viagenie.ca", jsCard.getEmails().get("EMAIL-1").getAddress());
        assertEquals("testCompleteVCard4 - 47", 1, jsCard.getCryptoKeys().size());
        assertTrue("testCompleteVCard4 - 48", jsCard.getCryptoKeys().get("KEY-1").asWork());
        assertEquals("testCompleteVCard4 - 49", "http://www.viagenie.ca/simon.perreault/simon.asc", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertEquals("testCompleteVCard4 - 50", 1, jsCard.getLinks().size());
        assertTrue("testCompleteVCard4 - 51", jsCard.getLinks().get("LINK-1").asPrivate());
        assertTrue("testCompleteVCard4 - 52", jsCard.getLinks().get("LINK-1").isGenericLink());
        assertEquals("testCompleteVCard4 - 53", "http://nomis80.org", jsCard.getLinks().get("LINK-1").getUri());
        assertTrue("testCompleteVCard4 - 54", StringUtils.isNotEmpty(jsCard.getUid()));
    }

    @Test
    public void testCompleteVCard5() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Wikipedia.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteVCard5 - 1", "Forrest Gump", jsCard.getName().getFull());
        assertNull("testCompleteVCard5 - 2", jsCard.getKind());
        assertEquals("testCompleteVCard5 - 3", 3, jsCard.getName().getComponents().length);
        assertTrue("testCompleteVCard5 - 4", jsCard.getName().getComponents()[2].isTitle());
        assertEquals("testCompleteVCard5 - 5", "Mr.", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testCompleteVCard5 - 6", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testCompleteVCard5 - 7", "Forrest", jsCard.getName().getGiven());
        assertTrue("testCompleteVCard5 - 8", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testCompleteVCard5 - 9", "Gump", jsCard.getName().getSurname());
        assertEquals("testCompleteVCard5 - 10", "Bubba Gump Shrimp Co.", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteVCard5 - 11", "Shrimp Man", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testCompleteVCard5 - 15", "http://www.example.com/dir_photos/my_photo.gif", jsCard.getMedia().get("PHOTO-1").getUri());
        assertEquals("testCompleteVCard5 - 16", "image/gif", jsCard.getMedia().get("PHOTO-1").getMediaType());
        assertEquals("testCompleteVCard5 - 17", 2, jsCard.getPhones().size());
        assertTrue("testCompleteVCard5 - 18", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteVCard5 - 19", jsCard.getPhones().get("PHONE-1").asWork());
        assertEquals("testCompleteVCard5 - 20", "tel:+1-111-555-1212", jsCard.getPhones().get("PHONE-1").getNumber());
        assertNull("testCompleteVCard5 - 21", jsCard.getPhones().get("PHONE-1").getLabel());
        assertTrue("testCompleteVCard5 - 22", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteVCard5 - 23", jsCard.getPhones().get("PHONE-2").asPrivate());
        assertEquals("testCompleteVCard5 - 24", "tel:+1-404-555-1212", jsCard.getPhones().get("PHONE-2").getNumber());
        assertNull("testCompleteVCard5 - 25", jsCard.getPhones().get("PHONE-2").getLabel());

        assertEquals("testCompleteVCard5 - 26", 2, jsCard.getAddresses().size());
        assertTrue("testCompleteVCard5 - 27", jsCard.getAddresses().get("ADR-1").asWork());
        assertEquals("testCompleteVCard5 - 28", 1, (int) jsCard.getAddresses().get("ADR-1").getPref());
//        assertTrue("testCompleteVCard5 - 29", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("100 Waters Edge\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertEquals("testCompleteVCard5 - 30", "100 Waters Edge", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testCompleteVCard5 - 31", "Baytown", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteVCard5 - 32", "LA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteVCard5 - 33", "United States of America", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteVCard5 - 34", "30314", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertTrue("testCompleteVCard5 - 35", jsCard.getAddresses().get("ADR-2").asPrivate());
//        assertTrue("testCompleteVCard5 - 36", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("42 Plantation St.\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertEquals("testCompleteVCard5 - 37", "42 Plantation St.", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testCompleteVCard5 - 38", "Baytown", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testCompleteVCard5 - 39", "LA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testCompleteVCard5 - 40", "United States of America", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testCompleteVCard5 - 41", "30314", jsCard.getAddresses().get("ADR-2").getPostcode());

        assertEquals("testCompleteVCard5 - 42", 1, jsCard.getEmails().size());
        assertEquals("testCompleteVCard5 - 43", "forrestgump@example.com", jsCard.getEmails().get("EMAIL-1").getAddress());
        assertEquals("testCompleteVCard5 - 44", 0, jsCard.getUpdated().compareTo(DateUtils.toCalendar("2008-04-24T19:52:43Z")));
        assertEquals("testCompleteVCard5 - 45", 2, jsCard.getVCardProps().length); //including VERSION
        assertEquals("testCompleteVCard5 - 46", "x-qq", jsCard.getVCardProps()[1].getName().toString());
        assertEquals("testCompleteVCard5 - 47", 0, jsCard.getVCardProps()[1].getParameters().size());
        assertNull("testCompleteVCard5 - 48",  jsCard.getVCardProps()[1].getType());
        assertEquals("testCompleteVCard5 - 49", "21588891", jsCard.getVCardProps()[1].getValue());
        assertTrue("testCompleteVCard5 - 50", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteVCard6() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-ezvcard-fullcontact.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCompleteVCard6 - 1", "Prefix FirstName MiddleName LastName Suffix", jsCard.getName().getFull());
        assertEquals("testCompleteVCard6 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testCompleteVCard6 - 3", jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testCompleteVCard6 - 4", "Prefix", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testCompleteVCard6 - 5", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testCompleteVCard6 - 6", "FirstName", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testCompleteVCard6 - 7", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testCompleteVCard6 - 8", "LastName", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testCompleteVCard6 - 9", jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testCompleteVCard6 - 10", "MiddleName", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testCompleteVCard6 - 11", jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testCompleteVCard6 - 12", "Suffix", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testCompleteVCard6 - 13", 1, jsCard.getNicknames().size());
        assertEquals("testCompleteVCard6 - 14", "Nickname", jsCard.getNicknames().get("NICK-1").getName());
        assertEquals("testCompleteVCard6 - 15", 9, jsCard.getPhones().size());
        assertTrue("testCompleteVCard6 - 16", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteVCard6 - 17", jsCard.getPhones().get("PHONE-1").asPrivate());
        assertEquals("testCompleteVCard6 - 18", "555-555-1111", jsCard.getPhones().get("PHONE-1").getNumber());
        assertNull("testCompleteVCard6 - 19", jsCard.getPhones().get("PHONE-1").getLabel());
        assertTrue("testCompleteVCard6 - 20", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteVCard6 - 21", jsCard.getPhones().get("PHONE-2").asWork());
        assertEquals("testCompleteVCard6 - 22", "555-555-1112", jsCard.getPhones().get("PHONE-2").getNumber());
        assertNull("testCompleteVCard6 - 23", jsCard.getPhones().get("PHONE-2").getLabel());
        assertTrue("testCompleteVCard6 - 24", jsCard.getPhones().get("PHONE-3").asVoice());
        assertFalse("testCompleteVCard6 - 25", jsCard.getPhones().get("PHONE-3").hasContext());
        assertEquals("testCompleteVCard6 - 26", "555-555-1113", jsCard.getPhones().get("PHONE-3").getNumber());
        assertTrue("testCompleteVCard6 - 27", jsCard.getPhones().get("PHONE-3").asMobile());
        assertTrue("testCompleteVCard6 - 29", jsCard.getPhones().get("PHONE-4").asVoice());
        assertFalse("testCompleteVCard6 - 30", jsCard.getPhones().get("PHONE-4").hasContext());
        assertEquals("testCompleteVCard6 - 31", "555-555-1114", jsCard.getPhones().get("PHONE-4").getNumber());
        assertTrue("testCompleteVCard6 - 32", jsCard.getPhones().get("PHONE-4").asMobile());
        assertTrue("testCompleteVCard6 - 34", jsCard.getPhones().get("PHONE-5").asVoice());
        assertFalse("testCompleteVCard6 - 35", jsCard.getPhones().get("PHONE-5").hasContext());
        assertEquals("testCompleteVCard6 - 36", "555-555-1115", jsCard.getPhones().get("PHONE-5").getNumber());
        assertNull("testCompleteVCard6 - 37", jsCard.getPhones().get("PHONE-5").getLabel());
        assertTrue("testCompleteVCard6 - 38", jsCard.getPhones().get("PHONE-6").asFax());
        assertTrue("testCompleteVCard6 - 39", jsCard.getPhones().get("PHONE-6").asPrivate());
        assertEquals("testCompleteVCard6 - 40", "555-555-1116", jsCard.getPhones().get("PHONE-6").getNumber());
        assertNull("testCompleteVCard6 - 41", jsCard.getPhones().get("PHONE-6").getLabel());
        assertTrue("testCompleteVCard6 - 42", jsCard.getPhones().get("PHONE-7").asFax());
        assertTrue("testCompleteVCard6 - 43", jsCard.getPhones().get("PHONE-7").asWork());
        assertEquals("testCompleteVCard6 - 44", "555-555-1117", jsCard.getPhones().get("PHONE-7").getNumber());
        assertNull("testCompleteVCard6 - 45", jsCard.getPhones().get("PHONE-7").getLabel());
        assertTrue("testCompleteVCard6 - 46", jsCard.getPhones().get("PHONE-8").asVoice());
        assertFalse("testCompleteVCard6 - 47", jsCard.getPhones().get("PHONE-8").hasContext());
        assertEquals("testCompleteVCard6 - 48", "555-555-1118", jsCard.getPhones().get("PHONE-8").getNumber());
        assertNull("testCompleteVCard6 - 49", jsCard.getPhones().get("PHONE-8").getLabel());
        assertTrue("testCompleteVCard6 - 50", jsCard.getPhones().get("PHONE-9").asVoice());
        assertFalse("testCompleteVCard6 - 51", jsCard.getPhones().get("PHONE-9").hasContext());
        assertEquals("testCompleteVCard6 - 52", "555-555-1119", jsCard.getPhones().get("PHONE-9").getNumber());
        assertNull("testCompleteVCard6 - 53", jsCard.getPhones().get("PHONE-9").getLabel());
        assertEquals("testCompleteVCard6 - 54", 5, jsCard.getEmails().size());
        assertTrue("testCompleteVCard6 - 55", jsCard.getEmails().get("EMAIL-1").asPrivate());
        assertEquals("testCompleteVCard6 - 56", "home@example.com", jsCard.getEmails().get("EMAIL-1").getAddress());
        assertTrue("testCompleteVCard6 - 58", jsCard.getEmails().get("EMAIL-2").asWork());
        assertEquals("testCompleteVCard6 - 59", "work@example.com", jsCard.getEmails().get("EMAIL-2").getAddress());
        assertFalse("testCompleteVCard6 - 60", jsCard.getEmails().get("EMAIL-3").hasContext());
        assertEquals("testCompleteVCard6 - 61", "school@example.com", jsCard.getEmails().get("EMAIL-3").getAddress());
        assertFalse("testCompleteVCard6 - 64", jsCard.getEmails().get("EMAIL-4").hasContext());
        assertEquals("testCompleteVCard6 - 65", "other@example.com", jsCard.getEmails().get("EMAIL-4").getAddress());
        assertFalse("testCompleteVCard6 - 67", jsCard.getEmails().get("EMAIL-5").hasContext());
        assertEquals("testCompleteVCard6 - 68", "custom@example.com", jsCard.getEmails().get("EMAIL-5").getAddress());
        assertEquals("testCompleteVCard6 - 71", 2, jsCard.getOrganizations().size());
        assertEquals("testCompleteVCard6 - 72", "Organization1", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testCompleteVCard6 - 72-1", "Department1", jsCard.getOrganizations().get("ORG-1").getUnits()[0].getName());
        assertEquals("testCompleteVCard6 - 73", "Organization2", jsCard.getOrganizations().get("ORG-2").getName());
        assertEquals("testCompleteVCard6 - 73", "Department2", jsCard.getOrganizations().get("ORG-2").getUnits()[0].getName());
        assertEquals("testCompleteVCard6 - 74", 2, jsCard.getTitles().size());
        assertEquals("testCompleteVCard6 - 75", "Title1", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testCompleteVCard6 - 76", "Title2", jsCard.getTitles().get("TITLE-2").getName());
        assertEquals("testCompleteVCard6 - 77", 1, jsCard.getKeywords().size());
        assertTrue("testCompleteVCard6 - 78", jsCard.getKeywords().containsKey("Tag"));
        assertNotNull("testCompleteVCard6 - 79", jsCard.getNotes());
//        assertTrue("testCompleteVCard6 - 80", jsCard.getNotes()[0].equals("Notes line 1\nNotes line 2"));
        assertEquals("testCompleteVCard6 - 81", "ez-vcard 0.9.14-fc", jsCard.getProdId());
        assertEquals("testCompleteVCard6 - 82", 1, jsCard.getAnniversaries().size());
        assertNotNull("testCompleteVCard6 - 83.a", jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().getPartialDate());
        assertEquals("testCompleteVCard6 - 83.b", 2016, (long) jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().getPartialDate().getYear());
        assertEquals("testCompleteVCard6 - 83.c", 8, (long) jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().getPartialDate().getMonth());
        assertEquals("testCompleteVCard6 - 83.d", 1, (long) jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().getPartialDate().getDay());
        assertTrue("testCompleteVCard6 - 84", jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertTrue("testCompleteVCard6 - 85", jsCard.getSpeakToAs().isMasculine());


        assertEquals("testCompleteVCard6 - 86", 3, jsCard.getMedia().size());
        assertEquals("testCompleteVCard6 - 87", "https://d3m0kzytmr41b1.cloudfront.net/c335e945d1b60edd9d75eb4837c432f637e95c8a", jsCard.getMedia().get("PHOTO-1").getUri());
        assertEquals("testCompleteVCard6 - 88", "https://d3m0kzytmr41b1.cloudfront.net/c335e945d1b60edd9d75eb4837c432f637e95c8a", jsCard.getMedia().get("PHOTO-2").getUri());
        assertEquals("testCompleteVCard6 - 89", "https://d2ojpxxtu63wzl.cloudfront.net/static/aa915d1f29f19baf560e5491decdd30a_67c95da9133249fde8b0da7ceebc298bf680117e6f52054f7f5f7a95e8377238", jsCard.getMedia().get("PHOTO-3").getUri());

        assertEquals("testCompleteVCard6 - 90", 7, jsCard.getOnlineServices().size());
        assertEquals("testCompleteVCard6 - 98", "xmpp:gtalk", jsCard.getOnlineServices().get("OS-1").getUri());
        assertEquals("testCompleteVCard6 - 102", "skype:skype", jsCard.getOnlineServices().get("OS-2").getUri());
        assertEquals("testCompleteVCard6 - 106", "ymsgr:yahoo", jsCard.getOnlineServices().get("OS-3").getUri());
        assertEquals("testCompleteVCard6 - 110", "aim:aim", jsCard.getOnlineServices().get("OS-4").getUri());
        assertEquals("testCompleteVCard6 - 114", "xmpp:jabber", jsCard.getOnlineServices().get("OS-5").getUri());
        assertEquals("testCompleteVCard6 - 118", "other:other", jsCard.getOnlineServices().get("OS-6").getUri());
        assertEquals("testCompleteVCard6 - 122", "customtype:custom", jsCard.getOnlineServices().get("OS-7").getUri());

        assertEquals("testCompleteVCard6 - 124", 4, jsCard.getLinks().size());
        assertTrue("testCompleteVCard6 - 125", jsCard.getLinks().get("LINK-1").isGenericLink());
        assertEquals("testCompleteVCard6 - 126", "http://www.homepage.com", jsCard.getLinks().get("LINK-1").getUri());
        assertTrue("testCompleteVCard6 - 129", jsCard.getLinks().get("LINK-2").isGenericLink());
        assertEquals("testCompleteVCard6 - 130", "http://www.blog.com", jsCard.getLinks().get("LINK-2").getUri());
        assertTrue("testCompleteVCard6 - 133", jsCard.getLinks().get("LINK-3").isGenericLink());
        assertEquals("testCompleteVCard6 - 134", "http://www.other.com", jsCard.getLinks().get("LINK-3").getUri());
        assertTrue("testCompleteVCard6 - 137", jsCard.getLinks().get("LINK-4").isGenericLink());
        assertEquals("testCompleteVCard6 - 138", "http://www.custom.com", jsCard.getLinks().get("LINK-4").getUri());
        assertEquals("testCompleteVCard6 - 141", 4, jsCard.getAddresses().size());
        assertTrue("testCompleteVCard6 - 142", jsCard.getAddresses().get("ADR-1").asPrivate());
        assertEquals("testCompleteVCard6 - 143", "HomeExtended\nHomeStreet\nHomeCity\nHomeState\nHomePostal\nHomeCountry", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testCompleteVCard6 - 144", "HomeExtended", jsCard.getAddresses().get("ADR-1").getStreetExtendedAddress());
        assertEquals("testCompleteVCard6 - 145", "HomeStreet", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testCompleteVCard6 - 146", "HomeCity", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testCompleteVCard6 - 147", "HomeState", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testCompleteVCard6 - 148", "HomeCountry", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testCompleteVCard6 - 149", "HomePostal", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertTrue("testCompleteVCard6 - 150", jsCard.getAddresses().get("ADR-2").asWork());
        assertEquals("testCompleteVCard6 - 151", "WorkExtended\nWorkStreet\nWorkCity\nWorkState\nWorkPostal\nWorkCountry", jsCard.getAddresses().get("ADR-2").getFull());
        assertEquals("testCompleteVCard6 - 152", "WorkExtended", jsCard.getAddresses().get("ADR-2").getStreetExtendedAddress());
        assertEquals("testCompleteVCard6 - 153", "WorkStreet", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testCompleteVCard6 - 154", "WorkCity", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testCompleteVCard6 - 155", "WorkState", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testCompleteVCard6 - 156", "WorkCountry", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testCompleteVCard6 - 157", "WorkPostal", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertFalse("testCompleteVCard6 - 158", jsCard.getAddresses().get("ADR-3").hasContext());
        assertEquals("testCompleteVCard6 - 159", "OtherExtended\nOtherStreet\nOtherCity\nOtherState\nOtherPostal\nOtherCountry", jsCard.getAddresses().get("ADR-3").getFull());
        assertEquals("testCompleteVCard6 - 160", "OtherExtended", jsCard.getAddresses().get("ADR-3").getStreetExtendedAddress());
        assertEquals("testCompleteVCard6 - 161", "OtherStreet", jsCard.getAddresses().get("ADR-3").getStreetName());
        assertEquals("testCompleteVCard6 - 162", "OtherCity", jsCard.getAddresses().get("ADR-3").getLocality());
        assertEquals("testCompleteVCard6 - 163", "OtherState", jsCard.getAddresses().get("ADR-3").getRegion());
        assertEquals("testCompleteVCard6 - 164", "OtherCountry", jsCard.getAddresses().get("ADR-3").getCountry());
        assertEquals("testCompleteVCard6 - 165", "OtherPostal", jsCard.getAddresses().get("ADR-3").getPostcode());
        assertFalse("testCompleteVCard6 - 166", jsCard.getAddresses().get("ADR-4").hasContext());
        assertEquals("testCompleteVCard6 - 167", "CustomExtended\nCustomStreet\nCustomCity\nCustomState\nCustomPostal\nCustomCountry", jsCard.getAddresses().get("ADR-4").getFull());
        assertEquals("testCompleteVCard6 - 168", "CustomExtended", jsCard.getAddresses().get("ADR-4").getStreetExtendedAddress());
        assertEquals("testCompleteVCard6 - 169", "CustomStreet", jsCard.getAddresses().get("ADR-4").getStreetAddress());
        assertEquals("testCompleteVCard6 - 170", "CustomCity", jsCard.getAddresses().get("ADR-4").getLocality());
        assertEquals("testCompleteVCard6 - 171", "CustomState", jsCard.getAddresses().get("ADR-4").getRegion());
        assertEquals("testCompleteVCard6 - 172", "CustomCountry", jsCard.getAddresses().get("ADR-4").getCountry());
        assertEquals("testCompleteVCard6 - 173", "CustomPostal", jsCard.getAddresses().get("ADR-4").getPostcode());
        assertEquals("testCompleteVCard6 - 174", 23, jsCard.getVCardPropsAsMap().size()); //including VERSION
        assertEquals("testCompleteVCard6 - 176", "male", jsCard.getVCardPropsAsMap().get("X-GENDER".toLowerCase()));
        assertEquals("testCompleteVCard6 - 177", "14f9aba0c9422da9ae376fe28bd89c2a.0", jsCard.getVCardPropsAsMap().get("X-ID".toLowerCase()));
        assertEquals("testCompleteVCard6 - 178", "fffffea9056d8166e2b7a427977e570c87dd51279d11d9b137c593eb", jsCard.getVCardPropsAsMap().get("X-ETAG".toLowerCase()));
        assertEquals("testCompleteVCard6 - 179", "579c773f-736d-11e6-8dff-0ac8448704fb", jsCard.getVCardPropsAsMap().get("X-FC-TAGS".toLowerCase()));
        assertEquals("testCompleteVCard6 - 180", "8ad23200aa3e1984736b11e688dc0add41994b95", jsCard.getVCardPropsAsMap().get("X-FC-LIST-ID".toLowerCase()));
        assertEquals("testCompleteVCard6 - 181", "Mother", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A4D6F74686572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 182", "Father", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A466174686572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 183", "Parent", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A506172656E74".toLowerCase()));
        assertEquals("testCompleteVCard6 - 184", "Brother", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A42726F74686572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 185", "Sister", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A536973746572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 186", "Child", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A4368696C64".toLowerCase()));
        assertEquals("testCompleteVCard6 - 187", "Friend", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A467269656E64".toLowerCase()));
        assertEquals("testCompleteVCard6 - 188", "Spouse", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A53706F757365".toLowerCase()));
        assertEquals("testCompleteVCard6 - 189", "Fiance", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A4669616E63C3A9".toLowerCase()));
        assertEquals("testCompleteVCard6 - 190", "Partner", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A506172746E6572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 191", "Assistant", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A417373697374616E74".toLowerCase()));
        assertEquals("testCompleteVCard6 - 192", "Manager", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A4D616E61676572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 193", "Other", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A4F74686572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 194", "Custom", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D52656C617465644E616D65733A437573746F6D54595045".toLowerCase()));
        assertEquals("testCompleteVCard6 - 195", "2016-08-02", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D4F7468657244617465733A416E6E6976657273617279".toLowerCase()));
        assertEquals("testCompleteVCard6 - 196", "2016-08-03", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D4F7468657244617465733A4F74686572".toLowerCase()));
        assertEquals("testCompleteVCard6 - 197", "2016-08-04", jsCard.getVCardPropsAsMap().get("X-FCENCODED-582D46432D4F7468657244617465733A437573746F6D54595045".toLowerCase()));

    }

}
