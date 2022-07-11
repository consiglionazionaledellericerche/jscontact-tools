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

import static org.junit.Assert.*;

public class EmailAddressTest extends JCard2JSContactTest {

    @Test
    public void testEmailAddressValid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"email\", {\"type\": \"work\"}, \"text\", \"jqpublic@xyz.example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testEmailAddressValid1 - 1", 1, jsCard.getEmails().size());
        assertEquals("testEmailAddressValid1 - 2", 1, jsCard.getEmails().get("EMAIL-1").getContexts().size());
        assertSame("testEmailAddressValid1 - 3", jsCard.getEmails().get("EMAIL-1").getContexts().get(Context.work()), Boolean.TRUE);
        assertEquals("testEmailAddressValid1 - 4", "jqpublic@xyz.example.com", jsCard.getEmails().get("EMAIL-1").getEmail());

    }

    @Test
    public void testEmailAddressValid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"email\", {\"type\": \"work\"}, \"text\", \"jqpublic@xyz.example.com\"], " +
                "[\"email\", {\"type\": \"home\", \"pref\": 1}, \"text\", \"jane_doe@example.com\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testEmailAddressValid2 - 1", 2, jsCard.getEmails().size());
        assertEquals("testEmailAddressValid2 - 2", 1, jsCard.getEmails().get("EMAIL-1").getContexts().size());
        assertSame("testEmailAddressValid2 - 3", jsCard.getEmails().get("EMAIL-1").getContexts().get(Context.work()), Boolean.TRUE);
        assertEquals("testEmailAddressValid2 - 4", "jqpublic@xyz.example.com", jsCard.getEmails().get("EMAIL-1").getEmail());
        assertEquals("testEmailAddressValid2 - 5", 1, jsCard.getEmails().get("EMAIL-2").getContexts().size());
        assertSame("testEmailAddressValid2 - 6", jsCard.getEmails().get("EMAIL-2").getContexts().get(Context.private_()), Boolean.TRUE);
        assertEquals("testEmailAddressValid2 - 7", 1, (int) jsCard.getEmails().get("EMAIL-2").getPref());
    }

}
