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
 * Class mapping the phoneticSystem values as defined in section 1.5.4 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-1.5.4">Section 1.5.4 of RFC9553</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PhoneticSystem extends ExtensibleEnumType<PhoneticSystemEnum> implements Serializable {

    /**
     * Tests if the phonetic system is "ipa".
     *
     * @return true if the phonetic system is "ipa", false otherwise
     */
    @JsonIgnore
    public boolean isIpa() { return isRfc(PhoneticSystemEnum.IPA); }

    /**
     * Tests if the phonetic system is "piny".
     *
     * @return true if the phonetic system is "piny", false otherwise
     */
    @JsonIgnore
    public boolean isPiny() { return isRfc(PhoneticSystemEnum.PINY); }

    /**
     * Tests if the phonetic system is "jyut".
     *
     * @return true if the phonetic system is "jyut", false otherwise
     */
    @JsonIgnore
    public boolean isJyut() { return isRfc(PhoneticSystemEnum.JYUT); }

    /**
     * Returns a phonetic system whose enum value is pre-defined.
     *
     * @param rfcValue the pre-defined phonetic system
     * @return a pre-defined phonetic system
     */
    public static PhoneticSystem rfc(PhoneticSystemEnum rfcValue) { return PhoneticSystem.builder().rfcValue(rfcValue).build();}

    /**
     * Returns an "ipa" phonetic system.
     *
     * @return an "ipa" phonetic system
     */
    public static PhoneticSystem ipa() { return rfc(PhoneticSystemEnum.IPA);}

    /**
     * Returns a "piny" phonetic system.
     *
     * @return a "piny" phonetic system
     */
    public static PhoneticSystem piny() { return rfc(PhoneticSystemEnum.PINY);}

    /**
     * Returns a "jyut" phonetic system.
     *
     * @return a "jyut" phonetic system
     */
    public static PhoneticSystem jyut() { return rfc(PhoneticSystemEnum.JYUT);}
    /**
     * Returns a custom phonetic system.
     *
     * @param extValue the custom phonetic system
     * @return a custom phonetic system
     */
    public static PhoneticSystem ext(String extValue) { return PhoneticSystem.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }

}
