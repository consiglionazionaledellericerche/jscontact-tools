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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.ChannelType;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RFCXXXXPropertiesTest extends JCard2JSContactTest {

    @Test
    public void testCreated() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"created\",{},\"timestamp\",\"20101010T101010Z\"] " +
                "]]";

        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testCreated - 1", 0, jsCard.getCreated().compareTo(DateUtils.toCalendar("2010-10-10T10:10:10Z")));

    }

    @Test
    public void testPreferredContactChannels() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"contact-channel-pref\",{\"type\":\"work\",\"pref\":\"1\"},\"text\",\"EMAIL\"], " +
                "[\"contact-channel-pref\",{\"type\":\"home\",\"pref\":\"2\"},\"text\",\"EMAIL\"], " +
                "[\"contact-channel-pref\",{\"type\":\"work\"},\"text\",\"TEL\"], " +
                "[\"contact-channel-pref\",{},\"text\",\"ADR\"]" +
                "]]";

        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
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


}
