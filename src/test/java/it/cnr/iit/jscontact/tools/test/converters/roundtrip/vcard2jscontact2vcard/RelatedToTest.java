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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.vcard2jscontact2vcard;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RelatedToTest extends RoundtripTest {

    @Test
    public void testRelatedTo1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "RELATED:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testRelatedTo1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testRelatedTo2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "RELATED;TYPE=friend:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testRelatedTo2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

}
