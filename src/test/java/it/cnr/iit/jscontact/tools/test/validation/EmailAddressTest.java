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
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class EmailAddressTest extends AbstractTest {

    @Test(expected = NullPointerException.class)
    public void testInvalidEmailBuild1() {

        // email missing
        EmailAddress.builder().build();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidEmailBuild2() {

        // email missing
        EmailAddress.builder().contexts(new HashMap<Context,Boolean>() {{ put(Context.WORK,Boolean.TRUE);}}).build();
    }


    @Test(expected = NullPointerException.class)
    public void testInvalidEmailBuild3() {

        // email missing
        EmailAddress.builder().pref(1).build();
    }

    @Test
    public void testValidEmail1() {

        EmailAddress email = EmailAddress.builder()
                .email("mario.loffredo@iit.cnr.it")
                .build();
        Map<String,EmailAddress> emailsMap = new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }};
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(emailsMap)
                .build();

        assertTrue("testValidEmail1", jsCard.isValid());
    }

    @Test
    public void testValidEmail2() {

        EmailAddress email = EmailAddress.builder()
                .contexts(new HashMap<Context, Boolean>() {{ put(Context.WORK, Boolean.TRUE);}})
                .email("mario.loffredo@iit.cnr.it")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertTrue("testValidEmail2", jsCard.isValid());
    }

    @Test
    public void testValidEmail3() {

        EmailAddress email = EmailAddress.builder()
                .contexts(new HashMap<Context, Boolean>() {{ put(Context.WORK, Boolean.TRUE); }})
                .email("mario.loffredo@iit.cnr.it")
                .pref(1)
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertTrue("testValidEmail3", jsCard.isValid());
    }

    @Test
    public void testInvalidEmail1() {

        //invalid email address
        EmailAddress email = EmailAddress.builder()
                .email("mario.loffredo")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertTrue("testInvalidEmail1-1", !jsCard.isValid());
        assertTrue("testInvalidEmail1-2", jsCard.getValidationMessage().equals("invalid email in Email"));
    }


    @Test
    public void testInvalidEmail2() {

        //invalid contexts
        EmailAddress email = EmailAddress.builder()
                .contexts(new HashMap<Context, Boolean>() {{ put(Context.WORK, Boolean.FALSE); }})
                .email("mario.loffredo@iit.cnr.it")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertTrue("testInvalidEmail2-1", !jsCard.isValid());
        assertTrue("testInvalidEmail2-2", jsCard.getValidationMessage().equals("invalid Map<Context,Boolean> contexts in EmailAddress - Only Boolean.TRUE allowed"));
    }


    @Test
    public void testInvalidEmail3() {

        //invalid pref
        EmailAddress email = EmailAddress.builder()
                .contexts(new HashMap<Context, Boolean>() {{ put(Context.WORK, Boolean.TRUE); }})
                .email("mario.loffredo@iit.cnr.it")
                .pref(0)
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(new HashMap<String, EmailAddress>() {{ put("EMAIL-1", email); }})
                .build();

        assertTrue("testInvalidEmail3-1", !jsCard.isValid());
        assertTrue("testInvalidEmail3-2", jsCard.getValidationMessage().equals("invalid pref in Email - min value must be 1"));
    }

}
