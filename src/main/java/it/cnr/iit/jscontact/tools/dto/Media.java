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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.MediaResourceTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.Pattern;


/**
 * Class mapping the Media type as defined in section 2.6.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","uri","kind","mediaType","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Media extends Resource implements HasKind {

    @Pattern(regexp = "Media", message="invalid @type value in Media")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Media";

    @JsonDeserialize(using = MediaResourceTypeDeserializer.class)
    MediaResourceKind kind;

    @JsonIgnore
    private boolean isMediaResource(MediaResourceKind type) { return this.kind.equals(type); }

    /**
     * Tests if this media resource is a photo.
     *
     * @return true if this media resource is a photo, false otherwise
     */
    @JsonIgnore
    public boolean isPhoto() { return isMediaResource(MediaResourceKind.photo()); }

    /**
     * Tests if this media resource is a sound.
     *
     * @return true if this media resource is a sound, false otherwise
     */
    @JsonIgnore
    public boolean isSound() { return isMediaResource(MediaResourceKind.sound()); }

    /**
     * Tests if this media resource is a logo.
     *
     * @return true if this media resource is a logo, false otherwise
     */
    @JsonIgnore
    public boolean isLogo() { return isMediaResource(MediaResourceKind.logo()); }

    private static Media resource(MediaResourceKind type, String uri) {
        return Media.builder()
                       .uri(uri)
                       .kind(type)
                       .build();
    }

    /**
     * Returns a photo
     *
     * @param uri photo uri
     * @return the photo
     */
    public static Media photo(String uri) { return resource(MediaResourceKind.photo(), uri);}

    /**
     * Returns a sound
     *
     * @param uri sound uri
     * @return the sound
     */
    public static Media sound(String uri) { return resource(MediaResourceKind.sound(), uri);}

    /**
     * Returns a logo
     *
     * @param uri logo uri
     * @return the logo
     */
    public static Media logo(String uri) { return resource(MediaResourceKind.logo(), uri);}

}
