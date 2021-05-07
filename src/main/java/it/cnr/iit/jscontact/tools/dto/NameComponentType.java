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

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class NameComponentType extends ExtensibleType<NameComponentEnum> implements Serializable {


    public static NameComponentType rfc(NameComponentEnum rfcValue) { return NameComponentType.builder().rfcValue(rfcValue).build();}
    public static NameComponentType prefix() { return rfc(NameComponentEnum.PREFIX);}
    public static NameComponentType personal() { return rfc(NameComponentEnum.PERSONAL);}
    public static NameComponentType surname() { return rfc(NameComponentEnum.SURNAME);}
    public static NameComponentType additional() { return rfc(NameComponentEnum.ADDITIONAL);}
    public static NameComponentType separator() { return rfc(NameComponentEnum.SEPARATOR);}
    public static NameComponentType suffix() { return rfc(NameComponentEnum.SUFFIX);}
}
