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

import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.RelationType;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class RelatedToTest extends JCard2JSContactTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRelatedToInvalid() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                                  "[\"fn\", {}, \"text\", \"test\"], " +
                                  "[\"related\", {\"type\":\"teammate\"}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"]" +
                                 "]]";
        jCard2JSContact.convert(jcard);

    }

    @Test
    public void testRelatedToValid1() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"related\", {}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testRelatedToValid1 - 1",jsCard.getRelatedTo().size() == 1);
        assertTrue("testRelatedToValid1 - 2",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getRelation().size() == 0);

    }

    @Test
    public void testRelatedToValid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"related\", {\"type\": \"friend\"}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testRelatedToValid2 - 1",jsCard.getRelatedTo().size() == 1);
        assertTrue("testRelatedToValid2 - 2",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getRelation().size()== 1);
        assertTrue("testRelatedToValid2 - 3",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").hasFriend());

    }


    @Test
    public void testRelatedToValid3() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"related\", {\"type\": \"friend\"}, \"uri\", \"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"], " +
                "[\"related\", {\"type\": \"contact\"}, \"uri\", \"http://example.com/directory/jdoe.vcf\"], " +
                "[\"related\", { }, \"text\", \"Please contact my assistant Jane Doe for any inquiries.\"] " +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testRelatedToValid3 - 1",jsCard.getRelatedTo().size() == 3);
        assertTrue("testRelatedToValid3 - 2",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").getRelation().size()== 1);
        assertTrue("testRelatedToValid3 - 3",jsCard.getRelatedTo().get("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6").hasFriend());
        assertTrue("testRelatedToValid3 - 4",jsCard.getRelatedTo().get("http://example.com/directory/jdoe.vcf").getRelation().size()== 1);
        assertTrue("testRelatedToValid3 - 5",jsCard.getRelatedTo().get("http://example.com/directory/jdoe.vcf").hasContact());
        assertTrue("testRelatedToValid3 - 6",jsCard.getRelatedTo().get("Please contact my assistant Jane Doe for any inquiries.").getRelation().size() == 0);

    }

}
