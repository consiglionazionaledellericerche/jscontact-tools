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

import static org.junit.Assert.*;

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
        assertEquals("testResourceValid1 - 1", 1, vcard.getSources().size());
        assertEquals("testResourceValid1 - 2", "http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf", vcard.getSources().get(0).getValue());
        assertEquals("testResourceValid1 - 3", "SOURCE-1", vcard.getSources().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testPhotoValid - 1", 1, vcard.getPhotos().size());
        assertEquals("testPhotoValid - 2", "http://www.example.com/pub/photos/jqpublic.gif", vcard.getPhotos().get(0).getUrl());
        assertSame("testPhotoValid - 3", vcard.getPhotos().get(0).getContentType(), ImageType.GIF);
        assertEquals("testPhotoValid - 4", "PHOTO-1", vcard.getPhotos().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid2 - 1", 1, vcard.getLogos().size());
        assertEquals("testResourceValid2 - 2", "http://www.example.com/pub/logos/abccorp.jpg", vcard.getLogos().get(0).getUrl());
        assertEquals("testResourceValid2 - 3", "LOGO-1", vcard.getLogos().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid3 - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testResourceValid3 - 2", "CONTACT-URI", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testResourceValid3 - 2", "mailto:contact@example.com", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testResourceValid3 - 4", "CONTACT-URI-1", vcard.getExtendedProperties().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid4 - 1", 1, vcard.getSounds().size());
        assertEquals("testResourceValid4 - 2", "sound.mp3", vcard.getSounds().get(0).getUrl());
        assertSame("testResourceValid4 - 3", vcard.getSounds().get(0).getContentType(), SoundType.MP3);
        assertEquals("testResourceValid4 - 4", "SOUND-1", vcard.getSounds().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid5 - 1", 1, vcard.getSounds().size());
        assertEquals("testResourceValid5 - 2", "sound.mp3", vcard.getSounds().get(0).getUrl());
        assertNull("testResourceValid5 - 3", vcard.getSounds().get(0).getContentType());
        assertEquals("testResourceValid5 - 4", "SOUND-1", vcard.getSounds().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid6 - 1", 1, vcard.getUrls().size());
        assertEquals("testResourceValid6 - 2", "http://example.org/restaurant.french/~chezchic.htm", vcard.getUrls().get(0).getValue());
        assertEquals("testResourceValid6 - 3", "URI-1", vcard.getUrls().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid7 - 1", 1, vcard.getKeys().size());
        assertEquals("testResourceValid7 - 2", "http://www.example.com/keys/jdoe.cer", vcard.getKeys().get(0).getUrl());
        assertEquals("testResourceValid7 - 3", "KEY-1", vcard.getKeys().get(0).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid8 - 1", 2, vcard.getFbUrls().size());
        assertEquals("testResourceValid8 - 2", "http://www.example.com/busy/janedoe", vcard.getFbUrls().get(0).getValue());
        assertEquals("testResourceValid8 - 3", 1, (int) vcard.getFbUrls().get(0).getPref());
        assertEquals("testResourceValid8 - 4", "ftp://example.com/busy/project-a.ifb", vcard.getFbUrls().get(1).getValue());
        assertEquals("testResourceValid8 - 5", "text/calendar", vcard.getFbUrls().get(1).getMediaType());
        assertEquals("testResourceValid8 - 6", "FBURL-1", vcard.getFbUrls().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testResourceValid8 - 7", "FBURL-2", vcard.getFbUrls().get(1).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid9 - 1", 2, vcard.getCalendarRequestUris().size());
        assertEquals("testResourceValid9 - 2", "mailto:janedoe@example.com", vcard.getCalendarRequestUris().get(0).getValue());
        assertEquals("testResourceValid9 - 3", 1, (int) vcard.getCalendarRequestUris().get(0).getPref());
        assertEquals("testResourceValid9 - 4", "http://example.com/calendar/jdoe", vcard.getCalendarRequestUris().get(1).getValue());
        assertEquals("testResourceValid9 - 5", "CALADRURI-1", vcard.getCalendarRequestUris().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testResourceValid9 - 6", "CALADRURI-2", vcard.getCalendarRequestUris().get(1).getParameter(PROP_ID_PARAM));
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
        assertEquals("testResourceValid10 - 1", 2, vcard.getCalendarUris().size());
        assertEquals("testResourceValid10 - 2", "http://cal.example.com/calA", vcard.getCalendarUris().get(0).getValue());
        assertEquals("testResourceValid10 - 3", 1, (int) vcard.getCalendarUris().get(0).getPref());
        assertEquals("testResourceValid10 - 4", "ftp://ftp.example.com/calA.ics", vcard.getCalendarUris().get(1).getValue());
        assertEquals("testResourceValid10 - 5", "text/calendar", vcard.getCalendarUris().get(1).getMediaType());
        assertEquals("testResourceValid10 - 6", "CALURI-1", vcard.getCalendarUris().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testResourceValid10 - 7", "CALURI-2", vcard.getCalendarUris().get(1).getParameter(PROP_ID_PARAM));
    }

}
