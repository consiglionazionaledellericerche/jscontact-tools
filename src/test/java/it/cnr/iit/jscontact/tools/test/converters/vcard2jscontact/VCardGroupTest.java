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

import it.cnr.iit.jscontact.tools.dto.JSCardGroup;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class VCardGroupTest extends VCard2JSContactTest {

    //member must appear only for a group card
    @Test(expected = CardException.class)
    public void testVCardGroupInvalid() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:individual\n" +
                "FN:The Doe family\n" +
                "MEMBER:urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\n" +
                "MEMBER:urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\n" +
                "END:VCARD";

        JSCardGroup jsCardGroup = (JSCardGroup) vCard2JSContact.convert(vcard).get(0);

    }


    @Test
    public void testVCardGroupValid1() throws IOException, CardException {

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

        JSCardGroup jsCardGroup = (JSCardGroup) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testVCardGroupValid1 - 1",jsCardGroup != null);
        assertTrue("testVCardGroupValid1 - 2",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testVCardGroupValid1 - 3",jsCardGroup.getName().equals("The Doe family"));
        assertTrue("testVCardGroupValid1 - 4",jsCardGroup.getCards().length == 2);
        assertTrue("testVCardGroupValid1 - 5",jsCardGroup.getCards()[0].getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testVCardGroupValid1 - 6",jsCardGroup.getCards()[0].getFullName().getValue().equals("John Doe"));
        assertTrue("testVCardGroupValid1 - 7",jsCardGroup.getCards()[1].getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testVCardGroupValid1 - 8",jsCardGroup.getCards()[1].getFullName().getValue().equals("Jane Doe"));

    }


    @Test
    public void testVCardGroupValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:group\n" +
                "FN:Funky distribution list\n" +
                "MEMBER:mailto:subscriber1@example.com\n" +
                "MEMBER:xmpp:subscriber2@example.com\n" +
                "MEMBER:sip:subscriber3@example.com\n" +
                "MEMBER:tel:+1-418-555-5555\n" +
                "END:VCARD";

        JSCardGroup jsCardGroup = (JSCardGroup) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testVCardGroupValid2 - 1",jsCardGroup != null);
        assertTrue("testVCardGroupValid2 - 2",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testVCardGroupValid2 - 3",jsCardGroup.getName().equals("Funky distribution list"));
        assertTrue("testVCardGroupValid2 - 4",jsCardGroup.getCards().length == 4);
        assertTrue("testVCardGroupValid2 - 5",jsCardGroup.getCards()[0].getUid().equals("mailto:subscriber1@example.com"));
        assertTrue("testVCardGroupValid2 - 6",jsCardGroup.getCards()[1].getUid().equals("xmpp:subscriber2@example.com"));
        assertTrue("testVCardGroupValid2 - 7",jsCardGroup.getCards()[2].getUid().equals("sip:subscriber3@example.com"));
        assertTrue("testVCardGroupValid2 - 8",jsCardGroup.getCards()[3].getUid().equals("tel:+1-418-555-5555"));

    }

    @Test
    public void testVCardGroupValid3() throws IOException, CardException {

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

        JSCardGroup jsCardGroup = (JSCardGroup) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testVCardGroupValid3 - 1",jsCardGroup != null);
        assertTrue("testVCardGroupValid3 - 2",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testVCardGroupValid3 - 3",jsCardGroup.getName().equals("The Doe family"));
        assertTrue("testVCardGroupValid3 - 4",jsCardGroup.getCards().length == 2);
        assertTrue("testVCardGroupValid3 - 5",jsCardGroup.getCards()[0].getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testVCardGroupValid3 - 6",jsCardGroup.getCards()[0].getFullName().getValue().equals("John Doe"));
        assertTrue("testVCardGroupValid3 - 7",jsCardGroup.getCards()[1].getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testVCardGroupValid3 - 8",jsCardGroup.getCards()[1].getFullName().getValue().equals("Jane Doe"));

    }

    @Test
    public void testVCardGroupValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "KIND:group\n" +
                "FN:Funky distribution list\n" +
                "MEMBER:mailto:subscriber1@example.com\n" +
                "MEMBER;PREF=2:xmpp:subscriber2@example.com\n" +
                "MEMBER:sip:subscriber3@example.com\n" +
                "MEMBER;PREF=1:tel:+1-418-555-5555\n" +
                "END:VCARD";

        JSCardGroup jsCardGroup = (JSCardGroup) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testVCardGroupValid4 - 1",jsCardGroup != null);
        assertTrue("testVCardGroupValid4 - 2",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testVCardGroupValid4 - 3",jsCardGroup.getName().equals("Funky distribution list"));
        assertTrue("testVCardGroupValid4 - 4",jsCardGroup.getCards().length == 4);
        assertTrue("testVCardGroupValid4 - 5",jsCardGroup.getCards()[2].getUid().equals("mailto:subscriber1@example.com"));
        assertTrue("testVCardGroupValid4 - 6",jsCardGroup.getCards()[1].getUid().equals("xmpp:subscriber2@example.com"));
        assertTrue("testVCardGroupValid4 - 7",jsCardGroup.getCards()[3].getUid().equals("sip:subscriber3@example.com"));
        assertTrue("testVCardGroupValid4 - 8",jsCardGroup.getCards()[0].getUid().equals("tel:+1-418-555-5555"));

    }


}
