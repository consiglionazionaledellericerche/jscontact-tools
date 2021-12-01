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
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.utils.HasIndexUtils;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasIndex;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the PersonalInformation type as defined in section 2.6.2 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.6.2">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","type","label","value","level"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInformation extends GroupableObject implements HasIndex, IdMapValue, Comparable<PersonalInformation>, Serializable {

    @NotNull
    @Pattern(regexp = "PersonalInformation", message="invalid @type value in PersonalInformation")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "PersonalInformation";

    PersonalInformationType type;

    String label;

    @NotNull(message = "value is missing in PersonalInformation")
    @NonNull
    String value;

    PersonalInformationLevel level;

    @JsonIgnore
    Integer index;

    /**
     * Compares this personal information with another based on the value of the "index" property.
     *
     * @param o the object this object must be compared with
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the given object.
     */
    @Override
    public int compareTo(PersonalInformation o) {

        return HasIndexUtils.compareTo(this, o);
    }

    /**
     * Tests if this personal information is an hobby.
     *
     * @return true if this personal information is an hobby, false otherwise
     */
    public boolean asHobby() { return type == PersonalInformationType.HOBBY; }
    /**
     * Tests if this personal information is an interest.
     *
     * @return true if this personal information is an interest, false otherwise
     */
    public boolean asInterest() { return type == PersonalInformationType.INTEREST; }
    /**
     * Tests if this personal information is an expertise.
     *
     * @return true if this personal information is an expertise, false otherwise
     */
    public boolean asExpertise() { return type == PersonalInformationType.EXPERTISE; }
    /**
     * Tests if this personal information is other than the known types.
     *
     * @return true if this personal information is other than the known types, false otherwise
     */
    public boolean asOtherPersonalInfo() { return type == null; }
    /**
     * Tests if the level of this personal information is high.
     *
     * @return true if the level of this personal information is high, false otherwise
     */
    public boolean ofHighLevel() { return level == PersonalInformationLevel.HIGH; }
    /**
     * Tests if the level of this personal information is medium.
     *
     * @return true if the level of this personal information is medium, false otherwise
     */
    public boolean ofMediumLevel() { return level == PersonalInformationLevel.MEDIUM; }
    /**
     * Tests if the level of this personal information is low.
     *
     * @return true if the level of this personal information is low, false otherwise
     */
    public boolean ofLowLevel() { return level == PersonalInformationLevel.LOW; }

}
