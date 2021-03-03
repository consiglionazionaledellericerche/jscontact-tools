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

import static org.junit.Assert.assertTrue;

public class CategoriesTest extends JSContact2VCardTest {

    @Test
    public void testCategories() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"categories\":{" +
                    "\"INTERNET\": true," +
                    "\"IETF\" : true, " +
                    "\"INDUSTRY\" : true, " +
                    "\"INFORMATION TECHNOLOGY\" : true " +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testCategories - 1",vcard.getCategories().getValues().size() == 4);
        assertTrue("testCategories - 2",vcard.getCategories().getValues().get(0).equals("INTERNET"));
        assertTrue("testCategories - 3",vcard.getCategories().getValues().get(1).equals("IETF"));
        assertTrue("testCategories - 4",vcard.getCategories().getValues().get(2).equals("INDUSTRY"));
        assertTrue("testCategories - 5",vcard.getCategories().getValues().get(3).equals("INFORMATION TECHNOLOGY"));
    }


}
