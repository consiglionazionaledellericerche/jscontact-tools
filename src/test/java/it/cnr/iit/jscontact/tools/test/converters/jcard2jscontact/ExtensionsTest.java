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

public class ExtensionsTest extends JCard2JSContactTest {

    @Test
    public void testExtendedJSContact1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"x-rfc0000-jsprop\",{\"x-rfc0000-jspath\":\"extension:myext1\"},\"uri\",\"data:application/json;%22extvalue%22\"]," +
                "[\"x-rfc0000-jsprop\",{\"x-rfc0000-jspath\":\"extension:myext2\"},\"uri\",\"data:application/json;base64,eyJleHRwcm9wIjoiZXh0dmFsdWUifQ==\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testExtendedJSContact1 - 1", 2, jsCard.getExtensions().size());
        assertEquals("testExtendedJSContact1 - 2", "extvalue", jsCard.getExtensions().get("extension:myext1"));
        assertEquals("testExtendedJSContact1 - 3", "{extprop=extvalue}", jsCard.getExtensions().get("extension:myext2").toString());
    }

    @Test
    public void textExtendedJSContact2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"nickname\",{\"language\":\"en\"},\"text\",\"Johnny\"]," +
                "[\"nickname\",{\"language\":\"en\"},\"text\",\"Joe\"]," +
                "[\"adr\",{\"cc\":\"US\",\"language\":\"en\"},\"text\",[\"\",\"\",\"54321 Oak St\",\"Reston\",\"VA\",\"20190\",\"USA\"]],"+
                "[\"lang\",{\"pref\":\"1\"},\"language-tag\",\"jp\"],"+
                "[\"lang\",{\"pref\":\"2\"},\"language-tag\",\"en\"],"+
                "[\"locale\",{},\"language-tag\",\"en\"],"+
                "[\"x-rfc0000-jsprop\",{\"x-rfc0000-jspath\":\"addresses/ADR-1/street/0/ext4\"},\"uri\",\"data:application/json;true\"],"+
                "[\"x-rfc0000-jsprop\",{\"x-rfc0000-jspath\":\"nickNames/NICK-1/ext3\"},\"uri\",\"data:application/json;%22text%22\"],"+
                "[\"x-rfc0000-jsprop\",{\"x-rfc0000-jspath\":\"addresses/ADR-1/ext2\"},\"uri\",\"data:application/json;base64,eyJwcm9wIjoxMH0=\"],"+
                "[\"x-rfc0000-jsprop\",{\"x-rfc0000-jspath\":\"preferredContactLanguages/jp/0/ext6\"},\"uri\",\"data:application/json;base64,WyIxIiwiMiJd\"],"+
                "[\"x-rfc0000-jsprop\",{\"x-rfc0000-jspath\":\"ext1\"},\"uri\",\"data:application/json;10\"]"+
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
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