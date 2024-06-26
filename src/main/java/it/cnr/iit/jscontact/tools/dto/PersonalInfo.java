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
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.PersonalInfoLevelDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.PersonalInfoKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the PersonalInfo type as defined in section 2.8.4 of [RFC9553].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.4">Section 2.8.4 of RFC9553</a>
 */
@JsonPropertyOrder({"@type", "kind", "value", "level", "listAs", "label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PersonalInfo extends AbstractJSContactType implements HasLabel, HasKind, IdMapValue, IsIANAType, Serializable {

    @Pattern(regexp = "PersonalInfo", message = "invalid @type value in PersonalInfo")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "PersonalInfo";

    @JsonDeserialize(using = PersonalInfoKindDeserializer.class)
    @ContainsExtensibleEnum(enumClass = PersonalInfoEnum.class, getMethod = "getKind")
    PersonalInfoKind kind;

    @NotNull(message = "value is missing in PersonalInfo")
    @NonNull
    String value;

    @JsonDeserialize(using = PersonalInfoLevelDeserializer.class)
    @ContainsExtensibleEnum(enumClass = PersonalInfoLevelEnum.class, getMethod = "getLevel")
    PersonalInfoLevelType level;

    @Min(value = 1, message = "invalid listAs in PersonalInfo - value must be greater or equal than 1")
    Integer listAs;

    String label;

    /**
     * Tests if this personal information is a hobby.
     *
     * @return true if this personal information is a hobby, false otherwise
     */
    public boolean asHobby() {
        return kind.isHobby();
    }

    /**
     * Tests if this personal information is an interest.
     *
     * @return true if this personal information is an interest, false otherwise
     */
    public boolean asInterest() { return kind.isInterest(); }
    /**
     * Tests if this personal information is an expertise.
     *
     * @return true if this personal information is an expertise, false otherwise
     */
    public boolean asExpertise() { return kind.isExpertise(); }
    /**
     * Tests if this personal information is other than the known types.
     *
     * @return true if this personal information is other than the known types, false otherwise
     */
    public boolean asOtherPersonalInfo() { return kind == null; }
    /**
     * Tests if the level of this personal information is high.
     *
     * @return true if the level of this personal information is high, false otherwise
     */
    public boolean ofHighLevel() { return level.isHigh(); }
    /**
     * Tests if the level of this personal information is medium.
     *
     * @return true if the level of this personal information is medium, false otherwise
     */
    public boolean ofMediumLevel() { return level.isMedium(); }
    /**
     * Tests if the level of this personal information is low.
     *
     * @return true if the level of this personal information is low, false otherwise
     */
    public boolean ofLowLevel() { return level.isLow(); }
    /**
     * Tests if this address is used in a custom context.
     *
     * @return true if the personal information level is equal to the given personal information level, false otherwise
     */
    public boolean ofOtherLevel() { return level.isExtValue(); }

}
