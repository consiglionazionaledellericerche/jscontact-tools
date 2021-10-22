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

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class OrganizationsTest extends VCard2JSContactTest {

    @Test
    public void testOrganizationsWithAltid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG;ALTID=1:ABC, Inc.;North American Division;Marketing\n" +
                "ORG;ALTID=1;LANGUAGE=it:ABC, Spa.;Divisione Nord America;Marketing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOrganizationsWithAltid1 - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationsWithAltid1 - 2",jsCard.getOrganizations().size() == 1);
        assertTrue("testOrganizationsWithAltid1 - 3",jsCard.getOrganizations().get("ORG-1").getName().equals("ABC, Inc."));
        assertTrue("testOrganizationsWithAltid1 - 4",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testOrganizationsWithAltid1 - 5",jsCard.getLocalization("it", "/organizations/ORG-1").get("name").asText().equals("ABC, Spa."));
        assertTrue("testOrganizationsWithAltid1 - 6",jsCard.getOrganizations().get("ORG-1").getUnits() != null);
        assertTrue("testOrganizationsWithAltid1 - 7",jsCard.getOrganizations().get("ORG-1").getUnits().length == 2);
        assertTrue("testOrganizationsWithAltid1 - 8",jsCard.getOrganizations().get("ORG-1").getUnits()[0].equals("North American Division"));
        assertTrue("testOrganizationsWithAltid1 - 9",jsCard.getLocalization("it","/organizations/ORG-1").get("units").get(0).asText().equals("Divisione Nord America"));
        assertTrue("testOrganizationsWithAltid1 - 10",jsCard.getOrganizations().get("ORG-1").getUnits()[1].equals("Marketing"));
        assertTrue("testOrganizationsWithAltid1 - 11",jsCard.getLocalization("it","/organizations/ORG-1").get("units").get(1).asText().equals("Marketing"));
    }

    @Test
    public void testOrganizationsWithoutAltid() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG:ABC, Inc.;North American Division;Marketing\n" +
                "ORG;LANGUAGE=it:ABC, Spa.;Divisione Nord America;Marketing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOrganizationsWithoutAltid - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationsWithoutAltid - 2",jsCard.getOrganizations().size() == 2);
        assertTrue("testOrganizationsWithoutAltid - 3",jsCard.getOrganizations().get("ORG-1").getName().equals("ABC, Inc."));
        assertTrue("testOrganizationsWithoutAltid - 4",jsCard.getOrganizations().get("ORG-1").getUnits().length == 2);
        assertTrue("testOrganizationsWithoutAltid - 5",jsCard.getOrganizations().get("ORG-1").getUnits()[0].equals("North American Division"));
        assertTrue("testOrganizationsWithoutAltid - 6",jsCard.getOrganizations().get("ORG-1").getUnits()[1].equals("Marketing"));
        assertTrue("testOrganizationsWithoutAltid - 7",jsCard.getOrganizations().get("ORG-2").getName().equals("ABC, Spa."));
        assertTrue("testOrganizationsWithoutAltid - 8",jsCard.getOrganizations().get("ORG-2").getUnits().length == 2);
        assertTrue("testOrganizationsWithoutAltid - 9",jsCard.getOrganizations().get("ORG-2").getUnits()[0].equals("Divisione Nord America"));
        assertTrue("testOrganizationsWithoutAltid - 10",jsCard.getOrganizations().get("ORG-2").getUnits()[1].equals("Marketing"));
    }

    @Test
    public void testOrganizationsWithAltid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG;ALTID=1:ABC, Inc.;North American Division;Marketing\n" +
                "ORG;ALTID=1;LANGUAGE=it:ABC, Spa.;Divisione Nord America;Marketing\n" +
                "ORG;PREF=1:University of North America\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOrganizationsWithAltid2 - 1",jsCard.getOrganizations()!=null);
        assertTrue("testOrganizationsWithAltid2 - 2",jsCard.getOrganizations().size() == 2);
        assertTrue("testOrganizationsWithAltid2 - 3",jsCard.getOrganizations().get("ORG-1").getName().equals("University of North America"));
        assertTrue("testOrganizationsWithAltid2 - 6",jsCard.getOrganizations().get("ORG-2").getName().equals("ABC, Inc."));
        assertTrue("testOrganizationsWithAltid2 - 7",jsCard.getLocalizationsPerLanguage("it").size() == 1);
        assertTrue("testOrganizationsWithAltid2 - 8",jsCard.getLocalization("it","/organizations/ORG-2").get("name").asText().equals("ABC, Spa."));
        assertTrue("testOrganizationsWithAltid2 - 9",jsCard.getOrganizations().get("ORG-2").getUnits() != null);
        assertTrue("testOrganizationsWithAltid2 - 10",jsCard.getOrganizations().get("ORG-2").getUnits().length == 2);
        assertTrue("testOrganizationsWithAltid2 - 11",jsCard.getOrganizations().get("ORG-2").getUnits()[0].equals("North American Division"));
        assertTrue("testOrganizationsWithAltid2 - 12",jsCard.getLocalization("it","/organizations/ORG-2").get("units").get(0).asText().equals("Divisione Nord America"));
        assertTrue("testOrganizationsWithAltid2 - 13",jsCard.getOrganizations().get("ORG-2").getUnits()[1].equals("Marketing"));
        assertTrue("testOrganizationsWithAltid2 - 14",jsCard.getLocalization("it","/organizations/ORG-2").get("units").get(1).asText().equals("Marketing"));
    }


}
