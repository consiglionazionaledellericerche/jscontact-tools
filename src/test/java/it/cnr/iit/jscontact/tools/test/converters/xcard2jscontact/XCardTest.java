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
package it.cnr.iit.jscontact.tools.test.converters.xcard2jscontact;

import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

public class XCardTest extends XCard2JSContactTest {

    @Test
    public void testCompleteXCard1() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("xcard/xCard-RFC6351.xml"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) xCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteXCard1 - 1", jsCard.getFullName().getValue().equals("Simon Perreault"));
        assertTrue("testCompleteXCard1 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteXCard1 - 3", jsCard.getName().length == 4);
        assertTrue("testCompleteXCard1 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteXCard1 - 5", jsCard.getName()[0].getValue().equals("Perreault"));
        assertTrue("testCompleteXCard1 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteXCard1 - 7", jsCard.getName()[1].getValue().equals("Simon"));
        assertTrue("testCompleteXCard1 - 8", jsCard.getName()[2].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteXCard1 - 9", jsCard.getName()[2].getValue().equals("ing. jr"));
        assertTrue("testCompleteXCard1 - 10", jsCard.getName()[3].getType() == NameComponentType.SUFFIX);
        assertTrue("testCompleteXCard1 - 11", jsCard.getName()[3].getValue().equals("M.Sc."));
        assertTrue("testCompleteXCard1 - 12", jsCard.getAnniversaries().length==2);
        assertTrue("testCompleteXCard1 - 13", jsCard.getAnniversaries()[0].getType() == AnniversaryType.BIRTH);
        assertTrue("testCompleteXCard1 - 14", jsCard.getAnniversaries()[0].getDate().equals("--02-03"));
        assertTrue("testCompleteXCard1 - 15", jsCard.getAnniversaries()[1].getType() == AnniversaryType.OTHER);
        assertTrue("testCompleteXCard1 - 16", jsCard.getAnniversaries()[1].getLabel().equals("marriage date"));
        assertTrue("testCompleteXCard1 - 17", jsCard.getAnniversaries()[1].getDate().equals("2009-08-08T14:30-05:00"));
        assertTrue("testCompleteXCard1 - 18", jsCard.getPreferredContactLanguages().size()==2);
        assertTrue("testCompleteXCard1 - 19", jsCard.getPreferredContactLanguages().get("fr")[0].getPref() == 1);
        assertTrue("testCompleteXCard1 - 20", jsCard.getPreferredContactLanguages().get("en")[0].getPref() == 2);
        assertTrue("testCompleteXCard1 - 21", jsCard.getOrganizations().get("ORG-1").getName().getValue().equals("Viagenie"));
        assertTrue("testCompleteXCard1 - 22", jsCard.getAddresses().size() == 1);
        assertTrue("testCompleteXCard1 - 23", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("Simon Perreault\n                    2875 boul. Laurier, suite D2-630\n                    Quebec, QC, Canada\n                    G1V 2M2"));
        assertTrue("testCompleteXCard1 - 25", jsCard.getAddresses().get("ADR-1").getStreet().equals("2875 boul. Laurier, suite D2-630"));
        assertTrue("testCompleteXCard1 - 26", jsCard.getAddresses().get("ADR-1").getLocality().equals("Quebec"));
        assertTrue("testCompleteXCard1 - 27", jsCard.getAddresses().get("ADR-1").getRegion().equals("QC"));
        assertTrue("testCompleteXCard1 - 28", jsCard.getAddresses().get("ADR-1").getCountry().equals("Canada"));
        assertTrue("testCompleteXCard1 - 29", jsCard.getAddresses().get("ADR-1").getPostcode().equals("G1V 2M2"));
        assertTrue("testCompleteXCard1 - 30", jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.766336,-71.28955"));
        assertTrue("testCompleteXCard1 - 31", jsCard.getAddresses().get("ADR-1").getTimeZone().equals("America/Montreal"));
        assertTrue("testCompleteXCard1 - 32", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteXCard1 - 33", jsCard.getPhones().get("PHONE-1").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteXCard1 - 34", jsCard.getPhones().get("PHONE-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteXCard1 - 35", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-418-656-9254;ext=102"));
        assertTrue("testCompleteXCard1 - 36", jsCard.getPhones().get("PHONE-1").getPref() == null);
        assertTrue("testCompleteXCard1 - 37", jsCard.getPhones().get("PHONE-2").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteXCard1 - 38", jsCard.getPhones().get("PHONE-2").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteXCard1 - 39", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-418-262-6501"));
        assertTrue("testCompleteXCard1 - 40", jsCard.getPhones().get("PHONE-2").getLabel().equals("text,cell,video"));
        assertTrue("testCompleteXCard1 - 44", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteXCard1 - 45", jsCard.getEmails().get("EMAIL-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteXCard1 - 46", jsCard.getEmails().get("EMAIL-1").getEmail().equals("simon.perreault@viagenie.ca"));
        assertTrue("testCompleteXCard1 - 47", jsCard.getOnline().size() == 2);
        assertTrue("testCompleteXCard1 - 48", jsCard.getOnline().get("KEY-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteXCard1 - 49", jsCard.getOnline().get("KEY-1").getType() == ResourceType.URI);
        assertTrue("testCompleteXCard1 - 50", jsCard.getOnline().get("KEY-1").getLabel().equals(OnlineLabelKey.KEY.getValue()));
        assertTrue("testCompleteXCard1 - 51", jsCard.getOnline().get("KEY-1").getResource().equals("http://www.viagenie.ca/simon.perreault/simon.asc"));
        assertTrue("testCompleteXCard1 - 52", jsCard.getOnline().get("URL-1").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteXCard1 - 53", jsCard.getOnline().get("URL-1").getType() == ResourceType.URI);
        assertTrue("testCompleteXCard1 - 54", jsCard.getOnline().get("URL-1").getLabel().equals("url"));
        assertTrue("testCompleteXCard1 - 55", jsCard.getOnline().get("URL-1").getResource().equals("http://nomis80.org"));
        assertTrue("testCompleteXCard1 - 56", StringUtils.isNotEmpty(jsCard.getUid()));

    }

    @Test
    public void testCompleteXCard2() throws IOException, CardException {

        String vcard = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("xcard/xCard-Wikipedia.xml"), Charset.forName("UTF-8"));
        JSCard jsCard = (JSCard) xCard2JSContact.convert(vcard).get(0);
        assertTrue("testCompleteXCard2 - 1", jsCard.getFullName().getValue().equals("Forrest Gump"));
        assertTrue("testCompleteXCard2 - 2", jsCard.getKind() == null);
        assertTrue("testCompleteXCard2 - 3", jsCard.getName().length == 3);
        assertTrue("testCompleteXCard2 - 4", jsCard.getName()[0].getType() == NameComponentType.SURNAME);
        assertTrue("testCompleteXCard2 - 5", jsCard.getName()[0].getValue().equals("Gump"));
        assertTrue("testCompleteXCard2 - 6", jsCard.getName()[1].getType() == NameComponentType.PERSONAL);
        assertTrue("testCompleteXCard2 - 7", jsCard.getName()[1].getValue().equals("Forrest"));
        assertTrue("testCompleteXCard2 - 8", jsCard.getName()[2].getType() == NameComponentType.PREFIX);
        assertTrue("testCompleteXCard2 - 9", jsCard.getName()[2].getValue().equals("Mr."));
        assertTrue("testCompleteXCard2 - 10", jsCard.getOrganizations().get("ORG-1").getName().getValue().equals("Bubba Gump Shrimp Co."));
        assertTrue("testCompleteXCard2 - 11", jsCard.getJobTitles().get("TITLE-1").getTitle().getValue().equals("Shrimp Man"));
        assertTrue("testCompleteXCard2 - 15", jsCard.getPhotos().get("PHOTO-1").getHref().equals("http://www.example.com/dir_photos/my_photo.gif"));
        assertTrue("testCompleteXCard2 - 16", jsCard.getPhotos().get("PHOTO-1").getMediaType().equals("image/gif"));
        assertTrue("testCompleteXCard2 - 17", jsCard.getPhones().size() == 2);
        assertTrue("testCompleteXCard2 - 18", jsCard.getPhones().get("PHONE-1").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteXCard2 - 19", jsCard.getPhones().get("PHONE-1").getContexts().containsKey(Context.WORK));
        assertTrue("testCompleteXCard2 - 20", jsCard.getPhones().get("PHONE-1").getPhone().equals("tel:+1-111-555-1212"));
        assertTrue("testCompleteXCard2 - 21", jsCard.getPhones().get("PHONE-1").getLabel() == null);
        assertTrue("testCompleteXCard2 - 22", jsCard.getPhones().get("PHONE-2").getFeatures().containsKey(PhoneType.VOICE));
        assertTrue("testCompleteXCard2 - 23", jsCard.getPhones().get("PHONE-2").getContexts().containsKey(Context.PRIVATE));
        assertTrue("testCompleteXCard2 - 24", jsCard.getPhones().get("PHONE-2").getPhone().equals("tel:+1-404-555-1212"));
        assertTrue("testCompleteXCard2 - 25", jsCard.getPhones().get("PHONE-2").getLabel() == null);

        assertTrue("testCompleteXCard2 - 26", jsCard.getAddresses().size() == 2);
        assertTrue("testCompleteXCard2 - 27", jsCard.getAddresses().get("ADR-1").getContexts().containsKey(AddressContext.WORK));
        assertTrue("testCompleteXCard2 - 28", jsCard.getAddresses().get("ADR-1").getPref() == 1);
//        assertTrue("testCompleteXCard2 - 29", jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("100 Waters Edge\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertTrue("testCompleteXCard2 - 30", jsCard.getAddresses().get("ADR-1").getStreet().equals("100 Waters Edge"));
        assertTrue("testCompleteXCard2 - 31", jsCard.getAddresses().get("ADR-1").getLocality().equals("Baytown"));
        assertTrue("testCompleteXCard2 - 32", jsCard.getAddresses().get("ADR-1").getRegion().equals("LA"));
        assertTrue("testCompleteXCard2 - 33", jsCard.getAddresses().get("ADR-1").getCountry().equals("United States of America"));
        assertTrue("testCompleteXCard2 - 34", jsCard.getAddresses().get("ADR-1").getPostcode().equals("30314"));
        assertTrue("testCompleteXCard2 - 35", jsCard.getAddresses().get("ADR-2").getContexts().containsKey(AddressContext.PRIVATE));
//        assertTrue("testCompleteXCard2 - 36", jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("42 Plantation St.\\nBaytown\\, LA 30314\\nUnited States of America"));
        assertTrue("testCompleteXCard2 - 37", jsCard.getAddresses().get("ADR-2").getStreet().equals("42 Plantation St."));
        assertTrue("testCompleteXCard2 - 38", jsCard.getAddresses().get("ADR-2").getLocality().equals("Baytown"));
        assertTrue("testCompleteXCard2 - 39", jsCard.getAddresses().get("ADR-2").getRegion().equals("LA"));
        assertTrue("testCompleteXCard2 - 40", jsCard.getAddresses().get("ADR-2").getCountry().equals("United States of America"));
        assertTrue("testCompleteXCard2 - 41", jsCard.getAddresses().get("ADR-2").getPostcode().equals("30314"));

        assertTrue("testCompleteXCard2 - 42", jsCard.getEmails().size() == 1);
        assertTrue("testCompleteXCard2 - 43", jsCard.getEmails().get("EMAIL-1").getEmail().equals("forrestgump@example.com"));
        assertTrue("testCompleteXCard2 - 44", jsCard.getUpdated().compareTo(VCardDateFormat.parseAsCalendar("2008-04-24T19:52:43Z"))==0);
        assertTrue("testCompleteXCard2 - 45", StringUtils.isNotEmpty(jsCard.getUid()));
    }

}
