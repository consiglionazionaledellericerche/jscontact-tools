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
    public void testPersonalInfo1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\"}, \"text\", \"reading\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfo1 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo1 - 2", 1, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo1 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo1 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo1 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfo2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\"}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\"}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
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

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
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

        String jcard = "[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 2}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfo4 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo4 - 2", 2, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo4 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo4 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo4 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertEquals("testPersonalInfo4 - 6", 2, (int) jsCard.getPersonalInfo().get("HOBBY-1").getListAs());
        assertTrue("testPersonalInfo4 - 7", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo4 - 8", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo4 - 9", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertEquals("testPersonalInfo4 - 10", 1, (int) jsCard.getPersonalInfo().get("HOBBY-2").getListAs());

    }


    @Test
    public void testPersonalInfo5() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 2}, \"text\", \"rock 'n' roll music\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfo5 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo5 - 2", 4, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo5 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo5 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo5 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertEquals("testPersonalInfo5 - 6", 1, (int) jsCard.getPersonalInfo().get("HOBBY-1").getListAs());
        assertTrue("testPersonalInfo5 - 7", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo5 - 8", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo5 - 9", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertEquals("testPersonalInfo5 - 10", 2, (int) jsCard.getPersonalInfo().get("HOBBY-2").getListAs());
        assertTrue("testPersonalInfo5 - 11", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfo5 - 12", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfo5 - 13", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertEquals("testPersonalInfo5 - 14", 1, (int) jsCard.getPersonalInfo().get("INTEREST-1").getListAs());
        assertTrue("testPersonalInfo5 - 15", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfo5 - 16", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfo5 - 17", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());
        assertEquals("testPersonalInfo5 - 18", 2, (int) jsCard.getPersonalInfo().get("INTEREST-2").getListAs());

    }

    @Test
    public void testPersonalInfo6() throws CardException {

        String jcard = "[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 2}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 1}, \"text\", \"rock 'n' roll music\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPersonalInfo6 - 2", 4, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo6 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo6 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo6 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertEquals("testPersonalInfo6 - 6", 2, (int) jsCard.getPersonalInfo().get("HOBBY-1").getListAs());
        assertTrue("testPersonalInfo6 - 7", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo6 - 8", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo6 - 9", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertEquals("testPersonalInfo6 - 10", 1, (int) jsCard.getPersonalInfo().get("HOBBY-2").getListAs());
        assertTrue("testPersonalInfo6 - 11", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfo6 - 12", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfo6 - 13", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertEquals("testPersonalInfo6 - 14", 2, (int) jsCard.getPersonalInfo().get("INTEREST-1").getListAs());
        assertTrue("testPersonalInfo6 - 15", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfo6 - 16", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfo6 - 17", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());
        assertEquals("testPersonalInfo6 - 18", 1, (int) jsCard.getPersonalInfo().get("INTEREST-2").getListAs());

    }

    @Test
    public void testPersonalInfo7() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 2}, \"text\", \"rock 'n' roll music\"], " +
                "[\"expertise\", {\"level\": \"beginner\", \"index\": 2}, \"text\", \"chinese literature\"], " +
                "[\"expertise\", {\"level\": \"expert\", \"index\": 1}, \"text\", \"chemistry\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPersonalInfo7 - 1", jsCard.getPersonalInfo());
        assertEquals("testPersonalInfo7 - 2", 6, jsCard.getPersonalInfo().size());
        assertTrue("testPersonalInfo7 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertEquals("testPersonalInfo7 - 4", "reading", jsCard.getPersonalInfo().get("HOBBY-1").getValue());
        assertTrue("testPersonalInfo7 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertEquals("testPersonalInfo7 - 6", 1, (int) jsCard.getPersonalInfo().get("HOBBY-1").getListAs());
        assertTrue("testPersonalInfo7 - 7", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertEquals("testPersonalInfo7 - 8", "sewing", jsCard.getPersonalInfo().get("HOBBY-2").getValue());
        assertTrue("testPersonalInfo7 - 9", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertEquals("testPersonalInfo7 - 10", 2, (int) jsCard.getPersonalInfo().get("HOBBY-2").getListAs());
        assertTrue("testPersonalInfo7 - 11", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertEquals("testPersonalInfo7 - 12", "r&b music", jsCard.getPersonalInfo().get("INTEREST-1").getValue());
        assertTrue("testPersonalInfo7 - 13", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertEquals("testPersonalInfo7 - 14", 1, (int) jsCard.getPersonalInfo().get("INTEREST-1").getListAs());
        assertTrue("testPersonalInfo7 - 15", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertEquals("testPersonalInfo7 - 16", "rock 'n' roll music", jsCard.getPersonalInfo().get("INTEREST-2").getValue());
        assertTrue("testPersonalInfo7 - 17", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());
        assertEquals("testPersonalInfo7 - 18", 2, (int) jsCard.getPersonalInfo().get("INTEREST-2").getListAs());
        assertTrue("testPersonalInfo7 - 19", jsCard.getPersonalInfo().get("EXPERTISE-1").asExpertise());
        assertEquals("testPersonalInfo7 - 20", "chinese literature", jsCard.getPersonalInfo().get("EXPERTISE-1").getValue());
        assertTrue("testPersonalInfo7 - 21", jsCard.getPersonalInfo().get("EXPERTISE-1").ofLowLevel());
        assertEquals("testPersonalInfo7 - 22", 2, (int) jsCard.getPersonalInfo().get("EXPERTISE-1").getListAs());
        assertTrue("testPersonalInfo7 - 23", jsCard.getPersonalInfo().get("EXPERTISE-2").asExpertise());
        assertEquals("testPersonalInfo7 - 24", "chemistry", jsCard.getPersonalInfo().get("EXPERTISE-2").getValue());
        assertTrue("testPersonalInfo7 - 25", jsCard.getPersonalInfo().get("EXPERTISE-2").ofHighLevel());
        assertEquals("testPersonalInfo7 - 26", 1, (int) jsCard.getPersonalInfo().get("EXPERTISE-2").getListAs());

    }

}
