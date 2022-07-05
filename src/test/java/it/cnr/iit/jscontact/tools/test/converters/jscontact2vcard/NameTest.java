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

public class NameTest extends JSContact2VCardTest {

    @Test
    public void testNameValid1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\": \"Mr. John Q. Public, Esq.\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"type\": \"prefix\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"type\": \"personal\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"type\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"type\": \"additional\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"type\": \"suffix\" }" +
                    "] " +
                "}, " +
                "\"nickNames\":[ \"Johnny\", \"Joe\" ]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testNameValid1 - 1",vcard.getFormattedName().getValue().equals("Mr. John Q. Public, Esq."));
        assertTrue("testNameValid1 - 2",vcard.getStructuredName() != null);
        assertTrue("testNameValid1 - 3",vcard.getStructuredName().getFamily().equals("Public"));
        assertTrue("testNameValid1 - 4",vcard.getStructuredName().getGiven().equals("John"));
        assertTrue("testNameValid1 - 5",vcard.getStructuredName().getAdditionalNames().size() == 1);
        assertTrue("testNameValid1 - 6",vcard.getStructuredName().getAdditionalNames().get(0).equals("Quinlan"));
        assertTrue("testNameValid1 - 7",vcard.getStructuredName().getPrefixes().size() == 1);
        assertTrue("testNameValid1 - 8",vcard.getStructuredName().getPrefixes().get(0).equals("Mr."));
        assertTrue("testNameValid1 - 9",vcard.getStructuredName().getSuffixes().size() == 1);
        assertTrue("testNameValid1 - 10",vcard.getStructuredName().getSuffixes().get(0).equals("Esq."));
        assertTrue("testNameValid1 - 11",vcard.getNickname().getValues().size() == 2);
        assertTrue("testNameValid1 - 12",vcard.getNickname().getValues().get(0).equals("Johnny"));
        assertTrue("testNameValid1 - 13",vcard.getNickname().getValues().get(1).equals("Joe"));

    }


    @Test
    public void testnameValid2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\": \"Mr. John Q. Public, Esq.\"," +
                "\"language\": \"en\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"type\": \"prefix\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"type\": \"personal\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"type\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"type\": \"additional\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"type\": \"suffix\" }" +
                    "] " +
                "}, " +
                "\"nickNames\":[ \"Johnny\", \"Joe\" ], " +
                "\"localizations\": { " +
                    "\"it\" : { " +
                          "\"nickNames\" : [ \"Giovanni\", \"Giò\" ]" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testnameValid2 - 1",vcard.getFormattedName().getValue().equals("Mr. John Q. Public, Esq."));
        assertTrue("testnameValid2 - 2",vcard.getStructuredName() != null);
        assertTrue("testnameValid2 - 3",vcard.getStructuredName().getFamily().equals("Public"));
        assertTrue("testnameValid2 - 4",vcard.getStructuredName().getGiven().equals("John"));
        assertTrue("testnameValid2 - 5",vcard.getStructuredName().getAdditionalNames().size() == 1);
        assertTrue("testnameValid2 - 6",vcard.getStructuredName().getAdditionalNames().get(0).equals("Quinlan"));
        assertTrue("testnameValid2 - 7",vcard.getStructuredName().getPrefixes().size() == 1);
        assertTrue("testnameValid2 - 8",vcard.getStructuredName().getPrefixes().get(0).equals("Mr."));
        assertTrue("testnameValid2 - 9",vcard.getStructuredName().getSuffixes().size() == 1);
        assertTrue("testnameValid2 - 10",vcard.getStructuredName().getSuffixes().get(0).equals("Esq."));
        assertTrue("testnameValid2 - 11",vcard.getNickname().getValues().size() == 2);
        assertTrue("testnameValid2 - 12",vcard.getNicknames().get(0).getValues().get(0).equals("Johnny"));
        assertTrue("testnameValid2 - 13",vcard.getNicknames().get(0).getValues().get(1).equals("Joe"));
        assertTrue("testnameValid2 - 14",vcard.getNicknames().get(0).getLanguage().equals("en"));
        assertTrue("testnameValid2 - 15",vcard.getNicknames().get(0).getAltId().equals("1"));
        assertTrue("testnameValid2 - 16",vcard.getNicknames().get(1).getValues().get(0).equals("Giovanni"));
        assertTrue("testnameValid2 - 17",vcard.getNicknames().get(1).getValues().get(1).equals("Giò"));
        assertTrue("testnameValid2 - 18",vcard.getNicknames().get(1).getLanguage().equals("it"));
        assertTrue("testnameValid2 - 19",vcard.getNicknames().get(1).getAltId().equals("1"));

    }


    @Test
    public void testFullNameValid3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"language\": \"jp\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"正仁\", \"type\": \"personal\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"大久保\", \"type\": \"surname\" }" +
                    "] " +
                "}, " +
                "\"localizations\" : {" +
                    "\"en\": {" +
                        "\"name/components\":[ " +
                            "{ \"@type\":\"NameComponent\", \"value\":\"Masahito\", \"type\": \"personal\" }," +
                            "{ \"@type\":\"NameComponent\", \"value\":\"Okubo\", \"type\": \"surname\" }" +
                        "]" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testNameValid2 - 1",vcard.getFormattedNames().get(0).getValue().equals("正仁 大久保"));
        assertTrue("testNameValid2 - 2",vcard.getFormattedNames().get(0).getLanguage().equals("jp"));
        assertTrue("testNameValid2 - 3",vcard.getFormattedNames().get(0).getAltId().equals("1"));
        assertTrue("testNameValid2 - 4",vcard.getFormattedNames().get(1).getValue().equals("Masahito Okubo"));
        assertTrue("testNameValid2 - 5",vcard.getFormattedNames().get(1).getLanguage().equals("en"));
        assertTrue("testNameValid2 - 6",vcard.getFormattedNames().get(1).getAltId().equals("1"));

        assertTrue("testnameValid2 - 7",vcard.getStructuredNames() != null);
        assertTrue("testnameValid2 - 8",vcard.getStructuredNames().get(0).getFamily().equals("大久保"));
        assertTrue("testnameValid2 - 9",vcard.getStructuredNames().get(0).getGiven().equals("正仁"));
        assertTrue("testnameValid2 - 11",vcard.getStructuredNames().get(0).getAdditionalNames().size() == 0);
        assertTrue("testnameValid2 - 12",vcard.getStructuredNames().get(0).getPrefixes().size() == 0);
        assertTrue("testnameValid2 - 13",vcard.getStructuredNames().get(0).getSuffixes().size() == 0);
        assertTrue("testnameValid2 - 14",vcard.getStructuredNames().get(0).getLanguage().equals("jp"));
        assertTrue("testnameValid2 - 15",vcard.getStructuredNames().get(0).getAltId().equals("1"));

        assertTrue("testnameValid2 - 16",vcard.getStructuredNames().get(1).getFamily().equals("Okubo"));
        assertTrue("testnameValid2 - 17",vcard.getStructuredNames().get(1).getGiven().equals("Masahito"));
        assertTrue("testnameValid2 - 18",vcard.getStructuredNames().get(1).getAdditionalNames().size() == 0);
        assertTrue("testnameValid2 - 19",vcard.getStructuredNames().get(1).getPrefixes().size() == 0);
        assertTrue("testnameValid2 - 20",vcard.getStructuredNames().get(1).getSuffixes().size() == 0);
        assertTrue("testnameValid2 - 21",vcard.getStructuredNames().get(1).getLanguage().equals("en"));
        assertTrue("testnameValid2 - 22",vcard.getStructuredNames().get(1).getAltId().equals("1"));
    }
    

}
