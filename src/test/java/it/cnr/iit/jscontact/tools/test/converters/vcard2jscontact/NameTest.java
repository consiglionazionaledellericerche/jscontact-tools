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

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class NameTest extends VCard2JSContactTest {

    @Test
    public void testNameValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testNameValid1 - 1",jsCard.getFullName().getValue().equals("John Q. Public, Esq."));
        assertTrue("testNameValid1 - 2",jsCard.getName().length == 5);
        assertTrue("testNameValid1 - 3",jsCard.getName()[0].isPrefix());
        assertTrue("testNameValid1 - 4",jsCard.getName()[0].getValue().equals("Mr."));
        assertTrue("testNameValid1 - 5",jsCard.getName()[1].isPersonal());
        assertTrue("testNameValid1 - 6",jsCard.getName()[1].getValue().equals("John"));
        assertTrue("testNameValid1 - 7",jsCard.getName()[2].isSurname());
        assertTrue("testNameValid1 - 8",jsCard.getName()[2].getValue().equals("Public"));
        assertTrue("testNameValid1 - 9",jsCard.getName()[3].isAdditional());
        assertTrue("testNameValid1 - 10",jsCard.getName()[3].getValue().equals("Quinlan"));
        assertTrue("testNameValid1 - 11",jsCard.getName()[4].isSuffix());
        assertTrue("testNameValid1 - 12",jsCard.getName()[4].getValue().equals("Esq."));

    }


    @Test
    public void testNameValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testNameValid2 - 1",jsCard.getFullName().getValue().equals("John Q. Public, Esq."));
        assertTrue("testNameValid2 - 2",jsCard.getName().length == 5);
        assertTrue("testNameValid2 - 3",jsCard.getName()[0].isPrefix());
        assertTrue("testNameValid2 - 4",jsCard.getName()[0].getValue().equals("Mr."));
        assertTrue("testNameValid2 - 5",jsCard.getName()[1].isPersonal());
        assertTrue("testNameValid2 - 6",jsCard.getName()[1].getValue().equals("John"));
        assertTrue("testNameValid2 - 7",jsCard.getName()[2].isSurname());
        assertTrue("testNameValid2 - 8",jsCard.getName()[2].getValue().equals("Public"));
        assertTrue("testNameValid2 - 9",jsCard.getName()[3].isAdditional());
        assertTrue("testNameValid2 - 10",jsCard.getName()[3].getValue().equals("Quinlan"));
        assertTrue("testNameValid2 - 11",jsCard.getName()[4].isSuffix());
        assertTrue("testNameValid2 - 12",jsCard.getName()[4].getValue().equals("Esq."));
        assertTrue("testNameValid2 - 12",jsCard.getNickNames().length == 1);
        assertTrue("testNameValid2 - 13",jsCard.getNickNames()[0].getValue().equals("Johnny"));

    }

    @Test
    public void testNameValid3() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "NICKNAME;PREF=1:Kid\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testNameValid3 - 1",jsCard.getFullName().getValue().equals("John Q. Public, Esq."));
        assertTrue("testNameValid3 - 2",jsCard.getName().length == 5);
        assertTrue("testNameValid3 - 3",jsCard.getName()[0].isPrefix());
        assertTrue("testNameValid3 - 4",jsCard.getName()[0].getValue().equals("Mr."));
        assertTrue("testNameValid3 - 5",jsCard.getName()[1].isPersonal());
        assertTrue("testNameValid3 - 6",jsCard.getName()[1].getValue().equals("John"));
        assertTrue("testNameValid3 - 7",jsCard.getName()[2].isSurname());
        assertTrue("testNameValid3 - 8",jsCard.getName()[2].getValue().equals("Public"));
        assertTrue("testNameValid3 - 9",jsCard.getName()[3].isAdditional());
        assertTrue("testNameValid3 - 10",jsCard.getName()[3].getValue().equals("Quinlan"));
        assertTrue("testNameValid3 - 11",jsCard.getName()[4].isSuffix());
        assertTrue("testNameValid3 - 12",jsCard.getName()[4].getValue().equals("Esq."));
        assertTrue("testNameValid3 - 12",jsCard.getNickNames().length == 2);
        assertTrue("testNameValid3 - 13",jsCard.getNickNames()[0].getValue().equals("Kid"));
        assertTrue("testNameValid3 - 14",jsCard.getNickNames()[1].getValue().equals("Johnny"));

    }

}
