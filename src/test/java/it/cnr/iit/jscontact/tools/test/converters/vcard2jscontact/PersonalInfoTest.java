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

public class PersonalInfoTest extends VCard2JSContactTest {


    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=very high:reading\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard);

    }

    @Test(expected = CardException.class)
    public void testPersonalInfoInvalid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "EXPERTISE;LEVEL=very high:chemistry\n" +
                "END:VCARD";

        vCard2JSContact.convert(vcard);

    }


    @Test
    public void testPersonalInfoValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high:reading\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid1 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid1 - 2", jsCard.getPersonalInfo().length == 1);
        assertTrue("testPersonalInfoValid1 - 3", jsCard.getPersonalInfo()[0].asHobby());
        assertTrue("testPersonalInfoValid1 - 4", jsCard.getPersonalInfo()[0].getValue().equals("reading"));
        assertTrue("testPersonalInfoValid1 - 5", jsCard.getPersonalInfo()[0].ofHighInterest());

    }

    @Test
    public void testPersonalInfoValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high:reading\n" +
                "HOBBY;LEVEL=medium:sewing\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid2 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid2 - 2", jsCard.getPersonalInfo().length == 2);
        assertTrue("testPersonalInfoValid2 - 3", jsCard.getPersonalInfo()[0].asHobby());
        assertTrue("testPersonalInfoValid2 - 4", jsCard.getPersonalInfo()[0].getValue().equals("reading"));
        assertTrue("testPersonalInfoValid2 - 5", jsCard.getPersonalInfo()[0].ofHighInterest());
        assertTrue("testPersonalInfoValid2 - 6", jsCard.getPersonalInfo()[1].asHobby());
        assertTrue("testPersonalInfoValid2 - 7", jsCard.getPersonalInfo()[1].getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid2 - 8", jsCard.getPersonalInfo()[1].ofMediumInterest());

    }

    @Test
    public void testPersonalInfoValid3() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid3 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid3 - 2", jsCard.getPersonalInfo().length == 2);
        assertTrue("testPersonalInfoValid3 - 3", jsCard.getPersonalInfo()[0].asHobby());
        assertTrue("testPersonalInfoValid3 - 4", jsCard.getPersonalInfo()[0].getValue().equals("reading"));
        assertTrue("testPersonalInfoValid3 - 5", jsCard.getPersonalInfo()[0].ofHighInterest());
        assertTrue("testPersonalInfoValid3 - 6", jsCard.getPersonalInfo()[1].asHobby());
        assertTrue("testPersonalInfoValid3 - 7", jsCard.getPersonalInfo()[1].getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid3 - 8", jsCard.getPersonalInfo()[1].ofMediumInterest());

    }

    @Test
    public void testPersonalInfoValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=2:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=1:sewing\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid4 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid4 - 2", jsCard.getPersonalInfo().length == 2);
        assertTrue("testPersonalInfoValid4 - 3", jsCard.getPersonalInfo()[1].asHobby());
        assertTrue("testPersonalInfoValid4 - 4", jsCard.getPersonalInfo()[1].getValue().equals("reading"));
        assertTrue("testPersonalInfoValid4 - 5", jsCard.getPersonalInfo()[1].ofHighInterest());
        assertTrue("testPersonalInfoValid4 - 6", jsCard.getPersonalInfo()[0].asHobby());
        assertTrue("testPersonalInfoValid4 - 7", jsCard.getPersonalInfo()[0].getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid4 - 8", jsCard.getPersonalInfo()[0].ofMediumInterest());

    }


    @Test
    public void testPersonalInfoValid5() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=1:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=2:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=1:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=2:rock 'n' roll music\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid5 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid5 - 2", jsCard.getPersonalInfo().length == 4);
        assertTrue("testPersonalInfoValid5 - 3", jsCard.getPersonalInfo()[0].asHobby());
        assertTrue("testPersonalInfoValid5 - 4", jsCard.getPersonalInfo()[0].getValue().equals("reading"));
        assertTrue("testPersonalInfoValid5 - 5", jsCard.getPersonalInfo()[0].ofHighInterest());
        assertTrue("testPersonalInfoValid5 - 6", jsCard.getPersonalInfo()[1].asHobby());
        assertTrue("testPersonalInfoValid5 - 7", jsCard.getPersonalInfo()[1].getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid5 - 8", jsCard.getPersonalInfo()[1].ofMediumInterest());
        assertTrue("testPersonalInfoValid5 - 9", jsCard.getPersonalInfo()[2].asInterest());
        assertTrue("testPersonalInfoValid5 - 10", jsCard.getPersonalInfo()[2].getValue().equals("r&b music"));
        assertTrue("testPersonalInfoValid5 - 11", jsCard.getPersonalInfo()[2].ofMediumInterest());
        assertTrue("testPersonalInfoValid5 - 12", jsCard.getPersonalInfo()[3].asInterest());
        assertTrue("testPersonalInfoValid5 - 13", jsCard.getPersonalInfo()[3].getValue().equals("rock 'n' roll music"));
        assertTrue("testPersonalInfoValid5 - 14", jsCard.getPersonalInfo()[3].ofHighInterest());

    }

    @Test
    public void testPersonalInfoValid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "HOBBY;LEVEL=high;INDEX=2:reading\n" +
                "HOBBY;LEVEL=medium;INDEX=1:sewing\n" +
                "INTEREST;LEVEL=medium;INDEX=2:r&b music\n" +
                "INTEREST;LEVEL=high;INDEX=1:rock 'n' roll music\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid6 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid6 - 2", jsCard.getPersonalInfo().length == 4);
        assertTrue("testPersonalInfoValid6 - 3", jsCard.getPersonalInfo()[1].asHobby());
        assertTrue("testPersonalInfoValid6 - 4", jsCard.getPersonalInfo()[1].getValue().equals("reading"));
        assertTrue("testPersonalInfoValid6 - 5", jsCard.getPersonalInfo()[1].ofHighInterest());
        assertTrue("testPersonalInfoValid6 - 6", jsCard.getPersonalInfo()[0].asHobby());
        assertTrue("testPersonalInfoValid6 - 7", jsCard.getPersonalInfo()[0].getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid6 - 8", jsCard.getPersonalInfo()[0].ofMediumInterest());
        assertTrue("testPersonalInfoValid6 - 9", jsCard.getPersonalInfo()[3].asInterest());
        assertTrue("testPersonalInfoValid6 - 10", jsCard.getPersonalInfo()[3].getValue().equals("r&b music"));
        assertTrue("testPersonalInfoValid6 - 11", jsCard.getPersonalInfo()[3].ofMediumInterest());
        assertTrue("testPersonalInfoValid6 - 12", jsCard.getPersonalInfo()[2].asInterest());
        assertTrue("testPersonalInfoValid6 - 13", jsCard.getPersonalInfo()[2].getValue().equals("rock 'n' roll music"));
        assertTrue("testPersonalInfoValid6 - 14", jsCard.getPersonalInfo()[2].ofHighInterest());

    }

    @Test
    public void testPersonalInfoValid7() throws IOException, CardException {

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

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPersonalInfoValid7 - 1", jsCard.getPersonalInfo()!=null);
        assertTrue("testPersonalInfoValid7 - 2", jsCard.getPersonalInfo().length == 6);
        assertTrue("testPersonalInfoValid7 - 3", jsCard.getPersonalInfo()[0].asHobby());
        assertTrue("testPersonalInfoValid7 - 4", jsCard.getPersonalInfo()[0].getValue().equals("reading"));
        assertTrue("testPersonalInfoValid7 - 5", jsCard.getPersonalInfo()[0].ofHighInterest());
        assertTrue("testPersonalInfoValid7 - 6", jsCard.getPersonalInfo()[1].asHobby());
        assertTrue("testPersonalInfoValid7 - 7", jsCard.getPersonalInfo()[1].getValue().equals("sewing"));
        assertTrue("testPersonalInfoValid7 - 8", jsCard.getPersonalInfo()[1].ofMediumInterest());
        assertTrue("testPersonalInfoValid7 - 9", jsCard.getPersonalInfo()[2].asInterest());
        assertTrue("testPersonalInfoValid7 - 10", jsCard.getPersonalInfo()[2].getValue().equals("r&b music"));
        assertTrue("testPersonalInfoValid7 - 11", jsCard.getPersonalInfo()[2].ofMediumInterest());
        assertTrue("testPersonalInfoValid7 - 12", jsCard.getPersonalInfo()[3].asInterest());
        assertTrue("testPersonalInfoValid7 - 13", jsCard.getPersonalInfo()[3].getValue().equals("rock 'n' roll music"));
        assertTrue("testPersonalInfoValid7 - 14", jsCard.getPersonalInfo()[3].ofHighInterest());
        assertTrue("testPersonalInfoValid7 - 15", jsCard.getPersonalInfo()[4].asExpertise());
        assertTrue("testPersonalInfoValid7 - 16", jsCard.getPersonalInfo()[4].getValue().equals("chemistry"));
        assertTrue("testPersonalInfoValid7 - 17", jsCard.getPersonalInfo()[4].ofHighInterest());
        assertTrue("testPersonalInfoValid7 - 18", jsCard.getPersonalInfo()[5].asExpertise());
        assertTrue("testPersonalInfoValid7 - 19", jsCard.getPersonalInfo()[5].getValue().equals("chinese literature"));
        assertTrue("testPersonalInfoValid7 - 20", jsCard.getPersonalInfo()[5].ofLowInterest());

    }

}
