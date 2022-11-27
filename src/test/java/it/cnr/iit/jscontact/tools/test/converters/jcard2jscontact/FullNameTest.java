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
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FullNameTest extends JCard2JSContactTest {

    @Test
    public void testEmptyFullName() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"\"] " +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertTrue("testEmptyFullName - 1",jsCard.getFullName().isEmpty());

    }

    @Test
    public void testFullName1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"] " +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testFullName1 - 1", "John Q. Public, Esq.", jsCard.getFullName());

    }

    @Test
    public void testFullName2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {\"language\":\"jp\", \"altid\" : \"1\"}, \"text\", \"大久保 正仁\"], " +
                "[\"fn\", {\"language\":\"en\", \"altid\" : \"1\"}, \"text\", \"Okubo Masahito\"] " +
                "]]";
        JCard2JSContact jCard2JSContact = JCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("jp").build()).build();

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testFullName2 - 1", "大久保 正仁", jsCard.getFullName());
        assertEquals("testFullName2 - 2", "Okubo Masahito", jsCard.getLocalizations().get("en").get("fullName").asText());

    }

    @Test
    public void testFullName3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {\"language\":\"jp\", \"altid\" : \"1\"}, \"text\", \"大久保 正仁\"], " +
                "[\"fn\", {\"language\":\"en\", \"altid\" : \"1\"}, \"text\", \"Okubo Masahito\"] " +
                "]]";
        JCard2JSContact jCard2JSContact = JCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("en").build()).build();

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testFullName3 - 1", "Okubo Masahito", jsCard.getFullName());
        assertEquals("testFullName3 - 2", "大久保 正仁", jsCard.getLocalizations().get("jp").get("fullName").asText());

    }
}
