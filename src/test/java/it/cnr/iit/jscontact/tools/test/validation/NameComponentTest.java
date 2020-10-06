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

import it.cnr.iit.jscontact.tools.dto.NameComponentType;
import it.cnr.iit.jscontact.tools.dto.NameComponent;
import org.junit.Test;

public class NameComponentTest {

    @Test(expected = NullPointerException.class)
    public void testInvalidNameComponentBuild1() {

        // type missing
        NameComponent.builder()
                       .value("Mario")
                       .build();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidNameComponentBuild2() {

        // value missing
        NameComponent.builder()
                .type(NameComponentType.PERSONAL)
                .build();
    }


}
