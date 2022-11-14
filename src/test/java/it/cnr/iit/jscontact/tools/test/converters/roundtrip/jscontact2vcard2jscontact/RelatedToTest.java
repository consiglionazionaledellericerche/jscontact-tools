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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.jscontact2vcard2jscontact;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RelatedToTest extends RoundtripTest {

    @Test
    public void testRelatedTo1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"relatedTo\": { " +
                   "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"@type\":\"Relation\",\"relation\":null}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testRelatedTo1 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testRelatedTo2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"relatedTo\": { " +
                   "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"@type\":\"Relation\",\"relation\": { \"friend\": true } }" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testRelatedTo2 - 1", jscard2, Card.toCard(jscard));
    }


    @Test
    public void testRelatedTo3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"relatedTo\": { " +
                    "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"@type\":\"Relation\",\"relation\": { \"friend\": true , \"colleague\": true } }," +
                    "\"http://example.com/directory/jdoe.vcf\": {\"@type\":\"Relation\",\"relation\": { \"contact\": true} }, " +
                    "\"Please contact my assistant Jane Doe for any inquiries.\": {\"@type\":\"Relation\",\"relation\": null } " +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testRelatedTo3 - 1", jscard2, Card.toCard(jscard));
    }

    @Test
    public void testRelatedTo4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"relatedTo\": { " +
                    "\"freetext\": {\"@type\":\"Relation\",\"relation\":null}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testRelatedTo4 - 1", jscard2, Card.toCard(jscard));
    }

}
