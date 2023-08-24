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
 * Class for configuring the conversion from a vCard 4.0 [RFC6350] and its transliterations to a JSContact object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 * @author Mario Loffredo
 */
@Data
@Builder
@AllArgsConstructor
public class VCard2JSContactConfig {

    @Builder.Default
    private String customTimeZonesPrefix = "tz";
    @Builder.Default
    private boolean validateCard = true;
    @Builder.Default
    private boolean useAutoIdsProfile = true;
    @Builder.Default
    private boolean usePropIds = true;
    @Builder.Default
    private boolean setAutoFullAddress = true;
    @Builder.Default
    private boolean useVoiceAsDefaultPhoneFeature = true;
    @Builder.Default
    private boolean setAutoMediaType = true;
    @Builder.Default
    private boolean convertGenderToSpeakToAs = true; //ignored if GRAMGENDER is present

    private String defaultLanguage;

    private VCard2JSContactIdsProfile idsProfileToUse;

}
