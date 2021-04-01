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
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import ezvcard.VCard;
import ezvcard.parameter.ImageType;
import ezvcard.parameter.SoundType;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class OnlineTest extends JSContact2VCardTest {

    @Test
    public void testOnlineValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"XMPP-1\": {" +
                        "\"type\": \"username\","+
                        "\"contexts\": {\"private\": true}," +
                        "\"label\": \"XMPP\"," +
                        "\"pref\": 1, " +
                        "\"resource\": \"xmpp:alice@example.com\"" +
                    "}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid1 - 1",vcard.getImpps().size() == 1);
        assertTrue("testOnlineValid1 - 2",vcard.getImpps().get(0).isXmpp());
        assertTrue("testOnlineValid1 - 3",vcard.getImpps().get(0).getHandle().equals("alice@example.com"));
        assertTrue("testOnlineValid1 - 4",vcard.getImpps().get(0).getParameter("TYPE").equals("home"));
        assertTrue("testOnlineValid1 - 5",vcard.getImpps().get(0).getPref() == 1);
    }

    @Test
    public void testOnlineValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"SOURCE-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"source\"," +
                        "\"resource\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid2 - 1",vcard.getSources().size() == 1);
        assertTrue("testOnlineValid2 - 2",vcard.getSources().get(0).getValue().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
    }

    @Test
    public void testPhotoValid() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"photos\": {"+
                    "\"PHOTO-1\": {" +
                        "\"mediaType\": \"image/gif\","+
                        "\"href\": \"http://www.example.com/pub/photos/jqpublic.gif\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhotoValid - 1",vcard.getPhotos().size() == 1);
        assertTrue("testPhotoValid - 2",vcard.getPhotos().get(0).getUrl().equals("http://www.example.com/pub/photos/jqpublic.gif"));
        assertTrue("testPhotoValid - 3",vcard.getPhotos().get(0).getContentType() == ImageType.GIF);
    }

    @Test
    public void testOnlineValid4() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"LOGO-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"logo\"," +
                        "\"resource\": \"http://www.example.com/pub/logos/abccorp.jpg\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid4 - 1",vcard.getLogos().size() == 1);
        assertTrue("testOnlineValid4 - 2",vcard.getLogos().get(0).getUrl().equals("http://www.example.com/pub/logos/abccorp.jpg"));
    }

    @Test
    public void testOnlineValid5() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"CONTACT-URI-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"contact-uri\"," +
                        "\"resource\": \"mailto:contact@example.com\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid5 - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testOnlineValid5 - 2",vcard.getExtendedProperties().get(0).getPropertyName().equals("CONTACT-URI"));
        assertTrue("testOnlineValid5 - 2",vcard.getExtendedProperties().get(0).getValue().equals("mailto:contact@example.com"));
    }

    @Test
    public void testOnlineValid6() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"SOUND-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"sound\"," +
                        "\"mediaType\": \"audio/mp3\"," +
                        "\"resource\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid6 - 1",vcard.getSounds().size() == 1);
        assertTrue("testOnlineValid6 - 2",vcard.getSounds().get(0).getUrl().equals("sound.mp3"));
        assertTrue("testOnlineValid6 - 3",vcard.getSounds().get(0).getContentType() == SoundType.MP3);
    }

    @Test
    public void testOnlineValid7() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":{"+
                    "\"SOUND-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"sound\"," +
                        "\"resource\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid7 - 1",vcard.getSounds().size() == 1);
        assertTrue("testOnlineValid7 - 2",vcard.getSounds().get(0).getUrl().equals("sound.mp3"));
        assertTrue("testOnlineValid7 - 2",vcard.getSounds().get(0).getContentType() == null);
    }

    @Test
    public void testOnlineValid8() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"URL-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"url\"," +
                        "\"resource\": \"http://example.org/restaurant.french/~chezchic.htm\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid8 - 1",vcard.getUrls().size() == 1);
        assertTrue("testOnlineValid8 - 2",vcard.getUrls().get(0).getValue().equals("http://example.org/restaurant.french/~chezchic.htm"));
    }

    @Test
    public void testOnlineValid9() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"KEY-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"key\"," +
                        "\"resource\": \"http://www.example.com/keys/jdoe.cer\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid9 - 1",vcard.getKeys().size() == 1);
        assertTrue("testOnlineValid9 - 2",vcard.getKeys().get(0).getUrl().equals("http://www.example.com/keys/jdoe.cer"));
    }

    @Test
    public void testOnlineValid10() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"FBURL-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"fburl\"," +
                        "\"pref\": 1," +
                        "\"resource\": \"http://www.example.com/busy/janedoe\"" +
                    "}," +
                    "\"FBURL-2\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"fburl\"," +
                        "\"mediaType\": \"text/calendar\"," +
                        "\"resource\": \"ftp://example.com/busy/project-a.ifb\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid10 - 1",vcard.getFbUrls().size() == 2);
        assertTrue("testOnlineValid10 - 2",vcard.getFbUrls().get(0).getValue().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testOnlineValid10 - 3",vcard.getFbUrls().get(0).getPref() == 1);
        assertTrue("testOnlineValid10 - 4",vcard.getFbUrls().get(1).getValue().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testOnlineValid10 - 5",vcard.getFbUrls().get(1).getMediaType().equals("text/calendar"));
    }

    @Test
    public void testOnlineValid11() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"CALADRURI-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"caladruri\"," +
                        "\"pref\": 1," +
                        "\"resource\": \"mailto:janedoe@example.com\"" +
                    "}," +
                    "\"CALADRURI-2\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"caladruri\"," +
                        "\"resource\": \"http://example.com/calendar/jdoe\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid11 - 1",vcard.getCalendarRequestUris().size() == 2);
        assertTrue("testOnlineValid11 - 2",vcard.getCalendarRequestUris().get(0).getValue().equals("mailto:janedoe@example.com"));
        assertTrue("testOnlineValid11 - 3",vcard.getCalendarRequestUris().get(0).getPref() == 1);
        assertTrue("testOnlineValid11 - 4",vcard.getCalendarRequestUris().get(1).getValue().equals("http://example.com/calendar/jdoe"));
    }

    @Test
    public void testOnlineValid12() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\": {"+
                    "\"CALURI-1\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"caluri\"," +
                        "\"pref\": 1," +
                        "\"resource\": \"http://cal.example.com/calA\"" +
                    "}," +
                    "\"CALURI-2\": {" +
                        "\"type\": \"uri\","+
                        "\"label\": \"caluri\"," +
                        "\"mediaType\": \"text/calendar\"," +
                        "\"resource\": \"ftp://ftp.example.com/calA.ics\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineValid12 - 1",vcard.getCalendarUris().size() == 2);
        assertTrue("testOnlineValid12 - 2",vcard.getCalendarUris().get(0).getValue().equals("http://cal.example.com/calA"));
        assertTrue("testOnlineValid12 - 3",vcard.getCalendarUris().get(0).getPref() == 1);
        assertTrue("testOnlineValid12 - 4",vcard.getCalendarUris().get(1).getValue().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testOnlineValid12 - 5",vcard.getCalendarUris().get(1).getMediaType().equals("text/calendar"));
    }
}
