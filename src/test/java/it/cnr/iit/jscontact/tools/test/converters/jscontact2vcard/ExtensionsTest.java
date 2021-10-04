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
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard.JSContact2VCard;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ExtensionsTest extends JSContact2VCardTest {

    @Test
    public void testExtendedJSContactValid() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"extension:myext\":\"extvalue\"" +
                "}";
        JSContact2VCard jsContact2VCard = JSContact2VCard.builder()
                                .config(JSContact2VCardConfig.builder()
                                .extensionsPrefix("extension:").build()
                                ).build();
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testExtendedJSContactValid - 4",vcard.getExtendedProperties().size() == 1);
        assertTrue("testExtendedJSContactValid - 5",vcard.getExtendedProperties().get(0).getPropertyName().equals("myext"));
        assertTrue("testExtendedJSContactValid - 5",vcard.getExtendedProperties().get(0).getValue().equals("extvalue"));
    }


}
