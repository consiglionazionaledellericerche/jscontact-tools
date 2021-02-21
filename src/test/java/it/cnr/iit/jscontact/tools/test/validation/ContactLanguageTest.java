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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ContactLanguageTest extends AbstractTest {

    @Test
    public void testValidContactLanguage1() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").build()});
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testValidContactLanguage1", jsCard.isValid());
    }

    @Test
    public void testValidContactLanguage2() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it", new ContactLanguage[] { ContactLanguage.builder().preference(1).build()});
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testValidContactLanguage2", jsCard.isValid());
    }

    @Test
    public void testValidContactLanguage3() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").preference(1).build()});
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testValidContactLanguage3", jsCard.isValid());
    }

    @Test
    public void testInvalidContactLanguage1() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it", null);
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage1", !jsCard.isValid());
    }

    @Test
    public void testInvalidContactLanguage2() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it",new ContactLanguage[] { null });
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage2", !jsCard.isValid());
    }

    @Test
    public void testInvalidContactLanguage3() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it",new ContactLanguage[] { ContactLanguage.builder().build()});
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage3", !jsCard.isValid());
    }

    @Test
    public void testInvalidContactLanguage4() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").preference(0).build()});
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage4", !jsCard.isValid());
    }

    @Test
    public void testInvalidContactLanguage5() {

        Map<String, ContactLanguage[]> map = new HashMap<String, ContactLanguage[]>();
        map.put("it", new ContactLanguage[] { ContactLanguage.builder().type("work").preference(101).build()});
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .preferredContactLanguages(map)
                .build();

        assertTrue("testInvalidContactLanguage5", !jsCard.isValid());
    }

}
