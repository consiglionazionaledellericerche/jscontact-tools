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
import ezvcard.util.TelUri;
import it.cnr.iit.jscontact.tools.dto.VCardPropEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedStructuredName;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ExtensionsTest extends JSContact2VCardTest {

    @Test
    public void testExtendedJSContact1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"extension:myext1\":\"extvalue\"," +
                "\"extension:myext2\": { \"extprop\":\"extvalue\" }" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testExtendedJSContact1 - 1", 2, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact1 - 2", "JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact1 - 3", "extension:myext1", vcard.getExtendedProperties().get(0).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact1 - 4", "\"extvalue\"", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testExtendedJSContact1 - 2", "JSPROP", vcard.getExtendedProperties().get(1).getPropertyName());
        assertEquals("testExtendedJSContact1 - 3", "extension:myext2", vcard.getExtendedProperties().get(1).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact1 - 4", "{\"extprop\":\"extvalue\"}", vcard.getExtendedProperties().get(1).getValue());
    }

    @Test
    public void textExtendedJSContact2() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"nicknames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"Nickname\", \"name\": \"Johnny\", \"ext3\":\"text\"  }, " +
                    "\"NICK-2\" : {  \"@type\":\"Nickname\", \"name\": \"Joe\" } " +
                "}," +
                "\"language\":\"en\"," +
                "\"ext1\": 10," +
                "\"preferredLanguages\":{" +
                    "\"LANG-1\":{\"@type\":\"LanguagePref\",\"pref\":1, \"language\":\"jp\",\"ext6\": [\"1\",\"2\"]}," +
                    "\"LANG-2\":{\"@type\":\"LanguagePref\",\"pref\":2, \"language\":\"en\"}" +
                "}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\",\"ext4\": 5}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"ext2\": { \"prop\": 10 }," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testExtendedJSContact2 - 1", 6, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact2 - 2", "LANGUAGE", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact2 - 3", "JSPROP", vcard.getExtendedProperties().get(1).getPropertyName());
        assertEquals("testExtendedJSContact2 - 4", "addresses/ADR-1/ext2", vcard.getExtendedProperties().get(1).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact2 - 5", VCardDataType.TEXT, vcard.getExtendedProperties().get(1).getDataType());
        assertEquals("testExtendedJSContact2 - 6", "{\"prop\":10}", vcard.getExtendedProperties().get(1).getValue());
        assertEquals("testExtendedJSContact2 - 7", "JSPROP", vcard.getExtendedProperties().get(2).getPropertyName());
        assertEquals("testExtendedJSContact2 - 8", "nicknames/NICK-1/ext3", vcard.getExtendedProperties().get(2).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact2 - 9", VCardDataType.TEXT, vcard.getExtendedProperties().get(2).getDataType());
        assertEquals("testExtendedJSContact2 - 10", "\"text\"", vcard.getExtendedProperties().get(2).getValue());
        assertEquals("testExtendedJSContact2 - 11", "JSPROP", vcard.getExtendedProperties().get(3).getPropertyName());
        assertEquals("testExtendedJSContact2 - 12", "preferredLanguages/LANG-1/ext6", vcard.getExtendedProperties().get(3).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact2 - 13", VCardDataType.TEXT, vcard.getExtendedProperties().get(3).getDataType());
        assertEquals("testExtendedJSContact2 - 14", "[\"1\",\"2\"]", vcard.getExtendedProperties().get(3).getValue());
        assertEquals("testExtendedJSContact2 - 15", "JSPROP", vcard.getExtendedProperties().get(4).getPropertyName());
        assertEquals("testExtendedJSContact2 - 16", "addresses/ADR-1/components/0/ext4", vcard.getExtendedProperties().get(4).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact2 - 17", VCardDataType.TEXT, vcard.getExtendedProperties().get(4).getDataType());
        assertEquals("testExtendedJSContact2 - 18", "5", vcard.getExtendedProperties().get(4).getValue());
        assertEquals("testExtendedJSContact2 - 19", "JSPROP", vcard.getExtendedProperties().get(5).getPropertyName());
        assertEquals("testExtendedJSContact2 - 20", "ext1", vcard.getExtendedProperties().get(5).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact2 - 21", VCardDataType.TEXT, vcard.getExtendedProperties().get(5).getDataType());
        assertEquals("testExtendedJSContact2 - 22", "10", vcard.getExtendedProperties().get(5).getValue());

    }

    @Test
    public void textExtendedJSContact3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"number\":\"tel:+33-01-23-45-6\", \"label\":\"a label\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testExtendedJSContact3 - 1", "G-PHONE-1", vcard.getTelephoneNumbers().get(0).getGroup());
        assertEquals("testExtendedJSContact3 - 2", 1, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact3 - 3", VCardPropEnum.X_ABLABEL.getValue(), vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact3 - 4", VCardDataType.TEXT, vcard.getExtendedProperties().get(0).getDataType());
        assertEquals("testExtendedJSContact3 - 5", "a label", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testExtendedJSContact3 - 6", "G-PHONE-1", vcard.getExtendedProperties().get(0).getGroup());
    }


    @Test
    public void textExtendedJSContact4() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\": { \"full\": \"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                "{" +
                    "\"@type\":\"Anniversary\"," +
                    "\"kind\":\"example.com:engagement\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testExtendedJSContact4 - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact4 - 3", "JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact4 - 4", "anniversaries/ANNIVERSARY-1", vcard.getExtendedProperties().get(0).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact4 - 5", VCardDataType.TEXT, vcard.getExtendedProperties().get(0).getDataType());
        assertEquals("testExtendedJSContact4 - 6", "{\"@type\":\"Anniversary\",\"kind\":\"example.com:engagement\",\"date\":{\"@type\":\"Timestamp\",\"utc\":\"1953-10-15T23:10:00Z\"}}", vcard.getExtendedProperties().get(0).getValue());
    }


    @Test
    public void textExtendedJSContact5() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Public, Esq.\"," +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"kind\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"kind\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"extvalue\", \"kind\": \"example.com:exttype\" }" +
                    "] " +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testExtendedJSContact5 - 1", "Mr. John Q. Public, Esq.", vcard.getFormattedName().getValue());
        assertNotNull("testExtendedJSContact5 - 2", vcard.getProperty(ExtendedStructuredName.class));
        assertEquals("testExtendedJSContact5 - 3", "Public", vcard.getProperty(ExtendedStructuredName.class).getFamily());
        assertEquals("testExtendedJSContact5 - 4", "John", vcard.getProperty(ExtendedStructuredName.class).getGiven());
        assertEquals("testExtendedJSContact5 - 5", 1, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact5 - 6", "JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact5 - 7", "name/components/2", vcard.getExtendedProperties().get(0).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact5 - 8", VCardDataType.TEXT, vcard.getExtendedProperties().get(0).getDataType());
        assertEquals("testExtendedJSContact5 - 9", "{\"@type\":\"NameComponent\",\"kind\":\"example.com:exttype\",\"value\":\"extvalue\"}", vcard.getExtendedProperties().get(0).getValue());
    }


    @Test
    public void testExtendedJSContact6() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"example.com:extcontext\": true},\"features\":{\"example.com:extfeature\": true},\"number\":\"tel:+33-01-23-45-6\"}}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testExtendedJSContact6 - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testExtendedJSContact6 - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertEquals("testExtendedJSContact6 - 3", 2, vcard.getExtendedProperties().size());
        assertEquals("testExtendedJSContact6 - 4", "JSPROP", vcard.getExtendedProperties().get(1).getPropertyName());
        assertEquals("testExtendedJSContact5 - 5", "phones/PHONE-1/features/example.com:extfeature", vcard.getExtendedProperties().get(1).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact5 - 6", VCardDataType.TEXT, vcard.getExtendedProperties().get(1).getDataType());
        assertEquals("testExtendedJSContact5 - 7", "true", vcard.getExtendedProperties().get(1).getValue());
        assertEquals("testExtendedJSContact6 - 8", "JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testExtendedJSContact5 - 9", "phones/PHONE-1/contexts/example.com:extcontext", vcard.getExtendedProperties().get(0).getParameter("JSPTR"));
        assertEquals("testExtendedJSContact5 - 10", VCardDataType.TEXT, vcard.getExtendedProperties().get(0).getDataType());
        assertEquals("testExtendedJSContact5 - 11", "true", vcard.getExtendedProperties().get(0).getValue());
    }


}
