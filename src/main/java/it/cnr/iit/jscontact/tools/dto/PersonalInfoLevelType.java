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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Class mapping the values of the "level" property of the PersonalInfo type as defined in section 2.8.4 of [RFC9553].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.4">Section 2.8.4 of RFC9553</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PersonalInfoLevelType extends ExtensibleEnumType<PersonalInfoLevelEnum> implements Serializable {

    /**
     * Tests if this personal information level is "high".
     *
     * @return true if this personal information level is "high", false otherwise
     */
    @JsonIgnore
    public boolean isHigh() {
        return isRfc(PersonalInfoLevelEnum.HIGH);
    }

    /**
     * Tests if this personal information level is "medium".
     *
     * @return true if this personal information level is "medium", false otherwise
     */
    @JsonIgnore
    public boolean isMedium() {
        return isRfc(PersonalInfoLevelEnum.MEDIUM);
    }

    /**
     * Tests if this personal information level is "low".
     *
     * @return true if this personal information level is "low", false otherwise
     */
    @JsonIgnore
    public boolean isLow() {
        return isRfc(PersonalInfoLevelEnum.LOW);
    }

    private static PersonalInfoLevelType rfc(PersonalInfoLevelEnum rfcValue) {
        return PersonalInfoLevelType.builder().rfcValue(rfcValue).build();
    }

    /**
     * Returns a "high" personal information level.
     *
     * @return a "high" personal information level
     */
    public static PersonalInfoLevelType high() {
        return rfc(PersonalInfoLevelEnum.HIGH);
    }

    /**
     * Returns a "medium" personal information level.
     *
     * @return a "medium" personal information level
     */
    public static PersonalInfoLevelType medium() {
        return rfc(PersonalInfoLevelEnum.MEDIUM);
    }

    /**
     * Returns a "low" personal information level.
     *
     * @return a "low" personal information level
     */
    public static PersonalInfoLevelType low() {
        return rfc(PersonalInfoLevelEnum.LOW);
    }

    /**
     * Returns a custom personal information level.
     *
     * @param extValue the custom personal information level in text format
     * @return a custom personal information level
     */
    public static PersonalInfoLevelType ext(String extValue) {
        return PersonalInfoLevelType.builder().extValue(V_Extension.toV_Extension(extValue)).build();
    }
}
