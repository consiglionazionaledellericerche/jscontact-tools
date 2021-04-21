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
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameComponent extends GroupableObject implements Serializable {

    @NotNull(message = "value is missing in NameComponent")
    @NonNull
    String value;

    @NotNull(message = "type is missing in NameComponent")
    @NonNull
    NameComponentType type;

    @JsonIgnore
    public boolean isPrefix() { return type == NameComponentType.PREFIX; }
    @JsonIgnore
    public boolean isPersonal() { return type == NameComponentType.PERSONAL; }
    @JsonIgnore
    public boolean isSurname() { return type == NameComponentType.SURNAME; }
    @JsonIgnore
    public boolean isAdditional() { return type == NameComponentType.ADDITIONAL; }
    @JsonIgnore
    public boolean isSuffix() { return type == NameComponentType.SUFFIX; }

}
