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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtensionsTest extends VCard2JSContactTest {

    @Test
    public void testExtendedJSContact1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "X-RFC0000-JSPROP;X-RFC0000-JSPATH=\"extension:myext1\";VALUE=uri:data:application/json;%22extvalue%22\n" +
                "X-RFC0000-JSPROP;X-RFC0000-JSPATH=\"extension:myext2\";VALUE=uri:data:application/json;base64,eyJleHRwcm9wIjoiZXh0dmFsdWUifQ==\n" +
                "END:VCARD";
        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testExtendedJSContact1 - 1", 2, jsCard.getExtensions().size());
        assertEquals("testExtendedJSContact1 - 2", "extvalue", jsCard.getExtensions().get("extension:myext1"));
        assertEquals("testExtendedJSContact1 - 3", "{extprop=extvalue}", jsCard.getExtensions().get("extension:myext2").toString());
    }

    @Test
    public void textExtendedJSContact2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "NICKNAME;LANGUAGE=en:Johnny\n" +
                "NICKNAME;LANGUAGE=en:Joe\n" +
                "ADR;CC=US;LANGUAGE=en:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "LANG;PREF=1:jp\n" +
                "LANG;PREF=2:en\n" +
                "LOCALE;VALUE=language-tag:en\n" +
                "X-RFC0000-JSPROP;X-RFC0000-JSPATH=addresses/ADR-1/street/0/ext4;VALUE=uri:data:application/json;true\n" +
                "X-RFC0000-JSPROP;X-RFC0000-JSPATH=nickNames/NICK-1/ext3;VALUE=uri:data:application/json;%22text%22\n" +
                "X-RFC0000-JSPROP;X-RFC0000-JSPATH=addresses/ADR-1/ext2;VALUE=uri:data:application/json;base64,eyJwcm9wIjoxMH0=\n" +
                "X-RFC0000-JSPROP;X-RFC0000-JSPATH=preferredContactLanguages/jp/0/ext6;VALUE=uri:data:application/json;base64,WyIxIiwiMiJd\n" +
                "X-RFC0000-JSPROP;X-RFC0000-JSPATH=ext1;VALUE=uri:data:application/json;10\n" +
                "END:VCARD";
        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testExtendedJSContact1 - 1", 1, jsCard.getExtensions().size());
        assertEquals("testExtendedJSContact1 - 2", 10, jsCard.getExtensions().get("ext1"));
        assertEquals("testExtendedJSContact1 - 3", 1, jsCard.getNickNames().get("NICK-1").getExtensions().size());
        assertEquals("testExtendedJSContact1 - 4", "text", jsCard.getNickNames().get("NICK-1").getExtensions().get("ext3"));
        assertEquals("testExtendedJSContact1 - 5", 1, jsCard.getPreferredContactLanguages().get("jp")[0].getExtensions().size());
        assertEquals("testExtendedJSContact1 - 6", "[1, 2]", jsCard.getPreferredContactLanguages().get("jp")[0].getExtensions().get("ext6").toString());
        assertEquals("testExtendedJSContact1 - 7", 1, jsCard.getAddresses().get("ADR-1").getExtensions().size());
        assertEquals("testExtendedJSContact1 - 8", "{prop=10}", jsCard.getAddresses().get("ADR-1").getExtensions().get("ext2").toString());
        assertEquals("testExtendedJSContact1 - 9", 1, jsCard.getAddresses().get("ADR-1").getStreet()[0].getExtensions().size());
        assertEquals("testExtendedJSContact1 - 10", true, jsCard.getAddresses().get("ADR-1").getStreet()[0].getExtensions().get("ext4"));

    }



}
