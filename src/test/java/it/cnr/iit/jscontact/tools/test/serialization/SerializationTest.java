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
package it.cnr.iit.jscontact.tools.test.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SerializationTest {


    @Test
    public void testSerialization1() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-RFC7483.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
        assertTrue("testSerialization1", jsCard.isValid());
        String serialized = objectMapper.writeValueAsString(jsCard);
        assertEquals(objectMapper.readTree(json), objectMapper.readTree(serialized));

    }

    @Test
    public void testSerialization2() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
        assertTrue("testSerialization2", jsCard.isValid());
        String serialized = objectMapper.writeValueAsString(jsCard);
        assertEquals(objectMapper.readTree(json), objectMapper.readTree(serialized));

    }

    @Test
    public void testSerialization3() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Unstructured.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
        assertTrue("testSerialization3", jsCard.isValid());
        String serialized = objectMapper.writeValueAsString(jsCard);
        assertEquals(objectMapper.readTree(json), objectMapper.readTree(serialized));

    }

    @Test
    public void testSerialization4() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCardGroup.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        objectMapper.registerModule(module);
        JSContact[] jsContacts = objectMapper.readValue(json, JSContact[].class);
        for (JSContact jsContact : jsContacts)
            assertTrue("testSerialization4", jsContact.isValid());
        String serialized = objectMapper.writeValueAsString(jsContacts);
        assertEquals(objectMapper.readTree(json), objectMapper.readTree(serialized));

    }

    @Test
    public void testSerialization5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"], [\"myext\", {}, \"text\", \"extvalue\"]]]";
        JCard2JSContact jCard2JSContact = JCard2JSContact.builder().config(VCard2JSContactConfig.builder().build()).build();
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        jsCard.setUid("549e9dd2-ecb1-46af-8df1-09e98329d0ff");
        ObjectMapper objectMapper = new ObjectMapper();
        String serialized = objectMapper.writeValueAsString(jsCard);
        assertTrue("testSerialization5", serialized.equals("{\"uid\":\"549e9dd2-ecb1-46af-8df1-09e98329d0ff\",\"fullName\":{\"value\":\"test\"},\"extension/myext\":\"extvalue\"}"));

    }





}
