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

import it.cnr.iit.jscontact.tools.dto.Card;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class CardCloneTest {


    @Test
    public void testClone1() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toJSCard(json);
        assertTrue("testClone1", jsCard.equals(jsCard.clone()));

    }

    @Test
    public void testClone2() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-RFC7483.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toJSCard(json);
        assertTrue("testClone2", jsCard.equals(jsCard.clone()));

    }

    @Test
    public void testClone3() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Unstructured.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toJSCard(json);
        assertTrue("testClone3", jsCard.equals(jsCard.clone()));

    }

}
