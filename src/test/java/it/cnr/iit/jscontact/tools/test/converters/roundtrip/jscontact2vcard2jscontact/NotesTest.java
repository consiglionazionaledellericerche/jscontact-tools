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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.jscontact2vcard2jscontact;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NotesTest extends RoundtripTest {

    @Test
    public void testNotes1() throws IOException, CardException {

        String jscard="{" +
                    "\"@type\":\"Card\"," +
                    "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                    "\"name\":{\"full\":\"test\"}," +
                    "\"notes\": {" +
                         "\"NOTE-1\" : { \"@type\": \"Note\", \"note\": \"This fax number is operational 0800 to 1715 EST, Mon-Fri\"} ," +
                         "\"NOTE-2\" : { \"@type\": \"Note\", \"note\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\", \"language\":\"it\" } " +
                    "}" +
                    "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testNotes1 - 1", jscard2, Card.toJSCard(jscard));
    }

}
