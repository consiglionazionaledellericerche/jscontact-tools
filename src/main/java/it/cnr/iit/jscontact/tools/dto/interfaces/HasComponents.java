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
package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.PhoneticSystem;

/**
 * This interface imposes that a class implementing it must include components with the "phonetic" property.
 *
 * @author Mario Loffredo
 */
public interface HasComponents {

    /**
     * Returns the value of the "phoneticSystem" property.
     *
     * @return the value of the "phoneticSystem" property
     */
    PhoneticSystem getPhoneticSystem();

    /**
     * Returns the value of the "phoneticScript" property.
     *
     * @return the value of the "phoneticScript" property
     */
    String getPhoneticScript();

    /**
     * Returns the value of the "components" property.
     *
     * @return the value of the "components" property
     */
    HasPhonetic[] getComponents();


    /**
     * Returns the value of the "isOrdered" property.
     *
     * @return the value of the "isOrdered" property
     */
    Boolean getIsOrdered();

    /**
     * Returns the value of the "defaultSeparator" property.
     *
     * @return the value of the "defaultSeparator" property
     */
    String getDefaultSeparator();


}
