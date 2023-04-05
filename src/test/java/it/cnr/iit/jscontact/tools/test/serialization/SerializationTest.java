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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.*;

public class SerializationTest {


    @Test
    public void testSerialization1() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-RFC7483.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toJSCard(json);
        assertTrue("testSerialization1 - 1", jsCard.isValid());
        String serialized = Card.toJson(jsCard);
        assertEquals("testSerialization1 - 2",jsCard, Card.toJSCard(serialized));

    }

    @Test
    public void testSerialization2() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toJSCard(json);
        assertTrue("testSerialization2 - 1", jsCard.isValid());
        String serialized = Card.toJson(jsCard);
        assertEquals("testSerialization2 - 2",jsCard, Card.toJSCard(serialized));

    }

    @Test
    public void testSerialization3() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Unstructured.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toJSCard(json);
        assertTrue("testSerialization3 - 1", jsCard.isValid());
        String serialized = Card.toJson(jsCard);
        assertEquals("testSerialization3 - 2",jsCard, Card.toJSCard(serialized));

    }

    @Test
    public void testSerialization4() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCardGroup.json")), StandardCharsets.UTF_8);
        Card[] jsCards = Card.toJSCards(json);
        for (Card jsCard : jsCards)
            assertTrue("testSerialization4 - 1", jsCard.isValid());
        String serialized = Card.toJson(jsCards);
        assertArrayEquals("testSerialization4 - 2",jsCards, Card.toJSCards(serialized));

    }

    @Test
    public void testSerialization5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], [\"fn\", {}, \"text\", \"test\"], [\"myext\", {}, \"text\", \"extvalue\"]]]";
        JCard2JSContact jCard2JSContact = JCard2JSContact.builder().config(VCard2JSContactConfig.builder().build()).build();
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        jsCard.setUid("549e9dd2-ecb1-46af-8df1-09e98329d0ff");
        String serialized = Card.toJson(jsCard);
        assertEquals("testSerialization5", "{\"@type\":\"Card\",\"@version\":\"1.0\",\"uid\":\"549e9dd2-ecb1-46af-8df1-09e98329d0ff\",\"fullName\":\"test\",\"vCardProps\":[[\"version\",{},\"text\",\"4.0\"],[\"myext\",{},\"text\",\"extvalue\"]]}", serialized);

    }

}
