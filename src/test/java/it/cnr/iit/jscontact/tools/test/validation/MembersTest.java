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

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.KindType;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;


public class MembersTest extends AbstractTest {

    @Test
    public void testValidMembersWithKindGroup() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .kind(KindType.group())
                .members(new HashMap<String,Boolean>(){{ put(getUUID(), Boolean.TRUE);}})
                .build();

        assertTrue("testValidMembersWithKindGroup", jsCard.isValid());
    }

    @Test
    public void testInvalidMembersWithoutKindGroup() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .version(getVersion())
                .members(new HashMap<String,Boolean>(){{ put(getUUID(), Boolean.TRUE);}})
                .build();

        assertFalse("testInvalidMembersWithoutKindGroup-1", jsCard.isValid());
        assertEquals("testInvalidMembersWithoutKindGroup-2", "not empty members requires kind=group", jsCard.getValidationMessage());

    }

}
