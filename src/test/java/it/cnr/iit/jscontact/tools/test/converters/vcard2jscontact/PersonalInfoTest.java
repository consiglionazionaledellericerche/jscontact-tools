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

public class PersonalInfoTest extends VCard2JSContactTest {


    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=very high:reading\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard);

    }

    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "EXPERTISE;LEVEL=very high:chemistry\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard);

    }


    @Test
    public void testPersonalInfo1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high:reading\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfo1 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo1 - 2", 1, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo1 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo1 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo1 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfo2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high:reading\n" +
                "HOBBY;LEVEL=medium:sewing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfo2 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo2 - 2", 2, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo2 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo2 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo2 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfo2 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo2 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo2 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());

    }

    @Test
    public void testPersonalInfo3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfo3 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo3 - 2", 2, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo3 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo3 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo3 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfo3 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo3 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo3 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());

    }

    @Test
    public void testPersonalInfo4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=2:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=1:sewing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfo4 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo4 - 2", 2, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo4 - 3", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo4 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo4 - 5", jsCard.getPersonalInfo().get("HOBBY-2").ofHighLevel());
        assertTrue("testPersonalInfo4 - 6", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo4 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo4 - 8", jsCard.getPersonalInfo().get("HOBBY-1").ofMediumLevel());

    }


    @Test
    public void testPersonalInfo5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=1:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=2:rock 'n' roll music\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfo5 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo5 - 2", 4, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo5 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo5 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo5 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfo5 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo5 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo5 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertTrue("testPersonalInfo5 - 9", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfo5 - 10", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfo5 - 11", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertTrue("testPersonalInfo5 - 12", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfo5 - 13", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfo5 - 14", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());

    }

    @Test
    public void testPersonalInfo6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=2:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=1:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=2:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=1:rock 'n' roll music\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfo6 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo6 - 2", 4, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo6 - 3", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo6 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo6 - 5", jsCard.getPersonalInfo().get("HOBBY-2").ofHighLevel());
        assertTrue("testPersonalInfo6 - 6", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo6 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo6 - 8", jsCard.getPersonalInfo().get("HOBBY-1").ofMediumLevel());
        assertTrue("testPersonalInfo6 - 9", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfo6 - 10", "r&b music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfo6 - 11", jsCard.getPersonalInfo().get("INTEREST-2").ofMediumLevel());
        assertTrue("testPersonalInfo6 - 12", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfo6 - 13", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfo6 - 14", jsCard.getPersonalInfo().get("INTEREST-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfo7() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=1:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=2:rock 'n' roll music\n" +
                "EXPERTISE;LEVEL=beginner;INDEX=2:chinese literature\n" +
                "EXPERTISE;LEVEL=expert;INDEX=1:chemistry\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testPersonalInfo7 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo7 - 2", 6, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo7 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo7 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo7 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfo7 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo7 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo7 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertTrue("testPersonalInfo7 - 9", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfo7 - 10", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfo7 - 11", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertTrue("testPersonalInfo7 - 12", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfo7 - 13", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfo7 - 14", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());
        assertTrue("testPersonalInfo7 - 15", jsCard.getPersonalInfo().get("EXPERTISE-1").asExpertise());
        assertEquals("testPersonalInfo7 - 16", "chemistry", jsCard.getPersonalInfo().get("EXPERTISE-1").getValue());
        assertTrue("testPersonalInfo7 - 17", jsCard.getPersonalInfo().get("EXPERTISE-1").ofHighLevel());
        assertTrue("testPersonalInfo7 - 18", jsCard.getPersonalInfo().get("EXPERTISE-2").asExpertise());
        assertEquals("testPersonalInfo7 - 19", "chinese literature", jsCard.getPersonalInfo().get("EXPERTISE-2").getValue());
        assertTrue("testPersonalInfo7 - 20", jsCard.getPersonalInfo().get("EXPERTISE-2").ofLowLevel());

    }

}
