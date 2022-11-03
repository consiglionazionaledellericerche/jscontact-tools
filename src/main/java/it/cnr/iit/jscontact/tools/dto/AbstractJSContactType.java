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

/**
 * Abstract class mapping the vCard 4.0 related properties that can be grouped as defined in section 3.3 of [RFC6350].
 *
 * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-3.3">RFC6350</a>
 * @author Mario Loffredo
 */
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractJSContactType extends AbstractExtensibleJSContactType {

    @JsonIgnore
    @Getter
    @Setter
    String group;

    @JsonIgnore
    @Getter
    @Setter
    String propId;

}
