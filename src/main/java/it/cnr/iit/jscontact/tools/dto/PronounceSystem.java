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
 * Class mapping the pronounce system values as defined in section 1.5.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-1.5.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PronounceSystem extends ExtensibleEnumType<PronounceSystemEnum> implements Serializable {

    /**
     * Tests if the pronounce system is "ipa".
     *
     * @return true if the pronounce system is "ipa", false otherwise
     */
    @JsonIgnore
    public boolean isIpa() { return isRfc(PronounceSystemEnum.IPA); }

    /**
     * Tests if the pronounce system is "piny".
     *
     * @return true if the pronounce system is "piny", false otherwise
     */
    @JsonIgnore
    public boolean isPiny() { return isRfc(PronounceSystemEnum.PINY); }

    /**
     * Tests if the pronounce system is "jyut".
     *
     * @return true if the pronounce system is "jyut", false otherwise
     */
    @JsonIgnore
    public boolean isJyut() { return isRfc(PronounceSystemEnum.JYUT); }

    /**
     * Returns a pronounce system whose enum value is pre-defined.
     *
     * @param rfcValue the pre-defined pronounce system
     * @return a pre-defined pronounce system
     */
    public static PronounceSystem rfc(PronounceSystemEnum rfcValue) { return PronounceSystem.builder().rfcValue(rfcValue).build();}

    /**
     * Returns a "ipa" pronounce system.
     *
     * @return a "ipa" pronounce system
     */
    public static PronounceSystem ipa() { return rfc(PronounceSystemEnum.IPA);}

    /**
     * Returns a "piny" pronounce system.
     *
     * @return a "piny" pronounce system
     */
    public static PronounceSystem piny() { return rfc(PronounceSystemEnum.PINY);}

    /**
     * Returns a "jyut" pronounce system.
     *
     * @return a "jyut" pronounce system
     */
    public static PronounceSystem jyut() { return rfc(PronounceSystemEnum.JYUT);}

    /**
     * Returns a custom relation type.
     *
     * @param extValue the custom relation type
     * @return a custom relation type
     */
    public static PronounceSystem ext(String extValue) { return PronounceSystem.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }

}
