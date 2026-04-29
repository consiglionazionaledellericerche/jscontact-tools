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
import it.cnr.iit.jscontact.tools.rdap.RdapJSContactProfileIds;
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
                        .profileIdsToUse(RdapJSContactProfileIds.getInstance())
                        .build())
                .build();
        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RDAP-profile.json")), StandardCharsets.UTF_8);
        Card jsCard = jCard2JSContact.convert(json).get(0);
        assertEquals("testRDAPProfile - 1", "Joe User", jsCard.getName().getFull());
        assertTrue("testRDAPProfile - 2", jsCard.getKind().isIndividual());
        assertEquals("testRDAPProfile - 3", 2, jsCard.getName().getComponents().length);
        assertTrue("testRDAPProfile - 4", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testRDAPProfile - 5", "Joe", jsCard.getName().getGiven());
        assertTrue("testRDAPProfile - 6", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testRDAPProfile - 7", "User", jsCard.getName().getSurname());
        assertEquals("testRDAPProfile - 8", "Example", jsCard.getOrganizations().get("org").getName());
        assertEquals("testRDAPProfile - 9", 1, jsCard.getAddresses().size());
        assertEquals("testRDAPProfile - 10", "Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada", jsCard.getAddresses().get("addr").getFull());
        assertEquals("testRDAPProfile - 11", "Suite 1234", jsCard.getAddresses().get("addr").getStreetExtendedAddress());
        assertEquals("testRDAPProfile - 12", "4321 Rue Somewhere", jsCard.getAddresses().get("addr").getStreetAddress());
        assertEquals("testRDAPProfile - 13", "Quebec", jsCard.getAddresses().get("addr").getLocality());
        assertEquals("testRDAPProfile - 14", "QC", jsCard.getAddresses().get("addr").getRegion());
        assertEquals("testRDAPProfile - 15", "Canada", jsCard.getAddresses().get("addr").getCountry());
        assertEquals("testRDAPProfile - 16", "G1V 2M2", jsCard.getAddresses().get("addr").getPostcode());
        assertEquals("testRDAPProfile - 17", 2, jsCard.getEmails().size());
        assertEquals("testRDAPProfile - 18", "joe.user@example.com", jsCard.getEmails().get("email").getAddress());
        assertEquals("testRDAPProfile - 19", "joe.user@example.net", jsCard.getEmails().get("email-1").getAddress());
        assertEquals("testRDAPProfile - 20", 2, jsCard.getPhones().size());
        assertTrue("testRDAPProfile - 21", jsCard.getPhones().get("voice").asVoice());
        assertEquals("testRDAPProfile - 22", "tel:+1-555-555-1234;ext=102", jsCard.getPhones().get("voice").getNumber());
        assertTrue("testRDAPProfile - 23", jsCard.getPhones().get("fax").asFax());
        assertEquals("testRDAPProfile - 24", "tel:+1-555-555-4321", jsCard.getPhones().get("fax").getNumber());
        assertEquals("testRDAPProfile - 25", 1, jsCard.getLinks().size());
        assertEquals("testRDAPProfile - 26", "http://example.org", jsCard.getLinks().get("url").getUri());
        assertTrue("testRDAPProfile - 27", jsCard.getLinks().get("url").isGenericLink());
        assertTrue("testRDAPProfile - 28", StringUtils.isEmpty(jsCard.getUid()));
    }


}
