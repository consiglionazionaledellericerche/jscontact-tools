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
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class LocaleTest extends AbstractTest {

    @Test
    public void testValidLocale() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .locale("it")
                .build();

        assertTrue("testValidLocale", jsCard.isValid());
    }

    @Test
    public void testInvalidLocale() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .locale("@@@@")
                .build();

        assertTrue("testInvalidLocale-1", !jsCard.isValid());
        assertTrue("testInvalidLocale-2", jsCard.getValidationMessage().equals("invalid language tag"));

    }


}
