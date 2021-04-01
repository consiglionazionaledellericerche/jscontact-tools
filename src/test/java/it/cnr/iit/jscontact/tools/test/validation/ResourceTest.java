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

import static javax.swing.UIManager.put;
import static org.junit.Assert.assertTrue;


public class ResourceTest extends AbstractTest {

    @Test
    public void testValidOnlineType() {

        Resource online = Resource.builder()
                .contexts(new HashMap<Context,Boolean>(){{put(Context.WORK, Boolean.TRUE);}})
                .type(ResourceType.USERNAME)
                .resource("mario-loffredo")
                .label("GitHub")
                .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .online(new HashMap<String,Resource>(){{ put("XMPP-1", online);}})
                .build();

        assertTrue("testValidOnlineType", jsCard.isValid());
    }


    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidResourceBuild() {

        // value missing
        Resource.builder()
                .contexts(new HashMap<Context,Boolean>(){{put(Context.WORK, Boolean.TRUE);}})
                .type(ResourceType.URI)
                .build();
    }

}
