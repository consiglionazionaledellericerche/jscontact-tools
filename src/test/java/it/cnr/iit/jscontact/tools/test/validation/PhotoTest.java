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

import it.cnr.iit.jscontact.tools.dto.MediaResource;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.MediaResourceKind;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class PhotoTest extends AbstractTest {

    @Test(expected = NullPointerException.class)
    public void testInvalidPhotoBuild1() {

        // href missing
        MediaResource.builder().build();
    }


    @Test(expected = NullPointerException.class)
    public void testInvalidPhotoBuild2() {

        // href missing
        MediaResource.builder().pref(1).build();
    }

    @Test
    public void testValidPhoto1() {

        MediaResource photo = MediaResource.builder()
                .uri("http://www.media.com/aphoto.png")
                .pref(1)
                .kind(MediaResourceKind.photo())
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .media(new HashMap<String, MediaResource>() {{ put("PHOTO-1", photo); }})
                .build();

        assertTrue("testValidPhoto1", jsCard.isValid());
    }

    @Test
    public void testInvalidPhoto1() {

        // invalid pref - lower than min value
        MediaResource photo = MediaResource.builder()
                         .uri("http://www.media.com/aphoto.png")
                         .pref(0)
                         .kind(MediaResourceKind.photo())
                         .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .media(new HashMap<String, MediaResource>() {{ put("PHOTO-1", photo); }})
                .build();

        assertFalse("testInvalidPhoto1-1", jsCard.isValid());
        assertEquals("testInvalidPhoto1-2", "invalid pref in Resource - value must be greater or equal than 1", jsCard.getValidationMessage());
    }


    @Test
    public void testInvalidPhoto2() {

        // invalid pref - lower than min value
        MediaResource photo = MediaResource.builder()
                .uri("http://www.media.com/aphoto.png")
                .kind(MediaResourceKind.photo())
                .pref(101)
                .build();
        Card jsCard = Card.builder()
                .uid(getUUID())
                .media(new HashMap<String, MediaResource>() {{ put("PHOTO-1", photo); }})
                .build();

        assertFalse("testInvalidPhoto2-1", jsCard.isValid());
        assertEquals("testInvalidPhoto2-2", "invalid pref in Resource - value must be less or equal than 100", jsCard.getValidationMessage());
    }

}
