package rdap;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.iit.jscontact.tools.constraints.groups.Version_2_0;
import it.cnr.iit.jscontact.tools.constraints.groups.profiles.Profile_RDAP;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.KindType;
import it.cnr.iit.jscontact.tools.dto.utils.VersionUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.rdap.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSContactForRdapTest {

    @Test(expected = MissingFieldException.class)
    public void testJSContactForRdapInvalid1() throws MissingFieldException, CardException {
        JSContactForRdapBuilder.builder().build();
    }

    public void testJSContactForRdapInvalid2() {
        try {
            JSContactForRdapBuilder.builder().name(JSContactNameForRdapBuilder.builder().build()).build();
            assertFalse(true);
        } catch(MissingFieldException | CardException e) {
            assertEquals("testJSContactForRdapInvalid2", e.getMessage(), "At least one between name, organizations, addresses, phones, emails and links must be set in JSCard");
        }
    }

    public void testJSContactForRdapInvalid3() {
        try {
            JSContactForRdapBuilder.builder().name(JSContactNameForRdapBuilder.builder().given("Mario").surname("Loffredo").build()).build();
            assertFalse(true);
        } catch(MissingFieldException | CardException e) {
            assertEquals("testJSContactForRdapInvalid3", e.getMessage(), "full is missing in Name for RDAP profile");
        }
    }

    public void testJSContactForRdapInvalid4() {
        try {
            JSContactForRdapBuilder.builder().name(JSContactNameForRdapBuilder.builder().full("Mario Loffredo").build()).kind(KindType.application()).build();
            assertFalse(true);
        } catch(MissingFieldException | CardException e) {
            assertEquals("testJSContactForRdapInvalid4", e.getMessage(), "invalid kind value for RDAP profile");
        }
    }

    public void testJSContactForRdapInvalid5() {

        try {
            Card jsCard = JSContactForRdapBuilder.builder()
                .name(JSContactNameForRdapBuilder.builder()
                        .full("Mario Loffredo")
                        .surname("Loffredo")
                        .given("Mario")
                        .build())
                .org(".it Registry")
                .email("mario.loffredo@iit.cnr.it")
                .voice("+39.0503139811")
                .fax("+39.0503139800")
                .addr(JSContactAddressForRdapBuilder.builder()
                        .cc("it")
                        .country("Italy")
                        .sp("PI")
                        .city("Pisa")
                        .pc("56124")
                        .street("Via Moruzzi, 1")
                        .build())
                .url("https://www.nic.it")
                .nameLoc("jp", JSContactNameForRdapBuilder.builder()
                        .full("マリオ ロフレド")
                        .surname("ロフレド")
                        .given("マリオ")
                        .build())
                .orgLoc("jp", ".itレジストリ")
                .addrLoc("jp", JSContactAddressForRdapBuilder.builder()
                        .cc("it")
                        .country("イタリア")
                        .sp("PI")
                        .city("ピサ")
                        .pc("56124")
                        .street("モルッツィ通り、1")
                        .build())
                .build();
            assertFalse(true);
        } catch(MissingFieldException | CardException e) {
            assertEquals("testJSContactForRdapInvalid5", e.getMessage(), "the language property should be set when the localizations property is set");
        }
    }


    public void testJSContactForRdapInvalid6() {
        try {
            JSContactForRdapBuilder.builder().version(VersionUtils.VersionEnum.VERSION_1_0).name(JSContactNameForRdapBuilder.builder().full("Mario Loffredo").build()).build();
            assertFalse(true);
        } catch(MissingFieldException | CardException e) {
            assertEquals("testJSContactForRdapInvalid6", e.getMessage(), "invalid version value for RDAP profile");
        }
    }

    public void testJSContactForRdapInvalid7() throws JsonProcessingException {
            String json = "{" +
                "\"version\":\"2.0\"," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                    "\"addr\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"locality\",\"value\":\"Osaka\"}" +
                        "]" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"jp\": {" +
                        "\"addresses/addr\" : { " +
                            "\"@type\":\"Address\"," +
                            "\"components\":[ " +
                                "{\"kind\":\"locality\",\"value\":\"大阪市\"}" +
                            "]" +
                        "}" +
                    "}" +
                "}" +
            "}";

            Card jsCard =  Card.toJSCard(json);
            boolean isValid = jsCard.isValid(Version_2_0.class, Profile_RDAP.class);
            assertFalse("testJSContactForRdapInvalid7 - 1",isValid);
            assertEquals("testJSContactForRdapInvalid7 - 2", jsCard.getValidationMessage(), "profile does not support nested PatchObject keys in localizations");
    }

    public void testJSContactForRdapInvalid8() throws JsonProcessingException {

        String json = "{" +
                "\"version\":\"2.0\"," +
                "\"addresses\":{" +
                    "\"address\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"locality\",\"value\":\"Osaka\"}" +
                        "]" +
                    "}" +
                "}" +
                "}";

        Card jsCard =  Card.toJSCard(json);
        boolean isValid = jsCard.isValid(Version_2_0.class, Profile_RDAP.class);
        assertFalse("testJSContactForRdapInvalid8 - 1",isValid);
        assertEquals("testJSContactForRdapInvalid8 - 2", jsCard.getValidationMessage(), "missing key in addresses map for RDAP profile, at least addr must be present");
    }

    public void testJSContactForRdapInvalid9() throws JsonProcessingException {

        String json = "{" +
                "\"version\":\"2.0\"," +
                "\"emails\":{" +
                    "\"email-address\": {" +
                        "\"@type\":\"EmailAddress\"," +
                        "\"address\": \"joe.user@example.com\"" +
                    "}" +
                "}" +
                "}";

        Card jsCard =  Card.toJSCard(json);
        boolean isValid = jsCard.isValid(Version_2_0.class, Profile_RDAP.class);
        assertFalse("testJSContactForRdapInvalid9 - 1",isValid);
        assertEquals("testJSContactForRdapInvalid9 - 2", jsCard.getValidationMessage(), "missing key in emails map for RDAP profile, at least email must be present");
    }

    public void testJSContactForRdapInvalid10() throws JsonProcessingException {

        String json = "{" +
                "\"version\":\"2.0\"," +
                "\"phones\":{" +
                    "\"phone-number\": {" +
                        "\"@type\":\"Phone\"," +
                        "\"number\": \"tel:+1-555-555-1234;ext=102\"" +
                    "}" +
                "}" +
                "}";

        Card jsCard =  Card.toJSCard(json);
        boolean isValid = jsCard.isValid(Version_2_0.class, Profile_RDAP.class);
        assertFalse("testJSContactForRdapInvalid10 - 1",isValid);
        assertEquals("testJSContactForRdapInvalid10 - 2", jsCard.getValidationMessage(), "missing key in phones map for RDAP profile, at least one between voice and fax must be present");
    }

    public void testJSContactForRdapInvalid11() throws JsonProcessingException {

        String json = "{" +
                "\"version\":\"2.0\"," +
                "\"organizations\":{" +
                    "\"organization\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"name\": \"Example\"" +
                    "}" +
                "}" +
                "}";

        Card jsCard =  Card.toJSCard(json);
        boolean isValid = jsCard.isValid(Version_2_0.class, Profile_RDAP.class);
        assertFalse("testJSContactForRdapInvalid11 - 1",isValid);
        assertEquals("testJSContactForRdapInvalid11 - 2", jsCard.getValidationMessage(), "missing key in organizations map for RDAP profile, at least org must be present");
    }

    public void testJSContactForRdapInvalid12() throws JsonProcessingException {

        String json = "{" +
                "\"version\":\"2.0\"," +
                "\"links\":{" +
                    "\"link1\": {" +
                        "\"@type\":\"Link\"," +
                        "\"uri\": \"http://example.org\"" +
                    "}" +
                "}" +
                "}";

        Card jsCard =  Card.toJSCard(json);
        boolean isValid = jsCard.isValid(Version_2_0.class, Profile_RDAP.class);
        assertFalse("testJSContactForRdapInvalid12 - 1",isValid);
        assertEquals("testJSContactForRdapInvalid12 - 2", jsCard.getValidationMessage(), "missing key in links map for RDAP profile, at least one between url and contact-uri must be present");
    }
    
    @Test
    public void testJSContactForRdapAndGetter() throws MissingFieldException, CardException {

        Card jsCard = JSContactForRdapBuilder.builder()
                .language("it")
                .name(JSContactNameForRdapBuilder.builder()
                        .full("Mario Loffredo")
                        .surname("Loffredo")
                        .given("Mario")
                        .build())
                .org(".it Registry")
                .email("mario.loffredo@iit.cnr.it")
                .voice("+39.0503139811")
                .fax("+39.0503139800")
                .addr(JSContactAddressForRdapBuilder.builder()
                        .cc("it")
                        .country("Italy")
                        .sp("PI")
                        .city("Pisa")
                        .pc("56124")
                        .street("Via Moruzzi, 1")
                        .build())
                .url("https://www.nic.it")
                .nameLoc("jp", JSContactNameForRdapBuilder.builder()
                        .full("マリオ ロフレド")
                        .surname("ロフレド")
                        .given("マリオ")
                        .build())
                .orgLoc("jp", ".itレジストリ")
                .addrLoc("jp", JSContactAddressForRdapBuilder.builder()
                        .cc("it")
                        .country("イタリア")
                        .sp("PI")
                        .city("ピサ")
                        .pc("56124")
                        .street("モルッツィ通り、1")
                        .build())
                .build();

        JSContactForRdapGetter rdapJSContactGetter = JSContactForRdapGetter.of(jsCard);
        // JSContactForRdapBuilder uses JSContact Version 2.0 that doesn't mandate the uid property
        assertNull("testJSContactForRdapAndGetter - 1", rdapJSContactGetter.uid());
        assertEquals("testJSContactForRdapAndGetter - 2", "it", rdapJSContactGetter.language());
        JSContactNameForRdapGetter rdapJSContactNameGetter = JSContactNameForRdapGetter.of(rdapJSContactGetter.name());
        assertEquals("testJSContactForRdapAndGetter - 3", "Mario Loffredo", rdapJSContactNameGetter.full());
        assertEquals("testJSContactForRdapAndGetter - 4", "Loffredo", rdapJSContactNameGetter.surname());
        assertEquals("testJSContactForRdapAndGetter - 5", "Mario", rdapJSContactNameGetter.given());
        assertEquals("testJSContactForRdapAndGetter - 6", ".it Registry", rdapJSContactGetter.org());
        assertEquals("testJSContactForRdapAndGetter - 7", "mario.loffredo@iit.cnr.it", rdapJSContactGetter.email());
        assertEquals("testJSContactForRdapAndGetter - 8", "+39.0503139811", rdapJSContactGetter.voice());
        assertEquals("testJSContactForRdapAndGetter - 9", "+39.0503139800", rdapJSContactGetter.fax());
        assertEquals("testJSContactForRdapAndGetter - 10", "https://www.nic.it", rdapJSContactGetter.url());
        JSContactAddressForRdapGetter rdapJSContactAddressGetter = JSContactAddressForRdapGetter.of(rdapJSContactGetter.address());
        assertEquals("testJSContactForRdapAndGetter - 11", "it", rdapJSContactAddressGetter.cc());
        assertEquals("testJSContactForRdapAndGetter - 12", "Italy", rdapJSContactAddressGetter.country());
        assertEquals("testJSContactForRdapAndGetter - 13", "PI", rdapJSContactAddressGetter.sp());
        assertEquals("testJSContactForRdapAndGetter - 14", "Pisa", rdapJSContactAddressGetter.city());
        assertEquals("testJSContactForRdapAndGetter - 15", "56124", rdapJSContactAddressGetter.pc());
        assertEquals("testJSContactForRdapAndGetter - 16", "Via Moruzzi, 1", rdapJSContactAddressGetter.street());
        JSContactNameForRdapGetter rdapJSContactNameLocGetter = JSContactNameForRdapGetter.of(rdapJSContactGetter.nameLoc("jp"));
        assertEquals("testJSContactForRdapAndGetter - 17", "マリオ ロフレド", rdapJSContactNameLocGetter.full());
        assertEquals("testJSContactForRdapAndGetter - 18", "ロフレド", rdapJSContactNameLocGetter.surname());
        assertEquals("testJSContactForRdapAndGetter - 19", "マリオ", rdapJSContactNameLocGetter.given());
        assertEquals("testJSContactForRdapAndGetter - 20", "マリオ", rdapJSContactNameLocGetter.given());
        JSContactAddressForRdapGetter rdapJSContactAddressLocGetter = JSContactAddressForRdapGetter.of(rdapJSContactGetter.addressLoc("jp"));
        assertEquals("testJSContactForRdapAndGetter - 22", "it", rdapJSContactAddressLocGetter.cc());
        assertEquals("testJSContactForRdapAndGetter - 22", "イタリア", rdapJSContactAddressLocGetter.country());
        assertEquals("testJSContactForRdapAndGetter - 23", "PI", rdapJSContactAddressLocGetter.sp());
        assertEquals("testJSContactForRdapAndGetter - 24", "ピサ", rdapJSContactAddressLocGetter.city());
        assertEquals("testJSContactForRdapAndGetter - 25", "56124", rdapJSContactAddressLocGetter.pc());
        assertEquals("testJSContactForRdapAndGetter - 26", "モルッツィ通り、1", rdapJSContactAddressLocGetter.street());
    }
}
