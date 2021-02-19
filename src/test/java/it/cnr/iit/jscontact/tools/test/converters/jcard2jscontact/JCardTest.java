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
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import it.cnr.iit.jscontact.tools.dto.*;
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
        jCard2JSContact.convert(jcard).get(0);
    }

    //jCard is an empty array
    @Test(expected = CardException.class)
    public void testJCardInvalid2() throws IOException, CardException {

        String jcard="[]";
        jCard2JSContact.convert(jcard).get(0);
    }

    //jCard array includes only one item
    @Test(expected = RuntimeException.class)
    public void testJCardInvalid3() throws IOException, CardException {

        String jcard="[\"vcard\"]";
        jCard2JSContact.convert(jcard).get(0);
    }

        //jCard array 2nd item is empty
        @Test(expected = CardException.class)
        public void testJCardInvalid4() throws IOException, CardException {

            String jcard="[\"vcard\",[]]";
            jCard2JSContact.convert(jcard).get(0);
        }

        //jCard does not include version
//        @Test(expected = JCardException.class)
        public void testJCardInvalid5() throws IOException, CardException {

            String jcard="[\"vcard\",[ [\"fn\", {}, \"text\", \"test\"]]]";
            jCard2JSContact.convert(jcard).get(0);
        }

    //jCard does not include fn
    @Test(expected = CardException.class)
    public void testJCardInvalid6() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard).get(0);
    }

    //version is not 1st property
//    @Test(expected = JCardException.class)
    public void testJCardInvalid7() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"fn\", {}, \"text\", \"test\"], [\"version\", {}, \"text\", \"4.0\"]]]";
        jCard2JSContact.convert(jcard).get(0);
    }

    //version param object appears as null instead of empty object
    @Test(expected = RuntimeException.class)
    public void testJCardInvalid8() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", null, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard).get(0);
    }


    //jCard array 1st is not vcard
    @Test(expected = CardException.class)
    public void testJCardInvalid10() throws IOException, CardException {

        String jcard="[\"jcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
    }

    //jCard includes an uppercase property name
//    @Test(expected = JCardException.class)
    public void testJCardInvalid11() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"VERSION\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
    }

    //jCard includes an uppercase param name
