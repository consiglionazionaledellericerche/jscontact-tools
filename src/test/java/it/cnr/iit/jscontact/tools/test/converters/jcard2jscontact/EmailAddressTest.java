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

import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class EmailAddressTest extends JCard2JSContactTest {

    @Test
    public void testEmailAddressValid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"email\", {\"type\": \"work\"}, \"text\", \"jqpublic@xyz.example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testEmailAddressValid1 - 1",jsCard.getEmails().size() == 1);
        assertTrue("testEmailAddressValid1 - 2",jsCard.getEmails().get("EMAIL-1").getContexts().size() == 1);
        assertTrue("testEmailAddressValid1 - 3",jsCard.getEmails().get("EMAIL-1").getContexts().get(Context.work()) == Boolean.TRUE);
        assertTrue("testEmailAddressValid1 - 4",jsCard.getEmails().get("EMAIL-1").getEmail().equals("jqpublic@xyz.example.com"));

    }

    @Test
    public void testEmailAddressValid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"email\", {\"type\": \"work\"}, \"text\", \"jqpublic@xyz.example.com\"], " +
                "[\"email\", {\"type\": \"home\", \"pref\": 1}, \"text\", \"jane_doe@example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testEmailAddressValid2 - 1",jsCard.getEmails().size() == 2);
        assertTrue("testEmailAddressValid2 - 2",jsCard.getEmails().get("EMAIL-1").getContexts().size() == 1);
        assertTrue("testEmailAddressValid2 - 3",jsCard.getEmails().get("EMAIL-1").getContexts().get(Context.work()) == Boolean.TRUE);
        assertTrue("testEmailAddressValid2 - 4",jsCard.getEmails().get("EMAIL-1").getEmail().equals("jqpublic@xyz.example.com"));
        assertTrue("testEmailAddressValid2 - 5",jsCard.getEmails().get("EMAIL-2").getContexts().size() == 1);
        assertTrue("testEmailAddressValid2 - 6",jsCard.getEmails().get("EMAIL-2").getContexts().get(Context.private_()) == Boolean.TRUE);
        assertTrue("testEmailAddressValid2 - 7",jsCard.getEmails().get("EMAIL-2").getPref() == 1);
    }

}
