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
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RFCXXXXPropertiesTest extends JSContact2VCardTest {

    @Test
    public void testCreated() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"created\":\"2010-10-10T10:10:10Z\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testCreated - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testCreated - 2", "CREATED", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testCreated - 3", "20101010T101010Z", vcard.getExtendedProperties().get(0).getValue());
    }

    @Test
    public void testLanguage() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"language\":\"it\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testLanguage - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testLanguage - 2", "LANGUAGE", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testLanguage - 3", "it", vcard.getExtendedProperties().get(0).getValue());
    }

    @Test
    public void testSpeakToAs1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"masculine\"" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs1 - 1","MASCULINE", vcard.getExtendedProperty("GRAMGENDER").getValue());
    }

    @Test
    public void testSpeakToAs2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"inanimate\"" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs2 - 1","INANIMATE", vcard.getExtendedProperty("GRAMGENDER").getValue());
    }

    @Test
    public void testSpeakToAs3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                        "\"pronouns\": { " +
                            "\"PRONOUNS-1\": { " +
                                "\"@type\":\"Pronouns\"," +
                                "\"pronouns\":\"he/him\"" +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs3 - 1", "he/him", vcard.getExtendedProperty("PRONOUNS").getValue());
        assertNull("testSpeakToAs3 - 1", vcard.getExtendedProperty("GRAMGENDER"));
    }

    @Test
    public void testSpeakToAs4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                    "\"speakToAs\": {" +
                        "\"@type\":\"SpeakToAs\"," +
                        "\"grammaticalGender\":\"masculine\"," +
                        "\"pronouns\": { " +
                            "\"PRONOUNS-1\": { " +
                                "\"@type\":\"Pronouns\"," +
                                "\"pronouns\":\"he/him\"" +
                            "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs4 - 1","MASCULINE", vcard.getExtendedProperty("GRAMGENDER").getValue());
        assertEquals("testSpeakToAs4 - 2", "he/him", vcard.getExtendedProperty("PRONOUNS").getValue());
    }

}
