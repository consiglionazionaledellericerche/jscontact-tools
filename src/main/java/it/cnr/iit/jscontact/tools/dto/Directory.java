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
import it.cnr.iit.jscontact.tools.dto.deserializers.DirectoryKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;


/**
 * Class mapping the Directory type as defined in section 2.6.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type", "uri", "kind", "mediaType", "contexts", "pref", "listAs", "label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Directory extends Resource implements HasKind {

    @Pattern(regexp = "Directory", message = "invalid @type value in Directory")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Directory";

    @JsonDeserialize(using = DirectoryKindDeserializer.class)
    DirectoryKind kind;

    @Min(value = 1, message = "invalid listAs in Directory - value must be greater or equal than 1")
    Integer listAs;

    @JsonIgnore
    private boolean isDirectoryResource(DirectoryKind type) {
        return this.kind.equals(type);
    }

    /**
     * Tests if this directory resource is a directory.
     *
     * @return true if this directory resource is a directory, false otherwise
     */
    @JsonIgnore
    public boolean isDirectory() { return isDirectoryResource(DirectoryKind.directory()); }

    /**
     * Tests if this directory resource is an entry.
     *
     * @return true if this directory resource is an entry, false otherwise
     */
    @JsonIgnore
    public boolean isEntry() { return isDirectoryResource(DirectoryKind.entry()); }

    private static Directory resource(DirectoryKind type, String uri) {
        return Directory.builder()
                       .uri(uri)
                       .kind(type)
                       .build();
    }

    /**
     * Returns a directory
     *
     * @param uri directory uri
     * @return the directory
     */
    public static Directory directory(String uri) { return resource(DirectoryKind.directory(), uri);}

    /**
     * Returns an entry
     *
     * @param uri entry uri
     * @return the entry
     */
    public static Directory entry(String uri) { return resource(DirectoryKind.entry(), uri);}

}
