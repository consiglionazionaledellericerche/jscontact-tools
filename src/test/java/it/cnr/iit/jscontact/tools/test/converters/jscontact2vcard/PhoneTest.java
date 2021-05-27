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
import ezvcard.util.TelUri;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PhoneTest extends JSContact2VCardTest {

    @Test
    public void testPhoneValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"phone\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneValid1 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneValid1 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneValid1 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("home,voice"));
    }

    @Test
    public void testPhoneValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"phones\":{ " +
                      "\"PHONE-1\": {\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"phone\":\"tel:+33-01-23-45-6\"}," +
                      "\"PHONE-2\": {\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"pref\":1,\"phone\":\"tel:+1-555-555-5555;ext=555\"}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneValid2 - 1",vcard.getTelephoneNumbers().size() == 2);
        assertTrue("testPhoneValid2 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneValid2 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("home,voice"));
        assertTrue("testPhoneValid2 - 4",vcard.getTelephoneNumbers().get(1).getUri().equals(TelUri.parse("tel:+1-555-555-5555;ext=555")));
        assertTrue("testPhoneValid2 - 5",vcard.getTelephoneNumbers().get(1).getParameter("TYPE").equals("home,voice"));
        assertTrue("testPhoneValid2 - 6",vcard.getTelephoneNumbers().get(1).getPref() == 1);
    }

    @Test
    public void testPhoneValid3() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"contexts\":{\"work\": true},\"features\":{\"fax\": true},\"phone\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneValid3 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneValid3 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneValid3 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("work,fax"));
    }

    @Test
    public void testPhoneValid4() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"contexts\":{\"work\": true},\"features\":{\"textphone\": true},\"phone\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneValid4 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneValid4 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneValid4 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("work,textphone"));
    }

    @Test
    public void testPhoneValid5() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"voice\": true} ,\"phone\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneValid5 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneValid5 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneValid5 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("home,work,voice"));
    }

    @Test
    public void testPhoneValid6() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"textphone\": true},\"phone\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneValid6 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneValid6 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneValid6 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("home,work,textphone"));
    }

    @Test
    public void testPhoneValid7() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"contexts\":{\"work\": true, \"private\":true},\"features\":{\"voice\": true, \"textphone\": true},\"phone\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneValid7 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneValid7 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneValid7 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("home,work,voice,textphone"));
    }

}
