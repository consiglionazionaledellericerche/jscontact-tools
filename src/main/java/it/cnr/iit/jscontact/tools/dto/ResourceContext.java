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
import it.cnr.iit.jscontact.tools.dto.interfaces.JCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum ResourceContext implements JCardTypeDerivedEnum {

    PRIVATE("private"),
    WORK("work"),
    OTHER("other");

    private String value;

    @Getter
    @JsonIgnore
    private static final Map<String, ResourceContext> aliases = new HashMap<String, ResourceContext>()
    {{
        put("home", PRIVATE);
    }};


    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ResourceContext getEnum(String value) throws IllegalArgumentException {
        return EnumUtils.getEnum(ResourceContext.class, value, aliases);
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonIgnore
    public static String getVCardType(ResourceContext context) {

        for (String key : aliases.keySet())
            if (aliases.get(key).equals(context))
                return key;

        return context.getValue();
    }

}

