package it.cnr.iit.jscontact.tools.test.localizations;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

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
                        "\"/addresses/ADR-1/locality\" : \"東京\"," +
                        "\"/addresses/ADR-2\" : {\"@type\":\"Address\",\"locality\": \"大阪市\"}" +
                    "}" +
                "}" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        Card localizedCard = jsCard.getLocalizedVersion("jp");

        assertTrue("testLocalizations1 - 1", localizedCard.getLanguage().equals("jp"));
        assertTrue("testLocalizations1 - 2", localizedCard.getLocalizations() == null);
        assertTrue("testLocalizations1 - 3", localizedCard.getAddresses().size() == 2);
        assertTrue("testLocalizations1 - 4", localizedCard.getAddresses().get("ADR-1").getLocality().equals("東京"));
        assertTrue("testLocalizations1 - 5", localizedCard.getAddresses().get("ADR-2").getLocality().equals("大阪市"));
    }

    @Test (expected = IllegalArgumentException.class)
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
                        "\"/addresses/ADR-1/locality\" : \"東京\"," +
                        "\"/addresses/ADR-2\" : {\"@type\":\"Address\",\"unknown\": \"大阪市\"}" +
                    "}" +
                "}" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        Card localizedCard = jsCard.getLocalizedVersion("jp");
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
                        "\"/addresses/ADR-1/locality\" : \"東京\"," +
                        "\"/addresses/ADR-2\" : {\"@type\":\"Address\",\"unknown\": \"大阪市\"}" +
                    "}" +
                "}" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        assertTrue("testLocalizations3 - 1", !jsCard.isValid());
        assertTrue("testLocalizations3 - 2", jsCard.getValidationMessage().replace("\n","").equals("type mismatch of JSON pointer in localizations: /addresses/ADR-2"));
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
                        "\"/addresses/ADR-1/locality\" : \"東京\"," +
                        "\"addresses/ADR-1\" : { \"@type\":\"Address\",\"unknown\": \"大阪市\"}" +
                    "}" +
                "}" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        assertTrue("testLocalizations4 - 1", !jsCard.isValid());
        assertTrue("testLocalizations4 - 2", jsCard.getValidationMessage().replace("\n","").equals("type mismatch of JSON pointer in localizations: addresses/ADR-1"));
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
                            "\"street\": [{\"@type\": \"StreetComponent\",\"type\": \"name\", \"value\": \"1 Street\"}, " +
                                         "{\"@type\": \"StreetComponent\",\"type\": \"postOfficeBox\", \"value\":\"01001\"} " +
                                         "], " +
                            "\"locality\":\"Kyiv\"," +
                            "\"countryCode\":\"UA\"" +
                        "}" +
                    "}," +
                    "\"localizations\":{" +
                        "\"uk\": {" +
                            "\"/fullName\":\"Вася Пупкин\"," +
                            "\"/organizations/org\": { \"@type\": \"Organization\", \"name\": \"Моя Компания\" }, " +
                            "\"/addresses/addr\":{" +
                                "\"@type\":\"Address\"," +
                                "\"street\": [{\"@type\": \"StreetComponent\",\"type\": \"name\",\"value\": \"1, Улица\"}, " +
                                             "{\"@type\": \"StreetComponent\",\"type\": \"postOfficeBox\",\"value\":\"01001\"} " +
                                             "], " +
                                "\"locality\":\"Киев\"," +
                                "\"countryCode\":\"UA\"" +
                            "}" +
                        "}" +
                    "}" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        assertTrue("testLocalizations5 - 1", jsCard.isValid());
    }


}