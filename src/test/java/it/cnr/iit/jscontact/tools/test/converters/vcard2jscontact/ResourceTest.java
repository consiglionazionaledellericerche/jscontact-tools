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
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest extends VCard2JSContactTest {
    
    @Test
    public void testResourceValid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOURCE:http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid1 - 1", 1, jsCard.getResources().size());
        assertEquals("testResourceValid1 - 2", "http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf", jsCard.getResources().get("SOURCE-1").getResource());
        assertTrue("testResourceValid1 - 3",jsCard.getResources().get("SOURCE-1").isDirectorySource());
        assertNull("testResourceValid1 - 4", jsCard.getResources().get("SOURCE-1").getPref());
        assertNull("testResourceValid1 - 5", jsCard.getResources().get("SOURCE-1").getMediaType());
        assertTrue("testResourceValid1 - 6",jsCard.getResources().get("SOURCE-1").hasNoContext());
    }

    @Test
    public void testResourceValid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LOGO:http://www.example.com/pub/logos/abccorp.jpg\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid2 - 1", 1, jsCard.getResources().size());
        assertEquals("testResourceValid2 - 2", "http://www.example.com/pub/logos/abccorp.jpg", jsCard.getResources().get("LOGO-1").getResource());
        assertTrue("testResourceValid2 - 3",jsCard.getResources().get("LOGO-1").isLogo());
        assertNull("testResourceValid2 - 4", jsCard.getResources().get("LOGO-1").getPref());
        assertEquals("testResourceValid2 - 5", MimeTypeUtils.MIME_IMAGE_JPEG, jsCard.getResources().get("LOGO-1").getMediaType());
        assertTrue("testResourceValid2 - 6",jsCard.getResources().get("LOGO-1").hasNoContext());
    }

    @Test
    public void testResourceValid3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CONTACT-URI;PREF=1:mailto:contact@example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid3 - 1", 1, jsCard.getResources().size());
        assertEquals("testResourceValid3 - 2", "mailto:contact@example.com", jsCard.getResources().get("CONTACT-URI-1").getResource());
        assertTrue("testResourceValid3 - 3",jsCard.getResources().get("CONTACT-URI-1").isContactUri());
        assertEquals("testResourceValid3 - 4", 1, (int) jsCard.getResources().get("CONTACT-URI-1").getPref());
        assertNull("testResourceValid3 - 5", jsCard.getResources().get("CONTACT-URI-1").getMediaType());
        assertTrue("testResourceValid3 - 6",jsCard.getResources().get("CONTACT-URI-1").hasNoContext());
    }

    @Test
    public void testResourceValid4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG-DIRECTORY;PREF=1:ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\n" +
                "ORG-DIRECTORY;INDEX=1:http://directory.mycompany.example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid4 - 1", 2, jsCard.getResources().size());
        assertEquals("testResourceValid4 - 2", "http://directory.mycompany.example.com", jsCard.getResources().get("ORG-DIRECTORY-1").getResource());
        assertTrue("testResourceValid4 - 3",jsCard.getResources().get("ORG-DIRECTORY-1").isDirectory());
        assertNull("testResourceValid4 - 4", jsCard.getResources().get("ORG-DIRECTORY-1").getPref());
        assertNull("testResourceValid4 - 5", jsCard.getResources().get("ORG-DIRECTORY-1").getMediaType());
        assertTrue("testResourceValid4 - 6",jsCard.getResources().get("ORG-DIRECTORY-1").hasNoContext());
        assertEquals("testResourceValid4 - 7", "ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering", jsCard.getResources().get("ORG-DIRECTORY-2").getResource());
        assertEquals("testResourceValid4 - 8", 1, (int) jsCard.getResources().get("ORG-DIRECTORY-2").getPref());
        assertNull("testResourceValid4 - 9", jsCard.getResources().get("ORG-DIRECTORY-2").getMediaType());
        assertTrue("testResourceValid4 - 10",jsCard.getResources().get("ORG-DIRECTORY-2").isDirectory());
        assertTrue("testResourceValid4 - 11",jsCard.getResources().get("ORG-DIRECTORY-2").hasNoContext());
    }

    @Test
    public void testResourceValid5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG-DIRECTORY;INDEX=2;PREF=1:ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\n" +
                "ORG-DIRECTORY;INDEX=1:http://directory.mycompany.example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid5 - 1", 2, jsCard.getResources().size());
        assertEquals("testResourceValid5 - 2", "http://directory.mycompany.example.com", jsCard.getResources().get("ORG-DIRECTORY-1").getResource());
        assertTrue("testResourceValid5 - 3",jsCard.getResources().get("ORG-DIRECTORY-1").isDirectory());
        assertNull("testResourceValid5 - 4", jsCard.getResources().get("ORG-DIRECTORY-1").getPref());
        assertNull("testResourceValid5 - 5", jsCard.getResources().get("ORG-DIRECTORY-1").getMediaType());
        assertTrue("testResourceValid5 - 6",jsCard.getResources().get("ORG-DIRECTORY-1").hasNoContext());
        assertEquals("testResourceValid5 - 7", "ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering", jsCard.getResources().get("ORG-DIRECTORY-2").getResource());
        assertEquals("testResourceValid5 - 8", 1, (int) jsCard.getResources().get("ORG-DIRECTORY-2").getPref());
        assertNull("testResourceValid5 - 9", jsCard.getResources().get("ORG-DIRECTORY-2").getMediaType());
        assertTrue("testResourceValid5 - 10",jsCard.getResources().get("ORG-DIRECTORY-2").hasNoContext());
        assertTrue("testResourceValid5 - 11",jsCard.getResources().get("ORG-DIRECTORY-2").isDirectory());
    }


    @Test
    public void testResourceValid6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOUND:CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid6 - 1", 1, jsCard.getResources().size());
        assertEquals("testResourceValid6 - 2", "CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com", jsCard.getResources().get("SOUND-1").getResource());
        assertTrue("testResourceValid6 - 3",jsCard.getResources().get("SOUND-1").isAudio());
        assertNull("testResourceValid6 - 4", jsCard.getResources().get("SOUND-1").getPref());
        assertNull("testResourceValid6 - 5", jsCard.getResources().get("SOUND-1").getMediaType());
        assertTrue("testResourceValid6 - 6",jsCard.getResources().get("SOUND-1").hasNoContext());
    }

    @Test
    public void testResourceValid7() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "URL:http://example.org/restaurant.french/~chezchic.htm\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid7 - 1", 1, jsCard.getResources().size());
        assertEquals("testResourceValid7 - 2", "http://example.org/restaurant.french/~chezchic.htm", jsCard.getResources().get("URI-1").getResource());
        assertTrue("testResourceValid7 - 3",jsCard.getResources().get("URI-1").isUri());
        assertNull("testResourceValid7 - 4", jsCard.getResources().get("URI-1").getPref());
        assertEquals("testResourceValid7 - 5", MimeTypeUtils.MIME_TEXT_HTML, jsCard.getResources().get("URI-1").getMediaType());
        assertTrue("testResourceValid7 - 6",jsCard.getResources().get("URI-1").hasNoContext());
    }

    @Test
    public void testResourceValid8() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "KEY:http://www.example.com/keys/jdoe.cer\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid8 - 1", 1, jsCard.getResources().size());
        assertEquals("testResourceValid8 - 2", "http://www.example.com/keys/jdoe.cer", jsCard.getResources().get("KEY-1").getResource());
        assertTrue("testResourceValid8 - 3",jsCard.getResources().get("KEY-1").isPublicKey());
        assertNull("testResourceValid8 - 4", jsCard.getResources().get("KEY-1").getPref());
        assertNull("testResourceValid8 - 5", jsCard.getResources().get("KEY-1").getMediaType());
        assertTrue("testResourceValid8 - 6",jsCard.getResources().get("KEY-1").hasNoContext());
    }


    @Test
    public void testResourceValid9() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "FBURL;PREF=1:http://www.example.com/busy/janedoe\n" +
                "FBURL;MEDIATYPE=text/calendar:ftp://example.com/busy/project-a.ifb\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid9 - 1", 2, jsCard.getResources().size());
        assertEquals("testResourceValid9 - 2", "http://www.example.com/busy/janedoe", jsCard.getResources().get("FBURL-1").getResource());
        assertTrue("testResourceValid9 - 3",jsCard.getResources().get("FBURL-1").isFreeBusy());
        assertEquals("testResourceValid9 - 4", 1, (int) jsCard.getResources().get("FBURL-1").getPref());
        assertNull("testResourceValid9 - 5", jsCard.getResources().get("FBURL-1").getMediaType());
        assertTrue("testResourceValid9 - 6",jsCard.getResources().get("FBURL-1").hasNoContext());
        assertEquals("testResourceValid9 - 7", "ftp://example.com/busy/project-a.ifb", jsCard.getResources().get("FBURL-2").getResource());
        assertTrue("testResourceValid9 - 8",jsCard.getResources().get("FBURL-2").isFreeBusy());
        assertNull("testResourceValid9 - 9", jsCard.getResources().get("FBURL-2").getPref());
        assertTrue("testResourceValid9 - 10",jsCard.getResources().get("FBURL-2").hasNoContext());
        assertEquals("testResourceValid9 - 11", "text/calendar", jsCard.getResources().get("FBURL-2").getMediaType());
    }

    @Test
    public void testResourceValid10() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALURI;PREF=1:http://cal.example.com/calA\n" +
                "CALURI;MEDIATYPE=text/calendar:ftp://ftp.example.com/calA.ics\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResourceValid10 - 1", 2, jsCard.getResources().size());
        assertEquals("testResourceValid10 - 2", "http://cal.example.com/calA", jsCard.getResources().get("CALURI-1").getResource());
        assertTrue("testResourceValid10 - 3",jsCard.getResources().get("CALURI-1").isCalendar());
        assertEquals("testResourceValid10 - 4", 1, (int) jsCard.getResources().get("CALURI-1").getPref());
        assertNull("testResourceValid10 - 5", jsCard.getResources().get("CALURI-1").getMediaType());
        assertTrue("testResourceValid10 - 6",jsCard.getResources().get("CALURI-1").hasNoContext());
        assertEquals("testResourceValid10 - 7", "ftp://ftp.example.com/calA.ics", jsCard.getResources().get("CALURI-2").getResource());
        assertTrue("testResourceValid10 - 8",jsCard.getResources().get("CALURI-2").isCalendar());
        assertNull("testResourceValid10 - 9", jsCard.getResources().get("CALURI-2").getPref());
        assertTrue("testResourceValid10 - 10",jsCard.getResources().get("CALURI-2").hasNoContext());
        assertEquals("testResourceValid10 - 11", "text/calendar", jsCard.getResources().get("CALURI-2").getMediaType());
    }

}
