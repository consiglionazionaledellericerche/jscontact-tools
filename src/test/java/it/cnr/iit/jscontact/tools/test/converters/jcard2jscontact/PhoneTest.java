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
package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PhoneTest extends JCard2JSContactTest {

    @Test
    public void testPhoneValid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid1 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid1 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid1 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());

    }

    @Test
    public void testPhoneValid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home\"}, \"uri\", \"tel:+33-01-23-45-6\"], " +
                "[\"tel\", {\"type\": \"voice,home\", \"pref\": 1}, \"uri\", \"tel:+1-555-555-5555;ext=555\"] " +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid2 - 1",jsCard.getPhones().size() == 2);
        assertTrue("testPhoneValid2 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid3 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhoneValid2 - 4",jsCard.getPhones().get("PHONE-1").asVoice());
        assertTrue("testPhoneValid2 - 5",jsCard.getPhones().get("PHONE-1").getLabel() == null);
        assertTrue("testPhoneValid2 - 6",jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-555-555-5555;ext=555"));
        assertTrue("testPhoneValid2 - 7",jsCard.getPhones().get("PHONE-2").asVoice());
        assertTrue("testPhoneValid2 - 8",jsCard.getPhones().get("PHONE-2").getPref() == 1);
        assertTrue("testPhoneValid2 - 9",jsCard.getPhones().get("PHONE-2").getLabel() == null);
    }

    @Test
    public void testPhoneValid3() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,fax\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid3 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid3 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid3 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhoneValid3 - 4",jsCard.getPhones().get("PHONE-1").asFax());
        assertTrue("testPhoneValid3 - 5",jsCard.getPhones().get("PHONE-1").getLabel() == null);

    }

    @Test
    public void testPhoneValid4() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,textphone\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid4 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid4 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid4 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhoneValid4 - 4",jsCard.getPhones().get("PHONE-1").asOtherFeature());
        assertTrue("testPhoneValid4 - 5",jsCard.getPhones().get("PHONE-1").getLabel().equals("textphone"));

    }


    @Test
    public void testPhoneValid5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home,work\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid5 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid5 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid5 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhoneValid5 - 4",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhoneValid5 - 5",jsCard.getPhones().get("PHONE-1").asVoice());

    }

    @Test
    public void testPhoneValid6() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,home\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid6 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid6 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid6 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhoneValid6 - 4",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhoneValid6 - 5",jsCard.getPhones().get("PHONE-1").asVoice());

    }


    @Test
    public void testPhoneValid7() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,home,textphone\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid7 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid7 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid7 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhoneValid7 - 4",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhoneValid7 - 5",jsCard.getPhones().get("PHONE-1").getLabel().equals("textphone"));
        assertTrue("testPhoneValid7 - 6",jsCard.getPhones().get("PHONE-1").asOtherFeature());

    }

    @Test
    public void testPhoneValid8() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,home,voice,textphone\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid8 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid8 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneValid8 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhoneValid8 - 4",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhoneValid8 - 5",jsCard.getPhones().get("PHONE-1").getLabel().equals("textphone"));
        assertTrue("testPhoneValid8 - 6",jsCard.getPhones().get("PHONE-1").asVoice());

    }

    @Test
    public void testPhoneValid9() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home\"}, \"text\", \"+33 01 23 45 6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhoneValid9 - 1",jsCard.getPhones().size() == 1);
        assertTrue("testPhoneValid9 - 2",jsCard.getPhones().get("PHONE-1").getPhone().equals("+33 01 23 45 6"));
        assertTrue("testPhoneValid9 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());

    }

}
