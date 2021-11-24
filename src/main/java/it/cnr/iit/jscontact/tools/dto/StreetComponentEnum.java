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
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensible;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

/**
 * Enum class mapping the values of the "type" property of the StreetComponent type as defined in section 2.4.1 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.4.1">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum StreetComponentEnum implements IsExtensible {

    NAME("name"),
    NUMBER("number"),
    DIRECTION("direction"),
    BUILDING("building"),
    FLOOR("floor"),
    APARTMENT("apartment"),
    ROOM("room"),
    EXTENSION("extension"),
    POST_OFFICE_BOX("postOfficeBox"),
    SEPARATOR("separator"),
    UNKNOWN("unknown");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StreetComponentEnum getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(StreetComponentEnum.class, value);
    }

    @Override
    public String toString() {
        return value;
    }

}

