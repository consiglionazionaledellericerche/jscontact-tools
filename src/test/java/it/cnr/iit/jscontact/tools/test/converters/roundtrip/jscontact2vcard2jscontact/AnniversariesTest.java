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
package it.cnr.iit.jscontact.tools.test.converters.roundtrip.jscontact2vcard2jscontact;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.test.converters.roundtrip.RoundtripTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AnniversariesTest extends RoundtripTest {

    @Test
    public void testAnniversaries1() throws IOException, CardException {

        String jscard = "{ " +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                        "\"name\":{\"full\":\"test\"}," +
                        "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                               "{" +
                                    "\"@type\":\"Anniversary\"," +
                                   "\"kind\":\"birth\", " +
                                    "\"date\":{" +
                                        "\"@type\":\"Timestamp\"," +
                                        "\"utc\":\"1953-10-15T23:10:00Z\"" +
                                    "}" +
                               "}" +
                        "}" +
                        "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries1 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries2() throws IOException, CardException {

        String jscard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\":{\"full\":\"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                           "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries2 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries3() throws IOException, CardException {

        String jscard = "{ " +
                        "\"@type\":\"Card\"," +
                        "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                        "\"name\":{\"full\":\"test\"}," +
                        "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                               "{" +
                                    "\"@type\":\"Anniversary\"," +
                                   "\"kind\":\"death\", " +
                                "\"date\":{" +
                                    "\"@type\":\"Timestamp\"," +
                                    "\"utc\":\"1953-10-15T23:10:00Z\"" +
                                "}" +
                               "}" +
                        "}" +
                        "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries3 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries4() throws IOException, CardException {

        String jscard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\":{\"full\":\"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries4 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries5() throws IOException, CardException {

        String jscard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\":{\"full\":\"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Los Angeles CA USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1993-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries5 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries6() throws IOException, CardException {

        String jscard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\":{\"full\":\"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Los Angeles CA USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1993-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"wedding\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1986-02-01T19:00:00Z\"" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries6 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries7() throws IOException, CardException {

        String jscard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\":{\"full\":\"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                              "\"@type\":\"Address\"," +
                              "\"coordinates\":\"geo:34.15876,-118.45728\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1993-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"wedding\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1986-02-01T19:00:00Z\"" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries7 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries8() throws IOException, CardException {

        String jscard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\":{\"full\":\"test\"}," +
                "\"anniversaries\": { \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"locality\":\"Los Angeles\"," +
                            "\"region\":\"CA\"," +
                            "\"country\":\"USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1993-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"wedding\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1986-02-01T19:00:00Z\"" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries8 - 1", jscard2, Card.toJSCard(jscard));
    }

    @Test
    public void testAnniversaries9() throws IOException, CardException {

        String jscard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\":{\"full\":\"test\"}," +
                "\"anniversaries\": { " +
                    "\"ANNIVERSARY-1\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"locality\":\"Los Angeles\"," +
                            "\"region\":\"CA\"," +
                            "\"country\":\"USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1993-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"@type\":\"Address\"," +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"wedding\", " +
                        "\"date\":{" +
                            "\"@type\":\"PartialDate\"," +
                            "\"year\":1986, " +
                            "\"month\":2 " +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        Card jscard2 = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testAnniversaries9 - 1", jscard2, Card.toJSCard(jscard));
    }

}
