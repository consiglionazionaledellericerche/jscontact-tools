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

public class KeywordsTest extends JCard2JSContactTest {

    @Test
    public void testKeywords1() throws CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s]", categories) +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testKeywords1 - 1", jsCard.getKeywords());
        assertEquals("testKeywords1 - 2", 4, jsCard.getKeywords().size());
        assertTrue("testKeywords1 - 3",jsCard.getKeywords().containsKey("INTERNET"));
        assertTrue("testKeywords1 - 4",jsCard.getKeywords().containsKey("IETF"));
        assertTrue("testKeywords1 - 5",jsCard.getKeywords().containsKey("INDUSTRY"));
        assertTrue("testKeywords1 - 6",jsCard.getKeywords().containsKey("INFORMATION TECHNOLOGY"));

    }

    @Test
    public void testKeywords2() throws CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s],", categories) +
                "[\"categories\", {}, \"text\", \"TRAVEL AGENT\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testKeywords2 - 1", jsCard.getKeywords());
        assertEquals("testKeywords2 - 2", 5, jsCard.getKeywords().size());
        assertTrue("testKeywords2 - 3",jsCard.getKeywords().containsKey("INTERNET"));
        assertTrue("testKeywords2 - 4",jsCard.getKeywords().containsKey("IETF"));
        assertTrue("testKeywords2 - 5",jsCard.getKeywords().containsKey("INDUSTRY"));
        assertTrue("testKeywords2 - 6",jsCard.getKeywords().containsKey("INFORMATION TECHNOLOGY"));
        assertTrue("testKeywords2 - 7",jsCard.getKeywords().containsKey("TRAVEL AGENT"));

    }


    @Test
    public void testKeywords3() throws CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s],", categories) +
                "[\"categories\", {\"pref\" : \"1\"}, \"text\", \"TRAVEL AGENT\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testKeywords3 - 1", jsCard.getKeywords());
        assertEquals("testKeywords3 - 2", 5, jsCard.getKeywords().size());
        Set<String> keys = jsCard.getKeywords().keySet();
        String[] array = keys.toArray(new String[0]);
        assertEquals("testKeywords3 - 3", "INTERNET", array[0]);
        assertEquals("testKeywords3 - 4", "IETF", array[1]);
        assertEquals("testKeywords3 - 5", "INDUSTRY", array[2]);
        assertEquals("testKeywords3 - 6", "INFORMATION TECHNOLOGY", array[3]);
        assertEquals("testKeywords3 - 7", "TRAVEL AGENT", array[4]);

    }

}
