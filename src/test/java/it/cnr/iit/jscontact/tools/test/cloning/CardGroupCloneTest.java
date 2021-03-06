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
package it.cnr.iit.jscontact.tools.test.cloning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.cnr.iit.jscontact.tools.dto.CardGroup;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class CardGroupCloneTest {

    @Test
    public void testClone1() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCardGroup.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        objectMapper.registerModule(module);
        JSContact[] jsContacts = objectMapper.readValue(json, JSContact[].class);
        for (JSContact jsContact : jsContacts) {
            if (jsContact instanceof CardGroup)
                assertTrue("testClone1", Objects.deepEquals(jsContact, ((CardGroup) jsContact).clone()));
        }
    }

}
