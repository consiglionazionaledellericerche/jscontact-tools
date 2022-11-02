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

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExtensionsTest extends JSContact2VCardTest {

    @Test
    public void testExtendedJSContact() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"extension:myext1\":\"extvalue\"," +
                "\"extension:myext2\": { \"extprop\":\"extvalue\" }" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testExtendedJSContact - 1", 2, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact - 2", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact - 3", "extension:myext1", vcard.getExtendedProperties().get(0).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact - 4", "data:application/json;%22extvalue%22", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testExtendedJSContact - 2", "X-RFC0000-JSPROP", vcard.getExtendedProperties().get(1).getPropertyName());
        assertEquals("testExtendedJSContact - 3", "extension:myext2", vcard.getExtendedProperties().get(1).getParameter("X-RFC0000-JSPATH"));
        assertEquals("testExtendedJSContact - 4", "data:application/json;base64,eyJleHRwcm9wIjoiZXh0dmFsdWUifQ==", vcard.getExtendedProperties().get(1).getValue());
    }


}
