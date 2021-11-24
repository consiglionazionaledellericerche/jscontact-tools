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

import it.cnr.iit.jscontact.tools.dto.Anniversary;
import it.cnr.iit.jscontact.tools.dto.AnniversaryDate;
import it.cnr.iit.jscontact.tools.dto.AnniversaryType;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import org.junit.Test;

public class AnniversaryTest {

    public void testValidAnniversaryBuild1() {

        Anniversary.builder()
                       .date(AnniversaryDate.builder().date(DateUtils.toCalendar("2020-01-01")).build())
                       .build();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidAnniversaryBuild2() {

        // date missing
        Anniversary.builder()
                .type(AnniversaryType.BIRTH)
                .build();
    }


}
