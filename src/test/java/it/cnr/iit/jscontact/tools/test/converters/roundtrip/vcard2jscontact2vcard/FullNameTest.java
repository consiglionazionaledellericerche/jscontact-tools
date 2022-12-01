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
import ezvcard.property.FormattedName;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FullNameTest extends RoundtripTest {

    @Test
    public void testFullName1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testFullName1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testFullName2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;LANGUAGE=jp;ALTID=1:大久保 正仁\n" +
                "FN;LANGUAGE=en;ALTID=1:Okubo Masahito\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("jp").build()).build();

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testFullName2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }


    @Test
    public void testFullName3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;LANGUAGE=jp;ALTID=1:大久保 正仁\n" +
                "FN;LANGUAGE=en;ALTID=1:Okubo Masahito\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("en").build()).build();

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testFullName3 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }
}
