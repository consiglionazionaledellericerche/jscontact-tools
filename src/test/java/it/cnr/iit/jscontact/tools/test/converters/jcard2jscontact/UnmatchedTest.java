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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnmatchedTest extends JCard2JSContactTest {

    @Test
    public void testUnmatchedParameter() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"], " +
                "[\"n\", {\"sort-as\": \"Public,John\"}, \"text\", [\"Public\", \"John\", \"Quinlan\", \"Mr.\", \"Esq.\"]] " +
                "]]";

        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testUnmatchedParameter - 1", 1, jsCard.getName().getJCardParams().size());
        assertEquals("testUnmatchedParameter - 2", "Public,John", String.join(",",jsCard.getName().getJCardParams().get("sort-as").getValues()));

    }


}
