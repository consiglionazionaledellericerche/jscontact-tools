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

import it.cnr.iit.jscontact.tools.dto.ResourceContext;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class EmailResourceTest extends JCard2JSContactTest {

    @Test
    public void testEmailResourceValid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"email\", {\"type\": \"work\"}, \"text\", \"jqpublic@xyz.example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testEmailResourceValid1 - 1",jsCard.getEmails().length == 1);
        assertTrue("testEmailResourceValid1 - 2",jsCard.getEmails()[0].getType() == null);
        assertTrue("testEmailResourceValid1 - 3",jsCard.getEmails()[0].getValue().equals("jqpublic@xyz.example.com"));
        assertTrue("testEmailResourceValid1 - 4",jsCard.getEmails()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));

    }

    @Test
    public void testEmailResourceValid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"email\", {\"type\": \"work\"}, \"text\", \"jqpublic@xyz.example.com\"], " +
                "[\"email\", {\"type\": \"home\", \"pref\": 1}, \"text\", \"jane_doe@example.com\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testEmailResourceValid2 - 1",jsCard.getEmails().length == 2);
        assertTrue("testEmailResourceValid2 - 2",jsCard.getEmails()[0].getType() == null);
        assertTrue("testEmailResourceValid2 - 3",jsCard.getEmails()[0].getValue().equals("jqpublic@xyz.example.com"));
        assertTrue("testEmailResourceValid2 - 4",jsCard.getEmails()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testEmailResourceValid2 - 5",jsCard.getEmails()[1].getType() == null);
        assertTrue("testEmailResourceValid2 - 6",jsCard.getEmails()[1].getValue().equals("jane_doe@example.com"));
        assertTrue("testEmailResourceValid2 - 7",jsCard.getEmails()[1].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));
        assertTrue("testEmailResourceValid2 - 8",jsCard.getEmails()[1].getPref() == 1);
    }

}
