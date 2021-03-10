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

    /*
    @Test
    public void testUnmatchedParameter() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N;SORT-AS=\"Public,John\":Public;John;Quinlan;Mr.;Esq.\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testUnmatchedParameter - 1",jsCard.getExtensions().size() == 1);
        assertTrue("testUnmatchedParameter - 2",jsCard.getExtensions().get("ietf.org/rfc6350/n/sort-as").equals("Public,John"));

    }
*/
}
