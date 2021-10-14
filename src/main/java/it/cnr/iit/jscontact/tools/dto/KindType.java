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
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class KindType extends ExtensibleEnum<KindEnum> implements Serializable {

    private boolean isRfc(KindEnum value) { return isRfcValue() && rfcValue == value; }

    /**
     * Tests if this kind of contact card is "individual". See vCard KIND property [RFC6350].
     * @return true if this kind of contact card is "individual"
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isIndividual() { return isRfc(KindEnum.INDIVIDUAL); }

    /**
     * Tests if this kind of contact card is "group". See vCard KIND property [RFC6350].
     * @return true if this kind of contact card is "group"
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isGroup() { return isRfc(KindEnum.GROUP); }

    /**
     * Tests if this kind of contact card is "org". See vCard KIND property [RFC6350].
     * @return true if this kind of contact card is "org"
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isOrg() { return isRfc(KindEnum.ORG); }

    /**
     * Tests if this kind of contact card is "device". See [RFC6869].
     * @return true if this kind of contact card is "device"
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6869">RFC6869</a>
     */
    @JsonIgnore
    public boolean isDevice() { return isRfc(KindEnum.DEVICE); }

    /**
     * Tests if this kind of contact card is "application". See [RFC6473].
     * @return true if this kind of contact card is "application"
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6473">RFC6473</a>
     */
    @JsonIgnore
    public boolean isApplication() { return isRfc(KindEnum.APPLICATION); }

    /**
     * Tests if this kind of contact card is "location". See vCard KIND property [RFC6350].
     * @return true if this kind of contact card is "location"
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isLocation() { return isRfc(KindEnum.LOCATION); }

    /**
     * Tests if this is a custom kind of contact card.
     * @return true if this is a custom kind of contact card
     */
    @JsonIgnore
    public boolean isExt() { return isExtValue(); }

    private static KindType rfc(KindEnum rfcValue) { return KindType.builder().rfcValue(rfcValue).build(); }

    /**
     * Creates an "individual" kind of contact card. See vCard KIND property [RFC6350].
     * @return an object representing an "individual" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    public static KindType individual() { return rfc(KindEnum.INDIVIDUAL);}

    /**
     * Creates a "group" kind of contact card. See vCard KIND property [RFC6350].
     * @return an object  representing a "group" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    public static KindType group() { return rfc(KindEnum.GROUP);}

    /**
     * Creates an "org" kind of contact card. See vCard KIND property [RFC6350].
     * @return an object representing a "org" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    public static KindType org() { return rfc(KindEnum.ORG);}

    /**
     * Creates a "device" kind of contact card. See [RFC6869].
     * @return an object representing a "device" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6869">RFC6869</a>
     */
    public static KindType device() { return rfc(KindEnum.DEVICE);}

    /**
     * Creates a "location" kind of contact card. See vCard KIND property [RFC6350].
     * @return an object representing a "location" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">Section 6.1.4 of RFC6350</a>
     */
    public static KindType location() { return rfc(KindEnum.LOCATION);}

    /**
     * Creates an "application" kind of contact card. See [RFC6473].
     * @return an object representing an "application" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6473">RFC6473</a>
     */
    public static KindType application() { return rfc(KindEnum.APPLICATION);}

    /**
     * Creates a custom kind of contact card.
     * @return an object representing a custom kind of contact card
     */
    private static KindType ext(String extValue) { return KindType.builder().extValue(extValue).build(); }
}
