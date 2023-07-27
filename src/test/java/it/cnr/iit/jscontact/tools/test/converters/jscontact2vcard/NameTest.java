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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedAddressScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedStructuredName;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class NameTest extends JSContact2VCardTest {

    @Test
    public void testName1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Public, Esq.\"," +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"kind\": \"title\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"kind\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"kind\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"kind\": \"given2\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"kind\": \"credential\" }" +
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
        assertNotNull("testName1 - 2", vcard.getProperty(ExtendedStructuredName.class));
        assertEquals("testName1 - 3", "Public", vcard.getProperty(ExtendedStructuredName.class).getFamily());
        assertEquals("testName1 - 4", "John", vcard.getProperty(ExtendedStructuredName.class).getGiven());
        assertEquals("testName1 - 5", 1, vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().size());
        assertEquals("testName1 - 6", "Quinlan", vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().get(0));
        assertEquals("testName1 - 7", 1, vcard.getProperty(ExtendedStructuredName.class).getPrefixes().size());
        assertEquals("testName1 - 8", "Mr.", vcard.getProperty(ExtendedStructuredName.class).getPrefixes().get(0));
        assertEquals("testName1 - 9", 1, vcard.getProperty(ExtendedStructuredName.class).getSuffixes().size());
        assertEquals("testName1 - 10", "Esq.", vcard.getProperty(ExtendedStructuredName.class).getSuffixes().get(0));
        assertEquals("testName1 - 11", "Public,John", String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,vcard.getProperty(ExtendedStructuredName.class).getSortAs()));
        assertEquals("testName1 - 12", 2, vcard.getNicknames().size());
        assertEquals("testName1 - 13", "Johnny", vcard.getNicknames().get(0).getValues().get(0));
        assertEquals("testName1 - 14", "Joe", vcard.getNicknames().get(1).getValues().get(0));

    }


    @Test
    public void testName2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { }," +
                "\"language\": \"en\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Public, Esq.\"," +
                    "\"components\":[ " +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Mr.\", \"kind\": \"title\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"John\", \"kind\": \"given\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Public\", \"kind\": \"surname\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Quinlan\", \"kind\": \"given2\" }," +
                        "{ \"@type\":\"NameComponent\",\"value\":\"Esq.\", \"kind\": \"credential\" }" +
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
        assertNotNull("testName2 - 2", vcard.getProperty(ExtendedStructuredName.class));
        assertEquals("testName2 - 3", "Public", vcard.getProperty(ExtendedStructuredName.class).getFamily());
        assertEquals("testName2 - 4", "John", vcard.getProperty(ExtendedStructuredName.class).getGiven());
        assertEquals("testName2 - 5", 1, vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().size());
        assertEquals("testName2 - 6", "Quinlan", vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().get(0));
        assertEquals("testName2 - 7", 1, vcard.getProperty(ExtendedStructuredName.class).getPrefixes().size());
        assertEquals("testName2 - 8", "Mr.", vcard.getProperty(ExtendedStructuredName.class).getPrefixes().get(0));
        assertEquals("testName2 - 9", 1, vcard.getProperty(ExtendedStructuredName.class).getSuffixes().size());
        assertEquals("testName2 - 10", "Esq.", vcard.getProperty(ExtendedStructuredName.class).getSuffixes().get(0));
        assertEquals("testName2 - 11", 4, vcard.getNicknames().size());
        assertEquals("testName2 - 12", "Johnny", vcard.getNicknames().get(0).getValues().get(0));
        assertEquals("testName2 - 13", "1", vcard.getNicknames().get(0).getAltId());
        assertEquals("testName2 - 14", "Giovannino", vcard.getNicknames().get(1).getValues().get(0));
        assertEquals("testName2 - 15", "it", vcard.getNicknames().get(1).getLanguage());
        assertEquals("testName2 - 16", "1", vcard.getNicknames().get(1).getAltId());
        assertEquals("testName2 - 17", "Joe", vcard.getNicknames().get(2).getValues().get(0));
        assertEquals("testName2 - 18", "2", vcard.getNicknames().get(2).getAltId());
        assertEquals("testName2 - 19", "Giò", vcard.getNicknames().get(3).getValues().get(0));
        assertEquals("testName2 - 20", "it", vcard.getNicknames().get(3).getLanguage());
        assertEquals("testName2 - 21", "2", vcard.getNicknames().get(3).getAltId());
    }


    @Test
    public void testName3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"language\": \"jp\"," +
                "\"name\":{ " +
                    "\"components\":[ " +
                        "{ \"value\":\"正仁\", \"kind\": \"given\" }," +
                        "{ \"value\":\"大久保\", \"kind\": \"surname\" }" +
                    "] " +
                "}, " +
                "\"localizations\" : {" +
                    "\"en\": {" +
                        "\"name/components\":[ " +
                            "{ \"value\":\"Masahito\", \"kind\": \"given\" }," +
                            "{ \"value\":\"Okubo\", \"kind\": \"surname\" }" +
                        "]" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testName3 - 1", "正仁 大久保", vcard.getFormattedNames().get(0).getValue());
        assertEquals("testName3 - 2", "jp", vcard.getFormattedNames().get(0).getLanguage());
        assertEquals("testName3 - 3", "1", vcard.getFormattedNames().get(0).getAltId());
        assertEquals("testName3 - 4", "Masahito Okubo", vcard.getFormattedNames().get(1).getValue());
        assertEquals("testName3 - 5", "en", vcard.getFormattedNames().get(1).getLanguage());
        assertEquals("testName3 - 6", "1", vcard.getFormattedNames().get(1).getAltId());

        assertNotNull("testName3 - 7", vcard.getStructuredNames());
        assertEquals("testName3 - 8", "大久保", vcard.getProperties(ExtendedStructuredName.class).get(0).getFamily());
        assertEquals("testName3 - 9", "正仁", vcard.getProperties(ExtendedStructuredName.class).get(0).getGiven());
        assertEquals("testName3 - 11", 0, vcard.getProperties(ExtendedStructuredName.class).get(0).getAdditionalNames().size());
        assertEquals("testName3 - 12", 0, vcard.getProperties(ExtendedStructuredName.class).get(0).getPrefixes().size());
        assertEquals("testName3 - 13", 0, vcard.getProperties(ExtendedStructuredName.class).get(0).getSuffixes().size());
        assertEquals("testName3 - 14", "jp", vcard.getProperties(ExtendedStructuredName.class).get(0).getLanguage());
        assertEquals("testName3 - 15", "1", vcard.getProperties(ExtendedStructuredName.class).get(0).getAltId());

        assertEquals("testName3 - 16", "Okubo", vcard.getProperties(ExtendedStructuredName.class).get(1).getFamily());
        assertEquals("testName3 - 17", "Masahito", vcard.getProperties(ExtendedStructuredName.class).get(1).getGiven());
        assertEquals("testName3 - 18", 0, vcard.getProperties(ExtendedStructuredName.class).get(1).getAdditionalNames().size());
        assertEquals("testName3 - 19", 0, vcard.getProperties(ExtendedStructuredName.class).get(1).getPrefixes().size());
        assertEquals("testName3 - 20", 0, vcard.getProperties(ExtendedStructuredName.class).get(1).getSuffixes().size());
        assertEquals("testName3 - 21", "en", vcard.getProperties(ExtendedStructuredName.class).get(1).getLanguage());
        assertEquals("testName3 - 22", "1", vcard.getProperties(ExtendedStructuredName.class).get(1).getAltId());
    }

    @Test
    public void testName4() throws IOException, CardException {


        String jscard = "{" +
                "\"@type\" : \"Card\"," +
                "\"@version\" : \"1.0\"," +
                "\"uid\" : \"e8e5d800-1254-4b2d-b06f-3d6fe7c9290d\"," +
                "\"name\" : { " +
                    "\"@type\" : \"Name\", " +
                    "\"full\" : \"John Paul Philip Stevenson\"," +
                    "\"components\" : [ { " +
                        "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"title\"," +
                            "\"value\" : \"Dr.\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"given\"," +
                            "\"value\" : \"John\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"surname\"," +
                            "\"value\" : \"Stevenson\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"given2\"," +
                            "\"value\" : \"Philip\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"given2\"," +
                            "\"value\" : \"Paul\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"generation\"," +
                            "\"value\" : \"Jr.\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"credential\"," +
                            "\"value\" : \"M.D.\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"credential\"," +
                            "\"value\" : \"A.C.P.\"" +
                        "} ]" +
                    "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testName4 - 1", "John Paul Philip Stevenson", vcard.getFormattedNames().get(0).getValue());
        assertEquals("testName4 - 2", "Stevenson", vcard.getProperty(ExtendedStructuredName.class).getFamily());
        assertEquals("testName4 - 3", "John", vcard.getProperty(ExtendedStructuredName.class).getGiven());
        assertEquals("testName4 - 4", "Philip", vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().get(0));
        assertEquals("testName4 - 5", "Paul", vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().get(1));
        assertEquals("testName4 - 6", "Dr.", vcard.getProperty(ExtendedStructuredName.class).getPrefixes().get(0));
        assertEquals("testName4 - 7", "Jr.", vcard.getProperty(ExtendedStructuredName.class).getGeneration().get(0));
        assertEquals("testName4 - 8", "M.D.", vcard.getProperty(ExtendedStructuredName.class).getSuffixes().get(0));
        assertEquals("testName4 - 9", "A.C.P.", vcard.getProperty(ExtendedStructuredName.class).getSuffixes().get(1));
    }

    @Test
    public void testName5() throws IOException, CardException {


        String jscard = "{" +
                "\"@type\" : \"Card\"," +
                "\"@version\" : \"1.0\"," +
                "\"uid\" : \"e8e5d800-1254-4b2d-b06f-3d6fe7c9290d\"," +
                "\"language\" : \"zh-Hant\"," +
                "\"name\" : { " +
                    "\"@type\" : \"Name\", " +
                    "\"full\" : \"John Paul Philip Stevenson\"," +
                    "\"components\" : [ { " +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"surname\"," +
                            "\"value\" : \"孫\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"given\"," +
                            "\"value\" : \"中山\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"given2\"," +
                            "\"value\" : \"文\"" +
                        "}, {" +
                            "\"@type\" : \"NameComponent\"," +
                            "\"kind\" : \"given2\"," +
                            "\"value\" : \"逸仙\"" +
                    "}]" +
                "}," +
                "\"localizations\" : { " +
                    "\"yue\" : { " +
                        "\"name/components/0/phonetic\" : \"syun1\", " +
                        "\"name/components/1/phonetic\" : \"zung1saan1\", " +
                        "\"name/components/2/phonetic\" : \"man4\", " +
                        "\"name/components/3/phonetic\" : \"jat6sin1\", " +
                        "\"name/phoneticScript\" : \"Latn\", " +
                        "\"name/phoneticSystem\" : \"jyut\" " +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        System.out.println(Ezvcard.write(vcard).register(new ExtendedStructuredNameScribe()).register(new ExtendedAddressScribe()).go());
    }
}
