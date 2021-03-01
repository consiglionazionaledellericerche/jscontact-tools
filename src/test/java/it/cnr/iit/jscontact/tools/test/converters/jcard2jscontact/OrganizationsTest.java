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

public class OrganizationsTest extends JCard2JSContactTest {

    @Test
    public void testOrganizationsWithAltid1() throws IOException, CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {\"altid\" : \"1\"}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOrganizationsWithAltid1 - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationsWithAltid1 - 2",jsCard.getOrganizations().length == 1);
        assertTrue("testOrganizationsWithAltid1 - 3",jsCard.getOrganizations()[0].getValue().equals("ABC, Inc.;North American Division;Marketing"));
        assertTrue("testOrganizationsWithAltid1 - 4",jsCard.getOrganizations()[0].getLanguage() == null);
        assertTrue("testOrganizationsWithAltid1 - 5",jsCard.getOrganizations()[0].getLocalizations() != null);
        assertTrue("testOrganizationsWithAltid1 - 6",jsCard.getOrganizations()[0].getLocalizations().size() == 1);
        assertTrue("testOrganizationsWithAltid1 - 7",jsCard.getOrganizations()[0].getLocalizations().get("it").equals("ABC, Spa.;Divisione Nord America;Marketing"));
    }

    @Test
    public void testOrganizationsWithoutAltid() throws IOException, CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOrganizationsWithoutAltid - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationsWithoutAltid - 2",jsCard.getOrganizations().length == 2);
        assertTrue("testOrganizationsWithoutAltid - 3",jsCard.getOrganizations()[0].getValue().equals("ABC, Inc.;North American Division;Marketing"));
        assertTrue("testOrganizationsWithoutAltid - 4",jsCard.getOrganizations()[0].getLanguage() == null);
        assertTrue("testOrganizationsWithoutAltid - 5",jsCard.getOrganizations()[0].getLocalizations() == null);
        assertTrue("testOrganizationsWithoutAltid - 6",jsCard.getOrganizations()[1].getValue().equals("ABC, Spa.;Divisione Nord America;Marketing"));
        assertTrue("testOrganizationsWithoutAltid - 7",jsCard.getOrganizations()[1].getLanguage().equals("it"));
        assertTrue("testOrganizationsWithoutAltid - 8",jsCard.getOrganizations()[1].getLocalizations() == null);
    }


    @Test
    public void testOrganizationsWithAltid2() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {\"altid\" : \"1\"}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"], " +
                "[\"org\", {\"pref\" : \"1\"}, \"text\", \"University of North America\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testOrganizationsWithAltid2 - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationsWithAltid2 - 2",jsCard.getOrganizations().length == 2);
        assertTrue("testOrganizationsWithAltid2 - 3",jsCard.getOrganizations()[1].getValue().equals("ABC, Inc.;North American Division;Marketing"));
        assertTrue("testOrganizationsWithAltid2 - 4",jsCard.getOrganizations()[1].getLanguage() == null);
        assertTrue("testOrganizationsWithAltid2 - 5",jsCard.getOrganizations()[1].getLocalizations() != null);
        assertTrue("testOrganizationsWithAltid2 - 6",jsCard.getOrganizations()[1].getLocalizations().size() == 1);
        assertTrue("testOrganizationsWithAltid2 - 7",jsCard.getOrganizations()[1].getLocalizations().get("it").equals("ABC, Spa.;Divisione Nord America;Marketing"));
        assertTrue("testOrganizationsWithAltid2 - 8",jsCard.getOrganizations()[0].getValue().equals("University of North America"));
    }


}