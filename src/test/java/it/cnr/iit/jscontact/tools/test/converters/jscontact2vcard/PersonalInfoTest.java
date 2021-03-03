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
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import ezvcard.VCard;
import ezvcard.parameter.ExpertiseLevel;
import ezvcard.parameter.HobbyLevel;
import ezvcard.parameter.InterestLevel;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PersonalInfoTest extends JSContact2VCardTest {

    @Test
    public void testPersonalInfo() throws IOException, CardException {

        String jscard="{" +
                       "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                       "\"fullName\":{\"value\":\"test\"}," +
                       "\"personalInfo\":[ " +
                            "{" +
                                "\"type\": \"expertise\"," +
                                "\"value\": \"chemistry\"," +
                                "\"level\": \"high\" " +
                            "}," +
                            "{" +
                                "\"type\": \"expertise\"," +
                                "\"value\": \"chinese literature\"," +
                                "\"level\": \"low\"" +
                            "}," +
                            "{" +
                                "\"type\": \"hobby\"," +
                                "\"value\": \"reading\"," +
                                "\"level\": \"high\"" +
                            "}," +
                            "{" +
                                "\"type\": \"hobby\"," +
                                "\"value\": \"sewing\"," +
                                "\"level\": \"high\"" +
                            "}," +
                            "{" +
                                "\"type\": \"interest\"," +
                                "\"value\": \"r&b music\"," +
                                "\"level\": \"medium\" " +
                            "}," +
                            "{" +
                                "\"type\": \"interest\"," +
                                "\"value\": \"rock 'n' roll music\"," +
                                "\"level\": \"high\" " +
                            "}" +
                       "]" +
                       "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPersonalInfo - 1",vcard.getExpertise().size() == 2);
        assertTrue("testPersonalInfo - 2",vcard.getExpertise().get(0).getValue().equals("chemistry"));
        assertTrue("testPersonalInfo - 3",vcard.getExpertise().get(0).getLevel() == ExpertiseLevel.EXPERT);
        assertTrue("testPersonalInfo - 4",vcard.getExpertise().get(1).getValue().equals("chinese literature"));
        assertTrue("testPersonalInfo - 5",vcard.getExpertise().get(1).getLevel() == ExpertiseLevel.BEGINNER);
        assertTrue("testPersonalInfo - 6",vcard.getHobbies().size() == 2);
        assertTrue("testPersonalInfo - 7",vcard.getHobbies().get(0).getValue().equals("reading"));
        assertTrue("testPersonalInfo - 8",vcard.getHobbies().get(0).getLevel() == HobbyLevel.HIGH);
        assertTrue("testPersonalInfo - 9",vcard.getHobbies().get(1).getValue().equals("sewing"));
        assertTrue("testPersonalInfo - 10",vcard.getHobbies().get(1).getLevel() == HobbyLevel.HIGH);
        assertTrue("testPersonalInfo - 11",vcard.getInterests().size() == 2);
        assertTrue("testPersonalInfo - 12",vcard.getInterests().get(0).getValue().equals("r&b music"));
        assertTrue("testPersonalInfo - 13",vcard.getInterests().get(0).getLevel() == InterestLevel.MEDIUM);
        assertTrue("testPersonalInfo - 14",vcard.getInterests().get(1).getValue().equals("rock 'n' roll music"));
        assertTrue("testPersonalInfo - 15",vcard.getInterests().get(1).getLevel() == InterestLevel.HIGH);
    }


}
