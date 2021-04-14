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

import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TitlesTest extends VCard2JSContactTest {

    @Test
    public void testTitleWithAltid1() throws IOException,CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testTitleWithAltid1 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid1 - 2",jsCard.getTitles().size() == 1);
        assertTrue("testTitleWithAltid1 - 3",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testTitleWithAltid1 - 4",jsCard.getTitles().get("TITLE-1").getTitle().getLanguage() == null);
        assertTrue("testTitleWithAltid1 - 5",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations() != null);
        assertTrue("testTitleWithAltid1 - 6",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations().size() == 1);
        assertTrue("testTitleWithAltid1 - 7",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations().get("it").equals("Ricercatore"));
    }

    @Test
    public void testTitleWithoutAltid() throws IOException,CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE:Research Scientist\n" +
                "TITLE;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testTitleWithoutAltid - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithoutAltid - 2",jsCard.getTitles().size() == 2);
        assertTrue("testTitleWithoutAltid - 3",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testTitleWithoutAltid - 4",jsCard.getTitles().get("TITLE-1").getTitle().getLanguage() == null);
        assertTrue("testTitleWithoutAltid - 5",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations() == null);
        assertTrue("testTitleWithoutAltid - 6",jsCard.getTitles().get("TITLE-2").getTitle().getValue().equals("Ricercatore"));
        assertTrue("testTitleWithoutAltid - 7",jsCard.getTitles().get("TITLE-2").getTitle().getLanguage().equals("it"));
        assertTrue("testTitleWithoutAltid - 8",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations() == null);
    }

    @Test
    public void testTitleWithAltid2() throws IOException,CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "TITLE;PREF=1:IETF Area Director\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testTitleWithAltid2 - 1",jsCard.getTitles()!=null);
        assertTrue("testTitleWithAltid2 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testTitleWithAltid2 - 3",jsCard.getTitles().get("TITLE-2").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testTitleWithAltid2 - 4",jsCard.getTitles().get("TITLE-2").getTitle().getLanguage() == null);
        assertTrue("testTitleWithAltid2 - 5",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations() != null);
        assertTrue("testTitleWithAltid2 - 6",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations().size() == 1);
        assertTrue("testTitleWithAltid2 - 7",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations().get("it").equals("Ricercatore"));
        assertTrue("testTitleWithAltid2 - 8",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("IETF Area Director"));
    }

    @Test
    public void testRoleWithAltid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;ALTID=1:Project Leader\n" +
                "ROLE;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testRoleWithAltid1 - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithAltid1 - 2",jsCard.getTitles().size() == 1);
        assertTrue("testRoleWithAltid1 - 3",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("Project Leader"));
        assertTrue("testRoleWithAltid1 - 4",jsCard.getTitles().get("TITLE-1").getTitle().getLanguage() == null);
        assertTrue("testRoleWithAltid1 - 5",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations() != null);
        assertTrue("testRoleWithAltid1 - 6",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations().size() == 1);
        assertTrue("testRoleWithAltid1 - 7",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations().get("it").equals("Capo Progetto"));
    }

    @Test
    public void testRoleWithoutAltid() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE:Project Leader\n" +
                "ROLE;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testRoleWithoutAltid - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithoutAltid - 2",jsCard.getTitles().size() == 2);
        assertTrue("testRoleWithoutAltid - 3",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("Project Leader"));
        assertTrue("testRoleWithoutAltid - 4",jsCard.getTitles().get("TITLE-1").getTitle().getLanguage() == null);
        assertTrue("testRoleWithoutAltid - 5",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations() == null);
        assertTrue("testRoleWithoutAltid - 6",jsCard.getTitles().get("TITLE-2").getTitle().getValue().equals("Capo Progetto"));
        assertTrue("testRoleWithoutAltid - 7",jsCard.getTitles().get("TITLE-2").getTitle().getLanguage().equals("it"));
        assertTrue("testRoleWithoutAltid - 8",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations() == null);
    }

    @Test
    public void testRoleWithAltid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;ALTID=1:Project Leader\n" +
                "ROLE;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "ROLE;PREF=1:IETF Area Director\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testRoleWithAltid2 - 1",jsCard.getTitles()!=null);
        assertTrue("testRoleWithAltid2 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testRoleWithAltid2 - 3",jsCard.getTitles().get("TITLE-2").getTitle().getValue().equals("Project Leader"));
        assertTrue("testRoleWithAltid2 - 4",jsCard.getTitles().get("TITLE-2").getTitle().getLanguage() == null);
        assertTrue("testRoleWithAltid2 - 5",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations() != null);
        assertTrue("testRoleWithAltid2 - 6",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations().size() == 1);
        assertTrue("testRoleWithAltid2 - 7",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations().get("it").equals("Capo Progetto"));
        assertTrue("testRoleWithAltid2 - 8",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("IETF Area Director"));
    }


}
