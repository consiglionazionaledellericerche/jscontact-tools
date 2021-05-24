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
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class FullNameTest extends JCard2JSContactTest {

    @Test
    public void testEmptyFullNameValid() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"\"] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testEmptyFullNameValid - 1",jsCard.getFullName().getValue().isEmpty());

    }

    @Test
    public void testFullNameValid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Q. Public, Esq.\"] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testFullNameValid1 - 1",jsCard.getFullName().getValue().equals("John Q. Public, Esq."));

    }

    @Test
    public void testFullNameValid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {\"language\":\"ja\", \"altid\" : \"1\"}, \"text\", \"大久保 正仁\"], " +
                "[\"fn\", {\"language\":\"en\", \"altid\" : \"1\"}, \"text\", \"Okubo Masahito\"] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testFullNameValid2 - 1",jsCard.getFullName().getValue().equals("大久保 正仁"));
        assertTrue("testFullNameValid2 - 2",jsCard.getFullName().getLanguage().equals("ja"));
        assertTrue("testFullNameValid2 - 3",jsCard.getFullName().getLocalizations().get("en").equals("Okubo Masahito"));

    }

}
