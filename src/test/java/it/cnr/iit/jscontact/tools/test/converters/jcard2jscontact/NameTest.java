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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NameTest extends JCard2JSContactTest {

    @Test
    public void testName1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName1 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName1 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName1 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName1 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName1 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testName1 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName1 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName1 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName1 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testName1 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName1 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testName1 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());

    }

    @Test
    public void testName2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]], " +
                "[\"nickname\", {}, \"text\", \"Johnny\"] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName2 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName2 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName2 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName2 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName2 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testName2 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName2 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName2 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName2 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testName2 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName2 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testName2 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName2 - 12", 1, jsCard.getNickNames().length);
        assertEquals("testName2 - 13", "Johnny", jsCard.getNickNames()[0]);

    }

    @Test
    public void testName3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]], " +
                "[\"nickname\", {}, \"text\", \"Johnny\"], " +
                "[\"nickname\", {\"pref\":\"1\"}, \"text\", \"Kid\"] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName3 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName3 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName3 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName3 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName3 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testName3 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName3 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName3 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName3 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testName3 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName3 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testName3 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName3 - 12", 2, jsCard.getNickNames().length);
        assertEquals("testName3 - 13", "Kid", jsCard.getNickNames()[0]);
        assertEquals("testName3 - 14", "Johnny", jsCard.getNickNames()[1]);

    }


    @Test
    public void testName4() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]], " +
                "[\"nickname\", {\"altid\":\"1\"}, \"text\", \"Johnny\"], " +
                "[\"nickname\", {\"pref\":\"1\",\"altid\":\"2\"}, \"text\", \"Kid\"], " +
                "[\"nickname\", {\"altid\":\"1\",\"language\":\"it\"}, \"text\", \"Giovannino\"], " +
                "[\"nickname\", {\"pref\":\"1\",\"altid\":\"2\",\"language\":\"it\"}, \"text\", \"Ragazzo\"] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName4 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName4 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName4 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName4 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName4 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testName4 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName4 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName4 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName4 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testName4 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName4 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testName4 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName4 - 12", 2, jsCard.getNickNames().length);
        assertEquals("testName4 - 13", "Kid", jsCard.getNickNames()[0]);
        assertEquals("testName4 - 14", "Johnny", jsCard.getNickNames()[1]);
        assertTrue("testName4 - 15",jsCard.getLocalization("it", "nickNames").isArray());
        assertEquals("testName4 - 16", "Ragazzo", jsCard.getLocalization("it", "nickNames").get(0).asText());
        assertEquals("testName4 - 17", "Giovannino", jsCard.getLocalization("it", "nickNames").get(1).asText());

    }

}
