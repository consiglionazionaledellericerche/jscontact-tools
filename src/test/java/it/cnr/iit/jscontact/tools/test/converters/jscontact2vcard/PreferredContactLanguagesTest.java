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

public class PreferredContactLanguagesTest extends JSContact2VCardTest {

    @Test
    public void testPreferredContactLanguagesValid1() throws IOException, CardException {

        String jsCard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                         "\"fullName\":\"test\"," +
                         "\"preferredContactLanguages\":{" +
                               "\"jp\":[{\"@type\":\"ContactLanguage\",\"pref\":1}]," +
                               "\"en\":[{\"@type\":\"ContactLanguage\",\"pref\":2}]" +
                         "}" +
                         "}";

        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testPreferredContactLanguagesValid1 - 1", 2, vcard.getLanguages().size());
        assertEquals("testPreferredContactLanguagesValid1 - 2", "jp", vcard.getLanguages().get(0).getValue());
        assertEquals("testPreferredContactLanguagesValid1 - 3", 1, (int) vcard.getLanguages().get(0).getPref());
        assertEquals("testPreferredContactLanguagesValid1 - 4", "en", vcard.getLanguages().get(1).getValue());
        assertEquals("testPreferredContactLanguagesValid1 - 5", 2, (int) vcard.getLanguages().get(1).getPref());
    }

    @Test
    public void testPreferredContactLanguagesValid2() throws IOException, CardException {

        String jsCard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                "\"fullName\":\"test\"," +
                "\"preferredContactLanguages\":{" +
                    "\"en\":[{\"@type\":\"ContactLanguage\",\"context\":\"work\",\"pref\":1}]," +
                    "\"fr\":[{\"@type\":\"ContactLanguage\",\"context\":\"work\",\"pref\":2},{\"context\":\"private\"}]" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testPreferredContactLanguagesValid2 - 1", 3, vcard.getLanguages().size());
        assertEquals("testPreferredContactLanguagesValid2 - 2", "en", vcard.getLanguages().get(0).getValue());
        assertEquals("testPreferredContactLanguagesValid2 - 3", 1, (int) vcard.getLanguages().get(0).getPref());
        assertEquals("testPreferredContactLanguagesValid2 - 4", "work", vcard.getLanguages().get(0).getType());
        assertEquals("testPreferredContactLanguagesValid2 - 5", "fr", vcard.getLanguages().get(1).getValue());
        assertEquals("testPreferredContactLanguagesValid2 - 6", 2, (int) vcard.getLanguages().get(1).getPref());
        assertEquals("testPreferredContactLanguagesValid2 - 7", "work", vcard.getLanguages().get(1).getType());
        assertEquals("testPreferredContactLanguagesValid2 - 8", "fr", vcard.getLanguages().get(2).getValue());
    }

}
