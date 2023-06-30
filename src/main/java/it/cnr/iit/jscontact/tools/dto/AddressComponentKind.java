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
 * Class mapping the values of the "kind" property of the AddressComponent type as defined in section 2.5.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.5.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddressComponentKind extends ExtensibleEnumType<AddressComponentEnum> implements Serializable {

    /**
     * Returns a street component type whose enum value is pre-defined.
     *
     * @param rfcValue a pre-defined street component type
     * @return a street component type
     */
    public static AddressComponentKind rfc(AddressComponentEnum rfcValue) { return AddressComponentKind.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "district" street component type.
     *
     * @return a "district" street component type
     */
    public static AddressComponentKind district() { return rfc(AddressComponentEnum.DISTRICT);}
    /**
     * Returns a "block" street component type.
     *
     * @return a "block" street component type
     */
    public static AddressComponentKind block() { return rfc(AddressComponentEnum.BLOCK);}
    /**
     * Returns a "name" street component type.
     *
     * @return a "name" street component type
     */
    public static AddressComponentKind name() { return rfc(AddressComponentEnum.NAME);}
    /**
     * Returns a "number" street component type.
     *
     * @return a "number" street component type
     */
    public static AddressComponentKind number() { return rfc(AddressComponentEnum.NUMBER);}
    /**
     * Returns a "direction" street component type.
     *
     * @return a "direction" street component type
     */
    public static AddressComponentKind direction() { return rfc(AddressComponentEnum.DIRECTION);}
    /**
     * Returns a "building" street component type.
     *
     * @return a "building" street component type
     */
    public static AddressComponentKind building() { return rfc(AddressComponentEnum.BUILDING);}
    /**
     * Returns a "floor" street component type.
     *
     * @return a "floor" street component type
     */
    public static AddressComponentKind floor() { return rfc(AddressComponentEnum.FLOOR);}
    /**
     * Returns an "apartment" street component type.
     *
     * @return an "apartment" street component type
     */
    public static AddressComponentKind apartment() { return rfc(AddressComponentEnum.APARTMENT);}
    /**
     * Returns a "room" street component type.
     *
     * @return a "room" street component type
     */
    public static AddressComponentKind room() { return rfc(AddressComponentEnum.ROOM);}
    /**
     * Returns an "landmark" street component type.
     *
     * @return an "landmark" street component type
     */
    public static AddressComponentKind landmark() { return rfc(AddressComponentEnum.LANDMARK);}
    /**
     * Returns an "extension" street component type.
     *
     * @return an "extension" street component type
     */
    public static AddressComponentKind extension() { return rfc(AddressComponentEnum.EXTENSION);}
    /**
     * Returns a "postOfficeBox" street component type.
     *
     * @return a "postOfficeBox" street component type
     */
    public static AddressComponentKind postOfficeBox() { return rfc(AddressComponentEnum.POST_OFFICE_BOX);}
    /**
     * Returns a "separator" street component type.
     *
     * @return a "separator" street component type
     */
    public static AddressComponentKind separator() { return rfc(AddressComponentEnum.SEPARATOR);}

}
