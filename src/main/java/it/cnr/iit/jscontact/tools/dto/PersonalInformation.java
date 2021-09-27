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
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.utils.HasIndexUtils;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasIndex;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInformation extends GroupableObject implements HasIndex, IdMapValue, Comparable<PersonalInformation>, Serializable {

    @NotNull
    @Pattern(regexp = "PersonalInformation", message="Invalid @type value in PersonalInformation")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "PersonalInformation";

    @NotNull(message = "type is missing in PersonalInformation")
    @NonNull
    PersonalInformationType type;

    @NotNull(message = "value is missing in PersonalInformation")
    @NonNull
    String value;

    PersonalInformationLevel level;

    @JsonIgnore
    Integer index;

    //to compare VCard HOBBY, INTEREST, EXPERTIZE instances based on index
    @Override
    public int compareTo(PersonalInformation o) {

        return HasIndexUtils.compareTo(this, o);
    }

    public boolean asHobby() { return type == PersonalInformationType.HOBBY; }
    public boolean asInterest() { return type == PersonalInformationType.INTEREST; }
    public boolean asExpertise() { return type == PersonalInformationType.EXPERTISE; }
    public boolean asOtherPersonalInfo() { return type == PersonalInformationType.OTHER; }
    public boolean ofHighInterest() { return level == PersonalInformationLevel.HIGH; }
    public boolean ofMediumInterest() { return level == PersonalInformationLevel.MEDIUM; }
    public boolean ofLowInterest() { return level == PersonalInformationLevel.LOW; }

}
