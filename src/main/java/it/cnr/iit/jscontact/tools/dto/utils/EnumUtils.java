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
package it.cnr.iit.jscontact.tools.dto.utils;

import java.util.Map;

public class EnumUtils {

    public static <E extends Enum <E>> E getEnum(Class<E> enumType, String value) {

        return getEnum(enumType, value, null);
    }

    public static <E extends Enum <E>> E getEnum(Class<E> enumType, String value, Map<String,E> aliases) {

        if (aliases!=null && aliases.containsKey(value))
            return aliases.get(value);

        for (E e : java.util.EnumSet.allOf(enumType)) {
            if (e.toString().equals(value))
                return e;
        }
        throw new IllegalArgumentException();
    }

}
