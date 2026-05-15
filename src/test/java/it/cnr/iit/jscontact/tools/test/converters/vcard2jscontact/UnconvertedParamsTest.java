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
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
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
        assertEquals("testImppUnconvertedParams - 6",jsCard.getVCard().getConvertedProperties().get("onlineServices/OS-1").getName(), "impp");
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
        assertEquals("testSocialProfileUnconvertedParams - 7",jsCard.getVCard().getConvertedProperties().get("onlineServices/OS-1").getName(), "socialprofile");
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
        assertEquals("testCreatedUnconvertedParams - 6", "1",jsCard.getVCard().getConvertedProperties().get("created").getParameters().get("pid").getValue());
        assertEquals("testCreatedUnconvertedParams - 7",jsCard.getVCard().getConvertedProperties().get("created").getName(), "created");
    }


}
