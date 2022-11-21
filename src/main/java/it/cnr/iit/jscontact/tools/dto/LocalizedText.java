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

import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPreference;
import it.cnr.iit.jscontact.tools.dto.utils.HasPreferenceUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class supporting the conversion of language-dependent alternative properties in a vCard 4.0 [RFC6350] and its transliterations.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-5.1">RFC6350</a>
 * @author Mario Loffredo
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"value"}, callSuper = false)
public class LocalizedText extends AbstractJSContactType implements HasLabel, HasAltid, HasPreference, Comparable<LocalizedText>, Serializable {

    @NotNull(message = "value is missing in LocalizedText")
    @NonNull
    String value;

    String language;

    Map<String,String> localizations;

    String altid;

    Integer preference;

    String label;

    Map<Context,Boolean> contexts;

    String[] sortAs;

    /**
     * Adds a localization to the "localizations" map.
     *
     * @param language the localization language
     * @param value the localization value
     */
    public void addLocalization(String language, String value) {

        if (localizations == null)
            localizations = new HashMap<>();

        localizations.put(StringUtils.defaultIfEmpty(language, "en" ), value);
    }

    /**
     * Compares this localized string with another based on the value of the "preference" property.
     *
     * @param o the object this object must be compared with
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the given object.
     */
    @Override
    public int compareTo(LocalizedText o) {

        return HasPreferenceUtils.compareTo(this, o);
    }

}
