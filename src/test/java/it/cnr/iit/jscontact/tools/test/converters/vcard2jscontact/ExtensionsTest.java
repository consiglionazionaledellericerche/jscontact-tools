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


import com.fasterxml.jackson.core.JsonProcessingException;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.V_Extension;
import it.cnr.iit.jscontact.tools.dto.serializers.PrettyPrintSerializer;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExtensionsTest extends VCard2JSContactTest {

    @Test
    public void testExtendedJSContact1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "JSPROP;JSPTR=\"extension:myext1\";VALUE=TEXT:\"extvalue\"\n" +
                "JSPROP;JSPTR=\"extension:myext2\";VALUE=TEXT:{\"extprop\":\"extvalue\"}\n" +
                "END:VCARD";
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testExtendedJSContact1 - 1", 2, jsCard.getExtensions().size());
        assertEquals("testExtendedJSContact1 - 2", "extvalue", jsCard.getExtensions().get("extension:myext1"));
        assertEquals("testExtendedJSContact1 - 3", "{extprop=extvalue}", jsCard.getExtensions().get("extension:myext2").toString());
    }

    @Test
    public void textExtendedJSContact2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "NICKNAME;LANGUAGE=en:Johnny\n" +
                "NICKNAME;LANGUAGE=en:Joe\n" +
                "ADR;CC=US;LANGUAGE=en:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "LANG;PREF=1:jp\n" +
                "LANG;PREF=2:en\n" +
                "DEFLANGUAGE;VALUE=language-tag:en\n" +
                "JSPROP;JSPTR=\"addresses/ADR-1/components/0/ext4\";VALUE=TEXT:true\n" +
                "JSPROP;JSPTR=\"nickNames/NICK-1/ext3\";VALUE=TEXT:\"text\"\n" +
                "JSPROP;JSPTR=\"addresses/ADR-1/ext2\";VALUE=TEXT:{\"prop\":10}\n" +
                "JSPROP;JSPTR=\"preferredLanguages/jp/0/ext6\";VALUE=TEXT:[\"1\",\"2\"]\n" +
                "JSPROP;JSPTR=\"ext1\";VALUE=TEXT:10\n" +
                "END:VCARD";
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testExtendedJSContact2 - 1", 1, jsCard.getExtensions().size());
        assertEquals("testExtendedJSContact2 - 2", 10, jsCard.getExtensions().get("ext1"));
        assertEquals("testExtendedJSContact2 - 3", 1, jsCard.getNickNames().get("NICK-1").getExtensions().size());
        assertEquals("testExtendedJSContact2 - 4", "text", jsCard.getNickNames().get("NICK-1").getExtensions().get("ext3"));
        assertEquals("testExtendedJSContact2 - 5", 1, jsCard.getPreferredLanguages().get("jp")[0].getExtensions().size());
        assertEquals("testExtendedJSContact2 - 6", "[1, 2]", jsCard.getPreferredLanguages().get("jp")[0].getExtensions().get("ext6").toString());
        assertEquals("testExtendedJSContact2 - 7", 1, jsCard.getAddresses().get("ADR-1").getExtensions().size());
        assertEquals("testExtendedJSContact2 - 8", "{prop=10}", jsCard.getAddresses().get("ADR-1").getExtensions().get("ext2").toString());
        assertEquals("testExtendedJSContact2 - 9", 1, jsCard.getAddresses().get("ADR-1").getComponents()[0].getExtensions().size());
        assertEquals("testExtendedJSContact2 - 10", true, jsCard.getAddresses().get("ADR-1").getComponents()[0].getExtensions().get("ext4"));

    }

    @Test
    public void testExtendedJSContact3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "G-PHONE-1.TEL;PROP-ID=PHONE-1;TYPE=home,voice;VALUE=uri:tel:+33-01-23-45-6\n" +
                "G-PHONE-1.X-ABLabel;VALUE=text:a label\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testExtendedJSContact3 - 1", "a label", jsCard.getPhones().get("PHONE-1").getLabel());
        assertEquals("testExtendedJSContact3 - 2", "G-PHONE-1", jsCard.getPhones().get("PHONE-1").getVCardParams().get("group").getValue());
    }

    @Test
    public void testExtendedJSContact4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                 "JSPROP;JSPTR=anniversaries/ANNIVERSARY-1;VALUE=TEXT:{\"@type\":\"Anniversary\",\"kind\":\"example.com:engagement\",\"date\":{\"@type\":\"Timestamp\",\"utc\":\"1953-10-15T23:10:00Z\"}}\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertTrue("testExtendedJSContact4 - 1", jsCard.getAnniversaries().get("ANNIVERSARY-1").getKind().isExtValue());
        assertEquals("testExtendedJSContact4 - 2", V_Extension.toV_Extension("example.com:engagement"), jsCard.getAnniversaries().get("ANNIVERSARY-1").getKind().getExtValue());
    }

    @Test
    public void testExtendedJSContact5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:Mr. John Q. Public, Esq.\n" +
                "N:Public;John;;;\n" +
                "JSPROP;JSPTR=name/components/2;VALUE=TEXT:{\"@type\":\"NameComponent\",\"kind\":\"example.com:exttype\",\"value\":\"extvalue\"}\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertTrue("testExtendedJSContact5 - 1", jsCard.getName().getComponents()[0].isGiven());
        assertEquals("testExtendedJSContact5 - 2", "John", jsCard.getName().getGiven());
        assertTrue("testExtendedJSContact5 - 3", jsCard.getName().getComponents()[1].isSurname());
        assertEquals("testExtendedJSContact5 - 4", "Public", jsCard.getName().getSurname());
        assertTrue("testExtendedJSContact5 - 5", jsCard.getName().getComponents()[2].isExt());
        assertEquals("testExtendedJSContact5 - 6", V_Extension.toV_Extension("example.com:exttype"), jsCard.getName().getComponents()[2].getKind().getExtValue());
        assertEquals("testExtendedJSContact5 - 7", "extvalue", jsCard.getName().getComponents()[2].getValue());
    }


    @Test
    public void testExtendedJSContact6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri:tel:+33-01-23-45-6\n" +
                "JSPROP;JSPTR=\"phones/PHONE-1/features/example.com:extfeature\";VALUE=TEXT:true\n" +
                "JSPROP;JSPTR=\"phones/PHONE-1/contexts/example.com:extcontext\";VALUE=TEXT:true\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testExtendedJSContact6 - 1", "tel:+33-01-23-45-6", jsCard.getPhones().get("PHONE-1").getNumber());
        assertTrue("testExtendedJSContact6 - 2", jsCard.getPhones().get("PHONE-1").asExtContext("example.com:extcontext"));
        assertTrue("testExtendedJSContact6 - 3", jsCard.getPhones().get("PHONE-1").asExt("example.com:extfeature"));
    }


    @Test
    public void testExtendedJSContact7() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:Mr. John Q. Smith, Esq.\n" +
                "N;SORT-AS=\"Smith,John\":Smith;John;Quinlan;Mr.;Esq.;;\n" +
                "NICKNAME;PROP-ID=NICK-1:Johnny\n" +
                "NICKNAME;PROP-ID=NICK-2:Joe\n" +
                "JSPROP;JSPTR=name/components/2/pronounce;VALUE=text:{\"@type\":\"Pronounce\",\"phonetics\":\"/smɪθ/\",\"system\":\"ipa\"}\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testExtendedJSContact7 - 1", "Mr. John Q. Smith, Esq.", jsCard.getName().getFull());
        assertNull("testExtendedJSContact7 - 2", jsCard.getName().getPronounce());
        assertNotNull("testExtendedJSContact7 - 3", jsCard.getName().getComponents()[2].getPronounce());
        assertEquals("testExtendedJSContact7 - 4", "/smɪθ/", jsCard.getName().getComponents()[2].getPronounce().getPhonetics());
        assertTrue("testExtendedJSContact7 - 5", jsCard.getName().getComponents()[2].getPronounce().getSystem().isIpa());
    }

    @Test
    public void testExtendedJSContact8() throws CardException, JsonProcessingException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:Smith\n" +
                "JSPROP;JSPTR=name/pronounce;VALUE=text:{\"@type\":\"Pronounce\",\"phonetics\":\"/smɪθ/\",\"system\":\"ipa\"}\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        VCard vCard = Ezvcard.parse(vcard).register(new ExtendedStructuredNameScribe()).first();
        assertEquals("testExtendedJSContact8 - 1", "Smith", jsCard.getName().getFull());
        assertNotNull("testExtendedJSContact8 - 2", jsCard.getName().getPronounce());
        assertEquals("testExtendedJSContact8 - 3", "/smɪθ/", jsCard.getName().getPronounce().getPhonetics());
        assertTrue("testExtendedJSContact8 - 4", jsCard.getName().getPronounce().getSystem().isIpa());
    }



}
