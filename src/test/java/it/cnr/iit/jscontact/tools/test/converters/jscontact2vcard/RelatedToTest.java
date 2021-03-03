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
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.RelationType;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class RelatedToTest extends JSContact2VCardTest {

    @Test
    public void testRelatedToValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"relatedTo\": { " +
                   "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"relation\":null}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testRelatedToValid1 - 1",vcard.getRelations().size() == 1);
        assertTrue("testRelatedToValid1 - 2",vcard.getRelations().get(0).getUri().equals("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6"));
        assertTrue("testRelatedToValid1 - 3",vcard.getRelations().get(0).getTypes().size() == 0);
    }

    @Test
    public void testRelatedToValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"relatedTo\": { " +
                   "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"relation\": { \"friend\": true } }" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testRelatedToValid2 - 1",vcard.getRelations().size() == 1);
        assertTrue("testRelatedToValid2 - 2",vcard.getRelations().get(0).getUri().equals("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6"));
        assertTrue("testRelatedToValid2 - 3",vcard.getRelations().get(0).getTypes().size() == 1);
        assertTrue("testRelatedToValid2 - 4",vcard.getRelations().get(0).getTypes().contains(RelatedType.FRIEND));

    }


    @Test
    public void testRelatedToValid3() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"relatedTo\": { " +
                    "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"relation\": { \"friend\": true , \"colleague\": true } }," +
                    "\"http://example.com/directory/jdoe.vcf\": {\"relation\": { \"contact\": true} }, " +
                    "\"Please contact my assistant Jane Doe for any inquiries.\": {\"relation\": null } " +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testRelatedToValid3 - 1",vcard.getRelations().size() == 3);
        assertTrue("testRelatedToValid3 - 2",vcard.getRelations().get(0).getUri().equals("urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6"));
        assertTrue("testRelatedToValid3 - 3",vcard.getRelations().get(0).getTypes().size() == 2);
        assertTrue("testRelatedToValid3 - 4",vcard.getRelations().get(0).getTypes().contains(RelatedType.FRIEND));
        assertTrue("testRelatedToValid3 - 5",vcard.getRelations().get(0).getTypes().contains(RelatedType.COLLEAGUE));
        assertTrue("testRelatedToValid3 - 6",vcard.getRelations().get(1).getUri().equals("http://example.com/directory/jdoe.vcf"));
        assertTrue("testRelatedToValid3 - 7",vcard.getRelations().get(1).getTypes().size() == 1);
        assertTrue("testRelatedToValid3 - 8",vcard.getRelations().get(1).getTypes().contains(RelatedType.CONTACT));
        assertTrue("testRelatedToValid3 - 9",vcard.getRelations().get(2).getText().equals("Please contact my assistant Jane Doe for any inquiries."));

    }

    @Test
    public void testRelatedToValid4() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"relatedTo\": { " +
                    "\"freetext\": {\"relation\":null}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testRelatedToValid4 - 1",vcard.getRelations().size() == 1);
        assertTrue("testRelatedToValid4 - 2",vcard.getRelations().get(0).getText().equals("freetext"));
        assertTrue("testRelatedToValid4 - 3",vcard.getRelations().get(0).getTypes().size() == 0);
    }

}
