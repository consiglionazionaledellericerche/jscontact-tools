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
 * Class mapping the values of the "type" property of the PersonalInfo type as defined in section 2.8.4 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.4">draft-ietf-calext-jscontact</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PersonalInfoType extends ExtensibleEnumType<PersonalInfoEnum> implements Serializable {

    /**
     * Tests if this personal information type is "hobby".
     *
     * @return true if this personal information type is "hobby", false otherwise
     */
    @JsonIgnore
    public boolean isHobby() {
        return isRfc(PersonalInfoEnum.HOBBY);
    }

    /**
     * Tests if this personal information type is "interest".
     *
     * @return true if this personal information type is "interest", false otherwise
     */
    @JsonIgnore
    public boolean isInterest() {
        return isRfc(PersonalInfoEnum.INTEREST);
    }

    /**
     * Tests if this personal information type is "expertise".
     *
     * @return true if this personal information type is "expertise", false otherwise
     */
    @JsonIgnore
    public boolean isExpertise() {
        return isRfc(PersonalInfoEnum.EXPERTISE);
    }

    private static PersonalInfoType rfc(PersonalInfoEnum rfcValue) {
        return PersonalInfoType.builder().rfcValue(rfcValue).build();
    }

    /**
     * Returns a "hobby" personal information type.
     *
     * @return a "hobby" personal information type
     */
    public static PersonalInfoType hobby() {
        return rfc(PersonalInfoEnum.HOBBY);
    }

    /**
     * Returns an "interest" personal information type.
     *
     * @return an "interest" personal information type
     */
    public static PersonalInfoType interest() {
        return rfc(PersonalInfoEnum.INTEREST);
    }

    /**
     * Returns a "expertise" personal information type.
     *
     * @return a "expertise" personal information type
     */
    public static PersonalInfoType expertise() {
        return rfc(PersonalInfoEnum.EXPERTISE);
    }

    /**
     * Returns a custom personal information type.
     *
     * @param extValue the custom personal information type in text format
     * @return a custom personal information type
     */
    public static PersonalInfoType ext(String extValue) {
        return PersonalInfoType.builder().extValue(V_Extension.toV_Extension(extValue)).build();
    }
}