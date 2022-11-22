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
import ezvcard.parameter.TelephoneType;
import ezvcard.util.TelUri;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhoneTest extends JSContact2VCardTest {

    @Test
    public void testPhone1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhone1 - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testPhone1 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhone1 - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.HOME));
        assertTrue("testPhone1 - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.VOICE));
        assertEquals("testPhone1 - 5", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testPhone2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"phones\":{ " +
                      "\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"number\":\"tel:+33-01-23-45-6\"}," +
                      "\"PHONE-2\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"pref\":1,\"number\":\"tel:+1-555-555-5555;ext=555\"}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhone2 - 1", 2, vcard.getTelephoneNumbers().size());
        assertEquals("testPhone2 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhone2 - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.HOME));
        assertTrue("testPhone2 - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.VOICE));
        assertEquals("testPhone2 - 5", vcard.getTelephoneNumbers().get(1).getUri(), TelUri.parse("tel:+1-555-555-5555;ext=555"));
        assertTrue("testPhone2 - 6", vcard.getTelephoneNumbers().get(1).getTypes().contains(TelephoneType.HOME));
        assertTrue("testPhone2 - 7", vcard.getTelephoneNumbers().get(1).getTypes().contains(TelephoneType.VOICE));
        assertEquals("testPhone2 - 8", 1, (int) vcard.getTelephoneNumbers().get(1).getPref());
        assertEquals("testPhone2 - 9", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(PROP_ID_PARAM));
        assertEquals("testPhone2 - 10", "PHONE-2", vcard.getTelephoneNumbers().get(1).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testPhone3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true},\"features\":{\"fax\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhone3 - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testPhone3 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhone3 - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.WORK));
        assertTrue("testPhone3 - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.FAX));
        assertEquals("testPhone3 - 4", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testPhone4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true},\"features\":{\"textphone\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhone4 - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testPhone4 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhone4 - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.WORK));
        assertTrue("testPhone4 - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.TEXTPHONE));
        assertEquals("testPhone4 - 5", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testPhone5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"voice\": true} ,\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhone5 - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testPhone5 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhone5 - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.HOME));
        assertTrue("testPhone5 - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.WORK));
        assertTrue("testPhone5 - 5", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.VOICE));
        assertEquals("testPhone5 - 6", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testPhone6() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"textphone\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhone6 - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testPhone6 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhone5 - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.HOME));
        assertTrue("testPhone5 - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.WORK));
        assertTrue("testPhone5 - 5", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.TEXTPHONE));
        assertEquals("testPhone6 - 6", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(PROP_ID_PARAM));
    }

    @Test
    public void testPhone7() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"voice\": true, \"textphone\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhone7 - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testPhone7 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhone5 - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.HOME));
        assertTrue("testPhone5 - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.WORK));
        assertTrue("testPhone5 - 5", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.VOICE));
        assertTrue("testPhone5 - 6", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.TEXTPHONE));
        assertEquals("testPhone7 - 7", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(PROP_ID_PARAM));
    }

}
