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
package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.ContactByType;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact.JCard2JSContact;
import org.junit.Test;

import static org.junit.Assert.*;

public class RFCXXXXPropertiesTest extends JCard2JSContactTest {

    @Test
    public void testCreated() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"created\",{},\"timestamp\",\"20101010T101010Z\"] " +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testCreated - 1", 0, jsCard.getCreated().compareTo(DateUtils.toCalendar("2010-10-10T10:10:10Z")));

    }

    @Test
    public void testContactBy() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"contact-by\",{\"type\":\"work\",\"pref\":\"1\"},\"text\",\"EMAIL\"], " +
                "[\"contact-by\",{\"type\":\"home\",\"pref\":\"2\"},\"text\",\"EMAIL\"], " +
                "[\"contact-by\",{\"type\":\"work\"},\"text\",\"TEL\"], " +
                "[\"contact-by\",{},\"text\",\"ADR\"]" +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testContactBy - 1", 3, jsCard.getContactBy().size());
        assertEquals("testContactBy - 2", 2, jsCard.getContactBy().get(ContactByType.emails()).length);
        assertEquals("testContactBy - 3", 1, (int) jsCard.getContactBy().get(ContactByType.emails())[0].getPref());
        assertTrue("testContactBy - 4", jsCard.getContactBy().get(ContactByType.emails())[0].asWork());
        assertEquals("testContactBy - 5", 2, (int) jsCard.getContactBy().get(ContactByType.emails())[1].getPref());
        assertTrue("testContactBy - 6", jsCard.getContactBy().get(ContactByType.emails())[1].asPrivate());
        assertEquals("testContactBy - 7", 1, jsCard.getContactBy().get(ContactByType.phones()).length);
        assertTrue("testContactBy - 8", jsCard.getContactBy().get(ContactByType.phones())[0].asWork());
        assertEquals("testContactBy - 9", 0, jsCard.getContactBy().get(ContactByType.addresses()).length);
    }


    @Test
    public void testLanguage() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"deflanguage\",{},\"text\",\"it\"] " +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testLanguage - 1", "it", jsCard.getLanguage());

    }


    @Test
    public void testSpeakToAsWithGender1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"gender\", {}, \"text\", \"M\"] " +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertTrue("testSpeakToAsWithGender1 - 1",jsCard.getSpeakToAs().isMasculine());
        assertNull("testSpeakToAsWithGender1 - 2", jsCard.getSpeakToAs().getPronouns());
        assertNull("testSpeakToAsWithGender1 - 3", jsCard.getExtensions());
    }

    @Test
    public void testSpeakToAsWithGender2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"gender\", {}, \"text\", [\"M\",\"boy\"]] " +
                "]]";

        JCard2JSContact jCard2JSContact = JCard2JSContact.builder().config(VCard2JSContactConfig.builder().convertGenderToSpeakToAs(false).build()).build();
        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testSpeakToAsWithGender2 - 1", 2, jsCard.getVCardProps().length); // including VERSION
        assertEquals("testSpeakToAsWithGender2 - 2", "gender", jsCard.getVCardProps()[0].getName().toString());
        assertEquals("testSpeakToAsWithGender2 - 3", 0, jsCard.getVCardProps()[0].getParameters().size());
        assertEquals("testSpeakToAsWithGender2 - 4", VCardDataType.TEXT, jsCard.getVCardProps()[0].getType());
        assertEquals("testSpeakToAsWithGender2 - 5", "M;boy", jsCard.getVCardProps()[0].getValue());

    }

    @Test
    public void testSpeakToAs1() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"gramgender\",{},\"text\",\"MASCULINE\"] " +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertTrue("testSpeakToAs1 - 1",jsCard.getSpeakToAs().isMasculine());
        assertNull("testSpeakToAs1 - 2", jsCard.getSpeakToAs().getPronouns());
    }

    @Test
    public void testSpeakToAs2() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"gramgender\",{},\"text\",\"INANIMATE\"] " +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertTrue("testSpeakToAs2 - 1",jsCard.getSpeakToAs().isInanimate());
        assertNull("testSpeakToAs2 - 2", jsCard.getSpeakToAs().getPronouns());
    }

    @Test
    public void testSpeakToAs3() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"pronouns\",{},\"text\",\"he/him\"] " +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testSpeakToAs3 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertNull("testSpeakToAs3 - 2", jsCard.getSpeakToAs().getGrammaticalGender());
    }

    @Test
    public void testSpeakToAs4() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"gramgender\",{},\"text\",\"MASCULINE\"], " +
                "[\"pronouns\",{},\"text\",\"he/him\"] " +
                "]]";

        Card jsCard = jCard2JSContact.convert(jcard).get(0);
        assertEquals("testSpeakToAs4 - 1","he/him",jsCard.getSpeakToAs().getPronouns().get("PRONOUNS-1").getPronouns());
        assertTrue("testSpeakToAs4 - 2", jsCard.getSpeakToAs().isMasculine());
    }

}
