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
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class NotesTest extends JSContact2VCardTest {

    @Test
    public void testNotes1() throws IOException, CardException {

        String jscard="{" +
                    "\"@type\":\"Card\"," +
                    "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                    "\"fullName\":\"test\"," +
                    "\"notes\": \"This fax number is operational 0800 to 1715 EST, Mon-Fri\"," +
                    "\"localizations\": { \"it\": { \"notes\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\" } }" +
                    "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testNotes1 - 1", 2, vcard.getNotes().size());
        assertEquals("testNotes1 - 2", "This fax number is operational 0800 to 1715 EST, Mon-Fri", vcard.getNotes().get(0).getValue());
        assertNull("testNotes1 - 3", vcard.getNotes().get(0).getLanguage());
        assertEquals("testNotes1 - 4", "1", vcard.getNotes().get(0).getAltId());
        assertEquals("testNotes1 - 5", "Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven", vcard.getNotes().get(1).getValue());
        assertEquals("testNotes1 - 6", "it", vcard.getNotes().get(1).getLanguage());
        assertEquals("testNotes1 - 7", "1", vcard.getNotes().get(1).getAltId());
    }

    @Test
    public void testNotes2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"notes\":\"This fax number is operational 0800 to 1715 EST, Mon-Fri\"," +
                "\"localizations\": { \"it\": { \"notes\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\" } }" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testNotes2 - 1", 2, vcard.getNotes().size());
        assertEquals("testNotes2 - 2", "This fax number is operational 0800 to 1715 EST, Mon-Fri", vcard.getNotes().get(0).getValue());
        assertNull("testNotes2 - 3", vcard.getNotes().get(0).getLanguage());
        assertEquals("testNotes2 - 4", "1", vcard.getNotes().get(0).getAltId());
        assertEquals("testNotes2 - 8", "Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven", vcard.getNotes().get(1).getValue());
        assertEquals("testNotes2 - 9", "it", vcard.getNotes().get(1).getLanguage());
        assertEquals("testNotes2 - 10", "1", vcard.getNotes().get(1).getAltId());
    }

    @Test
    public void testNotes3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"notes\":\"This fax number is operational 0800 to 1715 EST, Mon-Fri\\nThis is another note\"," +
                "\"localizations\": { \"it\": { \"notes\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\\nQuesta è un'altra nota\" } }" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testNotes3 - 1", 4, vcard.getNotes().size());
        assertEquals("testNotes3 - 2", "This fax number is operational 0800 to 1715 EST, Mon-Fri", vcard.getNotes().get(0).getValue());
        assertNull("testNotes3 - 3", vcard.getNotes().get(0).getLanguage());
        assertEquals("testNotes3 - 4", "1", vcard.getNotes().get(0).getAltId());
        assertEquals("testNotes3 - 5", "This is another note", vcard.getNotes().get(2).getValue());
        assertNull("testNotes3 - 6", vcard.getNotes().get(2).getLanguage());
        assertEquals("testNotes3 - 7", "2", vcard.getNotes().get(2).getAltId());
        assertEquals("testNotes3 - 8", "Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven", vcard.getNotes().get(1).getValue());
        assertEquals("testNotes3 - 9", "it", vcard.getNotes().get(1).getLanguage());
        assertEquals("testNotes3 - 10", "1", vcard.getNotes().get(1).getAltId());
        assertEquals("testNotes3 - 11", "Questa è un'altra nota", vcard.getNotes().get(3).getValue());
        assertEquals("testNotes3 - 12", "it", vcard.getNotes().get(3).getLanguage());
        assertEquals("testNotes3 - 13", "2", vcard.getNotes().get(3).getAltId());
    }

}
