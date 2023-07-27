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
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

/**
 * Enum class mapping the values of the "kind" property of the AddressComponent type as defined in section 2.5.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.5.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum AddressComponentEnum implements IsExtensibleEnum {

    LOCALITY("locality"),

    REGION("region"),

    COUNTRY("country"),

    POSTCODE("postcode"),

    DISTRICT("district"),
    SUBDISTRICT("subdistrict"),
    BLOCK("block"),

    NAME("name"),
    NUMBER("number"),
    DIRECTION("direction"),
    BUILDING("building"),
    FLOOR("floor"),
    APARTMENT("apartment"),
    ROOM("room"),
    LANDMARK("landmark"),

    POST_OFFICE_BOX("postOfficeBox"),
    SEPARATOR("separator");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AddressComponentEnum getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(AddressComponentEnum.class, value);
    }

    @Override
    public String toString() {
        return value;
    }

}
