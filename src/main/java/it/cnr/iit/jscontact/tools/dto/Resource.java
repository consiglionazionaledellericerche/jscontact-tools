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
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasIndex;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.utils.HasIndexUtils;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends GroupableObject implements HasIndex, Comparable<Resource>, IdMapValue {

    Context context;

    String type;

    @BooleanMapConstraint(message = "invalid Map<String,Boolean> in Resource - Boolean.FALSE is not allowed")
    Map<String,Boolean> labels;

    @NotNull(message = "value is missing in Resource")
    @NonNull
    String value;

    String mediaType;

    @Min(value=1, message = "invalid pref in Resource - min value must be 1")
    @Max(value=100, message = "invalid pref in Resource - max value must be 100")
    Integer pref;

    @JsonIgnore
    Integer index;

    //to compare VCard ORG-DIRECTORY instances based on index
    @Override
    public int compareTo(Resource o) {

        return HasIndexUtils.compareTo(this, o);
    }


}
