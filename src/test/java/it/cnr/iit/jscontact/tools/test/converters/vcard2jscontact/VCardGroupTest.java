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
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.CardGroup;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class VCardGroupTest extends VCard2JSContactTest {

    //member must appear only for a group card
    @Test(expected = CardException.class)
    public void testVCardGroupInvalid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:individual\n" +
                "FN:The Doe family\n" +
                "MEMBER:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "MEMBER:urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard);

    }


    @Test
    public void testVCardGroupValid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:group\n" +
                "FN:The Doe family\n" +
                "MEMBER:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "MEMBER:urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Doe\n" +
                "UID:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:Jane Doe\n" +
                "UID:urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\n" +
                "END:VCARD";

        List<JSContact> jsContacts = vCard2JSContact.convert(vcard);
        assertEquals("testVCardGroupValid1 - 1", 3, jsContacts.size());
        assertTrue("testVCardGroupValid1 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testVCardGroupValid1 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testVCardGroupValid1 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testVCardGroupValid1 - 5", "The Doe family", jsCardGroup.getCard().getFullName());
        assertEquals("testVCardGroupValid1 - 6", 2, jsCardGroup.getMembers().size());
        assertSame("testVCardGroupValid1 - 7", jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"), Boolean.TRUE);
        assertSame("testVCardGroupValid1 - 8", jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"), Boolean.TRUE);
        Card jsCard = (Card) jsContacts.get(1);
        assertEquals("testVCardGroupValid1 - 9", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());
        assertEquals("testVCardGroupValid1 - 10", "John Doe", jsCard.getFullName());
        jsCard = (Card) jsContacts.get(2);
        assertEquals("testVCardGroupValid1 - 11", "urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519", jsCard.getUid());
        assertEquals("testVCardGroupValid1 - 12", "Jane Doe", jsCard.getFullName());

    }


    @Test
    public void testVCardGroupValid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:group\n" +
                "FN:Funky distribution list\n" +
                "MEMBER:mailto:subscriber1@example.com\n" +
                "MEMBER:xmpp:subscriber2@example.com\n" +
                "MEMBER:sip:subscriber3@example.com\n" +
                "MEMBER:tel:+1-418-555-5555\n" +
                "END:VCARD";

        List<JSContact> jsContacts = vCard2JSContact.convert(vcard);
        assertEquals("testVCardGroupValid2 - 1", 1, jsContacts.size());
        assertTrue("testVCardGroupValid2 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testVCardGroupValid2 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testVCardGroupValid2 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testVCardGroupValid2 - 5", "Funky distribution list", jsCardGroup.getCard().getFullName());
        assertEquals("testVCardGroupValid2 - 6", 4, jsCardGroup.getMembers().size());
        assertSame("testVCardGroupValid2 - 7", jsCardGroup.getMembers().get("mailto:subscriber1@example.com"), Boolean.TRUE);
        assertSame("testVCardGroupValid2 - 8", jsCardGroup.getMembers().get("xmpp:subscriber2@example.com"), Boolean.TRUE);
        assertSame("testVCardGroupValid2 - 9", jsCardGroup.getMembers().get("sip:subscriber3@example.com"), Boolean.TRUE);
        assertSame("testVCardGroupValid2 - 10", jsCardGroup.getMembers().get("tel:+1-418-555-5555"), Boolean.TRUE);

    }

    @Test
    public void testVCardGroupValid3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Doe\n" +
                "UID:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:Jane Doe\n" +
                "UID:urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\n" +
                "END:VCARD\n" +
                "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:group\n" +
                "FN:The Doe family\n" +
                "MEMBER:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "MEMBER:urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\n" +
                "END:VCARD";

        List<JSContact> jsContacts = vCard2JSContact.convert(vcard);
        assertEquals("testVCardGroupValid3 - 1", 3, jsContacts.size());
        assertTrue("testVCardGroupValid3 - 2",jsContacts.get(2) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(2);
        assertTrue("testVCardGroupValid3 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testVCardGroupValid3 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testVCardGroupValid3 - 5", "The Doe family", jsCardGroup.getCard().getFullName());
        assertEquals("testVCardGroupValid3 - 6", 2, jsCardGroup.getMembers().size());
        assertSame("testVCardGroupValid3 - 7", jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"), Boolean.TRUE);
        assertSame("testVCardGroupValid3 - 8", jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"), Boolean.TRUE);
        Card jsCard = (Card) jsContacts.get(0);
        assertEquals("testVCardGroupValid3 - 9", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());
        assertEquals("testVCardGroupValid3 - 10", "John Doe", jsCard.getFullName());
        jsCard = (Card) jsContacts.get(1);
        assertEquals("testVCardGroupValid3 - 11", "urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519", jsCard.getUid());
        assertEquals("testVCardGroupValid3 - 12", "Jane Doe", jsCard.getFullName());

    }

    @Test
    public void testVCardGroupValid4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:group\n" +
                "FN:Funky distribution list\n" +
                "MEMBER:mailto:subscriber1@example.com\n" +
                "MEMBER;PREF=2:xmpp:subscriber2@example.com\n" +
                "MEMBER:sip:subscriber3@example.com\n" +
                "MEMBER;PREF=1:tel:+1-418-555-5555\n" +
                "END:VCARD";

        List<JSContact> jsContacts = vCard2JSContact.convert(vcard);
        assertEquals("testVCardGroupValid4 - 1", 1, jsContacts.size());
        assertTrue("testVCardGroupValid4 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testVCardGroupValid4 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testVCardGroupValid4 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testVCardGroupValid4 - 5", "Funky distribution list", jsCardGroup.getCard().getFullName());
        assertEquals("testVCardGroupValid4 - 6", 4, jsCardGroup.getMembers().size());
        assertSame("testVCardGroupValid4 - 7", jsCardGroup.getMembers().get("mailto:subscriber1@example.com"), Boolean.TRUE);
        assertSame("testVCardGroupValid4 - 8", jsCardGroup.getMembers().get("xmpp:subscriber2@example.com"), Boolean.TRUE);
        assertSame("testVCardGroupValid4 - 9", jsCardGroup.getMembers().get("sip:subscriber3@example.com"), Boolean.TRUE);
        assertSame("testVCardGroupValid4 - 10", jsCardGroup.getMembers().get("tel:+1-418-555-5555"), Boolean.TRUE);

        List<String> keys = new ArrayList<>(jsCardGroup.getMembers().keySet());
        assertEquals("testVCardGroupValid4 - 11", "tel:+1-418-555-5555", keys.get(0));
        assertEquals("testVCardGroupValid4 - 12", "xmpp:subscriber2@example.com", keys.get(1));

    }


}