//    @Test(expected = JCardException.class)
    public void testJCardInvalid12() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {\"ALTID\": 1}, \"text\", \"test\"]]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
    }

    //a jCard property includes the value parameter
    @Test(expected = CardException.class)
    public void testJCardInvalid15() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", { \"value\": \"a value\" }, \"text\", \"test\"]]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
    }

    //pref must be between 1 and 100
    @Test(expected = CardException.class)
    public void testJCardInvalid16() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {\"pref\":\"0\"}, \"text\", \"test\"]]]";
        jCard2JSContact.convert(jcard).get(0);
    }

    @Test
    public void testJCardValid() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"]]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testJCardValid - 1",jsCard != null);
        assertTrue("testJCardValid - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertTrue("testJCardValid - 3",jsCard.getFullName().getValue().equals("test"));

    }

    @Test
    public void testExtendedJCardValid() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"], [\"myext\", {}, \"text\", \"extvalue\"]]]";
        JCard2JSContact jCard2JSContact = JCard2JSContact.builder()
                .config(VCard2JSContactConfig.builder()
                        .extensionsPrefix("extension/")
                        .build()
                )
                .build();
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testExtendedJCardValid - 1",jsCard != null);
        assertTrue("testExtendedJCardValid - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertTrue("testExtendedJCardValid - 3",jsCard.getFullName().getValue().equals("test"));
        assertTrue("testExtendedJCardValid - 4",jsCard.getExtensions().size() == 1);
        assertTrue("testExtendedJCardValid - 5",jsCard.getExtensions().get("extension/myext").equals("extvalue"));
    }


    @Test
    public void testCompleteJCard1() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7483.json"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard1 - 1", jsCard.getFullName().getValue().equals("Joe User"));
        assertTrue("testCompleteJCard1 - 2", jsCard.getKind().isIndividual());
        assertTrue("testCompleteJCard1 - 3", jsCard.getName().length == 4);
        assertTrue("testCompleteJCard1 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteJCard1 - 5", jsCard.getName()[0].getValue().equals("User"));
        assertTrue("testCompleteJCard1 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteJCard1 - 7", jsCard.getName()[1].getValue().equals("Joe"));
        assertTrue("testCompleteJCard1 - 8", jsCard.getName()[2].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteJCard1 - 9", jsCard.getName()[2].getValue().equals("ing. jr"));
        assertTrue("testCompleteJCard1 - 10", jsCard.getName()[3].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteJCard1 - 11", jsCard.getName()[3].getValue().equals("M.Sc."));
        assertTrue("testCompleteJCard1 - 12", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteJCard1 - 13", jsCard.getPreferredContactLanguages().get("fr")[0].getPreference() == 1);
        assertTrue("testCompleteJCard1 - 14", jsCard.getPreferredContactLanguages().get("en")[0].getPreference() == 2);
        assertTrue("testCompleteJCard1 - 15", jsCard.getOrganizations()[0].getValue().equals("Example"));
        assertTrue("testCompleteJCard1 - 16", jsCard.getJobTitles()[0].getValue().equals("Research Scientist"));
        assertTrue("testCompleteJCard1 - 17", jsCard.getRoles()[0].getValue().equals("Project Lead"));
        assertTrue("testCompleteJCard1 - 18", jsCard.getAddresses().length == 2);
        assertTrue("testCompleteJCard1 - 19", jsCard.getAddresses()[0].getFullAddress().getValue().equals("Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada"));
        assertTrue("testCompleteJCard1 - 20", jsCard.getAddresses()[0].getExtension().equals("Suite 1234"));
        assertTrue("testCompleteJCard1 - 21", jsCard.getAddresses()[0].getStreet().equals("4321 Rue Somewhere"));
        assertTrue("testCompleteJCard1 - 22", jsCard.getAddresses()[0].getLocality().equals("Quebec"));
        assertTrue("testCompleteJCard1 - 23", jsCard.getAddresses()[0].getRegion().equals("QC"));
        assertTrue("testCompleteJCard1 - 24", jsCard.getAddresses()[0].getCountry().equals("Canada"));
        assertTrue("testCompleteJCard1 - 25", jsCard.getAddresses()[0].getPostcode().equals("G1V 2M2"));
        assertTrue("testCompleteJCard1 - 26", jsCard.getAddresses()[0].getCoordinates().equals("geo:46.772673,-71.282945"));
        assertTrue("testCompleteJCard1 - 27", jsCard.getAddresses()[0].getTimeZone().equals("Etc/GMT+5"));
        assertTrue("testCompleteJCard1 - 28", jsCard.getAddresses()[1].getFullAddress().getValue().equals("123 Maple Ave\nSuite 90001\nVancouver\nBC\n1239\n"));
        assertTrue("testCompleteJCard1 - 29", jsCard.getEmails().length == 1);
        assertTrue("testCompleteJCard1 - 30", jsCard.getEmails()[0].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard1 - 31", jsCard.getEmails()[0].getValue().equals("joe.user@example.com"));
        assertTrue("testCompleteJCard1 - 32", jsCard.getPhones().length == 2);
        assertTrue("testCompleteJCard1 - 33", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteJCard1 - 34", jsCard.getPhones()[0].getValue().equals("tel:+1-555-555-1234;ext=102"));
        assertTrue("testCompleteJCard1 - 35", jsCard.getPhones()[0].getIsPreferred() == Boolean.TRUE);
        assertTrue("testCompleteJCard1 - 36", jsCard.getPhones()[0].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard1 - 37", jsCard.getPhones()[0].getLabels() == null);
        assertTrue("testCompleteJCard1 - 38", jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteJCard1 - 39", jsCard.getPhones()[1].getValue().equals("tel:+1-555-555-4321"));
        assertTrue("testCompleteJCard1 - 40", jsCard.getPhones()[1].getIsPreferred() == null);
        assertTrue("testCompleteJCard1 - 41", jsCard.getPhones()[1].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard1 - 42", jsCard.getPhones()[1].getLabels().size() == 3);
        assertTrue("testCompleteJCard1 - 43", jsCard.getPhones()[1].getLabels().get("video") == Boolean.TRUE);
        assertTrue("testCompleteJCard1 - 44", jsCard.getPhones()[1].getLabels().get("text") == Boolean.TRUE);
        assertTrue("testCompleteJCard1 - 45", jsCard.getPhones()[1].getLabels().get("cell") == Boolean.TRUE);
        assertTrue("testCompleteJCard1 - 46", jsCard.getOnline().length == 2);
        Resource[] keys = jsCard.getOnlineKey();
        assertTrue("testCompleteJCard1 - 47", keys[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteJCard1 - 48", keys[0].getValue().equals("http://www.example.com/joe.user/joe.asc"));
        assertTrue("testCompleteJCard1 - 49", keys[0].getIsPreferred() == null);
        assertTrue("testCompleteJCard1 - 50", keys[0].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard1 - 51", keys[0].getLabels().size() == 1);
        assertTrue("testCompleteJCard1 - 52", keys[0].getLabels().get(LabelKey.KEY.getValue()) == Boolean.TRUE);
        Resource[] urls = jsCard.getOnlineUrl();
        assertTrue("testCompleteJCard1 - 53", urls[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteJCard1 - 54", urls[0].getValue().equals("http://example.org"));
        assertTrue("testCompleteJCard1 - 55", urls[0].getIsPreferred() == null);
        assertTrue("testCompleteJCard1 - 56", urls[0].getContext() == ResourceContext.PRIVATE);
        assertTrue("testCompleteJCard1 - 57", urls[0].getLabels().size() == 1);
        assertTrue("testCompleteJCard1 - 58", urls[0].getLabels().get(LabelKey.URL.getValue()) == Boolean.TRUE);
        assertTrue("testCompleteJCard1 - 59", StringUtils.isNotEmpty(jsCard.getUid()));

    }

    @Test
    public void testCompleteJCard2() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Multilingual.json"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard2 - 1", jsCard.getFullName().getValue().equals("大久保 正仁"));
        assertTrue("testCompleteJCard2 - 2", jsCard.getFullName().getLanguage().equals("ja"));
        assertTrue("testCompleteJCard2 - 3", jsCard.getFullName().getLocalizations().get("en").equals("Okubo Masahito"));
        assertTrue("testCompleteJCard2 - 4", jsCard.getKind().isIndividual());
        assertTrue("testCompleteJCard2 - 5", jsCard.getJobTitles().length == 1);
        assertTrue("testCompleteJCard2 - 6", jsCard.getJobTitles()[0].getValue().equals("事務局長"));
        assertTrue("testCompleteJCard2 - 7", jsCard.getJobTitles()[0].getLanguage().equals("ja"));
        assertTrue("testCompleteJCard2 - 8", jsCard.getJobTitles()[0].getLocalizations().get("en").equals("Secretary General"));
        assertTrue("testCompleteJCard2 - 9", jsCard.getKind().isIndividual());
        assertTrue("testCompleteJCard2 - 10", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteJCard2 - 11", jsCard.getPreferredContactLanguages().get("ja")[0].getPreference() == 1);
        assertTrue("testCompleteJCard2 - 12", jsCard.getPreferredContactLanguages().get("en")[0].getPreference() == 2);
        assertTrue("testCompleteJCard2 - 13", StringUtils.isNotEmpty(jsCard.getUid()));
    }

    @Test
    public void testCompleteJCard3() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Unstructured.json"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard3 - 1", jsCard.getFullName().getValue().equals("台灣固網股份有限公司"));
        assertTrue("testCompleteJCard3 - 2", jsCard.getFullName().getLanguage().equals("zh-Hant-TW"));
        assertTrue("testCompleteJCard3 - 3", jsCard.getFullName().getLocalizations().get("en").equals("Taiwan Fixed Network CO.,LTD."));
        assertTrue("testCompleteJCard3 - 4", jsCard.getKind().isOrg());
        assertTrue("testCompleteJCard3 - 5", jsCard.getAddresses().length == 1);
        assertTrue("testCompleteJCard3 - 6", jsCard.getAddresses()[0].getFullAddress().getValue().equals("8F., No.172-1, Sec.2, Ji-Lung Rd,"));
        assertTrue("testCompleteJCard3 - 7", jsCard.getEmails().length == 1);
        assertTrue("testCompleteJCard3 - 8", jsCard.getEmails()[0].getContext() == null);
        assertTrue("testCompleteJCard3 - 9", jsCard.getEmails()[0].getValue().isEmpty());
        assertTrue("testCompleteJCard3 - 10", jsCard.getPhones().length == 2);
        assertTrue("testCompleteJCard3 - 11", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteJCard3 - 12", jsCard.getPhones()[0].getContext() == null);
        assertTrue("testCompleteJCard3 - 13", jsCard.getPhones()[0].getValue().isEmpty());
        assertTrue("testCompleteJCard3 - 14", jsCard.getPhones()[1].getType().equals(PhoneResourceType.FAX.getValue()));
        assertTrue("testCompleteJCard3 - 15", jsCard.getPhones()[1].getContext() == null);
        assertTrue("testCompleteJCard3 - 16", jsCard.getPhones()[1].getValue().isEmpty());
        assertTrue("testCompleteJCard3 - 17", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteJCard4() throws IOException, CardException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7095.json"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) jCard2JSContact.convert(json).get(0);
        assertTrue("testCompleteJCard4 - 1", jsCard.getFullName().getValue().equals("Simon Perreault"));
        assertTrue("testCompleteJCard4 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteJCard4 - 3", jsCard.getName().length == 4);
        assertTrue("testCompleteJCard4 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteJCard4 - 5", jsCard.getName()[0].getValue().equals("Perreault"));
        assertTrue("testCompleteJCard4 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteJCard4 - 7", jsCard.getName()[1].getValue().equals("Simon"));
        assertTrue("testCompleteJCard4 - 8", jsCard.getName()[2].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteJCard4 - 9", jsCard.getName()[2].getValue().equals("ing. jr"));
        assertTrue("testCompleteJCard4 - 10", jsCard.getName()[3].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteJCard4 - 11", jsCard.getName()[3].getValue().equals("M.Sc."));
        assertTrue("testCompleteJCard4 - 12", jsCard.getAnniversaries().length==2);
        assertTrue("testCompleteJCard4 - 13", jsCard.getAnniversaries()[0].getType() == AnniversaryType.BIRTH);
        assertTrue("testCompleteJCard4 - 14", jsCard.getAnniversaries()[0].getDate().equals("--02-03"));
        assertTrue("testCompleteJCard4 - 15", jsCard.getAnniversaries()[1].getType() == AnniversaryType.OTHER);
        assertTrue("testCompleteJCard4 - 16", jsCard.getAnniversaries()[1].getLabel().equals("marriage date"));
        assertTrue("testCompleteJCard4 - 17", jsCard.getAnniversaries()[1].getDate().equals("2009-08-08T14:30:00-05:00"));
        assertTrue("testCompleteJCard4 - 18", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteJCard4 - 19", jsCard.getPreferredContactLanguages().get("fr")[0].getPreference() == 1);
        assertTrue("testCompleteJCard4 - 20", jsCard.getPreferredContactLanguages().get("en")[0].getPreference() == 2);
        assertTrue("testCompleteJCard4 - 21", jsCard.getOrganizations()[0].getValue().equals("Viagenie"));
        assertTrue("testCompleteJCard4 - 22", jsCard.getAddresses().length == 1);
        assertTrue("testCompleteJCard4 - 23", jsCard.getAddresses()[0].getFullAddress().getValue().equals("Suite D2-630\n2875 Laurier\nQuebec\nQC\nG1V 2M2\nCanada"));
        assertTrue("testCompleteJCard4 - 24", jsCard.getAddresses()[0].getExtension().equals("Suite D2-630"));
        assertTrue("testCompleteJCard4 - 25", jsCard.getAddresses()[0].getStreet().equals("2875 Laurier"));
        assertTrue("testCompleteJCard4 - 26", jsCard.getAddresses()[0].getLocality().equals("Quebec"));
        assertTrue("testCompleteJCard4 - 27", jsCard.getAddresses()[0].getRegion().equals("QC"));
        assertTrue("testCompleteJCard4 - 28", jsCard.getAddresses()[0].getCountry().equals("Canada"));
        assertTrue("testCompleteJCard4 - 29", jsCard.getAddresses()[0].getPostcode().equals("G1V 2M2"));
        assertTrue("testCompleteJCard4 - 30", jsCard.getAddresses()[0].getCoordinates().equals("geo:46.772673,-71.282945"));
        assertTrue("testCompleteJCard4 - 31", jsCard.getAddresses()[0].getTimeZone().equals("Etc/GMT+5"));
        assertTrue("testCompleteJCard4 - 32", jsCard.getPhones().length == 2);
        assertTrue("testCompleteJCard4 - 33", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteJCard4 - 34", jsCard.getPhones()[0].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard4 - 35", jsCard.getPhones()[0].getValue().equals("tel:+1-418-656-9254;ext=102"));
        assertTrue("testCompleteJCard4 - 36", jsCard.getPhones()[0].getIsPreferred() == Boolean.TRUE);
        assertTrue("testCompleteJCard4 - 37", jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteJCard4 - 38", jsCard.getPhones()[1].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard4 - 39", jsCard.getPhones()[1].getValue().equals("tel:+1-418-262-6501"));
        assertTrue("testCompleteJCard4 - 40", jsCard.getPhones()[1].getLabels().size() == 3);
        assertTrue("testCompleteJCard4 - 41", jsCard.getPhones()[1].getLabels().get("cell") == Boolean.TRUE);
        assertTrue("testCompleteJCard4 - 42", jsCard.getPhones()[1].getLabels().get("video") == Boolean.TRUE);
        assertTrue("testCompleteJCard4 - 43", jsCard.getPhones()[1].getLabels().get("text") == Boolean.TRUE);
        assertTrue("testCompleteJCard4 - 44", jsCard.getEmails().length == 1);
        assertTrue("testCompleteJCard4 - 45", jsCard.getEmails()[0].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard4 - 46", jsCard.getEmails()[0].getValue().equals("simon.perreault@viagenie.ca"));
        assertTrue("testCompleteJCard4 - 47", jsCard.getOnline().length == 2);
        Resource[] keys = jsCard.getOnlineKey();
        assertTrue("testCompleteJCard4 - 48", keys[0].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard4 - 49", keys[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteJCard4 - 50", keys[0].getLabels().get(LabelKey.KEY.getValue()) == Boolean.TRUE);
        assertTrue("testCompleteJCard4 - 51", keys[0].getValue().equals("http://www.viagenie.ca/simon.perreault/simon.asc"));
        Resource[] urls = jsCard.getOnlineUrl();
        assertTrue("testCompleteJCard4 - 52", urls[0].getContext() == ResourceContext.PRIVATE);
        assertTrue("testCompleteJCard4 - 53", urls[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteJCard4 - 54", urls[0].getLabels().get("url") == Boolean.TRUE);
        assertTrue("testCompleteJCard4 - 55", urls[0].getValue().equals("http://nomis80.org"));
        assertTrue("testCompleteJCard4 - 56", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteJCard5() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-Wikipedia.json"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) jCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteJCard5 - 1", jsCard.getFullName().getValue().equals("Forrest Gump"));
        assertTrue("testCompleteJCard5 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteJCard5 - 3", jsCard.getName().length == 3);
        assertTrue("testCompleteJCard5 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteJCard5 - 5", jsCard.getName()[0].getValue().equals("Gump"));
        assertTrue("testCompleteJCard5 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteJCard5 - 7", jsCard.getName()[1].getValue().equals("Forrest"));
        assertTrue("testCompleteJCard5 - 8", jsCard.getName()[2].getType() == NameComponentType.PREFIX);
        assertTrue("testCompleteJCard5 - 9", jsCard.getName()[2].getValue().equals("Mr."));
        assertTrue("testCompleteJCard5 - 10", jsCard.getOrganizations()[0].getValue().equals("Bubba Gump Shrimp Co."));
        assertTrue("testCompleteJCard5 - 11", jsCard.getJobTitles()[0].getValue().equals("Shrimp Man"));
        File[] photos = jsCard.getPhotos();
        assertTrue("testCompleteJCard5 - 15", photos[0].getHref().equals("http://www.example.com/dir_photos/my_photo.gif"));
        assertTrue("testCompleteJCard5 - 16", photos[0].getMediaType().equals("image/gif"));
        assertTrue("testCompleteJCard5 - 17", jsCard.getPhones().length == 2);
        assertTrue("testCompleteJCard5 - 18", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteJCard5 - 19", jsCard.getPhones()[0].getContext() == ResourceContext.WORK);
        assertTrue("testCompleteJCard5 - 20", jsCard.getPhones()[0].getValue().equals("tel:+1-111-555-1212"));
        assertTrue("testCompleteJCard5 - 21", jsCard.getPhones()[0].getLabels() == null);
        assertTrue("testCompleteJCard5 - 22", jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteJCard5 - 23", jsCard.getPhones()[1].getContext() == ResourceContext.PRIVATE);
        assertTrue("testCompleteJCard5 - 24", jsCard.getPhones()[1].getValue().equals("tel:+1-404-555-1212"));
        assertTrue("testCompleteJCard5 - 25", jsCard.getPhones()[1].getLabels() == null);

        assertTrue("testCompleteJCard5 - 26", jsCard.getAddresses().length == 2);
        assertTrue("testCompleteJCard5 - 27", jsCard.getAddresses()[0].getContext() == AddressContext.WORK);
        assertTrue("testCompleteJCard5 - 28", jsCard.getAddresses()[0].getIsPreferred() == Boolean.TRUE);
        assertTrue("testCompleteJCard5 - 29", jsCard.getAddresses()[0].getFullAddress().getValue().equals("100 Waters Edge\nBaytown, LA 30314\nUnited States of America"));
        assertTrue("testCompleteJCard5 - 30", jsCard.getAddresses()[0].getStreet().equals("100 Waters Edge"));
        assertTrue("testCompleteJCard5 - 31", jsCard.getAddresses()[0].getLocality().equals("Baytown"));
        assertTrue("testCompleteJCard5 - 32", jsCard.getAddresses()[0].getRegion().equals("LA"));
        assertTrue("testCompleteJCard5 - 33", jsCard.getAddresses()[0].getCountry().equals("United States of America"));
        assertTrue("testCompleteJCard5 - 34", jsCard.getAddresses()[0].getPostcode().equals("30314"));
        assertTrue("testCompleteJCard5 - 35", jsCard.getAddresses()[1].getContext() == AddressContext.PRIVATE);
        assertTrue("testCompleteJCard5 - 36", jsCard.getAddresses()[1].getFullAddress().getValue().equals("42 Plantation St.\nBaytown, LA 30314\nUnited States of America"));
        assertTrue("testCompleteJCard5 - 37", jsCard.getAddresses()[1].getStreet().equals("42 Plantation St."));
        assertTrue("testCompleteJCard5 - 38", jsCard.getAddresses()[1].getLocality().equals("Baytown"));
        assertTrue("testCompleteJCard5 - 39", jsCard.getAddresses()[1].getRegion().equals("LA"));
        assertTrue("testCompleteJCard5 - 40", jsCard.getAddresses()[1].getCountry().equals("United States of America"));
        assertTrue("testCompleteJCard5 - 41", jsCard.getAddresses()[1].getPostcode().equals("30314"));

        assertTrue("testCompleteJCard5 - 42", jsCard.getEmails().length == 1);
        assertTrue("testCompleteJCard5 - 43", jsCard.getEmails()[0].getValue().equals("forrestgump@example.com"));
        assertTrue("testCompleteJCard5 - 44", jsCard.getUpdated().equals("2008-04-24T19:52:43Z"));
        assertTrue("testCompleteJCard5 - 45", StringUtils.isNotEmpty(jsCard.getUid()));
    }

}
