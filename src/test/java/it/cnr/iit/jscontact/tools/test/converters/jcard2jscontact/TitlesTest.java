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

import static org.junit.Assert.*;

public class TitlesTest extends JCard2JSContactTest {

    @Test
    public void testTitleWithAltid1() throws CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {\"altid\" : \"1\"}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Ricercatore\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testTitleWithAltid1 - 1", jsCard.getTitles());
        assertEquals("testTitleWithAltid1 - 2", 1, jsCard.getTitles().size());
        assertEquals("testTitleWithAltid1 - 3", "Research Scientist", jsCard.getTitles().get("TITLE-1").getTitle());
        assertTrue("testTitleWithAltid1 - 4", jsCard.getTitles().get("TITLE-1").getType().isTitle());
        assertEquals("testTitleWithAltid1 - 5", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleWithAltid1 - 6", "Ricercatore", jsCard.getLocalization("it", "titles/TITLE-1").get("title").asText());
        assertEquals("testTitleWithAltid1 - 7", "title", jsCard.getLocalization("it", "titles/TITLE-1").get("type").asText());
    }

    @Test
    public void testTitleWithoutAltid() throws CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\"}, \"text\", \"Ricercatore\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testTitleWithoutAltid - 1", jsCard.getTitles());
        assertEquals("testTitleWithoutAltid - 2", 2, jsCard.getTitles().size());
        assertEquals("testTitleWithoutAltid - 3", "Research Scientist", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testTitleWithoutAltid - 4", "Ricercatore", jsCard.getTitles().get("TITLE-2").getTitle());
        assertNull("testTitleWithoutAltid - 5", jsCard.getLocalizations());
    }

    @Test
    public void testTitleWithAltid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {\"altid\" : \"1\"}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Ricercatore\"]," +
                "[\"title\", {\"pref\" : \"1\"}, \"text\", \"IETF Area Director\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testTitleWithAltid2 - 1", jsCard.getTitles());
        assertEquals("testTitleWithAltid2 - 2", 2, jsCard.getTitles().size());
        assertEquals("testTitleWithAltid2 - 3", "Research Scientist", jsCard.getTitles().get("TITLE-2").getTitle());
        assertEquals("testTitleWithAltid2 - 4", "IETF Area Director", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testTitleWithAltid2 - 5", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleWithAltid2 - 6", "Ricercatore", jsCard.getLocalization("it", "titles/TITLE-2").get("title").asText());
    }

    @Test
    public void testTitleWithAltid3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {\"altid\" : \"1\"}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Ricercatore\"]," +
                "[\"title\", {\"pref\" : \"1\", \"altid\" : \"2\"}, \"text\", \"IETF Area Director\"]," +
                "[\"title\", {\"pref\" : \"1\", \"language\" : \"it\", \"altid\" : \"2\"}, \"text\", \"Direttore Area IETF\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testTitleWithAltid3 - 1", jsCard.getTitles());
        assertEquals("testTitleWithAltid3 - 2", 2, jsCard.getTitles().size());
        assertEquals("testTitleWithAltid3 - 3", "Research Scientist", jsCard.getTitles().get("TITLE-2").getTitle());
        assertEquals("testTitleWithAltid3 - 4", "IETF Area Director", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testTitleWithAltid3 - 5", 2, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testTitleWithAltid3 - 6", "Ricercatore", jsCard.getLocalization("it", "titles/TITLE-2").get("title").asText());
        assertEquals("testTitleWithAltid3 - 7", "Direttore Area IETF", jsCard.getLocalization("it", "titles/TITLE-1").get("title").asText());
    }



    @Test
    public void testRoleWithAltid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"role\", {\"altid\" : \"1\"}, \"text\", \"Project Leader\"]," +
                "[\"role\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Capo Progetto\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testRoleWithAltid1 - 1", jsCard.getTitles());
        assertEquals("testRoleWithAltid1 - 2", 1, jsCard.getTitles().size());
        assertEquals("testRoleWithAltid1 - 3", "Project Leader", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testRoleWithAltid1 - 4", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testRoleWithAltid1 - 5", "Capo Progetto", jsCard.getLocalization("it", "titles/TITLE-1").get("title").asText());
    }

    @Test
    public void testRoleWithoutAltid() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"role\", {}, \"text\", \"Project Leader\"]," +
                "[\"role\", {\"language\" : \"it\"}, \"text\", \"Capo Progetto\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testRoleWithoutAltid - 1", jsCard.getTitles());
        assertEquals("testRoleWithoutAltid - 2", 2, jsCard.getTitles().size());
        assertEquals("testRoleWithoutAltid - 3", "Project Leader", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testRoleWithoutAltid - 4", "Capo Progetto", jsCard.getTitles().get("TITLE-2").getTitle());
        assertNull("testRoleWithoutAltid - 5", jsCard.getLocalizations());
    }

    @Test
    public void testRoleWithAltid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"role\", {\"altid\" : \"1\"}, \"text\", \"Project Leader\"]," +
                "[\"role\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Capo Progetto\"], " +
                "[\"role\", {\"pref\" : \"1\"}, \"text\", \"IETF Area Director\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testRoleWithAltid2 - 1", jsCard.getTitles());
        assertEquals("testRoleWithAltid2 - 2", 2, jsCard.getTitles().size());
        assertEquals("testRoleWithAltid2 - 3", "Project Leader", jsCard.getTitles().get("TITLE-2").getTitle());
        assertEquals("testRoleWithAltid2 - 4", "IETF Area Director", jsCard.getTitles().get("TITLE-1").getTitle());
        assertEquals("testRoleWithAltid2 - 5", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testRoleWithAltid2 - 6", "Capo Progetto", jsCard.getLocalization("it", "titles/TITLE-2").get("title").asText());

    }

}
