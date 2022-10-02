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

import static org.junit.Assert.*;

public class AnniversariesTest extends VCard2JSContactTest {

    @Test
    public void testAnniversaries1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaries1 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaries1 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaries1 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversaries1 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
    }

    @Test
    public void testAnniversaries2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaries2 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaries2 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaries2 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversaries2 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversaries2 - 5", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
    }

    @Test
    public void testAnniversaries3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaries3 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaries3 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaries3 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversaries3 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isDeath());
    }


    @Test
    public void testAnniversaries4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaries4 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaries4 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaries4 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversaries4 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isDeath());
        assertEquals("testAnniversaries4 - 5", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
    }


    @Test
    public void testAnniversaries5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Los Angeles CA USA\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaries5 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaries5 - 2", 2, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaries5 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversaries5 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversaries5 - 5", "Los Angeles CA USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
        assertTrue("testAnniversaries5 - 6",jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversaries5 - 7",jsCard.getAnniversaries().get("ANNIVERSARY-2").isDeath());
        assertEquals("testAnniversaries5 - 8", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-2").getPlace().getFullAddress());
    }

    @Test
    public void testAnniversaries6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Los Angeles CA USA\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "ANNIVERSARY:1986-02-01T19:00:00Z\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaries6 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaries6 - 2", 3, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaries6 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversaries6 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversaries6 - 5", "Los Angeles CA USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
        assertTrue("testAnniversaries6 - 6",jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversaries6 - 7",jsCard.getAnniversaries().get("ANNIVERSARY-2").isDeath());
        assertEquals("testAnniversaries6 - 8", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-2").getPlace().getFullAddress());
        assertTrue("testAnniversaries6 - 9",jsCard.getAnniversaries().get("ANNIVERSARY-3").isOtherAnniversary());
        assertTrue("testAnniversaries6 - 10",jsCard.getAnniversaries().get("ANNIVERSARY-3").getDate().isEqual("1986-02-01T19:00:00Z"));
        assertEquals("testAnniversaries6 - 11", "marriage date", jsCard.getAnniversaries().get("ANNIVERSARY-3").getLabel());
    }

    @Test
    public void testAnniversaries7() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE;VALUE=uri:geo:34.15876,-118.45728\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "ANNIVERSARY:19860201T190000Z\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversaries7 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversaries7 - 2", 3, jsCard.getAnniversaries().size());
        assertTrue("testAnniversaries7 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversaries7 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversaries7 - 5", "geo:34.15876,-118.45728", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getCoordinates());
        assertTrue("testAnniversaries7 - 6",jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversaries7 - 7",jsCard.getAnniversaries().get("ANNIVERSARY-2").isDeath());
        assertEquals("testAnniversaries7 - 8", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-2").getPlace().getFullAddress());
        assertTrue("testAnniversaries7 - 9",jsCard.getAnniversaries().get("ANNIVERSARY-3").isOtherAnniversary());
        assertTrue("testAnniversaries7 - 10",jsCard.getAnniversaries().get("ANNIVERSARY-3").getDate().isEqual("1986-02-01T19:00:00Z"));
        assertEquals("testAnniversaries7 - 11", "marriage date", jsCard.getAnniversaries().get("ANNIVERSARY-3").getLabel());
    }

}
