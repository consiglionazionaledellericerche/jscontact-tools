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

import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NameTest extends VCard2JSContactTest {

    @Test
    public void testName1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N;SORT-AS=\"Public,John\":Public;John;Quinlan;Mr.;Esq.\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName1 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName1 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName1 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName1 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName1 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName1 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName1 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName1 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName1 - 9",jsCard.getName().getComponents()[3].isMiddle());
        assertEquals("testName1 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName1 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testName1 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName1 - 13", "Public,John", String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,jsCard.getName().getSortAs()));

    }


    @Test
    public void testName2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName2 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName2 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName2 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName2 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName2 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName2 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName2 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName2 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName2 - 9",jsCard.getName().getComponents()[3].isMiddle());
        assertEquals("testName2 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName2 - 11",jsCard.getName().getComponents()[4].isSuffix());
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

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName3 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName3 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName3 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName3 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName3 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName3 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName3 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName3 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName3 - 9",jsCard.getName().getComponents()[3].isMiddle());
        assertEquals("testName3 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName3 - 11",jsCard.getName().getComponents()[4].isSuffix());
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

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName4 - 1", "John Q. Public, Esq.", jsCard.getFullName());
        assertEquals("testName4 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName4 - 3",jsCard.getName().getComponents()[0].isPrefix());
        assertEquals("testName4 - 4", "Mr.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName4 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName4 - 6", "John", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName4 - 7",jsCard.getName().getComponents()[2].isSurname());
        assertEquals("testName4 - 8", "Public", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName4 - 9",jsCard.getName().getComponents()[3].isMiddle());
        assertEquals("testName4 - 10", "Quinlan", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName4 - 11",jsCard.getName().getComponents()[4].isSuffix());
        assertEquals("testName4 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName4 - 12", 2, jsCard.getNickNames().size());
        assertEquals("testName4 - 13", "Johnny", jsCard.getNickNames().get("NICK-1").getName());
        assertEquals("testName4 - 14", "Kid", jsCard.getNickNames().get("NICK-2").getName());
        assertEquals("testName4 - 15", "Giovannino", jsCard.getLocalization("it", "nickNames/NICK-1").get("name").asText());
        assertEquals("testName4 - 16", "Ragazzo", jsCard.getLocalization("it", "nickNames/NICK-2").get("name").asText());

    }

}
