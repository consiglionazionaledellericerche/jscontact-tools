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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.vcard2jscontact2vcard;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameTest extends RoundtripTest {

    @Test
    public void testName1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testName1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
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
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testName2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    //TODO -UNDISCERNIBLE
    //@Test
    public void testName3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "NICKNAME;PREF=1:Kid\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testName3 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    //TODO -UNDISCERNIBLE
    //@Test
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
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testName4 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

}