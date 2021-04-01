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

import it.cnr.iit.jscontact.tools.dto.OnlineType;
import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.OnlineLabelKey;
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
        assertTrue("testOnlineValid1 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineValid1 - 2",jsCard.getOnline()[0].getValue().equals("xmpp:alice@example.com"));
        assertTrue("testOnlineValid1 - 3",jsCard.getOnline()[0].getContext().getValue().equals(Context.PRIVATE.getValue()));
        assertTrue("testOnlineValid1 - 4",jsCard.getOnline()[0].getType().equals(OnlineType.USERNAME.getValue()));
        assertTrue("testOnlineValid1 - 5",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineValid1 - 6",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid1 - 7",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.IMPP.getValue()));

    }

    @Test
    public void testOnlineValid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"source\", {}, \"uri\", \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid2 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineValid2 - 2",jsCard.getOnline()[0].getValue().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
        assertTrue("testOnlineValid2 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid2 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineValid2 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid2 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.SOURCE.getValue()));

    }

    @Test
    public void testPhotoValid() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"photo\", {}, \"uri\", \"http://www.example.com/pub/photos/jqpublic.gif\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhotoValid - 1",jsCard.getPhotos().size() == 1);
        assertTrue("testPhotoValid - 2",jsCard.getPhotos().get("PHOTO-1").getHref().equals("http://www.example.com/pub/photos/jqpublic.gif"));
        assertTrue("testPhotoValid - 3",jsCard.getPhotos().get("PHOTO-1").getPref() == null);
        assertTrue("testPhotoValid - 4",jsCard.getPhotos().get("PHOTO-1").getMediaType() == null);

    }

    @Test
    public void testOnlineValid4() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"logo\", {}, \"uri\", \"http://www.example.com/pub/logos/abccorp.jpg\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid4 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineValid4 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/pub/logos/abccorp.jpg"));
        assertTrue("testOnlineValid4 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid4 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineValid4 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid4 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.LOGO.getValue()));

    }


    @Test
    public void testOnlineValid5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"contact-uri\", {\"pref\" : 1}, \"uri\", \"mailto:contact@example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid5 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineValid5 - 2",jsCard.getOnline()[0].getValue().equals("mailto:contact@example.com"));
        assertTrue("testOnlineValid5 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid5 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineValid5 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid5 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.CONTACT_URI.getValue()));

    }

    @Test
    public void testOnlineValid6() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid6 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineValid6 - 2",jsCard.getOnline()[0].getValue().equals("http://directory.mycompany.example.com"));
        assertTrue("testOnlineValid6 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid6 - 4",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.ORG_DIRECTORY.getValue()));
        assertTrue("testOnlineValid6 - 5",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineValid6 - 6",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid6 - 7",jsCard.getOnline()[1].getValue().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testOnlineValid6 - 8",jsCard.getOnline()[1].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid6 - 9",jsCard.getOnline()[1].getPref() == 1);
        assertTrue("testOnlineValid6 - 10",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid6 - 11",jsCard.getOnline()[1].getLabel().equals(OnlineLabelKey.ORG_DIRECTORY.getValue()));


    }

    @Test
    public void testOnlineValid7() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org-directory\", {\"index\" : 2, \"pref\" : 1}, \"uri\", \"ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering\"], " +
                "[\"org-directory\", {\"index\" : 1}, \"uri\", \"http://directory.mycompany.example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid7 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineValid7 - 2",jsCard.getOnline()[0].getValue().equals("http://directory.mycompany.example.com"));
        assertTrue("testOnlineValid7 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid7 - 4",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.ORG_DIRECTORY.getValue()));
        assertTrue("testOnlineValid7 - 5",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineValid7 - 6",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid7 - 7",jsCard.getOnline()[1].getValue().equals("ldap://ldap.tech.example/o=Example%20Tech,ou=Engineering"));
        assertTrue("testOnlineValid7 - 8",jsCard.getOnline()[1].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid7 - 9",jsCard.getOnline()[1].getPref() == 1);
        assertTrue("testOnlineValid7 - 10",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid7 - 11",jsCard.getOnline()[1].getLabel().equals(OnlineLabelKey.ORG_DIRECTORY.getValue()));

    }


    @Test
    public void testOnlineValid8() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"sound\", {}, \"uri\", \"CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid8 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineValid8 - 2",jsCard.getOnline()[0].getValue().equals("CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com"));
        assertTrue("testOnlineValid8 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid8 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineValid8 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid8 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.SOUND.getValue()));

    }

    @Test
    public void testOnlineValid9() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"url\", {}, \"uri\", \"http://example.org/restaurant.french/~chezchic.htm\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid9 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineValid9 - 2",jsCard.getOnline()[0].getValue().equals("http://example.org/restaurant.french/~chezchic.htm"));
        assertTrue("testOnlineValid9 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid9 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineValid9 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid9 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.URL.getValue()));

    }

    @Test
    public void testOnlineValid10() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"key\", {}, \"uri\", \"http://www.example.com/keys/jdoe.cer\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid10 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineValid10 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/keys/jdoe.cer"));
        assertTrue("testOnlineValid10 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid10 - 4",jsCard.getOnline()[0].getPref() == null);
        assertTrue("testOnlineValid10 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid10 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.KEY.getValue()));

    }


    @Test
    public void testOnlineValid11() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"fburl\", {\"pref\": 1}, \"uri\", \"http://www.example.com/busy/janedoe\"], " +
                "[\"fburl\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://example.com/busy/project-a.ifb\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid11 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineValid11 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testOnlineValid11 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid11 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineValid11 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid11 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.FBURL.getValue()));
        assertTrue("testOnlineValid11 - 7",jsCard.getOnline()[1].getValue().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testOnlineValid11 - 8",jsCard.getOnline()[1].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid11 - 9",jsCard.getOnline()[1].getPref() == null);
        assertTrue("testOnlineValid11 - 10",jsCard.getOnline()[1].getMediaType().equals("text/calendar"));
        assertTrue("testOnlineValid11 - 11",jsCard.getOnline()[1].getLabel().equals(OnlineLabelKey.FBURL.getValue()));

    }


    @Test
    public void testOnlineValid12() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caladruri\", {\"pref\": 1}, \"uri\", \"mailto:janedoe@example.com\"], " +
                "[\"caladruri\", {}, \"uri\", \"http://example.com/calendar/jdoe\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid12 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineValid12 - 2",jsCard.getOnline()[0].getValue().equals("mailto:janedoe@example.com"));
        assertTrue("testOnlineValid12 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid12 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineValid12 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid12 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.CALADRURI.getValue()));
        assertTrue("testOnlineValid12 - 7",jsCard.getOnline()[1].getValue().equals("http://example.com/calendar/jdoe"));
        assertTrue("testOnlineValid12 - 8",jsCard.getOnline()[1].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid12 - 9",jsCard.getOnline()[1].getPref() == null);
        assertTrue("testOnlineValid12 - 10",jsCard.getOnline()[1].getMediaType() == null);
        assertTrue("testOnlineValid12 - 11",jsCard.getOnline()[1].getLabel().equals(OnlineLabelKey.CALADRURI.getValue()));

    }

    @Test
    public void testOnlineValid13() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"caluri\", {\"pref\": 1}, \"uri\", \"http://cal.example.com/calA\"], " +
                "[\"caluri\", {\"mediatype\": \"text/calendar\"}, \"uri\", \"ftp://ftp.example.com/calA.ics\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOnlineValid13 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineValid13 - 2",jsCard.getOnline()[0].getValue().equals("http://cal.example.com/calA"));
        assertTrue("testOnlineValid13 - 3",jsCard.getOnline()[0].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid13 - 4",jsCard.getOnline()[0].getPref() == 1);
        assertTrue("testOnlineValid13 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineValid13 - 6",jsCard.getOnline()[0].getLabel().equals(OnlineLabelKey.CALURI.getValue()));
        assertTrue("testOnlineValid13 - 7",jsCard.getOnline()[1].getValue().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testOnlineValid13 - 8",jsCard.getOnline()[1].getType().equals(OnlineType.URI.getValue()));
        assertTrue("testOnlineValid13 - 9",jsCard.getOnline()[1].getPref() == null);
        assertTrue("testOnlineValid13 - 10",jsCard.getOnline()[1].getMediaType().equals("text/calendar"));
        assertTrue("testOnlineValid13 - 11",jsCard.getOnline()[1].getLabel().equals(OnlineLabelKey.CALURI.getValue()));

    }

}
