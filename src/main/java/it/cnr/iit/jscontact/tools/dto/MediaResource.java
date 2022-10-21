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
import it.cnr.iit.jscontact.tools.constraints.ResourceConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.MediaResourceTypeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * Class mapping the MediaResource type as defined in section 2.6.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@ResourceConstraint
@JsonPropertyOrder({"@type","uri","type","mediaType","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResource extends Resource {

    @NotNull
    @Pattern(regexp = "MediaResource", message="invalid @type value in MediaResource")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "MediaResource";

    @JsonDeserialize(using = MediaResourceTypeDeserializer.class)
    MediaResourceType type;

    @JsonIgnore
    private boolean isMediaResource(MediaResourceType type) { return this.type == type; }

    /**
     * Tests if this media resource is a photo.
     *
     * @return true if this media resource is a photo, false otherwise
     */
    @JsonIgnore
    public boolean isPhoto() { return isMediaResource(MediaResourceType.photo()); }

    /**
     * Tests if this media resource is a sound.
     *
     * @return true if this media resource is a sound, false otherwise
     */
    @JsonIgnore
    public boolean isSound() { return isMediaResource(MediaResourceType.sound()); }

    /**
     * Tests if this media resource is a logo.
     *
     * @return true if this media resource is a logo, false otherwise
     */
    @JsonIgnore
    public boolean isLogo() { return isMediaResource(MediaResourceType.logo()); }

    private static MediaResource resource(MediaResourceType type, String uri) {
        return MediaResource.builder()
                       .uri(uri)
                       .type(type)
                       .build();
    }

    /**
     * Returns a photo
     *
     * @param uri photo uri
     * @return the photo
     */
    public static MediaResource photo(String uri) { return resource(MediaResourceType.photo(), uri);}

    /**
     * Returns a sound
     *
     * @param uri sound uri
     * @return the sound
     */
    public static MediaResource sound(String uri) { return resource(MediaResourceType.sound(), uri);}

    /**
     * Returns a logo
     *
     * @param uri logo uri
     * @return the logo
     */
    public static MediaResource logo(String uri) { return resource(MediaResourceType.logo(), uri);}

}
