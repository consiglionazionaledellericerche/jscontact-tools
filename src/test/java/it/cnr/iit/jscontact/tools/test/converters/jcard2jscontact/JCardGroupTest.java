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
package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JCardGroupTest extends JCard2JSContactTest {

    //member must appear only for a group card
    @Test(expected = CardException.class)
    public void testJCardGroupInvalid() throws CardException {

        String jcard="[" +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"individual\"], " +
                "[\"fn\", {}, \"text\", \"The Doe family\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"] " +
                "]]" +
                "]";
        jCard2JSContact.convert(jcard);
    }

    @Test
    public void testJCardGroup1() throws CardException {

        String jcard="[" +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"group\"], " +
                "[\"fn\", {}, \"text\", \"The Doe family\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"] " +
                "]] ,"  +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Doe\"], " +
                "[\"uid\", {}, \"uri\", \"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"] " +
                "]] ,"  +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"Jane Doe\"], " +
                "[\"uid\", {}, \"uri\", \"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"] " +
                "]]" +
                "]";

        List<Card> jsCards = jCard2JSContact.convert(jcard);
        assertEquals("testJCardGroup1 - 1", 3, jsCards.size());
        Card jsCardGroup = (Card) jsCards.get(0);
        assertTrue("testJCardGroup1 - 2", jsCardGroup.getKind().isGroup());
        assertTrue("testJCardGroup1 - 3",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testJCardGroup1 - 4", "The Doe family", jsCardGroup.getFullName());
        assertEquals("testJCardGroup1 - 5", 2, jsCardGroup.getMembers().size());
        assertSame("testJCardGroup1 - 6", jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"), Boolean.TRUE);
        assertSame("testJCardGroup1 - 7", jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"), Boolean.TRUE);
        Card jsCard = (Card) jsCards.get(1);
        assertEquals("testJCardGroup1 - 8", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());
        assertEquals("testJCardGroup1 - 9", "John Doe", jsCard.getFullName());
        jsCard = (Card) jsCards.get(2);
        assertEquals("testJCardGroup1 - 10", "urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519", jsCard.getUid());
        assertEquals("testJCardGroup1 - 11", "Jane Doe", jsCard.getFullName());

    }

    @Test
    public void testJCardGroup2() throws CardException {

        String jcard="[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"group\"], " +
                "[\"fn\", {}, \"text\", \"Funky distribution list\"], " +
                "[\"member\", {}, \"uri\", \"mailto:subscriber1@example.com\"], " +
                "[\"member\", {}, \"uri\", \"xmpp:subscriber2@example.com\"], " +
                "[\"member\", {}, \"uri\", \"sip:subscriber3@example.com\"], " +
                "[\"member\", {}, \"uri\", \"tel:+1-418-555-5555\"] " +
                "]]";

        List<Card> jsCards = jCard2JSContact.convert(jcard);
        assertEquals("testjCardGroup2 - 1", 1, jsCards.size());
        Card jsCardGroup = jsCards.get(0);
        assertTrue("testJCardGroup2 - 2", jsCardGroup.getKind().isGroup());
        assertTrue("testJCardGroup2 - 3",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testJCardGroup2 - 4", "Funky distribution list", jsCardGroup.getFullName());
        assertEquals("testJCardGroup2 - 5", 4, jsCardGroup.getMembers().size());
        assertSame("testJCardGroup2 - 6", jsCardGroup.getMembers().get("mailto:subscriber1@example.com"), Boolean.TRUE);
        assertSame("testJCardGroup2 - 7", jsCardGroup.getMembers().get("xmpp:subscriber2@example.com"), Boolean.TRUE);
        assertSame("testJCardGroup2 - 8", jsCardGroup.getMembers().get("sip:subscriber3@example.com"), Boolean.TRUE);
        assertSame("testJCardGroup2 - 9", jsCardGroup.getMembers().get("tel:+1-418-555-5555"), Boolean.TRUE);

    }


    @Test
    public void testJCardGroup3() throws CardException {

        String jcard="[" +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Doe\"], " +
                "[\"uid\", {}, \"uri\", \"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"] " +
                "]] ,"  +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"Jane Doe\"], " +
                "[\"uid\", {}, \"uri\", \"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"] " +
                "]] ," +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"group\"], " +
                "[\"fn\", {}, \"text\", \"The Doe family\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"] " +
                "]] "  +
                "]";

        List<Card> jsCards = jCard2JSContact.convert(jcard);
        assertEquals("testJCardGroup3 - 1", 3, jsCards.size());
        Card jsCardGroup = jsCards.get(2);
        assertTrue("testJCardGroup3 - 2", jsCardGroup.getKind().isGroup());
        assertTrue("testJCardGroup3 - 3",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testJCardGroup3 - 4", "The Doe family", jsCardGroup.getFullName());
        assertEquals("testJCardGroup3 - 5", 2, jsCardGroup.getMembers().size());
        assertSame("testJCardGroup3 - 6", jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"), Boolean.TRUE);
        assertSame("testJCardGroup3 - 7", jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"), Boolean.TRUE);
        Card jsCard = jsCards.get(0);
        assertEquals("testJCardGroup3 - 8", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());
        assertEquals("testJCardGroup3 - 9", "John Doe", jsCard.getFullName());
        jsCard = jsCards.get(1);
        assertEquals("testJCardGroup3 - 10", "urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519", jsCard.getUid());
        assertEquals("testJCardGroup3 - 11", "Jane Doe", jsCard.getFullName());

    }


    @Test
    public void testJCardGroup4() throws CardException {

        String jcard="[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"group\"], " +
                "[\"fn\", {}, \"text\", \"Funky distribution list\"], " +
                "[\"member\", {}, \"uri\", \"mailto:subscriber1@example.com\"], " +
                "[\"member\", {\"pref\":2}, \"uri\", \"xmpp:subscriber2@example.com\"], " +
                "[\"member\", {}, \"uri\", \"sip:subscriber3@example.com\"], " +
                "[\"member\", {\"pref\":1}, \"uri\", \"tel:+1-418-555-5555\"] " +
                "]]";

        List<Card> jsCards = jCard2JSContact.convert(jcard);
        assertEquals("testJCardGroup4 - 1", 1, jsCards.size());
        Card jsCardGroup = jsCards.get(0);
        assertTrue("testJCardGroup4 - 2", jsCardGroup.getKind().isGroup());
        assertTrue("testJCardGroup4 - 3",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testJCardGroup4 - 4", "Funky distribution list", jsCardGroup.getFullName());
        assertEquals("testJCardGroup4 - 5", 4, jsCardGroup.getMembers().size());
        assertSame("testJCardGroup4 - 6", jsCardGroup.getMembers().get("mailto:subscriber1@example.com"), Boolean.TRUE);
        assertSame("testJCardGroup4 - 7", jsCardGroup.getMembers().get("xmpp:subscriber2@example.com"), Boolean.TRUE);
        assertSame("testJCardGroup4 - 8", jsCardGroup.getMembers().get("sip:subscriber3@example.com"), Boolean.TRUE);
        assertSame("testJCardGroup4 - 9", jsCardGroup.getMembers().get("tel:+1-418-555-5555"), Boolean.TRUE);

        List<String> keys = new ArrayList<>(jsCardGroup.getMembers().keySet());
        assertEquals("testJCardGroup4 - 10", "tel:+1-418-555-5555", keys.get(0));
        assertEquals("testJCardGroup4 - 11", "xmpp:subscriber2@example.com", keys.get(1));

    }

}
