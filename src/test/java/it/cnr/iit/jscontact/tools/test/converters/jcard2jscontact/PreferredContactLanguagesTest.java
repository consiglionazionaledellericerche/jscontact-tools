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
import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PreferredContactLanguagesTest extends JCard2JSContactTest {

    @Test
    public void testPreferredContactLanguages1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[ \"lang\", { \"pref\": \"1\" }, \"language-tag\", \"jp\" ], " +
                "[ \"lang\", { \"pref\": \"2\" }, \"language-tag\", \"en\" ] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPreferredContactLanguages1 - 1", jsCard.getPreferredContactLanguages());
        assertEquals("testPreferredContactLanguages1 - 2", 2, jsCard.getPreferredContactLanguages().size());
        assertEquals("testPreferredContactLanguages1 - 3", 1, (int) jsCard.getPreferredContactLanguages().get("jp")[0].getPref());
        assertEquals("testPreferredContactLanguages1 - 4", 2, (int) jsCard.getPreferredContactLanguages().get("en")[0].getPref());
    }


    @Test
    public void testPreferredContactLanguages2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[ \"lang\", { \"type\": \"work\", \"pref\": \"1\" }, \"language-tag\", \"en\" ], " +
                "[ \"lang\", { \"type\": \"work\", \"pref\": \"2\" }, \"language-tag\", \"fr\" ], " +
                "[ \"lang\", { \"type\": \"home\" }, \"language-tag\", \"fr\" ] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testPreferredContactLanguages2 - 1", jsCard.getPreferredContactLanguages());
        assertEquals("testPreferredContactLanguages2 - 2", 2, jsCard.getPreferredContactLanguages().size());
        assertEquals("testPreferredContactLanguages2 - 3", 1, (int) jsCard.getPreferredContactLanguages().get("en")[0].getPref());
        assertSame("testPreferredContactLanguages2 - 4",jsCard.getPreferredContactLanguages().get("en")[0].getContexts().get(Context.work()), Boolean.TRUE);
        assertEquals("testPreferredContactLanguages2 - 5", 2, (int) jsCard.getPreferredContactLanguages().get("fr")[0].getPref());
        assertSame("testPreferredContactLanguages2 - 6",jsCard.getPreferredContactLanguages().get("fr")[0].getContexts().get(Context.work()), Boolean.TRUE);
        assertSame("testPreferredContactLanguages2 - 7",jsCard.getPreferredContactLanguages().get("fr")[1].getContexts().get(Context.private_()), Boolean.TRUE);
    }


}
