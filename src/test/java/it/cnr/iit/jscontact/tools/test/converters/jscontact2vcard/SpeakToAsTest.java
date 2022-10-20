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

public class SpeakToAsTest extends JSContact2VCardTest {

    @Test
    public void testSpeakToAs1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"male\"" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testSpeakToAs1 - 1",vcard.getGender().isMale());
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
        assertTrue("testSpeakToAs2 - 1",vcard.getGender().isNone());
        assertEquals("testSpeakToAs2 - 2", "inanimate", vcard.getGender().getText());
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
        assertNull("testSpeakToAs3 - 1", vcard.getGender());
        assertEquals("testSpeakToAs3 - 2", "he/him", vcard.getExtendedProperty("PRONOUNS").getValue());
    }

    @Test
    public void testSpeakToAs4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"inanimate\"," +
                    "\"pronouns\": { " +
                        "\"PRONOUNS-1\": { " +
                            "\"@type\":\"Pronouns\"," +
                            "\"pronouns\":\"he/him\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testSpeakToAs4 - 1",vcard.getGender().isNone());
        assertEquals("testSpeakToAs3 - 2", "inanimate", vcard.getGender().getText());
        assertEquals("testSpeakToAs4 - 3", "he/him", vcard.getExtendedProperty("PRONOUNS").getValue());
    }

}
