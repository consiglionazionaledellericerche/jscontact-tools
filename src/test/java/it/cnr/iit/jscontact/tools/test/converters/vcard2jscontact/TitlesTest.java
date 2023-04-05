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
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TitlesTest extends VCard2JSContactTest {

    @Test
    public void testTitleWithAltid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testTitleWithAltid1 - 1", jsCard.getTitles());
        assertEquals("testTitleWithAltid1 - 2", 1, jsCard.getTitles().size());
        assertEquals("testTitleWithAltid1 - 3", "Research Scientist", jsCard.getTitles().get("TITLE-1").getName());
        assertTrue("testTitleWithAltid1 - 4", jsCard.getTitles().get("TITLE-1").getKind().isTitle());
        assertEquals("testTitleWithAltid1 - 5", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleWithAltid1 - 6", "Ricercatore", jsCard.getLocalization("it", "titles/TITLE-1").get("name").asText());
        assertEquals("testTitleWithAltid1 - 7", "title", jsCard.getLocalization("it", "titles/TITLE-1").get("kind").asText());
    }

    @Test
    public void testTitleWithoutAltid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE:Research Scientist\n" +
                "TITLE;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testTitleWithoutAltid - 1", jsCard.getTitles());
        assertEquals("testTitleWithoutAltid - 2", 2, jsCard.getTitles().size());
        assertEquals("testTitleWithoutAltid - 3", "Research Scientist", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testTitleWithoutAltid - 4", "Ricercatore", jsCard.getTitles().get("TITLE-2").getName());
        assertNull("testTitleWithoutAltid - 5", jsCard.getLocalizations());
    }

    @Test
    public void testTitleWithAltid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "TITLE;PREF=1:IETF Area Director\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testTitleWithAltid2 - 1", jsCard.getTitles());
        assertEquals("testTitleWithAltid2 - 2", 2, jsCard.getTitles().size());
        assertEquals("testTitleWithAltid2 - 3", "Research Scientist", jsCard.getTitles().get("TITLE-2").getName());
        assertEquals("testTitleWithAltid2 - 4", "IETF Area Director", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testTitleWithAltid2 - 5", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleWithAltid2 - 6", "Ricercatore", jsCard.getLocalization("it", "titles/TITLE-2").get("name").asText());
    }

    @Test
    public void testTitleWithAltid3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "TITLE;PREF=1;ALTID=2:IETF Area Director\n" +
                "TITLE;PREF=1;LANGUAGE=it;ALTID=2:Direttore Area IETF\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testTitleWithAltid3 - 1", jsCard.getTitles());
        assertEquals("testTitleWithAltid3 - 2", 2, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleWithAltid3 - 3", "Research Scientist", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testTitleWithAltid3 - 4", "IETF Area Director", jsCard.getTitles().get("TITLE-2").getName());
        assertEquals("testTitleWithAltid3 - 5", 2, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleWithAltid3 - 6", "Ricercatore", jsCard.getLocalization("it", "titles/TITLE-1").get("name").asText());
        assertEquals("testTitleWithAltid3 - 7", "Direttore Area IETF", jsCard.getLocalization("it", "titles/TITLE-2").get("name").asText());
    }


    @Test
    public void testRoleWithAltid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;ALTID=1:Project Leader\n" +
                "ROLE;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testRoleWithAltid1 - 1", jsCard.getTitles());
        assertEquals("testRoleWithAltid1 - 2", 1, jsCard.getTitles().size());
        assertEquals("testRoleWithAltid1 - 3", "Project Leader", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testRoleWithAltid1 - 4", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testRoleWithAltid1 - 5", "Capo Progetto", jsCard.getLocalization("it", "titles/TITLE-1").get("name").asText());
    }

    @Test
    public void testRoleWithoutAltid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE:Project Leader\n" +
                "ROLE;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testRoleWithoutAltid - 1", jsCard.getTitles());
        assertEquals("testRoleWithoutAltid - 2", 2, jsCard.getTitles().size());
        assertEquals("testRoleWithoutAltid - 3", "Project Leader", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testRoleWithoutAltid - 4", "Capo Progetto", jsCard.getTitles().get("TITLE-2").getName());
        assertNull("testRoleWithoutAltid - 5", jsCard.getLocalizations());    }

    @Test
    public void testRoleWithAltid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;ALTID=1:Project Leader\n" +
                "ROLE;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "ROLE;PREF=1:IETF Area Director\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testRoleWithAltid2 - 1", jsCard.getTitles());
        assertEquals("testRoleWithAltid2 - 2", 2, jsCard.getTitles().size());
        assertEquals("testRoleWithAltid2 - 3", "Project Leader", jsCard.getTitles().get("TITLE-2").getName());
        assertEquals("testRoleWithAltid2 - 4", "IETF Area Director", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testRoleWithAltid2 - 5", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testRoleWithAltid2 - 6", "Capo Progetto", jsCard.getLocalization("it", "titles/TITLE-2").get("name").asText());
    }


}
