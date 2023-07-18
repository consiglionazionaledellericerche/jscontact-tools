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

import it.cnr.iit.jscontact.tools.dto.LanguagePref;
import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LanguagePreferenceTest extends AbstractTest {

    @Test
    public void testValidLanguagePreference1() {

        Map<String, LanguagePref[]> map = new HashMap<String, LanguagePref[]>(){{ put("it", new LanguagePref[] { LanguagePref.builder().context(Context.work(), Boolean.TRUE).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(map)
                .build();

        assertTrue("testValidLanguagePreference1", jsCard.isValid());
    }

    @Test
    public void testValidLanguagePreference2() {

        Map<String, LanguagePref[]> map = new HashMap<String, LanguagePref[]>(){{ put("it", new LanguagePref[] { LanguagePref.builder().pref(1).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(map)
                .build();
        assertTrue("testValidLanguagePreference2", jsCard.isValid());
    }

    @Test
    public void testValidLanguagePreference3() {

        Map<String, LanguagePref[]> map = new HashMap<String, LanguagePref[]>(){{ put("it", new LanguagePref[] { LanguagePref.builder().context(Context.work(), Boolean.TRUE).pref(1).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(map)
                .build();

        assertTrue("testValidLanguagePreference3", jsCard.isValid());
    }

    @Test
    public void testInvalidLanguagePreference1() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(new HashMap<String, LanguagePref[]>(){{put("it", null); }})
                .build();

        assertFalse("testInvalidLanguagePreference1-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidLanguagePreference1-2", messages.contains("null LanguagePref in preferredLanguages"));
        assertTrue("testInvalidLanguagePreference1-3", messages.contains("invalid preferredLanguages in JSContact"));
    }

    @Test
    public void testInvalidLanguagePreference2() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(new HashMap<String, LanguagePref[]>(){{put("it",new LanguagePref[] { LanguagePref.builder().build()});}})
                .build();

        assertFalse("testInvalidLanguagePreference2-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidLanguagePreference2-2", messages.contains("at least one not null member other than @type is required in LanguagePref"));
        assertTrue("testInvalidLanguagePreference2-3", messages.contains("invalid preferredLanguages in JSContact"));
    }

    @Test
    public void testInvalidLanguagePreference3() {

        Map<String, LanguagePref[]> map = new HashMap<String, LanguagePref[]>(){{ put("it", new LanguagePref[] { LanguagePref.builder().context(Context.work(), Boolean.TRUE).pref(0).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(map)
                .build();

        assertFalse("testInvalidLanguagePreference3-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidLanguagePreference3-2", messages.contains("invalid pref in LanguagePref - value must be greater or equal than 1"));
        assertTrue("testInvalidLanguagePreference3-3", messages.contains("invalid preferredLanguages in JSContact"));
    }

    @Test
    public void testInvalidLanguagePreference4() {

        Map<String, LanguagePref[]> map = new HashMap<String, LanguagePref[]>(){{ put("it", new LanguagePref[] { LanguagePref.builder().context(Context.work(), Boolean.TRUE).pref(101).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(map)
                .build();

        assertFalse("testInvalidLanguagePreference4-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidLanguagePreference4-2", messages.contains("invalid pref in LanguagePref - value must be less or equal than 100"));
        assertTrue("testInvalidLanguagePreference4-3", messages.contains("invalid preferredLanguages in JSContact"));
    }

    @Test
    public void testInvalidLanguagePreference5() {

        Map<String, LanguagePref[]> map = new HashMap<String, LanguagePref[]>(){{put("  ", new LanguagePref[] { LanguagePref.builder().context(Context.work(), Boolean.TRUE).pref(101).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredLanguages(map)
                .build();

        assertFalse("testInvalidLanguagePreference5-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidLanguagePreference5-2", messages.contains("invalid language tag in preferredLanguages"));
        assertTrue("testInvalidLanguagePreference5-3", messages.contains("invalid preferredLanguages in JSContact"));
    }

}
