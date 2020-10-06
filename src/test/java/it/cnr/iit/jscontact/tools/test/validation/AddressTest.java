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
package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AddressTest extends AbstractTest {

    @Test
    public void testInvalidCountryCode() {

        Address address = Address.builder()
                                 .countryCode("ita")
                                 .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(new Address[]{address})
                .build();
        assertTrue("testInvalidCountryCode", !jsCard.isValid());

    }

    @Test
    public void testValidCountryCode() {

        Address address = Address.builder()
                .countryCode("it")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(new Address[]{address})
                .build();

        assertTrue("testValidCountryCode", jsCard.isValid());

    }

    @Test
    public void testInvalidCoordinates() {

        Address address = Address.builder()
                .coordinates("46.772673,-71.282945")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(new Address[]{address})
                .build();

        assertTrue("testInvalidCoordinates", !jsCard.isValid());

    }

    @Test
    public void testValidCoordinates() {

        Address address = Address.builder()
                .coordinates("geo:46.772673,-71.282945")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(new Address[]{address})
                .build();

        assertTrue("testValidCoordinates", jsCard.isValid());

    }

}
