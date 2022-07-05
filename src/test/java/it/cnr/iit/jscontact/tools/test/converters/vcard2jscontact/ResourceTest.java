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

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ResourceTest extends VCard2JSContactTest {

    @Test
    public void testResourceValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "IMPP;TYPE=home;PREF=1:xmpp:alice@example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid1 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid1 - 2",jsCard.getResources().get("XMPP-1").getResource().equals("xmpp:alice@example.com"));
        assertTrue("testResourceValid1 - 3",jsCard.getResources().get("XMPP-1").asPrivate());
        assertTrue("testResourceValid1 - 4",jsCard.getResources().get("XMPP-1").isUsername());
        assertTrue("testResourceValid1 - 5",jsCard.getResources().get("XMPP-1").getPref() == 1);
        assertTrue("testResourceValid1 - 6",jsCard.getResources().get("XMPP-1").getMediaType() == null);
        assertTrue("testResourceValid1 - 7",jsCard.getResources().get("XMPP-1").isImpp());
    }

    @Test
    public void testResourceValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOURCE:http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid2 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid2 - 2",jsCard.getResources().get("SOURCE-1").getResource().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
        assertTrue("testResourceValid2 - 3",jsCard.getResources().get("SOURCE-1").isDirectorySource());
        assertTrue("testResourceValid2 - 4",jsCard.getResources().get("SOURCE-1").getPref() == null);
        assertTrue("testResourceValid2 - 5",jsCard.getResources().get("SOURCE-1").getMediaType() == null);
        assertTrue("testResourceValid2 - 6",jsCard.getResources().get("SOURCE-1").hasNoContext());
    }

    @Test
    public void testResourceValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LOGO:http://www.example.com/pub/logos/abccorp.jpg\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid4 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid4 - 2",jsCard.getResources().get("LOGO-1").getResource().equals("http://www.example.com/pub/logos/abccorp.jpg"));
        assertTrue("testResourceValid4 - 3",jsCard.getResources().get("LOGO-1").isLogo());
        assertTrue("testResourceValid4 - 4",jsCard.getResources().get("LOGO-1").getPref() == null);
        assertTrue("testResourceValid4 - 5",jsCard.getResources().get("LOGO-1").getMediaType().equals(MimeTypeUtils.MIME_IMAGE_JPEG));
        assertTrue("testResourceValid4 - 6",jsCard.getResources().get("LOGO-1").hasNoContext());
    }

    @Test
    public void testResourceValid5() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CONTACT-URI;PREF=1:mailto:contact@example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid5 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid5 - 2",jsCard.getResources().get("CONTACT-URI-1").getResource().equals("mailto:contact@example.com"));
        assertTrue("testResourceValid5 - 3",jsCard.getResources().get("CONTACT-URI-1").isContactUri());
        assertTrue("testResourceValid5 - 4",jsCard.getResources().get("CONTACT-URI-1").getPref() == 1);
        assertTrue("testResourceValid5 - 5",jsCard.getResources().get("CONTACT-URI-1").getMediaType() == null);
        assertTrue("testResourceValid5 - 6",jsCard.getResources().get("CONTACT-URI-1").hasNoContext());
    }

    @Test
    public void testResourceValid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG-DIRECTORY;PREF=1:ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\n" +
                "ORG-DIRECTORY;INDEX=1:http://directory.mycompany.example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid6 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid6 - 2",jsCard.getResources().get("ORG-DIRECTORY-1").getResource().equals("http://directory.mycompany.example.com"));
        assertTrue("testResourceValid6 - 3",jsCard.getResources().get("ORG-DIRECTORY-1").isDirectory());
        assertTrue("testResourceValid6 - 4",jsCard.getResources().get("ORG-DIRECTORY-1").getPref() == null);
        assertTrue("testResourceValid6 - 5",jsCard.getResources().get("ORG-DIRECTORY-1").getMediaType() == null);
        assertTrue("testResourceValid6 - 6",jsCard.getResources().get("ORG-DIRECTORY-1").hasNoContext());
        assertTrue("testResourceValid6 - 7",jsCard.getResources().get("ORG-DIRECTORY-2").getResource().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testResourceValid6 - 8",jsCard.getResources().get("ORG-DIRECTORY-2").getPref() == 1);
        assertTrue("testResourceValid6 - 9",jsCard.getResources().get("ORG-DIRECTORY-2").getMediaType() == null);
        assertTrue("testResourceValid6 - 10",jsCard.getResources().get("ORG-DIRECTORY-2").isDirectory());
        assertTrue("testResourceValid6 - 11",jsCard.getResources().get("ORG-DIRECTORY-2").hasNoContext());
    }

    @Test
    public void testResourceValid7() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG-DIRECTORY;INDEX=2;PREF=1:ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\n" +
                "ORG-DIRECTORY;INDEX=1:http://directory.mycompany.example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid7 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid7 - 2",jsCard.getResources().get("ORG-DIRECTORY-1").getResource().equals("http://directory.mycompany.example.com"));
        assertTrue("testResourceValid7 - 3",jsCard.getResources().get("ORG-DIRECTORY-1").isDirectory());
        assertTrue("testResourceValid7 - 4",jsCard.getResources().get("ORG-DIRECTORY-1").getPref() == null);
        assertTrue("testResourceValid7 - 5",jsCard.getResources().get("ORG-DIRECTORY-1").getMediaType() == null);
        assertTrue("testResourceValid7 - 6",jsCard.getResources().get("ORG-DIRECTORY-1").hasNoContext());
        assertTrue("testResourceValid7 - 7",jsCard.getResources().get("ORG-DIRECTORY-2").getResource().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testResourceValid7 - 8",jsCard.getResources().get("ORG-DIRECTORY-2").getPref() == 1);
        assertTrue("testResourceValid7 - 9",jsCard.getResources().get("ORG-DIRECTORY-2").getMediaType() == null);
        assertTrue("testResourceValid7 - 10",jsCard.getResources().get("ORG-DIRECTORY-2").hasNoContext());
        assertTrue("testResourceValid7 - 11",jsCard.getResources().get("ORG-DIRECTORY-2").isDirectory());
    }


    @Test
    public void testResourceValid8() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOUND:CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid8 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid8 - 2",jsCard.getResources().get("SOUND-1").getResource().equals("CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com"));
        assertTrue("testResourceValid8 - 3",jsCard.getResources().get("SOUND-1").isAudio());
        assertTrue("testResourceValid8 - 4",jsCard.getResources().get("SOUND-1").getPref() == null);
        assertTrue("testResourceValid8 - 5",jsCard.getResources().get("SOUND-1").getMediaType() == null);
        assertTrue("testResourceValid8 - 6",jsCard.getResources().get("SOUND-1").hasNoContext());
    }

    @Test
    public void testResourceValid9() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "URL:http://example.org/restaurant.french/~chezchic.htm\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid9 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid9 - 2",jsCard.getResources().get("URI-1").getResource().equals("http://example.org/restaurant.french/~chezchic.htm"));
        assertTrue("testResourceValid9 - 3",jsCard.getResources().get("URI-1").isUri());
        assertTrue("testResourceValid9 - 4",jsCard.getResources().get("URI-1").getPref() == null);
        assertTrue("testResourceValid9 - 5",jsCard.getResources().get("URI-1").getMediaType().equals(MimeTypeUtils.MIME_TEXT_HTML));
        assertTrue("testResourceValid9 - 6",jsCard.getResources().get("URI-1").hasNoContext());
    }

    @Test
    public void testResourceValid10() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "KEY:http://www.example.com/keys/jdoe.cer\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid10 - 1",jsCard.getResources().size() == 1);
        assertTrue("testResourceValid10 - 2",jsCard.getResources().get("KEY-1").getResource().equals("http://www.example.com/keys/jdoe.cer"));
        assertTrue("testResourceValid10 - 3",jsCard.getResources().get("KEY-1").isPublicKey());
        assertTrue("testResourceValid10 - 4",jsCard.getResources().get("KEY-1").getPref() == null);
        assertTrue("testResourceValid10 - 5",jsCard.getResources().get("KEY-1").getMediaType() == null);
        assertTrue("testResourceValid10 - 6",jsCard.getResources().get("KEY-1").hasNoContext());
    }


    @Test
    public void testResourceValid11() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "FBURL;PREF=1:http://www.example.com/busy/janedoe\n" +
                "FBURL;MEDIATYPE=text/calendar:ftp://example.com/busy/project-a.ifb\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid11 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid11 - 2",jsCard.getResources().get("FBURL-1").getResource().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testResourceValid11 - 3",jsCard.getResources().get("FBURL-1").isFreeBusy());
        assertTrue("testResourceValid11 - 4",jsCard.getResources().get("FBURL-1").getPref() == 1);
        assertTrue("testResourceValid11 - 5",jsCard.getResources().get("FBURL-1").getMediaType() == null);
        assertTrue("testResourceValid11 - 6",jsCard.getResources().get("FBURL-1").hasNoContext());
        assertTrue("testResourceValid11 - 7",jsCard.getResources().get("FBURL-2").getResource().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testResourceValid11 - 8",jsCard.getResources().get("FBURL-2").isFreeBusy());
        assertTrue("testResourceValid11 - 9",jsCard.getResources().get("FBURL-2").getPref() == null);
        assertTrue("testResourceValid11 - 10",jsCard.getResources().get("FBURL-2").hasNoContext());
        assertTrue("testResourceValid11 - 11",jsCard.getResources().get("FBURL-2").getMediaType().equals("text/calendar"));
    }

    @Test
    public void testResourceValid12() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALURI;PREF=1:http://cal.example.com/calA\n" +
                "CALURI;MEDIATYPE=text/calendar:ftp://ftp.example.com/calA.ics\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testResourceValid12 - 1",jsCard.getResources().size() == 2);
        assertTrue("testResourceValid12 - 2",jsCard.getResources().get("CALURI-1").getResource().equals("http://cal.example.com/calA"));
        assertTrue("testResourceValid12 - 3",jsCard.getResources().get("CALURI-1").isCalendar());
        assertTrue("testResourceValid12 - 4",jsCard.getResources().get("CALURI-1").getPref() == 1);
        assertTrue("testResourceValid12 - 5",jsCard.getResources().get("CALURI-1").getMediaType() == null);
        assertTrue("testResourceValid12 - 6",jsCard.getResources().get("CALURI-1").hasNoContext());
        assertTrue("testResourceValid12 - 7",jsCard.getResources().get("CALURI-2").getResource().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testResourceValid12 - 8",jsCard.getResources().get("CALURI-2").isCalendar());
        assertTrue("testResourceValid12 - 9",jsCard.getResources().get("CALURI-2").getPref() == null);
        assertTrue("testResourceValid12 - 10",jsCard.getResources().get("CALURI-2").hasNoContext());
        assertTrue("testResourceValid12 - 11",jsCard.getResources().get("CALURI-2").getMediaType().equals("text/calendar"));
    }

}
