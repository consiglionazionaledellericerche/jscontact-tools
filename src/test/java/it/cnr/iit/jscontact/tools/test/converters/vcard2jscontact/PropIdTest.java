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
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PropIdTest extends VCard2JSContactTest {

    @Test
    public void testPropId1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;PROP-ID=TEST;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder()
                                                        .config(VCard2JSContactConfig.builder()
                                                                .usePropIds(true)
                                                                .build())
                                                        .build();

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPropId1 - 1", jsCard.getAddresses());
        assertEquals("testPropId1 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testPropId1 - 3", "US", jsCard.getAddresses().get("TEST").getCountryCode());
        assertEquals("testPropId1 - 4", "USA", jsCard.getAddresses().get("TEST").getCountry());
        assertEquals("testPropId1 - 5", "20190", jsCard.getAddresses().get("TEST").getPostcode());
        assertEquals("testPropId1 - 6", "Reston", jsCard.getAddresses().get("TEST").getLocality());
        assertEquals("testPropId1 - 7", "VA", jsCard.getAddresses().get("TEST").getRegion());
        assertEquals("testPropId1 - 8", "54321 Oak St", jsCard.getAddresses().get("TEST").getStreetAddress());
        assertEquals("testPropId1 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("TEST").getFull());

    }


    @Test
    public void testPropId2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;PROP-ID=TEST;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder()
                .config(VCard2JSContactConfig.builder()
                        .usePropIds(true)
                        .build())
                .build();

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPropId2 - 1", jsCard.getAddresses());
        assertEquals("testPropId2 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testPropId2 - 3", "US", jsCard.getAddresses().get("TEST").getCountryCode());
        assertEquals("testPropId2 - 4", "USA", jsCard.getAddresses().get("TEST").getCountry());
        assertEquals("testPropId2 - 5", "20190", jsCard.getAddresses().get("TEST").getPostcode());
        assertEquals("testPropId2 - 6", "Reston", jsCard.getAddresses().get("TEST").getLocality());
        assertEquals("testPropId2 - 7", "VA", jsCard.getAddresses().get("TEST").getRegion());
        assertEquals("testPropId2 - 8", "54321 Oak St", jsCard.getAddresses().get("TEST").getStreetAddress());
        assertEquals("testPropId2 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("TEST").getFull());
        assertEquals("testPropId2 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testPropId2 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testPropId2 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testPropId2 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testPropId2 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testPropId2 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetName());
        assertEquals("testPropId2 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFull());

    }



}
