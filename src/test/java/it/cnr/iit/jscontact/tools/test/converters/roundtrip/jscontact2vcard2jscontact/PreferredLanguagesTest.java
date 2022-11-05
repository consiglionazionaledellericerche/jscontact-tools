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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.jscontact2vcard2jscontact;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PreferredLanguagesTest extends RoundtripTest {

    @Test
    public void testPreferredLanguages1() throws IOException, CardException {

        String jscard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                         "\"fullName\":\"test\"," +
                         "\"preferredLanguages\":{" +
                               "\"jp\":[{\"@type\":\"LanguagePreference\",\"pref\":1}]," +
                               "\"en\":[{\"@type\":\"LanguagePreference\",\"pref\":2}]" +
                         "}" +
                         "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPreferredLanguages1 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testPreferredLanguages2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                "\"fullName\":\"test\"," +
                "\"preferredLanguages\":{" +
                    "\"en\":[{\"@type\":\"LanguagePreference\",\"contexts\": {\"work\": true }, \"pref\":1}]," +
                    "\"fr\":[{\"@type\":\"LanguagePreference\",\"contexts\": {\"work\": true }, \"pref\":2},{\"@type\":\"LanguagePreference\",\"contexts\": {\"private\": true }}]" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPreferredLanguages2 - 1", jscard2, Card.toCard(jscard));
    }

}
