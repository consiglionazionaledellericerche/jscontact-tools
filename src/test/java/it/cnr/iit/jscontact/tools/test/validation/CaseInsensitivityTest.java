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
package it.cnr.iit.jscontact.tools.test.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.*;


public class CaseInsensitivityTest extends AbstractTest {

    @Test
    public void testInvalidNickName1() throws JsonProcessingException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Public, Esq.\"" +
                "}, " +
                "\"nickNames\": { " +
                    "\"NICK-1\" : {  \"name\": \"Johnny\" }, " +
                    "\"NICK-2\" : {  \"name\": \"Joe\" } " +
                "}" +
                "}";

        Card jsCard = Card.toJSCard(jscard);
        assertFalse("testInvalidNickName1-1", jsCard.isValid());
        assertTrue("testInvalidNickName1-2", jsCard.getValidationMessages().contains("the extension name nickNames differs in case of the IANA registered property nicknames for the object Card"));

    }

    @Test
    public void testInvalidNickName2() throws JsonProcessingException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\":{ " +
                "\"full\": \"Mr. John Q. Public, Esq.\"" +
                "}, " +
                "\"nickname\": {  \"@type\":\"NickName\", \"name\": \"Johnny\" } " +
                "}";

        Card jsCard = Card.toJSCard(jscard);
        assertFalse("testInvalidNickName2-1", jsCard.isValid());
        assertTrue("testInvalidNickName2-2", jsCard.getValidationMessages().contains("the type NickName of extension object differs in case of the IANA registered type Nickname"));

    }



}
