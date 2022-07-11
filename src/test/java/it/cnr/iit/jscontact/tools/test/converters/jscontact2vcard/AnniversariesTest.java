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

import ezvcard.VCard;
import ezvcard.util.GeoUri;
import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AnniversariesTest extends JSContact2VCardTest {

    @Test
    public void testAnniversariesValid1() throws IOException, CardException {

        String jsCard = "{ " +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                        "\"fullName\":\"test\"," +
                        "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                               "{" +
                                    "\"@type\":\"Anniversary\"," +
                                   "\"type\":\"birth\", " +
                                   "\"date\":\"1953-10-15T23:10:00Z\"" +
                               "}" +
                        "}" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid1 - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
    }

    @Test
    public void testAnniversariesValid2() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":\"test\"," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                           "\"fullAddress\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid2 - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid2 - 2", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getBirthplace().getText());
    }

    @Test
    public void testAnniversariesValid3() throws IOException, CardException {

        String jsCard = "{ " +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                        "\"fullName\":\"test\"," +
                        "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                               "{" +
                                    "\"@type\":\"Anniversary\"," +
                                   "\"type\":\"death\", " +
                                   "\"date\":\"1953-10-15T23:10:00Z\"" +
                               "}" +
                        "}" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid3 - 1", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
    }

    @Test
    public void testAnniversariesValid4() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":\"test\"," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"death\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid4 - 1", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid4 - 2", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getDeathplace().getText());
    }

    @Test
    public void testAnniversariesValid5() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":\"test\"," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":\"Los Angeles CA USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid5 - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid5 - 2", "Los Angeles CA USA", vcard.getBirthplace().getText());
        assertEquals("testAnniversariesValid5 - 3", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid5 - 4", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getDeathplace().getText());
    }

    @Test
    public void testAnniversariesValid6() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":\"test\"," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":\"Los Angeles CA USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"label\":\"marriage date\"," +
                        "\"date\":\"1986-02-01T19:00:00Z\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid6 - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid6 - 2", "Los Angeles CA USA", vcard.getBirthplace().getText());
        assertEquals("testAnniversariesValid6 - 3", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid6 - 4", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getDeathplace().getText());
        assertEquals("testAnniversariesValid6 - 5", 0, vcard.getAnniversary().getDate().compareTo(VCardDateFormat.parse("1986-02-01T19:00:00Z")));
    }

    @Test
    public void testAnniversariesValid7() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":\"test\"," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                              "\"@type\":\"Address\"," +
                              "\"coordinates\":\"geo:34.15876,-118.45728\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"fullAddress\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"label\":\"marriage date\"," +
                        "\"date\":\"1986-02-01T19:00:00Z\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid7 - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid7 - 2", vcard.getBirthplace().getGeoUri(), GeoUri.parse("geo:34.15876,-118.45728"));
        assertEquals("testAnniversariesValid7 - 3", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid7 - 4", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getDeathplace().getText());
        assertEquals("testAnniversariesValid7 - 5", 0, vcard.getAnniversary().getDate().compareTo(VCardDateFormat.parse("1986-02-01T19:00:00Z")));
    }

    @Test
    public void testAnniversariesValid8() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":\"test\"," +
                "\"anniversaries\": { \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"locality\":\"Los Angeles\"," +
                            "\"region\":\"CA\"," +
                            "\"country\":\"USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"fullAddress\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"label\":\"marriage date\"," +
                        "\"date\":\"1986-02-01T19:00:00Z\"" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversariesValid8 - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid8 - 2", "Los Angeles\nCA\nUSA", vcard.getBirthplace().getText());
        assertEquals("testAnniversariesValid8 - 3", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")));
        assertEquals("testAnniversariesValid8 - 4", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getDeathplace().getText());
        assertEquals("testAnniversariesValid8 - 5", 0, vcard.getAnniversary().getDate().compareTo(VCardDateFormat.parse("1986-02-01T19:00:00Z")));
    }

}
