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

public class RoleTest extends VCard2JSContactTest {

    @Test
    public void testRoleWithAltid1() throws IOException, CardException {
        
        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ROLE;ALTID=1:Project Leader\n" +
                "ROLE;ALTID=1;LANGUAGE=it:Capo Progetto\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testRoleWithAltid1 - 1",jsCard.getRole()!=null);
        assertTrue("testRoleWithAltid1 - 2",jsCard.getRole().length == 1);
        assertTrue("testRoleWithAltid1 - 3",jsCard.getRole()[0].getValue().equals("Project Leader"));
        assertTrue("testRoleWithAltid1 - 4",jsCard.getRole()[0].getLanguage() == null);
        assertTrue("testRoleWithAltid1 - 5",jsCard.getRole()[0].getLocalizations() != null);
        assertTrue("testRoleWithAltid1 - 6",jsCard.getRole()[0].getLocalizations().size() == 1);
        assertTrue("testRoleWithAltid1 - 7",jsCard.getRole()[0].getLocalizations().get("it").equals("Capo Progetto"));
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
        assertTrue("testRoleWithoutAltid - 1",jsCard.getRole()!=null);
        assertTrue("testRoleWithoutAltid - 2",jsCard.getRole().length == 2);
        assertTrue("testRoleWithoutAltid - 3",jsCard.getRole()[0].getValue().equals("Project Leader"));
        assertTrue("testRoleWithoutAltid - 4",jsCard.getRole()[0].getLanguage() == null);
        assertTrue("testRoleWithoutAltid - 5",jsCard.getRole()[0].getLocalizations() == null);
        assertTrue("testRoleWithoutAltid - 6",jsCard.getRole()[1].getValue().equals("Capo Progetto"));
        assertTrue("testRoleWithoutAltid - 7",jsCard.getRole()[1].getLanguage().equals("it"));
        assertTrue("testRoleWithoutAltid - 8",jsCard.getRole()[1].getLocalizations() == null);
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
        assertTrue("testRoleWithAltid2 - 1",jsCard.getRole()!=null);
        assertTrue("testRoleWithAltid2 - 2",jsCard.getRole().length == 2);
        assertTrue("testRoleWithAltid2 - 3",jsCard.getRole()[1].getValue().equals("Project Leader"));
        assertTrue("testRoleWithAltid2 - 4",jsCard.getRole()[1].getLanguage() == null);
        assertTrue("testRoleWithAltid2 - 5",jsCard.getRole()[1].getLocalizations() != null);
        assertTrue("testRoleWithAltid2 - 6",jsCard.getRole()[1].getLocalizations().size() == 1);
        assertTrue("testRoleWithAltid2 - 7",jsCard.getRole()[1].getLocalizations().get("it").equals("Capo Progetto"));
        assertTrue("testRoleWithAltid2 - 8",jsCard.getRole()[0].getValue().equals("IETF Area Director"));
    }

}
