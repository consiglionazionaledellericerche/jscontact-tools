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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.GrammaticalGenderType;
import it.cnr.iit.jscontact.tools.dto.Name;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertSame;

public class UnconvertedParamsTest extends VCard2JSContactTest {

    @Test
    public void testMemberUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:group\n" +
                "FN:Funky distribution list\n" +
                "MEMBER;PREF=1:mailto:subscriber1@example.com\n" +
                "MEMBER;PREF=2:xmpp:subscriber2@example.com\n" +
                "MEMBER:sip:subscriber3@example.com\n" +
                "MEMBER:tel:+1-418-555-5555\n" +
                "END:VCARD";

        List<Card> jsCards = vCard2JSContact.convert(vcard);
        assertEquals("testMemberUnconvertedParams - 1", 1, jsCards.size());
        Card jsCardGroup = jsCards.get(0);
        assertTrue("testMemberUnconvertedParams - 3", jsCardGroup.getKind().isGroup());
        assertTrue("testMemberUnconvertedParams - 4", StringUtils.isEmpty(jsCardGroup.getUid()));
        assertEquals("testMemberUnconvertedParams - 5", "Funky distribution list", jsCardGroup.getName().getFull());
        assertEquals("testMemberUnconvertedParams - 6", 4, jsCardGroup.getMembers().size());
        assertSame("testMemberUnconvertedParams - 7", jsCardGroup.getMembers().get("mailto:subscriber1@example.com"), Boolean.TRUE);
        assertSame("testMemberUnconvertedParams - 8", jsCardGroup.getMembers().get("xmpp:subscriber2@example.com"), Boolean.TRUE);
        assertSame("testMemberUnconvertedParams - 9", jsCardGroup.getMembers().get("sip:subscriber3@example.com"), Boolean.TRUE);
        assertSame("testMemberUnconvertedParams - 10", jsCardGroup.getMembers().get("tel:+1-418-555-5555"), Boolean.TRUE);
        assertEquals("testMemberUnconvertedParams - 11", "1",jsCardGroup.getVCard().getConvertedProperties().get("members/mailto:subscriber1@example.com").getParameters().get("pref").getValue());
        assertEquals("testMemberUnconvertedParams - 12", "member",jsCardGroup.getVCard().getConvertedProperties().get("members/mailto:subscriber1@example.com").getName());
        assertEquals("testMemberUnconvertedParams - 13", "2",jsCardGroup.getVCard().getConvertedProperties().get("members/xmpp:subscriber2@example.com").getParameters().get("pref").getValue());
        assertEquals("testMemberUnconvertedParams - 14", "member",jsCardGroup.getVCard().getConvertedProperties().get("members/xmpp:subscriber2@example.com").getName());
    }

    @Test
    public void testRelatedUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "RELATED;PREF=1;TYPE=friend:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\n" +
                "RELATED;TYPE=contact:http://example.com/directory/jdoe.vcf\n" +
                "RELATED;PREF=2:Please contact my assistant Jane Doe for any inquiries.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testRelatedUnconvertedParams - 1", 3, jsCard.getRelatedTo().size());
        assertEquals("testRelatedUnconvertedParams - 2", 1, jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getRelation().size());
        assertTrue("testRelatedUnconvertedParams - 3",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").asFriend());
        assertEquals("testRelatedUnconvertedParams - 4", 1, jsCard.getRelatedTo().get("http://example.com/directory/jdoe.vcf").getRelation().size());
        assertTrue("testRelatedUnconvertedParams - 5",jsCard.getRelatedTo().get("http://example.com/directory/jdoe.vcf").asContact());
        assertEquals("testRelatedUnconvertedParams - 6", 0, jsCard.getRelatedTo().get("Please contact my assistant Jane Doe for any inquiries.").getRelation().size());
        assertEquals("testMemberUnconvertedParams - 11", "1",jsCard.getVCard().getConvertedProperties().get("relatedTo/urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getParameters().get("pref").getValue());
        assertEquals("testMemberUnconvertedParams - 12", "related",jsCard.getVCard().getConvertedProperties().get("relatedTo/urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getName());
        assertEquals("testMemberUnconvertedParams - 13", "2",jsCard.getVCard().getConvertedProperties().get("relatedTo/Please contact my assistant Jane Doe for any inquiries.").getParameters().get("pref").getValue());
        assertEquals("testMemberUnconvertedParams - 14", "related",jsCard.getVCard().getConvertedProperties().get("relatedTo/Please contact my assistant Jane Doe for any inquiries.").getName());
    }

    @Test
    public void testKindUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND;X-PARAM=test:individual\n" +
                "FN:test\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertTrue("testKindUnconvertedParams - 1",jsCard.getKind().isIndividual());
        assertEquals("testKindUnconvertedParams - 2", "test",jsCard.getVCard().getConvertedProperties().get("kind").getParameters().get("x-param").getValue());
        assertEquals("testKindUnconvertedParams - 3", "kind",jsCard.getVCard().getConvertedProperties().get("kind").getName());
    }

    @Test
    public void testProdidUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PRODID;X-PARAM=test:-//ONLINE DIRECTORY//NONSGML Version 1//EN\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testProdidUnconvertedParams - 1", "-//ONLINE DIRECTORY//NONSGML Version 1//EN", jsCard.getProdId());
        assertEquals("testProdidUnconvertedParams - 2", "test",jsCard.getVCard().getConvertedProperties().get("prodId").getParameters().get("x-param").getValue());
        assertEquals("testProdidUnconvertedParams - 3", "prodid",jsCard.getVCard().getConvertedProperties().get("prodId").getName());
    }

    @Test
    public void testUpdatedUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "REV;X-PARAM=test:19951031T222710Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testUpdatedUnconvertedParams - 1", 0, jsCard.getUpdated().compareTo(DateUtils.toCalendar("1995-10-31T22:27:10Z")));
        assertEquals("testUpdatedUnconvertedParams - 2", "test",jsCard.getVCard().getConvertedProperties().get("updated").getParameters().get("x-param").getValue());
        assertEquals("testUpdatedUnconvertedParams - 3", "rev",jsCard.getVCard().getConvertedProperties().get("updated").getName());
    }

    @Test
    public void testUidUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "UID;X-PARAM=test:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testUidUnconvertedParams - 1", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());
        assertEquals("testUidUnconvertedParams - 2", "test",jsCard.getVCard().getConvertedProperties().get("uid").getParameters().get("x-param").getValue());
        assertEquals("testUidUnconvertedParams - 3", "uid",jsCard.getVCard().getConvertedProperties().get("uid").getName());
    }

    @Test
    public void testEmailAddressUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "EMAIL;TYPE=work;PID=1:jqpublic@xyz.example.com\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testEmailAddressUnconvertedParams - 1", 1, jsCard.getEmails().size());
        assertEquals("testEmailAddressUnconvertedParams - 2", 1, jsCard.getEmails().get("EMAIL-1").getContexts().size());
        assertTrue("testEmailAddressUnconvertedParams - 3",jsCard.getEmails().get("EMAIL-1").asWork());
        assertEquals("testEmailAddressUnconvertedParams - 4", "jqpublic@xyz.example.com", jsCard.getEmails().get("EMAIL-1").getAddress());
        assertEquals("testEmailAddressUnconvertedParams - 5", "1",jsCard.getVCard().getConvertedProperties().get("emails/EMAIL-1").getParameters().get("pid").getValue());
        assertEquals("testEmailAddressUnconvertedParams - 6", "email",jsCard.getVCard().getConvertedProperties().get("emails/EMAIL-1").getName());
    }


    @Test
    public void testPhoneUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;PID=1;TYPE=home:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhoneUnconvertedParams - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhoneUnconvertedParams - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhoneUnconvertedParams - 3",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertEquals("testPhoneUnconvertedParams - 5", "1",jsCard.getVCard().getConvertedProperties().get("phones/PHONE-1").getParameters().get("pid").getValue());
        assertEquals("testPhoneUnconvertedParams - 6", "tel",jsCard.getVCard().getConvertedProperties().get("phones/PHONE-1").getName());
    }


    @Test
    public void testPersonalInfoUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1;PID=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=1:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=2;X-PARAM=test:rock 'n' roll music\n" +
                "EXPERTISE;LEVEL=beginner;INDEX=2:chinese literature\n" +
                "EXPERTISE;LEVEL=expert;INDEX=1;PID=2:chemistry\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfoUnconvertedParams - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoUnconvertedParams - 2", 6, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoUnconvertedParams - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoUnconvertedParams - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoUnconvertedParams - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertEquals("testPersonalInfoUnconvertedParams - 6", 1, (int) jsCard.getPersonalInfo().get("HOBBY-1").getListAs());
        assertTrue("testPersonalInfoUnconvertedParams - 7", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfoUnconvertedParams - 8", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfoUnconvertedParams - 9", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertEquals("testPersonalInfoUnconvertedParams - 10", 2, (int) jsCard.getPersonalInfo().get("HOBBY-2").getListAs());
        assertTrue("testPersonalInfoUnconvertedParams - 11", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfoUnconvertedParams - 12", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfoUnconvertedParams - 13", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertEquals("testPersonalInfoUnconvertedParams - 14", 1, (int) jsCard.getPersonalInfo().get("INTEREST-1").getListAs());
        assertTrue("testPersonalInfoUnconvertedParams - 15", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfoUnconvertedParams - 16", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfoUnconvertedParams - 17", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());
        assertEquals("testPersonalInfoUnconvertedParams - 18", 2, (int) jsCard.getPersonalInfo().get("INTEREST-2").getListAs());
        assertTrue("testPersonalInfoUnconvertedParams - 19", jsCard.getPersonalInfo().get("EXPERTISE-1").asExpertise());
        assertEquals("testPersonalInfoUnconvertedParams - 20", "chinese literature", jsCard.getPersonalInfo().get("EXPERTISE-1").getValue());
        assertTrue("testPersonalInfoUnconvertedParams - 21", jsCard.getPersonalInfo().get("EXPERTISE-1").ofLowLevel());
        assertEquals("testPersonalInfoUnconvertedParams - 22", 2, (int) jsCard.getPersonalInfo().get("EXPERTISE-1").getListAs());
        assertTrue("testPersonalInfoUnconvertedParams - 23", jsCard.getPersonalInfo().get("EXPERTISE-2").asExpertise());
        assertEquals("testPersonalInfoUnconvertedParams - 24", "chemistry", jsCard.getPersonalInfo().get("EXPERTISE-2").getValue());
        assertTrue("testPersonalInfoUnconvertedParams - 25", jsCard.getPersonalInfo().get("EXPERTISE-2").ofHighLevel());
        assertEquals("testPersonalInfoUnconvertedParams - 26", 1, (int) jsCard.getPersonalInfo().get("EXPERTISE-2").getListAs());
        assertEquals("testPersonalInfoUnconvertedParams - 27", "1",jsCard.getVCard().getConvertedProperties().get("personalInfo/HOBBY-1").getParameters().get("pid").getValue());
        assertEquals("testPersonalInfoUnconvertedParams - 28", "hobby",jsCard.getVCard().getConvertedProperties().get("personalInfo/HOBBY-1").getName());
        assertEquals("testPersonalInfoUnconvertedParams - 29", "test",jsCard.getVCard().getConvertedProperties().get("personalInfo/INTEREST-2").getParameters().get("x-param").getValue());
        assertEquals("testPersonalInfoUnconvertedParams - 30", "interest",jsCard.getVCard().getConvertedProperties().get("personalInfo/INTEREST-2").getName());
        assertEquals("testPersonalInfoUnconvertedParams - 31", "2",jsCard.getVCard().getConvertedProperties().get("personalInfo/EXPERTISE-2").getParameters().get("pid").getValue());
        assertEquals("testPersonalInfoUnconvertedParams - 32", "expertise",jsCard.getVCard().getConvertedProperties().get("personalInfo/EXPERTISE-2").getName());
    }

    @Test
    public void testSourceUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOURCE;PID=1:http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSourceUnconvertedParams - 1", 1, jsCard.getDirectories().size());
        assertEquals("testSourceUnconvertedParams - 2", "http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf", jsCard.getDirectories().get("ENTRY-1").getUri());
        assertTrue("testSourceUnconvertedParams - 3",jsCard.getDirectories().get("ENTRY-1").isEntry());
        assertNull("testSourceUnconvertedParams - 4", jsCard.getDirectories().get("ENTRY-1").getPref());
        assertNull("testSourceUnconvertedParams - 5", jsCard.getDirectories().get("ENTRY-1").getMediaType());
        assertFalse("testSourceUnconvertedParams - 6",jsCard.getDirectories().get("ENTRY-1").hasContext());
        assertEquals("testSourceUnconvertedParams - 7", "1",jsCard.getVCard().getConvertedProperties().get("directories/ENTRY-1").getParameters().get("pid").getValue());
        assertEquals("testSourceUnconvertedParams - 8", "source",jsCard.getVCard().getConvertedProperties().get("directories/ENTRY-1").getName());
    }


    @Test
    public void testLogoUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LOGO;PID=1:http://www.example.com/pub/logos/abccorp.jpg\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testLogoUnconvertedParams - 1", 1, jsCard.getMedia().size());
        assertEquals("testLogoUnconvertedParams - 2", "http://www.example.com/pub/logos/abccorp.jpg", jsCard.getMedia().get("LOGO-1").getUri());
        assertTrue("testLogoUnconvertedParams - 3",jsCard.getMedia().get("LOGO-1").isLogo());
        assertNull("testLogoUnconvertedParams - 4", jsCard.getMedia().get("LOGO-1").getPref());
        assertEquals("testLogoUnconvertedParams - 5", MimeTypeUtils.MIME_IMAGE_JPEG, jsCard.getMedia().get("LOGO-1").getMediaType());
        assertFalse("testLogoUnconvertedParams - 6",jsCard.getMedia().get("LOGO-1").hasContext());
        assertEquals("testLogoUnconvertedParams - 7", "1",jsCard.getVCard().getConvertedProperties().get("media/LOGO-1").getParameters().get("pid").getValue());
        assertEquals("testLogoUnconvertedParams - 8", "logo",jsCard.getVCard().getConvertedProperties().get("media/LOGO-1").getName());
    }

    @Test
    public void testPhotoUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PHOTO;PID=1:http://www.example.com/pub/photos/jqpublic.gif\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhotoUnconvertedParams - 1", 1, jsCard.getMedia().size());
        assertEquals("testPhotoUnconvertedParams - 2", "http://www.example.com/pub/photos/jqpublic.gif", jsCard.getMedia().get("PHOTO-1").getUri());
        assertNull("testPhotoUnconvertedParams - 3", jsCard.getMedia().get("PHOTO-1").getPref());
        assertEquals("testPhotoUnconvertedParams - 4", MimeTypeUtils.MIME_IMAGE_GIF, jsCard.getMedia().get("PHOTO-1").getMediaType());
        assertEquals("testPhotoUnconvertedParams - 5", "1",jsCard.getVCard().getConvertedProperties().get("media/PHOTO-1").getParameters().get("pid").getValue());
        assertEquals("testPhotoUnconvertedParams - 6", "photo",jsCard.getVCard().getConvertedProperties().get("media/PHOTO-1").getName());
    }

    @Test
    public void testSoundUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOUND;PID=1:CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSoundUnconvertedParams - 1", 1, jsCard.getMedia().size());
        assertEquals("testSoundUnconvertedParams - 2", "CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com", jsCard.getMedia().get("SOUND-1").getUri());
        assertTrue("testSoundUnconvertedParams - 3",jsCard.getMedia().get("SOUND-1").isSound());
        assertNull("testSoundUnconvertedParams - 4", jsCard.getMedia().get("SOUND-1").getPref());
        assertNull("testSoundUnconvertedParams - 5", jsCard.getMedia().get("SOUND-1").getMediaType());
        assertFalse("testSoundUnconvertedParams - 6",jsCard.getMedia().get("SOUND-1").hasContext());
        assertEquals("testSoundUnconvertedParams - 7", "1",jsCard.getVCard().getConvertedProperties().get("media/SOUND-1").getParameters().get("pid").getValue());
        assertEquals("testSoundUnconvertedParams - 8", "sound",jsCard.getVCard().getConvertedProperties().get("media/SOUND-1").getName());
    }

    @Test
    public void testContactUriUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CONTACT-URI;X-PARAM=test;PREF=1:mailto:contact@example.com\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testContactUriUnconvertedParams - 1", 1, jsCard.getLinks().size());
        assertEquals("testContactUriUnconvertedParams - 2", "mailto:contact@example.com", jsCard.getLinks().get("CONTACT-1").getUri());
        assertTrue("testContactUriUnconvertedParams - 3",jsCard.getLinks().get("CONTACT-1").isContact());
        assertEquals("testContactUriUnconvertedParams - 4", 1, (int) jsCard.getLinks().get("CONTACT-1").getPref());
        assertNull("testContactUriUnconvertedParams - 5", jsCard.getLinks().get("CONTACT-1").getMediaType());
        assertFalse("testContactUriUnconvertedParams - 6",jsCard.getLinks().get("CONTACT-1").hasContext());
        assertEquals("testContactUriUnconvertedParams - 7", "test",jsCard.getVCard().getConvertedProperties().get("links/CONTACT-1").getParameters().get("x-param").getValue());
        assertEquals("testContactUriUnconvertedParams - 8", "contact-uri",jsCard.getVCard().getConvertedProperties().get("links/CONTACT-1").getName());
    }

    @Test
    public void testUrlUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "URL;PID=1:http://example.org/restaurant.french/~chezchic.htm\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testUrlUnconvertedParams - 1", 1, jsCard.getLinks().size());
        assertEquals("testUrlUnconvertedParams - 2", "http://example.org/restaurant.french/~chezchic.htm", jsCard.getLinks().get("LINK-1").getUri());
        assertTrue("testUrlUnconvertedParams - 3",jsCard.getLinks().get("LINK-1").isGenericLink());
        assertNull("testUrlUnconvertedParams - 4", jsCard.getLinks().get("LINK-1").getPref());
        assertEquals("testUrlUnconvertedParams - 5", MimeTypeUtils.MIME_TEXT_HTML, jsCard.getLinks().get("LINK-1").getMediaType());
        assertFalse("testUrlUnconvertedParams - 6",jsCard.getLinks().get("LINK-1").hasContext());
        assertEquals("testUrlUnconvertedParams - 7", "1",jsCard.getVCard().getConvertedProperties().get("links/LINK-1").getParameters().get("pid").getValue());
        assertEquals("testUrlUnconvertedParams - 8", "url",jsCard.getVCard().getConvertedProperties().get("links/LINK-1").getName());
    }

    @Test
    public void testKeyUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "KEY;PID=1:http://www.example.com/keys/jdoe.cer\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testKeyUnconvertedParams - 1", 1, jsCard.getCryptoKeys().size());
        assertEquals("testKeyUnconvertedParams - 2", "http://www.example.com/keys/jdoe.cer", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertNull("testKeyUnconvertedParams - 3", jsCard.getCryptoKeys().get("KEY-1").getPref());
        assertNull("testKeyUnconvertedParams - 4", jsCard.getCryptoKeys().get("KEY-1").getMediaType());
        assertFalse("testKeyUnconvertedParams - 5",jsCard.getCryptoKeys().get("KEY-1").hasContext());
        assertEquals("testUrlUnconvertedParams - 6", "1",jsCard.getVCard().getConvertedProperties().get("cryptoKeys/KEY-1").getParameters().get("pid").getValue());
        assertEquals("testKeyUnconvertedParams - 7", "key",jsCard.getVCard().getConvertedProperties().get("cryptoKeys/KEY-1").getName());
    }
    
    @Test
    public void testOrgDirectoryUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG-DIRECTORY;PREF=1:ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\n" +
                "ORG-DIRECTORY;PID=1;INDEX=1:http://directory.mycompany.example.com\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testOrgDirectoryUnconvertedParams - 1", 2, jsCard.getDirectories().size());
        assertEquals("testOrgDirectoryUnconvertedParams - 2", "ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering", jsCard.getDirectories().get("DIRECTORY-1").getUri());
        assertEquals("testOrgDirectoryUnconvertedParams - 3", 1, (int) jsCard.getDirectories().get("DIRECTORY-1").getPref());
        assertNull("testOrgDirectoryUnconvertedParams - 4", jsCard.getDirectories().get("DIRECTORY-1").getMediaType());
        assertTrue("testOrgDirectoryUnconvertedParams - 5", jsCard.getDirectories().get("DIRECTORY-1").isDirectory());
        assertFalse("testOrgDirectoryUnconvertedParams - 6", jsCard.getDirectories().get("DIRECTORY-1").hasContext());
        assertEquals("testOrgDirectoryUnconvertedParams - 7", "http://directory.mycompany.example.com", jsCard.getDirectories().get("DIRECTORY-2").getUri());
        assertTrue("testOrgDirectoryUnconvertedParams - 8", jsCard.getDirectories().get("DIRECTORY-2").isDirectory());
        assertNull("testOrgDirectoryUnconvertedParams - 9", jsCard.getDirectories().get("DIRECTORY-2").getPref());
        assertNull("testOrgDirectoryUnconvertedParams - 10", jsCard.getDirectories().get("DIRECTORY-2").getMediaType());
        assertFalse("testOrgDirectoryUnconvertedParams - 11", jsCard.getDirectories().get("DIRECTORY-2").hasContext());
        assertEquals("testOrgDirectoryUnconvertedParams - 12", 1, (int) jsCard.getDirectories().get("DIRECTORY-2").getListAs());
        assertEquals("testOrgDirectoryUnconvertedParams - 13", "1",jsCard.getVCard().getConvertedProperties().get("directories/DIRECTORY-2").getParameters().get("pid").getValue());
        assertEquals("testOrgDirectoryUnconvertedParams - 14", "org-directory",jsCard.getVCard().getConvertedProperties().get("directories/DIRECTORY-2").getName());
    }

    @Test
    public void testFbUrlUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "FBURL;PID=1;PREF=1:http://www.example.com/busy/janedoe\n" +
                "FBURL;MEDIATYPE=text/calendar:ftp://example.com/busy/project-a.ifb\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testFbUrlUnconvertedParams - 1", 2, jsCard.getCalendars().size());
        assertEquals("testFbUrlUnconvertedParams - 2", "http://www.example.com/busy/janedoe", jsCard.getCalendars().get("FREEBUSY-1").getUri());
        assertTrue("testFbUrlUnconvertedParams - 3",jsCard.getCalendars().get("FREEBUSY-1").isFreeBusy());
        assertEquals("testFbUrlUnconvertedParams - 4", 1, (int) jsCard.getCalendars().get("FREEBUSY-1").getPref());
        assertNull("testFbUrlUnconvertedParams - 5", jsCard.getCalendars().get("FREEBUSY-1").getMediaType());
        assertFalse("testFbUrlUnconvertedParams - 6",jsCard.getCalendars().get("FREEBUSY-1").hasContext());
        assertEquals("testFbUrlUnconvertedParams - 7", "ftp://example.com/busy/project-a.ifb", jsCard.getCalendars().get("FREEBUSY-2").getUri());
        assertTrue("testFbUrlUnconvertedParams - 8",jsCard.getCalendars().get("FREEBUSY-2").isFreeBusy());
        assertNull("testFbUrlUnconvertedParams - 9", jsCard.getCalendars().get("FREEBUSY-2").getPref());
        assertFalse("testFbUrlUnconvertedParams - 10",jsCard.getCalendars().get("FREEBUSY-2").hasContext());
        assertEquals("testFbUrlUnconvertedParams - 11", "text/calendar", jsCard.getCalendars().get("FREEBUSY-2").getMediaType());
        assertEquals("testFbUrlUnconvertedParams - 12", "1",jsCard.getVCard().getConvertedProperties().get("calendars/FREEBUSY-1").getParameters().get("pid").getValue());
        assertEquals("testFbUrlUnconvertedParams - 13", "fburl",jsCard.getVCard().getConvertedProperties().get("calendars/FREEBUSY-1").getName());
    }

    @Test
    public void testCaluriUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALURI;PID=1;PREF=1:http://cal.example.com/calA\n" +
                "CALURI;MEDIATYPE=text/calendar:ftp://ftp.example.com/calA.ics\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCaluriUnconvertedParams - 1", 2, jsCard.getCalendars().size());
        assertEquals("testCaluriUnconvertedParams - 2", "http://cal.example.com/calA", jsCard.getCalendars().get("CALENDAR-1").getUri());
        assertTrue("testCaluriUnconvertedParams - 3",jsCard.getCalendars().get("CALENDAR-1").isCalendar());
        assertEquals("testCaluriUnconvertedParams - 4", 1, (int) jsCard.getCalendars().get("CALENDAR-1").getPref());
        assertNull("testCaluriUnconvertedParams - 5", jsCard.getCalendars().get("CALENDAR-1").getMediaType());
        assertFalse("testCaluriUnconvertedParams - 6",jsCard.getCalendars().get("CALENDAR-1").hasContext());
        assertEquals("testCaluriUnconvertedParams - 7", "ftp://ftp.example.com/calA.ics", jsCard.getCalendars().get("CALENDAR-2").getUri());
        assertTrue("testCaluriUnconvertedParams - 8",jsCard.getCalendars().get("CALENDAR-2").isCalendar());
        assertNull("testCaluriUnconvertedParams - 9", jsCard.getCalendars().get("CALENDAR-2").getPref());
        assertFalse("testCaluriUnconvertedParams - 10",jsCard.getCalendars().get("CALENDAR-2").hasContext());
        assertEquals("testCaluriUnconvertedParams - 11", "text/calendar", jsCard.getCalendars().get("CALENDAR-2").getMediaType());
        assertEquals("testCaluriUnconvertedParams - 12", "1",jsCard.getVCard().getConvertedProperties().get("calendars/CALENDAR-1").getParameters().get("pid").getValue());
        assertEquals("testCaluriUnconvertedParams - 13", "caluri",jsCard.getVCard().getConvertedProperties().get("calendars/CALENDAR-1").getName());
    }

    @Test
    public void testCaladruriUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALADRURI;PID=1;PREF=1:mailto:janedoe@example.com\n" +
                "CALADRURI:http://example.com/calendar/jdoe\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCaladruriUnconvertedParams - 1", 2, jsCard.getSchedulingAddresses().size());
        assertEquals("testCaladruriUnconvertedParams - 2", "mailto:janedoe@example.com", jsCard.getSchedulingAddresses().get("SCHEDULING-1").getUri());
        assertEquals("testCaladruriUnconvertedParams - 3", 1, (int) jsCard.getSchedulingAddresses().get("SCHEDULING-1").getPref());
        assertEquals("testCaladruriUnconvertedParams - 4", "http://example.com/calendar/jdoe", jsCard.getSchedulingAddresses().get("SCHEDULING-2").getUri());
        assertNull("testCaladruriUnconvertedParams - 5", jsCard.getSchedulingAddresses().get("SCHEDULING-2").getPref());
        assertEquals("testCaladruriUnconvertedParams - 6", "1",jsCard.getVCard().getConvertedProperties().get("schedulingAddresses/SCHEDULING-1").getParameters().get("pid").getValue());
        assertEquals("testCaladruriUnconvertedParams - 7", "caladruri",jsCard.getVCard().getConvertedProperties().get("schedulingAddresses/SCHEDULING-1").getName());
    }

    @Test
    public void testImppUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "IMPP;PID=1;TYPE=home;PREF=1:xmpp:alice@example.com\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testImppUnconvertedParams - 1", 1, jsCard.getOnlineServices().size());
        assertEquals("testImppUnconvertedParams - 2", "xmpp:alice@example.com", jsCard.getOnlineServices().get("OS-1").getUri());
        assertTrue("testImppUnconvertedParams - 3",jsCard.getOnlineServices().get("OS-1").asPrivate());
        assertEquals("testImppUnconvertedParams - 4", 1, (int) jsCard.getOnlineServices().get("OS-1").getPref());
        assertEquals("testImppUnconvertedParams - 5", "1",jsCard.getVCard().getConvertedProperties().get("onlineServices/OS-1").getParameters().get("pid").getValue());
        assertEquals("testImppUnconvertedParams - 6", "impp", jsCard.getVCard().getConvertedProperties().get("onlineServices/OS-1").getName());
    }

    @Test
    public void testSocialProfileUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOCIALPROFILE;PID=1;SERVICE-TYPE=\"Twitter\";TYPE=home;PREF=1:https://twitter.com/ietf\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSocialProfileUnconvertedParams - 1", 1, jsCard.getOnlineServices().size());
        assertEquals("testSocialProfileUnconvertedParams - 2", "https://twitter.com/ietf", jsCard.getOnlineServices().get("OS-1").getUri());
        assertTrue("testSocialProfileUnconvertedParams - 3",jsCard.getOnlineServices().get("OS-1").asPrivate());
        assertEquals("testSocialProfileUnconvertedParams - 4", 1, (int) jsCard.getOnlineServices().get("OS-1").getPref());
        assertEquals("testSocialProfileUnconvertedParams - 5", "Twitter", jsCard.getOnlineServices().get("OS-1").getService());
        assertEquals("testSocialProfileUnconvertedParams - 6", "1",jsCard.getVCard().getConvertedProperties().get("onlineServices/OS-1").getParameters().get("pid").getValue());
        assertEquals("testSocialProfileUnconvertedParams - 7", "socialprofile",jsCard.getVCard().getConvertedProperties().get("onlineServices/OS-1").getName());
    }

    @Test
    public void testCreatedUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CREATED;PID=1;VALUE=timestamp:20101010T101010Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCreatedUnconvertedParams - 1", 0, jsCard.getCreated().compareTo(DateUtils.toCalendar("2010-10-10T10:10:10Z")));
        assertEquals("testCreatedUnconvertedParams - 2", "1",jsCard.getVCard().getConvertedProperties().get("created").getParameters().get("pid").getValue());
        assertEquals("testCreatedUnconvertedParams - 3", "created",jsCard.getVCard().getConvertedProperties().get("created").getName());
    }

    @Test
    public void testLanguageUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANGUAGE;X-PARAM=test:it\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testLanguageUnconvertedParams - 1", "it", jsCard.getLanguage());
        assertEquals("testLanguageUnconvertedParams - 2", "test",jsCard.getVCard().getConvertedProperties().get("language").getParameters().get("x-param").getValue());
        assertEquals("testLanguageUnconvertedParams - 3", "language",jsCard.getVCard().getConvertedProperties().get("language").getName());
    }
    
    @Test
    public void testBdayUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY;X-PARAM=test:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testBdayUnconvertedParams - 1", jsCard.getAnniversaries());
        assertEquals("testBdayUnconvertedParams - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testBdayUnconvertedParams - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testBdayUnconvertedParams - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testBdayUnconvertedParams - 5", "test",jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1").getParameters().get("x-param").getValue());
        assertEquals("testBdayUnconvertedParams - 6","bday", jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1").getName());
    }

    @Test
    public void testBirthplaceUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE;X-PARAM=test:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testBirthplaceUnconvertedParams - 1", jsCard.getAnniversaries());
        assertEquals("testBirthplaceUnconvertedParams - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testBirthplaceUnconvertedParams - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testBirthplaceUnconvertedParams - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testBirthplaceUnconvertedParams - 5", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFull());
        assertEquals("testBirthplaceUnconvertedParams - 6", "test",jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1/place").getParameters().get("x-param").getValue());
        assertEquals("testBirthplaceUnconvertedParams - 7","birthplace", jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1/place").getName());
    }

    @Test
    public void testDeathdateUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE;X-PARAM=test:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testDeathdateUnconvertedParams - 1", jsCard.getAnniversaries());
        assertEquals("testDeathdateUnconvertedParams - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testDeathdateUnconvertedParams - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testDeathdateUnconvertedParams - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isDeath());
        assertEquals("testDeathdateUnconvertedParams - 5", "test",jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1").getParameters().get("x-param").getValue());
        assertEquals("testDeathdateUnconvertedParams - 6","deathdate", jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1").getName());
    }

    @Test
    public void testDeathplaceUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE;X-PARAM=test:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testDeathplaceUnconvertedParams - 1", jsCard.getAnniversaries());
        assertEquals("testDeathplaceUnconvertedParams - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testDeathplaceUnconvertedParams - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testDeathplaceUnconvertedParams - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isDeath());
        assertEquals("testDeathplaceUnconvertedParams - 5", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFull());
        assertEquals("testDeathplaceUnconvertedParams - 6", "test",jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1/place").getParameters().get("x-param").getValue());
        assertEquals("testDeathplaceUnconvertedParams - 7","deathplace", jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-1/place").getName());
    }

    @Test
    public void testAnniversaryUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE;VALUE=uri:geo:34.15876,-118.45728\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "ANNIVERSARY;X-PARAM=test:19860201T190000Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaryUnconvertedParams - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaryUnconvertedParams - 2", 3, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaryUnconvertedParams - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversaryUnconvertedParams - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversaryUnconvertedParams - 5", "geo:34.15876,-118.45728", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getCoordinates());
        assertTrue("testAnniversaryUnconvertedParams - 6",jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversaryUnconvertedParams - 7",jsCard.getAnniversaries().get("ANNIVERSARY-2").isDeath());
        assertEquals("testAnniversaryUnconvertedParams - 8", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-2").getPlace().getFull());
        assertTrue("testAnniversaryUnconvertedParams - 9",jsCard.getAnniversaries().get("ANNIVERSARY-3").isWedding());
        assertTrue("testAnniversaryUnconvertedParams - 10",jsCard.getAnniversaries().get("ANNIVERSARY-3").getDate().isEqual("1986-02-01T19:00:00Z"));
        assertEquals("testAnniversaryUnconvertedParams - 11", "test",jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-3").getParameters().get("x-param").getValue());
        assertEquals("testAnniversaryUnconvertedParams - 12","anniversary", jsCard.getVCard().getConvertedProperties().get("anniversaries/ANNIVERSARY-3").getName());
    }

    @Test
    public void testGramgenderPronounsUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANGUAGE;VALUE=language-tag:en\n" +
                "PRONOUNS;X-PARAM1=test1;ALTID=1;JSID=PRONOUNS-1;VALUE=text:he/him\n" +
                "PRONOUNS;X-PARAM1IT=test1IT;LANGUAGE=it;ALTID=1;VALUE=text:egli/lui\n" +
                "GRAMGENDER;X-PARAM2=test2;VALUE=text:INANIMATE\n" +
                "GRAMGENDER;X-PARAM2IT=test2IT;LANGUAGE=it;VALUE=text:MASCULINE\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testGramgenderPronounsUnconvertedParams - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertTrue("testGramgenderPronounsUnconvertedParams - 2", jsCard.getSpeakToAs().isInanimate());
        assertEquals("testGramgenderPronounsUnconvertedParams - 3", 1, jsCard.getLocalizations().size());
        assertEquals("testGramgenderPronounsUnconvertedParams - 4", "egli/lui", jsCard.getLocalizations().get("it").get("speakToAs/pronouns/PRONOUNS-1").get("pronouns").asText());
        assertEquals("testGramgenderPronounsUnconvertedParams - 5", GrammaticalGenderType.MASCULINE, GrammaticalGenderType.getEnum(jsCard.getLocalizations().get("it").get("speakToAs/grammaticalGender").asText().toLowerCase()));
        assertEquals("testGramgenderPronounsUnconvertedParams - 6", "test1",jsCard.getVCard().getConvertedProperties().get("speakToAs/pronouns/PRONOUNS-1").getParameters().get("x-param1").getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 7","pronouns", jsCard.getVCard().getConvertedProperties().get("speakToAs/pronouns/PRONOUNS-1").getName());
        assertEquals("testGramgenderPronounsUnconvertedParams - 8", "test1IT",jsCard.getVCard().getConvertedProperties().get("localizations/it/speakToAs~1pronouns~1PRONOUNS-1").getParameters().get("x-param1it").getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 9","pronouns", jsCard.getVCard().getConvertedProperties().get("localizations/it/speakToAs~1pronouns~1PRONOUNS-1").getName());
        assertEquals("testGramgenderPronounsUnconvertedParams - 10", "test2",jsCard.getVCard().getConvertedProperties().get("speakToAs/grammaticalGender").getParameters().get("x-param2").getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 11","gramgender", jsCard.getVCard().getConvertedProperties().get("speakToAs/grammaticalGender").getName());
        assertEquals("testGramgenderPronounsUnconvertedParams - 12", "test2IT",jsCard.getVCard().getConvertedProperties().get("localizations/it/speakToAs~1grammaticalGender").getParameters().get("x-param2it").getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 13","gramgender", jsCard.getVCard().getConvertedProperties().get("localizations/it/speakToAs~1grammaticalGender").getName());
    }

    @Test
    public void testTitleUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;PID=1;ALTID=1:Research Scientist\n" +
                "TITLE;PID=2;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testTitleUnconvertedParams - 1", jsCard.getTitles());
        assertEquals("testTitleUnconvertedParams - 2", 1, jsCard.getTitles().size());
        assertEquals("testTitleUnconvertedParams - 3", "Research Scientist", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testTitleUnconvertedParams - 4", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleUnconvertedParams - 5", "Ricercatore", jsCard.getLocalization("it", "titles/TITLE-1").get("name").asText());
        assertEquals("testTitleUnconvertedParams - 6", "1",jsCard.getVCard().getConvertedProperties().get("titles/TITLE-1").getParameters().get("pid").getValue());
        assertEquals("testTitleUnconvertedParams - 7","title", jsCard.getVCard().getConvertedProperties().get("titles/TITLE-1").getName());
        assertEquals("testTitleUnconvertedParams - 8", "2",jsCard.getVCard().getConvertedProperties().get("localizations/it/titles~1TITLE-1").getParameters().get("pid").getValue());
        assertEquals("testTitleUnconvertedParams - 9","title", jsCard.getVCard().getConvertedProperties().get("localizations/it/titles~1TITLE-1").getName());
    }

    @Test
    public void testRoleUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;PID=1;ALTID=1:Project Leader\n" +
                "ROLE;PID=2;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testRoleUnconvertedParams - 1", jsCard.getTitles());
        assertEquals("testRoleUnconvertedParams - 2", 1, jsCard.getTitles().size());
        assertEquals("testRoleUnconvertedParams - 3", "Project Leader", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testRoleUnconvertedParams - 4", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testRoleUnconvertedParams - 5", "Capo Progetto", jsCard.getLocalization("it", "titles/TITLE-1").get("name").asText());
        assertEquals("testTitleUnconvertedParams - 6", "1",jsCard.getVCard().getConvertedProperties().get("titles/TITLE-1").getParameters().get("pid").getValue());
        assertEquals("testTitleUnconvertedParams - 7","role", jsCard.getVCard().getConvertedProperties().get("titles/TITLE-1").getName());
        assertEquals("testTitleUnconvertedParams - 8", "2",jsCard.getVCard().getConvertedProperties().get("localizations/it/titles~1TITLE-1").getParameters().get("pid").getValue());
        assertEquals("testTitleUnconvertedParams - 9","role", jsCard.getVCard().getConvertedProperties().get("localizations/it/titles~1TITLE-1").getName());
    }

    @Test
    public void testNoteUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "NOTE;PID=2;ALTID=1:This fax number is operational 0800 to 1715 EST, Mon-Fri\n" +
                "NOTE;ALTID=1;LANGUAGE=it:Questo numero di fax e' operativo dalle 8.00 alle 17.15, Lun-Ven\n" +
                "NOTE;PID=1:This is another note\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testNoteUnconvertedParams - 1", 2, jsCard.getNotes().size());
        assertEquals("testNoteUnconvertedParams - 2", "This is another note", jsCard.getNotes().get("NOTE-1").getNote());
        assertEquals("testNoteUnconvertedParams - 3", "This fax number is operational 0800 to 1715 EST, Mon-Fri", jsCard.getNotes().get("NOTE-2").getNote());
        assertEquals("testNoteUnconvertedParams - 4", 1, jsCard.getLocalizations().size());
        assertEquals("testNoteUnconvertedParams - 5", "Note", jsCard.getLocalizations().get("it").get("notes/NOTE-2").get("@type").asText());
        assertEquals("testNoteUnconvertedParams - 6", "Questo numero di fax e' operativo dalle 8.00 alle 17.15, Lun-Ven", jsCard.getLocalizations().get("it").get("notes/NOTE-2").get("note").asText());
        assertEquals("testNoteUnconvertedParams - 7", "1",jsCard.getVCard().getConvertedProperties().get("notes/NOTE-1").getParameters().get("pid").getValue());
        assertEquals("testNoteUnconvertedParams - 8","note", jsCard.getVCard().getConvertedProperties().get("notes/NOTE-1").getName());
        assertEquals("testNoteUnconvertedParams - 9", "2",jsCard.getVCard().getConvertedProperties().get("notes/NOTE-2").getParameters().get("pid").getValue());
        assertEquals("testNoteUnconvertedParams - 10","note", jsCard.getVCard().getConvertedProperties().get("notes/NOTE-2").getName());
    }


    @Test
    public void testNicknameUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME;PID=1;ALTID=1:Johnny\n" +
                "NICKNAME;PREF=1;ALTID=2:Kid\n" +
                "NICKNAME;PID=2;LANGUAGE=it;ALTID=1:Giovannino\n" +
                "NICKNAME;PREF=1;LANGUAGE=it;ALTID=2:Ragazzo\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testNicknameUnconvertedParams - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testNicknameUnconvertedParams - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testNicknameUnconvertedParams - 3",jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testNicknameUnconvertedParams - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testNicknameUnconvertedParams - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testNicknameUnconvertedParams - 6", "John", jsCard.getName().getGiven());
        assertTrue("testNicknameUnconvertedParams - 7",jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testNicknameUnconvertedParams - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testNicknameUnconvertedParams - 9",jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testNicknameUnconvertedParams - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testNicknameUnconvertedParams - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testNicknameUnconvertedParams - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testNicknameUnconvertedParams - 12", 2, jsCard.getNicknames().size());
        assertEquals("testNicknameUnconvertedParams - 13", "Johnny", jsCard.getNicknames().get("NICK-1").getName());
        assertEquals("testNicknameUnconvertedParams - 14", "Kid", jsCard.getNicknames().get("NICK-2").getName());
        assertEquals("testNicknameUnconvertedParams - 15", "Giovannino", jsCard.getLocalization("it", "nicknames/NICK-1").get("name").asText());
        assertEquals("testNicknameUnconvertedParams - 16", "Ragazzo", jsCard.getLocalization("it", "nicknames/NICK-2").get("name").asText());
        assertEquals("testNicknameUnconvertedParams - 17", "1",jsCard.getVCard().getConvertedProperties().get("nicknames/NICK-1").getParameters().get("pid").getValue());
        assertEquals("testNicknameUnconvertedParams - 18","nickname", jsCard.getVCard().getConvertedProperties().get("nicknames/NICK-1").getName());
        assertEquals("testNicknameUnconvertedParams - 19", "2",jsCard.getVCard().getConvertedProperties().get("localizations/it/nicknames~1NICK-1").getParameters().get("pid").getValue());
        assertEquals("testNicknameUnconvertedParams - 20","nickname", jsCard.getVCard().getConvertedProperties().get("localizations/it/nicknames~1NICK-1").getName());
    }


    @Test
    public void testAdrUnconvertedParams1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;PID=1;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;PID=2;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;PID=3;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAdrUnconvertedParams1 - 1", jsCard.getAddresses());
        assertEquals("testAdrUnconvertedParams1 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAdrUnconvertedParams1 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAdrUnconvertedParams1 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAdrUnconvertedParams1 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAdrUnconvertedParams1 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAdrUnconvertedParams1 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAdrUnconvertedParams1 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetName());
        assertEquals("testAdrUnconvertedParams1 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFull());
        assertEquals("testAdrUnconvertedParams1 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAdrUnconvertedParams1 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAdrUnconvertedParams1 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAdrUnconvertedParams1 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAdrUnconvertedParams1 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAdrUnconvertedParams1 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testAdrUnconvertedParams1 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFull());
        assertNotNull("testAdrUnconvertedParams1 - 17", jsCard.getLocalization("it", "addresses/ADR-2"));
        assertEquals("testAdrUnconvertedParams1 - 18", "1",jsCard.getVCard().getConvertedProperties().get("addresses/ADR-1").getParameters().get("pid").getValue());
        assertEquals("testAdrUnconvertedParams1 - 19","adr", jsCard.getVCard().getConvertedProperties().get("addresses/ADR-1").getName());
        assertEquals("testAdrUnconvertedParams1 - 20", "2",jsCard.getVCard().getConvertedProperties().get("addresses/ADR-2").getParameters().get("pid").getValue());
        assertEquals("testAdrUnconvertedParams1 - 21","adr", jsCard.getVCard().getConvertedProperties().get("addresses/ADR-2").getName());
        assertEquals("testAdrUnconvertedParams1 - 22", "3",jsCard.getVCard().getConvertedProperties().get("localizations/it/addresses~1ADR-2").getParameters().get("pid").getValue());
        assertEquals("testAdrUnconvertedParams1 - 23","adr", jsCard.getVCard().getConvertedProperties().get("localizations/it/addresses~1ADR-2").getName());
    }

    @Test
    public void testOrgUnconvertedParams() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG;PID=1;ALTID=1:;North American Division;Marketing\n" +
                "ORG;PID=2;ALTID=1;LANGUAGE=it:;Divisione Nord America;Marketing\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testOrgUnconvertedParams - 1", jsCard.getOrganizations());
        assertEquals("testOrgUnconvertedParams - 2", 1, jsCard.getOrganizations().size());
        assertNull("testOrgUnconvertedParams - 3", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testOrgUnconvertedParams - 4", 2, jsCard.getOrganizations().get("ORG-1").getUnits().length);
        assertEquals("testOrgUnconvertedParams - 5", "North American Division", jsCard.getOrganizations().get("ORG-1").getUnits()[0].getName());
        assertEquals("testOrgUnconvertedParams - 6", "Marketing", jsCard.getOrganizations().get("ORG-1").getUnits()[1].getName());
        assertNull("testOrgUnconvertedParams - 7", jsCard.getLocalization("it", "organizations/ORG-1").get("name"));
        assertEquals("testOrgUnconvertedParams - 8", "Divisione Nord America", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(0).get("name").asText());
        assertEquals("testOrgUnconvertedParams - 9", "Marketing", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(1).get("name").asText());
        assertEquals("testOrgUnconvertedParams - 10", "1",jsCard.getVCard().getConvertedProperties().get("organizations/ORG-1").getParameters().get("pid").getValue());
        assertEquals("testOrgUnconvertedParams - 11","org", jsCard.getVCard().getConvertedProperties().get("organizations/ORG-1").getName());
        assertEquals("testOrgUnconvertedParams - 12", "2",jsCard.getVCard().getConvertedProperties().get("localizations/it/organizations~1ORG-1").getParameters().get("pid").getValue());
        assertEquals("testOrgUnconvertedParams - 13","org", jsCard.getVCard().getConvertedProperties().get("localizations/it/organizations~1ORG-1").getName());
    }
    
    @Test //ez-vcard accepts only one family name and one given name
    public void testFnAndNUnconvertedParams1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;PREF=1:John Paul Philip Stevenson\n" +
                "N;PID=1:Stevenson;John;Philip,Paul;Dr.;Jr.,M.D.,A.C.P.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testFnAndNUnconvertedParams1 - 1", 8, jsCard.getName().getComponents().length);
        assertEquals("testFnAndNUnconvertedParams1 - 2", "Dr.", jsCard.getName().getComponents()[4].getValue());
        assertTrue("testFnAndNUnconvertedParams1 - 3",  jsCard.getName().getComponents()[4].isTitle());
        assertEquals("testFnAndNUnconvertedParams1 - 5", "John", jsCard.getName().getGiven());
        assertTrue("testFnAndNUnconvertedParams1 - 6",  jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testFnAndNUnconvertedParams1 - 8", "Stevenson", jsCard.getName().getSurname());
        assertTrue("testFnAndNUnconvertedParams1 - 9",  jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testFnAndNUnconvertedParams1 - 11", "Philip", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testFnAndNUnconvertedParams1 - 12",  jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testFnAndNUnconvertedParams1 - 14", "Paul", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testFnAndNUnconvertedParams1 - 15",  jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testFnAndNUnconvertedParams1 - 17", "Jr.", jsCard.getName().getComponents()[5].getValue());
        assertTrue("testFnAndNUnconvertedParams1 - 18",  jsCard.getName().getComponents()[5].isCredential());
        assertEquals("testFnAndNUnconvertedParams1 - 20", "M.D.", jsCard.getName().getComponents()[6].getValue());
        assertTrue("testFnAndNUnconvertedParams1 - 21",  jsCard.getName().getComponents()[6].isCredential());
        assertEquals("testFnAndNUnconvertedParams1 - 23", "A.C.P.", jsCard.getName().getComponents()[7].getValue());
        assertTrue("testFnAndNUnconvertedParams1 - 24",  jsCard.getName().getComponents()[7].isCredential());
        assertEquals("testFnAndNUnconvertedParams1 - 10", "1",jsCard.getVCard().getConvertedProperties().get("name/full").getParameters().get("pref").getValue());
        assertEquals("testFnAndNUnconvertedParams1 - 11","fn", jsCard.getVCard().getConvertedProperties().get("name/full").getName());
        assertEquals("testFnAndNUnconvertedParams1 - 12", "1",jsCard.getVCard().getConvertedProperties().get("name/components").getParameters().get("pid").getValue());
        assertEquals("testFnAndNUnconvertedParams1 - 13","n", jsCard.getVCard().getConvertedProperties().get("name/components").getName());

    }
    
    @Test
    public void testFnAndNUnconvertedParams2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;PID=1;DERIVED=true;ALTID=1:Mr. Ivan Petrovich Vasiliev\n" +
                "FN;PID=2;DERIVED=true;LANGUAGE=uk-Cyrl;ALTID=1:г-н Иван Петрович Васильев\n" +
                "N;PID=3;ALTID=1:Vasiliev;Ivan;Petrovich;Mr.;;;\n" +
                "N;PID=4;LANGUAGE=uk-Cyrl;ALTID=1:Васильев;Иван;Петрович;г-н;;;\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testFnAndNUnconvertedParams2 - 1", 4, jsCard.getName().getComponents().length);
        assertEquals("testFnAndNUnconvertedParams2 - 2", "Vasiliev", jsCard.getName().getSurname());
        assertEquals("testFnAndNUnconvertedParams2 - 3", "Ivan", jsCard.getName().getGiven());
        assertEquals("testFnAndNUnconvertedParams2 - 4", "Petrovich", jsCard.getName().getGiven2());
        assertTrue("testFnAndNUnconvertedParams2 - 5",  jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testFnAndNUnconvertedParams2 - 6", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testFnAndNUnconvertedParams2 - 7", 1, jsCard.getLocalizationsPerLanguage("uk-Cyrl").size());
        assertNotNull("testFnAndNUnconvertedParams2 - 8",  jsCard.getLocalization("uk-Cyrl","name"));
        Name nameLocalization = (Name) JsonNodeUtils.toObject(jsCard.getLocalization("uk-Cyrl","name"), Name.class);
        assertEquals("testFnAndNUnconvertedParams2 - 9", "Васильев", nameLocalization.getSurname());
        assertEquals("testFnAndNUnconvertedParams2 - 10", "Иван", nameLocalization.getGiven());
        assertEquals("testFnAndNUnconvertedParams2 - 11", "Петрович", nameLocalization.getGiven2());
        assertTrue("testFnAndNUnconvertedParams2 - 12",  nameLocalization.getComponents()[3].isTitle());
        assertEquals("testFnAndNUnconvertedParams2 - 13", "г-н", nameLocalization.getComponents()[3].getValue());
        assertEquals("testFnAndNUnconvertedParams2 - 14", "1",jsCard.getVCard().getConvertedProperties().get("name/full").getParameters().get("pid").getValue());
        assertEquals("testFnAndNUnconvertedParams2 - 15","fn", jsCard.getVCard().getConvertedProperties().get("name/full").getName());
        assertEquals("testFnAndNUnconvertedParams2 - 16", "2",jsCard.getVCard().getConvertedProperties().get("localizations/uk-Cyrl/name~1full").getParameters().get("pid").getValue());
        assertEquals("testFnAndNUnconvertedParams2 - 17","fn", jsCard.getVCard().getConvertedProperties().get("localizations/uk-Cyrl/name~1full").getName());
        assertEquals("testFnAndNUnconvertedParams2 - 18", "3",jsCard.getVCard().getConvertedProperties().get("name/components").getParameters().get("pid").getValue());
        assertEquals("testFnAndNUnconvertedParams2 - 19","n", jsCard.getVCard().getConvertedProperties().get("name/components").getName());
        assertEquals("testFnAndNUnconvertedParams2 - 20", "4",jsCard.getVCard().getConvertedProperties().get("localizations/uk-Cyrl/name~1components").getParameters().get("pid").getValue());
        assertEquals("testFnAndNUnconvertedParams2 - 21","n", jsCard.getVCard().getConvertedProperties().get("localizations/uk-Cyrl/name~1components").getName());
    }

}
