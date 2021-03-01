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

public class FullNameTest extends JSContact2VCardTest {

    @Test
    public void testEmptyFullNameValid() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                "\"value\": \"\"" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testEmptyFullNameValid - 1",vcard.getFormattedName().getValue().isEmpty());

    }

    @Test
    public void testFullNameValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                "\"value\": \"John Q. Public, Esq.\"" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testFullNameValid1 - 1",vcard.getFormattedName().getValue().equals("John Q. Public, Esq."));
    }

    @Test
    public void testFullNameValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                   "\"value\": \"大久保 正仁\"," +
                   "\"language\": \"ja\"," +
                   "\"localizations\": { " +
                     "\"en\": \"Okubo Masahito\""+
                   "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testFullNameValid2 - 1",vcard.getFormattedNames().get(0).getValue().equals("大久保 正仁"));
        assertTrue("testFullNameValid2 - 2",vcard.getFormattedNames().get(0).getLanguage().equals("ja"));
        assertTrue("testFullNameValid2 - 3",vcard.getFormattedNames().get(0).getAltId().equals("1"));
        assertTrue("testFullNameValid2 - 4",vcard.getFormattedNames().get(1).getValue().equals("Okubo Masahito"));
        assertTrue("testFullNameValid2 - 5",vcard.getFormattedNames().get(1).getLanguage().equals("en"));
        assertTrue("testFullNameValid2 - 6",vcard.getFormattedNames().get(1).getAltId().equals("1"));
   }

}
