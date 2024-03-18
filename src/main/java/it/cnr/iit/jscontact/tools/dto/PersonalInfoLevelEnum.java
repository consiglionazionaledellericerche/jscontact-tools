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
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum class mapping the values of the PersonalInfo "level" member as defined in section 2.8.4 of [RFC9553].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.4">RFC9553</a>
 */
@AllArgsConstructor
public enum PersonalInfoLevelEnum implements IsExtensibleEnum {

    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    private final String value;

    @JsonIgnore
    private static final Map<String, PersonalInfoLevelEnum> aliases = new HashMap<String, PersonalInfoLevelEnum>() {{
        put("beginner", LOW);
        put("average", MEDIUM);
        put("expert", HIGH);
    }};

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonIgnore
    public static String getVCardExpertiseLevel(PersonalInfoLevelEnum level) {

        for (String key : aliases.keySet())
            if (aliases.get(key).equals(level))
                return key;

        return null;
    }

    @JsonCreator
    public static PersonalInfoLevelEnum getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(PersonalInfoLevelEnum.class, value, aliases);
    }

    @Override
    public String toString() {
        return value;
    }

}

