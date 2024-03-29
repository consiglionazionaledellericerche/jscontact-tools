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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpeakToAsTest extends AbstractTest {

    @Test
    public void testValidSpeakToAs1() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .speakToAs(SpeakToAs.builder()
                                    .grammaticalGender(GrammaticalGenderType.MASCULINE)
                                    .build())
                .build();

        assertTrue("testValidSpeakToAs1", jsCard.isValid());
    }

    @Test
    public void testValidSpeakToAs2() {
        Map<String,Pronouns> pronouns = new HashMap<String,Pronouns>() {{ put("PRONOUNS-1", Pronouns.builder()
                .pronouns("he/him")
                .build());
        }};

        Card jsCard = Card.builder()
                .uid(getUUID())
                .speakToAs(SpeakToAs.builder()
                                    .pronouns(pronouns)
                                    .build())
                .build();

        assertTrue("testValidSpeakToAs2", jsCard.isValid());
    }

    @Test
    public void testValidSpeakToAs3() {

        Map<String,Pronouns> pronouns = new HashMap<String,Pronouns>() {{ put("PRONOUNS-1", Pronouns.builder()
                .pronouns("he/him")
                .build());
        }};

        Card jsCard = Card.builder()
                .uid(getUUID())
                .speakToAs(SpeakToAs.builder()
                                    .grammaticalGender(GrammaticalGenderType.MASCULINE)
                                    .pronouns(pronouns)
                                    .build())
                .build();

        assertTrue("testValidSpeakToAs3", jsCard.isValid());
    }

    @Test
    public void testInvalidSpeakToAs1() {

        Card jsCard = Card.builder()
                .uid(getUUID())
                .speakToAs(SpeakToAs.builder().build())
                .build();

        assertFalse("testInvalidSpeakToAs1-1", jsCard.isValid());
        List<String> messages = Arrays.asList(jsCard.getValidationMessage().split("\n"));
        assertTrue("testInvalidSpeakToAs1-2", messages.contains("at least one not null member between grammaticalGender and pronouns is required in SpeakToAs"));
    }

}
