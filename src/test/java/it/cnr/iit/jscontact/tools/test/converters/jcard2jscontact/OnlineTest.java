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

import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class OnlineTest extends JCard2JSContactTest {

    @Test
    public void testOnlineValid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"impp\", {\"type\": \"home\", \"pref\": 1}, \"uri\", \"xmpp:alice@example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid1 - 1",jsCard.getOnline().size() == 1);
        assertTrue("testOnlineValid1 - 2",jsCard.getOnline().get("XMPP-1").getResource().equals("xmpp:alice@example.com"));
        assertTrue("testOnlineValid1 - 3",jsCard.getOnline().get("XMPP-1").asPrivate());
        assertTrue("testOnlineValid1 - 4",jsCard.getOnline().get("XMPP-1").isUsername());
        assertTrue("testOnlineValid1 - 5",jsCard.getOnline().get("XMPP-1").getPref() == 1);
        assertTrue("testOnlineValid1 - 6",jsCard.getOnline().get("XMPP-1").getMediaType() == null);
        assertTrue("testOnlineValid1 - 7",jsCard.getOnline().get("XMPP-1").asImpp());
    }

    @Test
    public void testOnlineValid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"source\", {}, \"uri\", \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid2 - 1",jsCard.getOnline().size() == 1);
        assertTrue("testOnlineValid2 - 2",jsCard.getOnline().get("SOURCE-1").getResource().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
        assertTrue("testOnlineValid2 - 3",jsCard.getOnline().get("SOURCE-1").isUri());
        assertTrue("testOnlineValid2 - 4",jsCard.getOnline().get("SOURCE-1").getPref() == null);
        assertTrue("testOnlineValid2 - 5",jsCard.getOnline().get("SOURCE-1").getMediaType() == null);
        assertTrue("testOnlineValid2 - 6",jsCard.getOnline().get("SOURCE-1").asSource());
        assertTrue("testOnlineValid2 - 7",jsCard.getOnline().get("SOURCE-1").hasNoContext());
    }

    @Test
    public void testOnlineValid4() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"logo\", {}, \"uri\", \"http://www.example.com/pub/logos/abccorp.jpg\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid4 - 1",jsCard.getOnline().size() == 1);
        assertTrue("testOnlineValid4 - 2",jsCard.getOnline().get("LOGO-1").getResource().equals("http://www.example.com/pub/logos/abccorp.jpg"));
        assertTrue("testOnlineValid4 - 3",jsCard.getOnline().get("LOGO-1").isUri());
        assertTrue("testOnlineValid4 - 4",jsCard.getOnline().get("LOGO-1").getPref() == null);
        assertTrue("testOnlineValid4 - 5",jsCard.getOnline().get("LOGO-1").getMediaType().equals(MimeTypeUtils.MIME_IMAGE_JPEG));
        assertTrue("testOnlineValid4 - 6",jsCard.getOnline().get("LOGO-1").asLogo());
        assertTrue("testOnlineValid4 - 7",jsCard.getOnline().get("LOGO-1").hasNoContext());
    }


    @Test
    public void testOnlineValid5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"contact-uri\", {\"pref\" : 1}, \"uri\", \"mailto:contact@example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid5 - 1",jsCard.getOnline().size() == 1);
        assertTrue("testOnlineValid5 - 2",jsCard.getOnline().get("CONTACT-URI-1").getResource().equals("mailto:contact@example.com"));
        assertTrue("testOnlineValid5 - 3",jsCard.getOnline().get("CONTACT-URI-1").isUri());
        assertTrue("testOnlineValid5 - 4",jsCard.getOnline().get("CONTACT-URI-1").getPref() == 1);
        assertTrue("testOnlineValid5 - 5",jsCard.getOnline().get("CONTACT-URI-1").getMediaType() == null);
        assertTrue("testOnlineValid5 - 6",jsCard.getOnline().get("CONTACT-URI-1").asContactUri());
        assertTrue("testOnlineValid5 - 7",jsCard.getOnline().get("CONTACT-URI-1").hasNoContext());
    }

    @Test
    public void testOnlineValid6() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid6 - 1",jsCard.getOnline().size() == 2);
        assertTrue("testOnlineValid6 - 2",jsCard.getOnline().get("ORG-DIRECTORY-1").getResource().equals("http://directory.mycompany.example.com"));
        assertTrue("testOnlineValid6 - 3",jsCard.getOnline().get("ORG-DIRECTORY-1").isUri());
        assertTrue("testOnlineValid6 - 4",jsCard.getOnline().get("ORG-DIRECTORY-1").asOrgDirectory());
        assertTrue("testOnlineValid6 - 5",jsCard.getOnline().get("ORG-DIRECTORY-1").getPref() == null);
        assertTrue("testOnlineValid6 - 6",jsCard.getOnline().get("ORG-DIRECTORY-1").getMediaType() == null);
        assertTrue("testOnlineValid6 - 7",jsCard.getOnline().get("ORG-DIRECTORY-1").hasNoContext());
        assertTrue("testOnlineValid6 - 8",jsCard.getOnline().get("ORG-DIRECTORY-2").getResource().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testOnlineValid6 - 9",jsCard.getOnline().get("ORG-DIRECTORY-2").isUri());
        assertTrue("testOnlineValid6 - 10",jsCard.getOnline().get("ORG-DIRECTORY-2").getPref() == 1);
        assertTrue("testOnlineValid6 - 11",jsCard.getOnline().get("ORG-DIRECTORY-2").getMediaType() == null);
        assertTrue("testOnlineValid6 - 12",jsCard.getOnline().get("ORG-DIRECTORY-2").asOrgDirectory());
        assertTrue("testOnlineValid6 - 13",jsCard.getOnline().get("ORG-DIRECTORY-2").hasNoContext());
    }

    @Test
    public void testOnlineValid7() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"index\" : 2, \"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid7 - 1",jsCard.getOnline().size() == 2);
        assertTrue("testOnlineValid7 - 2",jsCard.getOnline().get("ORG-DIRECTORY-1").getResource().equals("http://directory.mycompany.example.com"));
        assertTrue("testOnlineValid7 - 3",jsCard.getOnline().get("ORG-DIRECTORY-1").isUri());
        assertTrue("testOnlineValid7 - 4",jsCard.getOnline().get("ORG-DIRECTORY-1").asOrgDirectory());
        assertTrue("testOnlineValid7 - 5",jsCard.getOnline().get("ORG-DIRECTORY-1").getPref() == null);
        assertTrue("testOnlineValid7 - 6",jsCard.getOnline().get("ORG-DIRECTORY-1").getMediaType() == null);
        assertTrue("testOnlineValid7 - 7",jsCard.getOnline().get("ORG-DIRECTORY-1").hasNoContext());
        assertTrue("testOnlineValid7 - 8",jsCard.getOnline().get("ORG-DIRECTORY-2").getResource().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testOnlineValid7 - 9",jsCard.getOnline().get("ORG-DIRECTORY-2").isUri());
        assertTrue("testOnlineValid7 - 10",jsCard.getOnline().get("ORG-DIRECTORY-2").getPref() == 1);
        assertTrue("testOnlineValid7 - 11",jsCard.getOnline().get("ORG-DIRECTORY-2").getMediaType() == null);
        assertTrue("testOnlineValid7 - 12",jsCard.getOnline().get("ORG-DIRECTORY-2").hasNoContext());
        assertTrue("testOnlineValid7 - 13",jsCard.getOnline().get("ORG-DIRECTORY-2").asOrgDirectory());
    }


    @Test
    public void testOnlineValid8() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"sound\", {}, \"uri\", \"CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid8 - 1",jsCard.getOnline().size() == 1);
        assertTrue("testOnlineValid8 - 2",jsCard.getOnline().get("SOUND-1").getResource().equals("CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com"));
        assertTrue("testOnlineValid8 - 3",jsCard.getOnline().get("SOUND-1").isUri());
        assertTrue("testOnlineValid8 - 4",jsCard.getOnline().get("SOUND-1").getPref() == null);
        assertTrue("testOnlineValid8 - 5",jsCard.getOnline().get("SOUND-1").getMediaType() == null);
        assertTrue("testOnlineValid8 - 6",jsCard.getOnline().get("SOUND-1").hasNoContext());
        assertTrue("testOnlineValid8 - 7",jsCard.getOnline().get("SOUND-1").asSound());
    }

    @Test
    public void testOnlineValid9() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"url\", {}, \"uri\", \"http://example.org/restaurant.french/~chezchic.htm\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid9 - 1",jsCard.getOnline().size() == 1);
        assertTrue("testOnlineValid9 - 2",jsCard.getOnline().get("URL-1").getResource().equals("http://example.org/restaurant.french/~chezchic.htm"));
        assertTrue("testOnlineValid9 - 3",jsCard.getOnline().get("URL-1").isUri());
        assertTrue("testOnlineValid9 - 4",jsCard.getOnline().get("URL-1").getPref() == null);
        assertTrue("testOnlineValid9 - 5",jsCard.getOnline().get("URL-1").getMediaType().equals(MimeTypeUtils.MIME_TEXT_HTML));
        assertTrue("testOnlineValid9 - 6",jsCard.getOnline().get("URL-1").hasNoContext());
        assertTrue("testOnlineValid9 - 7",jsCard.getOnline().get("URL-1").asUrl());
    }

    @Test
    public void testOnlineValid10() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"key\", {}, \"uri\", \"http://www.example.com/keys/jdoe.cer\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid10 - 1",jsCard.getOnline().size() == 1);
        assertTrue("testOnlineValid10 - 2",jsCard.getOnline().get("KEY-1").getResource().equals("http://www.example.com/keys/jdoe.cer"));
        assertTrue("testOnlineValid10 - 3",jsCard.getOnline().get("KEY-1").isUri());
        assertTrue("testOnlineValid10 - 4",jsCard.getOnline().get("KEY-1").getPref() == null);
        assertTrue("testOnlineValid10 - 5",jsCard.getOnline().get("KEY-1").getMediaType() == null);
        assertTrue("testOnlineValid10 - 6",jsCard.getOnline().get("KEY-1").hasNoContext());
        assertTrue("testOnlineValid10 - 7",jsCard.getOnline().get("KEY-1").asKey());
    }

    @Test
    public void testOnlineValid11() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"fburl\", {\"pref\": 1}, \"uri\", \"http://www.example.com/busy/janedoe\"], " +
                "[\"fburl\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://example.com/busy/project-a.ifb\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid11 - 1",jsCard.getOnline().size() == 2);
        assertTrue("testOnlineValid11 - 2",jsCard.getOnline().get("FBURL-1").getResource().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testOnlineValid11 - 3",jsCard.getOnline().get("FBURL-1").isUri());
        assertTrue("testOnlineValid11 - 4",jsCard.getOnline().get("FBURL-1").getPref() == 1);
        assertTrue("testOnlineValid11 - 5",jsCard.getOnline().get("FBURL-1").getMediaType() == null);
        assertTrue("testOnlineValid11 - 6",jsCard.getOnline().get("FBURL-1").hasNoContext());
        assertTrue("testOnlineValid11 - 7",jsCard.getOnline().get("FBURL-1").asFburl());
        assertTrue("testOnlineValid11 - 8",jsCard.getOnline().get("FBURL-2").getResource().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testOnlineValid11 - 9",jsCard.getOnline().get("FBURL-2").isUri());
        assertTrue("testOnlineValid11 - 10",jsCard.getOnline().get("FBURL-2").getPref() == null);
        assertTrue("testOnlineValid11 - 10",jsCard.getOnline().get("FBURL-2").hasNoContext());
        assertTrue("testOnlineValid11 - 11",jsCard.getOnline().get("FBURL-2").getMediaType().equals("text/calendar"));
        assertTrue("testOnlineValid11 - 12",jsCard.getOnline().get("FBURL-2").asFburl());
    }


    @Test
    public void testOnlineValid12() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caladruri\", {\"pref\": 1}, \"uri\", \"mailto:janedoe@example.com\"], " +
                "[\"caladruri\", {}, \"uri\", \"http://example.com/calendar/jdoe\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid12 - 1",jsCard.getOnline().size() == 2);
        assertTrue("testOnlineValid12 - 2",jsCard.getOnline().get("CALADRURI-1").getResource().equals("mailto:janedoe@example.com"));
        assertTrue("testOnlineValid12 - 3",jsCard.getOnline().get("CALADRURI-1").isUri());
        assertTrue("testOnlineValid12 - 4",jsCard.getOnline().get("CALADRURI-1").getPref() == 1);
        assertTrue("testOnlineValid12 - 5",jsCard.getOnline().get("CALADRURI-1").getMediaType() == null);
        assertTrue("testOnlineValid12 - 6",jsCard.getOnline().get("CALADRURI-1").hasNoContext());
        assertTrue("testOnlineValid12 - 7",jsCard.getOnline().get("CALADRURI-1").asCaladruri());
        assertTrue("testOnlineValid12 - 8",jsCard.getOnline().get("CALADRURI-2").getResource().equals("http://example.com/calendar/jdoe"));
        assertTrue("testOnlineValid12 - 9",jsCard.getOnline().get("CALADRURI-2").isUri());
        assertTrue("testOnlineValid12 - 10",jsCard.getOnline().get("CALADRURI-2").getPref() == null);
        assertTrue("testOnlineValid12 - 11",jsCard.getOnline().get("CALADRURI-2").getMediaType() == null);
        assertTrue("testOnlineValid12 - 12",jsCard.getOnline().get("CALADRURI-2").hasNoContext());
        assertTrue("testOnlineValid12 - 13",jsCard.getOnline().get("CALADRURI-2").asCaladruri());
    }

    @Test
    public void testOnlineValid13() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caluri\", {\"pref\": 1}, \"uri\", \"http://cal.example.com/calA\"], " +
                "[\"caluri\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://ftp.example.com/calA.ics\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid13 - 1",jsCard.getOnline().size() == 2);
        assertTrue("testOnlineValid13 - 2",jsCard.getOnline().get("CALURI-1").getResource().equals("http://cal.example.com/calA"));
        assertTrue("testOnlineValid13 - 3",jsCard.getOnline().get("CALURI-1").isUri());
        assertTrue("testOnlineValid13 - 4",jsCard.getOnline().get("CALURI-1").getPref() == 1);
        assertTrue("testOnlineValid13 - 5",jsCard.getOnline().get("CALURI-1").getMediaType() == null);
        assertTrue("testOnlineValid13 - 6",jsCard.getOnline().get("CALURI-1").hasNoContext());
        assertTrue("testOnlineValid13 - 7",jsCard.getOnline().get("CALURI-1").asCaluri());
        assertTrue("testOnlineValid13 - 8",jsCard.getOnline().get("CALURI-2").getResource().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testOnlineValid13 - 9",jsCard.getOnline().get("CALURI-2").isUri());
        assertTrue("testOnlineValid13 - 10",jsCard.getOnline().get("CALURI-2").getPref() == null);
        assertTrue("testOnlineValid13 - 11",jsCard.getOnline().get("CALURI-2").hasNoContext());
        assertTrue("testOnlineValid13 - 12",jsCard.getOnline().get("CALURI-2").getMediaType().equals("text/calendar"));
        assertTrue("testOnlineValid13 - 13",jsCard.getOnline().get("CALURI-2").asCaluri());
    }

}
