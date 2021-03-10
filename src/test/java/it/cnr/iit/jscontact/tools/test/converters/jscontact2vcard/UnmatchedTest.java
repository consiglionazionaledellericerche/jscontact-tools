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
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact.VCard2JSContactTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class UnmatchedTest extends JSContact2VCardTest {


    @Test
    public void testPreferredContactMethod() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"preferredContactMethod\":\"emails\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPreferredContactMethod - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testPreferredContactMethod - 2",vcard.getExtendedProperties().get(0).getPropertyName().equals("X-PREFERREDCONTACTMETHOD"));
        assertTrue("testPreferredContactMethod - 3",vcard.getExtendedProperties().get(0).getValue().equals("emails"));
    }

    @Test
    public void testUnmatchedProperty() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"ietf.org/rfc6350/GENDER\":\"M\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testUnmatchedProperty - 1",vcard.getGender().getGender().equals("M"));
    }

    @Test
    public void testUnmatchedParameter() throws IOException, CardException {

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
                    "{ \"value\":\"Esq.\", \"type\": \"suffix\" }," +
                    "{ \"value\":\"Johnny\", \"type\": \"nickname\" }" +
                "]," +
                "\"ietf.org/rfc6350/N/SORT-AS\":\"Public,John:Public;John;Quinlan;Mr.;Esq.\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testUnmatchedParameter - 1",vcard.getFormattedName().getValue().equals("Mr. John Q. Public, Esq."));
        assertTrue("testUnmatchedParameter - 2",vcard.getStructuredName() != null);
        assertTrue("testUnmatchedParameter - 3",vcard.getStructuredName().getFamily().equals("Public"));
        assertTrue("testUnmatchedParameter - 4",vcard.getStructuredName().getGiven().equals("John"));
        assertTrue("testUnmatchedParameter - 5",vcard.getStructuredName().getAdditionalNames().size() == 1);
        assertTrue("testUnmatchedParameter - 6",vcard.getStructuredName().getAdditionalNames().get(0).equals("Quinlan"));
        assertTrue("testUnmatchedParameter - 7",vcard.getStructuredName().getPrefixes().size() == 1);
        assertTrue("testUnmatchedParameter - 8",vcard.getStructuredName().getPrefixes().get(0).equals("Mr."));
        assertTrue("testUnmatchedParameter - 9",vcard.getStructuredName().getSuffixes().size() == 1);
        assertTrue("testUnmatchedParameter - 10",vcard.getStructuredName().getSuffixes().get(0).equals("Esq."));
        assertTrue("testUnmatchedParameter - 11",vcard.getNickname().getValues().size() == 1);
        assertTrue("testUnmatchedParameter - 12",vcard.getNickname().getValues().get(0).equals("Johnny"));
        assertTrue("testUnmatchedParameter - 13",vcard.getStructuredName().getSortAs().size()==1);
        assertTrue("testUnmatchedParameter - 13",vcard.getStructuredName().getSortAs().get(0).equals("Public,John:Public;John;Quinlan;Mr.;Esq."));

    }
}
