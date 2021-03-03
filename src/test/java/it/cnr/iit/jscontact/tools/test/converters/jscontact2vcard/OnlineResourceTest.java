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
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.ImageType;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.LabelKey;
import it.cnr.iit.jscontact.tools.dto.OnlineResourceType;
import it.cnr.iit.jscontact.tools.dto.ResourceContext;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class OnlineResourceTest extends JSContact2VCardTest {

    @Test
    public void testOnlineResourceValid1() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"username\","+
                        "\"context\":\"private\"," +
                        "\"labels\": { \"XMPP\": true }," +
                        "\"isPreferred\": true, " +
                        "\"value\": \"xmpp:alice@example.com\"" +
                    "}" +
                 "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid1 - 1",vcard.getImpps().size() == 1);
        assertTrue("testOnlineResourceValid1 - 2",vcard.getImpps().get(0).isXmpp());
        assertTrue("testOnlineResourceValid1 - 3",vcard.getImpps().get(0).getHandle().equals("alice@example.com"));
        assertTrue("testOnlineResourceValid1 - 4",vcard.getImpps().get(0).getParameter("TYPE").equals("home"));
        assertTrue("testOnlineResourceValid1 - 5",vcard.getImpps().get(0).getPref() == 1);
    }

    @Test
    public void testOnlineResourceValid2() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                        "\"type\": \"uri\","+
                        "\"labels\": { \"source\": true }," +
                        "\"value\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                 "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid2 - 1",vcard.getSources().size() == 1);
        assertTrue("testOnlineResourceValid2 - 2",vcard.getSources().get(0).getValue().equals("http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf"));
    }

    @Test
    public void testPhotoValid() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"photos\":["+
                    "{" +
                        "\"mediaType\": \"image/gif\","+
                        "\"href\": \"http://www.example.com/pub/photos/jqpublic.gif\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testPhotoValid - 1",vcard.getPhotos().size() == 1);
        assertTrue("testPhotoValid - 2",vcard.getPhotos().get(0).getUrl().equals("http://www.example.com/pub/photos/jqpublic.gif"));
        assertTrue("testPhotoValid - 3",vcard.getPhotos().get(0).getContentType() == ImageType.GIF);
    }

    @Test
    public void testOnlineResourceValid4() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                "{" +
                    "\"type\": \"uri\","+
                    "\"labels\": { \"logo\": true }," +
                    "\"value\": \"http://www.example.com/pub/logos/abccorp.jpg\"" +
                "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid4 - 1",vcard.getLogos().size() == 1);
        assertTrue("testOnlineResourceValid4 - 2",vcard.getLogos().get(0).getUrl().equals("http://www.example.com/pub/logos/abccorp.jpg"));
    }

    @Test
    public void testOnlineResourceValid5() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                    "\"type\": \"uri\","+
                    "\"labels\": { \"contact-uri\": true }," +
                    "\"value\": \"mailto:contact@example.com\"" +
                    "}" +
                "]" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid5 - 1",vcard.getExtendedProperties().size() == 1);
        assertTrue("testOnlineResourceValid5 - 2",vcard.getExtendedProperties().get(0).getPropertyName().equals("CONTACT-URI"));
        assertTrue("testOnlineResourceValid5 - 2",vcard.getExtendedProperties().get(0).getValue().equals("mailto:contact@example.com"));
    }

    /*
    @Test
    public void testOnlineResourceValid6() throws IOException, CardException {

        String jscard="{" +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"online\":["+
                    "{" +
                    "\"type\": \"uri\","+
                    "\"labels\": { \"sound\": true }," +
                    "\"value\": \"CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testOnlineResourceValid6 - 1",vcard.getSounds().size() == 1);
        assertTrue("testOnlineResourceValid6 - 2",vcard.getSounds().get(0).getUrl().equals("CID:JOHNQPUBLIC.part8.19960229T080000.xyzMail@example.com"));
    }

    /*
    @Test
    public void testOnlineResourceValid9() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "URL:http://example.org/restaurant.french/~chezchic.htm\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid9 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid9 - 2",jsCard.getOnline()[0].getValue().equals("http://example.org/restaurant.french/~chezchic.htm"));
        assertTrue("testOnlineResourceValid9 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid9 - 4",jsCard.getOnline()[0].getIsPreferred() == null);
        assertTrue("testOnlineResourceValid9 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid9 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.URL.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testOnlineResourceValid10() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "KEY:http://www.example.com/keys/jdoe.cer\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid10 - 1",jsCard.getOnline().length == 1);
        assertTrue("testOnlineResourceValid10 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/keys/jdoe.cer"));
        assertTrue("testOnlineResourceValid10 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid10 - 4",jsCard.getOnline()[0].getIsPreferred() == null);
        assertTrue("testOnlineResourceValid10 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid10 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.KEY.getValue()) == Boolean.TRUE);

    }


    @Test
    public void testOnlineResourceValid11() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "FBURL;PREF=1:http://www.example.com/busy/janedoe\n" +
                "FBURL;MEDIATYPE=text/calendar:ftp://example.com/busy/project-a.ifb\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid11 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid11 - 2",jsCard.getOnline()[0].getValue().equals("http://www.example.com/busy/janedoe"));
        assertTrue("testOnlineResourceValid11 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid11 - 4",jsCard.getOnline()[0].getIsPreferred() == Boolean.TRUE);
        assertTrue("testOnlineResourceValid11 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid11 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.FBURL.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid11 - 7",jsCard.getOnline()[1].getValue().equals("ftp://example.com/busy/project-a.ifb"));
        assertTrue("testOnlineResourceValid11 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid11 - 9",jsCard.getOnline()[1].getIsPreferred() == null);
        assertTrue("testOnlineResourceValid11 - 10",jsCard.getOnline()[1].getMediaType().equals("text/calendar"));
        assertTrue("testOnlineResourceValid11 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.FBURL.getValue()) == Boolean.TRUE);

    }


    @Test
    public void testOnlineResourceValid12() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALADRURI;PREF=1:mailto:janedoe@example.com\n" +
                "CALADRURI:http://example.com/calendar/jdoe\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid12 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid12 - 2",jsCard.getOnline()[0].getValue().equals("mailto:janedoe@example.com"));
        assertTrue("testOnlineResourceValid12 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid12 - 4",jsCard.getOnline()[0].getIsPreferred() == Boolean.TRUE);
        assertTrue("testOnlineResourceValid12 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid12 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.CALADRURI.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid12 - 7",jsCard.getOnline()[1].getValue().equals("http://example.com/calendar/jdoe"));
        assertTrue("testOnlineResourceValid12 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid12 - 9",jsCard.getOnline()[1].getIsPreferred() == null);
        assertTrue("testOnlineResourceValid12 - 10",jsCard.getOnline()[1].getMediaType() == null);
        assertTrue("testOnlineResourceValid12 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.CALADRURI.getValue()) == Boolean.TRUE);

    }

    @Test
    public void testOnlineResourceValid13() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "CALURI;PREF=1:http://cal.example.com/calA\n" +
                "CALURI;MEDIATYPE=text/calendar:ftp://ftp.example.com/calA.ics\n" +
                "END:VCARD";

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testOnlineResourceValid13 - 1",jsCard.getOnline().length == 2);
        assertTrue("testOnlineResourceValid13 - 2",jsCard.getOnline()[0].getValue().equals("http://cal.example.com/calA"));
        assertTrue("testOnlineResourceValid13 - 3",jsCard.getOnline()[0].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid13 - 4",jsCard.getOnline()[0].getIsPreferred() == Boolean.TRUE);
        assertTrue("testOnlineResourceValid13 - 5",jsCard.getOnline()[0].getMediaType() == null);
        assertTrue("testOnlineResourceValid13 - 6",jsCard.getOnline()[0].getLabels().get(LabelKey.CALURI.getValue()) == Boolean.TRUE);
        assertTrue("testOnlineResourceValid13 - 7",jsCard.getOnline()[1].getValue().equals("ftp://ftp.example.com/calA.ics"));
        assertTrue("testOnlineResourceValid13 - 8",jsCard.getOnline()[1].getType().equals(OnlineResourceType.URI.getValue()));
        assertTrue("testOnlineResourceValid13 - 9",jsCard.getOnline()[1].getIsPreferred() == null);
        assertTrue("testOnlineResourceValid13 - 10",jsCard.getOnline()[1].getMediaType().equals("text/calendar"));
        assertTrue("testOnlineResourceValid13 - 11",jsCard.getOnline()[1].getLabels().get(LabelKey.CALURI.getValue()) == Boolean.TRUE);

    }
*/
}
