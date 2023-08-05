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
import it.cnr.iit.jscontact.tools.dto.AddressComponent;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AddressTest extends AbstractTest {

    @Test
    public void testInvalidCountryCode() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                                .countryCode("ita")
                                                                                .build());
                                                                        }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertFalse("testInvalidCountryCode-1", jsCard.isValid());
        assertEquals("testInvalidCountryCode-2", "invalid countryCode in Address", jsCard.getValidationMessage());
    }

    @Test
    public void testValidCountryCode() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                                .countryCode("it")
                                                                                .build());
                                                                       }};
        Card jsCard = Card.builder()
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
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();

        assertFalse("testInvalidCoordinates-1", jsCard.isValid());
        assertEquals("testInvalidCoordinates-2", "invalid coordinates in Address", jsCard.getValidationMessage());
    }

    @Test
    public void testValidCoordinates() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                        .coordinates("geo:46.772673,-71.282945")
                                                                        .build());
                                                                       }};
        Card jsCard = Card.builder()
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
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertFalse("testInvalidAddressId-1", jsCard.isValid());
        assertEquals("testInvalidAddressId-2", "invalid Id in Map<Id,Address>", jsCard.getValidationMessage());
    }


    @Test
    public void testInvalidPhonetic() {

        List components = new ArrayList<AddressComponent>();
        components.add(AddressComponent.landmark("landmark", "phonetic"));
        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                .components((AddressComponent[]) components.toArray(new AddressComponent[0]))
                .build());
        }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertFalse("testInvalidPhonetic-1", jsCard.isValid());
        assertTrue("testInvalidPhonetic-2", jsCard.getValidationMessages().contains("if phonetic is set, at least one of the parent object phoneticSystem or phoneticScript properties must be set"));
    }

    @Test
    public void testInvalidSeparators1() {

        List components = new ArrayList<AddressComponent>();
        components.add(AddressComponent.landmark("landmark", null));
        components.add(AddressComponent.separator(" "));
        components.add(AddressComponent.separator(","));
        components.add(AddressComponent.country("it", null));
        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                .components((AddressComponent[]) components.toArray(new AddressComponent[0]))
                .isOrdered(Boolean.TRUE)
                .build());
        }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertFalse("testInvalidSeparators1-1", jsCard.isValid());
        assertTrue("testInvalidSeparators1-2", jsCard.getValidationMessages().contains("two consecutive separator components must not be set"));
    }

    @Test
    public void testInvalidSeparators2() {

        List components = new ArrayList<AddressComponent>();
        components.add(AddressComponent.landmark("landmark", null));
        components.add(AddressComponent.separator(","));
        components.add(AddressComponent.country("it", null));
        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                .components((AddressComponent[]) components.toArray(new AddressComponent[0]))
                .isOrdered(Boolean.FALSE)
                .build());
        }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertFalse("testInvalidSeparators2-1", jsCard.isValid());
        assertTrue("testInvalidSeparators2-2", jsCard.getValidationMessages().contains("if a separator component is set, the isOrdered member of the parent object must be true"));
    }

    @Test
    public void testInvalidComponents() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                .components(new AddressComponent[0])
                .build());
        }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertFalse("testInvalidComponents-1", jsCard.isValid());
        assertTrue("testInvalidComponents-2", jsCard.getValidationMessages().contains("components array must not be empty"));
    }

}
