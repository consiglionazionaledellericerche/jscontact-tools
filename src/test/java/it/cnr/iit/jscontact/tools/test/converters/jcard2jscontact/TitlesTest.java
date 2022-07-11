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

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TitlesTest extends JCard2JSContactTest {

    @Test
    public void testTitleWithAltid1() throws CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {\"altid\" : \"1\"}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Ricercatore\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testTitleWithAltid1 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid1 - 2",jsCard.getTitles().size() == 1);
        assertTrue("testTitleWithAltid1 - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithAltid1 - 4",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testTitleWithAltid1 - 5",jsCard.getLocalization("it","titles/TITLE-1").get("title").asText().equals("Ricercatore"));
    }

    @Test
    public void testTitleWithoutAltid() throws CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\"}, \"text\", \"Ricercatore\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testTitleWithoutAltid - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithoutAltid - 2",jsCard.getTitles().size() == 2);
        assertTrue("testTitleWithoutAltid - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithoutAltid - 4",jsCard.getTitles().get("TITLE-2").getTitle().equals("Ricercatore"));
        assertTrue("testTitleWithoutAltid - 5",jsCard.getLocalizations() == null);
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
        assertTrue("testTitleWithAltid2 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid2 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testTitleWithAltid2 - 3",jsCard.getTitles().get("TITLE-2").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithAltid2 - 4",jsCard.getTitles().get("TITLE-1").getTitle().equals("IETF Area Director"));
        assertTrue("testTitleWithAltid2 - 5",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testTitleWithAltid2 - 6",jsCard.getLocalization("it","titles/TITLE-2").get("title").asText().equals("Ricercatore"));
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
        assertTrue("testTitleWithAltid3 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid3 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testTitleWithAltid3 - 3",jsCard.getTitles().get("TITLE-2").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithAltid3 - 4",jsCard.getTitles().get("TITLE-1").getTitle().equals("IETF Area Director"));
        assertTrue("testTitleWithAltid3 - 5",jsCard.getLocalizationsPerLanguage("it").size() == 2);
        assertTrue("testTitleWithAltid3 - 6",jsCard.getLocalization("it","titles/TITLE-2").get("title").asText().equals("Ricercatore"));
        assertTrue("testTitleWithAltid3 - 7",jsCard.getLocalization("it","titles/TITLE-1").get("title").asText().equals("Direttore Area IETF"));
    }



    @Test
    public void testRoleWithAltid1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"role\", {\"altid\" : \"1\"}, \"text\", \"Project Leader\"]," +
                "[\"role\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Capo Progetto\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testRoleWithAltid1 - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithAltid1 - 2",jsCard.getTitles().size() == 1);
        assertTrue("testRoleWithAltid1 - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Project Leader"));
        assertTrue("testRoleWithAltid1 - 4",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testRoleWithAltid1 - 5",jsCard.getLocalization("it","titles/TITLE-1").get("title").asText().equals("Capo Progetto"));
    }

    @Test
    public void testRoleWithoutAltid() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"role\", {}, \"text\", \"Project Leader\"]," +
                "[\"role\", {\"language\" : \"it\"}, \"text\", \"Capo Progetto\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testRoleWithoutAltid - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithoutAltid - 2",jsCard.getTitles().size() == 2);
        assertTrue("testRoleWithoutAltid - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Project Leader"));
        assertTrue("testRoleWithoutAltid - 4",jsCard.getTitles().get("TITLE-2").getTitle().equals("Capo Progetto"));
        assertTrue("testRoleWithoutAltid - 5",jsCard.getLocalizations() == null);
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
        assertTrue("testRoleWithAltid2 - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithAltid2 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testRoleWithAltid2 - 3",jsCard.getTitles().get("TITLE-2").getTitle().equals("Project Leader"));
        assertTrue("testRoleWithAltid2 - 4",jsCard.getTitles().get("TITLE-1").getTitle().equals("IETF Area Director"));
        assertTrue("testRoleWithAltid2 - 5",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testRoleWithAltid2 - 6",jsCard.getLocalization("it","titles/TITLE-2").get("title").asText().equals("Capo Progetto"));

    }

}
