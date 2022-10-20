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
import it.cnr.iit.jscontact.tools.dto.ChannelType;
import it.cnr.iit.jscontact.tools.dto.ContactChannelPreference;
import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContactChannelPreferenceTest extends AbstractTest {

    @Test
    public void testValidContactChannelPreference1() {

        Map<ChannelType, ContactChannelPreference[]> map = new HashMap<ChannelType, ContactChannelPreference[]>(){{ put(ChannelType.emails(), new ContactChannelPreference[] { ContactChannelPreference.builder().context(Context.work(), Boolean.TRUE).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(map)
                .build();

        assertTrue("testValidContactChannelPreference1", jsCard.isValid());
    }

    @Test
    public void testValidContactChannelPreference2() {

        Map<ChannelType, ContactChannelPreference[]> map = new HashMap<ChannelType, ContactChannelPreference[]>(){{ put(ChannelType.emails(), new ContactChannelPreference[] { ContactChannelPreference.builder().pref(1).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(map)
                .build();
        assertTrue("testValidContactChannelPreference2", jsCard.isValid());
    }

    @Test
    public void testValidContactChannelPreference3() {

        Map<ChannelType, ContactChannelPreference[]> map = new HashMap<ChannelType, ContactChannelPreference[]>(){{ put(ChannelType.emails(), new ContactChannelPreference[] { ContactChannelPreference.builder().context(Context.work(), Boolean.TRUE).pref(1).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(map)
                .build();

        assertTrue("testValidContactChannelPreference3", jsCard.isValid());
    }

    @Test
    public void testInvalidContactChannelPreference1() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(new HashMap<ChannelType, ContactChannelPreference[]>(){{put(ChannelType.emails(), null); }})
                .build();

        assertFalse("testInvalidContactChannelPreference1-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactChannelPreference1-2", messages.contains("null ContactChannelPreference in preferredContactedChannels"));
        assertTrue("testInvalidContactChannelPreference1-3", messages.contains("invalid preferredContactChannels in JSContact"));
    }

    @Test
    public void testInvalidContactChannelPreference2() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(new HashMap<ChannelType, ContactChannelPreference[]>(){{put(ChannelType.emails(),new ContactChannelPreference[] { ContactChannelPreference.builder().build()});}})
                .build();

        assertFalse("testInvalidContactChannelPreference2-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactChannelPreference2-2", messages.contains("at least one not null member other than @type is missing in ContactChannelPreference"));
        assertTrue("testInvalidContactChannelPreference2-3", messages.contains("invalid preferredContactChannels in JSContact"));
    }

    @Test
    public void testInvalidContactChannelPreference3() {

        Map<ChannelType, ContactChannelPreference[]> map = new HashMap<ChannelType, ContactChannelPreference[]>(){{ put(ChannelType.emails(), new ContactChannelPreference[] { ContactChannelPreference.builder().context(Context.work(), Boolean.TRUE).pref(0).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(map)
                .build();

        assertFalse("testInvalidContactChannelPreference3-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactChannelPreference3-2", messages.contains("invalid pref in ContactChannelPreference - value must be greater or equal than 1"));
        assertTrue("testInvalidContactChannelPreference3-3", messages.contains("invalid preferredContactChannels in JSContact"));
    }

    @Test
    public void testInvalidContactChannelPreference4() {

        Map<ChannelType, ContactChannelPreference[]> map = new HashMap<ChannelType, ContactChannelPreference[]>(){{ put(ChannelType.emails(), new ContactChannelPreference[] { ContactChannelPreference.builder().context(Context.work(), Boolean.TRUE).pref(101).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(map)
                .build();

        assertFalse("testInvalidContactChannelPreference4-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactChannelPreference4-2", messages.contains("invalid pref in ContactChannelPreference - value must be less or equal than 100"));
        assertTrue("testInvalidContactChannelPreference4-3", messages.contains("invalid preferredContactChannels in JSContact"));
    }

    @Test
    public void testInvalidContactChannelPreference5() {

        Map<ChannelType, ContactChannelPreference[]> map = new HashMap<ChannelType, ContactChannelPreference[]>(){{put(ChannelType.ext("ext"), new ContactChannelPreference[] { ContactChannelPreference.builder().context(Context.work(), Boolean.TRUE).pref(101).build()});}};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .preferredContactChannels(map)
                .build();

        assertFalse("testInvalidContactChannelPreference5-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidContactChannelPreference5-2", messages.contains("invalid pref in ContactChannelPreference - value must be less or equal than 100"));
        assertTrue("testInvalidContactChannelPreference5-3", messages.contains("invalid preferredContactChannels in JSContact"));
    }

}
