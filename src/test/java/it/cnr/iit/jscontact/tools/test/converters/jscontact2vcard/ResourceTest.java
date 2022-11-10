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
    public void testResource1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"directories\": {"+
                    "\"ENTRY-1\": {" +
                        "\"@type\":\"DirectoryResource\"," +
                        "\"type\": \"entry\","+
                        "\"uri\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource1 - 1", 1, vcard.getSources().size());
        assertEquals("testResource1 - 2", "http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf", vcard.getSources().get(0).getValue());
        assertEquals("testResource1 - 3", "ENTRY-1", vcard.getSources().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testPhoto() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"media\": {"+
                    "\"PHOTO-1\": {" +
                        "\"@type\":\"MediaResource\"," +
                        "\"type\":\"photo\"," +
                        "\"mediaType\": \"image/gif\","+
                        "\"uri\": \"http://www.example.com/pub/photos/jqpublic.gif\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhoto - 1", 1, vcard.getPhotos().size());
        assertEquals("testPhoto - 2", "http://www.example.com/pub/photos/jqpublic.gif", vcard.getPhotos().get(0).getUrl());
        assertSame("testPhoto - 3", vcard.getPhotos().get(0).getContentType(), ImageType.GIF);
        assertEquals("testPhoto - 4", "PHOTO-1", vcard.getPhotos().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"media\": {"+
                    "\"LOGO-1\": {" +
                        "\"@type\":\"MediaResource\"," +
                        "\"type\": \"logo\","+
                        "\"uri\": \"http://www.example.com/pub/logos/abccorp.jpg\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource2 - 1", 1, vcard.getLogos().size());
        assertEquals("testResource2 - 2", "http://www.example.com/pub/logos/abccorp.jpg", vcard.getLogos().get(0).getUrl());
        assertEquals("testResource2 - 3", "LOGO-1", vcard.getLogos().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"links\": {"+
                    "\"CONTACT-1\": {" +
                        "\"@type\":\"LinkResource\"," +
                        "\"type\": \"contact\","+
                        "\"uri\": \"mailto:contact@example.com\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource3 - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testResource3 - 2", "CONTACT-URI", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testResource3 - 2", "mailto:contact@example.com", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testResource3 - 4", "CONTACT-1", vcard.getExtendedProperties().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"media\": {"+
                    "\"SOUND-1\": {" +
                        "\"@type\":\"MediaResource\"," +
                        "\"type\": \"sound\","+
                        "\"mediaType\": \"audio/mp3\"," +
                        "\"uri\": \"android.resource:///com.my.android.sharesound/2130968609\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource4 - 1", 1, vcard.getSounds().size());
        assertEquals("testResource4 - 2", "android.resource:///com.my.android.sharesound/2130968609", vcard.getSounds().get(0).getUrl());
        assertSame("testResource4 - 3", vcard.getSounds().get(0).getContentType(), SoundType.MP3);
        assertEquals("testResource4 - 4", "SOUND-1", vcard.getSounds().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"media\":{"+
                    "\"SOUND-1\": {" +
                        "\"@type\":\"MediaResource\"," +
                        "\"type\": \"sound\","+
                        "\"uri\": \"android.resource:///com.my.android.sharesound/2130968609\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource5 - 1", 1, vcard.getSounds().size());
        assertEquals("testResource5 - 2", "android.resource:///com.my.android.sharesound/2130968609", vcard.getSounds().get(0).getUrl());
        assertNull("testResource5 - 3", vcard.getSounds().get(0).getContentType());
        assertEquals("testResource5 - 4", "SOUND-1", vcard.getSounds().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource6() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"links\": {"+
                    "\"LINK-1\": {" +
                        "\"@type\":\"LinkResource\"," +
                        "\"uri\": \"http://example.org/restaurant.french/~chezchic.htm\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource6 - 1", 1, vcard.getUrls().size());
        assertEquals("testResource6 - 2", "http://example.org/restaurant.french/~chezchic.htm", vcard.getUrls().get(0).getValue());
        assertEquals("testResource6 - 3", "LINK-1", vcard.getUrls().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource7() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"cryptoKeys\": {"+
                    "\"KEY-1\": {" +
                        "\"@type\":\"CryptoResource\"," +
                        "\"uri\": \"http://www.example.com/keys/jdoe.cer\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource7 - 1", 1, vcard.getKeys().size());
        assertEquals("testResource7 - 2", "http://www.example.com/keys/jdoe.cer", vcard.getKeys().get(0).getUrl());
        assertEquals("testResource7 - 3", "KEY-1", vcard.getKeys().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource8() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"calendars\": {"+
                    "\"FREEBUSY-1\": {" +
                        "\"@type\":\"CalendarResource\"," +
                        "\"type\": \"freeBusy\","+
                        "\"pref\": 1," +
                        "\"uri\": \"http://www.example.com/busy/janedoe\"" +
                    "}," +
                    "\"FREEBUSY-2\": {" +
                        "\"@type\":\"CalendarResource\"," +
                        "\"type\": \"freeBusy\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"uri\": \"ftp://example.com/busy/project-a.ifb\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource8 - 1", 2, vcard.getFbUrls().size());
        assertEquals("testResource8 - 2", "http://www.example.com/busy/janedoe", vcard.getFbUrls().get(0).getValue());
        assertEquals("testResource8 - 3", 1, (int) vcard.getFbUrls().get(0).getPref());
        assertEquals("testResource8 - 4", "ftp://example.com/busy/project-a.ifb", vcard.getFbUrls().get(1).getValue());
        assertEquals("testResource8 - 5", "text/calendar", vcard.getFbUrls().get(1).getMediaType());
        assertEquals("testResource8 - 6", "FREEBUSY-1", vcard.getFbUrls().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testResource8 - 7", "FREEBUSY-2", vcard.getFbUrls().get(1).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource9() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"schedulingAddresses\": {"+
                    "\"SCHEDULING-1\": {" +
                        "\"@type\":\"SchedulingAddress\"," +
                        "\"pref\": 1," +
                        "\"type\": \"imip\"," +
                        "\"uri\": \"mailto:janedoe@example.com\"" +
                    "}," +
                    "\"SCHEDULING-2\": {" +
                        "\"@type\":\"SchedulingAddress\"," +
                        "\"uri\": \"http://example.com/calendar/jdoe\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource9 - 1", 2, vcard.getCalendarRequestUris().size());
        assertEquals("testResource9 - 2", "mailto:janedoe@example.com", vcard.getCalendarRequestUris().get(0).getValue());
        assertEquals("testResource9 - 3", 1, (int) vcard.getCalendarRequestUris().get(0).getPref());
        assertEquals("testResource9 - 4", "http://example.com/calendar/jdoe", vcard.getCalendarRequestUris().get(1).getValue());
        assertEquals("testResource9 - 5", "SCHEDULING-1", vcard.getCalendarRequestUris().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testResource9 - 6", "SCHEDULING-2", vcard.getCalendarRequestUris().get(1).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testResource10() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"calendars\": {"+
                    "\"CALENDAR-1\": {" +
                        "\"@type\":\"CalendarResource\"," +
                        "\"type\": \"calendar\","+
                        "\"pref\": 1," +
                        "\"uri\": \"http://cal.example.com/calA\"" +
                    "}," +
                    "\"CALENDAR-2\": {" +
                        "\"@type\":\"CalendarResource\"," +
                        "\"type\": \"calendar\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"uri\": \"ftp://ftp.example.com/calA.ics\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testResource10 - 1", 2, vcard.getCalendarUris().size());
        assertEquals("testResource10 - 2", "http://cal.example.com/calA", vcard.getCalendarUris().get(0).getValue());
        assertEquals("testResource10 - 3", 1, (int) vcard.getCalendarUris().get(0).getPref());
        assertEquals("testResource10 - 4", "ftp://ftp.example.com/calA.ics", vcard.getCalendarUris().get(1).getValue());
        assertEquals("testResource10 - 5", "text/calendar", vcard.getCalendarUris().get(1).getMediaType());
        assertEquals("testResource10 - 6", "CALENDAR-1", vcard.getCalendarUris().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testResource10 - 7", "CALENDAR-2", vcard.getCalendarUris().get(1).getParameter(PROP_ID_PARAM));
    }

}
