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
package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class CategoriesTest extends JCard2JSContactTest {

    @Test
    public void testCategories1() throws CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s]", categories) +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testCategories1 - 1", jsCard.getKeywords());
        assertEquals("testCategories1 - 2", 4, jsCard.getKeywords().size());
        assertTrue("testCategories1 - 3",jsCard.getKeywords().containsKey("INTERNET"));
        assertTrue("testCategories1 - 4",jsCard.getKeywords().containsKey("IETF"));
        assertTrue("testCategories1 - 5",jsCard.getKeywords().containsKey("INDUSTRY"));
        assertTrue("testCategories1 - 6",jsCard.getKeywords().containsKey("INFORMATION TECHNOLOGY"));

    }

    @Test
    public void testCategories2() throws CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s],", categories) +
                "[\"categories\", {}, \"text\", \"TRAVEL AGENT\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testCategories2 - 1", jsCard.getKeywords());
        assertEquals("testCategories2 - 2", 5, jsCard.getKeywords().size());
        assertTrue("testCategories2 - 3",jsCard.getKeywords().containsKey("INTERNET"));
        assertTrue("testCategories2 - 4",jsCard.getKeywords().containsKey("IETF"));
        assertTrue("testCategories2 - 5",jsCard.getKeywords().containsKey("INDUSTRY"));
        assertTrue("testCategories2 - 6",jsCard.getKeywords().containsKey("INFORMATION TECHNOLOGY"));
        assertTrue("testCategories2 - 7",jsCard.getKeywords().containsKey("TRAVEL AGENT"));

    }


    @Test
    public void testCategories3() throws CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s],", categories) +
                "[\"categories\", {\"pref\" : \"1\"}, \"text\", \"TRAVEL AGENT\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testCategories3 - 1", jsCard.getKeywords());
        assertEquals("testCategories3 - 2", 5, jsCard.getKeywords().size());
        Set<String> keys = jsCard.getKeywords().keySet();
        String[] array = keys.toArray(new String[0]);
        assertEquals("testCategories3 - 3", "INTERNET", array[0]);
        assertEquals("testCategories3 - 4", "IETF", array[1]);
        assertEquals("testCategories3 - 5", "INDUSTRY", array[2]);
        assertEquals("testCategories3 - 6", "INFORMATION TECHNOLOGY", array[3]);
        assertEquals("testCategories3 - 7", "TRAVEL AGENT", array[4]);

    }

}
