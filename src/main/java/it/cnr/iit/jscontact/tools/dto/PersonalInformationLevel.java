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

import java.util.HashMap;
import java.util.Map;

/**
 * Enum class mapping the values of the PersonalInformation "level" member as defined in section 2.6.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum PersonalInformationLevel {

    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    private final String value;

    @JsonIgnore
    private static final Map<String, PersonalInformationLevel> aliases = new HashMap<String, PersonalInformationLevel>()
    {{
       put("beginner", LOW);
       put("average", MEDIUM);
       put("expert", HIGH);
    }};

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonIgnore
    public static String getVCardExpertiseLevel(PersonalInformationLevel level) {

        for (String key : aliases.keySet())
            if (aliases.get(key).equals(level))
                return key;

        return null;
    }

    @JsonCreator
    public static PersonalInformationLevel getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(PersonalInformationLevel.class, value, aliases);
    }

    @Override
    public String toString() {
        return value;
    }

}

