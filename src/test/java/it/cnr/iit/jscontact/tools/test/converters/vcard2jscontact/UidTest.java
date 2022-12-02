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
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UidTest extends VCard2JSContactTest {

    @Test
    public void testUid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testUid1 - 1", jsCard.getUid());

    }

    @Test
    public void testUid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "UID:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testUid2 - 1", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());

    }

}
