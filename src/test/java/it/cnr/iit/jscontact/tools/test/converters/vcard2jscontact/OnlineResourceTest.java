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

import it.cnr.iit.jscontact.tools.dto.ResourceContext;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.LabelKey;
import it.cnr.iit.jscontact.tools.dto.OnlineResourceType;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class OnlineResourceTest extends VCard2JSContactTest {

    @Test
    public void testOnlineResourceValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "IMPP;TYPE=home;PREF=1:xmpp:alice@example.com\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid1 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid1 - 2",jsCard.getOnline()[0].getValue().equals("xmpp:alice@example.com"));
        assertTrue("testOnlineResourceValid1 - 3",jsCard.getOnline()[0].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));
        assertTrue("testOnlineResourceValid1 - 4",jsCard.getOnline()[0].getType().equals(OnlineResourceType.USERNAME.getValue()));
        assertTrue("testOnlineResourceValid1 - 5",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineResourceValid1 - 6",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid1 - 7",jsCard.getOnline()[0].getLabels().get(LabelKey.IMPP.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testOnlineResourceValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOURCE:http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid2 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid2 - 2",jsCard.getOnline()[0].getValue().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
        assertTrue("testOnlineResourceValid2 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid2 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineResourceValid2 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid2 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.SOURCE.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testPhotoValid() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PHOTO:http://www.example.com/pub/photos/jqpublic.gif\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhotoValid - 1",jsCard.getPhotos().size() == 1);
        assertTrue("testPhotoValid - 2",jsCard.getPhotos().get("PHOTO-1").getHref().equals("http://www.example.com/pub/photos/jqpublic.gif"));
        assertTrue("testPhotoValid - 3",jsCard.getPhotos().get("PHOTO-1").getPref() == null);
        assertTrue("testPhotoValid - 4",jsCard.getPhotos().get("PHOTO-1").getMediaType() == null);

    }

    @Test
    public void testOnlineResourceValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LOGO:http://www.example.com/pub/logos/abccorp.jpg\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid4 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid4 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/pub/logos/abccorp.jpg"));
        assertTrue("testOnlineResourceValid4 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid4 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineResourceValid4 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid4 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.LOGO.getValue()) == Boolean.TRUE);

    }


    @Test
    public void testOnlineResourceValid5() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CONTACT-URI;PREF=1:mailto:contact@example.com\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid5 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid5 - 2",jsCard.getOnline()[0].getValue().equals("mailto:contact@example.com"));
        assertTrue("testOnlineResourceValid5 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid5 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineResourceValid5 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid5 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.CONTACT_URI.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testOnlineResourceValid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG-DIRECTORY;PREF=1:ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\n" +
                "ORG-DIRECTORY;INDEX=1:http://directory.mycompany.example.com\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid6 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid6 - 2",jsCard.getOnline()[0].getValue().equals("http://directory.mycompany.example.com"));
        assertTrue("testOnlineResourceValid6 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid6 - 4",jsCard.getOnline()[0].getLabels().get(LabelKey.ORG_DIRECTORY.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid6 - 5",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineResourceValid6 - 6",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid6 - 7",jsCard.getOnline()[1].getValue().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testOnlineResourceValid6 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid6 - 9",jsCard.getOnline()[1].getPref() == 1);
        assertTrue("testOnlineResourceValid6 - 10",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid6 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.ORG_DIRECTORY.getValue()) == Boolean.TRUE);


    }

    @Test
    public void testOnlineResourceValid7() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG-DIRECTORY;INDEX=2;PREF=1:ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\n" +
                "ORG-DIRECTORY;INDEX=1:http://directory.mycompany.example.com\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid7 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid7 - 2",jsCard.getOnline()[0].getValue().equals("http://directory.mycompany.example.com"));
        assertTrue("testOnlineResourceValid7 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid7 - 4",jsCard.getOnline()[0].getLabels().get(LabelKey.ORG_DIRECTORY.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid7 - 5",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineResourceValid7 - 6",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid7 - 7",jsCard.getOnline()[1].getValue().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testOnlineResourceValid7 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid7 - 9",jsCard.getOnline()[1].getPref() == 1);
        assertTrue("testOnlineResourceValid7 - 10",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid7 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.ORG_DIRECTORY.getValue()) == Boolean.TRUE);

    }


    @Test
    public void testOnlineResourceValid8() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "SOUND:CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid8 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid8 - 2",jsCard.getOnline()[0].getValue().equals("CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com"));
        assertTrue("testOnlineResourceValid8 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid8 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineResourceValid8 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid8 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.SOUND.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testOnlineResourceValid9() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "URL:http://example.org/restaurant.french/~chezchic.htm\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid9 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid9 - 2",jsCard.getOnline()[0].getValue().equals("http://example.org/restaurant.french/~chezchic.htm"));
        assertTrue("testOnlineResourceValid9 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid9 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineResourceValid9 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid9 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.URL.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testOnlineResourceValid10() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "KEY:http://www.example.com/keys/jdoe.cer\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid10 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid10 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/keys/jdoe.cer"));
        assertTrue("testOnlineResourceValid10 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid10 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineResourceValid10 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid10 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.KEY.getValue()) == Boolean.TRUE);

    }


    @Test
    public void testOnlineResourceValid11() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "FBURL;PREF=1:http://www.example.com/busy/janedoe\n" +
                "FBURL;MEDIATYPE=text/calendar:ftp://example.com/busy/project-a.ifb\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid11 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid11 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testOnlineResourceValid11 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid11 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineResourceValid11 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid11 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.FBURL.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid11 - 7",jsCard.getOnline()[1].getValue().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testOnlineResourceValid11 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid11 - 9",jsCard.getOnline()[1].getPref() == null);
        assertTrue("testOnlineResourceValid11 - 10",jsCard.getOnline()[1].getMediaType().equals("text/calendar"));
        assertTrue("testOnlineResourceValid11 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.FBURL.getValue()) == Boolean.TRUE);

    }


    @Test
    public void testOnlineResourceValid12() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALADRURI;PREF=1:mailto:janedoe@example.com\n" +
                "CALADRURI:http://example.com/calendar/jdoe\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid12 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid12 - 2",jsCard.getOnline()[0].getValue().equals("mailto:janedoe@example.com"));
        assertTrue("testOnlineResourceValid12 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid12 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineResourceValid12 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid12 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.CALADRURI.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid12 - 7",jsCard.getOnline()[1].getValue().equals("http://example.com/calendar/jdoe"));
        assertTrue("testOnlineResourceValid12 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid12 - 9",jsCard.getOnline()[1].getPref() == null);
        assertTrue("testOnlineResourceValid12 - 10",jsCard.getOnline()[1].getMediaType() == null);
        assertTrue("testOnlineResourceValid12 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.CALADRURI.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testOnlineResourceValid13() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALURI;PREF=1:http://cal.example.com/calA\n" +
                "CALURI;MEDIATYPE=text/calendar:ftp://ftp.example.com/calA.ics\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid13 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid13 - 2",jsCard.getOnline()[0].getValue().equals("http://cal.example.com/calA"));
        assertTrue("testOnlineResourceValid13 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid13 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineResourceValid13 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid13 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.CALURI.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid13 - 7",jsCard.getOnline()[1].getValue().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testOnlineResourceValid13 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid13 - 9",jsCard.getOnline()[1].getPref() == null);
        assertTrue("testOnlineResourceValid13 - 10",jsCard.getOnline()[1].getMediaType().equals("text/calendar"));
        assertTrue("testOnlineResourceValid13 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.CALURI.getValue()) == Boolean.TRUE);

    }

}
