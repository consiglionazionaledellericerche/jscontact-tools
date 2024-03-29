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
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PersonalInfoTest extends JSContact2VCardTest {

    @Test
    public void testPersonalInfo() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"personalInfo\":{ " +
                "\"PERSINFO-1\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"kind\": \"expertise\"," +
                "\"value\": \"chemistry\"," +
                "\"level\": \"high\" " +
                "}," +
                "\"PERSINFO-2\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"kind\": \"expertise\"," +
                "\"value\": \"chinese literature\"," +
                "\"level\": \"low\"" +
                "}," +
                "\"PERSINFO-3\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"kind\": \"hobby\"," +
                "\"value\": \"reading\"," +
                "\"level\": \"high\"" +
                "}," +
                "\"PERSINFO-4\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"kind\": \"hobby\"," +
                "\"value\": \"sewing\"," +
                "\"level\": \"high\"" +
                "}," +
                "\"PERSINFO-5\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"kind\": \"interest\"," +
                "\"value\": \"r&b music\"," +
                "\"level\": \"medium\" " +
                "}," +
                "\"PERSINFO-6\": {" +
                "\"@type\":\"PersonalInfo\"," +
                "\"kind\": \"interest\"," +
                "\"value\": \"rock 'n' roll music\"," +
                "\"level\": \"high\" " +
                "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPersonalInfo - 1", 2, vcard.getExpertise().size());
        assertEquals("testPersonalInfo - 2", "chemistry", vcard.getExpertise().get(0).getValue());
        assertSame("testPersonalInfo - 3", vcard.getExpertise().get(0).getLevel(), ExpertiseLevel.EXPERT);
        assertEquals("testPersonalInfo - 4", "chinese literature", vcard.getExpertise().get(1).getValue());
        assertSame("testPersonalInfo - 5", vcard.getExpertise().get(1).getLevel(), ExpertiseLevel.BEGINNER);
        assertEquals("testPersonalInfo - 6", 2, vcard.getHobbies().size());
        assertEquals("testPersonalInfo - 7", "reading", vcard.getHobbies().get(0).getValue());
        assertSame("testPersonalInfo - 8", vcard.getHobbies().get(0).getLevel(), HobbyLevel.HIGH);
        assertEquals("testPersonalInfo - 9", "sewing", vcard.getHobbies().get(1).getValue());
        assertSame("testPersonalInfo - 10", vcard.getHobbies().get(1).getLevel(), HobbyLevel.HIGH);
        assertEquals("testPersonalInfo - 11", 2, vcard.getInterests().size());
        assertEquals("testPersonalInfo - 12", "r&b music", vcard.getInterests().get(0).getValue());
        assertSame("testPersonalInfo - 13", vcard.getInterests().get(0).getLevel(), InterestLevel.MEDIUM);
        assertEquals("testPersonalInfo - 14", "rock 'n' roll music", vcard.getInterests().get(1).getValue());
        assertSame("testPersonalInfo - 15", vcard.getInterests().get(1).getLevel(), InterestLevel.HIGH);
        assertEquals("testPersonalInfo - 16", "PERSINFO-1", vcard.getExpertise().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testPersonalInfo - 17", "PERSINFO-2", vcard.getExpertise().get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testPersonalInfo - 18", "PERSINFO-3", vcard.getHobbies().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testPersonalInfo - 19", "PERSINFO-4", vcard.getHobbies().get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testPersonalInfo - 20", "PERSINFO-5", vcard.getInterests().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testPersonalInfo - 21", "PERSINFO-6", vcard.getInterests().get(1).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }


}
