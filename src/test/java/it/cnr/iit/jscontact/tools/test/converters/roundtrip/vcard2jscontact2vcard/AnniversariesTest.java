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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.vcard2jscontact2vcard;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnniversariesTest extends RoundtripTest {

    @Test
    public void testAnniversaries1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAnniversaries1 - 1", vcard2, VCardParser.parse(vcard).get(0));
    }

    @Test
    public void testAnniversaries2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "BDAY:19531015T231000Z\n" +
                "BIRTHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAnniversaries2 - 1", vcard2, VCardParser.parse(vcard).get(0));
    }

    @Test
    public void testAnniversaries3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19531015T231000Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAnniversaries3 - 1", vcard2, VCardParser.parse(vcard).get(0));
    }

    @Test
    public void testAnniversaries4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "DEATHDATE:19931015T231000Z\n" +
                "DEATHPLACE:Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAnniversaries4 - 1", vcard2, VCardParser.parse(vcard).get(0));
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

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAnniversaries5 - 1", vcard2, VCardParser.parse(vcard).get(0));
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

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAnniversaries6 - 1", vcard2, VCardParser.parse(vcard).get(0));
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

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAnniversaries7 - 1", vcard2, VCardParser.parse(vcard).get(0));
    }

}
