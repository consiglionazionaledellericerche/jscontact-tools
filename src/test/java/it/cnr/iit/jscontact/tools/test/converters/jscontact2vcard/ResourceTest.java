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

public class ResourceTest extends JSContact2VCardTest {
    
    @Test
    public void testResourceValid1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"SOURCE-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"directorySource\","+
                        "\"resource\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid1 - 1",vcard.getSources().size() == 1);
        assertTrue("testResourceValid1 - 2",vcard.getSources().get(0).getValue().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
    }

    @Test
    public void testPhotoValid() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"photos\": {"+
                    "\"PHOTO-1\": {" +
                        "\"@type\":\"File\"," +
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
    public void testResourceValid2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"LOGO-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"logo\","+
                        "\"resource\": \"http://www.example.com/pub/logos/abccorp.jpg\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid2 - 1",vcard.getLogos().size() == 1);
        assertTrue("testResourceValid2 - 2",vcard.getLogos().get(0).getUrl().equals("http://www.example.com/pub/logos/abccorp.jpg"));
    }

    @Test
    public void testResourceValid3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"CONTACT-URI-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"contact\","+
                        "\"resource\": \"mailto:contact@example.com\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid3 - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testResourceValid3 - 2",vcard.getExtendedProperties().get(0).getPropertyName().equals("CONTACT-URI"));
        assertTrue("testResourceValid3 - 2",vcard.getExtendedProperties().get(0).getValue().equals("mailto:contact@example.com"));
    }

    @Test
    public void testResourceValid4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"SOUND-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"audio\","+
                        "\"mediaType\": \"audio/mp3\"," +
                        "\"resource\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid4 - 1",vcard.getSounds().size() == 1);
        assertTrue("testResourceValid4 - 2",vcard.getSounds().get(0).getUrl().equals("sound.mp3"));
        assertTrue("testResourceValid4 - 3",vcard.getSounds().get(0).getContentType() == SoundType.MP3);
    }

    @Test
    public void testResourceValid5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\":{"+
                    "\"SOUND-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"audio\","+
                        "\"resource\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid5 - 1",vcard.getSounds().size() == 1);
        assertTrue("testResourceValid5 - 2",vcard.getSounds().get(0).getUrl().equals("sound.mp3"));
        assertTrue("testResourceValid5 - 2",vcard.getSounds().get(0).getContentType() == null);
    }

    @Test
    public void testResourceValid6() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"URI-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"uri\","+
                        "\"resource\": \"http://example.org/restaurant.french/~chezchic.htm\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid6 - 1",vcard.getUrls().size() == 1);
        assertTrue("testResourceValid6 - 2",vcard.getUrls().get(0).getValue().equals("http://example.org/restaurant.french/~chezchic.htm"));
    }

    @Test
    public void testResourceValid7() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"KEY-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"publicKey\","+
                        "\"resource\": \"http://www.example.com/keys/jdoe.cer\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid7 - 1",vcard.getKeys().size() == 1);
        assertTrue("testResourceValid7 - 2",vcard.getKeys().get(0).getUrl().equals("http://www.example.com/keys/jdoe.cer"));
    }

    @Test
    public void testResourceValid8() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"FBURL-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"freeBusy\","+
                        "\"pref\": 1," +
                        "\"resource\": \"http://www.example.com/busy/janedoe\"" +
                    "}," +
                    "\"FBURL-2\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"freeBusy\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"resource\": \"ftp://example.com/busy/project-a.ifb\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid8 - 1",vcard.getFbUrls().size() == 2);
        assertTrue("testResourceValid8 - 2",vcard.getFbUrls().get(0).getValue().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testResourceValid8 - 3",vcard.getFbUrls().get(0).getPref() == 1);
        assertTrue("testResourceValid8 - 4",vcard.getFbUrls().get(1).getValue().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testResourceValid8 - 5",vcard.getFbUrls().get(1).getMediaType().equals("text/calendar"));
    }

    @Test
    public void testResourceValid9() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"scheduling\": {"+
                    "\"CALADRURI-1\": {" +
                        "\"@type\":\"Scheduling\"," +
                        "\"pref\": 1," +
                        "\"sendTo\": { \"imip\":\"mailto:janedoe@example.com\"}" +
                    "}," +
                    "\"CALADRURI-2\": {" +
                        "\"@type\":\"Scheduling\"," +
                        "\"sendTo\": { \"imip\": \"http://example.com/calendar/jdoe\"}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid9 - 1",vcard.getCalendarRequestUris().size() == 2);
        assertTrue("testResourceValid9 - 2",vcard.getCalendarRequestUris().get(0).getValue().equals("mailto:janedoe@example.com"));
        assertTrue("testResourceValid9 - 3",vcard.getCalendarRequestUris().get(0).getPref() == 1);
        assertTrue("testResourceValid9 - 4",vcard.getCalendarRequestUris().get(1).getValue().equals("http://example.com/calendar/jdoe"));
    }

    @Test
    public void testResourceValid10() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"CALURI-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"calendar\","+
                        "\"pref\": 1," +
                        "\"resource\": \"http://cal.example.com/calA\"" +
                    "}," +
                    "\"CALURI-2\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"calendar\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"resource\": \"ftp://ftp.example.com/calA.ics\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid10 - 1",vcard.getCalendarUris().size() == 2);
        assertTrue("testResourceValid10 - 2",vcard.getCalendarUris().get(0).getValue().equals("http://cal.example.com/calA"));
        assertTrue("testResourceValid10 - 3",vcard.getCalendarUris().get(0).getPref() == 1);
        assertTrue("testResourceValid10 - 4",vcard.getCalendarUris().get(1).getValue().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testResourceValid10 - 5",vcard.getCalendarUris().get(1).getMediaType().equals("text/calendar"));
    }


    @Test
    public void testResourceValid13() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"CONTACT-URI-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"contact\","+
                        "\"resource\": \"mailto:contact@example.com\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testResourceValid13 - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testResourceValid13 - 2",vcard.getExtendedProperty("CONTACT-URI").getValue().equals("mailto:contact@example.com"));
    }
}
