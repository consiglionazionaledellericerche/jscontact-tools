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

import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class AnniversariesTest extends VCard2JSContactTest {

    @Test
    public void testAnniversariesValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAnniversariesValid1 - 1",jsCard.getAnniversaries()!=null);
        assertTrue("testAnniversariesValid1 - 2",jsCard.getAnniversaries().length == 1);
        assertTrue("testAnniversariesValid1 - 3",jsCard.getAnniversaries()[0].getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid1 - 4",jsCard.getAnniversaries()[0].isBirth());
    }

    @Test
    public void testAnniversariesValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAnniversariesValid2 - 1",jsCard.getAnniversaries()!=null);
        assertTrue("testAnniversariesValid2 - 2",jsCard.getAnniversaries().length == 1);
        assertTrue("testAnniversariesValid2 - 3",jsCard.getAnniversaries()[0].getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid2 - 4",jsCard.getAnniversaries()[0].isBirth());
        assertTrue("testAnniversariesValid2 - 5",jsCard.getAnniversaries()[0].getPlace().getFullAddress().getValue().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA"));
    }

    @Test
    public void testAnniversariesValid3() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19531015T231000Z\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAnniversariesValid3 - 1",jsCard.getAnniversaries()!=null);
        assertTrue("testAnniversariesValid3 - 2",jsCard.getAnniversaries().length == 1);
        assertTrue("testAnniversariesValid3 - 3",jsCard.getAnniversaries()[0].getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid3 - 4",jsCard.getAnniversaries()[0].isDeath());
    }


    @Test
    public void testAnniversariesValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAnniversariesValid4 - 1",jsCard.getAnniversaries()!=null);
        assertTrue("testAnniversariesValid4 - 2",jsCard.getAnniversaries().length == 1);
        assertTrue("testAnniversariesValid4 - 3",jsCard.getAnniversaries()[0].getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid4 - 4",jsCard.getAnniversaries()[0].isDeath());
        assertTrue("testAnniversariesValid4 - 5",jsCard.getAnniversaries()[0].getPlace().getFullAddress().getValue().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA"));
    }


    @Test
    public void testAnniversariesValid5() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Los Angeles CA USA\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAnniversariesValid5 - 1",jsCard.getAnniversaries()!=null);
        assertTrue("testAnniversariesValid5 - 2",jsCard.getAnniversaries().length == 2);
        assertTrue("testAnniversariesValid5 - 3",jsCard.getAnniversaries()[0].getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid5 - 4",jsCard.getAnniversaries()[0].isBirth());
        assertTrue("testAnniversariesValid5 - 5",jsCard.getAnniversaries()[0].getPlace().getFullAddress().getValue().equals("Los Angeles CA USA"));
        assertTrue("testAnniversariesValid5 - 6",jsCard.getAnniversaries()[1].getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid5 - 7",jsCard.getAnniversaries()[1].isDeath());
        assertTrue("testAnniversariesValid5 - 8",jsCard.getAnniversaries()[1].getPlace().getFullAddress().getValue().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA"));
    }

    @Test
    public void testAnniversariesValid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Los Angeles CA USA\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "ANNIVERSARY:1986-02-01T19:00:00Z\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAnniversariesValid6 - 1",jsCard.getAnniversaries()!=null);
        assertTrue("testAnniversariesValid6 - 2",jsCard.getAnniversaries().length == 3);
        assertTrue("testAnniversariesValid6 - 3",jsCard.getAnniversaries()[0].getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid6 - 4",jsCard.getAnniversaries()[0].isBirth());
        assertTrue("testAnniversariesValid6 - 5",jsCard.getAnniversaries()[0].getPlace().getFullAddress().getValue().equals("Los Angeles CA USA"));
        assertTrue("testAnniversariesValid6 - 6",jsCard.getAnniversaries()[1].getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid6 - 7",jsCard.getAnniversaries()[1].isDeath());
        assertTrue("testAnniversariesValid6 - 8",jsCard.getAnniversaries()[1].getPlace().getFullAddress().getValue().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA"));
        assertTrue("testAnniversariesValid6 - 9",jsCard.getAnniversaries()[2].isOtherAnniversary());
        assertTrue("testAnniversariesValid6 - 10",jsCard.getAnniversaries()[2].getDate().isEqual("1986-02-01T19:00:00Z"));
        assertTrue("testAnniversariesValid6 - 11",jsCard.getAnniversaries()[2].getLabel().equals("marriage date"));
    }

    @Test
    public void testAnniversariesValid7() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE;VALUE=uri:geo:34.15876,-118.45728\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "ANNIVERSARY:19860201T190000Z\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAnniversariesValid7 - 1",jsCard.getAnniversaries()!=null);
        assertTrue("testAnniversariesValid7 - 2",jsCard.getAnniversaries().length == 3);
        assertTrue("testAnniversariesValid7 - 3",jsCard.getAnniversaries()[0].getDate().isEqual("1953-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid7 - 4",jsCard.getAnniversaries()[0].isBirth());
        assertTrue("testAnniversariesValid7 - 5",jsCard.getAnniversaries()[0].getPlace().getCoordinates().equals("geo:34.15876,-118.45728"));
        assertTrue("testAnniversariesValid7 - 6",jsCard.getAnniversaries()[1].getDate().isEqual("1993-10-15T23:10:00Z"));
        assertTrue("testAnniversariesValid7 - 7",jsCard.getAnniversaries()[1].isDeath());
        assertTrue("testAnniversariesValid7 - 8",jsCard.getAnniversaries()[1].getPlace().getFullAddress().getValue().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA"));
        assertTrue("testAnniversariesValid7 - 9",jsCard.getAnniversaries()[2].isOtherAnniversary());
        assertTrue("testAnniversariesValid7 - 10",jsCard.getAnniversaries()[2].getDate().isEqual("1986-02-01T19:00:00Z"));
        assertTrue("testAnniversariesValid7 - 11",jsCard.getAnniversaries()[2].getLabel().equals("marriage date"));
    }

}
