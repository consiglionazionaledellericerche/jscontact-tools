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
package it.cnr.iit.jscontact.tools.dto.wrappers;

import it.cnr.iit.jscontact.tools.dto.interfaces.HasPreference;
import it.cnr.iit.jscontact.tools.dto.utils.HasPreferenceUtils;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Wrapper class for vCard CATEGORIES property as defined in section 6.7.1 of [RFC6350].
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.7.1">RFC6350</a>
 * @author Mario Loffredo
 */
@Data
@Builder
public class CategoryWrapper implements HasPreference, Comparable<CategoryWrapper> {

    List<String> values;

    Integer preference;

    /**
     * Compares this object with another based on the value of the "preference" property.
     *
     * @param o the object this object must be compared with
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the given object.
     */
    @Override
    public int compareTo(CategoryWrapper o) {
        return HasPreferenceUtils.compareTo(this, o);
    }

}
