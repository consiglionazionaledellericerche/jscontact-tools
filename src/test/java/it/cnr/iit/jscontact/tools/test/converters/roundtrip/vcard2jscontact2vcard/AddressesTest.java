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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.vcard2jscontact2vcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.serializers.PrettyPrintSerializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard.JSContact2VCard;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AddressesTest extends RoundtripTest {

    @Test
    public void testAddresses1() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses2() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;LABEL=54321 Oak St Reston USA:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses3() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses3 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses4() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses4 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses5() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:-0500\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses5 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses6() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:America/New_York\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses6 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }


    public void testAddresses7() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=America/New_York;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                        .applyAutoAddrLabel(false)
                        .addPropIdParameter(false)
                        .build())
                .build();

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses7 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testAddresses8() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "TZ;ALTID=1:America/New_York\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses8 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testAddresses9() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=-0500;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                        .applyAutoAddrLabel(false)
                        .addPropIdParameter(false)
                        .convertTimezoneToTZParam(true)
                        .convertTimezoneToOffset(true)
                        .build())
                .build();

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses9 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses10() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:-0530\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses10 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }


//    @Test
    public void testAddresses11() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        System.out.println(PrettyPrintSerializer.print(jsCard));
        VCard vcard2 = jsContact2VCard.convert(PrettyPrintSerializer.print(jsCard)).get(0);
        pruneVCard(vcard2);
        System.out.println(Ezvcard.write(vcard2).go());
        assertEquals("testAddresses11 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }


    //@Test
    public void testAddresses12() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("en").build()).build();
        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses12 - 1", jsCard.getAddresses());
        assertEquals("testAddresses12 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses12 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses12 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses12 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses12 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses12 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses12 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddresses12 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddresses12 - 10", "US", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddresses12 - 11", "USA", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddresses12 - 12", "20190", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddresses12 - 13", "Reston", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddresses12 - 14", "VA", jsCard.getAddresses().get("ADR-2").getRegion());
        assertEquals("testAddresses12 - 15", "12345 Elm St", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testAddresses12 - 16", "12345 Elm St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-2").getFullAddress());
        assertNotNull("testAddresses12 - 17", jsCard.getLocalization("it", "addresses/ADR-2"));

    }


    //@Test
    public void testAddresses13() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().defaultLanguage("it").build()).build();
        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses13 - 1", jsCard.getAddresses());
        assertEquals("testAddresses13 - 2", 2, jsCard.getAddresses().size());
        assertEquals("testAddresses13 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses13 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses13 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses13 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses13 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses13 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddresses13 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddresses13 - 10", "IT", jsCard.getAddresses().get("ADR-2").getCountryCode());
        assertEquals("testAddresses13 - 11", "Italy", jsCard.getAddresses().get("ADR-2").getCountry());
        assertEquals("testAddresses13 - 12", "56124", jsCard.getAddresses().get("ADR-2").getPostcode());
        assertEquals("testAddresses13 - 13", "Pisa", jsCard.getAddresses().get("ADR-2").getLocality());
        assertEquals("testAddresses13 - 15", "Via Moruzzi,1", jsCard.getAddresses().get("ADR-2").getStreetDetails());
        assertEquals("testAddresses13 - 16", "Via Moruzzi,1\nPisa\n56124\nItaly", jsCard.getAddresses().get("ADR-2").getFullAddress());
        assertNotNull("testAddresses13 - 17", jsCard.getLocalization("en", "addresses/ADR-2"));

    }
}
