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

import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class OrganizationTest extends JCard2JSContactTest {

    @Test
    public void testOrganizationWithAltid1() throws IOException, CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {\"altid\" : \"1\"}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOrganizationWithAltid1 - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationWithAltid1 - 2",jsCard.getOrganizations().length == 1);
        assertTrue("testOrganizationWithAltid1 - 3",jsCard.getOrganizations()[0].getValue().equals("ABC, Inc.;North American Division;Marketing"));
        assertTrue("testOrganizationWithAltid1 - 4",jsCard.getOrganizations()[0].getLanguage() == null);
        assertTrue("testOrganizationWithAltid1 - 5",jsCard.getOrganizations()[0].getLocalizations() != null);
        assertTrue("testOrganizationWithAltid1 - 6",jsCard.getOrganizations()[0].getLocalizations().size() == 1);
        assertTrue("testOrganizationWithAltid1 - 7",jsCard.getOrganizations()[0].getLocalizations().get("it").equals("ABC, Spa.;Divisione Nord America;Marketing"));
    }

    @Test
    public void testOrganizationWithoutAltid() throws IOException, CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOrganizationWithoutAltid - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationWithoutAltid - 2",jsCard.getOrganizations().length == 2);
        assertTrue("testOrganizationWithoutAltid - 3",jsCard.getOrganizations()[0].getValue().equals("ABC, Inc.;North American Division;Marketing"));
        assertTrue("testOrganizationWithoutAltid - 4",jsCard.getOrganizations()[0].getLanguage() == null);
        assertTrue("testOrganizationWithoutAltid - 5",jsCard.getOrganizations()[0].getLocalizations() == null);
        assertTrue("testOrganizationWithoutAltid - 6",jsCard.getOrganizations()[1].getValue().equals("ABC, Spa.;Divisione Nord America;Marketing"));
        assertTrue("testOrganizationWithoutAltid - 7",jsCard.getOrganizations()[1].getLanguage().equals("it"));
        assertTrue("testOrganizationWithoutAltid - 8",jsCard.getOrganizations()[1].getLocalizations() == null);
    }


    @Test
    public void testOrganizationWithAltid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {\"altid\" : \"1\"}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"], " +
                "[\"org\", {\"pref\" : \"1\"}, \"text\", \"University of North America\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOrganizationWithAltid2 - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationWithAltid2 - 2",jsCard.getOrganizations().length == 2);
        assertTrue("testOrganizationWithAltid2 - 3",jsCard.getOrganizations()[1].getValue().equals("ABC, Inc.;North American Division;Marketing"));
        assertTrue("testOrganizationWithAltid2 - 4",jsCard.getOrganizations()[1].getLanguage() == null);
        assertTrue("testOrganizationWithAltid2 - 5",jsCard.getOrganizations()[1].getLocalizations() != null);
        assertTrue("testOrganizationWithAltid2 - 6",jsCard.getOrganizations()[1].getLocalizations().size() == 1);
        assertTrue("testOrganizationWithAltid2 - 7",jsCard.getOrganizations()[1].getLocalizations().get("it").equals("ABC, Spa.;Divisione Nord America;Marketing"));
        assertTrue("testOrganizationWithAltid2 - 8",jsCard.getOrganizations()[0].getValue().equals("University of North America"));
    }


}
