package it.cnr.iit.jscontact.tools.test.localizations;

import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class LocalizationsTest {

    @Test
    public void testLocalizations1() throws IOException {

        String json = "{" +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"locality\":\"Tokyo\"" +
                    "}," +
                    "\"ADR-2\": {" +
                        "\"@type\":\"Address\"," +
                        "\"locality\":\"Osaka\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"jp\": {" +
                        "\"addresses/ADR-1/locality\" : \"東京\"," +
                        "\"addresses/ADR-2\" : {\"@type\":\"Address\",\"locality\": \"大阪市\"}" +
                    "}" +
                "}" +
                "}";


        Card jsCard = Card.toJSCard(json);
        Card localizedCard = jsCard.getLocalizedVersion("jp");

        assertEquals("testLocalizations1 - 1", "jp", localizedCard.getLanguage());
        assertNull("testLocalizations1 - 2", localizedCard.getLocalizations());
        assertEquals("testLocalizations1 - 3", 2, localizedCard.getAddresses().size());
        assertEquals("testLocalizations1 - 4", "東京", localizedCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testLocalizations1 - 5", "大阪市", localizedCard.getAddresses().get("ADR-2").getLocality());
    }

    @Test
    public void testLocalizations2() throws IOException {

        String json = "{" +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"addresses\":{" +
                "\"ADR-1\": {" +
                "\"@type\":\"Address\"," +
                "\"locality\":\"Tokyo\"" +
                "}," +
                "\"ADR-2\": {" +
                "\"@type\":\"Address\"," +
                "\"locality\":\"Osaka\"" +
                "}" +
                "}," +
                "\"localizations\":{" +
                "\"jp\": {" +
                "\"addresses/ADR-1/locality\" : \"東京\"," +
                "\"addresses/ADR-2\" : {\"@type\":\"Title\",\"name\": \"大阪市\"}" +
                "}" +
                "}" +
                "}";

        Card jsCard = Card.toJSCard(json);
        assertFalse("testLocalizations2 - 1", jsCard.isValid());
        assertEquals("testLocalizations2 - 2", "type mismatch of JSON pointer in localizations: addresses/ADR-2", jsCard.getValidationMessage().replace("\n", ""));
    }

    @Test
    public void testLocalizations3() throws IOException {

        String json = "{" +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"locality\":\"Tokyo\"" +
                    "}," +
                    "\"ADR-2\": {" +
                        "\"@type\":\"Address\"," +
                        "\"locality\":\"Osaka\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"jp\": {" +
                        "\"addresses/ADR-1/locality\" : \"東京\"," +
                        "\"addresses/ADR-2\" : {\"@type\":\"Unknown\",\"unknown\": \"大阪市\"}" +
                    "}" +
                "}" +
                "}";

        Card jsCard = Card.toJSCard(json);
        assertFalse("testLocalizations3 - 1", jsCard.isValid());
        assertEquals("testLocalizations3 - 2", "type mismatch of JSON pointer in localizations: addresses/ADR-2", jsCard.getValidationMessage().replace("\n", ""));
    }

    @Test
    public void testLocalizations4() throws IOException {

        String json = "{" +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"locality\":\"Tokyo\"" +
                    "}," +
                    "\"ADR-2\": {" +
                        "\"@type\":\"Address\"," +
                        "\"locality\":\"Osaka\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"jp\": {" +
                        "\"addresses/ADR-1/locality\" : \"東京\"," +
                        "\"addresses/ADR-1\" : { \"@type\":\"Unknown\",\"unknown\": \"大阪市\"}" +
                    "}" +
                "}" +
                "}";

        Card jsCard = Card.toJSCard(json);
        assertFalse("testLocalizations4 - 1", jsCard.isValid());
        assertEquals("testLocalizations4 - 2", "type mismatch of JSON pointer in localizations: addresses/ADR-1", jsCard.getValidationMessage().replace("\n", ""));
    }


    @Test
    public void testLocalizations5() throws IOException {

        String json = "{" +
                    "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                    "\"fullName\":\"Vasya Pupkin\"," +
                    "\"organizations\": { \"org\": { \"@type\": \"Organization\", \"name\": \"My Company\" } }, " +
                    "\"addresses\":{" +
                        "\"addr\": {" +
                            "\"@type\":\"Address\"," +
                            "\"street\": [{\"@type\": \"StreetComponent\",\"kind\": \"name\", \"value\": \"1 Street\"}, " +
                                         "{\"@type\": \"StreetComponent\",\"kind\": \"postOfficeBox\", \"value\":\"01001\"} " +
                                         "], " +
                            "\"locality\":\"Kyiv\"," +
                            "\"countryCode\":\"UA\"" +
                        "}" +
                    "}," +
                    "\"localizations\":{" +
                        "\"uk\": {" +
                            "\"fullName\":\"Вася Пупкин\"," +
                            "\"organizations/org\": { \"@type\": \"Organization\", \"name\": \"Моя Компания\" }, " +
                            "\"addresses/addr\":{" +
                                "\"@type\":\"Address\"," +
                                "\"street\": [{\"@type\": \"StreetComponent\",\"kind\": \"name\",\"value\": \"1, Улица\"}, " +
                                             "{\"@type\": \"StreetComponent\",\"kind\": \"postOfficeBox\",\"value\":\"01001\"} " +
                                             "], " +
                                "\"locality\":\"Киев\"," +
                                "\"countryCode\":\"UA\"" +
                            "}" +
                        "}" +
                    "}" +
                "}";

        Card jsCard = Card.toJSCard(json);
        assertTrue("testLocalizations5 - 1", jsCard.isValid());
    }


}