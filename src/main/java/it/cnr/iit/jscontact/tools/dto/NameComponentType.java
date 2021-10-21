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
 * Class mapping the values of the "type" property of the NameComponent type as defined in section 2.2.1 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.2.1">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class NameComponentType extends ExtensibleEnum<NameComponentEnum> implements Serializable {

    /**
     * Creates a name component type.
     *
     * @return an object representing a name component type
     */
    public static NameComponentType rfc(NameComponentEnum rfcValue) { return NameComponentType.builder().rfcValue(rfcValue).build();}
    /**
     * Creates a "prefix" name component type.
     *
     * @return an object representing a "prefix" name component type
     */
    public static NameComponentType prefix() { return rfc(NameComponentEnum.PREFIX);}
    /**
     * Creates a "personal" name component type.
     *
     * @return an object representing a "personal" name component type
     */
    public static NameComponentType personal() { return rfc(NameComponentEnum.PERSONAL);}
    /**
     * Creates a "surname" name component type.
     *
     * @return an object representing a "surname" name component type
     */
    public static NameComponentType surname() { return rfc(NameComponentEnum.SURNAME);}
    /**
     * Creates an "additional" name component type.
     *
     * @return an object representing an "additional" name component type
     */
    public static NameComponentType additional() { return rfc(NameComponentEnum.ADDITIONAL);}
    /**
     * Creates a "separator" name component type.
     *
     * @return an object representing a "separator" name component type
     */
    public static NameComponentType separator() { return rfc(NameComponentEnum.SEPARATOR);}
    /**
     * Creates a "suffix" name component type.
     *
     * @return an object representing a "suffix" name component type
     */
    public static NameComponentType suffix() { return rfc(NameComponentEnum.SUFFIX);}
}
