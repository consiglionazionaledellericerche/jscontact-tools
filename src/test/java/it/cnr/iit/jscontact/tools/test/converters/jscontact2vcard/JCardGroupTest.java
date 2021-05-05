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
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class JCardGroupTest extends JSContact2VCardTest {

    @Test
    public void testJCardGroupValid1() throws IOException, CardException {

        String jsCards = "[" +
                         "{" +
                             "\"uid\":\"2feb4102-f15f-4047-b521-190d4acd0d29\"," +
                             "\"card\": {" +
                                 "\"kind\":\"group\"," +
                                 "\"fullName\": {" +
                                    "\"value\":\"The Doe family\"" +
                                 "}" +
                             "}," +
                             "\"members\": {" +
                                "\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\":true," +
                                "\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\":true" +
                             "}" +
                        "}," +
                        "{" +
                            "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                            "\"fullName\": {" +
                                "\"value\":\"John Doe\"" +
                            "}" +
                        "}," +
                        "{" +
                            "\"uid\":\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"," +
                            "\"fullName\": {" +
                                "\"value\":\"Jane Doe\"" +
                            "}" +
                        "}" +
                        "]";

        List<VCard> vcards = jsContact2VCard.convert(jsCards);
        assertTrue("testJCardGroupValid1 - 1",vcards.size() == 3);
        assertTrue("testJCardGroupValid1 - 3", vcards.get(0).getKind().isGroup());
        assertTrue("testJCardGroupValid1 - 4",StringUtils.isNotEmpty(vcards.get(0).getUid().getValue()));
        assertTrue("testJCardGroupValid1 - 5",vcards.get(0).getFormattedName().getValue().equals("The Doe family"));
        assertTrue("testJCardGroupValid1 - 6",vcards.get(0).getMembers().size() == 2);
        assertTrue("testJCardGroupValid1 - 7",vcards.get(0).getMembers().get(0).getUri().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 8",vcards.get(0).getMembers().get(1).getUri().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 9",vcards.get(1).getUid().getValue().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid1 - 10",vcards.get(1).getFormattedName().getValue().equals("John Doe"));
        assertTrue("testJCardGroupValid1 - 11",vcards.get(2).getUid().getValue().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid1 - 12",vcards.get(2).getFormattedName().getValue().equals("Jane Doe"));
    }
}
