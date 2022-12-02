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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest extends JCard2JSContactTest {
    
    @Test
    public void testResource1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"source\", {}, \"uri\", \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource1 - 1", 1, jsCard.getDirectories().size());
        assertEquals("testResource1 - 2", "http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf", jsCard.getDirectories().get("ENTRY-1").getUri());
        assertTrue("testResource1 - 3",jsCard.getDirectories().get("ENTRY-1").isEntry());
        assertNull("testResource1 - 4", jsCard.getDirectories().get("ENTRY-1").getPref());
        assertNull("testResource1 - 5", jsCard.getDirectories().get("ENTRY-1").getMediaType());
        assertTrue("testResource1 - 6",jsCard.getDirectories().get("ENTRY-1").hasNoContext());
    }

    @Test
    public void testResource2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"logo\", {}, \"uri\", \"http://www.example.com/pub/logos/abccorp.jpg\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource2 - 1", 1, jsCard.getMedia().size());
        assertEquals("testResource2 - 2", "http://www.example.com/pub/logos/abccorp.jpg", jsCard.getMedia().get("LOGO-1").getUri());
        assertTrue("testResource2 - 3",jsCard.getMedia().get("LOGO-1").isLogo());
        assertNull("testResource2 - 4", jsCard.getMedia().get("LOGO-1").getPref());
        assertEquals("testResource2 - 5", MimeTypeUtils.MIME_IMAGE_JPEG, jsCard.getMedia().get("LOGO-1").getMediaType());
        assertTrue("testResource2 - 6",jsCard.getMedia().get("LOGO-1").hasNoContext());
    }


    @Test
    public void testResource3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"contact-uri\", {\"pref\" : 1}, \"uri\", \"mailto:contact@example.com\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource3 - 1", 1, jsCard.getLinks().size());
        assertEquals("testResource3 - 2", "mailto:contact@example.com", jsCard.getLinks().get("CONTACT-1").getUri());
        assertTrue("testResource3 - 3",jsCard.getLinks().get("CONTACT-1").isContact());
        assertEquals("testResource3 - 4", 1, (int) jsCard.getLinks().get("CONTACT-1").getPref());
        assertNull("testResource3 - 5", jsCard.getLinks().get("CONTACT-1").getMediaType());
        assertTrue("testResource3 - 6",jsCard.getLinks().get("CONTACT-1").hasNoContext());
    }

    @Test
    public void testResource4() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource4 - 1", 2, jsCard.getDirectories().size());
        assertEquals("testResource4 - 2", "http://directory.mycompany.example.com", jsCard.getDirectories().get("DIRECTORY-1").getUri());
        assertTrue("testResource4 - 3",jsCard.getDirectories().get("DIRECTORY-1").isDirectory());
        assertNull("testResource4 - 4", jsCard.getDirectories().get("DIRECTORY-1").getPref());
        assertNull("testResource4 - 5", jsCard.getDirectories().get("DIRECTORY-1").getMediaType());
        assertTrue("testResource4 - 6",jsCard.getDirectories().get("DIRECTORY-1").hasNoContext());
        assertEquals("testResource4 - 7", "ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering", jsCard.getDirectories().get("DIRECTORY-2").getUri());
        assertTrue("testResource4 - 8",jsCard.getDirectories().get("DIRECTORY-2").isDirectory());
        assertEquals("testResource4 - 9", 1, (int) jsCard.getDirectories().get("DIRECTORY-2").getPref());
        assertNull("testResource4 - 10", jsCard.getDirectories().get("DIRECTORY-2").getMediaType());
        assertTrue("testResource4 - 11",jsCard.getDirectories().get("DIRECTORY-2").hasNoContext());
    }

    @Test
    public void testResource5() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"index\" : 2, \"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource5 - 1", 2, jsCard.getDirectories().size());
        assertEquals("testResource5 - 2", "http://directory.mycompany.example.com", jsCard.getDirectories().get("DIRECTORY-1").getUri());
        assertTrue("testResource5 - 3",jsCard.getDirectories().get("DIRECTORY-1").isDirectory());
        assertNull("testResource5 - 4", jsCard.getDirectories().get("DIRECTORY-1").getPref());
        assertNull("testResource5 - 5", jsCard.getDirectories().get("DIRECTORY-1").getMediaType());
        assertTrue("testResource5 - 6",jsCard.getDirectories().get("DIRECTORY-1").hasNoContext());
        assertEquals("testResource5 - 7", "ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering", jsCard.getDirectories().get("DIRECTORY-2").getUri());
        assertTrue("testResource5 - 8",jsCard.getDirectories().get("DIRECTORY-2").isDirectory());
        assertEquals("testResource5 - 9", 1, (int) jsCard.getDirectories().get("DIRECTORY-2").getPref());
        assertNull("testResource5 - 10", jsCard.getDirectories().get("DIRECTORY-2").getMediaType());
        assertTrue("testResource5 - 11",jsCard.getDirectories().get("DIRECTORY-2").hasNoContext());
    }


    @Test
    public void testResource6() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"sound\", {}, \"uri\", \"CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource6 - 1", 1, jsCard.getMedia().size());
        assertEquals("testResource6 - 2", "CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com", jsCard.getMedia().get("SOUND-1").getUri());
        assertTrue("testResource6 - 3",jsCard.getMedia().get("SOUND-1").isSound());
        assertNull("testResource6 - 4", jsCard.getMedia().get("SOUND-1").getPref());
        assertNull("testResource6 - 5", jsCard.getMedia().get("SOUND-1").getMediaType());
        assertTrue("testResource6 - 6",jsCard.getMedia().get("SOUND-1").hasNoContext());
    }

    @Test
    public void testResource7() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"url\", {}, \"uri\", \"http://example.org/restaurant.french/~chezchic.htm\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource7 - 1", 1, jsCard.getLinks().size());
        assertEquals("testResource7 - 2", "http://example.org/restaurant.french/~chezchic.htm", jsCard.getLinks().get("LINK-1").getUri());
        assertTrue("testResource7 - 3",jsCard.getLinks().get("LINK-1").isGenericLink());
        assertNull("testResource7 - 4", jsCard.getLinks().get("LINK-1").getPref());
        assertEquals("testResource7 - 5", MimeTypeUtils.MIME_TEXT_HTML, jsCard.getLinks().get("LINK-1").getMediaType());
        assertTrue("testResource7 - 6",jsCard.getLinks().get("LINK-1").hasNoContext());
    }

    @Test
    public void testResource8() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"key\", {}, \"uri\", \"http://www.example.com/keys/jdoe.cer\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource8 - 1", 1, jsCard.getCryptoKeys().size());
        assertEquals("testResource8 - 2", "http://www.example.com/keys/jdoe.cer", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertNull("testResource8 - 3", jsCard.getCryptoKeys().get("KEY-1").getPref());
        assertNull("testResource8 - 4", jsCard.getCryptoKeys().get("KEY-1").getMediaType());
        assertTrue("testResource8 - 5",jsCard.getCryptoKeys().get("KEY-1").hasNoContext());
    }

    @Test
    public void testResource9() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"fburl\", {\"pref\": 1}, \"uri\", \"http://www.example.com/busy/janedoe\"], " +
                "[\"fburl\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://example.com/busy/project-a.ifb\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource9 - 1", 2, jsCard.getCalendars().size());
        assertEquals("testResource9 - 2", "http://www.example.com/busy/janedoe", jsCard.getCalendars().get("FREEBUSY-1").getUri());
        assertTrue("testResource9 - 3",jsCard.getCalendars().get("FREEBUSY-1").isFreeBusy());
        assertEquals("testResource9 - 4", 1, (int) jsCard.getCalendars().get("FREEBUSY-1").getPref());
        assertNull("testResource9 - 5", jsCard.getCalendars().get("FREEBUSY-1").getMediaType());
        assertTrue("testResource9 - 6",jsCard.getCalendars().get("FREEBUSY-1").hasNoContext());
        assertEquals("testResource9 - 7", "ftp://example.com/busy/project-a.ifb", jsCard.getCalendars().get("FREEBUSY-2").getUri());
        assertTrue("testResource9 - 8",jsCard.getCalendars().get("FREEBUSY-2").isFreeBusy());
        assertNull("testResource9 - 9", jsCard.getCalendars().get("FREEBUSY-2").getPref());
        assertTrue("testResource9 - 10",jsCard.getCalendars().get("FREEBUSY-2").hasNoContext());
        assertEquals("testResource9 - 11", "text/calendar", jsCard.getCalendars().get("FREEBUSY-2").getMediaType());
    }
    

    @Test
    public void testResource10() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caluri\", {\"pref\": 1}, \"uri\", \"http://cal.example.com/calA\"], " +
                "[\"caluri\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://ftp.example.com/calA.ics\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testResource10 - 1", 2, jsCard.getCalendars().size());
        assertEquals("testResource10 - 2", "http://cal.example.com/calA", jsCard.getCalendars().get("CALENDAR-1").getUri());
        assertTrue("testResource10 - 3",jsCard.getCalendars().get("CALENDAR-1").isCalendar());
        assertEquals("testResource10 - 4", 1, (int) jsCard.getCalendars().get("CALENDAR-1").getPref());
        assertNull("testResource10 - 5", jsCard.getCalendars().get("CALENDAR-1").getMediaType());
        assertTrue("testResource10 - 6",jsCard.getCalendars().get("CALENDAR-1").hasNoContext());
        assertEquals("testResource10 - 8", "ftp://ftp.example.com/calA.ics", jsCard.getCalendars().get("CALENDAR-2").getUri());
        assertTrue("testResource10 - 9",jsCard.getCalendars().get("CALENDAR-2").isCalendar());
        assertNull("testResource10 - 10", jsCard.getCalendars().get("CALENDAR-2").getPref());
        assertTrue("testResource10 - 11",jsCard.getCalendars().get("CALENDAR-2").hasNoContext());
        assertEquals("testResource10 - 12", "text/calendar", jsCard.getCalendars().get("CALENDAR-2").getMediaType());
    }

}
