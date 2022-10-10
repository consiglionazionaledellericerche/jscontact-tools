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
import java.util.Objects;

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

}
