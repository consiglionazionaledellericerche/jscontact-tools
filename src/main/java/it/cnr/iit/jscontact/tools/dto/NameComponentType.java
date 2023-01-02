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
 * Class mapping the values of the "type" property of the NameComponent type as defined in section 2.2.2 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.2">draft-ietf-calext-jscontact</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class NameComponentType extends ExtensibleEnumType<NameComponentEnum> implements Serializable {

    /**
     * Returns a name component type whose enum value is pre-defined.
     *
     * @param rfcValue a pre-defined name component type
     * @return a name component type
     */
    public static NameComponentType rfc(NameComponentEnum rfcValue) { return NameComponentType.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "prefix" name component type.
     *
     * @return a "prefix" name component type
     */
    public static NameComponentType prefix() { return rfc(NameComponentEnum.PREFIX);}
    /**
     * Returns a "given" name component type.
     *
     * @return a "given" name component type
     */
    public static NameComponentType given() { return rfc(NameComponentEnum.GIVEN);}
    /**
     * Returns a "surname" name component type.
     *
     * @return a "surname" name component type
     */
    public static NameComponentType surname() { return rfc(NameComponentEnum.SURNAME);}
    /**
     * Returns a "middle" name component type.
     *
     * @return a "middle" name component type
     */
    public static NameComponentType middle() { return rfc(NameComponentEnum.MIDDLE);}
    /**
     * Returns a "separator" name component type.
     *
     * @return a "separator" name component type
     */
    public static NameComponentType separator() { return rfc(NameComponentEnum.SEPARATOR);}
    /**
     * Returns a "suffix" name component type.
     *
     * @return a "suffix" name component type
     */
    public static NameComponentType suffix() { return rfc(NameComponentEnum.SUFFIX);}
}
