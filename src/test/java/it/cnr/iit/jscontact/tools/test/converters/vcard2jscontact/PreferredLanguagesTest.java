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
import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PreferredLanguagesTest extends VCard2JSContactTest {

    @Test
    public void testPreferredLanguages1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANG;PREF=1:jp\n" +
                "LANG;PREF=2:en\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPreferredLanguages1 - 1", jsCard.getPreferredLanguages());
        assertEquals("testPreferredLanguages1 - 2", 2, jsCard.getPreferredLanguages().size());
        assertEquals("testPreferredLanguages1 - 3", 1, (int) jsCard.getPreferredLanguages().get("LANG-1").getPref());
        assertEquals("testPreferredLanguages1 - 4", 2, (int) jsCard.getPreferredLanguages().get("LANG-2").getPref());
        assertEquals("testPreferredLanguages1 - 5", "jp", jsCard.getPreferredLanguages().get("LANG-1").getLanguage());
        assertEquals("testPreferredLanguages1 - 6", "en", jsCard.getPreferredLanguages().get("LANG-2").getLanguage());
    }


    @Test
    public void testPreferredLanguages2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANG;TYPE=work;PREF=1:en\n" +
                "LANG;TYPE=work;PREF=2:fr\n" +
                "LANG;TYPE=home:fr\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPreferredLanguages2 - 1", jsCard.getPreferredLanguages());
        assertEquals("testPreferredLanguages2 - 2", 3, jsCard.getPreferredLanguages().size());
        assertEquals("testPreferredLanguages2 - 3", 1, (int) jsCard.getPreferredLanguages().get("LANG-1").getPref());
        assertSame("testPreferredLanguages2 - 4",jsCard.getPreferredLanguages().get("LANG-1").getContexts().get(Context.work()), Boolean.TRUE);
        assertEquals("testPreferredLanguages2 - 5", 2, (int) jsCard.getPreferredLanguages().get("LANG-2").getPref());
        assertSame("testPreferredLanguages2 - 6",jsCard.getPreferredLanguages().get("LANG-2").getContexts().get(Context.work()), Boolean.TRUE);
        assertSame("testPreferredLanguages2 - 7",jsCard.getPreferredLanguages().get("LANG-3").getContexts().get(Context.private_()), Boolean.TRUE);
        assertEquals("testPreferredLanguages1 - 8", "en", jsCard.getPreferredLanguages().get("LANG-1").getLanguage());
        assertEquals("testPreferredLanguages1 - 9", "fr", jsCard.getPreferredLanguages().get("LANG-2").getLanguage());
        assertEquals("testPreferredLanguages1 - 10", "fr", jsCard.getPreferredLanguages().get("LANG-3").getLanguage());
    }


}
