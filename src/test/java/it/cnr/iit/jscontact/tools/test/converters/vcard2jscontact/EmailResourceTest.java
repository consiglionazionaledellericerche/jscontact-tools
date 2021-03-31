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

import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class EmailResourceTest extends VCard2JSContactTest {

    @Test
    public void testEmailResourceValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "EMAIL;TYPE=work:jqpublic@xyz.example.com\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testEmailResourceValid1 - 1",jsCard.getEmails().length == 1);
        assertTrue("testEmailResourceValid1 - 2",jsCard.getEmails()[0].getType() == null);
        assertTrue("testEmailResourceValid1 - 3",jsCard.getEmails()[0].getValue().equals("jqpublic@xyz.example.com"));
        assertTrue("testEmailResourceValid1 - 4",jsCard.getEmails()[0].getContext().getValue().equals(Context.WORK.getValue()));

    }

    @Test
    public void testEmailResourceValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "EMAIL;TYPE=work:jqpublic@xyz.example.com\n" +
                "EMAIL;TYPE=home;PREF=1:jane_doe@example.com\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testEmailResourceValid2 - 1",jsCard.getEmails().length == 2);
        assertTrue("testEmailResourceValid2 - 2",jsCard.getEmails()[0].getType() == null);
        assertTrue("testEmailResourceValid2 - 3",jsCard.getEmails()[0].getValue().equals("jqpublic@xyz.example.com"));
        assertTrue("testEmailResourceValid2 - 4",jsCard.getEmails()[0].getContext().getValue().equals(Context.WORK.getValue()));
        assertTrue("testEmailResourceValid2 - 5",jsCard.getEmails()[1].getType() == null);
        assertTrue("testEmailResourceValid2 - 6",jsCard.getEmails()[1].getValue().equals("jane_doe@example.com"));
        assertTrue("testEmailResourceValid2 - 7",jsCard.getEmails()[1].getContext().getValue().equals(Context.PRIVATE.getValue()));
        assertTrue("testEmailResourceValid2 - 8",jsCard.getEmails()[1].getPref() == 1);
    }

}
