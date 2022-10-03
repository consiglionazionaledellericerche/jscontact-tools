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

import com.fasterxml.jackson.core.JsonProcessingException;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.serializers.PrettyPrintSerializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmailAddressTest extends RoundtripTest {

    @Test
    public void testEmailAddress1() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "EMAIL;TYPE=work:jqpublic@xyz.example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testEmailAddress1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testEmailAddress2() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "EMAIL;TYPE=work:jqpublic@xyz.example.com\n" +
                "EMAIL;TYPE=home;PREF=1:jane_doe@example.com\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testEmailAddress2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

}
