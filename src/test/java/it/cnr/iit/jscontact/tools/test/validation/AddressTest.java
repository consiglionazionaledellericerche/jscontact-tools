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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class AddressTest extends AbstractTest {

    @Test
    public void testInvalidCountryCode() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                                .countryCode("ita")
                                                                                .build());
                                                                        }};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertTrue("testInvalidCountryCode-1", !jsCard.isValid());
        assertTrue("testInvalidCountryCode-2", jsCard.getValidationMessage().equals("invalid countryCode in Address"));
    }

    @Test
    public void testValidCountryCode() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                                .countryCode("it")
                                                                                .build());
                                                                       }};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();

        assertTrue("testValidCountryCode", jsCard.isValid());

    }

    @Test
    public void testInvalidCoordinates() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                            .coordinates("46.772673,-71.282945")
                                                                            .build());
                                                                    }};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();

        assertTrue("testInvalidCoordinates-1", !jsCard.isValid());
        assertTrue("testInvalidCoordinates-2", jsCard.getValidationMessage().equals("invalid coordinates in Address"));
    }

    @Test
    public void testValidCoordinates() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                        .coordinates("geo:46.772673,-71.282945")
                                                                        .build());
                                                                       }};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();

        assertTrue("testValidCoordinates", jsCard.isValid());

    }

    @Test
    public void testInvalidAddressId() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("$$$$$", Address.builder()
                .coordinates("geo:46.772673,-71.282945")
                .build());
        }};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertTrue("testInvalidAddressId-1", !jsCard.isValid());
        assertTrue("testInvalidAddressId-2", jsCard.getValidationMessage().equals("invalid Id in Map<Id,Address>"));
    }

}
