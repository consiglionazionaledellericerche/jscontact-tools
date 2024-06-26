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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.VCardPropEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FullNameTest extends VCard2JSContactTest {

    @Test
    public void testFullName1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testFullName1 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());

    }

    @Test
    public void testFullName2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;ALTID=1;LANGUAGE=jp:大久保 正仁\n" +
                "FN;ALTID=1;LANGUAGE=en:Okubo Masahito\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("jp").build()).build();

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testFullName2 - 1", "大久保 正仁", jsCard.getName().getFull());
        assertEquals("testFullName2 - 2", "Okubo Masahito", jsCard.getLocalizations().get("en").get("name").get("full").asText());

    }


    @Test
    public void testFullName3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;ALTID=1;LANGUAGE=jp:大久保 正仁\n" +
                "FN;ALTID=1;LANGUAGE=en:Okubo Masahito\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("en").build()).build();

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testFullName3 - 1", "Okubo Masahito", jsCard.getName().getFull());
        assertEquals("testFullName3 - 2", "大久保 正仁", jsCard.getLocalizations().get("jp").get("name").get("full").asText());

    }


    @Test
    public void testFullNameDerivedFromUid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "UID:86203c2f-bb2c-41c1-ac55-386d6a84919d\n" +
                "FN;DERIVED=true:86203c2f-bb2c-41c1-ac55-386d6a84919d\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNull("testFullNameDerivedFromUid - 1", jsCard.getName());
        assertEquals("testFullNameDerivedFromUid - 2", "86203c2f-bb2c-41c1-ac55-386d6a84919d", jsCard.getUid());

    }

    @Test
    public void testFullName4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;PREF=1:Jane Doe\n" +
                "FN:Jane\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testFullName4 - 1", "Jane Doe", jsCard.getName().getFull());
        assertEquals("testFullName4 - 2", VCardPropEnum.FN.getValue(), jsCard.getVCardProps()[0].getName().toString().toUpperCase());
        assertEquals("testFullName4 - 3", "Jane", jsCard.getVCardProps()[0].getValue());
    }
}
