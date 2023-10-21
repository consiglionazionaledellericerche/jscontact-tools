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
import it.cnr.iit.jscontact.tools.dto.serializers.PrettyPrintSerializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardReader;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.*;

public class VCardTest extends RoundtripTest {


    //@Test
    public void testCompleteVCard1() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-RFC7483.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        System.out.println(PrettyPrintSerializer.print(jsCard));
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        System.out.println(Ezvcard.write(vcard2).go());
        assertEquals("testCompleteVCard1 - 1", vcard2, VCardReader.parse(vcard).get(0));

    }

    //@Test
    public void testCompleteVCard2() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Multilingual.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        System.out.println(PrettyPrintSerializer.print(jsCard));
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        System.out.println(Ezvcard.write(vcard2).go());
        assertEquals("testCompleteVCard2 - 1", vcard2, VCardReader.parse(vcard).get(0));
    }

    //@Test
    public void testCompleteVCard3() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Unstructured.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        System.out.println(PrettyPrintSerializer.print(jsCard));
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        System.out.println(Ezvcard.write(vcard2).go());
        assertEquals("testCompleteVCard1 - 3", vcard2, VCardReader.parse(vcard).get(0));
    }


    //@Test
    public void testCompleteVCard4() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-RFC7095.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        System.out.println(PrettyPrintSerializer.print(jsCard));
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        System.out.println(Ezvcard.write(vcard2).go());
        assertEquals("testCompleteVCard1 - 4", vcard2, VCardReader.parse(vcard).get(0));
    }

    //@Test
    public void testCompleteVCard5() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-Wikipedia.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        System.out.println(PrettyPrintSerializer.print(jsCard));
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        System.out.println(Ezvcard.write(vcard2).go());
        assertEquals("testCompleteVCard5 - 1", vcard2, VCardReader.parse(vcard).get(0));
    }


    //@Test
    public void testCompleteVCard6() throws IOException, CardException {

        String vcard = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("vcard/vCard-ezvcard-fullcontact.vcf")), StandardCharsets.UTF_8);
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        System.out.println(PrettyPrintSerializer.print(jsCard));
        VCard vcard2 = jsContact2VCard.convert(jsCard).get(0);
        pruneVCard(vcard2);
        System.out.println(Ezvcard.write(vcard2).go());
        assertEquals("testCompleteVCard6 - 1", vcard2, VCardReader.parse(vcard).get(0));
    }

}
