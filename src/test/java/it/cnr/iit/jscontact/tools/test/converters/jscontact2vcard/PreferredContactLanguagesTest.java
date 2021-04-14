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

public class PreferredContactLanguagesTest extends JSContact2VCardTest {

    @Test
    public void testPreferredContactLanguagesValid1() throws IOException, CardException {

        String jsCard = "{" +
                         "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                         "\"fullName\":{\"value\":\"test\"}," +
                         "\"preferredContactLanguages\":{" +
                               "\"ja\":[{\"pref\":1}]," +
                               "\"en\":[{\"pref\":2}]" +
                         "}" +
                         "}";

        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testPreferredContactLanguagesValid1 - 1",vcard.getLanguages().size() == 2);
        assertTrue("testPreferredContactLanguagesValid1 - 2",vcard.getLanguages().get(0).getValue().equals("ja"));
        assertTrue("testPreferredContactLanguagesValid1 - 3",vcard.getLanguages().get(0).getPref() == 1);
        assertTrue("testPreferredContactLanguagesValid1 - 4",vcard.getLanguages().get(1).getValue().equals("en"));
        assertTrue("testPreferredContactLanguagesValid1 - 5",vcard.getLanguages().get(1).getPref() == 2);
    }

    @Test
    public void testPreferredContactLanguagesValid2() throws IOException, CardException {

        String jsCard = "{" +
                "\"uid\":\"139c2287-90ae-4f86-9a85-6e58a8f667d2\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"preferredContactLanguages\":{" +
                    "\"en\":[{\"context\":\"work\",\"pref\":1}]," +
                    "\"fr\":[{\"context\":\"work\",\"pref\":2},{\"context\":\"private\"}]" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testPreferredContactLanguagesValid2 - 1",vcard.getLanguages().size() == 3);
        assertTrue("testPreferredContactLanguagesValid2 - 2",vcard.getLanguages().get(0).getValue().equals("en"));
        assertTrue("testPreferredContactLanguagesValid2 - 3",vcard.getLanguages().get(0).getPref() == 1);
        assertTrue("testPreferredContactLanguagesValid2 - 4",vcard.getLanguages().get(0).getType().equals("work"));
        assertTrue("testPreferredContactLanguagesValid2 - 5",vcard.getLanguages().get(1).getValue().equals("fr"));
        assertTrue("testPreferredContactLanguagesValid2 - 6",vcard.getLanguages().get(1).getPref() == 2);
        assertTrue("testPreferredContactLanguagesValid2 - 7",vcard.getLanguages().get(1).getType().equals("work"));
        assertTrue("testPreferredContactLanguagesValid2 - 8",vcard.getLanguages().get(2).getValue().equals("fr"));
    }

}
