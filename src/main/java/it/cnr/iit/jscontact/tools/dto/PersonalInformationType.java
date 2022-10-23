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
 * Class mapping the values of the "type" property of the PersonalInformation type as defined in section 2.8.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PersonalInformationType extends ExtensibleEnumType<PersonalInformationEnum> implements Serializable {

    /**
     * Tests if this personal information type is "hobby".
     *
     * @return true if this personal information type is "hobby", false otherwise
     */
    @JsonIgnore
    public boolean isHobby() { return isRfc(PersonalInformationEnum.HOBBY); }

    /**
     * Tests if this personal information type is "interest".
     *
     * @return true if this personal information type is "interest", false otherwise
     */
    @JsonIgnore
    public boolean isInterest() { return isRfc(PersonalInformationEnum.INTEREST); }

    /**
     * Tests if this personal information type is "expertise".
     *
     * @return true if this personal information type is "expertise", false otherwise
     */
    @JsonIgnore
    public boolean isExpertise() { return isRfc(PersonalInformationEnum.EXPERTISE); }

    private static PersonalInformationType rfc(PersonalInformationEnum rfcValue) { return PersonalInformationType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "hobby" personal information type.
     *
     * @return a "hobby" personal information type
     */
    public static PersonalInformationType hobby() { return rfc(PersonalInformationEnum.HOBBY);}

    /**
     * Returns a "interest" personal information type.
     *
     * @return a "interest" personal information type
     */
    public static PersonalInformationType interest() { return rfc(PersonalInformationEnum.INTEREST);}

    /**
     * Returns a "expertise" personal information type.
     *
     * @return a "expertise" personal information type
     */
    public static PersonalInformationType expertise() { return rfc(PersonalInformationEnum.EXPERTISE);}

    /**
     * Returns a custom personal information type.
     *
     * @return a custom personal information type
     */
    public static PersonalInformationType ext(String extValue) { return PersonalInformationType.builder().extValue(extValue).build(); }
}
