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

import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

public class JCardTest extends JCard2JSContactTest {

    //jCard is not an array
    @Test(expected = CardException.class)
    public void testJCardInvalid1() throws IOException, CardException {

        String jcard="{}";
        jCard2JSContact.convert(jcard);
    }

    //jCard is an empty array
    @Test(expected = CardException.class)
    public void testJCardInvalid2() throws IOException, CardException {

        String jcard="[]";
        jCard2JSContact.convert(jcard);
    }

    //jCard array includes only one item
    @Test(expected = RuntimeException.class)
    public void testJCardInvalid3() throws IOException, CardException {

        String jcard="[\"vcard\"]";
        jCard2JSContact.convert(jcard);
    }

        //jCard array 2nd item is empty
        @Test(expected = CardException.class)
        public void testJCardInvalid4() throws IOException, CardException {

            String jcard="[\"vcard\",[]]";
            jCard2JSContact.convert(jcard);
        }

    //jCard does not include fn
    @Test(expected = CardException.class)
    public void testJCardInvalid5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }


    //version param object appears as null instead of empty object
    @Test(expected = RuntimeException.class)
    public void testJCardInvalid6() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", null, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }


    //jCard array 1st is not vcard
    @Test(expected = CardException.class)
    public void testJCardInvalid7() throws IOException, CardException {

        String jcard="[\"jcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }

    //a jCard property includes the value parameter
    @Test(expected = CardException.class)
    public void testJCardInvalid8() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", { \"value\": \"a value\" }, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }

    //pref must be between 1 and 100
    @Test(expected = CardException.class)
    public void testJCardInvalid9() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {\"pref\":\"0\"}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard);
    }

    @Test
    public void testJCardValid() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testJCardValid - 1",jsCard != null);
        assertTrue("testJCardValid - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertTrue("testJCardValid - 3",jsCard.getFullName().equals("test"));

    }

    @Test
    public void testExtendedJCardValid() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"], [\"myext\", {}, \"text\", \"extvalue\"]]]";
        JCard2JSContact jCard2JSContact = JCard2JSContact.builder()
                .config(VCard2JSContactConfig.builder()
                        .extensionsPrefix("extension:")
                        .build()
                )
                .build();
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testExtendedJCardValid - 1",jsCard != null);
        assertTrue("testExtendedJCardValid - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertTrue("testExtendedJCardValid - 3",jsCard.getFullName().equals("test"));
        assertTrue("testExtendedJCardValid - 4",jsCard.getExtensions().size() == 1);
        assertTrue("testExtendedJCardValid - 5",jsCard.getExtensions().get("extension:myext").equals("extvalue"));
    }


    @Test
    public void testCompleteJCard1() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7483.json"), Charset.forName("UTF-8"));
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard1 - 1", jsCard.getFullName().equals("Joe User"));
        assertTrue("testCompleteJCard1 - 2", jsCard.getKind().isIndividual());
        assertTrue("testCompleteJCard1 - 3", jsCard.getName().getComponents().length == 4);
        assertTrue("testCompleteJCard1 - 4", jsCard.getName().getComponents()[0].isPersonal());
        assertTrue("testCompleteJCard1 - 5", jsCard.getName().getComponents()[0].getValue().equals("Joe"));
        assertTrue("testCompleteJCard1 - 6", jsCard.getName().getComponents()[1].isSurname());
        assertTrue("testCompleteJCard1 - 7", jsCard.getName().getComponents()[1].getValue().equals("User"));
        assertTrue("testCompleteJCard1 - 8", jsCard.getName().getComponents()[2].isSuffix());
        assertTrue("testCompleteJCard1 - 9", jsCard.getName().getComponents()[2].getValue().equals("ing. jr"));
        assertTrue("testCompleteJCard1 - 10", jsCard.getName().getComponents()[3].isSuffix());
        assertTrue("testCompleteJCard1 - 11", jsCard.getName().getComponents()[3].getValue().equals("M.Sc."));
        assertTrue("testCompleteJCard1 - 12", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteJCard1 - 13", jsCard.getPreferredContactLanguages().get("fr")[0].getPref() == 1);
        assertTrue("testCompleteJCard1 - 14", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
        assertTrue("testCompleteJCard1 - 15", jsCard.getOrganizations().get("ORG-1").getName().equals("Example"));
        assertTrue("testCompleteJCard1 - 16", jsCard.getTitles().get("TITLE-1").getTitle().equals("Research Scientist"));
        assertTrue("testCompleteJCard1 - 17", jsCard.getTitles().get("TITLE-2").getTitle().equals("Project Lead"));
        assertTrue("testCompleteJCard1 - 18", jsCard.getAddresses().size() == 2);
        assertTrue("testCompleteJCard1 - 19", jsCard.getAddresses().get("ADR-1").getFullAddress().equals("Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada"));
        assertTrue("testCompleteJCard1 - 20", jsCard.getAddresses().get("ADR-1").getStreetExtensions().equals("Suite 1234"));
        assertTrue("testCompleteJCard1 - 21", jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("4321 Rue Somewhere"));
        assertTrue("testCompleteJCard1 - 22", jsCard.getAddresses().get("ADR-1").getLocality().equals("Quebec"));
        assertTrue("testCompleteJCard1 - 23", jsCard.getAddresses().get("ADR-1").getRegion().equals("QC"));
        assertTrue("testCompleteJCard1 - 24", jsCard.getAddresses().get("ADR-1").getCountry().equals("Canada"));
        assertTrue("testCompleteJCard1 - 25", jsCard.getAddresses().get("ADR-1").getPostcode().equals("G1V 2M2"));
        assertTrue("testCompleteJCard1 - 26", jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));
        assertTrue("testCompleteJCard1 - 27", jsCard.getAddresses().get("ADR-1").getTimeZone().equals("Etc/GMT+5"));
        assertTrue("testCompleteJCard1 - 28", jsCard.getAddresses().get("ADR-2").getFullAddress().equals("123 Maple Ave\nSuite 90001\nVancouver\nBC\n1239\n"));
        assertTrue("testCompleteJCard1 - 29", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteJCard1 - 30", jsCard.getEmails().get("EMAIL-1").asWork());
        assertTrue("testCompleteJCard1 - 31", jsCard.getEmails().get("EMAIL-1").getEmail().equals("joe.user@example.com"));
        assertTrue("testCompleteJCard1 - 32", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteJCard1 - 33", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteJCard1 - 34", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-555-555-1234;ext=102"));
        assertTrue("testCompleteJCard1 - 35", jsCard.getPhones().get("PHONE-1").getPref() == 1);
        assertTrue("testCompleteJCard1 - 36", jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testCompleteJCard1 - 37", jsCard.getPhones().get("PHONE-1").getLabel() == null);
        assertTrue("testCompleteJCard1 - 38", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteJCard1 - 39", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-555-555-4321"));
        assertTrue("testCompleteJCard1 - 40", jsCard.getPhones().get("PHONE-2").getPref() == null);
        assertTrue("testCompleteJCard1 - 41", jsCard.getPhones().get("PHONE-2").asWork());
        assertTrue("testCompleteJCard1 - 42", jsCard.getPhones().get("PHONE-2").asCell());
        assertTrue("testCompleteJCard1 - 43", jsCard.getPhones().get("PHONE-2").asVideo());
        assertTrue("testCompleteJCard1 - 44", jsCard.getPhones().get("PHONE-2").asText());
        assertTrue("testCompleteJCard1 - 45", jsCard.getOnline().size() == 2);
        assertTrue("testCompleteJCard1 - 46", jsCard.getOnline().get("KEY-1").isPublicKey());
        assertTrue("testCompleteJCard1 - 47", jsCard.getOnline().get("KEY-1").getResource().equals("http://www.example.com/joe.user/joe.asc"));
        assertTrue("testCompleteJCard1 - 48", jsCard.getOnline().get("KEY-1").getPref() == null);
        assertTrue("testCompleteJCard1 - 49", jsCard.getOnline().get("KEY-1").asWork());
        assertTrue("testCompleteJCard1 - 50", jsCard.getOnline().get("URI-1").getResource().equals("http://example.org"));
        assertTrue("testCompleteJCard1 - 51", jsCard.getOnline().get("URI-1").getPref() == null);
        assertTrue("testCompleteJCard1 - 52", jsCard.getOnline().get("URI-1").asPrivate());
        assertTrue("testCompleteJCard1 - 53", jsCard.getOnline().get("URI-1").isUri());
        assertTrue("testCompleteJCard1 - 54", StringUtils.isNotEmpty(jsCard.getUid()));

    }

    @Test
    public void testCompleteJCard2() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Multilingual.json"), Charset.forName("UTF-8"));
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard2 - 1", jsCard.getFullName().equals("大久保 正仁"));
        assertTrue("testCompleteJCard2 - 3", jsCard.getLocalizations().get("en").get("/fullName").asText().equals("Okubo Masahito"));
        assertTrue("testCompleteJCard2 - 4", jsCard.getKind().isIndividual());
        assertTrue("testCompleteJCard2 - 5", jsCard.getTitles().size() == 1);
        assertTrue("testCompleteJCard2 - 6", jsCard.getTitles().get("TITLE-1").getTitle().equals("事務局長"));
        assertTrue("testCompleteJCard2 - 8", jsCard.getLocalization("en","/titles/TITLE-1").get("title").asText().equals("Secretary General"));
        assertTrue("testCompleteJCard2 - 9", jsCard.getKind().isIndividual());
        assertTrue("testCompleteJCard2 - 10", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteJCard2 - 11", jsCard.getPreferredContactLanguages().get("jp")[0].getPref() == 1);
        assertTrue("testCompleteJCard2 - 12", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
        assertTrue("testCompleteJCard2 - 13", StringUtils.isNotEmpty(jsCard.getUid()));
    }

    @Test
    public void testCompleteJCard3() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Unstructured.json"), Charset.forName("UTF-8"));
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard3 - 1", jsCard.getFullName().equals("台灣固網股份有限公司"));
        assertTrue("testCompleteJCard3 - 3", jsCard.getLocalizations().get("en").get("/fullName").asText().equals("Taiwan Fixed Network CO.,LTD."));
        assertTrue("testCompleteJCard3 - 4", jsCard.getKind().isOrg());
        assertTrue("testCompleteJCard3 - 5", jsCard.getAddresses().size() == 1);
        assertTrue("testCompleteJCard3 - 6", jsCard.getAddresses().get("ADR-1").getFullAddress().equals("8F., No.172-1, Sec.2, Ji-Lung Rd,"));
        assertTrue("testCompleteJCard3 - 8", jsCard.getEmails() == null);
        assertTrue("testCompleteJCard3 - 10", jsCard.getPhones().size() == 2);
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

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7095.json"), Charset.forName("UTF-8"));
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard4 - 1", jsCard.getFullName().equals("Simon Perreault"));
        assertTrue("testCompleteJCard4 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteJCard4 - 3", jsCard.getName().getComponents().length == 4);
        assertTrue("testCompleteJCard4 - 4", jsCard.getName().getComponents()[0].isPersonal());
        assertTrue("testCompleteJCard4 - 5", jsCard.getName().getComponents()[0].getValue().equals("Simon"));
        assertTrue("testCompleteJCard4 - 6", jsCard.getName().getComponents()[1].isSurname());
        assertTrue("testCompleteJCard4 - 7", jsCard.getName().getComponents()[1].getValue().equals("Perreault"));
        assertTrue("testCompleteJCard4 - 8", jsCard.getName().getComponents()[2].isSuffix());
        assertTrue("testCompleteJCard4 - 9", jsCard.getName().getComponents()[2].getValue().equals("ing. jr"));
        assertTrue("testCompleteJCard4 - 10", jsCard.getName().getComponents()[3].isSuffix());
        assertTrue("testCompleteJCard4 - 11", jsCard.getName().getComponents()[3].getValue().equals("M.Sc."));
        assertTrue("testCompleteJCard4 - 12", jsCard.getAnniversaries().size()==2);
        assertTrue("testCompleteJCard4 - 13", jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertTrue("testCompleteJCard4 - 14", jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("0000-02-03"));
        assertTrue("testCompleteJCard4 - 15", jsCard.getAnniversaries().get("ANNIVERSARY-2").isOtherAnniversary());
        assertTrue("testCompleteJCard4 - 16", jsCard.getAnniversaries().get("ANNIVERSARY-2").getLabel().equals("marriage date"));
        assertTrue("testCompleteJCard4 - 17", jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("2009-08-08T14:30:00-05:00"));
        assertTrue("testCompleteJCard4 - 18", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteJCard4 - 19", jsCard.getPreferredContactLanguages().get("fr")[0].getPref() == 1);
        assertTrue("testCompleteJCard4 - 20", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
        assertTrue("testCompleteJCard4 - 21", jsCard.getOrganizations().get("ORG-1").getName().equals("Viagenie"));
        assertTrue("testCompleteJCard4 - 22", jsCard.getAddresses().size() == 1);
        assertTrue("testCompleteJCard4 - 23", jsCard.getAddresses().get("ADR-1").getFullAddress().equals("Suite D2-630\n2875 Laurier\nQuebec\nQC\nG1V 2M2\nCanada"));
        assertTrue("testCompleteJCard4 - 24", jsCard.getAddresses().get("ADR-1").getStreetExtensions().equals("Suite D2-630"));
        assertTrue("testCompleteJCard4 - 25", jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("2875 Laurier"));
        assertTrue("testCompleteJCard4 - 26", jsCard.getAddresses().get("ADR-1").getLocality().equals("Quebec"));
        assertTrue("testCompleteJCard4 - 27", jsCard.getAddresses().get("ADR-1").getRegion().equals("QC"));
        assertTrue("testCompleteJCard4 - 28", jsCard.getAddresses().get("ADR-1").getCountry().equals("Canada"));
        assertTrue("testCompleteJCard4 - 29", jsCard.getAddresses().get("ADR-1").getPostcode().equals("G1V 2M2"));
        assertTrue("testCompleteJCard4 - 30", jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));
        assertTrue("testCompleteJCard4 - 31", jsCard.getAddresses().get("ADR-1").getTimeZone().equals("Etc/GMT+5"));
        assertTrue("testCompleteJCard4 - 32", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteJCard4 - 33", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteJCard4 - 34", jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testCompleteJCard4 - 35", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-418-656-9254;ext=102"));
        assertTrue("testCompleteJCard4 - 36", jsCard.getPhones().get("PHONE-1").getPref() == 1);
        assertTrue("testCompleteJCard4 - 37", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteJCard4 - 38", jsCard.getPhones().get("PHONE-2").asWork());
        assertTrue("testCompleteJCard4 - 39", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-418-262-6501"));
        assertTrue("testCompleteJCard4 - 40", jsCard.getPhones().get("PHONE-2").asCell());
        assertTrue("testCompleteJCard4 - 41", jsCard.getPhones().get("PHONE-2").asVideo());
        assertTrue("testCompleteJCard4 - 42", jsCard.getPhones().get("PHONE-2").asText());
        assertTrue("testCompleteJCard4 - 43", jsCard.getPhones().get("PHONE-2").getLabel() == null);
        assertTrue("testCompleteJCard4 - 44", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteJCard4 - 45", jsCard.getEmails().get("EMAIL-1").asWork());
        assertTrue("testCompleteJCard4 - 46", jsCard.getEmails().get("EMAIL-1").getEmail().equals("simon.perreault@viagenie.ca"));
        assertTrue("testCompleteJCard4 - 47", jsCard.getOnline().size() == 2);
        assertTrue("testCompleteJCard4 - 48", jsCard.getOnline().get("KEY-1").asWork());
        assertTrue("testCompleteJCard4 - 50", jsCard.getOnline().get("KEY-1").isPublicKey());
        assertTrue("testCompleteJCard4 - 51", jsCard.getOnline().get("KEY-1").getResource().equals("http://www.viagenie.ca/simon.perreault/simon.asc"));
        assertTrue("testCompleteJCard4 - 52", jsCard.getOnline().get("URI-1").asPrivate());
        assertTrue("testCompleteJCard4 - 53", jsCard.getOnline().get("URI-1").getResource().equals("http://nomis80.org"));
        assertTrue("testCompleteJCard4 - 54", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteJCard5() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Wikipedia.json"), Charset.forName("UTF-8"));
        Card jsCard = (Card) jCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteJCard5 - 1", jsCard.getFullName().equals("Forrest Gump"));
        assertTrue("testCompleteJCard5 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteJCard5 - 3", jsCard.getName().getComponents().length == 3);
        assertTrue("testCompleteJCard5 - 4", jsCard.getName().getComponents()[0].isPrefix());
        assertTrue("testCompleteJCard5 - 5", jsCard.getName().getComponents()[0].getValue().equals("Mr."));
        assertTrue("testCompleteJCard5 - 6", jsCard.getName().getComponents()[1].isPersonal());
        assertTrue("testCompleteJCard5 - 7", jsCard.getName().getComponents()[1].getValue().equals("Forrest"));
        assertTrue("testCompleteJCard5 - 8", jsCard.getName().getComponents()[2].isSurname());
        assertTrue("testCompleteJCard5 - 9", jsCard.getName().getComponents()[2].getValue().equals("Gump"));
        assertTrue("testCompleteJCard5 - 10", jsCard.getOrganizations().get("ORG-1").getName().equals("Bubba Gump Shrimp Co."));
        assertTrue("testCompleteJCard5 - 11", jsCard.getTitles().get("TITLE-1").getTitle().equals("Shrimp Man"));
        assertTrue("testCompleteJCard5 - 15", jsCard.getPhotos().get("PHOTO-1").getHref().equals("http://www.example.com/dir_photos/my_photo.gif"));
        assertTrue("testCompleteJCard5 - 16", jsCard.getPhotos().get("PHOTO-1").getMediaType().equals("image/gif"));
        assertTrue("testCompleteJCard5 - 17", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteJCard5 - 18", jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testCompleteJCard5 - 19", jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testCompleteJCard5 - 20", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-111-555-1212"));
        assertTrue("testCompleteJCard5 - 21", jsCard.getPhones().get("PHONE-1").getLabel() == null);
        assertTrue("testCompleteJCard5 - 22", jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testCompleteJCard5 - 23", jsCard.getPhones().get("PHONE-2").asPrivate());
        assertTrue("testCompleteJCard5 - 24", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-404-555-1212"));
        assertTrue("testCompleteJCard5 - 25", jsCard.getPhones().get("PHONE-2").getLabel() == null);

        assertTrue("testCompleteJCard5 - 26", jsCard.getAddresses().size() == 2);
        assertTrue("testCompleteJCard5 - 27", jsCard.getAddresses().get("ADR-1").asWork());
        assertTrue("testCompleteJCard5 - 28", jsCard.getAddresses().get("ADR-1").getPref() == 1);
        assertTrue("testCompleteJCard5 - 29", jsCard.getAddresses().get("ADR-1").getFullAddress().equals("100 Waters Edge\nBaytown, LA 30314\nUnited States of America"));
        assertTrue("testCompleteJCard5 - 30", jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("100 Waters Edge"));
        assertTrue("testCompleteJCard5 - 31", jsCard.getAddresses().get("ADR-1").getLocality().equals("Baytown"));
        assertTrue("testCompleteJCard5 - 32", jsCard.getAddresses().get("ADR-1").getRegion().equals("LA"));
        assertTrue("testCompleteJCard5 - 33", jsCard.getAddresses().get("ADR-1").getCountry().equals("United States of America"));
        assertTrue("testCompleteJCard5 - 34", jsCard.getAddresses().get("ADR-1").getPostcode().equals("30314"));
        assertTrue("testCompleteJCard5 - 35", jsCard.getAddresses().get("ADR-2").asPrivate());
        assertTrue("testCompleteJCard5 - 36", jsCard.getAddresses().get("ADR-2").getFullAddress().equals("42 Plantation St.\nBaytown, LA 30314\nUnited States of America"));
        assertTrue("testCompleteJCard5 - 37", jsCard.getAddresses().get("ADR-2").getStreetDetails().equals("42 Plantation St."));
        assertTrue("testCompleteJCard5 - 38", jsCard.getAddresses().get("ADR-2").getLocality().equals("Baytown"));
        assertTrue("testCompleteJCard5 - 39", jsCard.getAddresses().get("ADR-2").getRegion().equals("LA"));
        assertTrue("testCompleteJCard5 - 40", jsCard.getAddresses().get("ADR-2").getCountry().equals("United States of America"));
        assertTrue("testCompleteJCard5 - 41", jsCard.getAddresses().get("ADR-2").getPostcode().equals("30314"));

        assertTrue("testCompleteJCard5 - 42", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteJCard5 - 43", jsCard.getEmails().get("EMAIL-1").getEmail().equals("forrestgump@example.com"));
        assertTrue("testCompleteJCard5 - 44", jsCard.getUpdated().compareTo(DateUtils.toCalendar("2008-04-24T19:52:43Z"))==0);
        assertTrue("testCompleteJCard5 - 45", StringUtils.isNotEmpty(jsCard.getUid()));
    }

}
