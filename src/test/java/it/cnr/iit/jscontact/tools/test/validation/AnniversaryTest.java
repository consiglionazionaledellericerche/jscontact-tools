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
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class AnniversaryTest extends AbstractTest  {

    @Test
    public void testValidAnniversaryBuild1() {

        Anniversary.builder()
                .date(AnniversaryDate.builder().date(Timestamp.builder().utc(DateUtils.toCalendar("2020-01-01")).build()).build())
                .build();
    }

    @Test
    public void testValidAnniversaryBuild2() {

        Anniversary.builder()
                .date(AnniversaryDate.builder().partialDate(PartialDate.builder().year(2020).month(1).day(1).build()).build())
                .build();
    }



    @Test(expected = NullPointerException.class)
    public void testInvalidAnniversaryBuild2() {

        // date missing
        Anniversary.builder()
                .kind(AnniversaryKind.birth())
                .build();
    }

    @Test
    public void testInvalidAnniversaryBuild3() {

        Anniversary anniversary = Anniversary.builder()
                .kind(AnniversaryKind.wedding())
                .date(AnniversaryDate.builder().partialDate(PartialDate.builder().year(2020).day(10).build()).build())
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .anniversaries(new HashMap<String, Anniversary>() {{ put("AN-1", anniversary); }})
                .build();

        assertFalse("testInvalidAnniversaryBuild3-1", jsCard.isValid());
        assertTrue("testValidAnniversaryBuild3-2", jsCard.getValidationMessage().contains("if day is set, month must be set"));
    }

    @Test
    public void testInvalidAnniversaryBuild4() {

        Anniversary anniversary = Anniversary.builder()
                .kind(AnniversaryKind.wedding())
                .date(AnniversaryDate.builder().partialDate(PartialDate.builder().day(10).build()).build())
                .build();

        Card jsCard = Card.builder()
                .uid(getUUID())
                .anniversaries(new HashMap<String, Anniversary>() {{ put("AN-1", anniversary); }})
                .build();

        assertFalse("testInvalidAnniversaryBuild4-1", jsCard.isValid());
        assertTrue("testValidAnniversaryBuild4-2", jsCard.getValidationMessage().contains("if day is set, month must be set"));
        assertTrue("testValidAnniversaryBuild4-3", jsCard.getValidationMessage().contains("at least one not null member between year and month is required in PartialDate"));
    }

}
