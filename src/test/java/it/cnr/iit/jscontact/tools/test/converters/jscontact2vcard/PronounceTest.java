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
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedStructuredName;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PronounceTest extends JSContact2VCardTest {

    @Test
    public void testPronounce1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Smith, Esq.\"," +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"kind\": \"title\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"kind\": \"given\" }," +
                        "{ " +
                            "\"@type\":\"NameComponent\"," +
                            "\"value\":\"Smith\"," +
                            "\"kind\": \"surname\"," +
                            "\"pronounce\": {" +
                                "\"@type\":\"Pronounce\"," +
                                "\"phonetics\": \"/smɪθ/\"," +
                                "\"system\": \"ipa\"" +
                            "}" +
                        "}," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"kind\": \"given2\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"kind\": \"credential\" }" +
                    "], " +
                    "\"sortAs\": { \"surname\":\"Smith\",\"given\":\"John\" }" +
                "}, " +
                "\"nickNames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"NickName\",\"name\": \"Johnny\" }, " +
                    "\"NICK-2\" : {  \"@type\":\"NickName\",\"name\": \"Joe\" } " +
                "}" +
        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPronounce1 - 1", "Mr. John Q. Smith, Esq.", vcard.getFormattedName().getValue());
        assertNotNull("testPronounce1 - 2", vcard.getProperty(ExtendedStructuredName.class));
        assertEquals("testPronounce1 - 3", "Smith", vcard.getProperty(ExtendedStructuredName.class).getFamily());
        assertEquals("testPronounce1 - 4", "John", vcard.getProperty(ExtendedStructuredName.class).getGiven());
        assertEquals("testPronounce1 - 5", 1, vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().size());
        assertEquals("testPronounce1 - 6", "Quinlan", vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().get(0));
        assertEquals("testPronounce1 - 7", 1, vcard.getProperty(ExtendedStructuredName.class).getPrefixes().size());
        assertEquals("testPronounce1 - 8", "Mr.", vcard.getProperty(ExtendedStructuredName.class).getPrefixes().get(0));
        assertEquals("testPronounce1 - 9", 1, vcard.getProperty(ExtendedStructuredName.class).getSuffixes().size());
        assertEquals("testPronounce1 - 10", "Esq.", vcard.getProperty(ExtendedStructuredName.class).getSuffixes().get(0));
        assertEquals("testPronounce1 - 11", "Smith,John", String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,vcard.getProperty(ExtendedStructuredName.class).getSortAs()));
        assertEquals("testPronounce1 - 12", 2, vcard.getNicknames().size());
        assertEquals("testPronounce1 - 13", "Johnny", vcard.getNicknames().get(0).getValues().get(0));
        assertEquals("testPronounce1 - 14", "Joe", vcard.getNicknames().get(1).getValues().get(0));
        assertEquals("testPronounce1 - 15", "JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testPronounce1 - 16", "name/components/2/pronounce", vcard.getExtendedProperties().get(0).getParameter("JSPTR"));
        assertEquals("testPronounce1 - 17", "{\"@type\":\"Pronounce\",\"phonetics\":\"/smɪθ/\",\"system\":\"ipa\"}", vcard.getExtendedProperties().get(0).getValue());

    }

    @Test
    public void testPronounce2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                    "\"full\": \"Smith\"," +
                    "\"pronounce\": {" +
                        "\"@type\":\"Pronounce\"," +
                        "\"phonetics\": \"/smɪθ/\"," +
                        "\"system\": \"ipa\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPronounce2 - 1", "Smith", vcard.getFormattedName().getValue());
        assertEquals("testPronounce2 - 2", "JSPROP", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testPronounce2 - 3", "name/pronounce", vcard.getExtendedProperties().get(0).getParameter("JSPTR"));
        assertEquals("testPronounce2 - 4", "{\"@type\":\"Pronounce\",\"phonetics\":\"/smɪθ/\",\"system\":\"ipa\"}", vcard.getExtendedProperties().get(0).getValue());

    }
    
    
}
