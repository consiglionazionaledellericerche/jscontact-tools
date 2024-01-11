package rdap;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.rdap.JSContactAddressForRdapBuilder;
import it.cnr.iit.jscontact.tools.rdap.JSContactForRdapBuilder;
import it.cnr.iit.jscontact.tools.rdap.JSContactNameForRdapBuilder;
import it.cnr.iit.jscontact.tools.rdap.MissingFieldsException;
import org.junit.Test;

public class JSContactForRdapBuilderTest {

    @Test(expected = MissingFieldsException.class)
    public void testJSContactForRdapBuilderInvalid1() throws MissingFieldsException, CardException {
        Card jsCard = JSContactForRdapBuilder.builder().build();
    }

    @Test(expected = CardException.class) // Missing fields in JSContact Name object
    public void testJSContactForRdapBuilderInvalid2() throws MissingFieldsException, CardException {
        Card jsCard = JSContactForRdapBuilder.builder().name(JSContactNameForRdapBuilder.builder().build()).build();
    }

    @Test
    public void testJSContactForRdapBuilder1() throws MissingFieldsException, CardException {
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
                                            .build();
    }

    @Test
    public void testJSContactForRdapBuilder2() throws MissingFieldsException, CardException {

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
                    .nameLocalization("jp", JSContactNameForRdapBuilder.builder()
                            .full("マリオ ロフレド")
                            .surname("ロフレド")
                            .given("マリオ")
                            .build())
                    .orgLocalization("jp", ".itレジストリ")
                    .addrLocalization("jp", JSContactAddressForRdapBuilder.builder()
                            .cc("it")
                            .country("イタリア")
                            .sp("PI")
                            .city("ピサ")
                            .pc("56124")
                            .street("モルッツィ通り、1")
                            .build())
                    .build();
    }

}
