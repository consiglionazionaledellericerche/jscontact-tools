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
import it.cnr.iit.jscontact.tools.dto.utils.VersionUtils;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class CardTest extends AbstractTest {

    @Test(expected = NullPointerException.class)
    public void testInvalidCardBuildPerVersion_1_0() {

        // Version 1.0 is the default version
        //uid missing
        VersionUtils.setDefaultVersion(VersionUtils.VersionEnum.VERSION_1_0);
        Card.builder()
               .kind(KindType.individual())
               .buildPerVersionAndProfile();
        VersionUtils.setDefaultVersion(VersionUtils.VersionEnum.VERSION_2_0);
    }


    public void testValidCardBuildPerVersion_2_0() {

        VersionUtils.setDefaultVersion(VersionUtils.VersionEnum.VERSION_2_0);

        //uid missing
        Card card = Card.builder()
                .kind(KindType.individual())
                .buildPerVersionAndProfile();

        assertNotNull("testValidCardBuildPerVersion_2_0", card);
    }

}
