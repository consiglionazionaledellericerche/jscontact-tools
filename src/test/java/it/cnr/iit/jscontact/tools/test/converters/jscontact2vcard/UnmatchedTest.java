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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class UnmatchedTest extends JSContact2VCardTest {


    @Test
    public void testPreferredContactMethod() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"preferredContactMethod\":\"emails\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPreferredContactMethod - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testPreferredContactMethod - 2",vcard.getExtendedProperties().get(0).getPropertyName().equals("X-JSCONTACT-PREFERREDCONTACTMETHOD"));
        assertTrue("testPreferredContactMethod - 3",vcard.getExtendedProperties().get(0).getValue().equals("emails"));
    }

    @Test
    public void testCreated() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"created\":\"2010-10-10T10:10:10Z\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testCreated - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testCreated - 2",vcard.getExtendedProperties().get(0).getPropertyName().equals("X-JSCONTACT-CREATED"));
        assertTrue("testCreated - 3",vcard.getExtendedProperties().get(0).getValue().equals("20101010T101010Z"));
    }

    @Test
    public void testUnmatchedProperty() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"ietf.org:rfc6350:XML\":\"<note>This is a not in xml</note>\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        System.out.println(Ezvcard.write(vcard).go());
        assertTrue("testUnmatchedProperty - 1",vcard.getXmls().size() == 1);
    }

    @Test
    public void testUnmatchedParameter1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\": \"Mr. John Q. Public, Esq.\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"type\": \"prefix\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"type\": \"personal\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"type\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"type\": \"additional\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"type\": \"suffix\" }" +
                    "] " +
                "}, " +
                "\"nickNames\":[ \"Johnny\" ]," +
                "\"ietf.org:rfc6350:N:SORT-AS\":\"Public,John:Public;John;Quinlan;Mr.;Esq.\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testUnmatchedParameter1 - 1",vcard.getFormattedName().getValue().equals("Mr. John Q. Public, Esq."));
        assertTrue("testUnmatchedParameter1 - 2",vcard.getStructuredName() != null);
        assertTrue("testUnmatchedParameter1 - 3",vcard.getStructuredName().getFamily().equals("Public"));
        assertTrue("testUnmatchedParameter1 - 4",vcard.getStructuredName().getGiven().equals("John"));
        assertTrue("testUnmatchedParameter1 - 5",vcard.getStructuredName().getAdditionalNames().size() == 1);
        assertTrue("testUnmatchedParameter1 - 6",vcard.getStructuredName().getAdditionalNames().get(0).equals("Quinlan"));
        assertTrue("testUnmatchedParameter1 - 7",vcard.getStructuredName().getPrefixes().size() == 1);
        assertTrue("testUnmatchedParameter1 - 8",vcard.getStructuredName().getPrefixes().get(0).equals("Mr."));
        assertTrue("testUnmatchedParameter1 - 9",vcard.getStructuredName().getSuffixes().size() == 1);
        assertTrue("testUnmatchedParameter1 - 10",vcard.getStructuredName().getSuffixes().get(0).equals("Esq."));
        assertTrue("testUnmatchedParameter1 - 11",vcard.getNickname().getValues().size() == 1);
        assertTrue("testUnmatchedParameter1 - 12",vcard.getNickname().getValues().get(0).equals("Johnny"));
        assertTrue("testUnmatchedParameter1 - 13",vcard.getStructuredName().getSortAs().size()==1);
        assertTrue("testUnmatchedParameter1 - 13",vcard.getStructuredName().getSortAs().get(0).equals("Public,John:Public;John;Quinlan;Mr.;Esq."));

    }

    @Test
    public void testUnmatchedParameter2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\": \"test\"," +
                "\"ietf.org:rfc6350:FN:GROUP\":\"contact\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testUnmatchedParameter2 - 1",vcard.getFormattedName().getValue().equals("test"));
        assertTrue("testUnmatchedParameter2 - 2",vcard.getFormattedName().getGroup().equals("contact"));

    }

}
