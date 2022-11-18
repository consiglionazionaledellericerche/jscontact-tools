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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.jscontact2vcard2jscontact;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ResourceTest extends RoundtripTest {
    
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
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource1 - 1", jscard2, Card.toJSCard(jscard));
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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhoto - 1", jscard2, Card.toJSCard(jscard));
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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource2 - 1", jscard2, Card.toJSCard(jscard));
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
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource3 - 1", jscard2, Card.toJSCard(jscard));
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
                        "\"uri\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource4 - 1", jscard2, Card.toJSCard(jscard));
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
                        "\"uri\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource5 - 1", jscard2, Card.toJSCard(jscard));
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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource6 - 1", jscard2, Card.toJSCard(jscard));
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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource7 - 1", jscard2, Card.toJSCard(jscard));
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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource8 - 1", jscard2, Card.toJSCard(jscard));
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
                        "\"uri\":\"mailto:janedoe@example.com\"" +
                    "}," +
                    "\"SCHEDULING-2\": {" +
                        "\"@type\":\"SchedulingAddress\"," +
                        "\"uri\": \"http://example.com/calendar/jdoe\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource9 - 1", jscard2, Card.toJSCard(jscard));
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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource10 - 1", jscard2, Card.toJSCard(jscard));
    }

}
