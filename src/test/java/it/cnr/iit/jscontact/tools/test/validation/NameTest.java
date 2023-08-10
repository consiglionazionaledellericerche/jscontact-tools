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

import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NameTest extends AbstractTest  {

    @Test
    public void testInvalidSortAs1() {

        Map sortAs = new HashMap<NameComponentKind, String>(){{ put(NameComponentKind.surname(), "Loffredo");}};

        Name name = Name.builder()
             .full("Mario Loffredo")
             .sortAs(sortAs)
             .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .name(name)
                .build();

        assertFalse("testInvalidSortAs1-1", jsCard.isValid());
        assertTrue("testInvalidSortAs1-2", jsCard.getValidationMessages().contains("if sortAs is set, components must be set"));

    }


    @Test
    public void testInvalidSortAs2() {

        Map sortAs = new HashMap<NameComponentKind, String>(){{ put(NameComponentKind.surname(), "Loffredo");}};

        List components = new ArrayList<NameComponent>();
        components.add(NameComponent.given("Mario"));

        Name name = Name.builder()
                .full("Mario Loffredo")
                .components((NameComponent[]) components.toArray(new NameComponent[0]))
                .sortAs(sortAs)
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .name(name)
                .build();

        assertFalse("testInvalidSortAs2-1", jsCard.isValid());
        assertTrue("testInvalidSortAs2-2", jsCard.getValidationMessages().contains("the name component surname of the sortAs member must be present in the components member"));

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

        List components = new ArrayList<NameComponent>();
        components.add(NameComponent.given("Mario"));
        components.add(NameComponent.separator(","));
        components.add(NameComponent.separator(" "));
        components.add(NameComponent.surname("Loffredo"));
        Name name = Name.builder()
                .full("Mario Loffredo")
                .components((NameComponent[]) components.toArray(new NameComponent[0]))
                .isOrdered(Boolean.TRUE)
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .name(name)
                .build();

        assertFalse("testInvalidSeparators1-1", jsCard.isValid());
        assertTrue("testInvalidSeparators1-2", jsCard.getValidationMessages().contains("two consecutive separator components must not be set"));
    }

    @Test
    public void testInvalidSeparators2() {

        List components = new ArrayList<NameComponent>();
        components.add(NameComponent.given("Mario"));
        components.add(NameComponent.separator(","));
        components.add(NameComponent.surname("Loffredo"));
        Name name = Name.builder()
                .full("Mario Loffredo")
                .components((NameComponent[]) components.toArray(new NameComponent[0]))
                .isOrdered(Boolean.FALSE)
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .name(name)
                .build();

        assertFalse("testInvalidSeparators2-1", jsCard.isValid());
        assertTrue("testInvalidSeparators2-2", jsCard.getValidationMessages().contains("if a separator component is set, the isOrdered member of the parent object must be true"));
    }

    @Test
    public void testInvalidSeparators3() {

        List components = new ArrayList<NameComponent>();
        components.add(NameComponent.separator(","));

        Name name = Name.builder()
                .full("Mario Loffredo")
                .components((NameComponent[]) components.toArray(new NameComponent[0]))
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .name(name)
                .build();

        assertFalse("testInvalidSeparators3-1", jsCard.isValid());
        assertTrue("testInvalidSeparators3-2", jsCard.getValidationMessages().contains("the components array must include at least one component other than separator"));

    }

    @Test
    public void testInvalidComponents() {

        Name name = Name.builder()
                .full("Mario Loffredo")
                .components(new NameComponent[0])
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .name(name)
                .build();

        assertFalse("testInvalidComponents-1", jsCard.isValid());
        assertTrue("testInvalidComponents-2", jsCard.getValidationMessages().contains("components array must not be empty"));
    }



}
