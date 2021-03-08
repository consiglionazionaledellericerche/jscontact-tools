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

import static org.junit.Assert.assertTrue;

public class AnniversariesTest extends JSContact2VCardTest {

    @Test
    public void testAnniversariesValid1() throws IOException, CardException {

        String jsCard = "{ " +
                        "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                        "\"fullName\":{\"value\":\"test\"}," +
                        "\"anniversaries\":[ " +
                               "{" +
                                   "\"type\":\"birth\", " +
                                   "\"date\":\"1953-10-15T23:10:00Z\"" +
                               "}" +
                        "]" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid1 - 1",vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
    }

    @Test
    public void testAnniversariesValid2() throws IOException, CardException {

        String jsCard = "{ " +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"anniversaries\":[ " +
                    "{" +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                             "\"fullAddress\":{ " +
                                "\"value\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A.\"" +
                             "}" +
                        "}" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid2 - 1",vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid2 - 2",vcard.getBirthplace().getText().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A."));
    }

    @Test
    public void testAnniversariesValid3() throws IOException, CardException {

        String jsCard = "{ " +
                        "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                        "\"fullName\":{\"value\":\"test\"}," +
                        "\"anniversaries\":[ " +
                               "{" +
                                   "\"type\":\"death\", " +
                                   "\"date\":\"1953-10-15T23:10:00Z\"" +
                               "}" +
                        "]" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid3 - 1",vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
    }

    @Test
    public void testAnniversariesValid4() throws IOException, CardException {

        String jsCard = "{ " +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"anniversaries\":[ " +
                    "{" +
                        "\"type\":\"death\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":{ " +
                                "\"value\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A.\"" +
                            "}" +
                        "}" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid4 - 1",vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid4 - 2",vcard.getDeathplace().getText().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A."));
    }

    @Test
    public void testAnniversariesValid5() throws IOException, CardException {

        String jsCard = "{ " +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"anniversaries\":[ " +
                    "{" +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":{ " +
                                "\"value\":\"Los Angeles CA U.S.A.\"" +
                            "}" +
                        "}" +
                    "}," +
                    "{" +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":{ " +
                                "\"value\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A.\"" +
                            "}" +
                        "}" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid5 - 1",vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid5 - 2",vcard.getBirthplace().getText().equals("Los Angeles CA U.S.A."));
        assertTrue("testAnniversariesValid5 - 3",vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid5 - 4",vcard.getDeathplace().getText().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A."));
    }

    @Test
    public void testAnniversariesValid6() throws IOException, CardException {

        String jsCard = "{ " +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"anniversaries\":[ " +
                    "{" +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":{ " +
                                "\"value\":\"Los Angeles CA U.S.A.\"" +
                            "}" +
                        "}" +
                    "}," +
                    "{" +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":{ " +
                                "\"value\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A.\"" +
                            "}" +
                        "}" +
                    "}," +
                    "{" +
                        "\"type\":\"other\", " +
                        "\"label\":\"marriage date\"," +
                        "\"date\":\"1986-02-01T19:00:00Z\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid6 - 1",vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid6 - 2",vcard.getBirthplace().getText().equals("Los Angeles CA U.S.A."));
        assertTrue("testAnniversariesValid6 - 3",vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid6 - 4",vcard.getDeathplace().getText().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A."));
        assertTrue("testAnniversariesValid6 - 5",vcard.getAnniversary().getDate().compareTo(VCardDateFormat.parse("1986-02-01T19:00:00Z")) == 0 );
    }

    @Test
    public void testAnniversariesValid7() throws IOException, CardException {

        String jsCard = "{ " +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"anniversaries\":[ " +
                    "{" +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                              "\"coordinates\":\"geo:34.15876,-118.45728\"" +
                        "}" +
                    "}," +
                    "{" +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":{ " +
                                "\"value\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A.\"" +
                            "}" +
                        "}" +
                    "}," +
                    "{" +
                        "\"type\":\"other\", " +
                        "\"label\":\"marriage date\"," +
                        "\"date\":\"1986-02-01T19:00:00Z\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid7 - 1",vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid7 - 2",vcard.getBirthplace().getGeoUri().equals(GeoUri.parse("geo:34.15876,-118.45728")));
        assertTrue("testAnniversariesValid7 - 3",vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid7 - 4",vcard.getDeathplace().getText().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A."));
        assertTrue("testAnniversariesValid7 - 5",vcard.getAnniversary().getDate().compareTo(VCardDateFormat.parse("1986-02-01T19:00:00Z")) == 0 );
    }

    @Test
    public void testAnniversariesValid8() throws IOException, CardException {

        String jsCard = "{ " +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"anniversaries\":[ " +
                    "{" +
                        "\"type\":\"birth\", " +
                        "\"date\":\"1953-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"locality\":\"Los Angeles\"," +
                            "\"region\":\"CA\"," +
                            "\"country\":\"U.S.A.\"" +
                        "}" +
                    "}," +
                    "{" +
                        "\"type\":\"death\", " +
                        "\"date\":\"1993-10-15T23:10:00Z\"," +
                        "\"place\":{ " +
                            "\"fullAddress\":{ " +
                                "\"value\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A.\"" +
                            "}" +
                        "}" +
                    "}," +
                    "{" +
                        "\"type\":\"other\", " +
                        "\"label\":\"marriage date\"," +
                        "\"date\":\"1986-02-01T19:00:00Z\"" +
                    "}" +
                "]" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertTrue("testAnniversariesValid8 - 1",vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid8 - 2",vcard.getBirthplace().getText().equals("Los Angeles CA U.S.A."));
        assertTrue("testAnniversariesValid8 - 3",vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")) == 0 );
        assertTrue("testAnniversariesValid8 - 4",vcard.getDeathplace().getText().equals("Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 U.S.A."));
        assertTrue("testAnniversariesValid8 - 5",vcard.getAnniversary().getDate().compareTo(VCardDateFormat.parse("1986-02-01T19:00:00Z")) == 0 );
    }

}
