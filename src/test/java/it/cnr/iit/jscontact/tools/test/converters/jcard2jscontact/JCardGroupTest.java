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
import it.cnr.iit.jscontact.tools.dto.CardGroup;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class JCardGroupTest extends JCard2JSContactTest {

    //member must appear only for a group card
    @Test(expected = CardException.class)
    public void testJCardGroupInvalid() throws IOException, CardException {

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
    public void testJCardGroupValid1() throws IOException, CardException {

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

        List<JSContact> jsContacts = jCard2JSContact.convert(jcard);
        assertTrue("testJCardGroupValid1 - 1",jsContacts.size() == 3);
        assertTrue("testJCardGroupValid1 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testJCardGroupValid1 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testJCardGroupValid1 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid1 - 5",jsCardGroup.getCard().getFullName().equals("The Doe family"));
        assertTrue("testJCardGroupValid1 - 6",jsCardGroup.getMembers().size() == 2);
        assertTrue("testJCardGroupValid1 - 7",jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 8",jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        Card jsCard = (Card) jsContacts.get(1);
        assertTrue("testJCardGroupValid1 - 9",jsCard.getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid1 - 10",jsCard.getFullName().equals("John Doe"));
        jsCard = (Card) jsContacts.get(2);
        assertTrue("testJCardGroupValid1 - 11",jsCard.getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid1 - 12",jsCard.getFullName().equals("Jane Doe"));

    }

    @Test
    public void testJCardGroupValid2() throws IOException, CardException {

        String jcard="[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"group\"], " +
                "[\"fn\", {}, \"text\", \"Funky distribution list\"], " +
                "[\"member\", {}, \"uri\", \"mailto:subscriber1@example.com\"], " +
                "[\"member\", {}, \"uri\", \"xmpp:subscriber2@example.com\"], " +
                "[\"member\", {}, \"uri\", \"sip:subscriber3@example.com\"], " +
                "[\"member\", {}, \"uri\", \"tel:+1-418-555-5555\"] " +
                "]]";

        List<JSContact> jsContacts = jCard2JSContact.convert(jcard);
        assertTrue("testjCardGroupValid2 - 1",jsContacts.size() == 1);
        assertTrue("testJCardGroupValid2 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testJCardGroupValid2 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testJCardGroupValid2 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid2 - 5",jsCardGroup.getCard().getFullName().equals("Funky distribution list"));
        assertTrue("testJCardGroupValid2 - 6",jsCardGroup.getMembers().size() == 4);
        assertTrue("testJCardGroupValid2 - 7",jsCardGroup.getMembers().get("mailto:subscriber1@example.com") == Boolean.TRUE);
        assertTrue("testJCardGroupValid2 - 8",jsCardGroup.getMembers().get("xmpp:subscriber2@example.com") == Boolean.TRUE);
        assertTrue("testJCardGroupValid2 - 9",jsCardGroup.getMembers().get("sip:subscriber3@example.com") == Boolean.TRUE);
        assertTrue("testJCardGroupValid2 - 10",jsCardGroup.getMembers().get("tel:+1-418-555-5555") == Boolean.TRUE);

    }


    @Test
    public void testJCardGroupValid3() throws IOException, CardException {

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

        List<JSContact> jsContacts = jCard2JSContact.convert(jcard);
        assertTrue("testJCardGroupValid3 - 1",jsContacts.size() == 3);
        assertTrue("testJCardGroupValid3 - 2",jsContacts.get(2) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(2);
        assertTrue("testJCardGroupValid3 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testJCardGroupValid3 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid3 - 5",jsCardGroup.getCard().getFullName().equals("The Doe family"));
        assertTrue("testJCardGroupValid3 - 6",jsCardGroup.getMembers().size() == 2);
        assertTrue("testJCardGroupValid3 - 7",jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testJCardGroupValid3 - 8",jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        Card jsCard = (Card) jsContacts.get(0);
        assertTrue("testJCardGroupValid3 - 9",jsCard.getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid3 - 10",jsCard.getFullName().equals("John Doe"));
        jsCard = (Card) jsContacts.get(1);
        assertTrue("testJCardGroupValid3 - 11",jsCard.getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid3 - 12",jsCard.getFullName().equals("Jane Doe"));

    }


    @Test
    public void testJCardGroupValid4() throws IOException, CardException {

        String jcard="[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"group\"], " +
                "[\"fn\", {}, \"text\", \"Funky distribution list\"], " +
                "[\"member\", {}, \"uri\", \"mailto:subscriber1@example.com\"], " +
                "[\"member\", {\"pref\":\"2\"}, \"uri\", \"xmpp:subscriber2@example.com\"], " +
                "[\"member\", {}, \"uri\", \"sip:subscriber3@example.com\"], " +
                "[\"member\", {\"pref\":\"1\"}, \"uri\", \"tel:+1-418-555-5555\"] " +
                "]]";

        List<JSContact> jsContacts = jCard2JSContact.convert(jcard);
        assertTrue("testJCardGroupValid4 - 1",jsContacts.size() == 1);
        assertTrue("testJCardGroupValid4 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testJCardGroupValid4 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testJCardGroupValid4 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid4 - 5",jsCardGroup.getCard().getFullName().equals("Funky distribution list"));
        assertTrue("testJCardGroupValid4 - 6",jsCardGroup.getMembers().size() == 4);
        assertTrue("testJCardGroupValid4 - 7",jsCardGroup.getMembers().get("mailto:subscriber1@example.com") == Boolean.TRUE);
        assertTrue("testJCardGroupValid4 - 8",jsCardGroup.getMembers().get("xmpp:subscriber2@example.com") == Boolean.TRUE);
        assertTrue("testJCardGroupValid4 - 9",jsCardGroup.getMembers().get("sip:subscriber3@example.com") == Boolean.TRUE);
        assertTrue("testJCardGroupValid4 - 10",jsCardGroup.getMembers().get("tel:+1-418-555-5555") == Boolean.TRUE);

        List<String> keys = new ArrayList<>(jsCardGroup.getMembers().keySet());
        assertTrue("testJCardGroupValid4 - 11",keys.get(0).equals("tel:+1-418-555-5555"));
        assertTrue("testJCardGroupValid4 - 12",keys.get(1).equals("xmpp:subscriber2@example.com"));

    }

}
