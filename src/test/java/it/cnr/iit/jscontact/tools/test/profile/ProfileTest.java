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
package it.cnr.iit.jscontact.tools.test.profile;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.rdap.RdapJSContactIdsProfile;
import it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact.JCard2JSContactTest;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.*;

public class ProfileTest extends JCard2JSContactTest {


    @Test
    public void testRDAPProfile() throws IOException, CardException {

        JCard2JSContact jCard2JSContact = JCard2JSContact.builder()
                        .config(VCard2JSContactConfig.builder()
                        .idsProfileToUse(RdapJSContactIdsProfile.getInstance())
                        .build())
                .build();
        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7483.json")), StandardCharsets.UTF_8);
        Card jsCard = jCard2JSContact.convert(json).get(0);
        assertEquals("testRDAPProfile - 1", "Joe User", jsCard.getName().getFull());
        assertTrue("testRDAPProfile - 2", jsCard.getKind().isIndividual());
        assertEquals("testRDAPProfile - 3", 4, jsCard.getName().getComponents().length);
        assertTrue("testRDAPProfile - 4", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testRDAPProfile - 5", "Joe", jsCard.getName().getGiven());
        assertTrue("testRDAPProfile - 6", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testRDAPProfile - 7", "User", jsCard.getName().getSurname());
        assertTrue("testRDAPProfile - 8", jsCard.getName().getComponents()[2].isCredential());
        assertEquals("testRDAPProfile - 9", "ing. jr", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testRDAPProfile - 10", jsCard.getName().getComponents()[3].isCredential());
        assertEquals("testRDAPProfile - 11", "M.Sc.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testRDAPProfile - 12", 2, jsCard.getPreferredLanguages().size());
        assertEquals("testRDAPProfile - 13", 1, (int) jsCard.getPreferredLanguages().get("LANG-1").getPref());
        assertEquals("testRDAPProfile - 14", 2, (int) jsCard.getPreferredLanguages().get("LANG-2").getPref());
        assertEquals("testRDAPProfile - 13", "fr", jsCard.getPreferredLanguages().get("LANG-1").getLanguage());
        assertEquals("testRDAPProfile - 14", "en", jsCard.getPreferredLanguages().get("LANG-2").getLanguage());
        assertEquals("testRDAPProfile - 15", "Example", jsCard.getOrganizations().get("org").getName());
        assertEquals("testRDAPProfile - 16", "Research Scientist", jsCard.getTitles().get("TITLE-1").getName());
        assertEquals("testRDAPProfile - 17", "Project Lead", jsCard.getTitles().get("TITLE-2").getName());
        assertEquals("testRDAPProfile - 18", 1, jsCard.getAddresses().size());
        assertEquals("testRDAPProfile - 19", "Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada", jsCard.getAddresses().get("addr").getFull());
        assertEquals("testRDAPProfile - 20", "Suite 1234", jsCard.getAddresses().get("addr").getStreetExtendedAddress());
        assertEquals("testRDAPProfile - 21", "4321 Rue Somewhere", jsCard.getAddresses().get("addr").getStreetAddress());
        assertEquals("testRDAPProfile - 22", "Quebec", jsCard.getAddresses().get("addr").getLocality());
        assertEquals("testRDAPProfile - 23", "QC", jsCard.getAddresses().get("addr").getRegion());
        assertEquals("testRDAPProfile - 24", "Canada", jsCard.getAddresses().get("addr").getCountry());
        assertEquals("testRDAPProfile - 25", "G1V 2M2", jsCard.getAddresses().get("addr").getPostcode());
        assertEquals("testRDAPProfile - 26", "geo:46.772673,-71.282945", jsCard.getAddresses().get("addr").getCoordinates());
        assertEquals("testRDAPProfile - 27", "Etc/GMT+5", jsCard.getAddresses().get("addr").getTimeZone());
        assertEquals("testRDAPProfile - 29", 1, jsCard.getEmails().size());
        assertTrue("testRDAPProfile - 30", jsCard.getEmails().get("email").asWork());
        assertEquals("testRDAPProfile - 31", "joe.user@example.com", jsCard.getEmails().get("email").getAddress());
        assertEquals("testRDAPProfile - 32", 2, jsCard.getPhones().size());
        assertTrue("testRDAPProfile - 33", jsCard.getPhones().get("voice").asVoice());
        assertEquals("testRDAPProfile - 34", "tel:+1-555-555-1234;ext=102", jsCard.getPhones().get("voice").getNumber());
        assertEquals("testRDAPProfile - 35", 1, (int) jsCard.getPhones().get("voice").getPref());
        assertTrue("testRDAPProfile - 36", jsCard.getPhones().get("voice").asWork());
        assertNull("testRDAPProfile - 37", jsCard.getPhones().get("voice").getLabel());
        assertTrue("testRDAPProfile - 38", jsCard.getPhones().get("fax").asVoice());
        assertEquals("testRDAPProfile - 39", "tel:+1-555-555-4321", jsCard.getPhones().get("fax").getNumber());
        assertNull("testRDAPProfile - 40", jsCard.getPhones().get("fax").getPref());
        assertTrue("testRDAPProfile - 41", jsCard.getPhones().get("fax").asWork());
        assertTrue("testRDAPProfile - 42", jsCard.getPhones().get("fax").asMobile());
        assertTrue("testRDAPProfile - 43", jsCard.getPhones().get("fax").asVideo());
        assertTrue("testRDAPProfile - 44", jsCard.getPhones().get("fax").asText());
        assertEquals("testRDAPProfile - 45", 1, jsCard.getCryptoKeys().size());
        assertEquals("testRDAPProfile - 46", "http://www.example.com/joe.user/joe.asc", jsCard.getCryptoKeys().get("KEY-1").getUri());
        assertNull("testRDAPProfile - 47", jsCard.getCryptoKeys().get("KEY-1").getPref());
        assertTrue("testRDAPProfile - 48", jsCard.getCryptoKeys().get("KEY-1").asWork());
        assertEquals("testRDAPProfile - 45", 1, jsCard.getLinks().size());
        assertEquals("testRDAPProfile - 50", "http://example.org", jsCard.getLinks().get("LINK-1").getUri());
        assertNull("testRDAPProfile - 51", jsCard.getLinks().get("LINK-1").getPref());
        assertTrue("testRDAPProfile - 52", jsCard.getLinks().get("LINK-1").asPrivate());
        assertTrue("testRDAPProfile - 53", jsCard.getLinks().get("LINK-1").isGenericLink());
        assertTrue("testRDAPProfile - 54", StringUtils.isNotEmpty(jsCard.getUid()));

    }


}
