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
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPreference;
import it.cnr.iit.jscontact.tools.dto.utils.HasPreferenceUtils;
import lombok.*;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameComponent extends GroupableObject implements HasPreference, Comparable<NameComponent> {

    @NotNull(message = "value is missing in NameComponent")
    @NonNull
    String value;

    @NotNull(message = "type is missing in NameComponent")
    @NonNull
    NameComponentType type;

    @JsonIgnore
    Integer preference;

    //to compare VCard NICKNAME instances based on preference
    @Override
    public int compareTo(NameComponent o) {

        return HasPreferenceUtils.compareTo(this, o);
    }

}
