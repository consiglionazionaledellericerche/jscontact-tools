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

public class AddressesTest extends VCard2JSContactTest {

    @Test
    public void testAddressesValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid1 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid1 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid1 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid1 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid1 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid1 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid1 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid1 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid1 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));

    }

    @Test
    public void testAddressesValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;LABEL=54321 Oak St Reston USA:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid2 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid2 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid2 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid2 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid2 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid2 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid2 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid2 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid2 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St Reston USA"));

    }

    @Test
    public void testAddressesValid3() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid3 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid3 - 2",jsCard.getAddresses().size() == 2);
        assertTrue("testAddressesValid3 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid3 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid3 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid3 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid3 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid3 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid3 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid3 - 10",jsCard.getAddresses().get("ADR-2").getCountryCode().equals("US"));
        assertTrue("testAddressesValid3 - 11",jsCard.getAddresses().get("ADR-2").getCountry().equals("USA"));
        assertTrue("testAddressesValid3 - 12",jsCard.getAddresses().get("ADR-2").getPostcode().equals("20190"));
        assertTrue("testAddressesValid3 - 13",jsCard.getAddresses().get("ADR-2").getLocality().equals("Reston"));
        assertTrue("testAddressesValid3 - 14",jsCard.getAddresses().get("ADR-2").getRegion().equals("VA"));
        assertTrue("testAddressesValid3 - 15",jsCard.getAddresses().get("ADR-2").getStreetDetails().equals("12345 Elm St"));
        assertTrue("testAddressesValid3 - 16",jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("12345 Elm St\nReston\nVA\n20190\nUSA"));

    }


    @Test
    public void testAddressesValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid4 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid4 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid4 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid4 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid4 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid4 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid4 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid4 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid4 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid4 - 10",jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));

    }

    @Test
    public void testAddressesValid5() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:-0500\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid5 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid5 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid5 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid5 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid5 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid5 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid5 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid5 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid5 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid5 - 10",jsCard.getAddresses().get("ADR-1").getTimeZone().equals("Etc/GMT+5"));
    }

    @Test
    public void testAddressesValid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:America/New_York\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid6 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid6 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid6 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid6 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid6 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid6 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid6 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid6 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid6 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid6 - 10",jsCard.getAddresses().get("ADR-1").getTimeZone().equals("America/New_York"));

    }

    @Test
    public void testAddressesValid7() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=America/New_York;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid7 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid7 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid7 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid7 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid7 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid7 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid7 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid7 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid7 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid7 - 10",jsCard.getAddresses().get("ADR-1").getTimeZone().equals("America/New_York"));
        assertTrue("testAddressesValid7 - 11",jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));

    }


    @Test
    public void testAddressesValid8() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "TZ;ALTID=1:America/New_York\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid8 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid8 - 2",jsCard.getAddresses().size() == 2);
        assertTrue("testAddressesValid8 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid8 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid8 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid8 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid8 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid8 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid8 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid8 - 10",jsCard.getAddresses().get("ADR-2").getCountryCode().equals("US"));
        assertTrue("testAddressesValid8 - 11",jsCard.getAddresses().get("ADR-2").getCountry().equals("USA"));
        assertTrue("testAddressesValid8 - 12",jsCard.getAddresses().get("ADR-2").getPostcode().equals("20190"));
        assertTrue("testAddressesValid8 - 13",jsCard.getAddresses().get("ADR-2").getLocality().equals("Reston"));
        assertTrue("testAddressesValid8 - 14",jsCard.getAddresses().get("ADR-2").getRegion().equals("VA"));
        assertTrue("testAddressesValid8 - 15",jsCard.getAddresses().get("ADR-2").getStreetDetails().equals("12345 Elm St"));
        assertTrue("testAddressesValid8 - 16",jsCard.getAddresses().get("ADR-2").getFullAddress().getValue().equals("12345 Elm St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid8 - 17",jsCard.getAddresses().get("ADR-2").getTimeZone().equals("America/New_York"));

    }

    @Test
    public void testAddressesValid9() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=-0500;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid9 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid9 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid9 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid9 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid9 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid9 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid9 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid9 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid9 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid9 - 10",jsCard.getAddresses().get("ADR-1").getTimeZone().equals("Etc/GMT+5"));
        assertTrue("testAddressesValid9 - 11",jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));

    }

    @Test
    public void testAddressesValid10() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:-0530\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid10 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid10 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid10 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid10 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid10 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid10 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid10 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid10 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid10 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid10 - 10",jsCard.getAddresses().get("ADR-1").getTimeZone().equals("Etc/GMT+5:30"));
    }

}
