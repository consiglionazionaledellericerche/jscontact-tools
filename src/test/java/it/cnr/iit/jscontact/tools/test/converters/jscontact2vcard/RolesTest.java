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
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class RolesTest extends JSContact2VCardTest {

    @Test
    public void testRoles1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"roles\":[" +
                    "{" +
                        "\"value\": \"Project Leader\"," +
                        "\"localizations\": { \"it\":\"Capo Progetto\" }" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testRoles1 - 1",vcard.getRoles().size() == 2);
        assertTrue("testRoles1 - 2",vcard.getRoles().get(0).getValue().equals("Project Leader"));
        assertTrue("testRoles1 - 3",vcard.getRoles().get(0).getLanguage() == null);
        assertTrue("testRoles1 - 4",vcard.getRoles().get(0).getAltId().equals("1"));
        assertTrue("testRoles1 - 5",vcard.getRoles().get(1).getValue().equals("Capo Progetto"));
        assertTrue("testRoles1 - 6",vcard.getRoles().get(1).getLanguage().equals("it"));
        assertTrue("testRoles1 - 7",vcard.getRoles().get(1).getAltId().equals("1"));
    }

    @Test
    public void testRoles2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"roles\":[" +
                    "{" +
                        "\"value\": \"Project Leader\"," +
                        "\"localizations\": { \"it\":\"Capo Progetto\" }" +
                    "}," +
                    "{" +
                        "\"value\": \"IETF Area Director\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testRoles2 - 1",vcard.getRoles().size() == 3);
        assertTrue("testRoles2 - 2",vcard.getRoles().get(0).getValue().equals("Project Leader"));
        assertTrue("testRoles2 - 3",vcard.getRoles().get(0).getLanguage() == null);
        assertTrue("testRoles2 - 4",vcard.getRoles().get(0).getAltId().equals("1"));
        assertTrue("testRoles2 - 5",vcard.getRoles().get(1).getValue().equals("Capo Progetto"));
        assertTrue("testRoles2 - 6",vcard.getRoles().get(1).getLanguage().equals("it"));
        assertTrue("testRoles2 - 7",vcard.getRoles().get(1).getAltId().equals("1"));
        assertTrue("testRoles2 - 8",vcard.getRoles().get(2).getValue().equals("IETF Area Director"));
        assertTrue("testRoles2 - 9",vcard.getRoles().get(2).getLanguage() == null);
        assertTrue("testRoles2 - 10",vcard.getRoles().get(2).getAltId() == null);
    }

    @Test
    public void testRoles3() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"roles\":[" +
                    "{" +
                        "\"value\": \"Project Leader\"," +
                        "\"localizations\": { \"it\":\"Capo Progetto\" }" +
                    "}," +
                    "{" +
                        "\"value\": \"IETF Area Director\"," +
                        "\"localizations\": { \"it\":\"Direttore Area IETF\" }" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testRoles3 - 1",vcard.getRoles().size() == 4);
        assertTrue("testRoles3 - 2",vcard.getRoles().get(0).getValue().equals("Project Leader"));
        assertTrue("testRoles3 - 3",vcard.getRoles().get(0).getLanguage() == null);
        assertTrue("testRoles3 - 4",vcard.getRoles().get(0).getAltId().equals("1"));
        assertTrue("testRoles3 - 5",vcard.getRoles().get(1).getValue().equals("Capo Progetto"));
        assertTrue("testRoles3 - 6",vcard.getRoles().get(1).getLanguage().equals("it"));
        assertTrue("testRoles3 - 7",vcard.getRoles().get(1).getAltId().equals("1"));
        assertTrue("testRoles3 - 8",vcard.getRoles().get(2).getValue().equals("IETF Area Director"));
        assertTrue("testRoles3 - 9",vcard.getRoles().get(2).getLanguage() == null);
        assertTrue("testRoles3 - 10",vcard.getRoles().get(2).getAltId().equals("2"));
        assertTrue("testRoles3 - 11",vcard.getRoles().get(3).getValue().equals("Direttore Area IETF"));
        assertTrue("testRoles3 - 12",vcard.getRoles().get(3).getLanguage().equals("it"));
        assertTrue("testRoles3 - 13",vcard.getRoles().get(3).getAltId().equals("2"));
    }




}
