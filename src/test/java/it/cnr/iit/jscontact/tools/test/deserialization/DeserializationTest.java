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
package it.cnr.iit.jscontact.tools.test.deserialization;


import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeserializationTest {


    @Test
    public void testDeserialization1() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toCard(json);
        assertTrue("testDeserialization1", jsCard.isValid());

    }

    @Test
    public void testDeserialization2() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-RFC7483.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toCard(json);
        assertTrue("testDeserialization2", jsCard.isValid());

    }

    @Test
    public void testDeserialization3() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Unstructured.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toCard(json);
        assertTrue("testDeserialization3", jsCard.isValid());

    }

    @Test
    public void testDeserialization4() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCardGroup.json")), StandardCharsets.UTF_8);
        JSContact[] jsContacts = JSContact.toJSContacts(json);
        for (JSContact jsContact : jsContacts)
            assertTrue("testDeserialization4", jsContact.isValid());
    }



    @Test
    public void testDeserialization5() throws IOException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"nickNames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"NickName\", \"name\": \"Johnny\", \"ext3\":\"text\"  }, " +
                    "\"NICK-2\" : {  \"@type\":\"NickName\", \"name\": \"Joe\" } " +
                "}," +
                "\"locale\":\"en\"," +
                "\"ext1\": 10," +
                "\"preferredContactLanguages\":{" +
                    "\"jp\":[{\"@type\":\"ContactLanguage\",\"pref\":1, \"ext6\": [\"1\",\"2\"]}]," +
                    "\"en\":[{\"@type\":\"ContactLanguage\",\"pref\":2}]" +
                "}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"street\":[{\"@type\":\"StreetComponent\",\"type\":\"name\", \"value\":\"54321 Oak St\", \"ext4\": 5}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
                        "\"ext2\": { \"prop\": 10 }," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}" +
                "}";
        JSContact[] jsContacts = JSContact.toJSContacts(jscard);
        Card jsCard = ((Card) jsContacts[0]);
        assertEquals("testDeserialization5 - 1", 1, jsCard.getExtensions().size());
        assertEquals("testDeserialization5 - 2", 1, jsCard.getAddresses().get("ADR-1").getExtensions().size());
        assertTrue("testDeserialization5 - 3", jsCard.getExtensions().get("ext1") instanceof Integer);
        assertEquals("testDeserialization5 - 4", LinkedHashMap.class, jsCard.getAddresses().get("ADR-1").getExtensions().get("ext2").getClass());
        Map<String,Object> allExtensionsMap = new HashMap<String,Object>();
        jsCard.buildAllExtensionsMap(allExtensionsMap,"");
        assertEquals("testDeserialization6 - 5", 5, allExtensionsMap.size());
        assertTrue("testDeserialization6 - 6", allExtensionsMap.keySet().contains("addresses/ADR-1/street/0/ext4"));
        assertTrue("testDeserialization6 - 7", allExtensionsMap.keySet().contains("nickNames/NICK-1/ext3"));
        assertTrue("testDeserialization6 - 8", allExtensionsMap.keySet().contains("addresses/ADR-1/ext2"));
        assertTrue("testDeserialization6 - 9", allExtensionsMap.keySet().contains("preferredContactLanguages/jp/0/ext6"));
        assertTrue("testDeserialization6 - 10", allExtensionsMap.keySet().contains("ext1"));

    }

}
