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

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Class mapping the values of the "kind" property of the AddressComponent type as defined in section 2.5.1 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.5.1">RFC9553</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddressComponentKind extends ExtensibleEnumType<AddressComponentEnum> implements Serializable {

    /**
     * Returns a address component type whose enum value is pre-defined.
     *
     * @param rfcValue a pre-defined address component type
     * @return a address component type
     */
    public static AddressComponentKind rfc(AddressComponentEnum rfcValue) { return AddressComponentKind.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "locality" address component type.
     *
     * @return a "locality" address component type
     */
    public static AddressComponentKind locality() { return rfc(AddressComponentEnum.LOCALITY);}
    /**
     * Returns a "region" address component type.
     *
     * @return a "region" address component type
     */
    public static AddressComponentKind region() { return rfc(AddressComponentEnum.REGION);}
    /**
     * Returns a "country" address component type.
     *
     * @return a "country" address component type
     */
    public static AddressComponentKind country() { return rfc(AddressComponentEnum.COUNTRY);}
    /**
     * Returns a "postcode" address component type.
     *
     * @return a "postcode" address component type
     */
    public static AddressComponentKind postcode() { return rfc(AddressComponentEnum.POSTCODE);}

    /**
     * Returns a "district" address component type.
     *
     * @return a "district" address component type
     */
    public static AddressComponentKind district() { return rfc(AddressComponentEnum.DISTRICT);}
    /**
     * Returns a "subdistrict" address component type.
     *
     * @return a "subdistrict" address component type
     */
    public static AddressComponentKind subdistrict() { return rfc(AddressComponentEnum.SUBDISTRICT);}
    /**
     * Returns a "block" address component type.
     *
     * @return a "block" address component type
     */
    public static AddressComponentKind block() { return rfc(AddressComponentEnum.BLOCK);}
    /**
     * Returns a "name" address component type.
     *
     * @return a "name" address component type
     */
    public static AddressComponentKind name() { return rfc(AddressComponentEnum.NAME);}
    /**
     * Returns a "number" address component type.
     *
     * @return a "number" address component type
     */
    public static AddressComponentKind number() { return rfc(AddressComponentEnum.NUMBER);}
    /**
     * Returns a "direction" address component type.
     *
     * @return a "direction" address component type
     */
    public static AddressComponentKind direction() { return rfc(AddressComponentEnum.DIRECTION);}
    /**
     * Returns a "building" address component type.
     *
     * @return a "building" address component type
     */
    public static AddressComponentKind building() { return rfc(AddressComponentEnum.BUILDING);}
    /**
     * Returns a "floor" address component type.
     *
     * @return a "floor" address component type
     */
    public static AddressComponentKind floor() { return rfc(AddressComponentEnum.FLOOR);}
    /**
     * Returns an "apartment" address component type.
     *
     * @return an "apartment" address component type
     */
    public static AddressComponentKind apartment() { return rfc(AddressComponentEnum.APARTMENT);}
    /**
     * Returns a "room" address component type.
     *
     * @return a "room" address component type
     */
    public static AddressComponentKind room() { return rfc(AddressComponentEnum.ROOM);}
    /**
     * Returns an "landmark" address component type.
     *
     * @return an "landmark" address component type
     */
    public static AddressComponentKind landmark() { return rfc(AddressComponentEnum.LANDMARK);}
    /**
     * Returns a "postOfficeBox" address component type.
     *
     * @return a "postOfficeBox" address component type
     */
    public static AddressComponentKind postOfficeBox() { return rfc(AddressComponentEnum.POST_OFFICE_BOX);}
    /**
     * Returns a "separator" address component type.
     *
     * @return a "separator" address component type
     */
    public static AddressComponentKind separator() { return rfc(AddressComponentEnum.SEPARATOR);}

}
