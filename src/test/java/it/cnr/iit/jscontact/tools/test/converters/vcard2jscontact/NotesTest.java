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

import static org.junit.Assert.*;

public class NotesTest extends VCard2JSContactTest {

    @Test
    public void testNotesWithAltid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "NOTE;ALTID=1:This fax number is operational 0800 to 1715 EST, Mon-Fri\n" +
                "NOTE;ALTID=1;LANGUAGE=it:Questo numero di fax e' operativo dalle 8.00 alle 17.15, Lun-Ven\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testNotesWithAltid1 - 1", jsCard.getNotes());
        assertEquals("testNotesWithAltid1 - 2", "This fax number is operational 0800 to 1715 EST, Mon-Fri", jsCard.getNotes());
        assertNotNull("testNotesWithAltid1 - 3", jsCard.getLocalizationsPerPath("notes"));
        assertEquals("testNotesWithAltid1 - 4", 1, jsCard.getLocalizationsPerPath("notes").size());
        assertEquals("testNotesWithAltid1 - 5", "Questo numero di fax e' operativo dalle 8.00 alle 17.15, Lun-Ven", jsCard.getLocalizationsPerPath("notes").get("it").asText());
    }

    @Test
    public void testNotesWithAltid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "NOTE;ALTID=1:This fax number is operational 0800 to 1715 EST, Mon-Fri\n" +
                "NOTE;ALTID=1;LANGUAGE=it:Questo numero di fax e' operativo dalle 8.00 alle 17.15, Lun-Ven\n" +
                "NOTE:This is another note\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testNotesWithAltid2 - 1", jsCard.getNotes());
        assertEquals("testNotesWithAltid2 - 2", "This fax number is operational 0800 to 1715 EST, Mon-Fri\nThis is another note", jsCard.getNotes());
        assertNotNull("testNotesWithAltid2 - 3", jsCard.getLocalizationsPerPath("notes"));
        assertEquals("testNotesWithAltid3 - 4", 1, jsCard.getLocalizationsPerPath("notes").size());
        assertEquals("testNotesWithAltid2 - 5", "Questo numero di fax e' operativo dalle 8.00 alle 17.15, Lun-Ven", jsCard.getLocalizationsPerPath("notes").get("it").asText());
    }

}
