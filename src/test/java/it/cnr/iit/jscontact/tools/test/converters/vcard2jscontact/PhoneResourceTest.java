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
import it.cnr.iit.jscontact.tools.dto.ResourceContext;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.PhoneResourceType;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PhoneResourceTest extends VCard2JSContactTest {

    @Test
    public void testPhoneResourceValid1() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=home:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid1 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid1 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid1 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));
    }

    @Test
    public void testPhoneResourceValid2() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=home:tel:+33-01-23-45-6\n" +
                "TEL;VALUE=uri;TYPE=voice,home;PREF=1:tel:+1-555-555-5555;ext=555\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid2 - 1",jsCard.getPhones().length == 2);
        assertTrue("testPhoneResourceValid2 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid3 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));
        assertTrue("testPhoneResourceValid2 - 4",jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testPhoneResourceValid2 - 5",jsCard.getPhones()[0].getLabels() == null);
        assertTrue("testPhoneResourceValid2 - 6",jsCard.getPhones()[1].getValue().equals("tel:+1-555-555-5555;ext=555"));
        assertTrue("testPhoneResourceValid2 - 7",jsCard.getPhones()[1].getType().equals(PhoneResourceType.VOICE.getValue()));
        assertTrue("testPhoneResourceValid2 - 8",jsCard.getPhones()[1].getIsPreferred() == Boolean.TRUE);
        assertTrue("testPhoneResourceValid2 - 9",jsCard.getPhones()[1].getLabels() == null);
    }

    @Test
    public void testPhoneResourceValid3() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,fax:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid3 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid3 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid3 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid3 - 4",jsCard.getPhones()[0].getType().equals(PhoneResourceType.FAX.getValue()));
        assertTrue("testPhoneResourceValid3 - 5",jsCard.getPhones()[0].getLabels() == null);

    }

    @Test
    public void testPhoneResourceValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid4 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid4 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid4 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid4 - 4",jsCard.getPhones()[0].getType().equals(PhoneResourceType.OTHER.getValue()));
        assertTrue("testPhoneResourceValid4 - 5",jsCard.getPhones()[0].getLabels().get("textphone") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid4 - 6",jsCard.getPhones()[0].getLabels().size() == 1);
        assertTrue("testPhoneResourceValid4 - 7",jsCard.getPhones()[0].getType().equals(PhoneResourceType.OTHER.getValue()));

    }


    @Test
    public void testPhoneResourceValid5() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=home,work:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid5 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid5 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid5 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));
        assertTrue("testPhoneResourceValid5 - 4",jsCard.getPhones()[0].getLabels().size() == 1);
        assertTrue("testPhoneResourceValid5 - 5",jsCard.getPhones()[0].getLabels().get("work") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid5 - 6",jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));

    }

    @Test
    public void testPhoneResourceValid6() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid6 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid6 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid6 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid6 - 4",jsCard.getPhones()[0].getLabels().size() == 1);
        assertTrue("testPhoneResourceValid6 - 5",jsCard.getPhones()[0].getLabels().get("private") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid6 - 6",jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));

    }


    @Test
    public void testPhoneResourceValid7() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid7 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid7 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid7 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid7 - 4",jsCard.getPhones()[0].getLabels().size() == 2);
        assertTrue("testPhoneResourceValid7 - 5",jsCard.getPhones()[0].getLabels().get("private") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid7 - 6",jsCard.getPhones()[0].getLabels().get("textphone") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid7 - 7",jsCard.getPhones()[0].getType().equals(PhoneResourceType.OTHER.getValue()));

    }

    @Test
    public void testPhoneResourceValid8() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=uri;TYPE=work,home,voice,textphone:tel:+33-01-23-45-6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid8 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid8 - 2",jsCard.getPhones()[0].getValue().equals("tel:+33-01-23-45-6"));
        assertTrue("testPhoneResourceValid8 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.WORK.getValue()));
        assertTrue("testPhoneResourceValid8 - 4",jsCard.getPhones()[0].getLabels().size() == 2);
        assertTrue("testPhoneResourceValid8 - 5",jsCard.getPhones()[0].getLabels().get("private") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid8 - 6",jsCard.getPhones()[0].getLabels().get("textphone") == Boolean.TRUE);
        assertTrue("testPhoneResourceValid8 - 7",jsCard.getPhones()[0].getType().equals(PhoneResourceType.VOICE.getValue()));

    }

    @Test
    public void testPhoneResourceValid9() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "TEL;VALUE=text;TYPE=home:+33 01 23 45 6\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testPhoneResourceValid9 - 1",jsCard.getPhones().length == 1);
        assertTrue("testPhoneResourceValid9 - 2",jsCard.getPhones()[0].getValue().equals("+33 01 23 45 6"));
        assertTrue("testPhoneResourceValid9 - 3",jsCard.getPhones()[0].getContext().getValue().equals(ResourceContext.PRIVATE.getValue()));

    }

}
