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

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PersonalInfoTest extends JCard2JSContactTest {


    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                                  "[\"fn\", {}, \"text\", \"test\"], " +
                                  "[\"hobby\", {\"level\": \"very high\"}, \"text\", \"reading\"]" +
                                 "]]";
        jCard2JSContact.convert(jcard);

    }

    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"expertise\", {\"level\": \"very high\"}, \"text\", \"chemistry\"]" +
                "]]";
        jCard2JSContact.convert(jcard);

    }

    @Test
    public void testPersonalInfoValid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\"}, \"text\", \"reading\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPersonalInfoValid1 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid1 - 2", jsCard.getPersonalInfo().size() == 1);
        assertTrue("testPersonalInfoValid1 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid1 - 4", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid1 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfoValid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\"}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\"}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPersonalInfoValid2 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid2 - 2", jsCard.getPersonalInfo().size() == 2);
        assertTrue("testPersonalInfoValid2 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid2 - 4", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid2 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid2 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertTrue("testPersonalInfoValid2 - 7", jsCard.getPersonalInfo().get("HOBBY-2").getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid2 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());

    }

    @Test
    public void testPersonalInfoValid3() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPersonalInfoValid3 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid3 - 2", jsCard.getPersonalInfo().size() == 2);
        assertTrue("testPersonalInfoValid3 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid3 - 4", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid3 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid3 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertTrue("testPersonalInfoValid3 - 7", jsCard.getPersonalInfo().get("HOBBY-2").getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid3 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());

    }

    @Test
    public void testPersonalInfoValid4() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 2}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"sewing\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPersonalInfoValid4 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid4 - 2", jsCard.getPersonalInfo().size() == 2);
        assertTrue("testPersonalInfoValid4 - 3", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertTrue("testPersonalInfoValid4 - 4", jsCard.getPersonalInfo().get("HOBBY-2").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid4 - 5", jsCard.getPersonalInfo().get("HOBBY-2").ofHighLevel());
        assertTrue("testPersonalInfoValid4 - 6", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid4 - 7", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid4 - 8", jsCard.getPersonalInfo().get("HOBBY-1").ofMediumLevel());

    }


    @Test
    public void testPersonalInfoValid5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 1}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 2}, \"text\", \"rock 'n' roll music\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPersonalInfoValid5 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid5 - 2", jsCard.getPersonalInfo().size() == 4);
        assertTrue("testPersonalInfoValid5 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid5 - 4", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid5 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid5 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertTrue("testPersonalInfoValid5 - 7", jsCard.getPersonalInfo().get("HOBBY-2").getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid5 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertTrue("testPersonalInfoValid5 - 9", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertTrue("testPersonalInfoValid5 - 10", jsCard.getPersonalInfo().get("INTEREST-1").getValue().equals("r&b music"));
        assertTrue("testPersonalInfoValid5 - 11", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertTrue("testPersonalInfoValid5 - 12", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertTrue("testPersonalInfoValid5 - 13", jsCard.getPersonalInfo().get("INTEREST-2").getValue().equals("rock 'n' roll music"));
        assertTrue("testPersonalInfoValid5 - 14", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());

    }

    @Test
    public void testPersonalInfoValid6() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"hobby\", {\"level\": \"high\", \"index\": 2}, \"text\", \"reading\"], " +
                "[\"hobby\", {\"level\": \"medium\", \"index\": 1}, \"text\", \"sewing\"], " +
                "[\"interest\", {\"level\": \"medium\", \"index\": 2}, \"text\", \"r&b music\"], " +
                "[\"interest\", {\"level\": \"high\", \"index\": 1}, \"text\", \"rock 'n' roll music\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPersonalInfoValid6 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid6 - 2", jsCard.getPersonalInfo().size() == 4);
        assertTrue("testPersonalInfoValid6 - 3", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertTrue("testPersonalInfoValid6 - 4", jsCard.getPersonalInfo().get("HOBBY-2").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid6 - 5", jsCard.getPersonalInfo().get("HOBBY-2").ofHighLevel());
        assertTrue("testPersonalInfoValid6 - 6", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid6 - 7", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid6 - 8", jsCard.getPersonalInfo().get("HOBBY-1").ofMediumLevel());
        assertTrue("testPersonalInfoValid6 - 9", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertTrue("testPersonalInfoValid6 - 10", jsCard.getPersonalInfo().get("INTEREST-2").getValue().equals("r&b music"));
        assertTrue("testPersonalInfoValid6 - 11", jsCard.getPersonalInfo().get("INTEREST-2").ofMediumLevel());
        assertTrue("testPersonalInfoValid6 - 12", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertTrue("testPersonalInfoValid6 - 13", jsCard.getPersonalInfo().get("INTEREST-1").getValue().equals("rock 'n' roll music"));
        assertTrue("testPersonalInfoValid6 - 14", jsCard.getPersonalInfo().get("INTEREST-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfoValid7() throws IOException, CardException {

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
        assertTrue("testPersonalInfoValid7 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid7 - 2", jsCard.getPersonalInfo().size() == 6);
        assertTrue("testPersonalInfoValid7 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid7 - 4", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid7 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());
        assertTrue("testPersonalInfoValid7 - 6", jsCard.getPersonalInfo().get("HOBBY-2").asHobby());
        assertTrue("testPersonalInfoValid7 - 7", jsCard.getPersonalInfo().get("HOBBY-2").getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid7 - 8", jsCard.getPersonalInfo().get("HOBBY-2").ofMediumLevel());
        assertTrue("testPersonalInfoValid7 - 9", jsCard.getPersonalInfo().get("INTEREST-1").asInterest());
        assertTrue("testPersonalInfoValid7 - 10", jsCard.getPersonalInfo().get("INTEREST-1").getValue().equals("r&b music"));
        assertTrue("testPersonalInfoValid7 - 11", jsCard.getPersonalInfo().get("INTEREST-1").ofMediumLevel());
        assertTrue("testPersonalInfoValid7 - 12", jsCard.getPersonalInfo().get("INTEREST-2").asInterest());
        assertTrue("testPersonalInfoValid7 - 13", jsCard.getPersonalInfo().get("INTEREST-2").getValue().equals("rock 'n' roll music"));
        assertTrue("testPersonalInfoValid7 - 14", jsCard.getPersonalInfo().get("INTEREST-2").ofHighLevel());
        assertTrue("testPersonalInfoValid7 - 15", jsCard.getPersonalInfo().get("EXPERTISE-1").asExpertise());
        assertTrue("testPersonalInfoValid7 - 16", jsCard.getPersonalInfo().get("EXPERTISE-1").getValue().equals("chemistry"));
        assertTrue("testPersonalInfoValid7 - 17", jsCard.getPersonalInfo().get("EXPERTISE-1").ofHighLevel());
        assertTrue("testPersonalInfoValid7 - 18", jsCard.getPersonalInfo().get("EXPERTISE-2").asExpertise());
        assertTrue("testPersonalInfoValid7 - 19", jsCard.getPersonalInfo().get("EXPERTISE-2").getValue().equals("chinese literature"));
        assertTrue("testPersonalInfoValid7 - 20", jsCard.getPersonalInfo().get("EXPERTISE-2").ofLowLevel());

    }

}
