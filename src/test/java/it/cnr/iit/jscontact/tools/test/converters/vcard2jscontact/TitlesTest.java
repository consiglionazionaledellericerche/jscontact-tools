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

import static org.junit.Assert.assertTrue;

public class TitlesTest extends VCard2JSContactTest {

    @Test
    public void testTitleWithAltid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testTitleWithAltid1 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid1 - 2",jsCard.getTitles().size() == 1);
        assertTrue("testTitleWithAltid1 - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithAltid1 - 4",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testTitleWithAltid1 - 5",jsCard.getLocalization("it","titles/TITLE-1").get("title").asText().equals("Ricercatore"));
    }

    @Test
    public void testTitleWithoutAltid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE:Research Scientist\n" +
                "TITLE;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testTitleWithoutAltid - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithoutAltid - 2",jsCard.getTitles().size() == 2);
        assertTrue("testTitleWithoutAltid - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithoutAltid - 4",jsCard.getTitles().get("TITLE-2").getTitle().equals("Ricercatore"));
        assertTrue("testTitleWithoutAltid - 5",jsCard.getLocalizations() == null);
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

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testTitleWithAltid2 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid2 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testTitleWithAltid2 - 3",jsCard.getTitles().get("TITLE-2").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithAltid2 - 4",jsCard.getTitles().get("TITLE-1").getTitle().equals("IETF Area Director"));
        assertTrue("testTitleWithAltid2 - 5",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testTitleWithAltid2 - 6",jsCard.getLocalization("it","titles/TITLE-2").get("title").asText().equals("Ricercatore"));
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

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testTitleWithAltid3 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid3 - 2",jsCard.getLocalizationsPerLanguage("it").size() == 2);
        assertTrue("testTitleWithAltid3 - 3",jsCard.getTitles().get("TITLE-2").getTitle().equals("Research Scientist"));
        assertTrue("testTitleWithAltid3 - 4",jsCard.getTitles().get("TITLE-1").getTitle().equals("IETF Area Director"));
        assertTrue("testTitleWithAltid3 - 5",jsCard.getLocalizationsPerLanguage("it").size() == 2);
        assertTrue("testTitleWithAltid3 - 6",jsCard.getLocalization("it","titles/TITLE-2").get("title").asText().equals("Ricercatore"));
        assertTrue("testTitleWithAltid3 - 7",jsCard.getLocalization("it","titles/TITLE-1").get("title").asText().equals("Direttore Area IETF"));
    }


    @Test
    public void testRoleWithAltid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;ALTID=1:Project Leader\n" +
                "ROLE;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testRoleWithAltid1 - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithAltid1 - 2",jsCard.getTitles().size() == 1);
        assertTrue("testRoleWithAltid1 - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Project Leader"));
        assertTrue("testRoleWithAltid1 - 4",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testRoleWithAltid1 - 5",jsCard.getLocalization("it","titles/TITLE-1").get("title").asText().equals("Capo Progetto"));
    }

    @Test
    public void testRoleWithoutAltid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE:Project Leader\n" +
                "ROLE;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testRoleWithoutAltid - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithoutAltid - 2",jsCard.getTitles().size() == 2);
        assertTrue("testRoleWithoutAltid - 3",jsCard.getTitles().get("TITLE-1").getTitle().equals("Project Leader"));
        assertTrue("testRoleWithoutAltid - 4",jsCard.getTitles().get("TITLE-2").getTitle().equals("Capo Progetto"));
        assertTrue("testRoleWithoutAltid - 5",jsCard.getLocalizations() == null);    }

    @Test
    public void testRoleWithAltid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;ALTID=1:Project Leader\n" +
                "ROLE;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "ROLE;PREF=1:IETF Area Director\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testRoleWithAltid2 - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithAltid2 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testRoleWithAltid2 - 3",jsCard.getTitles().get("TITLE-2").getTitle().equals("Project Leader"));
        assertTrue("testRoleWithAltid2 - 4",jsCard.getTitles().get("TITLE-1").getTitle().equals("IETF Area Director"));
        assertTrue("testRoleWithAltid2 - 5",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testRoleWithAltid2 - 6",jsCard.getLocalization("it","titles/TITLE-2").get("title").asText().equals("Capo Progetto"));
    }


}
