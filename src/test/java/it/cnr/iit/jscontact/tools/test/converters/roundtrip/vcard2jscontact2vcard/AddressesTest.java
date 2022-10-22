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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard.JSContact2VCard;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddressesTest extends RoundtripTest {

    @Test
    public void testAddresses1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;LABEL=54321 Oak St Reston USA:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses3 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses4 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:-0500\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses5 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:America/New_York\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses6 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }


    @Test
    public void testAddresses7() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=America/New_York;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                        .setAutoAddrLabel(false)
                        .setPropIdParam(false)
                        .build())
                .build();

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses7 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testAddresses8() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "TZ;ALTID=1:America/New_York\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses8 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    @Test
    public void testAddresses9() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US;TZ=-0500;GEO=\"geo:46.772673,-71.282945\":;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                        .setAutoAddrLabel(false)
                        .setPropIdParam(false)
                        .convertTimezoneToOffset(true)
                        .build())
                .build();

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses9 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }

    @Test
    public void testAddresses10() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "TZ:-0530\n" +
                "END:VCARD";

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                        .setAutoAddrLabel(false)
                        .setPropIdParam(false)
                        .convertCoordinatesToGEOParam(false)
                        .convertTimezoneToTZParam(false)
                        .build())
                .build();

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses10 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }


    //TODO - INDISCERNIBLE
    //@Test
    public void testAddresses11() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US;ALTID=1;LANGUAGE=en:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "ADR;CC=IT;ALTID=1;LANGUAGE=it:;;Via Moruzzi,1;Pisa;;56124;Italy\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses11 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }


    //TODO - INDISCERNIBLE
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
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses12 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }


    //TODO - INDISCERNIBLE
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
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testAddresses13 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }
}
