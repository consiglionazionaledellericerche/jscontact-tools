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

import static org.junit.Assert.*;

public class CategoriesTest extends RoundtripTest {

    @Test
    public void testCategories1() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CATEGORIES:INTERNET,IETF,INDUSTRY,INFORMATION TECHNOLOGY\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testCategories1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    //TODO - INDISCERNIBLE
    //@Test
    public void testCategories2() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CATEGORIES:INTERNET,IETF,INDUSTRY,INFORMATION TECHNOLOGY\n" +
                "CATEGORIES:TRAVEL AGENT\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testCategories2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

}
