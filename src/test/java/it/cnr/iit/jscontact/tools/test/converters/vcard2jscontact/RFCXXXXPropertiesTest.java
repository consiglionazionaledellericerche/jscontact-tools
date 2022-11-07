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
import it.cnr.iit.jscontact.tools.dto.ChannelType;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.vcard2jscontact.VCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.*;

public class RFCXXXXPropertiesTest extends VCard2JSContactTest {

    @Test
    public void testCreated() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CREATED;VALUE=timestamp:20101010T101010Z\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testCreated - 1", 0, jsCard.getCreated().compareTo(DateUtils.toCalendar("2010-10-10T10:10:10Z")));
    }

    @Test
    public void testPreferredContactChannels() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CONTACT-CHANNEL-PREF;TYPE=work;PREF=1:EMAIL\n" +
                "CONTACT-CHANNEL-PREF;TYPE=home;PREF=2:EMAIL\n" +
                "CONTACT-CHANNEL-PREF;TYPE=work:TEL\n" +
                "CONTACT-CHANNEL-PREF:ADR\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPreferredContactChannels - 1", 3, jsCard.getPreferredContactChannels().size());
        assertEquals("testPreferredContactChannels - 2", 2, jsCard.getPreferredContactChannels().get(ChannelType.emails()).length);
        assertEquals("testPreferredContactChannels - 3", 1, (int) jsCard.getPreferredContactChannels().get(ChannelType.emails())[0].getPref());
        assertTrue("testPreferredContactChannels - 4", jsCard.getPreferredContactChannels().get(ChannelType.emails())[0].asWork());
        assertEquals("testPreferredContactChannels - 5", 2, (int) jsCard.getPreferredContactChannels().get(ChannelType.emails())[1].getPref());
        assertTrue("testPreferredContactChannels - 6", jsCard.getPreferredContactChannels().get(ChannelType.emails())[1].asPrivate());
        assertEquals("testPreferredContactChannels - 7", 1, jsCard.getPreferredContactChannels().get(ChannelType.phones()).length);
        assertTrue("testPreferredContactChannels - 8", jsCard.getPreferredContactChannels().get(ChannelType.phones())[0].asWork());
        assertEquals("testPreferredContactChannels - 9", 0, jsCard.getPreferredContactChannels().get(ChannelType.addresses()).length);
    }

    @Test
    public void testLocale() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "LOCALE:it\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testLocale - 1", "it", jsCard.getLocale());
    }

    @Test
    public void testSpeakToAsWithGender1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GENDER:M\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAsWithGender1 - 1",jsCard.getSpeakToAs().isMale());
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
        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAsWithGender2 - 1", 1, jsCard.getJCardExtensions().length);
        assertEquals("testSpeakToAsWithGender2 - 2", "gender", jsCard.getJCardExtensions()[0].getName().toString());
        assertEquals("testSpeakToAsWithGender2 - 3", 0, jsCard.getJCardExtensions()[0].getParameters().size());
        assertEquals("testSpeakToAsWithGender2 - 4", VCardDataType.TEXT, jsCard.getJCardExtensions()[0].getType());
        assertEquals("testSpeakToAsWithGender2 - 5", "M;boy", jsCard.getJCardExtensions()[0].getValue());
    }

    public void testSpeakTo1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GRAMMATICAL-GENDER;VALUE=text:MALE\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAs1 - 1",jsCard.getSpeakToAs().isMale());
        assertNull("testSpeakToAs1 - 2", jsCard.getSpeakToAs().getPronouns());
    }

    public void testSpeakTo2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GRAMMATICAL-GENDER;VALUE=text:INANIMATE\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testSpeakToAs2 - 1",jsCard.getSpeakToAs().isInanimate());
        assertNull("testSpeakToAs2 - 2", jsCard.getSpeakToAs().getPronouns());
    }

    public void testSpeakTo3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PRONOUNS;VALUE=text:he/him\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAs3 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertNull("testSpeakToAs3 - 2", jsCard.getSpeakToAs().getGrammaticalGender());
    }

    public void testSpeakTo4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GRAMMATICAL-GENDER;VALUE=text:MALE\n" +
                "PRONOUNS;VALUE=text:he/him\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testSpeakToAs4 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertTrue("testSpeakToAs4 - 2", jsCard.getSpeakToAs().isMale());
    }

}
