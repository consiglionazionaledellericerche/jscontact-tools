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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PropIdTest extends VCard2JSContactTest {

    @Test
    public void testAddressesValid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;PROP-ID=TEST;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddressesValid1 - 1", jsCard.getAddresses());
        assertEquals("testAddressesValid1 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddressesValid1 - 3", "US", jsCard.getAddresses().get("TEST").getCountryCode());
        assertEquals("testAddressesValid1 - 4", "USA", jsCard.getAddresses().get("TEST").getCountry());
        assertEquals("testAddressesValid1 - 5", "20190", jsCard.getAddresses().get("TEST").getPostcode());
        assertEquals("testAddressesValid1 - 6", "Reston", jsCard.getAddresses().get("TEST").getLocality());
        assertEquals("testAddressesValid1 - 7", "VA", jsCard.getAddresses().get("TEST").getRegion());
        assertEquals("testAddressesValid1 - 8", "54321 Oak St", jsCard.getAddresses().get("TEST").getStreetDetails());
        assertEquals("testAddressesValid1 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("TEST").getFullAddress());

    }

}
