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

import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class JobTitleTest extends JCard2JSContactTest {

    @Test
    public void testJobTitleWithAltid1() throws IOException,CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {\"altid\" : \"1\"}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Ricercatore\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testJobTitleWithAltid1 - 1",jsCard.getJobTitle()!=null);
        assertTrue("testJobTitleWithAltid1 - 2",jsCard.getJobTitle().length == 1);
        assertTrue("testJobTitleWithAltid1 - 3",jsCard.getJobTitle()[0].getValue().equals("Research Scientist"));
        assertTrue("testJobTitleWithAltid1 - 4",jsCard.getJobTitle()[0].getLanguage() == null);
        assertTrue("testJobTitleWithAltid1 - 5",jsCard.getJobTitle()[0].getLocalizations() != null);
        assertTrue("testJobTitleWithAltid1 - 6",jsCard.getJobTitle()[0].getLocalizations().size() == 1);
        assertTrue("testJobTitleWithAltid1 - 7",jsCard.getJobTitle()[0].getLocalizations().get("it").equals("Ricercatore"));
    }

    @Test
    public void testJobTitleWithoutAltid() throws IOException,CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\"}, \"text\", \"Ricercatore\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testJobTitleWithoutAltid - 1",jsCard.getJobTitle()!=null);
        assertTrue("testJobTitleWithoutAltid - 2",jsCard.getJobTitle().length == 2);
        assertTrue("testJobTitleWithoutAltid - 3",jsCard.getJobTitle()[0].getValue().equals("Research Scientist"));
        assertTrue("testJobTitleWithoutAltid - 4",jsCard.getJobTitle()[0].getLanguage() == null);
        assertTrue("testJobTitleWithoutAltid - 5",jsCard.getJobTitle()[0].getLocalizations() == null);
        assertTrue("testJobTitleWithoutAltid - 6",jsCard.getJobTitle()[1].getValue().equals("Ricercatore"));
        assertTrue("testJobTitleWithoutAltid - 7",jsCard.getJobTitle()[1].getLanguage().equals("it"));
        assertTrue("testJobTitleWithoutAltid - 8",jsCard.getJobTitle()[1].getLocalizations() == null);
    }

    @Test
    public void testJobTitleWithAltid2() throws IOException,CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"title\", {\"altid\" : \"1\"}, \"text\", \"Research Scientist\"]," +
                "[\"title\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"Ricercatore\"]," +
                "[\"title\", {\"pref\" : \"1\"}, \"text\", \"IETF Area Director\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testJobTitleWithAltid2 - 1",jsCard.getJobTitle()!=null);
        assertTrue("testJobTitleWithAltid2 - 2",jsCard.getJobTitle().length == 2);
        assertTrue("testJobTitleWithAltid2 - 3",jsCard.getJobTitle()[1].getValue().equals("Research Scientist"));
        assertTrue("testJobTitleWithAltid2 - 4",jsCard.getJobTitle()[1].getLanguage() == null);
        assertTrue("testJobTitleWithAltid2 - 5",jsCard.getJobTitle()[1].getLocalizations() != null);
        assertTrue("testJobTitleWithAltid2 - 6",jsCard.getJobTitle()[1].getLocalizations().size() == 1);
        assertTrue("testJobTitleWithAltid2 - 7",jsCard.getJobTitle()[1].getLocalizations().get("it").equals("Ricercatore"));
        assertTrue("testJobTitleWithAltid2 - 8",jsCard.getJobTitle()[0].getValue().equals("IETF Area Director"));
    }

}
