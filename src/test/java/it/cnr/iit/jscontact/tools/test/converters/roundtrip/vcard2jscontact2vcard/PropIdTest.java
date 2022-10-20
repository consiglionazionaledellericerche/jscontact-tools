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
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard.JSContact2VCard;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropIdTest extends RoundtripTest {

    @Test
    public void testPropId1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;PROP-ID=TEST;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder()
                                                        .config(VCard2JSContactConfig.builder()
                                                                .setAutoFullAddress(false)
                                                                .setUsePropIds(true)
                                                                .build())
                                                        .build();

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                        .setAutoAddrLabel(false)
                        .setPropIdParam(true)
                        .build())
                .build();

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testPropId1 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));

    }

    //TODO - INDISCERNIBLE
    //@Test
    public void testPropId2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;PROP-ID=TEST;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "ADR;CC=US:;;12345 Elm St;Reston;VA;20190;USA\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder()
                .config(VCard2JSContactConfig.builder()
                        .setUsePropIds(true)
                        .build())
                .build();

        JSContact2VCard jsContact2VCard = JSContact2VCard.builder().config(JSContact2VCardConfig.builder()
                        .setAutoAddrLabel(false)
                        .setPropIdParam(true)
                        .build())
                .build();

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        assertEquals("testPropId2 - 1", vcard2, (Ezvcard.parse(vcard).all()).get(0));
    }



}
