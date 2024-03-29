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
import it.cnr.iit.jscontact.tools.dto.NameComponentKind;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NameTest extends JCard2JSContactTest {

    @Test
    public void testName1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {\"sort-as\":[\"Public\",\"John\"]}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]] " +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName1 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName1 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName1 - 3", jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName1 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName1 - 5", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName1 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName1 - 7", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName1 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName1 - 9", jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName1 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName1 - 11", jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName1 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName1 - 13", "Public", jsCard.getName().getSortAs().get(NameComponentKind.surname()));
        assertEquals("testName1 - 14", "John", jsCard.getName().getSortAs().get(NameComponentKind.given()));

    }

    @Test
    public void testName2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]], " +
                "[\"nickname\", {}, \"text\", \"Johnny\"] " +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName2 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName2 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName2 - 3",jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName2 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName2 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName2 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName2 - 7",jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName2 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName2 - 9",jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName2 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName2 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName2 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName2 - 12", 1, jsCard.getNicknames().size());
        assertEquals("testName2 - 13", "Johnny", jsCard.getNicknames().get("NICK-1").getName());

    }

    @Test
    public void testName3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]], " +
                "[\"nickname\", {}, \"text\", \"Johnny\"], " +
                "[\"nickname\", {\"pref\":\"1\"}, \"text\", \"Kid\"] " +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName3 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName3 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName3 - 3",jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName3 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName3 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName3 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName3 - 7",jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName3 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName3 - 9",jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName3 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName3 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName3 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName3 - 12", 2, jsCard.getNicknames().size());
        assertEquals("testName3 - 13", "Johnny", jsCard.getNicknames().get("NICK-1").getName());
        assertEquals("testName3 - 14", "Kid", jsCard.getNicknames().get("NICK-2").getName());
        assertEquals("testName3 - 15", 1, jsCard.getNicknames().get("NICK-2").getPref().intValue());

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
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testName4 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName4 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName4 - 3",jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName4 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName4 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName4 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName4 - 7",jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName4 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName4 - 9",jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName4 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName4 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName4 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName4 - 12", 2, jsCard.getNicknames().size());
        assertEquals("testName4 - 13", "Johnny", jsCard.getNicknames().get("NICK-1").getName());
        assertEquals("testName4 - 14", "Kid", jsCard.getNicknames().get("NICK-2").getName());
        assertEquals("testName4 - 15", "Giovannino", jsCard.getLocalization("it", "nicknames/NICK-1").get("name").asText());
        assertEquals("testName4 - 16", "Ragazzo", jsCard.getLocalization("it", "nicknames/NICK-2").get("name").asText());

    }

}
