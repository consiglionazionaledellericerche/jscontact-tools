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

import java.util.Collection;
import java.util.Map;

/**
 * Utility class for handling generic enums.
 *
 * @author Mario Loffredo
 */

public class EnumUtils {

    /**
     * Returns the value of an enum class matching the given text value.
     * @param enumType the enum class
     * @param value the text value
     * @return the enum value matching the text value.
     */
    public static <E extends Enum <E>> E getEnum(Class<E> enumType, String value) {

        return getEnum(enumType, value, null);
    }

    /**
     * Returns the value of an enum class matching the given text value.
     * The matching is done by considering possible aliases.
     * @param enumType the enum class
     * @param value the text value
     * @return the enum value matching the text value
     */
    public static <E extends Enum <E>> E getEnum(Class<E> enumType, String value, Map<String,E> aliases) {

        if (aliases!=null && aliases.containsKey(value))
            return aliases.get(value);

        for (E e : java.util.EnumSet.allOf(enumType)) {
            if (e.toString().equals(value))
                return e;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the value of vCard [RFC6350] TYPE parameter matching the given context value.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-5.6">Section 5.6 of RFC6350</a>
     * @param context the context value
     * @return the value of vCard TYPE parameter
     */
    public static <E extends Enum <E>> String toVCardType(E context) {

        if (context == null)
            return null;

        if (context.toString().equals("work"))
            return "work";
        else if (context.toString().equals("private"))
            return "home";
        else
            return null;
    }

    /**
     * Returns an array of strings matching the given enum values.
     * @param enumValues a collection of enum values
     * @return the array of strings
     */
    public static <E extends Enum <E>> String[] toStrings(Collection<E> enumValues) {

        if (enumValues == null)
            return null;

        String[] array = new String[enumValues.size()];
        int i = 0;
        for (E item : enumValues)
            array[i++] = item.toString();

        return array;
    }

}
