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
 * Class mapping the values of the "level" property of the PersonalInformation type as defined in section 2.8.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PersonalInformationLevelType extends ExtensibleEnumType<PersonalInformationLevelEnum> implements Serializable {

    /**
     * Tests if this personal information level is "high".
     *
     * @return true if this personal information level is "high", false otherwise
     */
    @JsonIgnore
    public boolean isHigh() { return isRfc(PersonalInformationLevelEnum.HIGH); }

    /**
     * Tests if this personal information level is "medium".
     *
     * @return true if this personal information level is "medium", false otherwise
     */
    @JsonIgnore
    public boolean isMedium() { return isRfc(PersonalInformationLevelEnum.MEDIUM); }

    /**
     * Tests if this personal information level is "low".
     *
     * @return true if this personal information level is "low", false otherwise
     */
    @JsonIgnore
    public boolean isLow() { return isRfc(PersonalInformationLevelEnum.LOW); }

    private static PersonalInformationLevelType rfc(PersonalInformationLevelEnum rfcValue) { return PersonalInformationLevelType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "high" personal information level.
     *
     * @return a "high" personal information level
     */
    public static PersonalInformationLevelType high() { return rfc(PersonalInformationLevelEnum.HIGH);}

    /**
     * Returns a "medium" personal information level.
     *
     * @return a "medium" personal information level
     */
    public static PersonalInformationLevelType medium() { return rfc(PersonalInformationLevelEnum.MEDIUM);}

    /**
     * Returns a "low" personal information level.
     *
     * @return a "low" personal information level
     */
    public static PersonalInformationLevelType low() { return rfc(PersonalInformationLevelEnum.LOW);}

    /**
     * Returns a custom personal information level.
     *
     * @param extValue the custom personal information level in text format
     * @return a custom personal information level
     */
    public static PersonalInformationLevelType ext(String extValue) { return PersonalInformationLevelType.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}
