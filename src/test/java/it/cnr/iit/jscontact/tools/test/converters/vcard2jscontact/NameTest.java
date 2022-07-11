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
        assertTrue("testNameValid1 - 1",jsCard.getFullName().equals("John Q. Public, Esq."));
        assertTrue("testNameValid1 - 2",jsCard.getName().getComponents().length == 5);
        assertTrue("testNameValid1 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertTrue("testNameValid1 - 4",jsCard.getName().getComponents()[0].getValue().equals("Mr."));
        assertTrue("testNameValid1 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertTrue("testNameValid1 - 6",jsCard.getName().getComponents()[1].getValue().equals("John"));
        assertTrue("testNameValid1 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertTrue("testNameValid1 - 8",jsCard.getName().getComponents()[2].getValue().equals("Public"));
        assertTrue("testNameValid1 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertTrue("testNameValid1 - 10",jsCard.getName().getComponents()[3].getValue().equals("Quinlan"));
        assertTrue("testNameValid1 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertTrue("testNameValid1 - 12",jsCard.getName().getComponents()[4].getValue().equals("Esq."));

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
        assertTrue("testNameValid2 - 1",jsCard.getFullName().equals("John Q. Public, Esq."));
        assertTrue("testNameValid2 - 2",jsCard.getName().getComponents().length == 5);
        assertTrue("testNameValid2 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertTrue("testNameValid2 - 4",jsCard.getName().getComponents()[0].getValue().equals("Mr."));
        assertTrue("testNameValid2 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertTrue("testNameValid2 - 6",jsCard.getName().getComponents()[1].getValue().equals("John"));
        assertTrue("testNameValid2 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertTrue("testNameValid2 - 8",jsCard.getName().getComponents()[2].getValue().equals("Public"));
        assertTrue("testNameValid2 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertTrue("testNameValid2 - 10",jsCard.getName().getComponents()[3].getValue().equals("Quinlan"));
        assertTrue("testNameValid2 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertTrue("testNameValid2 - 12",jsCard.getName().getComponents()[4].getValue().equals("Esq."));
        assertTrue("testNameValid2 - 12",jsCard.getNickNames().length == 1);
        assertTrue("testNameValid2 - 13",jsCard.getNickNames()[0].equals("Johnny"));

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
        assertTrue("testNameValid3 - 1",jsCard.getFullName().equals("John Q. Public, Esq."));
        assertTrue("testNameValid3 - 2",jsCard.getName().getComponents().length == 5);
        assertTrue("testNameValid3 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertTrue("testNameValid3 - 4",jsCard.getName().getComponents()[0].getValue().equals("Mr."));
        assertTrue("testNameValid3 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertTrue("testNameValid3 - 6",jsCard.getName().getComponents()[1].getValue().equals("John"));
        assertTrue("testNameValid3 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertTrue("testNameValid3 - 8",jsCard.getName().getComponents()[2].getValue().equals("Public"));
        assertTrue("testNameValid3 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertTrue("testNameValid3 - 10",jsCard.getName().getComponents()[3].getValue().equals("Quinlan"));
        assertTrue("testNameValid3 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertTrue("testNameValid3 - 12",jsCard.getName().getComponents()[4].getValue().equals("Esq."));
        assertTrue("testNameValid3 - 12",jsCard.getNickNames().length == 2);
        assertTrue("testNameValid3 - 13",jsCard.getNickNames()[0].equals("Kid"));
        assertTrue("testNameValid3 - 14",jsCard.getNickNames()[1].equals("Johnny"));

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
        assertTrue("testNameValid4 - 1",jsCard.getFullName().equals("John Q. Public, Esq."));
        assertTrue("testNameValid4 - 2",jsCard.getName().getComponents().length == 5);
        assertTrue("testNameValid4 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertTrue("testNameValid4 - 4",jsCard.getName().getComponents()[0].getValue().equals("Mr."));
        assertTrue("testNameValid4 - 5",jsCard.getName().getComponents()[1].isPersonal());
        assertTrue("testNameValid4 - 6",jsCard.getName().getComponents()[1].getValue().equals("John"));
        assertTrue("testNameValid4 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertTrue("testNameValid4 - 8",jsCard.getName().getComponents()[2].getValue().equals("Public"));
        assertTrue("testNameValid4 - 9",jsCard.getName().getComponents()[3].isAdditional());
        assertTrue("testNameValid4 - 10",jsCard.getName().getComponents()[3].getValue().equals("Quinlan"));
        assertTrue("testNameValid4 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertTrue("testNameValid4 - 12",jsCard.getName().getComponents()[4].getValue().equals("Esq."));
        assertTrue("testNameValid4 - 12",jsCard.getNickNames().length == 2);
        assertTrue("testNameValid4 - 13",jsCard.getNickNames()[0].equals("Kid"));
        assertTrue("testNameValid4 - 14",jsCard.getNickNames()[1].equals("Johnny"));
        assertTrue("testNameValid4 - 15",jsCard.getLocalization("it", "nickNames").isArray());
        assertTrue("testNameValid4 - 16", jsCard.getLocalization("it", "nickNames").get(0).asText().equals("Ragazzo"));
        assertTrue("testNameValid4 - 17", jsCard.getLocalization("it", "nickNames").get(1).asText().equals("Giovannino"));

    }

}
