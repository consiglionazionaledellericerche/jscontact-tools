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
package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Class mapping the values of the "kind" property of the Media type as defined in section 2.6.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class MediaKind extends ExtensibleEnumType<MediaEnum> implements Serializable {

    /**
     * Tests if this media resource type is "photo".
     *
     * @return true if media resource type is "photo", false otherwise
     */
    @JsonIgnore
    public boolean isPhoto() { return isRfc(MediaEnum.PHOTO); }

    /**
     * Tests if this media resource type is "sound".
     *
     * @return true if media resource type is "sound", false otherwise
     */
    @JsonIgnore
    public boolean isSound() { return isRfc(MediaEnum.SOUND); }

    /**
     * Tests if this media resource type is "logo".
     *
     * @return true if media resource type is "logo", false otherwise
     */
    @JsonIgnore
    public boolean isLogo() { return isRfc(MediaEnum.LOGO); }

    private static MediaKind rfc(MediaEnum rfcValue) { return MediaKind.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "photo" media resource type.
     *
     * @return a "photo" media resource type
     */
    public static MediaKind photo() { return rfc(MediaEnum.PHOTO);}

    /**
     * Returns a "sound" media resource type.
     *
     * @return a "sound" media resource type
     */
    public static MediaKind sound() { return rfc(MediaEnum.SOUND);}

    /**
     * Returns a "logo" media resource type.
     *
     * @return a "logo" media resource type
     */
    public static MediaKind logo() { return rfc(MediaEnum.LOGO);}

    /**
     * Returns a custom media resource type.
     *
     * @return a custom media resource type
     */
    private static MediaKind ext(String extValue) { return MediaKind.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}
