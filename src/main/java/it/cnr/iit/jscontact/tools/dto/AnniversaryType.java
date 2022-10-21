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
 * Class mapping the values of the "type" property of the Anniversary type as defined in section 2.8.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AnniversaryType extends ExtensibleEnum<AnniversaryEnum> implements Serializable {

    /**
     * Tests if this anniverary type is "birth".
     *
     * @return true if this anniverary type is "birth", false otherwise
     */
    @JsonIgnore
    public boolean isBirth() { return isRfc(AnniversaryEnum.BIRTH); }

    /**
     * Tests if this anniverary type is "death".
     *
     * @return true if this anniverary type is "death", false otherwise
     */
    @JsonIgnore
    public boolean isDeath() { return isRfc(AnniversaryEnum.DEATH); }


    private static AnniversaryType rfc(AnniversaryEnum rfcValue) { return AnniversaryType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "birth" anniversary type.
     *
     * @return a "birth" anniversary type
     */
    public static AnniversaryType birth() { return rfc(AnniversaryEnum.BIRTH);}

    /**
     * Returns a "death" anniversary type.
     *
     * @return a "death" anniversary type
     */
    public static AnniversaryType death() { return rfc(AnniversaryEnum.DEATH);}

    /**
     * Returns a custom anniverary type.
     *
     * @return a custom anniverary type
     */
    private static AnniversaryType ext(String extValue) { return AnniversaryType.builder().extValue(extValue).build(); }
}
