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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RelatedToTest extends JCard2JSContactTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRelatedToInvalid() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                                  "[\"fn\", {}, \"text\", \"test\"], " +
                                  "[\"related\", {\"type\":\"teammate\"}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"]" +
                                 "]]";
        jCard2JSContact.convert(jcard);

    }

    @Test
    public void testRelatedTo1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"related\", {}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testRelatedTo1 - 1", 1, jsCard.getRelatedTo().size());
        assertEquals("testRelatedTo1 - 2", 0, jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getRelation().size());

    }

    @Test
    public void testRelatedTo2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"related\", {\"type\": \"friend\"}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testRelatedTo2 - 1", 1, jsCard.getRelatedTo().size());
        assertEquals("testRelatedTo2 - 2", 1, jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getRelation().size());
        assertTrue("testRelatedTo2 - 3",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").asFriend());

    }


    @Test
    public void testRelatedTo3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"related\", {\"type\": \"friend\"}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"], " +
                "[\"related\", {\"type\": \"contact\"}, \"uri\", \"http://example.com/directory/jdoe.vcf\"], " +
                "[\"related\", { }, \"text\", \"Please contact my assistant Jane Doe for any inquiries.\"] " +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testRelatedTo3 - 1", 3, jsCard.getRelatedTo().size());
        assertEquals("testRelatedTo3 - 2", 1, jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getRelation().size());
        assertTrue("testRelatedTo3 - 3",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").asFriend());
        assertEquals("testRelatedTo3 - 4", 1, jsCard.getRelatedTo().get("http://example.com/directory/jdoe.vcf").getRelation().size());
        assertTrue("testRelatedTo3 - 5",jsCard.getRelatedTo().get("http://example.com/directory/jdoe.vcf").asContact());
        assertEquals("testRelatedTo3 - 6", 0, jsCard.getRelatedTo().get("Please contact my assistant Jane Doe for any inquiries.").getRelation().size());

    }

}
