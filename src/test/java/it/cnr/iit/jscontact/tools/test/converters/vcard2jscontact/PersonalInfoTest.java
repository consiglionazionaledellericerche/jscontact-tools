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

import static org.junit.Assert.assertTrue;

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
    public void testPersonalInfoValid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high:reading\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid1 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid1 - 2", jsCard.getPersonalInfo().size() == 1);
        assertTrue("testPersonalInfoValid1 - 3", jsCard.getPersonalInfo().get("HOBBY-1").asHobby());
        assertTrue("testPersonalInfoValid1 - 4", jsCard.getPersonalInfo().get("HOBBY-1").getValue().equals("reading"));
        assertTrue("testPersonalInfoValid1 - 5", jsCard.getPersonalInfo().get("HOBBY-1").ofHighLevel());

    }

    @Test
    public void testPersonalInfoValid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high:reading\n" +
                "HOBBY;LEVEL=medium:sewing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
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
    public void testPersonalInfoValid3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
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
    public void testPersonalInfoValid4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=2:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=1:sewing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
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
    public void testPersonalInfoValid5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=1:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=2:rock 'n' roll music\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
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
    public void testPersonalInfoValid6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=2:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=1:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=2:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=1:rock 'n' roll music\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
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
    public void testPersonalInfoValid7() throws CardException {

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
