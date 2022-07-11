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

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ResourceTest extends JCard2JSContactTest {
    
    @Test
    public void testResourceValid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"source\", {}, \"uri\", \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid1 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid1 - 2",jsCard.getResources().get("SOURCE-1").getResource().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
        assertTrue("testResourceValid1 - 3",jsCard.getResources().get("SOURCE-1").isDirectorySource());
        assertTrue("testResourceValid1 - 4",jsCard.getResources().get("SOURCE-1").getPref() == null);
        assertTrue("testResourceValid1 - 5",jsCard.getResources().get("SOURCE-1").getMediaType() == null);
        assertTrue("testResourceValid1 - 6",jsCard.getResources().get("SOURCE-1").hasNoContext());
    }

    @Test
    public void testResourceValid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"logo\", {}, \"uri\", \"http://www.example.com/pub/logos/abccorp.jpg\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid2 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid2 - 2",jsCard.getResources().get("LOGO-1").getResource().equals("http://www.example.com/pub/logos/abccorp.jpg"));
        assertTrue("testResourceValid2 - 3",jsCard.getResources().get("LOGO-1").isLogo());
        assertTrue("testResourceValid2 - 4",jsCard.getResources().get("LOGO-1").getPref() == null);
        assertTrue("testResourceValid2 - 5",jsCard.getResources().get("LOGO-1").getMediaType().equals(MimeTypeUtils.MIME_IMAGE_JPEG));
        assertTrue("testResourceValid2 - 6",jsCard.getResources().get("LOGO-1").hasNoContext());
    }


    @Test
    public void testResourceValid3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"contact-uri\", {\"pref\" : 1}, \"uri\", \"mailto:contact@example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid3 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid3 - 2",jsCard.getResources().get("CONTACT-URI-1").getResource().equals("mailto:contact@example.com"));
        assertTrue("testResourceValid3 - 3",jsCard.getResources().get("CONTACT-URI-1").isContactUri());
        assertTrue("testResourceValid3 - 4",jsCard.getResources().get("CONTACT-URI-1").getPref() == 1);
        assertTrue("testResourceValid3 - 5",jsCard.getResources().get("CONTACT-URI-1").getMediaType() == null);
        assertTrue("testResourceValid3 - 6",jsCard.getResources().get("CONTACT-URI-1").hasNoContext());
    }

    @Test
    public void testResourceValid4() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid4 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid4 - 2",jsCard.getResources().get("ORG-DIRECTORY-1").getResource().equals("http://directory.mycompany.example.com"));
        assertTrue("testResourceValid4 - 3",jsCard.getResources().get("ORG-DIRECTORY-1").isDirectory());
        assertTrue("testResourceValid4 - 4",jsCard.getResources().get("ORG-DIRECTORY-1").getPref() == null);
        assertTrue("testResourceValid4 - 5",jsCard.getResources().get("ORG-DIRECTORY-1").getMediaType() == null);
        assertTrue("testResourceValid4 - 6",jsCard.getResources().get("ORG-DIRECTORY-1").hasNoContext());
        assertTrue("testResourceValid4 - 7",jsCard.getResources().get("ORG-DIRECTORY-2").getResource().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testResourceValid4 - 8",jsCard.getResources().get("ORG-DIRECTORY-2").isDirectory());
        assertTrue("testResourceValid4 - 9",jsCard.getResources().get("ORG-DIRECTORY-2").getPref() == 1);
        assertTrue("testResourceValid4 - 10",jsCard.getResources().get("ORG-DIRECTORY-2").getMediaType() == null);
        assertTrue("testResourceValid4 - 11",jsCard.getResources().get("ORG-DIRECTORY-2").hasNoContext());
    }

    @Test
    public void testResourceValid5() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"index\" : 2, \"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid5 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid5 - 2",jsCard.getResources().get("ORG-DIRECTORY-1").getResource().equals("http://directory.mycompany.example.com"));
        assertTrue("testResourceValid5 - 3",jsCard.getResources().get("ORG-DIRECTORY-1").isDirectory());
        assertTrue("testResourceValid5 - 4",jsCard.getResources().get("ORG-DIRECTORY-1").getPref() == null);
        assertTrue("testResourceValid5 - 5",jsCard.getResources().get("ORG-DIRECTORY-1").getMediaType() == null);
        assertTrue("testResourceValid5 - 6",jsCard.getResources().get("ORG-DIRECTORY-1").hasNoContext());
        assertTrue("testResourceValid5 - 7",jsCard.getResources().get("ORG-DIRECTORY-2").getResource().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testResourceValid5 - 8",jsCard.getResources().get("ORG-DIRECTORY-2").isDirectory());
        assertTrue("testResourceValid5 - 9",jsCard.getResources().get("ORG-DIRECTORY-2").getPref() == 1);
        assertTrue("testResourceValid5 - 10",jsCard.getResources().get("ORG-DIRECTORY-2").getMediaType() == null);
        assertTrue("testResourceValid5 - 11",jsCard.getResources().get("ORG-DIRECTORY-2").hasNoContext());
    }


    @Test
    public void testResourceValid6() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"sound\", {}, \"uri\", \"CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid6 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid6 - 2",jsCard.getResources().get("SOUND-1").getResource().equals("CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com"));
        assertTrue("testResourceValid6 - 3",jsCard.getResources().get("SOUND-1").isAudio());
        assertTrue("testResourceValid6 - 4",jsCard.getResources().get("SOUND-1").getPref() == null);
        assertTrue("testResourceValid6 - 5",jsCard.getResources().get("SOUND-1").getMediaType() == null);
        assertTrue("testResourceValid6 - 6",jsCard.getResources().get("SOUND-1").hasNoContext());
    }

    @Test
    public void testResourceValid7() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"url\", {}, \"uri\", \"http://example.org/restaurant.french/~chezchic.htm\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid7 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid7 - 2",jsCard.getResources().get("URI-1").getResource().equals("http://example.org/restaurant.french/~chezchic.htm"));
        assertTrue("testResourceValid7 - 3",jsCard.getResources().get("URI-1").isUri());
        assertTrue("testResourceValid7 - 4",jsCard.getResources().get("URI-1").getPref() == null);
        assertTrue("testResourceValid7 - 5",jsCard.getResources().get("URI-1").getMediaType().equals(MimeTypeUtils.MIME_TEXT_HTML));
        assertTrue("testResourceValid7 - 6",jsCard.getResources().get("URI-1").hasNoContext());
    }

    @Test
    public void testResourceValid8() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"key\", {}, \"uri\", \"http://www.example.com/keys/jdoe.cer\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid8 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid8 - 2",jsCard.getResources().get("KEY-1").getResource().equals("http://www.example.com/keys/jdoe.cer"));
        assertTrue("testResourceValid8 - 3",jsCard.getResources().get("KEY-1").isPublicKey());
        assertTrue("testResourceValid8 - 4",jsCard.getResources().get("KEY-1").getPref() == null);
        assertTrue("testResourceValid8 - 5",jsCard.getResources().get("KEY-1").getMediaType() == null);
        assertTrue("testResourceValid8 - 6",jsCard.getResources().get("KEY-1").hasNoContext());
    }

    @Test
    public void testResourceValid9() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"fburl\", {\"pref\": 1}, \"uri\", \"http://www.example.com/busy/janedoe\"], " +
                "[\"fburl\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://example.com/busy/project-a.ifb\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid9 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid9 - 2",jsCard.getResources().get("FBURL-1").getResource().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testResourceValid9 - 3",jsCard.getResources().get("FBURL-1").isFreeBusy());
        assertTrue("testResourceValid9 - 4",jsCard.getResources().get("FBURL-1").getPref() == 1);
        assertTrue("testResourceValid9 - 5",jsCard.getResources().get("FBURL-1").getMediaType() == null);
        assertTrue("testResourceValid9 - 6",jsCard.getResources().get("FBURL-1").hasNoContext());
        assertTrue("testResourceValid9 - 7",jsCard.getResources().get("FBURL-2").getResource().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testResourceValid9 - 8",jsCard.getResources().get("FBURL-2").isFreeBusy());
        assertTrue("testResourceValid9 - 9",jsCard.getResources().get("FBURL-2").getPref() == null);
        assertTrue("testResourceValid9 - 10",jsCard.getResources().get("FBURL-2").hasNoContext());
        assertTrue("testResourceValid9 - 11",jsCard.getResources().get("FBURL-2").getMediaType().equals("text/calendar"));
    }
    

    @Test
    public void testResourceValid10() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caluri\", {\"pref\": 1}, \"uri\", \"http://cal.example.com/calA\"], " +
                "[\"caluri\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://ftp.example.com/calA.ics\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testResourceValid10 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid10 - 2",jsCard.getResources().get("CALURI-1").getResource().equals("http://cal.example.com/calA"));
        assertTrue("testResourceValid10 - 3",jsCard.getResources().get("CALURI-1").isCalendar());
        assertTrue("testResourceValid10 - 4",jsCard.getResources().get("CALURI-1").getPref() == 1);
        assertTrue("testResourceValid10 - 5",jsCard.getResources().get("CALURI-1").getMediaType() == null);
        assertTrue("testResourceValid10 - 6",jsCard.getResources().get("CALURI-1").hasNoContext());
        assertTrue("testResourceValid10 - 8",jsCard.getResources().get("CALURI-2").getResource().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testResourceValid10 - 9",jsCard.getResources().get("CALURI-2").isCalendar());
        assertTrue("testResourceValid10 - 10",jsCard.getResources().get("CALURI-2").getPref() == null);
        assertTrue("testResourceValid10 - 11",jsCard.getResources().get("CALURI-2").hasNoContext());
        assertTrue("testResourceValid10 - 12",jsCard.getResources().get("CALURI-2").getMediaType().equals("text/calendar"));
    }

}
