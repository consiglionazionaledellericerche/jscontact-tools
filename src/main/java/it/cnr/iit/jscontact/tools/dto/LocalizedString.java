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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPreference;
import it.cnr.iit.jscontact.tools.dto.utils.HasPreferenceUtils;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"value"})
public class LocalizedString extends GroupableObject implements HasAltid, HasPreference, Comparable<LocalizedString> {

    @NotNull(message = "value is missing in LocalizedString")
    @NonNull
    String value;

    String language;

    Map<String,String> localizations;

    @JsonIgnore
    String altid;

    @JsonIgnore
    Integer preference;

    public void addLocalization(String language, String value) {

        if (localizations == null)
            localizations = new HashMap<String, String>();

        localizations.put((language == null) ? "en" : language, value);
    }

    //to compare VCard TITLE, ROLE, NOTES instances based on preference
    @Override
    public int compareTo(LocalizedString o) {

        return HasPreferenceUtils.compareTo(this, o);
    }

}
