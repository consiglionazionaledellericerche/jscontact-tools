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
package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.dto.EmailAddress;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.builders.ContextsBuilder;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EmailAddressTest extends AbstractTest {

    @Test(expected = NullPointerException.class)
    public void testInvalidEmailBuild1() {

        // email missing
        EmailAddress.builder().build();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidEmailBuild2() {

        // email missing
        EmailAddress.builder().contexts(ContextsBuilder.builder().work().build()).build();
    }


    @Test(expected = NullPointerException.class)
    public void testInvalidEmailBuild3() {

        // email missing
        EmailAddress.builder().pref(1).build();
    }

    @Test
    public void testValidEmail1() {

        EmailAddress email = EmailAddress.builder()
                .address("mario.loffredo@iit.cnr.it")
                .build();
        Map<String, EmailAddress> emailsMap = new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .emails(emailsMap)
                .build();

        assertTrue("testValidEmail1", jsCard.isValid());
    }

    @Test
    public void testValidEmail2() {

        EmailAddress email = EmailAddress.builder()
                .contexts(ContextsBuilder.builder().work().build())
                .address("mario.loffredo@iit.cnr.it")
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertTrue("testValidEmail2", jsCard.isValid());
    }

    @Test
    public void testValidEmail3() {

        EmailAddress email = EmailAddress.builder()
                .contexts(ContextsBuilder.builder().work().build())
                .address("mario.loffredo@iit.cnr.it")
                .pref(1)
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertTrue("testValidEmail3", jsCard.isValid());
    }

    @Test
    public void testInvalidEmail1() {

        //invalid email address
        EmailAddress email = EmailAddress.builder()
                .address("mario.loffredo")
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertFalse("testInvalidEmail1-1", jsCard.isValid());
        assertEquals("testInvalidEmail1-2", "invalid address in EmailAddress", jsCard.getValidationMessage());
    }


    @Test
    public void testInvalidEmail2() {

        //invalid contexts
        EmailAddress email = EmailAddress.builder()
                .contexts(new HashMap<Context,Boolean>(){{put (Context.work(), Boolean.FALSE);}})
                .address("mario.loffredo@iit.cnr.it")
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertFalse("testInvalidEmail2-1", jsCard.isValid());
        assertEquals("testInvalidEmail2-2", "invalid Map<Context,Boolean> contexts in EmailAddress - Only Boolean.TRUE allowed", jsCard.getValidationMessage());
    }


    @Test
    public void testInvalidEmail3() {

        //invalid pref
        EmailAddress email = EmailAddress.builder()
                .contexts(ContextsBuilder.builder().work().build())
                .address("mario.loffredo@iit.cnr.it")
                .pref(0)
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertFalse("testInvalidEmail3-1", jsCard.isValid());
        assertEquals("testInvalidEmail3-2", "invalid pref in EmailAddress - value must be greater or equal than 1", jsCard.getValidationMessage());
    }

}
