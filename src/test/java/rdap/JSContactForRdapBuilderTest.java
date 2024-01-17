package rdap;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.rdap.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSContactForRdapBuilderTest {

    @Test(expected = MissingFieldException.class)
    public void testJSContactForRdapBuilderInvalid1() throws MissingFieldException, CardException {
        JSContactForRdapBuilder.builder().build();
    }

    @Test(expected = CardException.class) // Missing fields in JSContact Name object
    public void testJSContactForRdapBuilderInvalid2() throws MissingFieldException, CardException {
        JSContactForRdapBuilder.builder().name(JSContactNameForRdapBuilder.builder().build()).build();
    }

    @Test
    public void testJSContactForRdapBuilderAndGetter() throws MissingFieldException, CardException {

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
                .address(JSContactAddressForRdapBuilder.builder()
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
        JSContactNameForRdapGetter rdapJSContactNameGetter = JSContactNameForRdapGetter.of(rdapJSContactGetter.name());
        assertEquals("testJSContactForRdapBuilderAndGetter - 1", "Mario Loffredo", rdapJSContactNameGetter.full());
        assertEquals("testJSContactForRdapBuilderAndGetter - 2", "Loffredo", rdapJSContactNameGetter.surname());
        assertEquals("testJSContactForRdapBuilderAndGetter - 3", "Mario", rdapJSContactNameGetter.given());
        assertEquals("testJSContactForRdapBuilderAndGetter - 4", ".it Registry", rdapJSContactGetter.org());
        assertEquals("testJSContactForRdapBuilderAndGetter - 5", "mario.loffredo@iit.cnr.it", rdapJSContactGetter.email());
        assertEquals("testJSContactForRdapBuilderAndGetter - 6", "+39.0503139811", rdapJSContactGetter.voice());
        assertEquals("testJSContactForRdapBuilderAndGetter - 7", "+39.0503139800", rdapJSContactGetter.fax());
        assertEquals("testJSContactForRdapBuilderAndGetter - 8", "https://www.nic.it", rdapJSContactGetter.url());
        JSContactAddressForRdapGetter rdapJSContactAddressGetter = JSContactAddressForRdapGetter.of(rdapJSContactGetter.address());
        assertEquals("testJSContactForRdapBuilderAndGetter - 9", "it", rdapJSContactAddressGetter.cc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 10", "Italy", rdapJSContactAddressGetter.country());
        assertEquals("testJSContactForRdapBuilderAndGetter - 11", "PI", rdapJSContactAddressGetter.sp());
        assertEquals("testJSContactForRdapBuilderAndGetter - 12", "Pisa", rdapJSContactAddressGetter.city());
        assertEquals("testJSContactForRdapBuilderAndGetter - 13", "56124", rdapJSContactAddressGetter.pc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 14", "Via Moruzzi, 1", rdapJSContactAddressGetter.street());
        JSContactNameForRdapGetter rdapJSContactNameLocGetter = JSContactNameForRdapGetter.of(rdapJSContactGetter.nameLoc("jp"));
        assertEquals("testJSContactForRdapBuilderAndGetter - 15", "マリオ ロフレド", rdapJSContactNameLocGetter.full());
        assertEquals("testJSContactForRdapBuilderAndGetter - 16", "ロフレド", rdapJSContactNameLocGetter.surname());
        assertEquals("testJSContactForRdapBuilderAndGetter - 17", "マリオ", rdapJSContactNameLocGetter.given());
        assertEquals("testJSContactForRdapBuilderAndGetter - 17", "マリオ", rdapJSContactNameLocGetter.given());
        JSContactAddressForRdapGetter rdapJSContactAddressLocGetter = JSContactAddressForRdapGetter.of(rdapJSContactGetter.addressLoc("jp"));
        assertEquals("testJSContactForRdapBuilderAndGetter - 18", "it", rdapJSContactAddressLocGetter.cc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 19", "イタリア", rdapJSContactAddressLocGetter.country());
        assertEquals("testJSContactForRdapBuilderAndGetter - 20", "PI", rdapJSContactAddressLocGetter.sp());
        assertEquals("testJSContactForRdapBuilderAndGetter - 21", "ピサ", rdapJSContactAddressLocGetter.city());
        assertEquals("testJSContactForRdapBuilderAndGetter - 22", "56124", rdapJSContactAddressLocGetter.pc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 23", "モルッツィ通り、1", rdapJSContactAddressLocGetter.street());
    }
}
