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

import static org.junit.Assert.*;

public class PersonalInfoTest extends JCard2JSContactTest {


    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                                  "[\"fn\", {}, \"text\", \"test\"], " +
                                  "[\"hobby\", {\"level\": \"very high\"}, \"text\", \"reading\"]" +
                                 "]]";
        jCard2JSContact.convert(jcard);

    }

    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"expertise\", {\"level\": \"very high\"}, \"text\", \"chemistry\"]" +
                "]]";
        jCard2JSContact.convert(jcard);

    }

    @Test
    public void testPersonalInfoValid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\"}, \"text\", \"reading\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfoValid1 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoValid1 - 2", 1, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoValid1 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoValid1 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoValid1 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfoValid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\"}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\"}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfoValid2 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoValid2 - 2", 2, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoValid2 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoValid2 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoValid2 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid2 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfoValid2 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfoValid2 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());

    }

    @Test
    public void testPersonalInfoValid3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfoValid3 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoValid3 - 2", 2, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoValid3 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoValid3 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoValid3 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid3 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfoValid3 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfoValid3 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());

    }

    @Test
    public void testPersonalInfoValid4() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 2}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfoValid4 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoValid4 - 2", 2, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoValid4 - 3", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfoValid4 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfoValid4 - 5", jsCard.getPersonalInfo().get("HOBBY-2").ofHighLevel());
        assertTrue("testPersonalInfoValid4 - 6", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoValid4 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoValid4 - 8", jsCard.getPersonalInfo().get("HOBBY-1").ofMediumLevel());

    }


    @Test
    public void testPersonalInfoValid5() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 2}, \"text\", \"rock 'n' roll music\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfoValid5 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoValid5 - 2", 4, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoValid5 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoValid5 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoValid5 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid5 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfoValid5 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfoValid5 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertTrue("testPersonalInfoValid5 - 9", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfoValid5 - 10", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfoValid5 - 11", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertTrue("testPersonalInfoValid5 - 12", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfoValid5 - 13", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfoValid5 - 14", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());

    }

    @Test
    public void testPersonalInfoValid6() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 2}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 1}, \"text\", \"rock 'n' roll music\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfoValid6 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoValid6 - 2", 4, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoValid6 - 3", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfoValid6 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfoValid6 - 5", jsCard.getPersonalInfo().get("HOBBY-2").ofHighLevel());
        assertTrue("testPersonalInfoValid6 - 6", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoValid6 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoValid6 - 8", jsCard.getPersonalInfo().get("HOBBY-1").ofMediumLevel());
        assertTrue("testPersonalInfoValid6 - 9", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfoValid6 - 10", "r&b music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfoValid6 - 11", jsCard.getPersonalInfo().get("INTEREST-2").ofMediumLevel());
        assertTrue("testPersonalInfoValid6 - 12", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfoValid6 - 13", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfoValid6 - 14", jsCard.getPersonalInfo().get("INTEREST-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfoValid7() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 2}, \"text\", \"rock 'n' roll music\"], " +
                "[\"expertise\", {\"level\": \"beginner\", \"index\": 2}, \"text\", \"chinese literature\"], " +
                "[\"expertise\", {\"level\": \"expert\", \"index\": 1}, \"text\", \"chemistry\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfoValid7 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfoValid7 - 2", 6, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfoValid7 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfoValid7 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfoValid7 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid7 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfoValid7 - 7", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfoValid7 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertTrue("testPersonalInfoValid7 - 9", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfoValid7 - 10", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfoValid7 - 11", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertTrue("testPersonalInfoValid7 - 12", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfoValid7 - 13", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfoValid7 - 14", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());
        assertTrue("testPersonalInfoValid7 - 15", jsCard.getPersonalInfo().get("EXPERTISE-1").asExpertise());
        assertEquals("testPersonalInfoValid7 - 16", "chemistry", jsCard.getPersonalInfo().get("EXPERTISE-1").getValue());
        assertTrue("testPersonalInfoValid7 - 17", jsCard.getPersonalInfo().get("EXPERTISE-1").ofHighLevel());
        assertTrue("testPersonalInfoValid7 - 18", jsCard.getPersonalInfo().get("EXPERTISE-2").asExpertise());
        assertEquals("testPersonalInfoValid7 - 19", "chinese literature", jsCard.getPersonalInfo().get("EXPERTISE-2").getValue());
        assertTrue("testPersonalInfoValid7 - 20", jsCard.getPersonalInfo().get("EXPERTISE-2").ofLowLevel());

    }

}
