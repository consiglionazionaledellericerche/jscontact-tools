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
 * Class mapping the values of the "kind" property of the StreetComponent type as defined in section 2.5.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.5.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class StreetComponentKind extends ExtensibleEnumType<StreetComponentEnum> implements Serializable {

    /**
     * Returns a street component type whose enum value is pre-defined.
     *
     * @param rfcValue a pre-defined street component type
     * @return a street component type
     */
    public static StreetComponentKind rfc(StreetComponentEnum rfcValue) { return StreetComponentKind.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "name" street component type.
     *
     * @return a "name" street component type
     */
    public static StreetComponentKind name() { return rfc(StreetComponentEnum.NAME);}
    /**
     * Returns a "number" street component type.
     *
     * @return a "number" street component type
     */
    public static StreetComponentKind number() { return rfc(StreetComponentEnum.NUMBER);}
    /**
     * Returns a "direction" street component type.
     *
     * @return a "direction" street component type
     */
    public static StreetComponentKind direction() { return rfc(StreetComponentEnum.DIRECTION);}
    /**
     * Returns a "building" street component type.
     *
     * @return a "building" street component type
     */
    public static StreetComponentKind building() { return rfc(StreetComponentEnum.BUILDING);}
    /**
     * Returns a "floor" street component type.
     *
     * @return a "floor" street component type
     */
    public static StreetComponentKind floor() { return rfc(StreetComponentEnum.FLOOR);}
    /**
     * Returns an "apartment" street component type.
     *
     * @return an "apartment" street component type
     */
    public static StreetComponentKind apartment() { return rfc(StreetComponentEnum.APARTMENT);}
    /**
     * Returns a "room" street component type.
     *
     * @return a "room" street component type
     */
    public static StreetComponentKind room() { return rfc(StreetComponentEnum.ROOM);}
    /**
     * Returns an "extension" street component type.
     *
     * @return an "extension" street component type
     */
    public static StreetComponentKind extension() { return rfc(StreetComponentEnum.EXTENSION);}
    /**
     * Returns a "postOfficeBox" street component type.
     *
     * @return a "postOfficeBox" street component type
     */
    public static StreetComponentKind postOfficeBox() { return rfc(StreetComponentEnum.POST_OFFICE_BOX);}
    /**
     * Returns a "separator" street component type.
     *
     * @return a "separator" street component type
     */
    public static StreetComponentKind separator() { return rfc(StreetComponentEnum.SEPARATOR);}
    /**
     * Returns an "unknown" street component type.
     *
     * @return an "unknown" street component type
     */
    public static StreetComponentKind unknown() { return rfc(StreetComponentEnum.UNKNOWN);}

}
