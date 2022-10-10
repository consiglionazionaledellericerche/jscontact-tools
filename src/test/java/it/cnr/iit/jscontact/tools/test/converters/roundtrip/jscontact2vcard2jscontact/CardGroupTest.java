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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.dto.serializers.PrettyPrintSerializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CardGroupTest extends RoundtripTest {

    @Test
    public void testCardGroup1() throws IOException, CardException {

        String jscards = "[" +
                         "{" +
                            "\"@type\":\"CardGroup\"," +
                             "\"uid\":\"2feb4102-f15f-4047-b521-190d4acd0d29\"," +
                             "\"card\": {" +
                                "\"@type\":\"Card\"," +
                                 "\"uid\":\"2feb4102-f15f-4047-b521-190d4acd0d29\"," +
                                 "\"kind\":\"group\"," +
                                 "\"fullName\": \"The Doe family\"" +
                             "}," +
                             "\"members\": {" +
                                "\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\":true," +
                                "\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\":true" +
                             "}" +
                        "}," +
                        "{" +
                            "\"@type\":\"Card\"," +
                            "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                            "\"fullName\":\"John Doe\"" +
                        "}," +
                        "{" +
                            "\"@type\":\"Card\"," +
                            "\"uid\":\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"," +
                            "\"fullName\": \"Jane Doe\"" +
                        "}" +
                        "]";

        List<VCard> vcards = jsContact2VCard.convert(jscards);
        List<JSContact> jsContacts = vCard2JSContact.convert(vcards.toArray(new VCard[]{}));
        JSContact[] jsContacts2 = JSContact.toJSContacts(jscards);
        for (int i = 0; i < jsContacts2.length ; i++)
            assertEquals("testCardGroup1 - 1", jsContacts.get(i), jsContacts2[i]);
    }
}
