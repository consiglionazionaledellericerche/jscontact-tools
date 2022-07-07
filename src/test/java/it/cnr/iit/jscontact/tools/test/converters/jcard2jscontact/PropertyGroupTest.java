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
import it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact.VCard2JSContactTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PropertyGroupTest extends JCard2JSContactTest {
    

    @Test
    public void testPropertyGroup1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"prodid\",{},\"text\",\"ez-vcard 0.11.3\"], " +
                "[\"uid\",{},\"uri\",\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"], " +
                "[\"fn\",{},\"text\",\"test\"], " +
                "[\"adr\",{\"cc\":\"US\",\"label\":\"54321 Oak St Reston VA 20190 USA\",\"group\":\"CONTACT\"},\"text\",[\"\",\"\",\"54321 Oak St\",\"Reston\",\"VA\",\"20190\",\"USA\"]]" +
                "]]";

        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPropertyGroup1 - 1",jsCard.getPropertyGroups().size() == 1);
        assertTrue("testPropertyGroup1 - 2",jsCard.getPropertyGroups().get("CONTACT").getMembers().get("addresses/ADR-1") != null);

    }

}
