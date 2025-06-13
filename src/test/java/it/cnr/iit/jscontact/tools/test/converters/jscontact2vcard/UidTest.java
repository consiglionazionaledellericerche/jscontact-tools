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
import it.cnr.iit.jscontact.tools.constraints.groups.Version_1_0;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard.JSContact2VCard;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UidTest extends JSContact2VCardTest {

    //jscard version 1.0 doesn't include uid
    @Test(expected = CardException.class)
    public void testUidInvalid() throws IOException, CardException {

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder().versionGroup(Version_1_0.class).build()).build();
        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"name\": { \"full\": \"test\"}" +
                "}";
        jsContact2VCard.convert(jscard);
    }

    @Test
    public void testUid() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"name\": { \"full\": \"test\"}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testUid - 1", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", vcard.getUid().getValue());
    }

}
