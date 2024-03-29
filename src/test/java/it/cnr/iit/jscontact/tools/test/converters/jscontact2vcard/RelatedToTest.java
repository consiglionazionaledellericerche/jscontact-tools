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
import ezvcard.parameter.RelatedType;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RelatedToTest extends JSContact2VCardTest {

    @Test
    public void testRelatedTo1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"relatedTo\": { " +
                   "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"@type\":\"Relation\",\"relation\":null}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testRelatedTo1 - 1", 1, vcard.getRelations().size());
        assertEquals("testRelatedTo1 - 2", "urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6", vcard.getRelations().get(0).getUri());
        assertEquals("testRelatedTo1 - 3", 0, vcard.getRelations().get(0).getTypes().size());
    }

    @Test
    public void testRelatedTo2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"relatedTo\": { " +
                   "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"@type\":\"Relation\",\"relation\": { \"friend\": true } }" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testRelatedTo2 - 1", 1, vcard.getRelations().size());
        assertEquals("testRelatedTo2 - 2", "urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6", vcard.getRelations().get(0).getUri());
        assertEquals("testRelatedTo2 - 3", 1, vcard.getRelations().get(0).getTypes().size());
        assertTrue("testRelatedTo2 - 4",vcard.getRelations().get(0).getTypes().contains(RelatedType.FRIEND));

    }


    @Test
    public void testRelatedTo3() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"relatedTo\": { " +
                    "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"@type\":\"Relation\",\"relation\": { \"friend\": true , \"colleague\": true } }," +
                    "\"http://example.com/directory/jdoe.vcf\": {\"@type\":\"Relation\",\"relation\": { \"contact\": true} }, " +
                    "\"Please contact my assistant Jane Doe for any inquiries.\": {\"@type\":\"Relation\",\"relation\": null } " +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testRelatedTo3 - 1", 3, vcard.getRelations().size());
        assertEquals("testRelatedTo3 - 2", "urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6", vcard.getRelations().get(0).getUri());
        assertEquals("testRelatedTo3 - 3", 2, vcard.getRelations().get(0).getTypes().size());
        assertTrue("testRelatedTo3 - 4",vcard.getRelations().get(0).getTypes().contains(RelatedType.FRIEND));
        assertTrue("testRelatedTo3 - 5",vcard.getRelations().get(0).getTypes().contains(RelatedType.COLLEAGUE));
        assertEquals("testRelatedTo3 - 6", "http://example.com/directory/jdoe.vcf", vcard.getRelations().get(1).getUri());
        assertEquals("testRelatedTo3 - 7", 1, vcard.getRelations().get(1).getTypes().size());
        assertTrue("testRelatedTo3 - 8",vcard.getRelations().get(1).getTypes().contains(RelatedType.CONTACT));
        assertEquals("testRelatedTo3 - 9", "Please contact my assistant Jane Doe for any inquiries.", vcard.getRelations().get(2).getText());

    }

    @Test
    public void testRelatedTo4() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"relatedTo\": { " +
                    "\"freetext\": {\"@type\":\"Relation\",\"relation\":null}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testRelatedTo4 - 1", 1, vcard.getRelations().size());
        assertEquals("testRelatedTo4 - 2", "freetext", vcard.getRelations().get(0).getText());
        assertEquals("testRelatedTo4 - 3", 0, vcard.getRelations().get(0).getTypes().size());
    }

}
