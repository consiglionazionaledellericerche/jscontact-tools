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

import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrganizationsTest extends JCard2JSContactTest {

    @Test
    public void testOrganizationsWithAltid1() throws CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {\"altid\" : \"1\",\"sort-as\":[\"ABC\"]}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testOrganizationsWithAltid1 - 1", jsCard.getOrganizations());
        assertEquals("testOrganizationsWithAltid1 - 2", 1, jsCard.getOrganizations().size());
        assertEquals("testOrganizationsWithAltid1 - 3", "ABC, Inc.", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testOrganizationsWithAltid1 - 4", 1, jsCard.getLocalizationsPerLanguage("it").size());
        assertEquals("testOrganizationsWithAltid1 - 5", "ABC, Spa.", jsCard.getLocalization("it", "organizations/ORG-1").get("name").asText());
        assertNotNull("testOrganizationsWithAltid1 - 6", jsCard.getOrganizations().get("ORG-1").getUnits());
        assertEquals("testOrganizationsWithAltid1 - 7", 2, jsCard.getOrganizations().get("ORG-1").getUnits().length);
        assertEquals("testOrganizationsWithAltid1 - 8", "North American Division", jsCard.getOrganizations().get("ORG-1").getUnits()[0]);
        assertEquals("testOrganizationsWithAltid1 - 9", "ABC", String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,jsCard.getOrganizations().get("ORG-1").getSortAs()));
        assertEquals("testOrganizationsWithAltid1 - 10", "Divisione Nord America", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(0).asText());
        assertEquals("testOrganizationsWithAltid1 - 11", "Marketing", jsCard.getOrganizations().get("ORG-1").getUnits()[1]);
        assertEquals("testOrganizationsWithAltid1 - 12", "Marketing", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(1).asText());
    }

    @Test
    public void testOrganizationsWithoutAltid1() throws CardException {
        
        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testOrganizationsWithoutAltid1 - 1", jsCard.getOrganizations());
        assertEquals("testOrganizationsWithoutAltid1 - 2", 2, jsCard.getOrganizations().size());
        assertEquals("testOrganizationsWithoutAltid1 - 3", "ABC, Inc.", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testOrganizationsWithoutAltid1 - 4", 2, jsCard.getOrganizations().get("ORG-1").getUnits().length);
        assertEquals("testOrganizationsWithoutAltid1 - 5", "North American Division", jsCard.getOrganizations().get("ORG-1").getUnits()[0]);
        assertEquals("testOrganizationsWithoutAltid1 - 6", "Marketing", jsCard.getOrganizations().get("ORG-1").getUnits()[1]);
        assertEquals("testOrganizationsWithoutAltid1 - 7", "ABC, Spa.", jsCard.getOrganizations().get("ORG-2").getName());
        assertEquals("testOrganizationsWithoutAltid1 - 8", 2, jsCard.getOrganizations().get("ORG-2").getUnits().length);
        assertEquals("testOrganizationsWithoutAltid1 - 9", "Divisione Nord America", jsCard.getOrganizations().get("ORG-2").getUnits()[0]);
        assertEquals("testOrganizationsWithoutAltid1 - 10", "Marketing", jsCard.getOrganizations().get("ORG-2").getUnits()[1]);
    }


    @Test
    public void testOrganizationsWithAltid2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {\"altid\" : \"1\"}, \"text\", \"ABC, Inc.;North American Division;Marketing\"]," +
                "[\"org\", {\"language\" : \"it\", \"altid\" : \"1\"}, \"text\", \"ABC, Spa.;Divisione Nord America;Marketing\"], " +
                "[\"org\", {\"pref\" : \"1\"}, \"text\", \"University of North America\"]" +
                "]]";
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
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

    @Test
    public void testOrganizationsWithAltid3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"org\", {\"altid\" : \"1\"}, \"text\", \";North American Division;Marketing\"]," +
                "[\"org\", {\"altid\" : \"1\", \"language\" : \"it\"}, \"text\", \";Divisione Nord America;Marketing\"]" +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertNotNull("testOrganizationsWithAltid3 - 1", jsCard.getOrganizations());
        assertEquals("testOrganizationsWithAltid3 - 2", 1, jsCard.getOrganizations().size());
        assertNull("testOrganizationsWithAltid3 - 3", jsCard.getOrganizations().get("ORG-1").getName());
        assertEquals("testOrganizationsWithAltid3 - 4", 2, jsCard.getOrganizations().get("ORG-1").getUnits().length);
        assertEquals("testOrganizationsWithAltid3 - 5", "North American Division", jsCard.getOrganizations().get("ORG-1").getUnits()[0]);
        assertEquals("testOrganizationsWithAltid3 - 6", "Marketing", jsCard.getOrganizations().get("ORG-1").getUnits()[1]);
        assertNull("testOrganizationsWithAltid3 - 7", jsCard.getLocalization("it", "organizations/ORG-1").get("name"));
        assertEquals("testOrganizationsWithAltid3 - 8", "Divisione Nord America", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(0).asText());
        assertEquals("testOrganizationsWithAltid3 - 9", "Marketing", jsCard.getLocalization("it", "organizations/ORG-1").get("units").get(1).asText());
    }


}
