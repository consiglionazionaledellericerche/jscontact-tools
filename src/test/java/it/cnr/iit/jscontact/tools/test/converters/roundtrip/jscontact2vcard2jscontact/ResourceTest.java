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
                "\"resources\": {"+
                    "\"SOURCE-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"entry\","+
                        "\"uri\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource1 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testPhoto() throws IOException, CardException {

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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhoto - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"LOGO-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"logo\","+
                        "\"uri\": \"http://www.example.com/pub/logos/abccorp.jpg\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource2 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"CONTACT-URI-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"contact\","+
                        "\"uri\": \"mailto:contact@example.com\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource3 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"SOUND-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"sound\","+
                        "\"mediaType\": \"audio/mp3\"," +
                        "\"uri\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource4 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\":{"+
                    "\"SOUND-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"sound\","+
                        "\"uri\": \"sound.mp3\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource5 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource6() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"URI-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"uri\","+
                        "\"uri\": \"http://example.org/restaurant.french/~chezchic.htm\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource6 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource7() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"KEY-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"publicKey\","+
                        "\"uri\": \"http://www.example.com/keys/jdoe.cer\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource7 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource8() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"FBURL-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"freeBusy\","+
                        "\"pref\": 1," +
                        "\"uri\": \"http://www.example.com/busy/janedoe\"" +
                    "}," +
                    "\"FBURL-2\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"freeBusy\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"uri\": \"ftp://example.com/busy/project-a.ifb\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource8 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource9() throws IOException, CardException {

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
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource9 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testResource10() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"resources\": {"+
                    "\"CALURI-1\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"calendar\","+
                        "\"pref\": 1," +
                        "\"uri\": \"http://cal.example.com/calA\"" +
                    "}," +
                    "\"CALURI-2\": {" +
                        "\"@type\":\"Resource\"," +
                        "\"type\": \"calendar\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"uri\": \"ftp://ftp.example.com/calA.ics\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testResource10 - 1", jscard2, Card.toCard(jscard));
    }

}
