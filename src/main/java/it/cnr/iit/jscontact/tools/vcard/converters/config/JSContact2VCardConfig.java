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
package it.cnr.iit.jscontact.tools.vcard.converters.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Class for configuring the conversion from a JSContact object to a vCard 4.0 [RFC6350] and its transliterations.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 * @author Mario Loffredo
 */
@Data
@Builder
@AllArgsConstructor
public class JSContact2VCardConfig {

    @Builder.Default
    private String extensionsPrefix = "extension:";
    @Builder.Default
    private boolean cardToValidate = true;
    @Builder.Default
    private boolean applyAutoAddrLabel = true;
    @Builder.Default
    private boolean addPropIdParameter = true;
    @Builder.Default
    private boolean convertTimezoneToTZParam = true;
    @Builder.Default
    private boolean convertCoordinatesToGEOParam = true;
    @Builder.Default
    private boolean convertTimezoneToOffset = false;

}
