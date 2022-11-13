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
package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.ExtensibleEnumType;

/**
 * This interface imposes that a class implementing it must include the "type" property and type is extensible.
 *
 * @author Mario Loffredo
 */
public interface HasType {

    /**
     * Returns the value of the "type" property.
     *
     * @return the value of the "type" property
     */
    <T extends ExtensibleEnumType> T getType();
}
