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
import it.cnr.iit.jscontact.tools.dto.annotations.JSContactCollection;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the Name type as defined in section 2.2.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","components","locale"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name extends AbstractJSContactType implements Serializable {

    @NotNull
    @Pattern(regexp = "Name", message="invalid @type value in Name")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Name";

    @JSContactCollection(addMethod = "addComponent")
    @NotNull(message = "components is missing in Name")
    @NonNull
    @Valid
    NameComponent[] components;

    String locale;

    /**
     * Adds a name component to this object.
     *
     * @param nc the name component
     * @param components the name components
     * @return the name components in input plus the nc component
     */
    public static NameComponent[] addComponent(NameComponent[] components, NameComponent nc) {
        return ArrayUtils.add(components, nc);
    }
}
