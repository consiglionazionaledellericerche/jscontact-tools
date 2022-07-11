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

import static org.junit.Assert.*;

public class OrganizationsTest extends VCard2JSContactTest {

    @Test
    public void testOrganizationsWithAltid1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG;ALTID=1:ABC, Inc.;North American Division;Marketing\n" +
                "ORG;ALTID=1;LANGUAGE=it:ABC, Spa.;Divisione Nord America;Marketing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testOrganizationsWithAltid1 - 1", jsCard.getOrganizations());
        assertEquals("testOrganizationsWithAltid1 - 2", 1, jsCard.getOrganizations().size());
        assertEquals("testOrganizationsWithAltid1 - 3", "ABC, Inc.", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testOrganizationsWithAltid1 - 4", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testOrganizationsWithAltid1 - 5", "ABC, Spa.", jsCard.getLocalization("it", "organizations/ORG-1").get("name").asText());
        assertNotNull("testOrganizationsWithAltid1 - 6", jsCard.getOrganizations().get("ORG-1").getUnits());
        assertEquals("testOrganizationsWithAltid1 - 7", 2, jsCard.getOrganizations().get("ORG-1").getUnits().length);
        assertEquals("testOrganizationsWithAltid1 - 8", "North American Division", jsCard.getOrganizations().get("ORG-1").getUnits()[0]);
        assertEquals("testOrganizationsWithAltid1 - 9", "Divisione Nord America", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(0).asText());
        assertEquals("testOrganizationsWithAltid1 - 10", "Marketing", jsCard.getOrganizations().get("ORG-1").getUnits()[1]);
        assertEquals("testOrganizationsWithAltid1 - 11", "Marketing", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(1).asText());
    }

    @Test
    public void testOrganizationsWithoutAltid() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG:ABC, Inc.;North American Division;Marketing\n" +
                "ORG;LANGUAGE=it:ABC, Spa.;Divisione Nord America;Marketing\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testOrganizationsWithoutAltid - 1", jsCard.getOrganizations());
        assertEquals("testOrganizationsWithoutAltid - 2", 2, jsCard.getOrganizations().size());
        assertEquals("testOrganizationsWithoutAltid - 3", "ABC, Inc.", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testOrganizationsWithoutAltid - 4", 2, jsCard.getOrganizations().get("ORG-1").getUnits().length);
        assertEquals("testOrganizationsWithoutAltid - 5", "North American Division", jsCard.getOrganizations().get("ORG-1").getUnits()[0]);
        assertEquals("testOrganizationsWithoutAltid - 6", "Marketing", jsCard.getOrganizations().get("ORG-1").getUnits()[1]);
        assertEquals("testOrganizationsWithoutAltid - 7", "ABC, Spa.", jsCard.getOrganizations().get("ORG-2").getName());
        assertEquals("testOrganizationsWithoutAltid - 8", 2, jsCard.getOrganizations().get("ORG-2").getUnits().length);
        assertEquals("testOrganizationsWithoutAltid - 9", "Divisione Nord America", jsCard.getOrganizations().get("ORG-2").getUnits()[0]);
        assertEquals("testOrganizationsWithoutAltid - 10", "Marketing", jsCard.getOrganizations().get("ORG-2").getUnits()[1]);
    }

    @Test
    public void testOrganizationsWithAltid2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ORG;ALTID=1:ABC, Inc.;North American Division;Marketing\n" +
                "ORG;ALTID=1;LANGUAGE=it:ABC, Spa.;Divisione Nord America;Marketing\n" +
                "ORG;PREF=1:University of North America\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testOrganizationsWithAltid2 - 1", jsCard.getOrganizations());
        assertEquals("testOrganizationsWithAltid2 - 2", 2, jsCard.getOrganizations().size());
        assertEquals("testOrganizationsWithAltid2 - 3", "University of North America", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testOrganizationsWithAltid2 - 6", "ABC, Inc.", jsCard.getOrganizations().get("ORG-2").getName());
        assertEquals("testOrganizationsWithAltid2 - 7", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testOrganizationsWithAltid2 - 8", "ABC, Spa.", jsCard.getLocalization("it", "organizations/ORG-2").get("name").asText());
        assertNotNull("testOrganizationsWithAltid2 - 9", jsCard.getOrganizations().get("ORG-2").getUnits());
        assertEquals("testOrganizationsWithAltid2 - 10", 2, jsCard.getOrganizations().get("ORG-2").getUnits().length);
        assertEquals("testOrganizationsWithAltid2 - 11", "North American Division", jsCard.getOrganizations().get("ORG-2").getUnits()[0]);
        assertEquals("testOrganizationsWithAltid2 - 12", "Divisione Nord America", jsCard.getLocalization("it", "organizations/ORG-2").get("units").get(0).asText());
        assertEquals("testOrganizationsWithAltid2 - 13", "Marketing", jsCard.getOrganizations().get("ORG-2").getUnits()[1]);
        assertEquals("testOrganizationsWithAltid2 - 14", "Marketing", jsCard.getLocalization("it", "organizations/ORG-2").get("units").get(1).asText());
    }


}
