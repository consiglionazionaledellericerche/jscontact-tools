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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.annotations.JSContactCollection;
import it.cnr.iit.jscontact.tools.dto.deserializers.NameSortAsDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.NameSortAsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the Name type as defined in section 2.2.2 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.2">draft-ietf-calext-jscontact</a>
 */
@JsonPropertyOrder({"@type", "components", "sortAs", "label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Name extends AbstractJSContactType implements Serializable {

    @Pattern(regexp = "Name", message="invalid @type value in Name")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Name";

    @JSContactCollection(addMethod = "addComponent", itemClass = NameComponent.class)
    @NotEmpty(message = "components is missing or empty in Name")
    @NonNull
    @Valid
    NameComponent[] components;

    String defaultSeparator;

    @JsonSerialize(using = NameSortAsSerializer.class)
    @JsonDeserialize(using = NameSortAsDeserializer.class)
    Map<NameComponentKind, String> sortAs;

    /**
     * Adds a name component to this object.
     *
     * @param nc         the name component
     * @param components the name components
     * @return the name components in input plus the nc component
     */
    public static NameComponent[] addComponent(NameComponent[] components, NameComponent nc) {
        return ArrayUtils.add(components, nc);
    }

    /**
     * Adds a name component to this object.
     *
     * @param nc the name component
     */
    public void addComponent(NameComponent nc) {
        components = ArrayUtils.add(components, nc);
    }


}
