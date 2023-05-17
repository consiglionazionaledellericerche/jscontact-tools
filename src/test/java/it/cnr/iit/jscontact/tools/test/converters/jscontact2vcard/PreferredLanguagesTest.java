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

public class PreferredLanguagesTest extends JSContact2VCardTest {

    @Test
    public void testPreferredLanguages1() throws IOException, CardException {

        String jsCard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                         "\"fullName\":\"test\"," +
                         "\"preferredLanguages\":{" +
                               "\"jp\":[{\"@type\":\"LanguagePref\",\"pref\":1}]," +
                               "\"en\":[{\"@type\":\"LanguagePref\",\"pref\":2}]" +
                         "}" +
                         "}";

        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testPreferredLanguages1 - 1", 2, vcard.getLanguages().size());
        assertEquals("testPreferredLanguages1 - 2", "jp", vcard.getLanguages().get(0).getValue());
        assertEquals("testPreferredLanguages1 - 3", 1, (int) vcard.getLanguages().get(0).getPref());
        assertEquals("testPreferredLanguages1 - 4", "en", vcard.getLanguages().get(1).getValue());
        assertEquals("testPreferredLanguages1 - 5", 2, (int) vcard.getLanguages().get(1).getPref());
    }

    @Test
    public void testPreferredLanguages2() throws IOException, CardException {

        String jsCard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                "\"fullName\":\"test\"," +
                "\"preferredLanguages\":{" +
                    "\"en\":[{\"@type\":\"LanguagePref\",\"contexts\": {\"work\": true }, \"pref\":1}]," +
                    "\"fr\":[{\"@type\":\"LanguagePref\",\"contexts\": {\"work\": true }, \"pref\":2},{\"@type\":\"LanguagePref\",\"contexts\": {\"private\": true }}]" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testPreferredLanguages2 - 1", 3, vcard.getLanguages().size());
        assertEquals("testPreferredLanguages2 - 2", "en", vcard.getLanguages().get(0).getValue());
        assertEquals("testPreferredLanguages2 - 3", 1, (int) vcard.getLanguages().get(0).getPref());
        assertEquals("testPreferredLanguages2 - 4", "work", vcard.getLanguages().get(0).getType());
        assertEquals("testPreferredLanguages2 - 5", "fr", vcard.getLanguages().get(1).getValue());
        assertEquals("testPreferredLanguages2 - 6", 2, (int) vcard.getLanguages().get(1).getPref());
        assertEquals("testPreferredLanguages2 - 7", "work", vcard.getLanguages().get(1).getType());
        assertEquals("testPreferredLanguages2 - 8", "fr", vcard.getLanguages().get(2).getValue());
    }

}
