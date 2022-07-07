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
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class SpeakToAsTest extends VCard2JSContactTest {

    @Test
    public void testSpeakToAs1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GENDER:M\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAs1 - 1",jsCard.getSpeakToAs().isMale());
        assertTrue("testSpeakToAs1 - 2",jsCard.getSpeakToAs().getPronouns() == null);
        assertTrue("testSpeakToAs1 - 3",jsCard.getExtensions() == null);
    }

    @Test
    public void testSpeakToAs2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GENDER:M;boy\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAs2 - 1",jsCard.getSpeakToAs().isMale());
        assertTrue("testSpeakToAs2 - 2",jsCard.getSpeakToAs().getPronouns() == null);
        assertTrue("testSpeakToAs2 - 3",jsCard.getExtensions().get("ietf.org:rfc6350:GENDER").equals("boy"));
    }

}