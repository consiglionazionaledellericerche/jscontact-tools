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
import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RFC9554PropertiesTest extends JSContact2VCardTest {

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

    @Test
    public void testSpeakToAs5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"speakToAs\": {" +
                    "\"grammaticalGender\":\"neuter\"," +
                    "\"pronouns\": { " +
                        "\"k19\": { " +
                            "\"pref\":2, " +
                            "\"pronouns\":\"they/them\"" +
                        "}," +
                        "\"k32\": { " +
                            "\"pref\":1, " +
                            "\"pronouns\":\"xe/xir\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs5 - 1","NEUTER", vcard.getExtendedProperty("GRAMGENDER").getValue());
        assertEquals("testSpeakToAs5 - 2", "they/them", vcard.getExtendedProperties("PRONOUNS").get(0).getValue());
        assertEquals("testSpeakToAs5 - 3", "2", vcard.getExtendedProperties("PRONOUNS").get(0).getParameter(VCardParamEnum.PREF.getValue()));
        assertEquals("testSpeakToAs5 - 4", "k19", vcard.getExtendedProperties("PRONOUNS").get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testSpeakToAs5 - 5", "xe/xir", vcard.getExtendedProperties("PRONOUNS").get(1).getValue());
        assertEquals("testSpeakToAs5 - 6", "1", vcard.getExtendedProperties("PRONOUNS").get(1).getParameter(VCardParamEnum.PREF.getValue()));
        assertEquals("testSpeakToAs5 - 7", "k32", vcard.getExtendedProperties("PRONOUNS").get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }


    @Test
    public void testSpeakToAs6() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"language\":\"en\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"inanimate\"" +
                "}," +
                "\"localizations\":{" +
                    "\"it\":{" +
                        "\"speakToAs/grammaticalGender\":\"masculine\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs6 - 1","INANIMATE", vcard.getExtendedProperties("GRAMGENDER").get(0).getValue());
        assertEquals("testSpeakToAs6 - 2","MASCULINE", vcard.getExtendedProperties("GRAMGENDER").get(1).getValue());
        assertEquals("testSpeakToAs6 - 3","it", vcard.getExtendedProperties("GRAMGENDER").get(1).getParameter(VCardParamEnum.LANGUAGE.getValue()));
    }

    @Test
    public void testSpeakToAs7() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"language\":\"en\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"masculine\"," +
                    "\"pronouns\": { " +
                        "\"PRONOUNS-1\": { " +
                            "\"@type\":\"Pronouns\"," +
                            "\"pronouns\":\"he/him\"" +
                        "}" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"it\":{" +
                        "\"speakToAs/pronouns/PRONOUNS-1\":{ " +
                            "\"@type\":\"Pronouns\"," +
                            "\"pronouns\":\"egli/lui\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs7 - 1","MASCULINE", vcard.getExtendedProperty("GRAMGENDER").getValue());
        assertEquals("testSpeakToAs7 - 2", "he/him", vcard.getExtendedProperties("PRONOUNS").get(0).getValue());
        assertEquals("testSpeakToAs7 - 3", "egli/lui", vcard.getExtendedProperties("PRONOUNS").get(1).getValue());
        assertEquals("testSpeakToAs7 - 4", "it", vcard.getExtendedProperties("PRONOUNS").get(1).getParameter(VCardParamEnum.LANGUAGE.getValue()));
    }


    @Test
    public void testSpeakToAs8() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"language\":\"en\"," +
                "\"speakToAs\": {" +
                "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"inanimate\"," +
                    "\"pronouns\": { " +
                        "\"PRONOUNS-1\": { " +
                            "\"@type\":\"Pronouns\"," +
                            "\"pronouns\":\"he/him\"" +
                        "}" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                "\"it\":{" +
                    "\"speakToAs/grammaticalGender\":\"masculine\"," +
                    "\"speakToAs/pronouns/PRONOUNS-1\":{ " +
                        "\"@type\":\"Pronouns\"," +
                        "\"pronouns\":\"egli/lui\"" +
                    "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSpeakToAs8 - 1","INANIMATE", vcard.getExtendedProperties("GRAMGENDER").get(0).getValue());
        assertEquals("testSpeakToAs8 - 2", "he/him", vcard.getExtendedProperties("PRONOUNS").get(0).getValue());
        assertEquals("testSpeakToAs8 - 3", "egli/lui", vcard.getExtendedProperties("PRONOUNS").get(1).getValue());
        assertEquals("testSpeakToAs8 - 4", "it", vcard.getExtendedProperties("PRONOUNS").get(1).getParameter(VCardParamEnum.LANGUAGE.getValue()));
        assertEquals("testSpeakToAs8 - 5","MASCULINE", vcard.getExtendedProperties("GRAMGENDER").get(1).getValue());
        assertEquals("testSpeakToAs8 - 6","it", vcard.getExtendedProperties("GRAMGENDER").get(1).getParameter(VCardParamEnum.LANGUAGE.getValue()));
    }

    @Test
    public void testVCardProps() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"vCardProps\": [ " +
                    "[\"x-foo1\", {\"x-bar\":\"Hello\",\"group\":\"item1\"}, \"unknown\", \"World!\"], " +
                    "[\"x-foo2\", {\"pref\": 1}, \"integer\", 100 ] " +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testVCardProps - 1",2, vcard.getExtendedProperties().size());
        assertEquals("testVCardProps - 2","World!", vcard.getExtendedProperty("X-FOO1").getValue());
        assertEquals("testVCardProps - 3","Hello", vcard.getExtendedProperty("X-FOO1").getParameter("X-BAR"));
        assertEquals("testVCardProps - 4","item1", vcard.getExtendedProperty("X-FOO1").getGroup());
        assertEquals("testVCardProps - 5","100", vcard.getExtendedProperty("X-FOO2").getValue());
        assertEquals("testVCardProps - 6","1", vcard.getExtendedProperty("X-FOO2").getParameter("PREF"));
        assertEquals("testVCardProps - 7",VCardDataType.INTEGER, vcard.getExtendedProperty("X-FOO2").getDataType());

    }

}
