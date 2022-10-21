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

/**
 * Class mapping the "kind" property as defined in section 2.1.6 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.1.6">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class KindType extends ExtensibleEnum<KindEnum> implements Serializable {

    /**
     * Tests if this kind of contact card is "individual". See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return true if this kind of contact card is "individual", false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    @JsonIgnore
    public boolean isIndividual() { return isRfc(KindEnum.INDIVIDUAL); }

    /**
     * Tests if this kind of contact card is "group". See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return true if this kind of contact card is "group", false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    @JsonIgnore
    public boolean isGroup() { return isRfc(KindEnum.GROUP); }

    /**
     * Tests if this kind of contact card is "org". See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return true if this kind of contact card is "org", false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    @JsonIgnore
    public boolean isOrg() { return isRfc(KindEnum.ORG); }

    /**
     * Tests if this kind of contact card is "device". See vCard KIND property as defined in section 6.1.4 of [RFC6869].
     *
     * @return true if this kind of contact card is "device", false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6869">RFC6869</a>
     */
    @JsonIgnore
    public boolean isDevice() { return isRfc(KindEnum.DEVICE); }

    /**
     * Tests if this kind of contact card is "application". See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6473].
     *
     * @return true if this kind of contact card is "application", false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6473">RFC6473</a>
     */
    @JsonIgnore
    public boolean isApplication() { return isRfc(KindEnum.APPLICATION); }

    /**
     * Tests if this kind of contact card is "location". See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return true if this kind of contact card is "location", false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    @JsonIgnore
    public boolean isLocation() { return isRfc(KindEnum.LOCATION); }


    private static KindType rfc(KindEnum rfcValue) { return KindType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns an "individual" kind of contact card. See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return an "individual" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    public static KindType individual() { return rfc(KindEnum.INDIVIDUAL);}

    /**
     * Returns a "group" kind of contact card. See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return an object  representing a "group" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    public static KindType group() { return rfc(KindEnum.GROUP);}

    /**
     * Returns an "org" kind of contact card. See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return "org" as kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    public static KindType org() { return rfc(KindEnum.ORG);}

    /**
     * Returns a "device" kind of contact card. See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6869].
     *
     * @return a "device" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6869">RFC6869</a>
     */
    public static KindType device() { return rfc(KindEnum.DEVICE);}

    /**
     * Returns a "location" kind of contact card. See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6350].
     *
     * @return a "location" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.4">RFC6350</a>
     */
    public static KindType location() { return rfc(KindEnum.LOCATION);}

    /**
     * Returns an "application" kind of contact card. See vCard 4.0 KIND property as defined in section 6.1.4 of [RFC6473].
     *
     * @return an "application" kind of contact card
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6473">RFC6473</a>
     */
    public static KindType application() { return rfc(KindEnum.APPLICATION);}

    /**
     * Returns a custom kind of contact card.
     *
     * @return a custom kind of contact card
     */
    private static KindType ext(String extValue) { return KindType.builder().extValue(extValue).build(); }
}
