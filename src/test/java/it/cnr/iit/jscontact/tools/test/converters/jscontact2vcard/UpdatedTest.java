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
import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class UpdatedTest extends JSContact2VCardTest {

    @Test
    public void testUpdatedValid() throws IOException, CardException {

        String jscard="{" +
                    "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                    "\"fullName\":{\"value\":\"test\"}," +
                    "\"updated\":\"1995-10-31T22:27:10Z\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testUpdatedValid - 1",vcard.getRevision().getCalendar().compareTo(VCardDateFormat.parseAsCalendar("1995-10-31T22:27:10Z"))==0);

    }

}
