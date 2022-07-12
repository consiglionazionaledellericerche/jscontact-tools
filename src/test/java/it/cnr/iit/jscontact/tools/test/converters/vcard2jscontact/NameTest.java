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
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NameTest extends VCard2JSContactTest {

    @Test
    public void testNameValid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testNameValid1 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testNameValid1 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testNameValid1 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testNameValid1 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testNameValid1 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testNameValid1 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testNameValid1 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testNameValid1 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testNameValid1 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testNameValid1 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testNameValid1 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testNameValid1 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());

    }


    @Test
    public void testNameValid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testNameValid2 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testNameValid2 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testNameValid2 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testNameValid2 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testNameValid2 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testNameValid2 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testNameValid2 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testNameValid2 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testNameValid2 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testNameValid2 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testNameValid2 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testNameValid2 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testNameValid2 - 12", 1, jsCard.getNickNames().length);
        assertEquals("testNameValid2 - 13", "Johnny", jsCard.getNickNames()[0]);

    }

    @Test
    public void testNameValid3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "NICKNAME;PREF=1:Kid\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testNameValid3 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testNameValid3 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testNameValid3 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testNameValid3 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testNameValid3 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testNameValid3 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testNameValid3 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testNameValid3 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testNameValid3 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testNameValid3 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testNameValid3 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testNameValid3 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testNameValid3 - 12", 2, jsCard.getNickNames().length);
        assertEquals("testNameValid3 - 13", "Kid", jsCard.getNickNames()[0]);
        assertEquals("testNameValid3 - 14", "Johnny", jsCard.getNickNames()[1]);

    }

    @Test
    public void testNameValid4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME;ALTID=1:Johnny\n" +
                "NICKNAME;PREF=1;ALTID=2:Kid\n" +
                "NICKNAME;LANGUAGE=it;ALTID=1:Giovannino\n" +
                "NICKNAME;PREF=1;LANGUAGE=it;ALTID=2:Ragazzo\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testNameValid4 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testNameValid4 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testNameValid4 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testNameValid4 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testNameValid4 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertEquals("testNameValid4 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testNameValid4 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testNameValid4 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testNameValid4 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertEquals("testNameValid4 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testNameValid4 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testNameValid4 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testNameValid4 - 12", 2, jsCard.getNickNames().length);
        assertEquals("testNameValid4 - 13", "Kid", jsCard.getNickNames()[0]);
        assertEquals("testNameValid4 - 14", "Johnny", jsCard.getNickNames()[1]);
        assertTrue("testNameValid4 - 15",jsCard.getLocalization("it", "nickNames").isArray());
        assertEquals("testNameValid4 - 16", "Ragazzo", jsCard.getLocalization("it", "nickNames").get(0).asText());
        assertEquals("testNameValid4 - 17", "Giovannino", jsCard.getLocalization("it", "nickNames").get(1).asText());

    }

}
