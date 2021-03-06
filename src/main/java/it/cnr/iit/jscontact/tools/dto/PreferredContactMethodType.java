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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PreferredContactMethodType {

    EMAILS("emails"),
    PHONES("phones"),
    ONLINE("online");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PreferredContactMethodType getEnum(String value) throws IllegalArgumentException {

        for(PreferredContactMethodType en : PreferredContactMethodType.values()) {
            if (en.getValue().equals(value))
                return en;
        }

        throw new IllegalArgumentException();
    }

}

