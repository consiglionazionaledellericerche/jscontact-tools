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

public class PreferredContactLanguagesTest extends RoundtripTest {

    @Test
    public void testPreferredContactLanguages1() throws IOException, CardException {

        String jscard = "{" +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                         "\"fullName\":\"test\"," +
                         "\"preferredContactLanguages\":{" +
                               "\"jp\":[{\"@type\":\"ContactLanguage\",\"pref\":1}]," +
                               "\"en\":[{\"@type\":\"ContactLanguage\",\"pref\":2}]" +
                         "}" +
                         "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPreferredContactLanguages1 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testPreferredContactLanguages2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                "\"fullName\":\"test\"," +
                "\"preferredContactLanguages\":{" +
                    "\"en\":[{\"@type\":\"ContactLanguage\",\"context\":\"work\",\"pref\":1}]," +
                    "\"fr\":[{\"@type\":\"ContactLanguage\",\"context\":\"work\",\"pref\":2},{\"context\":\"private\"}]" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPreferredContactLanguages2 - 1", jscard2, Card.toCard(jscard));
    }

}
