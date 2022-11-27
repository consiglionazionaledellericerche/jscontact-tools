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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtensionsTest extends JCard2JSContactTest {

    @Test
    public void testExtendedJSContact1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"jscontact-prop\",{\"jsptr\":\"extension:myext1\"},\"text\",\"\\\"extvalue\\\"\"]," +
                "[\"jscontact-prop\",{\"jsptr\":\"extension:myext2\"},\"text\",\"{\\\"extprop\\\":\\\"extvalue\\\"}\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testExtendedJSContact1 - 1", 2, jsCard.getExtensions().size());
        assertEquals("testExtendedJSContact1 - 2", "extvalue", jsCard.getExtensions().get("extension:myext1"));
        assertEquals("testExtendedJSContact1 - 3", "{extprop=extvalue}", jsCard.getExtensions().get("extension:myext2").toString());
    }

    @Test
    public void textExtendedJSContact2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"nickname\",{\"language\":\"en\"},\"text\",\"Johnny\"]," +
                "[\"nickname\",{\"language\":\"en\"},\"text\",\"Joe\"]," +
                "[\"adr\",{\"cc\":\"US\",\"language\":\"en\"},\"text\",[\"\",\"\",\"54321 Oak St\",\"Reston\",\"VA\",\"20190\",\"USA\"]],"+
                "[\"lang\",{\"pref\":\"1\"},\"language-tag\",\"jp\"],"+
                "[\"lang\",{\"pref\":\"2\"},\"language-tag\",\"en\"],"+
                "[\"locale\",{},\"language-tag\",\"en\"],"+
                "[\"jscontact-prop\",{\"jsptr\":\"addresses/ADR-1/street/0/ext4\"},\"text\",\"true\"],"+
                "[\"jscontact-prop\",{\"jsptr\":\"nickNames/NICK-1/ext3\"},\"text\",\"\\\"text\\\"\"],"+
                "[\"jscontact-prop\",{\"jsptr\":\"addresses/ADR-1/ext2\"},\"text\",\"{\\\"prop\\\":10}\"],"+
                "[\"jscontact-prop\",{\"jsptr\":\"preferredLanguages/jp/0/ext6\"},\"text\",\"[\\\"1\\\",\\\"2\\\"]\"],"+
                "[\"jscontact-prop\",{\"jsptr\":\"ext1\"},\"text\",\"10\"]"+
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testExtendedJSContact2 - 1", 1, jsCard.getExtensions().size());
        assertEquals("testExtendedJSContact2 - 2", 10, jsCard.getExtensions().get("ext1"));
        assertEquals("testExtendedJSContact2 - 3", 1, jsCard.getNickNames().get("NICK-1").getExtensions().size());
        assertEquals("testExtendedJSContact2 - 4", "text", jsCard.getNickNames().get("NICK-1").getExtensions().get("ext3"));
        assertEquals("testExtendedJSContact2 - 5", 1, jsCard.getPreferredLanguages().get("jp")[0].getExtensions().size());
        assertEquals("testExtendedJSContact2 - 6", "[1, 2]", jsCard.getPreferredLanguages().get("jp")[0].getExtensions().get("ext6").toString());
        assertEquals("testExtendedJSContact2 - 7", 1, jsCard.getAddresses().get("ADR-1").getExtensions().size());
        assertEquals("testExtendedJSContact2 - 8", "{prop=10}", jsCard.getAddresses().get("ADR-1").getExtensions().get("ext2").toString());
        assertEquals("testExtendedJSContact2 - 9", 1, jsCard.getAddresses().get("ADR-1").getStreet()[0].getExtensions().size());
        assertEquals("testExtendedJSContact2 - 10", true, jsCard.getAddresses().get("ADR-1").getStreet()[0].getExtensions().get("ext4"));
    }





}
