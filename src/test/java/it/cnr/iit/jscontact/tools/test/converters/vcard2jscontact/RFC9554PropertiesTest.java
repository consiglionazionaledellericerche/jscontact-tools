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

import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.GrammaticalGenderType;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.*;

public class RFC9554PropertiesTest extends VCard2JSContactTest {

    @Test
    public void testCreated() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CREATED;VALUE=timestamp:20101010T101010Z\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCreated - 1", 0, jsCard.getCreated().compareTo(DateUtils.toCalendar("2010-10-10T10:10:10Z")));
    }

    @Test
    public void testLanguage() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANGUAGE:it\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testLanguage - 1", "it", jsCard.getLanguage());
    }

    @Test
    public void testSpeakToAsWithGender1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GENDER:M\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAsWithGender1 - 1",jsCard.getSpeakToAs().isMasculine());
        assertNull("testSpeakToAsWithGender1 - 2", jsCard.getSpeakToAs().getPronouns());
        assertNull("testSpeakToAsWithGender1 - 3", jsCard.getExtensions());
    }

    @Test
    public void testSpeakToAsWithGender2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GENDER:M;boy\n" +
                "END:VCARD";

        VCard2JSContact vCard2JSContact = VCard2JSContact.builder().config(VCard2JSContactConfig.builder().convertGenderToSpeakToAs(false).build()).build();
        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAsWithGender2 - 1", 2, jsCard.getVCardProps().length); //including VERSION
        assertEquals("testSpeakToAsWithGender2 - 2", "gender", jsCard.getVCardProps()[0].getName().toString());
        assertEquals("testSpeakToAsWithGender2 - 3", 0, jsCard.getVCardProps()[0].getParameters().size());
        assertEquals("testSpeakToAsWithGender2 - 4", VCardDataType.TEXT, jsCard.getVCardProps()[0].getType());
        assertEquals("testSpeakToAsWithGender2 - 5", "M;boy", jsCard.getVCardProps()[0].getValue());
    }

    @Test
    public void testSpeakToAs1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GRAMGENDER;VALUE=text:MASCULINE\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAs1 - 1",jsCard.getSpeakToAs().isMasculine());
        assertNull("testSpeakToAs1 - 2", jsCard.getSpeakToAs().getPronouns());
    }

    @Test
    public void testSpeakToAs2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GRAMGENDER;VALUE=text:INANIMATE\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAs2 - 1",jsCard.getSpeakToAs().isInanimate());
        assertNull("testSpeakToAs2 - 2", jsCard.getSpeakToAs().getPronouns());
    }

    @Test
    public void testSpeakToAs3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PRONOUNS;VALUE=text:he/him\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAs3 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertNull("testSpeakToAs3 - 2", jsCard.getSpeakToAs().getGrammaticalGender());
    }

    @Test
    public void testSpeakToAs4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GRAMGENDER;VALUE=text:MASCULINE\n" +
                "PRONOUNS;VALUE=text:he/him\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAs4 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertTrue("testSpeakToAs4 - 2", jsCard.getSpeakToAs().isMasculine());
    }


    @Test
    public void testSpeakToAs5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANGUAGE;VALUE=language-tag:en\n" +
                "GRAMGENDER;VALUE=text:INANIMATE\n" +
                "GRAMGENDER;LANGUAGE=it;VALUE=text:MASCULINE\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAs5 - 1",jsCard.getSpeakToAs().isInanimate());
        assertNull("testSpeakToAs5 - 2", jsCard.getSpeakToAs().getPronouns());
        assertEquals("testSpeakToAs5 - 3", 1, jsCard.getLocalizations().size());
        assertEquals("testSpeakToAs5 - 4", GrammaticalGenderType.MASCULINE, GrammaticalGenderType.getEnum(jsCard.getLocalizations().get("it").get("speakToAs/grammaticalGender").asText().toLowerCase()));
    }

    @Test
    public void testSpeakToAs6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANGUAGE;VALUE=language-tag:en\n" +
                "PRONOUNS;ALTID=1;PROP-ID=PRONOUNS-1;VALUE=text:he/him\n" +
                "PRONOUNS;LANGUAGE=it;ALTID=1;VALUE=text:egli/lui\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAs6 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertNull("testSpeakToAs6 - 2", jsCard.getSpeakToAs().getGrammaticalGender());
        assertEquals("testSpeakToAs6 - 3", 1, jsCard.getLocalizations().size());
        assertEquals("testSpeakToAs6 - 4", "egli/lui", jsCard.getLocalizations().get("it").get("speakToAs/pronouns/PRONOUNS-1").get("pronouns").asText());
    }


    @Test
    public void testSpeakToAs7() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LANGUAGE;VALUE=language-tag:en\n" +
                "PRONOUNS;ALTID=1;PROP-ID=PRONOUNS-1;VALUE=text:he/him\n" +
                "PRONOUNS;LANGUAGE=it;ALTID=1;VALUE=text:egli/lui\n" +
                "GRAMGENDER;VALUE=text:INANIMATE\n" +
                "GRAMGENDER;LANGUAGE=it;VALUE=text:MASCULINE\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAs7 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertTrue("testSpeakToAs7 - 2", jsCard.getSpeakToAs().isInanimate());
        assertEquals("testSpeakToAs7 - 3", 1, jsCard.getLocalizations().size());
        assertEquals("testSpeakToAs7 - 4", "egli/lui", jsCard.getLocalizations().get("it").get("speakToAs/pronouns/PRONOUNS-1").get("pronouns").asText());
        assertEquals("testSpeakToAs7 - 5", GrammaticalGenderType.MASCULINE, GrammaticalGenderType.getEnum(jsCard.getLocalizations().get("it").get("speakToAs/grammaticalGender").asText().toLowerCase()));
    }
}
