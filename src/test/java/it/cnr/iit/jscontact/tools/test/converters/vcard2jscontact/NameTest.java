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

import it.cnr.iit.jscontact.tools.dto.NameComponentKind;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import static org.junit.Assert.*;

public class NameTest extends VCard2JSContactTest {

    @Test
    public void testName1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N;SORT-AS=\"Public,John\":Public;John;Quinlan;Mr.;Esq.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName1 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName1 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName1 - 3", jsCard.getName().getComponents()[0].isTitle());
        assertEquals("testName1 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName1 - 5", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName1 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName1 - 7", jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName1 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName1 - 9", jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testName1 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName1 - 11", jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName1 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName1 - 13", "Public", jsCard.getName().getSortAs().get(NameComponentKind.surname()));
        assertEquals("testName1 - 14", "John", jsCard.getName().getSortAs().get(NameComponentKind.given()));

    }


    @Test
    public void testName2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName2 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName2 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName2 - 3",jsCard.getName().getComponents()[0].isTitle());
        assertEquals("testName2 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName2 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName2 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName2 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName2 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName2 - 9",jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testName2 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName2 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName2 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName2 - 12", 1, jsCard.getNickNames().size());
        assertEquals("testName2 - 13", "Johnny", jsCard.getNickNames().get("NICK-1").getName());

    }

    @Test
    public void testName3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "NICKNAME;PREF=1:Kid\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName3 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName3 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName3 - 3",jsCard.getName().getComponents()[0].isTitle());
        assertEquals("testName3 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName3 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName3 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName3 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName3 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName3 - 9",jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testName3 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName3 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName3 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName3 - 12", 2, jsCard.getNickNames().size());
        assertEquals("testName3 - 13", "Johnny", jsCard.getNickNames().get("NICK-1").getName());
        assertEquals("testName3 - 14", "Kid", jsCard.getNickNames().get("NICK-2").getName());
        assertEquals("testName3 - 15", 1, jsCard.getNickNames().get("NICK-2").getPref().intValue());

    }

    @Test
    public void testName4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME;ALTID=1:Johnny\n" +
                "NICKNAME;PREF=1;ALTID=2:Kid\n" +
                "NICKNAME;LANGUAGE=it;ALTID=1:Giovannino\n" +
                "NICKNAME;PREF=1;LANGUAGE=it;ALTID=2:Ragazzo\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName4 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName4 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName4 - 3",jsCard.getName().getComponents()[0].isTitle());
        assertEquals("testName4 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName4 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName4 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName4 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName4 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName4 - 9",jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testName4 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName4 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName4 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName4 - 12", 2, jsCard.getNickNames().size());
        assertEquals("testName4 - 13", "Johnny", jsCard.getNickNames().get("NICK-1").getName());
        assertEquals("testName4 - 14", "Kid", jsCard.getNickNames().get("NICK-2").getName());
        assertEquals("testName4 - 15", "Giovannino", jsCard.getLocalization("it", "nickNames/NICK-1").get("name").asText());
        assertEquals("testName4 - 16", "Ragazzo", jsCard.getLocalization("it", "nickNames/NICK-2").get("name").asText());

    }

    @Test //ez-vcard accepts only one family name and one given name
    public void testName5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Paul Philip Stevenson\n" +
                "N:Stevenson;John;Philip,Paul;Dr.;Jr.,M.D.,A.C.P.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName5 - 1", 8, jsCard.getName().getComponents().length);
        assertEquals("testName5 - 2", "Dr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName5 - 3",  jsCard.getName().getComponents()[0].isTitle());
        assertEquals("testName5 - 5", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName5 - 6",  jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName5 - 8", "Stevenson", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName5 - 9",  jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName5 - 11", "Philip", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName5 - 12",  jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testName5 - 14", "Paul", jsCard.getName().getComponents()[4].getValue());
        assertTrue("testName5 - 15",  jsCard.getName().getComponents()[4].isGiven2());
        assertEquals("testName5 - 17", "Jr.", jsCard.getName().getComponents()[5].getValue());
        assertTrue("testName5 - 18",  jsCard.getName().getComponents()[5].isCredential());
        assertEquals("testName5 - 20", "M.D.", jsCard.getName().getComponents()[6].getValue());
        assertTrue("testName5 - 21",  jsCard.getName().getComponents()[6].isCredential());
        assertEquals("testName5 - 23", "A.C.P.", jsCard.getName().getComponents()[7].getValue());
        assertTrue("testName5 - 24",  jsCard.getName().getComponents()[7].isCredential());

    }


}
