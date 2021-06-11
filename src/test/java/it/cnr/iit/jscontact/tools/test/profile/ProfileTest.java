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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact.JCard2JSContactTest;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactIdsProfile;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

public class ProfileTest extends JCard2JSContactTest {


    @Test
    public void testRDAPProfile() throws IOException, CardException {

        JCard2JSContact jCard2JSContact = JCard2JSContact.builder()
                                                         .config(VCard2JSContactConfig.builder()
                                                                                      .applyAutoIdsProfile(false)
                                                                                      .idsProfileToApply(VCard2JSContactIdsProfile.RDAP_PROFILE)
                                                                                      .build())
                                                         .build();
        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jCard-RFC7483.json"), Charset.forName("UTF-8"));
        Card jsCard = (Card) jCard2JSContact.convert(json).get(0);
        assertTrue("testRDAPProfile - 1", jsCard.getFullName().getValue().equals("Joe User"));
        assertTrue("testRDAPProfile - 2", jsCard.getKind().isIndividual());
        assertTrue("testRDAPProfile - 3", jsCard.getName().length == 4);
        assertTrue("testRDAPProfile - 4", jsCard.getName()[0].isPersonal());
        assertTrue("testRDAPProfile - 5", jsCard.getName()[0].getValue().equals("Joe"));
        assertTrue("testRDAPProfile - 6", jsCard.getName()[1].isSurname());
        assertTrue("testRDAPProfile - 7", jsCard.getName()[1].getValue().equals("User"));
        assertTrue("testRDAPProfile - 8", jsCard.getName()[2].isSuffix());
        assertTrue("testRDAPProfile - 9", jsCard.getName()[2].getValue().equals("ing. jr"));
        assertTrue("testRDAPProfile - 10", jsCard.getName()[3].isSuffix());
        assertTrue("testRDAPProfile - 11", jsCard.getName()[3].getValue().equals("M.Sc."));
        assertTrue("testRDAPProfile - 12", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testRDAPProfile - 13", jsCard.getPreferredContactLanguages().get("fr")[0].getPref() == 1);
        assertTrue("testRDAPProfile - 14", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
        assertTrue("testRDAPProfile - 15", jsCard.getOrganizations().get("org").getName().getValue().equals("Example"));
        assertTrue("testRDAPProfile - 16", jsCard.getTitles().get("TITLE-1").getTitle().getValue().equals("Research Scientist"));
        assertTrue("testRDAPProfile - 17", jsCard.getTitles().get("TITLE-2").getTitle().getValue().equals("Project Lead"));
        assertTrue("testRDAPProfile - 18", jsCard.getAddresses().size() == 2);
        assertTrue("testRDAPProfile - 19", jsCard.getAddresses().get("int").getFullAddress().getValue().equals("Suite 1234\n4321 Rue Somewhere\nQuebec\nQC\nG1V 2M2\nCanada"));
        assertTrue("testRDAPProfile - 20", jsCard.getAddresses().get("int").getStreetExtensions().equals("Suite 1234"));
        assertTrue("testRDAPProfile - 21", jsCard.getAddresses().get("int").getStreetDetails().equals("4321 Rue Somewhere"));
        assertTrue("testRDAPProfile - 22", jsCard.getAddresses().get("int").getLocality().equals("Quebec"));
        assertTrue("testRDAPProfile - 23", jsCard.getAddresses().get("int").getRegion().equals("QC"));
        assertTrue("testRDAPProfile - 24", jsCard.getAddresses().get("int").getCountry().equals("Canada"));
        assertTrue("testRDAPProfile - 25", jsCard.getAddresses().get("int").getPostcode().equals("G1V 2M2"));
        assertTrue("testRDAPProfile - 26", jsCard.getAddresses().get("int").getCoordinates().equals("geo:46.772673,-71.282945"));
        assertTrue("testRDAPProfile - 27", jsCard.getAddresses().get("int").getTimeZone().equals("Etc/GMT+5"));
        assertTrue("testRDAPProfile - 28", jsCard.getAddresses().get("loc").getFullAddress().getValue().equals("123 Maple Ave\nSuite 90001\nVancouver\nBC\n1239\n"));
        assertTrue("testRDAPProfile - 29", jsCard.getEmails().size() == 1);
        assertTrue("testRDAPProfile - 30", jsCard.getEmails().get("email").asWork());
        assertTrue("testRDAPProfile - 31", jsCard.getEmails().get("email").getEmail().equals("joe.user@example.com"));
        assertTrue("testRDAPProfile - 32", jsCard.getPhones().size() == 2);
        assertTrue("testRDAPProfile - 33", jsCard.getPhones().get("voice").asVoice());
        assertTrue("testRDAPProfile - 34", jsCard.getPhones().get("voice").getPhone().equals("tel:+1-555-555-1234;ext=102"));
        assertTrue("testRDAPProfile - 35", jsCard.getPhones().get("voice").getPref() == 1);
        assertTrue("testRDAPProfile - 36", jsCard.getPhones().get("voice").asWork());
        assertTrue("testRDAPProfile - 37", jsCard.getPhones().get("voice").getLabel() == null);
        assertTrue("testRDAPProfile - 38", jsCard.getPhones().get("fax").asVoice());
        assertTrue("testRDAPProfile - 39", jsCard.getPhones().get("fax").getPhone().equals("tel:+1-555-555-4321"));
        assertTrue("testRDAPProfile - 40", jsCard.getPhones().get("fax").getPref() == null);
        assertTrue("testRDAPProfile - 41", jsCard.getPhones().get("fax").asWork());
        assertTrue("testRDAPProfile - 42", jsCard.getPhones().get("fax").asCell());
        assertTrue("testRDAPProfile - 43", jsCard.getPhones().get("fax").asVideo());
        assertTrue("testRDAPProfile - 44", jsCard.getPhones().get("fax").asText());
        assertTrue("testRDAPProfile - 46", jsCard.getOnline().size() == 2);
        assertTrue("testRDAPProfile - 47", jsCard.getOnline().get("KEY-1").isUri());
        assertTrue("testRDAPProfile - 48", jsCard.getOnline().get("KEY-1").getResource().equals("http://www.example.com/joe.user/joe.asc"));
        assertTrue("testRDAPProfile - 49", jsCard.getOnline().get("KEY-1").getPref() == null);
        assertTrue("testRDAPProfile - 50", jsCard.getOnline().get("KEY-1").asWork());
        assertTrue("testRDAPProfile - 52", jsCard.getOnline().get("KEY-1").asKey());
        assertTrue("testRDAPProfile - 53", jsCard.getOnline().get("URL-1").isUri());
        assertTrue("testRDAPProfile - 54", jsCard.getOnline().get("URL-1").getResource().equals("http://example.org"));
        assertTrue("testRDAPProfile - 55", jsCard.getOnline().get("URL-1").getPref() == null);
        assertTrue("testRDAPProfile - 56", jsCard.getOnline().get("URL-1").asPrivate());
        assertTrue("testRDAPProfile - 58", jsCard.getOnline().get("URL-1").asUrl());
        assertTrue("testRDAPProfile - 59", StringUtils.isNotEmpty(jsCard.getUid()));

    }


}
