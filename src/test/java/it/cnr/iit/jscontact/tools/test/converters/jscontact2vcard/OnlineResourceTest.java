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

public class OnlineResourceTest extends JSContact2VCardTest {

    @Test
    public void testOnlineResourceValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"username\","+
                        "\"context\":\"private\"," +
                        "\"labels\": { \"XMPP\": true }," +
                        "\"isPreferred\": true, " +
                        "\"value\": \"xmpp:alice@example.com\"" +
                    "}" +
                 "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid1 - 1",vcard.getImpps().size() == 1);
        assertTrue("testOnlineResourceValid1 - 2",vcard.getImpps().get(0).isXmpp());
        assertTrue("testOnlineResourceValid1 - 3",vcard.getImpps().get(0).getHandle().equals("alice@example.com"));
        assertTrue("testOnlineResourceValid1 - 4",vcard.getImpps().get(0).getParameter("TYPE").equals("home"));
        assertTrue("testOnlineResourceValid1 - 5",vcard.getImpps().get(0).getPref() == 1);
    }

    @Test
    public void testOnlineResourceValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"source\": true }," +
                        "\"value\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                 "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid2 - 1",vcard.getSources().size() == 1);
        assertTrue("testOnlineResourceValid2 - 2",vcard.getSources().get(0).getValue().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
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
    public void testOnlineResourceValid4() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                "{" +
                    "\"type\": \"uri\","+
                    "\"labels\": { \"logo\": true }," +
                    "\"value\": \"http://www.example.com/pub/logos/abccorp.jpg\"" +
                "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid4 - 1",vcard.getLogos().size() == 1);
        assertTrue("testOnlineResourceValid4 - 2",vcard.getLogos().get(0).getUrl().equals("http://www.example.com/pub/logos/abccorp.jpg"));
    }

    @Test
    public void testOnlineResourceValid5() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"contact-uri\": true }," +
                        "\"value\": \"mailto:contact@example.com\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid5 - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testOnlineResourceValid5 - 2",vcard.getExtendedProperties().get(0).getPropertyName().equals("CONTACT-URI"));
        assertTrue("testOnlineResourceValid5 - 2",vcard.getExtendedProperties().get(0).getValue().equals("mailto:contact@example.com"));
    }

    @Test
    public void testOnlineResourceValid6() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"sound\": true }," +
                        "\"mediaType\": \"audio/mp3\"," +
                        "\"value\": \"sound.mp3\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid6 - 1",vcard.getSounds().size() == 1);
        assertTrue("testOnlineResourceValid6 - 2",vcard.getSounds().get(0).getUrl().equals("sound.mp3"));
        assertTrue("testOnlineResourceValid6 - 3",vcard.getSounds().get(0).getContentType() == SoundType.MP3);
    }

    @Test
    public void testOnlineResourceValid7() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"sound\": true }," +
                        "\"value\": \"sound.mp3\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid7 - 1",vcard.getSounds().size() == 1);
        assertTrue("testOnlineResourceValid7 - 2",vcard.getSounds().get(0).getUrl().equals("sound.mp3"));
        assertTrue("testOnlineResourceValid7 - 2",vcard.getSounds().get(0).getContentType() == null);
    }

    @Test
    public void testOnlineResourceValid8() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"url\": true }," +
                        "\"value\": \"http://example.org/restaurant.french/~chezchic.htm\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid8 - 1",vcard.getUrls().size() == 1);
        assertTrue("testOnlineResourceValid8 - 2",vcard.getUrls().get(0).getValue().equals("http://example.org/restaurant.french/~chezchic.htm"));
    }

    @Test
    public void testOnlineResourceValid9() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"key\": true }," +
                        "\"value\": \"http://www.example.com/keys/jdoe.cer\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid9 - 1",vcard.getKeys().size() == 1);
        assertTrue("testOnlineResourceValid9 - 2",vcard.getKeys().get(0).getUrl().equals("http://www.example.com/keys/jdoe.cer"));
    }

    @Test
    public void testOnlineResourceValid10() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"fburl\": true }," +
                        "\"isPreferred\": true ," +
                        "\"value\": \"http://www.example.com/busy/janedoe\"" +
                    "}," +
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"fburl\": true }," +
                        "\"mediaType\": \"text/calendar\"," +
                        "\"value\": \"ftp://example.com/busy/project-a.ifb\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid10 - 1",vcard.getFbUrls().size() == 2);
        assertTrue("testOnlineResourceValid10 - 2",vcard.getFbUrls().get(0).getValue().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testOnlineResourceValid10 - 3",vcard.getFbUrls().get(0).getPref() == 1);
        assertTrue("testOnlineResourceValid10 - 4",vcard.getFbUrls().get(1).getValue().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testOnlineResourceValid10 - 5",vcard.getFbUrls().get(1).getMediaType().equals("text/calendar"));
    }

    @Test
    public void testOnlineResourceValid11() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"caladruri\": true }," +
                        "\"isPreferred\": true ," +
                        "\"value\": \"mailto:janedoe@example.com\"" +
                    "}," +
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"caladruri\": true }," +
                        "\"value\": \"http://example.com/calendar/jdoe\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid11 - 1",vcard.getCalendarRequestUris().size() == 2);
        assertTrue("testOnlineResourceValid11 - 2",vcard.getCalendarRequestUris().get(0).getValue().equals("mailto:janedoe@example.com"));
        assertTrue("testOnlineResourceValid11 - 3",vcard.getCalendarRequestUris().get(0).getPref() == 1);
        assertTrue("testOnlineResourceValid11 - 4",vcard.getCalendarRequestUris().get(1).getValue().equals("http://example.com/calendar/jdoe"));
    }

    @Test
    public void testOnlineResourceValid12() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"caluri\": true }," +
                        "\"isPreferred\": true ," +
                        "\"value\": \"http://cal.example.com/calA\"" +
                    "}," +
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"caluri\": true }," +
                        "\"mediaType\": \"text/calendar\"," +
                        "\"value\": \"ftp://ftp.example.com/calA.ics\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid12 - 1",vcard.getCalendarUris().size() == 2);
        assertTrue("testOnlineResourceValid12 - 2",vcard.getCalendarUris().get(0).getValue().equals("http://cal.example.com/calA"));
        assertTrue("testOnlineResourceValid12 - 3",vcard.getCalendarUris().get(0).getPref() == 1);
        assertTrue("testOnlineResourceValid12 - 4",vcard.getCalendarUris().get(1).getValue().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testOnlineResourceValid12 - 5",vcard.getCalendarUris().get(1).getMediaType().equals("text/calendar"));
    }
}
