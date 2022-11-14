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
import it.cnr.iit.jscontact.tools.dto.deserializers.LinkResourceTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * Class mapping the LinkResource type as defined in section 2.6.3 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.3">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@ResourceConstraint
@JsonPropertyOrder({"@type","uri","type","mediaType","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkResource extends Resource implements HasType {

    @NotNull
    @Pattern(regexp = "LinkResource", message="invalid @type value in LinkResource")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "LinkResource";

    @JsonDeserialize(using = LinkResourceTypeDeserializer.class)
    LinkResourceType type;

    @JsonIgnore
    private boolean isLinkResource(LinkResourceType type) { return this.type.equals(type); }

    /**
     * Tests if this directory resource is a contact link.
     *
     * @return true if this directory resource is a contact link, false otherwise
     */
    @JsonIgnore
    public boolean isContact() { return isLinkResource(LinkResourceType.contact()); }

    /**
     * Tests if this directory resource is a generic link.
     *
     * @return true if this directory resource is a generic link, false otherwise
     */
    @JsonIgnore
    public boolean isGenericLink() { return type == null; }

    private static LinkResource resource(LinkResourceType type, String uri) {
        return LinkResource.builder()
                       .uri(uri)
                       .type(type)
                       .build();
    }

    /**
     * Returns a contact link
     *
     * @param uri contact link uri
     * @return the contact link
     */
    public static LinkResource contact(String uri) { return resource(LinkResourceType.contact(), uri);}

    /**
     * Returns an unspecified link
     *
     * @param uri link uri
     * @return the link
     */
    public static LinkResource genericLink(String uri) { return resource(null, uri);}

}
