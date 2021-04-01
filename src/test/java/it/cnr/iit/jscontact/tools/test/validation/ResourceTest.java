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

import static org.junit.Assert.assertTrue;


public class ResourceTest extends AbstractTest {

    @Test
    public void testValidOnlineType() {

        Resource online = Resource.builder()
                .context(Context.WORK)
                .type(OnlineType.USERNAME.getValue())
                .value("mario-loffredo")
                .label("GitHub")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new Resource[]{online})
                .build();

        assertTrue("testValidOnlineType", jsCard.isValid());
    }

    @Test
    public void testInvalidOnlineType() {

        Resource online = Resource.builder()
                .context(Context.WORK)
                .type(PhoneType.VOICE.getValue())
                .value("mario-loffredo")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new Resource[]{online})
                .build();

        assertTrue("testInvalidOnlineType-1", !jsCard.isValid());
        assertTrue("testInvalidOnlineType-2", jsCard.getValidationMessage().equals("invalid online Resource in JSContact"));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidResourceBuild() {

        // value missing
        Resource.builder()
                .context(Context.WORK)
                .type(OnlineType.URI.getValue())
                .build();
    }

}
