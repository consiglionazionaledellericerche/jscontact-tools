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
import java.util.Arrays;

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
        assertTrue("testCompleteVCard1 - 13", jsCard.getPreferredContactLanguages().get("fr")[0].getPref() == 1);
        assertTrue("testCompleteVCard1 - 14", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
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
        assertTrue("testCompleteVCard1 - 29", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteVCard1 - 30", jsCard.getEmails().get("EMAIL-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard1 - 31", jsCard.getEmails().get("EMAIL-1").getEmail().equals("joe.user@example.com"));
        assertTrue("testCompleteVCard1 - 32", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteVCard1 - 33", jsCard.getPhones().get("PHONE-1").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard1 - 34", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-555-555-1234;ext=102"));
        assertTrue("testCompleteVCard1 - 35", jsCard.getPhones().get("PHONE-1").getPref() == 1);
        assertTrue("testCompleteVCard1 - 36", jsCard.getPhones().get("PHONE-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard1 - 37", jsCard.getPhones().get("PHONE-1").getLabel() == null);
        assertTrue("testCompleteVCard1 - 38", jsCard.getPhones().get("PHONE-2").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard1 - 39", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-555-555-4321"));
        assertTrue("testCompleteVCard1 - 40", jsCard.getPhones().get("PHONE-2").getPref() == null);
        assertTrue("testCompleteVCard1 - 41", jsCard.getPhones().get("PHONE-2").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard1 - 42", jsCard.getPhones().get("PHONE-2").getLabel().equals("cell,video,text"));
        assertTrue("testCompleteVCard1 - 46", jsCard.getOnline().size() == 2);
        assertTrue("testCompleteVCard1 - 47", jsCard.getOnline().get("KEY-1").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard1 - 48", jsCard.getOnline().get("KEY-1").getResource().equals("http://www.example.com/joe.user/joe.asc"));
        assertTrue("testCompleteVCard1 - 49", jsCard.getOnline().get("KEY-1").getPref() == null);
        assertTrue("testCompleteVCard1 - 50", jsCard.getOnline().get("KEY-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard1 - 52", jsCard.getOnline().get("KEY-1").getLabel().equals(OnlineLabelKey.KEY.getValue()));
        assertTrue("testCompleteVCard1 - 53", jsCard.getOnline().get("URL-1").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard1 - 54", jsCard.getOnline().get("URL-1").getResource().equals("http://example.org"));
        assertTrue("testCompleteVCard1 - 55", jsCard.getOnline().get("URL-1").getPref() == null);
        assertTrue("testCompleteVCard1 - 56", jsCard.getOnline().get("URL-1").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteVCard1 - 58", jsCard.getOnline().get("URL-1").getLabel().equals(OnlineLabelKey.URL.getValue()));
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
        assertTrue("testCompleteVCard2 - 11", jsCard.getPreferredContactLanguages().get("ja")[0].getPref() == 1);
        assertTrue("testCompleteVCard2 - 12", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
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
        assertTrue("testCompleteVCard3 - 7", jsCard.getEmails() == null);
        assertTrue("testCompleteVCard3 - 10", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteVCard3 - 11", jsCard.getPhones().get("PHONE-1").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard3 - 12", jsCard.getPhones().get("PHONE-1").getContexts() == null);
        assertTrue("testCompleteVCard3 - 13", jsCard.getPhones().get("PHONE-1").getPhone().isEmpty());
        assertTrue("testCompleteVCard3 - 14", jsCard.getPhones().get("PHONE-2").getFeatures().containsKey(PhoneType.FAX));
        assertTrue("testCompleteVCard3 - 15", jsCard.getPhones().get("PHONE-2").getContexts() == null);
        assertTrue("testCompleteVCard3 - 16", jsCard.getPhones().get("PHONE-2").getPhone().isEmpty());
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
        assertTrue("testCompleteVCard4 - 19", jsCard.getPreferredContactLanguages().get("fr")[0].getPref() == 1);
        assertTrue("testCompleteVCard4 - 20", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
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
        assertTrue("testCompleteVCard4 - 32", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteVCard4 - 33", jsCard.getPhones().get("PHONE-1").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard4 - 34", jsCard.getPhones().get("PHONE-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard4 - 35", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-418-656-9254;ext=102"));
        assertTrue("testCompleteVCard4 - 36", jsCard.getPhones().get("PHONE-1").getPref() == 1);
        assertTrue("testCompleteVCard4 - 37", jsCard.getPhones().get("PHONE-2").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard4 - 38", jsCard.getPhones().get("PHONE-2").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard4 - 39", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-418-262-6501"));
        assertTrue("testCompleteVCard4 - 40", jsCard.getPhones().get("PHONE-2").getLabel().equals("cell,video,text"));
        assertTrue("testCompleteVCard4 - 44", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteVCard4 - 45", jsCard.getEmails().get("EMAIL-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard4 - 46", jsCard.getEmails().get("EMAIL-1").getEmail().equals("simon.perreault@viagenie.ca"));
        assertTrue("testCompleteVCard4 - 47", jsCard.getOnline().size() == 2);
        assertTrue("testCompleteVCard4 - 48", jsCard.getOnline().get("KEY-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard4 - 49", jsCard.getOnline().get("KEY-1").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard4 - 50", jsCard.getOnline().get("KEY-1").getLabel().equals(OnlineLabelKey.KEY.getValue()));
        assertTrue("testCompleteVCard4 - 51", jsCard.getOnline().get("KEY-1").getResource().equals("http://www.viagenie.ca/simon.perreault/simon.asc"));
        assertTrue("testCompleteVCard4 - 52", jsCard.getOnline().get("URL-1").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteVCard4 - 53", jsCard.getOnline().get("URL-1").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard4 - 54", jsCard.getOnline().get("URL-1").getLabel().equals("url"));
        assertTrue("testCompleteVCard4 - 55", jsCard.getOnline().get("URL-1").getResource().equals("http://nomis80.org"));
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
        assertTrue("testCompleteVCard5 - 17", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteVCard5 - 18", jsCard.getPhones().get("PHONE-1").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard5 - 19", jsCard.getPhones().get("PHONE-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard5 - 20", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-111-555-1212"));
        assertTrue("testCompleteVCard5 - 21", jsCard.getPhones().get("PHONE-1").getLabel() == null);
        assertTrue("testCompleteVCard5 - 22", jsCard.getPhones().get("PHONE-2").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard5 - 23", jsCard.getPhones().get("PHONE-2").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteVCard5 - 24", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-404-555-1212"));
        assertTrue("testCompleteVCard5 - 25", jsCard.getPhones().get("PHONE-2").getLabel() == null);

        assertTrue("testCompleteVCard5 - 26", jsCard.getAddresses().size() == 2);
        assertTrue("testCompleteVCard5 - 27", jsCard.getAddresses().get("ADR-1").getContexts().containsKey(AddressContext.WORK));
        assertTrue("testCompleteVCard5 - 28", jsCard.getAddresses().get("ADR-1").getPref() == 1);
//        assertTrue("testCompleteVCard5 - 29", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("100 Waters Edge\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertTrue("testCompleteVCard5 - 30", jsCard.getAddresses().get("ADR-1").getStreet().equals("100 Waters Edge"));
        assertTrue("testCompleteVCard5 - 31", jsCard.getAddresses().get("ADR-1").getLocality().equals("Baytown"));
        assertTrue("testCompleteVCard5 - 32", jsCard.getAddresses().get("ADR-1").getRegion().equals("LA"));
        assertTrue("testCompleteVCard5 - 33", jsCard.getAddresses().get("ADR-1").getCountry().equals("United States of America"));
        assertTrue("testCompleteVCard5 - 34", jsCard.getAddresses().get("ADR-1").getPostcode().equals("30314"));
        assertTrue("testCompleteVCard5 - 35", jsCard.getAddresses().get("ADR-2").getContexts().containsKey(AddressContext.PRIVATE));
//        assertTrue("testCompleteVCard5 - 36", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("42 Plantation St.\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertTrue("testCompleteVCard5 - 37", jsCard.getAddresses().get("ADR-2").getStreet().equals("42 Plantation St."));
        assertTrue("testCompleteVCard5 - 38", jsCard.getAddresses().get("ADR-2").getLocality().equals("Baytown"));
        assertTrue("testCompleteVCard5 - 39", jsCard.getAddresses().get("ADR-2").getRegion().equals("LA"));
        assertTrue("testCompleteVCard5 - 40", jsCard.getAddresses().get("ADR-2").getCountry().equals("United States of America"));
        assertTrue("testCompleteVCard5 - 41", jsCard.getAddresses().get("ADR-2").getPostcode().equals("30314"));

        assertTrue("testCompleteVCard5 - 42", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteVCard5 - 43", jsCard.getEmails().get("EMAIL-1").getEmail().equals("forrestgump@example.com"));
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
        assertTrue("testCompleteVCard6 - 15", jsCard.getPhones().size() == 9);
        assertTrue("testCompleteVCard6 - 16", jsCard.getPhones().get("PHONE-1").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard6 - 17", jsCard.getPhones().get("PHONE-1").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteVCard6 - 18", jsCard.getPhones().get("PHONE-1").getPhone().equals("555-555-1111"));
        assertTrue("testCompleteVCard6 - 19", jsCard.getPhones().get("PHONE-1").getLabel() == null);
        assertTrue("testCompleteVCard6 - 20", jsCard.getPhones().get("PHONE-2").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard6 - 21", jsCard.getPhones().get("PHONE-2").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard6 - 22", jsCard.getPhones().get("PHONE-2").getPhone().equals("555-555-1112"));
        assertTrue("testCompleteVCard6 - 23", jsCard.getPhones().get("PHONE-2").getLabel() == null);
        assertTrue("testCompleteVCard6 - 24", jsCard.getPhones().get("PHONE-3").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard6 - 25", jsCard.getPhones().get("PHONE-3").getContexts() == null);
        assertTrue("testCompleteVCard6 - 26", jsCard.getPhones().get("PHONE-3").getPhone().equals("555-555-1113"));
        assertTrue("testCompleteVCard6 - 27", jsCard.getPhones().get("PHONE-3").getLabel().equals("cell"));
        assertTrue("testCompleteVCard6 - 29", jsCard.getPhones().get("PHONE-4").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard6 - 30", jsCard.getPhones().get("PHONE-4").getContexts() == null);
        assertTrue("testCompleteVCard6 - 31", jsCard.getPhones().get("PHONE-4").getPhone().equals("555-555-1114"));
        assertTrue("testCompleteVCard6 - 32", jsCard.getPhones().get("PHONE-4").getLabel().equals("cell"));
        assertTrue("testCompleteVCard6 - 34", jsCard.getPhones().get("PHONE-5").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard6 - 35", jsCard.getPhones().get("PHONE-5").getContexts() == null);
        assertTrue("testCompleteVCard6 - 36", jsCard.getPhones().get("PHONE-5").getPhone().equals("555-555-1115"));
        assertTrue("testCompleteVCard6 - 37", jsCard.getPhones().get("PHONE-5").getLabel() == null);
        assertTrue("testCompleteVCard6 - 38", jsCard.getPhones().get("PHONE-6").getFeatures().containsKey(PhoneType.FAX));
        assertTrue("testCompleteVCard6 - 39", jsCard.getPhones().get("PHONE-6").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteVCard6 - 40", jsCard.getPhones().get("PHONE-6").getPhone().equals("555-555-1116"));
        assertTrue("testCompleteVCard6 - 41", jsCard.getPhones().get("PHONE-6").getLabel() == null);
        assertTrue("testCompleteVCard6 - 42", jsCard.getPhones().get("PHONE-7").getFeatures().containsKey(PhoneType.FAX));
        assertTrue("testCompleteVCard6 - 43", jsCard.getPhones().get("PHONE-7").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard6 - 44", jsCard.getPhones().get("PHONE-7").getPhone().equals("555-555-1117"));
        assertTrue("testCompleteVCard6 - 45", jsCard.getPhones().get("PHONE-7").getLabel() == null);
        assertTrue("testCompleteVCard6 - 46", jsCard.getPhones().get("PHONE-8").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard6 - 47", jsCard.getPhones().get("PHONE-8").getContexts() == null);
        assertTrue("testCompleteVCard6 - 48", jsCard.getPhones().get("PHONE-8").getPhone().equals("555-555-1118"));
        assertTrue("testCompleteVCard6 - 49", jsCard.getPhones().get("PHONE-8").getLabel() == null);
        assertTrue("testCompleteVCard6 - 50", jsCard.getPhones().get("PHONE-9").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteVCard6 - 51", jsCard.getPhones().get("PHONE-9").getContexts() == null);
        assertTrue("testCompleteVCard6 - 52", jsCard.getPhones().get("PHONE-9").getPhone().equals("555-555-1119"));
        assertTrue("testCompleteVCard6 - 53", jsCard.getPhones().get("PHONE-9").getLabel() == null);
        assertTrue("testCompleteVCard6 - 54", jsCard.getEmails().size() == 5);
        assertTrue("testCompleteVCard6 - 55", jsCard.getEmails().get("EMAIL-1").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteVCard6 - 56", jsCard.getEmails().get("EMAIL-1").getEmail().equals("home@example.com"));
        assertTrue("testCompleteVCard6 - 58", jsCard.getEmails().get("EMAIL-2").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteVCard6 - 59", jsCard.getEmails().get("EMAIL-2").getEmail().equals("work@example.com"));
        assertTrue("testCompleteVCard6 - 60", jsCard.getEmails().get("EMAIL-3").getContexts() == null);
        assertTrue("testCompleteVCard6 - 61", jsCard.getEmails().get("EMAIL-3").getEmail().equals("school@example.com"));
        assertTrue("testCompleteVCard6 - 64", jsCard.getEmails().get("EMAIL-4").getContexts().containsKey(Context.OTHER));
        assertTrue("testCompleteVCard6 - 65", jsCard.getEmails().get("EMAIL-4").getEmail().equals("other@example.com"));
        assertTrue("testCompleteVCard6 - 67", jsCard.getEmails().get("EMAIL-5").getContexts() == null);
        assertTrue("testCompleteVCard6 - 68", jsCard.getEmails().get("EMAIL-5").getEmail().equals("custom@example.com"));
        assertTrue("testCompleteVCard6 - 71", jsCard.getOrganizations().size() == 2);
        assertTrue("testCompleteVCard6 - 72", jsCard.getOrganizations().get("ORG-1").getName().getValue().equals("Organization1"));
        assertTrue("testCompleteVCard6 - 72-1", jsCard.getOrganizations().get("ORG-1").getUnits()[0].getValue().equals("Department1"));
        assertTrue("testCompleteVCard6 - 73", jsCard.getOrganizations().get("ORG-2").getName().getValue().equals("Organization2"));
        assertTrue("testCompleteVCard6 - 73", jsCard.getOrganizations().get("ORG-2").getUnits()[0].getValue().equals("Department2"));
        assertTrue("testCompleteVCard6 - 74", jsCard.getJobTitles().size() == 2);
        assertTrue("testCompleteVCard6 - 75", jsCard.getJobTitles().get("TITLE-1").getTitle().getValue().equals("Title1"));
        assertTrue("testCompleteVCard6 - 76", jsCard.getJobTitles().get("TITLE-2").getTitle().getValue().equals("Title2"));
        assertTrue("testCompleteVCard6 - 77", jsCard.getCategories().size() == 1);
        assertTrue("testCompleteVCard6 - 78", jsCard.getCategories().containsKey("Tag"));
        assertTrue("testCompleteVCard6 - 79", jsCard.getNotes()!= null);
//        assertTrue("testCompleteVCard6 - 80", jsCard.getNotes()[0].equals("Notes line 1\nNotes line 2"));
        assertTrue("testCompleteVCard6 - 81", jsCard.getProdId().equals("ez-vcard 0.9.14-fc"));
        assertTrue("testCompleteVCard6 - 82", jsCard.getAnniversaries().length == 1);
        assertTrue("testCompleteVCard6 - 83", jsCard.getAnniversaries()[0].getDate().equals("2016-08-01"));

        assertTrue("testCompleteVCard6 - 84", jsCard.getPhotos().size() == 3);
        assertTrue("testCompleteVCard6 - 85", jsCard.getPhotos().get("PHOTO-1").getHref().equals("https://d3m0kzytmr41b1.cloudfront.net/c335e945d1b60edd9d75eb4837c432f637e95c8a"));
        assertTrue("testCompleteVCard6 - 86", jsCard.getPhotos().get("PHOTO-2").getHref().equals("https://d3m0kzytmr41b1.cloudfront.net/c335e945d1b60edd9d75eb4837c432f637e95c8a"));
        assertTrue("testCompleteVCard6 - 87", jsCard.getPhotos().get("PHOTO-3").getHref().equals("https://d2ojpxxtu63wzl.cloudfront.net/static/aa915d1f29f19baf560e5491decdd30a_67c95da9133249fde8b0da7ceebc298bf680117e6f52054f7f5f7a95e8377238"));

        assertTrue("testCompleteVCard6 - 88", jsCard.getOnline().size() == 11);

        assertTrue("testCompleteVCard6 - 97", jsCard.getOnline().get("XMPP-1").getType() == ResourceType.USERNAME);
        assertTrue("testCompleteVCard6 - 98", jsCard.getOnline().get("XMPP-1").getResource().equals("xmpp:gtalk"));
        assertTrue("testCompleteVCard6 - 100", jsCard.getOnline().get("XMPP-1").getLabel().equals("XMPP"));
        assertTrue("testCompleteVCard6 - 101", jsCard.getOnline().get("XMPP-2").getType() == ResourceType.USERNAME);
        assertTrue("testCompleteVCard6 - 102", jsCard.getOnline().get("XMPP-2").getResource().equals("skype:skype"));
        assertTrue("testCompleteVCard6 - 104", jsCard.getOnline().get("XMPP-2").getLabel().equals("XMPP"));
        assertTrue("testCompleteVCard6 - 105", jsCard.getOnline().get("XMPP-3").getType() == ResourceType.USERNAME);
        assertTrue("testCompleteVCard6 - 106", jsCard.getOnline().get("XMPP-3").getResource().equals("ymsgr:yahoo"));
        assertTrue("testCompleteVCard6 - 108", jsCard.getOnline().get("XMPP-3").getLabel().equals("XMPP"));
        assertTrue("testCompleteVCard6 - 109", jsCard.getOnline().get("XMPP-4").getType() == ResourceType.USERNAME);
        assertTrue("testCompleteVCard6 - 110", jsCard.getOnline().get("XMPP-4").getResource().equals("aim:aim"));
        assertTrue("testCompleteVCard6 - 112", jsCard.getOnline().get("XMPP-4").getLabel().equals("XMPP"));
        assertTrue("testCompleteVCard6 - 113", jsCard.getOnline().get("XMPP-5").getType() == ResourceType.USERNAME);
        assertTrue("testCompleteVCard6 - 114", jsCard.getOnline().get("XMPP-5").getResource().equals("xmpp:jabber"));
        assertTrue("testCompleteVCard6 - 116", jsCard.getOnline().get("XMPP-5").getLabel().equals("XMPP"));
        assertTrue("testCompleteVCard6 - 117", jsCard.getOnline().get("XMPP-6").getType() == ResourceType.USERNAME);
        assertTrue("testCompleteVCard6 - 118", jsCard.getOnline().get("XMPP-6").getResource().equals("other:other"));
        assertTrue("testCompleteVCard6 - 120", jsCard.getOnline().get("XMPP-6").getLabel().equals("XMPP"));
        assertTrue("testCompleteVCard6 - 121", jsCard.getOnline().get("XMPP-7").getType() == ResourceType.USERNAME);
        assertTrue("testCompleteVCard6 - 122", jsCard.getOnline().get("XMPP-7").getResource().equals("customtype:custom"));
        assertTrue("testCompleteVCard6 - 124", jsCard.getOnline().get("XMPP-7").getLabel().equals("XMPP"));

        assertTrue("testCompleteVCard6 - 125", jsCard.getOnline().get("URL-1").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard6 - 126", jsCard.getOnline().get("URL-1").getResource().equals("http://www.homepage.com"));
        assertTrue("testCompleteVCard6 - 128", jsCard.getOnline().get("URL-1").getLabel().equals("url"));
        assertTrue("testCompleteVCard6 - 129", jsCard.getOnline().get("URL-2").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard6 - 130", jsCard.getOnline().get("URL-2").getResource().equals("http://www.blog.com"));
        assertTrue("testCompleteVCard6 - 132", jsCard.getOnline().get("URL-2").getLabel().equals("url"));
        assertTrue("testCompleteVCard6 - 133", jsCard.getOnline().get("URL-3").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard6 - 134", jsCard.getOnline().get("URL-3").getResource().equals("http://www.other.com"));
        assertTrue("testCompleteVCard6 - 136", jsCard.getOnline().get("URL-3").getLabel().equals("url"));
        assertTrue("testCompleteVCard6 - 137", jsCard.getOnline().get("URL-4").getType() == ResourceType.URI);
        assertTrue("testCompleteVCard6 - 138", jsCard.getOnline().get("URL-4").getResource().equals("http://www.custom.com"));
        assertTrue("testCompleteVCard6 - 140", jsCard.getOnline().get("URL-4").getLabel().equals("url"));
        assertTrue("testCompleteVCard6 - 141", jsCard.getAddresses().size() == 4);
        assertTrue("testCompleteVCard6 - 142", jsCard.getAddresses().get("ADR-1").getContexts().containsKey(AddressContext.PRIVATE));
        assertTrue("testCompleteVCard6 - 143", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("HomeExtended\nHomeStreet\nHomeCity\nHomeState\nHomePostal\nHomeCountry"));
        assertTrue("testCompleteVCard6 - 144", jsCard.getAddresses().get("ADR-1").getExtension().equals("HomeExtended"));
        assertTrue("testCompleteVCard6 - 145", jsCard.getAddresses().get("ADR-1").getStreet().equals("HomeStreet"));
        assertTrue("testCompleteVCard6 - 146", jsCard.getAddresses().get("ADR-1").getLocality().equals("HomeCity"));
        assertTrue("testCompleteVCard6 - 147", jsCard.getAddresses().get("ADR-1").getRegion().equals("HomeState"));
        assertTrue("testCompleteVCard6 - 148", jsCard.getAddresses().get("ADR-1").getCountry().equals("HomeCountry"));
        assertTrue("testCompleteVCard6 - 149", jsCard.getAddresses().get("ADR-1").getPostcode().equals("HomePostal"));
        assertTrue("testCompleteVCard6 - 150", jsCard.getAddresses().get("ADR-2").getContexts().containsKey(AddressContext.WORK));
        assertTrue("testCompleteVCard6 - 151", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("WorkExtended\nWorkStreet\nWorkCity\nWorkState\nWorkPostal\nWorkCountry"));
        assertTrue("testCompleteVCard6 - 152", jsCard.getAddresses().get("ADR-2").getExtension().equals("WorkExtended"));
        assertTrue("testCompleteVCard6 - 153", jsCard.getAddresses().get("ADR-2").getStreet().equals("WorkStreet"));
        assertTrue("testCompleteVCard6 - 154", jsCard.getAddresses().get("ADR-2").getLocality().equals("WorkCity"));
        assertTrue("testCompleteVCard6 - 155", jsCard.getAddresses().get("ADR-2").getRegion().equals("WorkState"));
        assertTrue("testCompleteVCard6 - 156", jsCard.getAddresses().get("ADR-2").getCountry().equals("WorkCountry"));
        assertTrue("testCompleteVCard6 - 157", jsCard.getAddresses().get("ADR-2").getPostcode().equals("WorkPostal"));
        assertTrue("testCompleteVCard6 - 158", jsCard.getAddresses().get("ADR-3").getContexts().containsKey(AddressContext.OTHER));
        assertTrue("testCompleteVCard6 - 159", jsCard.getAddresses().get("ADR-3").getFullAddress().getValue().equals("OtherExtended\nOtherStreet\nOtherCity\nOtherState\nOtherPostal\nOtherCountry"));
        assertTrue("testCompleteVCard6 - 160", jsCard.getAddresses().get("ADR-3").getExtension().equals("OtherExtended"));
        assertTrue("testCompleteVCard6 - 161", jsCard.getAddresses().get("ADR-3").getStreet().equals("OtherStreet"));
        assertTrue("testCompleteVCard6 - 162", jsCard.getAddresses().get("ADR-3").getLocality().equals("OtherCity"));
        assertTrue("testCompleteVCard6 - 163", jsCard.getAddresses().get("ADR-3").getRegion().equals("OtherState"));
        assertTrue("testCompleteVCard6 - 164", jsCard.getAddresses().get("ADR-3").getCountry().equals("OtherCountry"));
        assertTrue("testCompleteVCard6 - 165", jsCard.getAddresses().get("ADR-3").getPostcode().equals("OtherPostal"));
        assertTrue("testCompleteVCard6 - 166", jsCard.getAddresses().get("ADR-4").getContexts() == null);
        assertTrue("testCompleteVCard6 - 167", jsCard.getAddresses().get("ADR-4").getFullAddress().getValue().equals("CustomExtended\nCustomStreet\nCustomCity\nCustomState\nCustomPostal\nCustomCountry"));
        assertTrue("testCompleteVCard6 - 168", jsCard.getAddresses().get("ADR-4").getExtension().equals("CustomExtended"));
        assertTrue("testCompleteVCard6 - 169", jsCard.getAddresses().get("ADR-4").getStreet().equals("CustomStreet"));
        assertTrue("testCompleteVCard6 - 170", jsCard.getAddresses().get("ADR-4").getLocality().equals("CustomCity"));
        assertTrue("testCompleteVCard6 - 171", jsCard.getAddresses().get("ADR-4").getRegion().equals("CustomState"));
        assertTrue("testCompleteVCard6 - 172", jsCard.getAddresses().get("ADR-4").getCountry().equals("CustomCountry"));
        assertTrue("testCompleteVCard6 - 173", jsCard.getAddresses().get("ADR-4").getPostcode().equals("CustomPostal"));
        assertTrue("testCompleteVCard6 - 174", jsCard.getExtensions().size() == 23);
        assertTrue("testCompleteVCard6 - 175", jsCard.getExtensions().get("ietf.org/rfc6350/GENDER").equals("M"));
        assertTrue("testCompleteVCard6 - 176", jsCard.getExtensions().get("extension/X-GENDER").equals("male"));
        assertTrue("testCompleteVCard6 - 177", jsCard.getExtensions().get("extension/X-ID").equals("14f9aba0c9422da9ae376fe28bd89c2a.0"));
        assertTrue("testCompleteVCard6 - 178", jsCard.getExtensions().get("extension/X-ETAG").equals("fffffea9056d8166e2b7a427977e570c87dd51279d11d9b137c593eb"));
        assertTrue("testCompleteVCard6 - 179", jsCard.getExtensions().get("extension/X-FC-TAGS").equals("579c773f-736d-11e6-8dff-0ac8448704fb"));
        assertTrue("testCompleteVCard6 - 180", jsCard.getExtensions().get("extension/X-FC-LIST-ID").equals("8ad23200aa3e1984736b11e688dc0add41994b95"));
        assertTrue("testCompleteVCard6 - 181", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4D6F74686572").equals("Mother"));
        assertTrue("testCompleteVCard6 - 182", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A466174686572").equals("Father"));
        assertTrue("testCompleteVCard6 - 183", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A506172656E74").equals("Parent"));
        assertTrue("testCompleteVCard6 - 184", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A42726F74686572").equals("Brother"));
        assertTrue("testCompleteVCard6 - 185", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A536973746572").equals("Sister"));
        assertTrue("testCompleteVCard6 - 186", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4368696C64").equals("Child"));
        assertTrue("testCompleteVCard6 - 187", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A467269656E64").equals("Friend"));
        assertTrue("testCompleteVCard6 - 188", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A53706F757365").equals("Spouse"));
        assertTrue("testCompleteVCard6 - 189", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4669616E63C3A9").equals("Fiance"));
        assertTrue("testCompleteVCard6 - 190", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A506172746E6572").equals("Partner"));
        assertTrue("testCompleteVCard6 - 191", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A417373697374616E74").equals("Assistant"));
        assertTrue("testCompleteVCard6 - 192", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4D616E61676572").equals("Manager"));
        assertTrue("testCompleteVCard6 - 193", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A4F74686572").equals("Other"));
        assertTrue("testCompleteVCard6 - 194", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D52656C617465644E616D65733A437573746F6D54595045").equals("Custom"));
        assertTrue("testCompleteVCard6 - 195", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D4F7468657244617465733A416E6E6976657273617279").equals("2016-08-02"));
        assertTrue("testCompleteVCard6 - 196", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D4F7468657244617465733A4F74686572").equals("2016-08-03"));
        assertTrue("testCompleteVCard6 - 197", jsCard.getExtensions().get("extension/X-FCENCODED-582D46432D4F7468657244617465733A437573746F6D54595045").equals("2016-08-04"));


    }

}
