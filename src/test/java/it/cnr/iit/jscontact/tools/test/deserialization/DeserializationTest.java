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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

public class DeserializationTest {


    @Test
    public void testDeserialization1() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
        assertTrue("testDeserialization1", jsCard.isValid());

    }

    @Test
    public void testDeserialization2() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-RFC7483.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
        assertTrue("testDeserialization2", jsCard.isValid());

    }

    @Test
    public void testDeserialization3() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Unstructured.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
        assertTrue("testDeserialization3", jsCard.isValid());

    }

    @Test
    public void testDeserialization4() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCardGroup.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        objectMapper.registerModule(module);
        JSContact[] jsContacts = objectMapper.readValue(json, JSContact[].class);
        for (JSContact jsContact : jsContacts)
            assertTrue("testDeserialization4", jsContact.isValid());
    }

}
