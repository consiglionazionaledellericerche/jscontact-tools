/*
 *    Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *     This program is fre software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Fre Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  Se the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, se <https://www.gnu.org/licenses/>.
 */
package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.ContactByType;
import it.cnr.iit.jscontact.tools.dto.ContactBy;
import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContactByTest extends AbstractTest {

    @Test
    public void testValidContactBy1() {

        Map<ContactByType, ContactBy[]> map = new HashMap<ContactByType, ContactBy[]>(){{ put(ContactByType.emails(), new ContactBy[] { ContactBy.builder().context(Context.work(), Boolean.TRUE).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(map)
                .build();

        assertTrue("testValidContactBy1", jsCard.isValid());
    }

    @Test
    public void testValidContactBy2() {

        Map<ContactByType, ContactBy[]> map = new HashMap<ContactByType, ContactBy[]>(){{ put(ContactByType.emails(), new ContactBy[] { ContactBy.builder().pref(1).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(map)
                .build();
        assertTrue("testValidContactBy2", jsCard.isValid());
    }

    @Test
    public void testValidContactBy3() {

        Map<ContactByType, ContactBy[]> map = new HashMap<ContactByType, ContactBy[]>(){{ put(ContactByType.emails(), new ContactBy[] { ContactBy.builder().context(Context.work(), Boolean.TRUE).pref(1).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(map)
                .build();

        assertTrue("testValidContactBy3", jsCard.isValid());
    }

    @Test
    public void testInvalidContactBy1() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(new HashMap<ContactByType, ContactBy[]>(){{put(ContactByType.emails(), null); }})
                .build();

        assertFalse("testInvalidContactBy1-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactBy1-2", messages.contains("null ContactBy in contactBy"));
        assertTrue("testInvalidContactBy1-3", messages.contains("invalid contactBy in JSContact"));
    }

    @Test
    public void testInvalidContactBy2() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(new HashMap<ContactByType, ContactBy[]>(){{put(ContactByType.emails(),new ContactBy[] { ContactBy.builder().build()});}})
                .build();

        assertFalse("testInvalidContactBy2-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactBy2-2", messages.contains("at least one not null member other than @type is missing in ContactBy"));
        assertTrue("testInvalidContactBy2-3", messages.contains("invalid contactBy in JSContact"));
    }

    @Test
    public void testInvalidContactBy3() {

        Map<ContactByType, ContactBy[]> map = new HashMap<ContactByType, ContactBy[]>(){{ put(ContactByType.emails(), new ContactBy[] { ContactBy.builder().context(Context.work(), Boolean.TRUE).pref(0).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(map)
                .build();

        assertFalse("testInvalidContactBy3-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactBy3-2", messages.contains("invalid pref in ContactBy - value must be greater or equal than 1"));
        assertTrue("testInvalidContactBy3-3", messages.contains("invalid contactBy in JSContact"));
    }

    @Test
    public void testInvalidContactBy4() {

        Map<ContactByType, ContactBy[]> map = new HashMap<ContactByType, ContactBy[]>(){{ put(ContactByType.emails(), new ContactBy[] { ContactBy.builder().context(Context.work(), Boolean.TRUE).pref(101).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(map)
                .build();

        assertFalse("testInvalidContactBy4-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactBy4-2", messages.contains("invalid pref in ContactBy - value must be less or equal than 100"));
        assertTrue("testInvalidContactBy4-3", messages.contains("invalid contactBy in JSContact"));
    }

    @Test
    public void testInvalidContactBy5() {

        Map<ContactByType, ContactBy[]> map = new HashMap<ContactByType, ContactBy[]>(){{put(ContactByType.ext("example.com:ext"), new ContactBy[] { ContactBy.builder().context(Context.work(), Boolean.TRUE).pref(101).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .contactBy(map)
                .build();

        assertFalse("testInvalidContactBy5-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactBy5-2", messages.contains("invalid pref in ContactBy - value must be less or equal than 100"));
        assertTrue("testInvalidContactBy5-3", messages.contains("invalid contactBy in JSContact"));
    }

}
