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
public enum Context implements JCardTypeDerivedEnum {

    PRIVATE("private"),
    WORK("work"),
    OTHER("other");

    private String value;

    @Getter
    @JsonIgnore
    private static final Map<String, Context> aliases = new HashMap<String, Context>()
    {{
        put("home", PRIVATE);
    }};


    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Context getEnum(String value) throws IllegalArgumentException {
        return EnumUtils.getEnum(Context.class, value, aliases);
    }

    @Override
    public String toString() {
        return value;
    }


    @JsonIgnore
    public static String getVCardType(String context) {

        try {
            Context rc = Context.getEnum(context);
            return getVCardType(rc);
        }
        catch(Exception e) {
            return context;
        }

    }

    @JsonIgnore
    public static String getVCardType(Context context) {

        if (context == null)
            return null;

        for (String key : aliases.keySet())
            if (aliases.get(key).equals(context))
                return key;

        return context.getValue();
    }

}

