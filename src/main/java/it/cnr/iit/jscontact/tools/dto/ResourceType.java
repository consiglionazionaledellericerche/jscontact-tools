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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

/**
 * Enum class mapping the values of the "type" property of the Resource type as defined in section 2.3.4 of [draft-ietf-calext-jscontact].
 * The values are those corresponding to vCard 4.0 [RFC6350] [RFC6715] [RFC8605] properties that are not directly mapped to a JSContact property.
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.4">draft-ietf-calext-jscontact</a>
 * @see <a href="https://datatracker.ietf.org/doc/rfc6350">RFC6350</a>
 * @see <a href="https://datatracker.ietf.org/doc/rfc6715">RFC6715</a>
 * @see <a href="https://datatracker.ietf.org/doc/rfc8605">RFC8605</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum ResourceType {

    URI("uri"),
    SOURCE("directorySource"),
    LOGO("logo"),
    CONTACT_URI("contact"),
    ORG_DIRECTORY("directory"),
    SOUND("audio"),
    KEY("publicKey"),
    FBURL("freeBusy"),
    CALURI("calendar");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ResourceType getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(ResourceType.class, value);
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonIgnore
    public String getMapTag() {
        return this.name().replace("_", "-");
    }

}

