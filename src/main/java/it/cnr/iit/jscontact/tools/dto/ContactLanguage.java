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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNullAnyConstraint(fieldNames={"context","pref"}, message = "at least one not null member is missing in ContactLanguage")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactLanguage extends GroupableObject implements Serializable {

    @NotNull
    @Pattern(regexp = "ContactLanguage", message="Invalid @type value in ContactLanguage")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "ContactLanguage";

    @JsonSerialize(using = ContextSerializer.class)
    @JsonDeserialize(using = ContextDeserializer.class)
    Context context;

    @Min(value=1, message = "invalid pref in ContactLanguage - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in ContactLanguage - value must be less or equal than 100")
    Integer pref;

}
