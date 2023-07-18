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

public class FullNameTest extends JSContact2VCardTest {


    @Test
    public void testMissingFullName() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testMissingFullName - 1", vcard.getFormattedName().getValue(), vcard.getUid().getValue());

    }

    @Test
    public void testEmptyFullName() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"\"}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testEmptyFullName - 1", vcard.getFormattedName().getValue(), vcard.getUid().getValue());

    }

    @Test
    public void testEmptyFullNameWithValidName() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"kind\": \"title\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"kind\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"kind\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"kind\": \"given2\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"kind\": \"credential\" }" +
                    "] " +
                "} " +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testEmptyFullNameWithValidName - 1", "Mr. John Public Quinlan Esq.", vcard.getFormattedName().getValue());
    }


    @Test
    public void testFullName1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\":\"John Q. Public, Esq.\"}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testFullName1 - 1", "John Q. Public, Esq.", vcard.getFormattedName().getValue());
    }

    @Test
    public void testFullName2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"language\": \"jp\"," +
                "\"name\": { \"full\":\"大久保 正仁\"}," +
                "\"localizations\" : {" +
                  "\"en\": {" +
                     "\"name\": { \"full\":\"Okubo Masahito\"}"+
                  "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testFullName2 - 1", "大久保 正仁", vcard.getFormattedNames().get(0).getValue());
        assertEquals("testFullName2 - 2", "jp", vcard.getFormattedNames().get(0).getLanguage());
        assertEquals("testFullName2 - 3", "1", vcard.getFormattedNames().get(0).getAltId());
        assertEquals("testFullName2 - 4", "Okubo Masahito", vcard.getFormattedNames().get(1).getValue());
        assertEquals("testFullName2 - 5", "en", vcard.getFormattedNames().get(1).getLanguage());
        assertEquals("testFullName2 - 6", "1", vcard.getFormattedNames().get(1).getAltId());
   }


    @Test
    public void testFullName3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"language\": \"jp\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"value\":\"正仁\", \"kind\": \"given\" }," +
                        "{ \"value\":\"大久保\", \"kind\": \"surname\" }" +
                    "] " +
                "}, " +
                "\"localizations\" : {" +
                    "\"en\": {" +
                        "\"name/components\":[ " +
                        "{ \"value\":\"Masahito\", \"kind\": \"given\" }," +
                        "{ \"value\":\"Okubo\", \"kind\": \"surname\" }" +
                        "]" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testFullName2 - 1", "正仁 大久保", vcard.getFormattedNames().get(0).getValue());
        assertEquals("testFullName2 - 2", "jp", vcard.getFormattedNames().get(0).getLanguage());
        assertEquals("testFullName2 - 3", "1", vcard.getFormattedNames().get(0).getAltId());
        assertEquals("testFullName2 - 4", "Masahito Okubo", vcard.getFormattedNames().get(1).getValue());
        assertEquals("testFullName2 - 5", "en", vcard.getFormattedNames().get(1).getLanguage());
        assertEquals("testFullName2 - 6", "1", vcard.getFormattedNames().get(1).getAltId());
    }
}
