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

public class NameTest extends JSContact2VCardTest {

    @Test
    public void testName() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{" +
                    "\"value\": \"Mr. John Q. Public, Esq.\"" +
                "}," +
                "\"name\":[ " +
                    "{ \"value\":\"Mr.\", \"type\": \"prefix\" }," +
                    "{ \"value\":\"John\", \"type\": \"personal\" }," +
                    "{ \"value\":\"Public\", \"type\": \"surname\" }," +
                    "{ \"value\":\"Quinlan\", \"type\": \"additional\" }," +
                    "{ \"value\":\"Esq.\", \"type\": \"suffix\" }" +
                "], " +
                "\"nickNames\":[" +
                    "{" +
                        "\"value\": \"Johnny\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testName - 1",vcard.getFormattedName().getValue().equals("Mr. John Q. Public, Esq."));
        assertTrue("testName - 2",vcard.getStructuredName() != null);
        assertTrue("testName - 3",vcard.getStructuredName().getFamily().equals("Public"));
        assertTrue("testName - 4",vcard.getStructuredName().getGiven().equals("John"));
        assertTrue("testName - 5",vcard.getStructuredName().getAdditionalNames().size() == 1);
        assertTrue("testName - 6",vcard.getStructuredName().getAdditionalNames().get(0).equals("Quinlan"));
        assertTrue("testName - 7",vcard.getStructuredName().getPrefixes().size() == 1);
        assertTrue("testName - 8",vcard.getStructuredName().getPrefixes().get(0).equals("Mr."));
        assertTrue("testName - 9",vcard.getStructuredName().getSuffixes().size() == 1);
        assertTrue("testName - 10",vcard.getStructuredName().getSuffixes().get(0).equals("Esq."));
        assertTrue("testName - 11",vcard.getNickname().getValues().size() == 1);
        assertTrue("testName - 12",vcard.getNickname().getValues().get(0).equals("Johnny"));

    }
    

}
