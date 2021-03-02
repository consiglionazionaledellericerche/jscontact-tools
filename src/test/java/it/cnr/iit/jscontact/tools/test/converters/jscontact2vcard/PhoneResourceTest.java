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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.util.TelUri;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PhoneResourceTest extends JSContact2VCardTest {


    @Test
    public void testPhoneResourceValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                    "\"value\": \"test\"" +
                "}," +
                "\"phones\":[{\"context\":\"private\",\"type\":\"voice\",\"value\":\"tel:+33-01-23-45-6\"}]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneResourceValid1 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneResourceValid1 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneResourceValid1 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("home,voice"));
    }


    @Test
    public void testPhoneResourceValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                "\"value\": \"test\"" +
                "}," +
                "\"phones\":[ " +
                      "{\"context\":\"private\",\"type\":\"voice\",\"value\":\"tel:+33-01-23-45-6\"}," +
                      "{\"context\":\"private\",\"type\":\"voice\",\"isPreferred\":true,\"value\":\"tel:+1-555-555-5555;ext=555\"}" +
                 "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneResourceValid2 - 1",vcard.getTelephoneNumbers().size() == 2);
        assertTrue("testPhoneResourceValid2 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneResourceValid2 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("home,voice"));
        assertTrue("testPhoneResourceValid2 - 4",vcard.getTelephoneNumbers().get(1).getUri().equals(TelUri.parse("tel:+1-555-555-5555;ext=555")));
        assertTrue("testPhoneResourceValid2 - 5",vcard.getTelephoneNumbers().get(1).getParameter("TYPE").equals("home,voice"));
        assertTrue("testPhoneResourceValid2 - 6",vcard.getTelephoneNumbers().get(1).getPref() == 1);
    }


    @Test
    public void testPhoneResourceValid3() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                "\"value\": \"test\"" +
                "}," +
                "\"phones\":[{\"context\":\"work\",\"type\":\"fax\",\"value\":\"tel:+33-01-23-45-6\"}]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneResourceValid3 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneResourceValid3 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneResourceValid3 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("work,fax"));

    }

        /*
    @Test
    public void testPhoneResourceValid4() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                "\"value\": \"test\"" +
                "}," +
                "\"phones\":[{\"context\":\"work\",\"type\":\"other\",\"value\":\"tel:+33-01-23-45-6\",\"labels\":{\"textphone\":true}}]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhoneResourceValid4 - 1",vcard.getTelephoneNumbers().size() == 1);
        assertTrue("testPhoneResourceValid4 - 2",vcard.getTelephoneNumbers().get(0).getUri().equals(TelUri.parse("tel:+33-01-23-45-6")));
        assertTrue("testPhoneResourceValid4 - 3",vcard.getTelephoneNumbers().get(0).getParameter("TYPE").equals("work,textphone"));

    }


    @Test
    public void testPhoneResourceValid5() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=home,work:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid5 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid5 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid5 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));
        assertTrue("testPhoneResourceValid5 - 4",jsCard.getPhones()[0].getLabels().size() == 1);
        assertTrue("testPhoneResourceValid5 - 5",jsCard.getPhones()[0].getLabels().get("work") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid5 - 6",jsCard.getPhones()[0].getType().equals(PhoneResourceType.OTHER.getValue()));

    }

    @Test
    public void testPhoneResourceValid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid6 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid6 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid6 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid6 - 4",jsCard.getPhones()[0].getLabels().size() == 1);
        assertTrue("testPhoneResourceValid6 - 5",jsCard.getPhones()[0].getLabels().get("private") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid6 - 6",jsCard.getPhones()[0].getType().equals(PhoneResourceType.OTHER.getValue()));

    }


    @Test
    public void testPhoneResourceValid7() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid7 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid7 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid7 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid7 - 4",jsCard.getPhones()[0].getLabels().size() == 2);
        assertTrue("testPhoneResourceValid7 - 5",jsCard.getPhones()[0].getLabels().get("private") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid7 - 6",jsCard.getPhones()[0].getLabels().get("textphone") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid7 - 7",jsCard.getPhones()[0].getType().equals(PhoneResourceType.OTHER.getValue()));

    }

    @Test
    public void testPhoneResourceValid8() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home,voice,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid8 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid8 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid8 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid8 - 4",jsCard.getPhones()[0].getLabels().size() == 2);
        assertTrue("testPhoneResourceValid8 - 5",jsCard.getPhones()[0].getLabels().get("private") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid8 - 6",jsCard.getPhones()[0].getLabels().get("textphone") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid8 - 7",jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));

    }

    @Test
    public void testPhoneResourceValid9() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=text;TYPE=home:+33 01 23 45 6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid9 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid9 - 2",jsCard.getPhones()[0].getValue().equals("+33 01 23 45 6"));
        assertTrue("testPhoneResourceValid9 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));

    }
*/

}
