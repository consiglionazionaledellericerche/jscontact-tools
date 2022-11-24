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
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class NameTest extends JSContact2VCardTest {

    @Test
    public void testName1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\": \"Mr. John Q. Public, Esq.\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"type\": \"prefix\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"type\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"type\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"type\": \"middle\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"type\": \"suffix\" }" +
                    "], " +
                    "\"sortAs\": { \"surname\":\"Public\",\"given\":\"John\" }" +
                "}, " +
                "\"nickNames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"NickName\",\"name\": \"Johnny\" }, " +
                    "\"NICK-2\" : {  \"@type\":\"NickName\",\"name\": \"Joe\" } " +
                "}" +
        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testName1 - 1", "Mr. John Q. Public, Esq.", vcard.getFormattedName().getValue());
        assertNotNull("testName1 - 2", vcard.getStructuredName());
        assertEquals("testName1 - 3", "Public", vcard.getStructuredName().getFamily());
        assertEquals("testName1 - 4", "John", vcard.getStructuredName().getGiven());
        assertEquals("testName1 - 5", 1, vcard.getStructuredName().getAdditionalNames().size());
        assertEquals("testName1 - 6", "Quinlan", vcard.getStructuredName().getAdditionalNames().get(0));
        assertEquals("testName1 - 7", 1, vcard.getStructuredName().getPrefixes().size());
        assertEquals("testName1 - 8", "Mr.", vcard.getStructuredName().getPrefixes().get(0));
        assertEquals("testName1 - 9", 1, vcard.getStructuredName().getSuffixes().size());
        assertEquals("testName1 - 10", "Esq.", vcard.getStructuredName().getSuffixes().get(0));
        assertEquals("testName1 - 11", "Public,John", String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,vcard.getStructuredName().getSortAs()));
        assertEquals("testName1 - 12", 2, vcard.getNicknames().size());
        assertEquals("testName1 - 13", "Johnny", vcard.getNicknames().get(0).getValues().get(0));
        assertEquals("testName1 - 14", "Joe", vcard.getNicknames().get(1).getValues().get(0));

    }


    @Test
    public void testName2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\": \"Mr. John Q. Public, Esq.\"," +
                "\"locale\": \"en\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"type\": \"prefix\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"type\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"type\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"type\": \"middle\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"type\": \"suffix\" }" +
                    "] " +
                "}, " +
                "\"nickNames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"NickName\", \"name\": \"Johnny\" }, " +
                    "\"NICK-2\" : {  \"@type\":\"NickName\", \"name\": \"Joe\" } " +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"nickNames/NICK-1\" : {  \"@type\":\"NickName\", \"name\": \"Giovannino\" }, " +
                        "\"nickNames/NICK-2\" : {  \"@type\":\"NickName\", \"name\": \"Giò\" } " +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testName2 - 1", "Mr. John Q. Public, Esq.", vcard.getFormattedName().getValue());
        assertNotNull("testName2 - 2", vcard.getStructuredName());
        assertEquals("testName2 - 3", "Public", vcard.getStructuredName().getFamily());
        assertEquals("testName2 - 4", "John", vcard.getStructuredName().getGiven());
        assertEquals("testName2 - 5", 1, vcard.getStructuredName().getAdditionalNames().size());
        assertEquals("testName2 - 6", "Quinlan", vcard.getStructuredName().getAdditionalNames().get(0));
        assertEquals("testName2 - 7", 1, vcard.getStructuredName().getPrefixes().size());
        assertEquals("testName2 - 8", "Mr.", vcard.getStructuredName().getPrefixes().get(0));
        assertEquals("testName2 - 9", 1, vcard.getStructuredName().getSuffixes().size());
        assertEquals("testName2 - 10", "Esq.", vcard.getStructuredName().getSuffixes().get(0));
        assertEquals("testName2 - 11", 4, vcard.getNicknames().size());
        assertEquals("testName2 - 12", "Johnny", vcard.getNicknames().get(0).getValues().get(0));
        assertEquals("testName2 - 13", "en", vcard.getNicknames().get(0).getLanguage());
        assertEquals("testName2 - 14", "1", vcard.getNicknames().get(0).getAltId());
        assertEquals("testName2 - 15", "Giovannino", vcard.getNicknames().get(1).getValues().get(0));
        assertEquals("testName2 - 16", "it", vcard.getNicknames().get(1).getLanguage());
        assertEquals("testName2 - 17", "1", vcard.getNicknames().get(1).getAltId());
        assertEquals("testName2 - 18", "Joe", vcard.getNicknames().get(2).getValues().get(0));
        assertEquals("testName2 - 19", "en", vcard.getNicknames().get(2).getLanguage());
        assertEquals("testName2 - 20", "2", vcard.getNicknames().get(2).getAltId());
        assertEquals("testName2 - 21", "Giò", vcard.getNicknames().get(3).getValues().get(0));
        assertEquals("testName2 - 22", "it", vcard.getNicknames().get(3).getLanguage());
        assertEquals("testName2 - 23", "2", vcard.getNicknames().get(3).getAltId());
    }


    @Test
    public void testFullName3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"locale\": \"jp\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"正仁\", \"type\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"大久保\", \"type\": \"surname\" }" +
                    "] " +
                "}, " +
                "\"localizations\" : {" +
                    "\"en\": {" +
                        "\"name/components\":[ " +
                            "{ \"@type\":\"NameComponent\", \"value\":\"Masahito\", \"type\": \"given\" }," +
                            "{ \"@type\":\"NameComponent\", \"value\":\"Okubo\", \"type\": \"surname\" }" +
                        "]" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testName2 - 1", "正仁 大久保", vcard.getFormattedNames().get(0).getValue());
        assertEquals("testName2 - 2", "jp", vcard.getFormattedNames().get(0).getLanguage());
        assertEquals("testName2 - 3", "1", vcard.getFormattedNames().get(0).getAltId());
        assertEquals("testName2 - 4", "Masahito Okubo", vcard.getFormattedNames().get(1).getValue());
        assertEquals("testName2 - 5", "en", vcard.getFormattedNames().get(1).getLanguage());
        assertEquals("testName2 - 6", "1", vcard.getFormattedNames().get(1).getAltId());

        assertNotNull("testName2 - 7", vcard.getStructuredNames());
        assertEquals("testName2 - 8", "大久保", vcard.getStructuredNames().get(0).getFamily());
        assertEquals("testName2 - 9", "正仁", vcard.getStructuredNames().get(0).getGiven());
        assertEquals("testName2 - 11", 0, vcard.getStructuredNames().get(0).getAdditionalNames().size());
        assertEquals("testName2 - 12", 0, vcard.getStructuredNames().get(0).getPrefixes().size());
        assertEquals("testName2 - 13", 0, vcard.getStructuredNames().get(0).getSuffixes().size());
        assertEquals("testName2 - 14", "jp", vcard.getStructuredNames().get(0).getLanguage());
        assertEquals("testName2 - 15", "1", vcard.getStructuredNames().get(0).getAltId());

        assertEquals("testName2 - 16", "Okubo", vcard.getStructuredNames().get(1).getFamily());
        assertEquals("testName2 - 17", "Masahito", vcard.getStructuredNames().get(1).getGiven());
        assertEquals("testName2 - 18", 0, vcard.getStructuredNames().get(1).getAdditionalNames().size());
        assertEquals("testName2 - 19", 0, vcard.getStructuredNames().get(1).getPrefixes().size());
        assertEquals("testName2 - 20", 0, vcard.getStructuredNames().get(1).getSuffixes().size());
        assertEquals("testName2 - 21", "en", vcard.getStructuredNames().get(1).getLanguage());
        assertEquals("testName2 - 22", "1", vcard.getStructuredNames().get(1).getAltId());
    }
    

}
