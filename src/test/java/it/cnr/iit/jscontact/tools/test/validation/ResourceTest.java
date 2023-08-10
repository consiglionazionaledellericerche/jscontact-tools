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
import it.cnr.iit.jscontact.tools.dto.utils.builders.ContextsBuilder;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;


public class ResourceTest extends AbstractTest {

    @Test
    public void testValidLinkResource() {

        LinkResource resource = LinkResource.builder()
                .uri("mailto:mario.loffredo@iit.cnr.it")
                .kind(LinkResourceKind.contact())
                .contexts(new ContextsBuilder().work().build())
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .links(new HashMap<String,LinkResource>(){{ put("CONTACT-1", resource);}})
                .build();

        assertTrue("testValidLinkResource", jsCard.isValid());
    }


    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidLinkResourceBuild() {

        // value missing
        LinkResource.builder()
                .contexts(new ContextsBuilder().work().build())
                .kind(LinkResourceKind.contact())
                .build();
    }

    @Test
    public void testInvalidLinkResourceUri() {

        LinkResource resource = LinkResource.builder()
                .kind(LinkResourceKind.contact())
                .uri(" ")
                .label("url")
                .contexts(new ContextsBuilder().work().build())
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .links(new HashMap<String,LinkResource>(){{ put("CONTACT-1", resource);}})
                .build();

        assertFalse("testInvalidResourceUri-1", jsCard.isValid());
        assertEquals("testInvalidResourceUri-2", "invalid uri", jsCard.getValidationMessage());
    }


}
