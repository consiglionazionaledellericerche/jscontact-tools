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
package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class NotesTest extends JCard2JSContactTest {

    @Test
    public void testNotesWithAltid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"note\", {\"altid\" : \"1\"}, \"text\", \"This fax number is operational 0800 to 1715 EST, Mon-Fri\"]," +
                "[\"note\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testNotesWithAltid1 - 1",jsCard.getNotes()!=null);
        assertTrue("testNotesWithAltid1 - 2",jsCard.getNotes().getValue().equals("This fax number is operational 0800 to 1715 EST, Mon-Fri"));
        assertTrue("testNotesWithAltid1 - 3",jsCard.getNotes().getLanguage() == null);
        assertTrue("testNotesWithAltid1 - 4",jsCard.getNotes().getLocalizations() != null);
        assertTrue("testNotesWithAltid1 - 5",jsCard.getNotes().getLocalizations().size() == 1);
        assertTrue("testNotesWithAltid1 - 6",jsCard.getNotes().getLocalizations().get("it").equals("Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven"));
    }

    @Test
    public void testNotesWithAltid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"note\", {\"altid\": \"1\"}, \"text\", \"This fax number is operational 0800 to 1715 EST, Mon-Fri\"]," +
                "[\"note\", {\"language\" : \"it\", \"altid\": \"1\"}, \"text\", \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\"], " +
                "[\"note\", {}, \"text\", \"This is another note\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testNotesWithAltid2 - 1",jsCard.getNotes()!=null);
        assertTrue("testNotesWithAltid2 - 3",jsCard.getNotes().getValue().equals("This fax number is operational 0800 to 1715 EST, Mon-Fri\nThis is another note"));
        assertTrue("testNotesWithAltid2 - 4",jsCard.getNotes().getLanguage() == null);
        assertTrue("testNotesWithAltid2 - 5",jsCard.getNotes().getLocalizations().get("it").equals("Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven"));
    }

}
