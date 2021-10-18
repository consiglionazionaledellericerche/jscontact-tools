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

import it.cnr.iit.jscontact.tools.dto.interfaces.HasPreference;

/**
 * Utility class for handling objects implementing the HasPreference interface.
 *
 * @author Mario Loffredo
 */
public class HasPreferenceUtils {

    public static int compareTo(HasPreference o1, HasPreference o2) {

        if (o1.getPreference() == null) {
            if (o2.getPreference() == null)
                return 0;
            else
                return 1;
        } else {
            if (o2.getPreference() == null)
                return -1;
        }

        return o1.getPreference().compareTo(o2.getPreference());
    }

}
