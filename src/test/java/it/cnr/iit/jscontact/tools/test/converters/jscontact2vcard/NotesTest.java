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

import static org.junit.Assert.assertTrue;

public class NotesTest extends JSContact2VCardTest {

    @Test
    public void testNotes1() throws IOException, CardException {

        String jscard="{" +
                    "\"@type\":\"Card\"," +
                    "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                    "\"fullName\":\"test\"," +
                    "\"notes\": \"This fax number is operational 0800 to 1715 EST, Mon-Fri\"," +
                    "\"localizations\": { \"it\": { \"/notes\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\" } }" +
                    "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testNotes1 - 1",vcard.getNotes().size() == 2);
        assertTrue("testNotes1 - 2",vcard.getNotes().get(0).getValue().equals("This fax number is operational 0800 to 1715 EST, Mon-Fri"));
        assertTrue("testNotes1 - 3",vcard.getNotes().get(0).getLanguage() == null);
        assertTrue("testNotes1 - 4",vcard.getNotes().get(0).getAltId().equals("1"));
        assertTrue("testNotes1 - 5",vcard.getNotes().get(1).getValue().equals("Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven"));
        assertTrue("testNotes1 - 6",vcard.getNotes().get(1).getLanguage().equals("it"));
        assertTrue("testNotes1 - 7",vcard.getNotes().get(1).getAltId().equals("1"));
    }

    @Test
    public void testNotes2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"notes\":\"This fax number is operational 0800 to 1715 EST, Mon-Fri\"," +
                "\"localizations\": { \"it\": { \"/notes\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\" } }" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testNotes2 - 1",vcard.getNotes().size() == 2);
        assertTrue("testNotes2 - 2",vcard.getNotes().get(0).getValue().equals("This fax number is operational 0800 to 1715 EST, Mon-Fri"));
        assertTrue("testNotes2 - 3",vcard.getNotes().get(0).getLanguage() == null);
        assertTrue("testNotes2 - 4",vcard.getNotes().get(0).getAltId().equals("1"));
        assertTrue("testNotes2 - 8",vcard.getNotes().get(1).getValue().equals("Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven"));
        assertTrue("testNotes2 - 9",vcard.getNotes().get(1).getLanguage().equals("it"));
        assertTrue("testNotes2 - 10",vcard.getNotes().get(1).getAltId().equals("1"));
    }

    @Test
    public void testNotes3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"notes\":\"This fax number is operational 0800 to 1715 EST, Mon-Fri\\nThis is another note\"," +
                "\"localizations\": { \"it\": { \"/notes\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\\nQuesta è un'altra nota\" } }" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testNotes3 - 1",vcard.getNotes().size() == 4);
        assertTrue("testNotes3 - 2",vcard.getNotes().get(0).getValue().equals("This fax number is operational 0800 to 1715 EST, Mon-Fri"));
        assertTrue("testNotes3 - 3",vcard.getNotes().get(0).getLanguage() == null);
        assertTrue("testNotes3 - 4",vcard.getNotes().get(0).getAltId().equals("1"));
        assertTrue("testNotes3 - 5",vcard.getNotes().get(2).getValue().equals("This is another note"));
        assertTrue("testNotes3 - 6",vcard.getNotes().get(2).getLanguage() == null);
        assertTrue("testNotes3 - 7",vcard.getNotes().get(2).getAltId().equals("2"));
        assertTrue("testNotes3 - 8",vcard.getNotes().get(1).getValue().equals("Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven"));
        assertTrue("testNotes3 - 9",vcard.getNotes().get(1).getLanguage().equals("it"));
        assertTrue("testNotes3 - 10",vcard.getNotes().get(1).getAltId().equals("1"));
        assertTrue("testNotes3 - 11",vcard.getNotes().get(3).getValue().equals("Questa è un'altra nota"));
        assertTrue("testNotes3 - 12",vcard.getNotes().get(3).getLanguage().equals("it"));
        assertTrue("testNotes3 - 13",vcard.getNotes().get(3).getAltId().equals("2"));
    }

}
