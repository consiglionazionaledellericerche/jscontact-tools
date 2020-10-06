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

import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CategoriesTest extends JCard2JSContactTest {

    @Test
    public void testCategoriesValid1() throws IOException, CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s]", categories) +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testCategoriesValid1 - 1",jsCard.getCategories()!=null);
        assertTrue("testCategoriesValid1 - 2",jsCard.getCategories().length == 4);
        assertTrue("testCategoriesValid1 - 3",jsCard.getCategories()[0].equals("INTERNET"));
        assertTrue("testCategoriesValid1 - 4",jsCard.getCategories()[1].equals("IETF"));
        assertTrue("testCategoriesValid1 - 5",jsCard.getCategories()[2].equals("INDUSTRY"));
        assertTrue("testCategoriesValid1 - 6",jsCard.getCategories()[3].equals("INFORMATION TECHNOLOGY"));

    }

    @Test
    public void testCategoriesValid2() throws IOException, CardException {

        String categories = "\"INTERNET\",\"IETF\",\"INDUSTRY\",\"INFORMATION TECHNOLOGY\"";

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                String.format("[\"categories\", {}, \"text\", %s],", categories) +
                "[\"categories\", {}, \"text\", \"TRAVEL AGENT\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testCategoriesValid2 - 1",jsCard.getCategories()!=null);
        assertTrue("testCategoriesValid2 - 2",jsCard.getCategories().length == 5);
        assertTrue("testCategoriesValid2 - 3",jsCard.getCategories()[0].equals("INTERNET"));
        assertTrue("testCategoriesValid2 - 4",jsCard.getCategories()[1].equals("IETF"));
        assertTrue("testCategoriesValid2 - 5",jsCard.getCategories()[2].equals("INDUSTRY"));
        assertTrue("testCategoriesValid2 - 6",jsCard.getCategories()[3].equals("INFORMATION TECHNOLOGY"));
        assertTrue("testCategoriesValid2 - 7",jsCard.getCategories()[4].equals("TRAVEL AGENT"));

    }


}
