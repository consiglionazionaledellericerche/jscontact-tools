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

public class PhoneTest extends RoundtripTest {

    @Test
    public void testPhone1() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=home:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));    }

    @Test
    public void testPhone2() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=home:tel:+33-01-23-45-6\n" +
                "TEL;VALUE=uri;TYPE=voice,home;PREF=1:tel:+1-555-555-5555;ext=555\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testPhone3() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,fax:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone3 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testPhone4() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone4 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testPhone5() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=home,work:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone5 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testPhone6() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home,fax:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone6 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }


    @Test
    public void testPhone7() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone7 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testPhone8() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home,voice,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone8 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testPhone9() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=text;TYPE=home:+33 01 23 45 6\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testPhone9 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

}
