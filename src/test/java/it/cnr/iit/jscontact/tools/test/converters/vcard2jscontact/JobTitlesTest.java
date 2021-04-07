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

public class JobTitlesTest extends VCard2JSContactTest {

    @Test
    public void testJobTitleWithAltid1() throws IOException,CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testJobTitleWithAltid1 - 1",jsCard.getTitles()!=null);
        assertTrue("testJobTitleWithAltid1 - 2",jsCard.getTitles().size() == 1);
        assertTrue("testJobTitleWithAltid1 - 3",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testJobTitleWithAltid1 - 4",jsCard.getTitles().get("TITLE-1").getTitle().getLanguage() == null);
        assertTrue("testJobTitleWithAltid1 - 5",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations() != null);
        assertTrue("testJobTitleWithAltid1 - 6",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations().size() == 1);
        assertTrue("testJobTitleWithAltid1 - 7",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations().get("it").equals("Ricercatore"));
    }

    @Test
    public void testJobTitleWithoutAltid() throws IOException,CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE:Research Scientist\n" +
                "TITLE;LANGUAGE=it:Ricercatore\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testJobTitleWithoutAltid - 1",jsCard.getTitles()!=null);
        assertTrue("testJobTitleWithoutAltid - 2",jsCard.getTitles().size() == 2);
        assertTrue("testJobTitleWithoutAltid - 3",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testJobTitleWithoutAltid - 4",jsCard.getTitles().get("TITLE-1").getTitle().getLanguage() == null);
        assertTrue("testJobTitleWithoutAltid - 5",jsCard.getTitles().get("TITLE-1").getTitle().getLocalizations() == null);
        assertTrue("testJobTitleWithoutAltid - 6",jsCard.getTitles().get("TITLE-2").getTitle().getValue().equals("Ricercatore"));
        assertTrue("testJobTitleWithoutAltid - 7",jsCard.getTitles().get("TITLE-2").getTitle().getLanguage().equals("it"));
        assertTrue("testJobTitleWithoutAltid - 8",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations() == null);
    }

    @Test
    public void testJobTitleWithAltid2() throws IOException,CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TITLE;ALTID=1:Research Scientist\n" +
                "TITLE;ALTID=1;LANGUAGE=it:Ricercatore\n" +
                "TITLE;PREF=1:IETF Area Director\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testJobTitleWithAltid2 - 1",jsCard.getTitles()!=null);
        assertTrue("testJobTitleWithAltid2 - 2",jsCard.getTitles().size() == 2);
        assertTrue("testJobTitleWithAltid2 - 3",jsCard.getTitles().get("TITLE-2").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testJobTitleWithAltid2 - 4",jsCard.getTitles().get("TITLE-2").getTitle().getLanguage() == null);
        assertTrue("testJobTitleWithAltid2 - 5",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations() != null);
        assertTrue("testJobTitleWithAltid2 - 6",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations().size() == 1);
        assertTrue("testJobTitleWithAltid2 - 7",jsCard.getTitles().get("TITLE-2").getTitle().getLocalizations().get("it").equals("Ricercatore"));
        assertTrue("testJobTitleWithAltid2 - 8",jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("IETF Area Director"));
    }


}
