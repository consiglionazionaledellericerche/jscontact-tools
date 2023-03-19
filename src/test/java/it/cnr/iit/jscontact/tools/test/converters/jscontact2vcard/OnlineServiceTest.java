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
import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.dto.VCardPropEnum;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OnlineServiceTest extends JSContact2VCardTest {

    @Test
    public void testOnlineService1() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"onlineServices\": {"+
                    "\"OS-1\": {" +
                        "\"@type\":\"OnlineService\"," +
                        "\"kind\": \"impp\", " +
                        "\"contexts\": {\"private\": true}," +
                        "\"pref\": 1, " +
                        "\"user\": \"xmpp:alice@example.com\"" +
                    "}" +
                 "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOnlineService1 - 1", 1, vcard.getImpps().size());
        assertEquals("testOnlineService1 - 2", "alice@example.com", vcard.getImpps().get(0).getHandle());
        assertEquals("testOnlineService1 - 3", "home", vcard.getImpps().get(0).getParameter(VCardParamEnum.TYPE.getValue()));
        assertEquals("testOnlineService1 - 4", 1, (int) vcard.getImpps().get(0).getPref());
        assertEquals("testOnlineService1 - 5", "OS-1", vcard.getImpps().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
    }

    @Test
    public void testOnlineService2() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":\"test\"," +
                "\"onlineServices\": {"+
                    "\"OS-1\": {" +
                        "\"@type\":\"OnlineService\"," +
                        "\"kind\": \"uri\", " +
                        "\"service\": \"Twitter\", " +
                        "\"contexts\": {\"private\": true}," +
                        "\"pref\": 1, " +
                        "\"user\": \"https://twitter.com/ietf\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOnlineService2 - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testOnlineService2 - 2", VCardPropEnum.SOCIALSERVICE.getValue(), vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testOnlineService2 - 3", "home", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.TYPE.getValue()));
        assertEquals("testOnlineService2 - 4", "1", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.PREF.getValue()));
        assertEquals("testOnlineService2 - 5", "OS-1", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        assertEquals("testOnlineService2 - 6", "Twitter", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.SERVICE_TYPE.getValue()));
        assertEquals("testOnlineService2 - 7", VCardDataType.URI, vcard.getExtendedProperties().get(0).getDataType());
    }


}
