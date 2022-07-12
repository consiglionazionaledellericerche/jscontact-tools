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
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.Resource;
import it.cnr.iit.jscontact.tools.dto.ResourceType;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;


public class ResourceTest extends AbstractTest {

    @Test
    public void testValidResource() {

        Resource resource = Resource.builder()
                .context(Context.work(), Boolean.TRUE)
                .resource("mailto:mario.loffredo@iit.cnr.it")
                .type(ResourceType.CONTACT_URI)
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .resources(new HashMap<String,Resource>(){{ put("MAILTO-1", resource);}})
                .build();

        assertTrue("testValidResource", jsCard.isValid());
    }


    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidResourceBuild() {

        // value missing
        Resource.builder()
                .context(Context.work(), Boolean.TRUE)
                .type(ResourceType.URI)
                .build();
    }

    @Test
    public void testInvalidResourceUri() {

        Resource resource = Resource.builder()
                .context(Context.work(), Boolean.TRUE)
                .type(ResourceType.URI)
                .resource(" ")
                .label("url")
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .resources(new HashMap<String,Resource>(){{ put("URI-1", resource);}})
                .build();

        assertFalse("testInvalidResourceUri-1", jsCard.isValid());
        assertEquals("testInvalidResourceUri-2", "invalid uri in Resource", jsCard.getValidationMessage());
    }


}
