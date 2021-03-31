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

import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class VCardTest extends VCard2JSContactTest {

        //vCard does not include version
//        @Test(expected = JCardException.class)
        public void testVCardInvalid5() throws IOException, CardException {

            String vcard = "BEGIN:VCARD\n" +
                    "FN:test\n" +
                    "END:VCARD";

            vCard2JSContact.convert(vcard).get(0);
        }

    //vCard does not include fn
    @Test(expected = CardException.class)
    public void testVCardInvalid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:test\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard).get(0);
    }

    //version is not 1st property
//    @Test(expected = JCardException.class)
    public void testVCardInvalid7() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "FN:test\n" +
                "VERSION:test\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard).get(0);
    }

    //PREF must be between 1 and 100
    @Test(expected = CardException.class)
    public void testVCardInvalid8() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:test\n" +
                "FN;PREF=0:test\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard).get(0);
    }


    @Test
    public void testVCardValid() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testVCardValid - 1",jsCard != null);
        assertTrue("testVCardValid - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertTrue("testVCardValid - 3",jsCard.getFullName().getValue().equals("test"));

    }

    @Test
    public void testExtendedVCardValid() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "myext:extvalue\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder()
                .config(VCard2JSContactConfig.builder()
                        .extensionsPrefix("extension/")
                        .build()
                )
                .build();
        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testExtendedVCardValid - 1",jsCard != null);
        assertTrue("testExtendedVCardValid - 2", StringUtils.isNotEmpty(jsCard.getUid()));
        assertTrue("testExtendedVCardValid - 3",jsCard.getFullName().getValue().equals("test"));
        assertTrue("testExtendedVCardValid - 4",jsCard.getExtensions().size() == 1);
        assertTrue("testExtendedVCardValid - 5",jsCard.getExtensions().get("extension/myext").equals("extvalue"));
    }


    @Test
    public void testCompleteVCard1() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("vcard/vCard-RFC7483.vcf"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteVCard1 - 1", jsCard.getFullName().getValue().equals("Joe User"));
        assertTrue("testCompleteVCard1 - 2", jsCard.getKind().isIndividual());
        assertTrue("testCompleteVCard1 - 3", jsCard.getName().length == 4);
        assertTrue("testCompleteVCard1 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteVCard1 - 5", jsCard.getName()[0].getValue().equals("User"));
        assertTrue("testCompleteVCard1 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteVCard1 - 7", jsCard.getName()[1].getValue().equals("Joe"));
        assertTrue("testCompleteVCard1 - 8", jsCard.getName()[2].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteVCard1 - 9", jsCard.getName()[2].getValue().equals("ing. jr"));
        assertTrue("testCompleteVCard1 - 10", jsCard.getName()[3].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteVCard1 - 11", jsCard.getName()[3].getValue().equals("M.Sc."));
        assertTrue("testCompleteVCard1 - 12", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteVCard1 - 13", jsCard.getPreferredContactLanguages().get("fr")[0].getPreference() == 1);
        assertTrue("testCompleteVCard1 - 14", jsCard.getPreferredContactLanguages().get("en")[0].getPreference() == 2);
        assertTrue("testCompleteVCard1 - 15", jsCard.getOrganizations().get("ORG-1").getName().getValue().equals("Example"));
        assertTrue("testCompleteVCard1 - 16", jsCard.getJobTitles().get("TITLE-1").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testCompleteVCard1 - 17", jsCard.getRoles()[0].getValue().equals("Project Lead"));
        assertTrue("testCompleteVCard1 - 18", jsCard.getAddresses().size() == 2);
        assertTrue("testCompleteVCard1 - 19", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada"));
        assertTrue("testCompleteVCard1 - 20", jsCard.getAddresses().get("ADR-1").getExtension().equals("Suite 1234"));
        assertTrue("testCompleteVCard1 - 21", jsCard.getAddresses().get("ADR-1").getStreet().equals("4321 Rue Somewhere"));
        assertTrue("testCompleteVCard1 - 22", jsCard.getAddresses().get("ADR-1").getLocality().equals("Quebec"));
        assertTrue("testCompleteVCard1 - 23", jsCard.getAddresses().get("ADR-1").getRegion().equals("QC"));
        assertTrue("testCompleteVCard1 - 24", jsCard.getAddresses().get("ADR-1").getCountry().equals("Canada"));
        assertTrue("testCompleteVCard1 - 25", jsCard.getAddresses().get("ADR-1").getPostcode().equals("G1V 2M2"));
        assertTrue("testCompleteVCard1 - 26", jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));
        assertTrue("testCompleteVCard1 - 27", jsCard.getAddresses().get("ADR-1").getTimeZone().equals("Etc/GMT+5"));
 //       assertTrue("testCompleteVCard1 - 28", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("123 Maple Ave\nSuite 90001\nVancouver\nBC\n1239"));
        assertTrue("testCompleteVCard1 - 29", jsCard.getEmails().length == 1);
        assertTrue("testCompleteVCard1 - 30", jsCard.getEmails()[0].getContext() == Context.WORK);
        assertTrue("testCompleteVCard1 - 31", jsCard.getEmails()[0].getValue().equals("joe.user@example.com"));
        assertTrue("testCompleteVCard1 - 32", jsCard.getPhones().length == 2);
        assertTrue("testCompleteVCard1 - 33", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard1 - 34", jsCard.getPhones()[0].getValue().equals("tel:+1-555-555-1234;ext=102"));
        assertTrue("testCompleteVCard1 - 35", jsCard.getPhones()[0].getPref() == 1);
        assertTrue("testCompleteVCard1 - 36", jsCard.getPhones()[0].getContext() == Context.WORK);
        assertTrue("testCompleteVCard1 - 37", jsCard.getPhones()[0].getLabels() == null);
        assertTrue("testCompleteVCard1 - 38", jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard1 - 39", jsCard.getPhones()[1].getValue().equals("tel:+1-555-555-4321"));
        assertTrue("testCompleteVCard1 - 40", jsCard.getPhones()[1].getPref() == null);
        assertTrue("testCompleteVCard1 - 41", jsCard.getPhones()[1].getContext() == Context.WORK);
        assertTrue("testCompleteVCard1 - 42", jsCard.getPhones()[1].getLabels().size() == 3);
        assertTrue("testCompleteVCard1 - 43", jsCard.getPhones()[1].getLabels().get("video") == Boolean.TRUE);
        assertTrue("testCompleteVCard1 - 44", jsCard.getPhones()[1].getLabels().get("text") == Boolean.TRUE);
        assertTrue("testCompleteVCard1 - 45", jsCard.getPhones()[1].getLabels().get("cell") == Boolean.TRUE);
        assertTrue("testCompleteVCard1 - 46", jsCard.getOnline().length == 2);
        Resource[] keys = jsCard.getOnlineKey();
        assertTrue("testCompleteVCard1 - 47", keys[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard1 - 48", keys[0].getValue().equals("http://www.example.com/joe.user/joe.asc"));
        assertTrue("testCompleteVCard1 - 49", keys[0].getPref() == null);
        assertTrue("testCompleteVCard1 - 50", keys[0].getContext() == Context.WORK);
        assertTrue("testCompleteVCard1 - 51", keys[0].getLabels().size() == 1);
        assertTrue("testCompleteVCard1 - 52", keys[0].getLabels().get(LabelKey.KEY.getValue()) == Boolean.TRUE);
        Resource[] urls = jsCard.getOnlineUrl();
        assertTrue("testCompleteVCard1 - 53", urls[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard1 - 54", urls[0].getValue().equals("http://example.org"));
        assertTrue("testCompleteVCard1 - 55", urls[0].getPref() == null);
        assertTrue("testCompleteVCard1 - 56", urls[0].getContext() == Context.PRIVATE);
        assertTrue("testCompleteVCard1 - 57", urls[0].getLabels().size() == 1);
        assertTrue("testCompleteVCard1 - 58", urls[0].getLabels().get(LabelKey.URL.getValue()) == Boolean.TRUE);
        assertTrue("testCompleteVCard1 - 59", StringUtils.isNotEmpty(jsCard.getUid()));

    }

    @Test
    public void testCompleteVCard2() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Multilingual.vcf"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteVCard2 - 1", jsCard.getFullName().getValue().equals("大久保 正仁"));
        assertTrue("testCompleteVCard2 - 2", jsCard.getFullName().getLanguage().equals("ja"));
        assertTrue("testCompleteVCard2 - 3", jsCard.getFullName().getLocalizations().get("en").equals("Okubo Masahito"));
        assertTrue("testCompleteVCard2 - 4", jsCard.getKind().isIndividual());
        assertTrue("testCompleteVCard2 - 5", jsCard.getJobTitles().size() == 1);
        assertTrue("testCompleteVCard2 - 6", jsCard.getJobTitles().get("TITLE-1").getTitle().getValue().equals("事務局長"));
        assertTrue("testCompleteVCard2 - 7", jsCard.getJobTitles().get("TITLE-1").getTitle().getLanguage().equals("ja"));
        assertTrue("testCompleteVCard2 - 8", jsCard.getJobTitles().get("TITLE-1").getTitle().getLocalizations().get("en").equals("Secretary General"));
        assertTrue("testCompleteVCard2 - 9", jsCard.getKind().isIndividual());
        assertTrue("testCompleteVCard2 - 10", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteVCard2 - 11", jsCard.getPreferredContactLanguages().get("ja")[0].getPreference() == 1);
        assertTrue("testCompleteVCard2 - 12", jsCard.getPreferredContactLanguages().get("en")[0].getPreference() == 2);
        assertTrue("testCompleteVCard2 - 13", StringUtils.isNotEmpty(jsCard.getUid()));
    }

    @Test
    public void testCompleteVCard3() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Unstructured.vcf"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteVCard3 - 1", jsCard.getFullName().getValue().equals("台灣固網股份有限公司"));
        assertTrue("testCompleteVCard3 - 2", jsCard.getFullName().getLanguage().equals("zh-Hant-TW"));
        assertTrue("testCompleteVCard3 - 3", jsCard.getFullName().getLocalizations().get("en").equals("Taiwan Fixed Network CO.,LTD."));
        assertTrue("testCompleteVCard3 - 4", jsCard.getKind().isOrg());
        assertTrue("testCompleteVCard3 - 5", jsCard.getAddresses().size() == 1);
        assertTrue("testCompleteVCard3 - 6", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("8F., No.172-1, Sec.2, Ji-Lung Rd,"));
        assertTrue("testCompleteVCard3 - 7", jsCard.getEmails().length == 1);
        assertTrue("testCompleteVCard3 - 8", jsCard.getEmails()[0].getContext() == null);
        assertTrue("testCompleteVCard3 - 9", jsCard.getEmails()[0].getValue().isEmpty());
        assertTrue("testCompleteVCard3 - 10", jsCard.getPhones().length == 2);
        assertTrue("testCompleteVCard3 - 11", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard3 - 12", jsCard.getPhones()[0].getContext() == null);
        assertTrue("testCompleteVCard3 - 13", jsCard.getPhones()[0].getValue().isEmpty());
        assertTrue("testCompleteVCard3 - 14", jsCard.getPhones()[1].getType().equals(PhoneResourceType.FAX.getValue()));
        assertTrue("testCompleteVCard3 - 15", jsCard.getPhones()[1].getContext() == null);
        assertTrue("testCompleteVCard3 - 16", jsCard.getPhones()[1].getValue().isEmpty());
        assertTrue("testCompleteVCard3 - 17", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteVCard4() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("vcard/vCard-RFC7095.vcf"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteVCard4 - 1", jsCard.getFullName().getValue().equals("Simon Perreault"));
        assertTrue("testCompleteVCard4 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteVCard4 - 3", jsCard.getName().length == 4);
        assertTrue("testCompleteVCard4 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteVCard4 - 5", jsCard.getName()[0].getValue().equals("Perreault"));
        assertTrue("testCompleteVCard4 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteVCard4 - 7", jsCard.getName()[1].getValue().equals("Simon"));
        assertTrue("testCompleteVCard4 - 8", jsCard.getName()[2].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteVCard4 - 9", jsCard.getName()[2].getValue().equals("ing. jr"));
        assertTrue("testCompleteVCard4 - 10", jsCard.getName()[3].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteVCard4 - 11", jsCard.getName()[3].getValue().equals("M.Sc."));
        assertTrue("testCompleteVCard4 - 12", jsCard.getAnniversaries().length==2);
        assertTrue("testCompleteVCard4 - 13", jsCard.getAnniversaries()[0].getType() == AnniversaryType.BIRTH);
        assertTrue("testCompleteVCard4 - 14", jsCard.getAnniversaries()[0].getDate().equals("--02-03"));
        assertTrue("testCompleteVCard4 - 15", jsCard.getAnniversaries()[1].getType() == AnniversaryType.OTHER);
        assertTrue("testCompleteVCard4 - 16", jsCard.getAnniversaries()[1].getLabel().equals("marriage date"));
        assertTrue("testCompleteVCard4 - 17", jsCard.getAnniversaries()[1].getDate().equals("2009-08-08T14:30-05:00"));
        assertTrue("testCompleteVCard4 - 18", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteVCard4 - 19", jsCard.getPreferredContactLanguages().get("fr")[0].getPreference() == 1);
        assertTrue("testCompleteVCard4 - 20", jsCard.getPreferredContactLanguages().get("en")[0].getPreference() == 2);
        assertTrue("testCompleteVCard4 - 21", jsCard.getOrganizations().get("ORG-1").getName().getValue().equals("Viagenie"));
        assertTrue("testCompleteVCard4 - 22", jsCard.getAddresses().size() == 1);
        assertTrue("testCompleteVCard4 - 23", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("Suite D2-630\n2875 Laurier\nQuebec\nQC\nG1V 2M2\nCanada"));
        assertTrue("testCompleteVCard4 - 24", jsCard.getAddresses().get("ADR-1").getExtension().equals("Suite D2-630"));
        assertTrue("testCompleteVCard4 - 25", jsCard.getAddresses().get("ADR-1").getStreet().equals("2875 Laurier"));
        assertTrue("testCompleteVCard4 - 26", jsCard.getAddresses().get("ADR-1").getLocality().equals("Quebec"));
        assertTrue("testCompleteVCard4 - 27", jsCard.getAddresses().get("ADR-1").getRegion().equals("QC"));
        assertTrue("testCompleteVCard4 - 28", jsCard.getAddresses().get("ADR-1").getCountry().equals("Canada"));
        assertTrue("testCompleteVCard4 - 29", jsCard.getAddresses().get("ADR-1").getPostcode().equals("G1V 2M2"));
        assertTrue("testCompleteVCard4 - 30", jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));
        assertTrue("testCompleteVCard4 - 31", jsCard.getAddresses().get("ADR-1").getTimeZone().equals("Etc/GMT+5"));
        assertTrue("testCompleteVCard4 - 32", jsCard.getPhones().length == 2);
        assertTrue("testCompleteVCard4 - 33", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard4 - 34", jsCard.getPhones()[0].getContext() == Context.WORK);
        assertTrue("testCompleteVCard4 - 35", jsCard.getPhones()[0].getValue().equals("tel:+1-418-656-9254;ext=102"));
        assertTrue("testCompleteVCard4 - 36", jsCard.getPhones()[0].getPref() == 1);
        assertTrue("testCompleteVCard4 - 37", jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard4 - 38", jsCard.getPhones()[1].getContext() == Context.WORK);
        assertTrue("testCompleteVCard4 - 39", jsCard.getPhones()[1].getValue().equals("tel:+1-418-262-6501"));
        assertTrue("testCompleteVCard4 - 40", jsCard.getPhones()[1].getLabels().size() == 3);
        assertTrue("testCompleteVCard4 - 41", jsCard.getPhones()[1].getLabels().get("cell") == Boolean.TRUE);
        assertTrue("testCompleteVCard4 - 42", jsCard.getPhones()[1].getLabels().get("video") == Boolean.TRUE);
        assertTrue("testCompleteVCard4 - 43", jsCard.getPhones()[1].getLabels().get("text") == Boolean.TRUE);
        assertTrue("testCompleteVCard4 - 44", jsCard.getEmails().length == 1);
        assertTrue("testCompleteVCard4 - 45", jsCard.getEmails()[0].getContext() == Context.WORK);
        assertTrue("testCompleteVCard4 - 46", jsCard.getEmails()[0].getValue().equals("simon.perreault@viagenie.ca"));
        assertTrue("testCompleteVCard4 - 47", jsCard.getOnline().length == 2);
        Resource[] keys = jsCard.getOnlineKey();
        assertTrue("testCompleteVCard4 - 48", keys[0].getContext() == Context.WORK);
        assertTrue("testCompleteVCard4 - 49", keys[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard4 - 50", keys[0].getLabels().get(LabelKey.KEY.getValue()) == Boolean.TRUE);
        assertTrue("testCompleteVCard4 - 51", keys[0].getValue().equals("http://www.viagenie.ca/simon.perreault/simon.asc"));
        Resource[] urls = jsCard.getOnlineUrl();
        assertTrue("testCompleteVCard4 - 52", urls[0].getContext() == Context.PRIVATE);
        assertTrue("testCompleteVCard4 - 53", urls[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard4 - 54", urls[0].getLabels().get("url") == Boolean.TRUE);
        assertTrue("testCompleteVCard4 - 55", urls[0].getValue().equals("http://nomis80.org"));
        assertTrue("testCompleteVCard4 - 56", StringUtils.isNotEmpty(jsCard.getUid()));
    }

    @Test
    public void testCompleteVCard5() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Wikipedia.vcf"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteVCard5 - 1", jsCard.getFullName().getValue().equals("Forrest Gump"));
        assertTrue("testCompleteVCard5 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteVCard5 - 3", jsCard.getName().length == 3);
        assertTrue("testCompleteVCard5 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteVCard5 - 5", jsCard.getName()[0].getValue().equals("Gump"));
        assertTrue("testCompleteVCard5 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteVCard5 - 7", jsCard.getName()[1].getValue().equals("Forrest"));
        assertTrue("testCompleteVCard5 - 8", jsCard.getName()[2].getType() == NameComponentType.PREFIX);
        assertTrue("testCompleteVCard5 - 9", jsCard.getName()[2].getValue().equals("Mr."));
        assertTrue("testCompleteVCard5 - 10", jsCard.getOrganizations().get("ORG-1").getName().getValue().equals("Bubba Gump Shrimp Co."));
        assertTrue("testCompleteVCard5 - 11", jsCard.getJobTitles().get("TITLE-1").getTitle().getValue().equals("Shrimp Man"));
        assertTrue("testCompleteVCard5 - 15", jsCard.getPhotos().get("PHOTO-1").getHref().equals("http://www.example.com/dir_photos/my_photo.gif"));
        assertTrue("testCompleteVCard5 - 16", jsCard.getPhotos().get("PHOTO-1").getMediaType().equals("image/gif"));
        assertTrue("testCompleteVCard5 - 17", jsCard.getPhones().length == 2);
        assertTrue("testCompleteVCard5 - 18", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 19", jsCard.getPhones()[0].getContext() == Context.WORK);
        assertTrue("testCompleteVCard5 - 20", jsCard.getPhones()[0].getValue().equals("tel:+1-111-555-1212"));
        assertTrue("testCompleteVCard5 - 21", jsCard.getPhones()[0].getLabels() == null);
        assertTrue("testCompleteVCard5 - 22", jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 23", jsCard.getPhones()[1].getContext() == Context.PRIVATE);
        assertTrue("testCompleteVCard5 - 24", jsCard.getPhones()[1].getValue().equals("tel:+1-404-555-1212"));
        assertTrue("testCompleteVCard5 - 25", jsCard.getPhones()[1].getLabels() == null);

        assertTrue("testCompleteVCard5 - 26", jsCard.getAddresses().size() == 2);
        assertTrue("testCompleteVCard5 - 27", jsCard.getAddresses().get("ADR-1").getContext() == AddressContext.WORK);
        assertTrue("testCompleteVCard5 - 28", jsCard.getAddresses().get("ADR-1").getPref() == 1);
//        assertTrue("testCompleteVCard5 - 29", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("100 Waters Edge\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertTrue("testCompleteVCard5 - 30", jsCard.getAddresses().get("ADR-1").getStreet().equals("100 Waters Edge"));
        assertTrue("testCompleteVCard5 - 31", jsCard.getAddresses().get("ADR-1").getLocality().equals("Baytown"));
        assertTrue("testCompleteVCard5 - 32", jsCard.getAddresses().get("ADR-1").getRegion().equals("LA"));
        assertTrue("testCompleteVCard5 - 33", jsCard.getAddresses().get("ADR-1").getCountry().equals("United States of America"));
        assertTrue("testCompleteVCard5 - 34", jsCard.getAddresses().get("ADR-1").getPostcode().equals("30314"));
        assertTrue("testCompleteVCard5 - 35", jsCard.getAddresses().get("ADR-2").getContext() == AddressContext.PRIVATE);
//        assertTrue("testCompleteVCard5 - 36", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("42 Plantation St.\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertTrue("testCompleteVCard5 - 37", jsCard.getAddresses().get("ADR-2").getStreet().equals("42 Plantation St."));
        assertTrue("testCompleteVCard5 - 38", jsCard.getAddresses().get("ADR-2").getLocality().equals("Baytown"));
        assertTrue("testCompleteVCard5 - 39", jsCard.getAddresses().get("ADR-2").getRegion().equals("LA"));
        assertTrue("testCompleteVCard5 - 40", jsCard.getAddresses().get("ADR-2").getCountry().equals("United States of America"));
        assertTrue("testCompleteVCard5 - 41", jsCard.getAddresses().get("ADR-2").getPostcode().equals("30314"));

        assertTrue("testCompleteVCard5 - 42", jsCard.getEmails().length == 1);
        assertTrue("testCompleteVCard5 - 43", jsCard.getEmails()[0].getValue().equals("forrestgump@example.com"));
        assertTrue("testCompleteVCard5 - 44", jsCard.getUpdated().compareTo(VCardDateFormat.parseAsCalendar("2008-04-24T19:52:43Z"))==0);
        assertTrue("testCompleteVCard5 - 45", jsCard.getExtensions().size() == 1);
        assertTrue("testCompleteVCard5 - 46", jsCard.getExtensions().get("extension/x-qq").equals("21588891"));
        assertTrue("testCompleteVCard5 - 47", StringUtils.isNotEmpty(jsCard.getUid()));
    }


    @Test
    public void testCompleteVCard6() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("vcard/vCard-ezvcard-fullcontact.vcf"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteVCard6 - 1", jsCard.getFullName().getValue().equals("Prefix FirstName MiddleName LastName Suffix"));
        assertTrue("testCompleteVCard6 - 2", jsCard.getName().length == 5);
        assertTrue("testCompleteVCard6 - 3", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteVCard6 - 4", jsCard.getName()[0].getValue().equals("LastName"));
        assertTrue("testCompleteVCard6 - 5", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteVCard6 - 6", jsCard.getName()[1].getValue().equals("FirstName"));
        assertTrue("testCompleteVCard6 - 7", jsCard.getName()[2].getType() == NameComponentType.ADDITIONAL);
        assertTrue("testCompleteVCard6 - 8", jsCard.getName()[2].getValue().equals("MiddleName"));
        assertTrue("testCompleteVCard6 - 9", jsCard.getName()[3].getType() == NameComponentType.PREFIX);
        assertTrue("testCompleteVCard6 - 10", jsCard.getName()[3].getValue().equals("Prefix"));
        assertTrue("testCompleteVCard6 - 11", jsCard.getName()[4].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteVCard6 - 12", jsCard.getName()[4].getValue().equals("Suffix"));
        assertTrue("testCompleteVCard6 - 13", jsCard.getNickNames().length == 1);
        assertTrue("testCompleteVCard6 - 14", jsCard.getNickNames()[0].getValue().equals("NickName"));
        assertTrue("testCompleteVCard5 - 15", jsCard.getPhones().length == 9);
        assertTrue("testCompleteVCard5 - 16", jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 17", jsCard.getPhones()[0].getContext() == Context.PRIVATE);
        assertTrue("testCompleteVCard5 - 18", jsCard.getPhones()[0].getValue().equals("555-555-1111"));
        assertTrue("testCompleteVCard5 - 19", jsCard.getPhones()[0].getLabels() == null);
        assertTrue("testCompleteVCard5 - 20", jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 21", jsCard.getPhones()[1].getContext() == Context.WORK);
        assertTrue("testCompleteVCard5 - 22", jsCard.getPhones()[1].getValue().equals("555-555-1112"));
        assertTrue("testCompleteVCard5 - 23", jsCard.getPhones()[1].getLabels() == null);
        assertTrue("testCompleteVCard5 - 24", jsCard.getPhones()[2].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 25", jsCard.getPhones()[2].getContext() == null);
        assertTrue("testCompleteVCard5 - 26", jsCard.getPhones()[2].getValue().equals("555-555-1113"));
        assertTrue("testCompleteVCard5 - 27", jsCard.getPhones()[2].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 28", jsCard.getPhones()[2].getLabels().get("cell") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 29", jsCard.getPhones()[3].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 30", jsCard.getPhones()[3].getContext() == null);
        assertTrue("testCompleteVCard5 - 31", jsCard.getPhones()[3].getValue().equals("555-555-1114"));
        assertTrue("testCompleteVCard5 - 32", jsCard.getPhones()[3].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 33", jsCard.getPhones()[3].getLabels().get("cell") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 34", jsCard.getPhones()[4].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 35", jsCard.getPhones()[4].getContext() == null);
        assertTrue("testCompleteVCard5 - 36", jsCard.getPhones()[4].getValue().equals("555-555-1115"));
        assertTrue("testCompleteVCard5 - 37", jsCard.getPhones()[4].getLabels() == null);
        assertTrue("testCompleteVCard5 - 38", jsCard.getPhones()[5].getType().equals(PhoneResourceType.FAX.getValue()));
        assertTrue("testCompleteVCard5 - 39", jsCard.getPhones()[5].getContext() == Context.PRIVATE);
        assertTrue("testCompleteVCard5 - 40", jsCard.getPhones()[5].getValue().equals("555-555-1116"));
        assertTrue("testCompleteVCard5 - 41", jsCard.getPhones()[5].getLabels() == null);
        assertTrue("testCompleteVCard5 - 42", jsCard.getPhones()[6].getType().equals(PhoneResourceType.FAX.getValue()));
        assertTrue("testCompleteVCard5 - 43", jsCard.getPhones()[6].getContext() == Context.WORK);
        assertTrue("testCompleteVCard5 - 44", jsCard.getPhones()[6].getValue().equals("555-555-1117"));
        assertTrue("testCompleteVCard5 - 45", jsCard.getPhones()[6].getLabels() == null);
        assertTrue("testCompleteVCard5 - 46", jsCard.getPhones()[7].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 47", jsCard.getPhones()[7].getContext() == null);
        assertTrue("testCompleteVCard5 - 48", jsCard.getPhones()[7].getValue().equals("555-555-1118"));
        assertTrue("testCompleteVCard5 - 49", jsCard.getPhones()[7].getLabels() == null);
        assertTrue("testCompleteVCard5 - 50", jsCard.getPhones()[8].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testCompleteVCard5 - 51", jsCard.getPhones()[8].getContext() == null);
        assertTrue("testCompleteVCard5 - 52", jsCard.getPhones()[8].getValue().equals("555-555-1119"));
        assertTrue("testCompleteVCard5 - 53", jsCard.getPhones()[8].getLabels() == null);
        assertTrue("testCompleteVCard5 - 54", jsCard.getEmails().length == 5);
        assertTrue("testCompleteVCard5 - 55", jsCard.getEmails()[0].getContext() == Context.PRIVATE);
        assertTrue("testCompleteVCard5 - 56", jsCard.getEmails()[0].getValue().equals("home@example.com"));
        assertTrue("testCompleteVCard5 - 57", jsCard.getEmails()[0].getLabels() == null);
        assertTrue("testCompleteVCard5 - 58", jsCard.getEmails()[1].getContext() == Context.WORK);
        assertTrue("testCompleteVCard5 - 59", jsCard.getEmails()[1].getValue().equals("work@example.com"));
        assertTrue("testCompleteVCard5 - 60", jsCard.getEmails()[1].getLabels() == null);
        assertTrue("testCompleteVCard5 - 60", jsCard.getEmails()[2].getContext() == null);
        assertTrue("testCompleteVCard5 - 61", jsCard.getEmails()[2].getValue().equals("school@example.com"));
        assertTrue("testCompleteVCard5 - 62", jsCard.getEmails()[2].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 63", jsCard.getEmails()[2].getLabels().get("school") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 64", jsCard.getEmails()[3].getContext() == Context.OTHER);
        assertTrue("testCompleteVCard5 - 65", jsCard.getEmails()[3].getValue().equals("other@example.com"));
        assertTrue("testCompleteVCard5 - 66", jsCard.getEmails()[3].getLabels() == null);
        assertTrue("testCompleteVCard5 - 67", jsCard.getEmails()[4].getContext() == null);
        assertTrue("testCompleteVCard5 - 68", jsCard.getEmails()[4].getValue().equals("custom@example.com"));
        assertTrue("testCompleteVCard5 - 69", jsCard.getEmails()[4].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 70", jsCard.getEmails()[4].getLabels().get("customtype") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 71", jsCard.getOrganizations().size() == 2);
        assertTrue("testCompleteVCard5 - 72", jsCard.getOrganizations().get("ORG-1").getName().getValue().equals("Organization1"));
        assertTrue("testCompleteVCard5 - 72-1", jsCard.getOrganizations().get("ORG-1").getUnits()[0].getValue().equals("Department1"));
        assertTrue("testCompleteVCard5 - 73", jsCard.getOrganizations().get("ORG-2").getName().getValue().equals("Organization2"));
        assertTrue("testCompleteVCard5 - 73", jsCard.getOrganizations().get("ORG-2").getUnits()[0].getValue().equals("Department2"));
        assertTrue("testCompleteVCard5 - 74", jsCard.getJobTitles().size() == 2);
        assertTrue("testCompleteVCard5 - 75", jsCard.getJobTitles().get("TITLE-1").getTitle().getValue().equals("Title1"));
        assertTrue("testCompleteVCard5 - 76", jsCard.getJobTitles().get("TITLE-2").getTitle().getValue().equals("Title2"));
        assertTrue("testCompleteVCard5 - 77", jsCard.getCategories().size() == 1);
        assertTrue("testCompleteVCard5 - 78", jsCard.getCategories().containsKey("Tag"));
        assertTrue("testCompleteVCard5 - 79", jsCard.getNotes()!= null);
//        assertTrue("testCompleteVCard5 - 80", jsCard.getNotes()[0].equals("Notes line 1\nNotes line 2"));
        assertTrue("testCompleteVCard5 - 81", jsCard.getProdId().equals("ez-vcard 0.9.14-fc"));
        assertTrue("testCompleteVCard5 - 82", jsCard.getAnniversaries().length == 1);
        assertTrue("testCompleteVCard5 - 83", jsCard.getAnniversaries()[0].getDate().equals("2016-08-01"));

        assertTrue("testCompleteVCard5 - 84", jsCard.getPhotos().size() == 3);
        assertTrue("testCompleteVCard5 - 85", jsCard.getPhotos().get("PHOTO-1").getHref().equals("https://d3m0kzytmr41b1.cloudfront.net/c335e945d1b60edd9d75eb4837c432f637e95c8a"));
        assertTrue("testCompleteVCard5 - 86", jsCard.getPhotos().get("PHOTO-2").getHref().equals("https://d3m0kzytmr41b1.cloudfront.net/c335e945d1b60edd9d75eb4837c432f637e95c8a"));
        assertTrue("testCompleteVCard5 - 87", jsCard.getPhotos().get("PHOTO-3").getHref().equals("https://d2ojpxxtu63wzl.cloudfront.net/static/aa915d1f29f19baf560e5491decdd30a_67c95da9133249fde8b0da7ceebc298bf680117e6f52054f7f5f7a95e8377238"));

        assertTrue("testCompleteVCard5 - 88", jsCard.getOnline().length == 11);

        assertTrue("testCompleteVCard5 - 97", Objects.equals(jsCard.getOnline()[0].getType(), OnlineResourceType.USERNAME.getValue()));
        assertTrue("testCompleteVCard5 - 98", jsCard.getOnline()[0].getValue().equals("xmpp:gtalk"));
        assertTrue("testCompleteVCard5 - 99", jsCard.getOnline()[0].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 100", jsCard.getOnline()[0].getLabels().get("XMPP") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 101", Objects.equals(jsCard.getOnline()[1].getType(), OnlineResourceType.USERNAME.getValue()));
        assertTrue("testCompleteVCard5 - 102", jsCard.getOnline()[1].getValue().equals("skype:skype"));
        assertTrue("testCompleteVCard5 - 103", jsCard.getOnline()[1].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 104", jsCard.getOnline()[1].getLabels().get("XMPP") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 105", Objects.equals(jsCard.getOnline()[2].getType(), OnlineResourceType.USERNAME.getValue()));
        assertTrue("testCompleteVCard5 - 106", jsCard.getOnline()[2].getValue().equals("ymsgr:yahoo"));
        assertTrue("testCompleteVCard5 - 107", jsCard.getOnline()[2].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 108", jsCard.getOnline()[2].getLabels().get("XMPP") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 109", Objects.equals(jsCard.getOnline()[3].getType(), OnlineResourceType.USERNAME.getValue()));
        assertTrue("testCompleteVCard5 - 110", jsCard.getOnline()[3].getValue().equals("aim:aim"));
        assertTrue("testCompleteVCard5 - 111", jsCard.getOnline()[3].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 112", jsCard.getOnline()[3].getLabels().get("XMPP") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 113", Objects.equals(jsCard.getOnline()[4].getType(), OnlineResourceType.USERNAME.getValue()));
        assertTrue("testCompleteVCard5 - 114", jsCard.getOnline()[4].getValue().equals("xmpp:jabber"));
        assertTrue("testCompleteVCard5 - 115", jsCard.getOnline()[4].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 116", jsCard.getOnline()[4].getLabels().get("XMPP") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 117", Objects.equals(jsCard.getOnline()[5].getType(), OnlineResourceType.USERNAME.getValue()));
        assertTrue("testCompleteVCard5 - 118", jsCard.getOnline()[5].getValue().equals("other:other"));
        assertTrue("testCompleteVCard5 - 119", jsCard.getOnline()[5].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 120", jsCard.getOnline()[5].getLabels().get("XMPP") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 121", Objects.equals(jsCard.getOnline()[6].getType(), OnlineResourceType.USERNAME.getValue()));
        assertTrue("testCompleteVCard5 - 122", jsCard.getOnline()[6].getValue().equals("customtype:custom"));
        assertTrue("testCompleteVCard5 - 123", jsCard.getOnline()[6].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 124", jsCard.getOnline()[6].getLabels().get("XMPP") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 125", Objects.equals(jsCard.getOnline()[7].getType(), OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard5 - 126", jsCard.getOnline()[7].getValue().equals("http://www.homepage.com"));
        assertTrue("testCompleteVCard5 - 127", jsCard.getOnline()[7].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 128", jsCard.getOnline()[7].getLabels().get("url") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 129", Objects.equals(jsCard.getOnline()[8].getType(), OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard5 - 130", jsCard.getOnline()[8].getValue().equals("http://www.blog.com"));
        assertTrue("testCompleteVCard5 - 131", jsCard.getOnline()[8].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 132", jsCard.getOnline()[8].getLabels().get("url") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 133", Objects.equals(jsCard.getOnline()[9].getType(), OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard5 - 134", jsCard.getOnline()[9].getValue().equals("http://www.other.com"));
        assertTrue("testCompleteVCard5 - 135", jsCard.getOnline()[9].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 136", jsCard.getOnline()[9].getLabels().get("url") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 137", Objects.equals(jsCard.getOnline()[10].getType(), OnlineResourceType.URI.getValue()));
        assertTrue("testCompleteVCard5 - 138", jsCard.getOnline()[10].getValue().equals("http://www.custom.com"));
        assertTrue("testCompleteVCard5 - 139", jsCard.getOnline()[10].getLabels().size() == 1);
        assertTrue("testCompleteVCard5 - 140", jsCard.getOnline()[10].getLabels().get("url") == Boolean.TRUE);
        assertTrue("testCompleteVCard5 - 141", jsCard.getAddresses().size() == 4);
        assertTrue("testCompleteVCard5 - 142", jsCard.getAddresses().get("ADR-1").getContext() == AddressContext.PRIVATE);
        assertTrue("testCompleteVCard5 - 143", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("HomeExtended\nHomeStreet\nHomeCity\nHomeState\nHomePostal\nHomeCountry"));
        assertTrue("testCompleteVCard5 - 144", jsCard.getAddresses().get("ADR-1").getExtension().equals("HomeExtended"));
        assertTrue("testCompleteVCard5 - 145", jsCard.getAddresses().get("ADR-1").getStreet().equals("HomeStreet"));
        assertTrue("testCompleteVCard5 - 146", jsCard.getAddresses().get("ADR-1").getLocality().equals("HomeCity"));
        assertTrue("testCompleteVCard5 - 147", jsCard.getAddresses().get("ADR-1").getRegion().equals("HomeState"));
        assertTrue("testCompleteVCard5 - 148", jsCard.getAddresses().get("ADR-1").getCountry().equals("HomeCountry"));
        assertTrue("testCompleteVCard5 - 149", jsCard.getAddresses().get("ADR-1").getPostcode().equals("HomePostal"));
        assertTrue("testCompleteVCard5 - 150", jsCard.getAddresses().get("ADR-2").getContext() == AddressContext.WORK);
        assertTrue("testCompleteVCard5 - 151", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("WorkExtended\nWorkStreet\nWorkCity\nWorkState\nWorkPostal\nWorkCountry"));
        assertTrue("testCompleteVCard5 - 152", jsCard.getAddresses().get("ADR-2").getExtension().equals("WorkExtended"));
        assertTrue("testCompleteVCard5 - 153", jsCard.getAddresses().get("ADR-2").getStreet().equals("WorkStreet"));
        assertTrue("testCompleteVCard5 - 154", jsCard.getAddresses().get("ADR-2").getLocality().equals("WorkCity"));
        assertTrue("testCompleteVCard5 - 155", jsCard.getAddresses().get("ADR-2").getRegion().equals("WorkState"));
        assertTrue("testCompleteVCard5 - 156", jsCard.getAddresses().get("ADR-2").getCountry().equals("WorkCountry"));
        assertTrue("testCompleteVCard5 - 157", jsCard.getAddresses().get("ADR-2").getPostcode().equals("WorkPostal"));
        assertTrue("testCompleteVCard5 - 158", jsCard.getAddresses().get("ADR-3").getContext() == AddressContext.OTHER);
        assertTrue("testCompleteVCard5 - 159", jsCard.getAddresses().get("ADR-3").getFullAddress().getValue().equals("OtherExtended\nOtherStreet\nOtherCity\nOtherState\nOtherPostal\nOtherCountry"));
        assertTrue("testCompleteVCard5 - 160", jsCard.getAddresses().get("ADR-3").getExtension().equals("OtherExtended"));
        assertTrue("testCompleteVCard5 - 161", jsCard.getAddresses().get("ADR-3").getStreet().equals("OtherStreet"));
        assertTrue("testCompleteVCard5 - 162", jsCard.getAddresses().get("ADR-3").getLocality().equals("OtherCity"));
        assertTrue("testCompleteVCard5 - 163", jsCard.getAddresses().get("ADR-3").getRegion().equals("OtherState"));
        assertTrue("testCompleteVCard5 - 164", jsCard.getAddresses().get("ADR-3").getCountry().equals("OtherCountry"));
        assertTrue("testCompleteVCard5 - 165", jsCard.getAddresses().get("ADR-3").getPostcode().equals("OtherPostal"));
        assertTrue("testCompleteVCard5 - 166", jsCard.getAddresses().get("ADR-4").getContext() == null);
        assertTrue("testCompleteVCard5 - 167", jsCard.getAddresses().get("ADR-4").getFullAddress().getValue().equals("CustomExtended\nCustomStreet\nCustomCity\nCustomState\nCustomPostal\nCustomCountry"));
        assertTrue("testCompleteVCard5 - 168", jsCard.getAddresses().get("ADR-4").getExtension().equals("CustomExtended"));
        assertTrue("testCompleteVCard5 - 169", jsCard.getAddresses().get("ADR-4").getStreet().equals("CustomStreet"));
        assertTrue("testCompleteVCard5 - 170", jsCard.getAddresses().get("ADR-4").getLocality().equals("CustomCity"));
        assertTrue("testCompleteVCard5 - 171", jsCard.getAddresses().get("ADR-4").getRegion().equals("CustomState"));
        assertTrue("testCompleteVCard5 - 172", jsCard.getAddresses().get("ADR-4").getCountry().equals("CustomCountry"));
        assertTrue("testCompleteVCard5 - 173", jsCard.getAddresses().get("ADR-4").getPostcode().equals("CustomPostal"));
        assertTrue("testCompleteVCard5 - 174", jsCard.getExtensions().size() == 23);
        assertTrue("testCompleteVCard5 - 175", jsCard.getExtensions().get("ietf.org/rfc6350/GENDER").equals("M"));
        assertTrue("testCompleteVCard5 - 176", jsCard.getExtensions().get("extension/X-GENDER").equals("male"));
        assertTrue("testCompleteVCard5 - 177", jsCard.getExtensions().get("extension/X-ID").equals("14f9aba0c9422da9ae376fe28bd89c2a.0"));
        assertTrue("testCompleteVCard5 - 178", jsCard.getExtensions().get("extension/X-ETAG").equals("fffffea9056d8166e2b7a427977e570c87dd51279d11d9b137c593eb"));
        assertTrue("testCompleteVCard5 - 179", jsCard.getExtensions().get("extension/X-FC-TAGS").equals("579c773f-736d-11e6-8dff-0ac8448704fb"));
        assertTrue("testCompleteVCard5 - 180", jsCard.getExtensions().get("extension/X-FC-LIST-ID").equals("8ad23200aa3e1984736b11e688dc0add41994b95"));
        assertTrue("testCompleteVCard5 - 181", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4D6F74686572").equals("Mother"));
        assertTrue("testCompleteVCard5 - 182", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A466174686572").equals("Father"));
        assertTrue("testCompleteVCard5 - 183", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A506172656E74").equals("Parent"));
        assertTrue("testCompleteVCard5 - 184", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A42726F74686572").equals("Brother"));
        assertTrue("testCompleteVCard5 - 185", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A536973746572").equals("Sister"));
        assertTrue("testCompleteVCard5 - 186", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4368696C64").equals("Child"));
        assertTrue("testCompleteVCard5 - 187", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A467269656E64").equals("Friend"));
        assertTrue("testCompleteVCard5 - 188", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A53706F757365").equals("Spouse"));
        assertTrue("testCompleteVCard5 - 189", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4669616E63C3A9").equals("Fiance"));
        assertTrue("testCompleteVCard5 - 190", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A506172746E6572").equals("Partner"));
        assertTrue("testCompleteVCard5 - 191", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A417373697374616E74").equals("Assistant"));
        assertTrue("testCompleteVCard5 - 192", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4D616E61676572").equals("Manager"));
        assertTrue("testCompleteVCard5 - 193", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4F74686572").equals("Other"));
        assertTrue("testCompleteVCard5 - 194", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A437573746F6D54595045").equals("Custom"));
        assertTrue("testCompleteVCard5 - 195", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D4F7468657244617465733A416E6E6976657273617279").equals("2016-08-02"));
        assertTrue("testCompleteVCard5 - 196", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D4F7468657244617465733A4F74686572").equals("2016-08-03"));
        assertTrue("testCompleteVCard5 - 197", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D4F7468657244617465733A437573746F6D54595045").equals("2016-08-04"));


    }

}
