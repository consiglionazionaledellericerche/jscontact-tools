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

import static org.junit.Assert.assertEquals;

public class PhoneTest extends RoundtripTest {

    @Test
    public void testPhone1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhone1 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testPhone2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"phones\":{ " +
                      "\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"number\":\"tel:+33-01-23-45-6\"}," +
                      "\"PHONE-2\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"pref\":1,\"number\":\"tel:+1-555-555-5555;ext=555\"}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhone2 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testPhone3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true},\"features\":{\"fax\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhone3 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testPhone4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true},\"features\":{\"textphone\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhone4 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testPhone5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"voice\": true} ,\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhone5 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testPhone6() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"textphone\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhone6 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testPhone7() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"voice\": true, \"textphone\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhone7 - 1", jscard2, Card.toJSCard(jscard));
    }

}
