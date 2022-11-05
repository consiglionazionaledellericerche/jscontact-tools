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
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExtensionsTest extends JSContact2VCardTest {

    @Test
    public void testExtendedJSContact1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"extension:myext1\":\"extvalue\"," +
                "\"extension:myext2\": { \"extprop\":\"extvalue\" }" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testExtendedJSContact1 - 1", 2, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact1 - 2", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact1 - 3", "extension:myext1", vcard.getExtendedProperties().get(0).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact1 - 4", "data:application/json;%22extvalue%22", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testExtendedJSContact1 - 2", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(1).getPropertyName());
        assertEquals("testExtendedJSContact1 - 3", "extension:myext2", vcard.getExtendedProperties().get(1).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact1 - 4", "data:application/json;base64,eyJleHRwcm9wIjoiZXh0dmFsdWUifQ==", vcard.getExtendedProperties().get(1).getValue());
    }

    @Test
    public void textExtendedJSContact2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"nickNames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"NickName\", \"name\": \"Johnny\", \"ext3\":\"text\"  }, " +
                    "\"NICK-2\" : {  \"@type\":\"NickName\", \"name\": \"Joe\" } " +
                "}," +
                "\"locale\":\"en\"," +
                "\"ext1\": 10," +
                "\"preferredLanguages\":{" +
                    "\"jp\":[{\"@type\":\"LanguagePreference\",\"pref\":1, \"ext6\": [\"1\",\"2\"]}]," +
                    "\"en\":[{\"@type\":\"LanguagePreference\",\"pref\":2}]" +
                "}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"@type\":\"StreetComponent\",\"type\":\"name\", \"value\":\"54321 Oak St\", \"ext4\": 5}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"ext2\": { \"prop\": 10 }," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        System.out.println(Ezvcard.write(vcard).go());
        assertEquals("testExtendedJSContact2 - 1", 6, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact2 - 2", "LOCALE", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact2 - 3", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(1).getPropertyName());
        assertEquals("testExtendedJSContact2 - 4", "addresses/ADR-1/street/0/ext4", vcard.getExtendedProperties().get(1).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact2 - 5", VCardDataType.URI, vcard.getExtendedProperties().get(1).getDataType());
        assertEquals("testExtendedJSContact2 - 6", "data:application/json;5", vcard.getExtendedProperties().get(1).getValue());
        assertEquals("testExtendedJSContact2 - 7", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(2).getPropertyName());
        assertEquals("testExtendedJSContact2 - 8", "nickNames/NICK-1/ext3", vcard.getExtendedProperties().get(2).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact2 - 9", VCardDataType.URI, vcard.getExtendedProperties().get(2).getDataType());
        assertEquals("testExtendedJSContact2 - 10", "data:application/json;%22text%22", vcard.getExtendedProperties().get(2).getValue());
        assertEquals("testExtendedJSContact2 - 11", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(3).getPropertyName());
        assertEquals("testExtendedJSContact2 - 12", "addresses/ADR-1/ext2", vcard.getExtendedProperties().get(3).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact2 - 13", VCardDataType.URI, vcard.getExtendedProperties().get(3).getDataType());
        assertEquals("testExtendedJSContact2 - 14", "data:application/json;base64,eyJwcm9wIjoxMH0=", vcard.getExtendedProperties().get(3).getValue());
        assertEquals("testExtendedJSContact2 - 15", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(5).getPropertyName());
        assertEquals("testExtendedJSContact2 - 16", "preferredLanguages/jp/0/ext6", vcard.getExtendedProperties().get(5).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact2 - 17", VCardDataType.URI, vcard.getExtendedProperties().get(5).getDataType());
        assertEquals("testExtendedJSContact2 - 18", "data:application/json;base64,WyIxIiwiMiJd", vcard.getExtendedProperties().get(5).getValue());
        assertEquals("testExtendedJSContact2 - 19", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(4).getPropertyName());
        assertEquals("testExtendedJSContact2 - 20", "ext1", vcard.getExtendedProperties().get(4).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact2 - 21", VCardDataType.URI, vcard.getExtendedProperties().get(4).getDataType());
        assertEquals("testExtendedJSContact2 - 22", "data:application/json;10", vcard.getExtendedProperties().get(4).getValue());

    }



}
