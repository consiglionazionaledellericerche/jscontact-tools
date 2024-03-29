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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ProdidTest extends JSContact2VCardTest {

    @Test
    public void testProdid() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"prodId\":\"-//ONLINE DIRECTORY//NONSGML Version 1//EN\"" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testProdid - 1", "-//ONLINE DIRECTORY//NONSGML Version 1//EN", vcard.getProductId().getValue());
        String text1 = VCardWriter.write(vcard);
        assertFalse("testProdid - 2", text1.contains("-//ONLINE DIRECTORY//NONSGML Version 1//EN"));
        assertTrue("testProdid - 3", text1.contains("ez-vcard 0.11.3"));
    }

    @Test
    public void testAutoProdid() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertNull("testAutoProdid1 - 1", vcard.getProductId());
        String text1 = VCardWriter.write(vcard);
        assertTrue("testAutoProdid2 - 1", text1.contains("PRODID"));
        text1 = Ezvcard.write(vcard).prodId(false).go();
        assertFalse("testAutoProdid3 - 1", text1.contains("PRODID"));
    }

}
