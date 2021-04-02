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

import it.cnr.iit.jscontact.tools.dto.ContactLanguage;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.KindType;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class ContactLanguageTest extends AbstractTest {

    @Test
    public void testValidContactLanguage1() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>(){{ put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").build()});}};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testValidContactLanguage1", jsCard.isValid());
    }

    @Test
    public void testValidContactLanguage2() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>(){{ put("it", new ContactLanguage[] { ContactLanguage.builder().pref(1).build()});}};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();
        assertTrue("testValidContactLanguage2", jsCard.isValid());
    }

    @Test
    public void testValidContactLanguage3() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>(){{ put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").pref(1).build()});}};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testValidContactLanguage3", jsCard.isValid());
    }

    @Test
    public void testInvalidContactLanguage1() {

        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(new HashMap<String, ContactLanguage[]>(){{put("it", null); }})
                .build();

        assertTrue("testInvalidContactLanguage1-1", !jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactLanguage1-2", messages.contains("null ContactLanguage in PreferredContactedLanguages"));
        assertTrue("testInvalidContactLanguage1-3", messages.contains("invalid preferredContactLanguages in JSContact"));
    }

    @Test
    public void testInvalidContactLanguage2() {

        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(new HashMap<String, ContactLanguage[]>(){{put("it",new ContactLanguage[] { ContactLanguage.builder().build()});}})
                .build();

        assertTrue("testInvalidContactLanguage2-1", !jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactLanguage2-2", messages.contains("at least one not null member is missing in ContactLanguage"));
        assertTrue("testInvalidContactLanguage2-3", messages.contains("invalid preferredContactLanguages in JSContact"));
    }

    @Test
    public void testInvalidContactLanguage3() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>(){{ put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").pref(0).build()});}};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage3-1", !jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactLanguage3-2", messages.contains("invalid pref in ContactLanguage - min value must be 1"));
        assertTrue("testInvalidContactLanguage3-3", messages.contains("invalid preferredContactLanguages in JSContact"));
    }

    @Test
    public void testInvalidContactLanguage4() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>(){{ put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").pref(101).build()});}};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage4-1", !jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactLanguage4-2", messages.contains("invalid pref in ContactLanguage - max value must be 100"));
        assertTrue("testInvalidContactLanguage4-3", messages.contains("invalid preferredContactLanguages in JSContact"));
    }

    @Test
    public void testInvalidContactLanguage5() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>(){{put("  ", new ContactLanguage[] { ContactLanguage.builder().type("work").pref(101).build()});}};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage5-1", !jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactLanguage5-2", messages.contains("invalid language tag in PreferredContactedLanguages"));
        assertTrue("testInvalidContactLanguage5-3", messages.contains("invalid preferredContactLanguages in JSContact"));
    }

}
