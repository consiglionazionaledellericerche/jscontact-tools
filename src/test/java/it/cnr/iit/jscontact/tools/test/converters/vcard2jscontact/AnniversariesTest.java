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
    public void testAnniversariesValid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversariesValid1 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversariesValid1 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversariesValid1 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid1 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
    }

    @Test
    public void testAnniversariesValid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversariesValid2 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversariesValid2 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversariesValid2 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid2 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversariesValid2 - 5", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
    }

    @Test
    public void testAnniversariesValid3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversariesValid3 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversariesValid3 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversariesValid3 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid3 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isDeath());
    }


    @Test
    public void testAnniversariesValid4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversariesValid4 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversariesValid4 - 2", 1, jsCard.getAnniversaries().size());
        assertTrue("testAnniversariesValid4 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid4 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isDeath());
        assertEquals("testAnniversariesValid4 - 5", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
    }


    @Test
    public void testAnniversariesValid5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Los Angeles CA USA\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAnniversariesValid5 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversariesValid5 - 2", 2, jsCard.getAnniversaries().size());
        assertTrue("testAnniversariesValid5 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid5 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversariesValid5 - 5", "Los Angeles CA USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
        assertTrue("testAnniversariesValid5 - 6",jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid5 - 7",jsCard.getAnniversaries().get("ANNIVERSARY-2").isDeath());
        assertEquals("testAnniversariesValid5 - 8", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-2").getPlace().getFullAddress());
    }

    @Test
    public void testAnniversariesValid6() throws CardException {

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
        assertNotNull("testAnniversariesValid6 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversariesValid6 - 2", 3, jsCard.getAnniversaries().size());
        assertTrue("testAnniversariesValid6 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid6 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversariesValid6 - 5", "Los Angeles CA USA", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getFullAddress());
        assertTrue("testAnniversariesValid6 - 6",jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid6 - 7",jsCard.getAnniversaries().get("ANNIVERSARY-2").isDeath());
        assertEquals("testAnniversariesValid6 - 8", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-2").getPlace().getFullAddress());
        assertTrue("testAnniversariesValid6 - 9",jsCard.getAnniversaries().get("ANNIVERSARY-3").isOtherAnniversary());
        assertTrue("testAnniversariesValid6 - 10",jsCard.getAnniversaries().get("ANNIVERSARY-3").getDate().isEqual("1986-02-01T19:00:00Z"));
        assertEquals("testAnniversariesValid6 - 11", "marriage date", jsCard.getAnniversaries().get("ANNIVERSARY-3").getLabel());
    }

    @Test
    public void testAnniversariesValid7() throws CardException {

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
        assertNotNull("testAnniversariesValid7 - 1", jsCard.getAnniversaries());
        assertEquals("testAnniversariesValid7 - 2", 3, jsCard.getAnniversaries().size());
        assertTrue("testAnniversariesValid7 - 3",jsCard.getAnniversaries().get("ANNIVERSARY-1").getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid7 - 4",jsCard.getAnniversaries().get("ANNIVERSARY-1").isBirth());
        assertEquals("testAnniversariesValid7 - 5", "geo:34.15876,-118.45728", jsCard.getAnniversaries().get("ANNIVERSARY-1").getPlace().getCoordinates());
        assertTrue("testAnniversariesValid7 - 6",jsCard.getAnniversaries().get("ANNIVERSARY-2").getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid7 - 7",jsCard.getAnniversaries().get("ANNIVERSARY-2").isDeath());
        assertEquals("testAnniversariesValid7 - 8", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", jsCard.getAnniversaries().get("ANNIVERSARY-2").getPlace().getFullAddress());
        assertTrue("testAnniversariesValid7 - 9",jsCard.getAnniversaries().get("ANNIVERSARY-3").isOtherAnniversary());
        assertTrue("testAnniversariesValid7 - 10",jsCard.getAnniversaries().get("ANNIVERSARY-3").getDate().isEqual("1986-02-01T19:00:00Z"));
        assertEquals("testAnniversariesValid7 - 11", "marriage date", jsCard.getAnniversaries().get("ANNIVERSARY-3").getLabel());
    }

}
