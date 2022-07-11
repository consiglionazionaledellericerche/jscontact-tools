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
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CategoriesTest extends VCard2JSContactTest {

    @Test
    public void testCategoriesValid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CATEGORIES:INTERNET,IETF,INDUSTRY,INFORMATION TECHNOLOGY\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCategoriesValid1 - 1",jsCard.getCategories()!=null);
        assertTrue("testCategoriesValid1 - 2",jsCard.getCategories().size() == 4);
        assertTrue("testCategoriesValid1 - 3",jsCard.getCategories().containsKey("INTERNET"));
        assertTrue("testCategoriesValid1 - 4",jsCard.getCategories().containsKey("IETF"));
        assertTrue("testCategoriesValid1 - 5",jsCard.getCategories().containsKey("INDUSTRY"));
        assertTrue("testCategoriesValid1 - 6",jsCard.getCategories().containsKey("INFORMATION TECHNOLOGY"));
    }

    @Test
    public void testCategoriesValid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CATEGORIES:INTERNET,IETF,INDUSTRY,INFORMATION TECHNOLOGY\n" +
                "CATEGORIES:TRAVEL AGENT\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testCategoriesValid2 - 1",jsCard.getCategories()!=null);
        assertTrue("testCategoriesValid2 - 2",jsCard.getCategories().size() == 5);
        assertTrue("testCategoriesValid2 - 3",jsCard.getCategories().containsKey("INTERNET"));
        assertTrue("testCategoriesValid2 - 4",jsCard.getCategories().containsKey("IETF"));
        assertTrue("testCategoriesValid2 - 5",jsCard.getCategories().containsKey("INDUSTRY"));
        assertTrue("testCategoriesValid2 - 6",jsCard.getCategories().containsKey("INFORMATION TECHNOLOGY"));
        assertTrue("testCategoriesValid2 - 7",jsCard.getCategories().containsKey("TRAVEL AGENT"));
    }

}
