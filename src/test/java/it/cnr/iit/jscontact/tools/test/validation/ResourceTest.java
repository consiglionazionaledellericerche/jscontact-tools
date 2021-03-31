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

import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;


public class ResourceTest extends AbstractTest {


    @Test
    public void testValidEmailResourceType1() {

        Resource email = Resource.builder()
                         .context(Context.WORK)
                         .type(EmailResourceType.EMAIL.getValue())
                         .value("mario.loffredo@iit.cnr.it")
                         .build();
        JSCard jsCard = JSCard.builder()
                         .uid(getUUID())
                         .emails(new Resource[]{email})
                         .build();

        assertTrue("testValidEmailResourceType1", jsCard.isValid());
    }

    @Test
    public void testValidEmailResourceType2() {

        Resource email = Resource.builder()
                .context(Context.WORK)
                .value("mario.loffredo@iit.cnr.it")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(new Resource[]{email})
                .build();

        assertTrue("testValidEmailResourceType2", jsCard.isValid());
    }

    @Test
    public void testInvalidEmailResourceType() {

        Resource email = Resource.builder()
                .context(Context.WORK)
                .type(PhoneResourceType.VOICE.getValue())
                .value("mario.loffredo@iit.cnr.it")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .emails(new Resource[]{email})
                .build();

        assertTrue("testInvalidEmailResourceType", !jsCard.isValid());
    }

    @Test
    public void testValidPhoneResourceType() {

        Resource phone = Resource.builder()
                .context(Context.WORK)
                .type(PhoneResourceType.VOICE.getValue())
                .value("+39.050000001")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .phones(new Resource[]{phone})
                .build();

        assertTrue("testValidPhoneResourceType", jsCard.isValid());
    }

    @Test
    public void testInvalidPhoneResourceType() {

        Resource phone = Resource.builder()
                .context(Context.WORK)
                .type(EmailResourceType.EMAIL.getValue())
                .value("+39.050000001")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .phones(new Resource[]{phone})
                .build();

        assertTrue("testInvalidPhoneResourceType", !jsCard.isValid());
    }


    @Test
    public void testValidOnlineResourceType() {

        Map<String,Boolean> labelsMap = new HashMap<String,Boolean>();
        labelsMap.put("GitHub", Boolean.TRUE);
        Resource online = Resource.builder()
                .context(Context.WORK)
                .type(OnlineResourceType.USERNAME.getValue())
                .value("mario-loffredo")
                .labels(labelsMap)
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new Resource[]{online})
                .build();

        assertTrue("testValidOnlineResourceType", jsCard.isValid());
    }

    @Test
    public void testInvalidOnlineResourceType() {

        Resource online = Resource.builder()
                .context(Context.WORK)
                .type(EmailResourceType.EMAIL.getValue())
                .value("mario-loffredo")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new Resource[]{online})
                .build();

        assertTrue("testInvalidOnlineResourceType", !jsCard.isValid());
    }


    @Test
    public void testInvalidResourceLabels1() {

        Map<String,Boolean> labels = new HashMap<String,Boolean>();
        labels.put("label", null);
        Resource online = Resource.builder()
                .context(Context.WORK)
                .type(OnlineResourceType.USERNAME.getValue())
                .value("mario-loffredo")
                .labels(labels)
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new Resource[]{online})
                .build();

        assertTrue("testInvalidResourceLabels1", !jsCard.isValid());
    }

    @Test
    public void testInvalidResourceLabels2() {

        Map<String,Boolean> labels = new HashMap<String,Boolean>();
        labels.put("label", Boolean.FALSE);
        Resource online = Resource.builder()
                .context(Context.WORK)
                .type(OnlineResourceType.USERNAME.getValue())
                .value("mario-loffredo")
                .labels(labels)
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new Resource[]{online})
                .build();

        assertTrue("testInvalidResourceLabels2", !jsCard.isValid());
    }

    @Test
    public void testValidResourceLabels() {

        Map<String,Boolean> labels = new HashMap<String,Boolean>();
        labels.put("label", Boolean.TRUE);
        Resource online = Resource.builder()
                .context(Context.WORK)
                .type(OnlineResourceType.USERNAME.getValue())
                .value("mario-loffredo")
                .labels(labels)
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new Resource[]{online})
                .build();

        assertTrue("testValidResourceLabels", jsCard.isValid());
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidResourceBuild() {

        // value missing
        Resource.builder()
                .context(Context.WORK)
                .type(EmailResourceType.EMAIL.getValue())
                .build();
    }

}
