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
import it.cnr.iit.jscontact.tools.dto.deserializers.LinkKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasOptionalKind;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.Pattern;


/**
 * Class mapping the Link type as defined in section 2.6.3 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.3">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","uri","kind","mediaType","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Link extends Resource implements HasKind, HasOptionalKind {

    @Pattern(regexp = "Link", message="invalid @type value in Link")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Link";

    @JsonDeserialize(using = LinkKindDeserializer.class)
    LinkKind kind;

    @JsonIgnore
    private boolean isLinkResource(LinkKind type) { return this.kind.equals(type); }

    /**
     * Tests if this directory resource is a contact link.
     *
     * @return true if this directory resource is a contact link, false otherwise
     */
    @JsonIgnore
    public boolean isContact() { return isLinkResource(LinkKind.contact()); }

    /**
     * Tests if this directory resource is a generic link.
     *
     * @return true if this directory resource is a generic link, false otherwise
     */
    @JsonIgnore
    public boolean isGenericLink() { return kind == null; }

    private static Link resource(LinkKind type, String uri) {
        return Link.builder()
                       .uri(uri)
                       .kind(type)
                       .build();
    }

    /**
     * Returns a contact link
     *
     * @param uri contact link uri
     * @return the contact link
     */
    public static Link contact(String uri) { return resource(LinkKind.contact(), uri);}

    /**
     * Returns an unspecified link
     *
     * @param uri link uri
     * @return the link
     */
    public static Link genericLink(String uri) { return resource(null, uri);}

}
