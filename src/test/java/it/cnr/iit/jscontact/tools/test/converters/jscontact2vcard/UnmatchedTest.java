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

import static org.junit.Assert.*;

public class UnmatchedTest extends JSContact2VCardTest {

    //@Test
    public void testPreferredContactChannels() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"preferredContactMethod\":\"emails\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPreferredContactMethod - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testPreferredContactMethod - 2", "X-JSCONTACT-PREFERREDCONTACTMETHOD", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testPreferredContactMethod - 3", "emails", vcard.getExtendedProperties().get(0).getValue());
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
        assertEquals("testCreated - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testCreated - 2", "CREATED", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testCreated - 3", "20101010T101010Z", vcard.getExtendedProperties().get(0).getValue());
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
        assertEquals("testUnmatchedProperty - 1", 1, vcard.getXmls().size());
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
                "\"nickNames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"NickName\", \"name\": \"Johnny\" } " +
                "}," +
                "\"ietf.org:rfc6350:N:SORT-AS\":\"Public,John:Public;John;Quinlan;Mr.;Esq.\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testUnmatchedParameter1 - 1", "Mr. John Q. Public, Esq.", vcard.getFormattedName().getValue());
        assertNotNull("testUnmatchedParameter1 - 2", vcard.getStructuredName());
        assertEquals("testUnmatchedParameter1 - 3", "Public", vcard.getStructuredName().getFamily());
        assertEquals("testUnmatchedParameter1 - 4", "John", vcard.getStructuredName().getGiven());
        assertEquals("testUnmatchedParameter1 - 5", 1, vcard.getStructuredName().getAdditionalNames().size());
        assertEquals("testUnmatchedParameter1 - 6", "Quinlan", vcard.getStructuredName().getAdditionalNames().get(0));
        assertEquals("testUnmatchedParameter1 - 7", 1, vcard.getStructuredName().getPrefixes().size());
        assertEquals("testUnmatchedParameter1 - 8", "Mr.", vcard.getStructuredName().getPrefixes().get(0));
        assertEquals("testUnmatchedParameter1 - 9", 1, vcard.getStructuredName().getSuffixes().size());
        assertEquals("testUnmatchedParameter1 - 10", "Esq.", vcard.getStructuredName().getSuffixes().get(0));
        assertEquals("testUnmatchedParameter1 - 11", 1, vcard.getNickname().getValues().size());
        assertEquals("testUnmatchedParameter1 - 12", "Johnny", vcard.getNickname().getValues().get(0));
        assertEquals("testUnmatchedParameter1 - 13", 1, vcard.getStructuredName().getSortAs().size());
        assertEquals("testUnmatchedParameter1 - 13", "Public,John:Public;John;Quinlan;Mr.;Esq.", vcard.getStructuredName().getSortAs().get(0));

    }

}
