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
 * Class mapping the values of the "kind" property of the NameComponent type as defined in section 2.2.1 of [RFC9553].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.1">RFC9553</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class NameComponentKind extends ExtensibleEnumType<NameComponentEnum> implements Serializable {

    /**
     * Returns a name component type whose enum value is pre-defined.
     *
     * @param rfcValue a pre-defined name component type
     * @return a name component type
     */
    public static NameComponentKind rfc(NameComponentEnum rfcValue) { return NameComponentKind.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "title" name component type.
     *
     * @return a "title" name component type
     */
    public static NameComponentKind title() { return rfc(NameComponentEnum.TITLE);}
    /**
     * Returns a "given" name component type.
     *
     * @return a "given" name component type
     */
    public static NameComponentKind given() { return rfc(NameComponentEnum.GIVEN);}
    /**
     * Returns a "surname" name component type.
     *
     * @return a "surname" name component type
     */
    public static NameComponentKind surname() { return rfc(NameComponentEnum.SURNAME);}
    /**
     * Returns a "surname2" name component type.
     *
     * @return a "surname2" name component type
     */
    public static NameComponentKind surname2() { return rfc(NameComponentEnum.SURNAME2);}
    /**
     * Returns a "given2" name component type.
     *
     * @return a "given2" name component type
     */
    public static NameComponentKind given2() { return rfc(NameComponentEnum.GIVEN2);}
    /**
     * Returns a "separator" name component type.
     *
     * @return a "separator" name component type
     */
    public static NameComponentKind separator() { return rfc(NameComponentEnum.SEPARATOR);}
    /**
     * Returns a "credential" name component type.
     *
     * @return a "credential" name component type
     */
    public static NameComponentKind credential() { return rfc(NameComponentEnum.CREDENTIAL);}
    /**
     * Returns a "generation" name component type.
     *
     * @return a "generation" name component type
     */
    public static NameComponentKind generation() { return rfc(NameComponentEnum.GENERATION);}

}
