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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneTest extends JCard2JSContactTest {

    @Test
    public void testPhone1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone1 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone1 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone1 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());

    }

    @Test
    public void testPhone2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home\"}, \"uri\", \"tel:+33-01-23-45-6\"], " +
                "[\"tel\", {\"type\": \"voice,home\", \"pref\": 1}, \"uri\", \"tel:+1-555-555-5555;ext=555\"] " +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone2 - 1", 2, jsCard.getPhones().size());
        assertEquals("testPhone2 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone3 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertFalse("testPhone2 - 4",jsCard.getPhones().get("PHONE-1").hasFeature());
        assertNull("testPhone2 - 5", jsCard.getPhones().get("PHONE-1").getLabel());
        assertEquals("testPhone2 - 6", "tel:+1-555-555-5555;ext=555", jsCard.getPhones().get("PHONE-2").getNumber());
        assertTrue("testPhone2 - 7",jsCard.getPhones().get("PHONE-2").asVoice());
        assertEquals("testPhone2 - 8", 1, (int) jsCard.getPhones().get("PHONE-2").getPref());
        assertNull("testPhone2 - 9", jsCard.getPhones().get("PHONE-2").getLabel());
    }

    @Test
    public void testPhone3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,fax\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone3 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone3 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone3 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhone3 - 4",jsCard.getPhones().get("PHONE-1").asFax());
        assertNull("testPhone3 - 5", jsCard.getPhones().get("PHONE-1").getLabel());

    }

    @Test
    public void testPhone4() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,textphone\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone4 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone4 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone4 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhone4 - 4",jsCard.getPhones().get("PHONE-1").asTextphone());

    }


    @Test
    public void testPhone5() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home,work\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone5 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone5 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone5 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhone5 - 4",jsCard.getPhones().get("PHONE-1").asWork());
        assertFalse("testPhone5 - 5",jsCard.getPhones().get("PHONE-1").hasFeature());

    }

    @Test
    public void testPhone6() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,home\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone6 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone6 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone6 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhone6 - 4",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertFalse("testPhone6 - 5",jsCard.getPhones().get("PHONE-1").hasFeature());

    }


    @Test
    public void testPhone7() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,home,textphone\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone7 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone7 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone7 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhone7 - 4",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhone7 - 5",jsCard.getPhones().get("PHONE-1").asTextphone());

    }

    @Test
    public void testPhone8() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"work,home,voice,textphone\"}, \"uri\", \"tel:+33-01-23-45-6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone8 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone8 - 2", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone8 - 3",jsCard.getPhones().get("PHONE-1").asWork());
        assertTrue("testPhone8 - 4",jsCard.getPhones().get("PHONE-1").asPrivate());
        assertTrue("testPhone8 - 5",jsCard.getPhones().get("PHONE-1").asTextphone());
        assertTrue("testPhone8 - 6",jsCard.getPhones().get("PHONE-1").asVoice());

    }

    @Test
    public void testPhone9() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"tel\", {\"type\": \"home\"}, \"text\", \"+33 01 23 45 6\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhone9 - 1", 1, jsCard.getPhones().size());
        assertEquals("testPhone9 - 2", "+33 01 23 45 6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testPhone9 - 3",jsCard.getPhones().get("PHONE-1").asPrivate());

    }

}
