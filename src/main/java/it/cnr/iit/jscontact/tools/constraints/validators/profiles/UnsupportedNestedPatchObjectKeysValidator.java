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
package it.cnr.iit.jscontact.tools.constraints.validators.profiles;

import com.fasterxml.jackson.databind.JsonNode;
import it.cnr.iit.jscontact.tools.constraints.profiles.UnsupportedNestedPatchObjectKeysConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class UnsupportedNestedPatchObjectKeysValidator implements ConstraintValidator<UnsupportedNestedPatchObjectKeysConstraint, Map<String, Map<String, JsonNode>>> {

    public void initialize(UnsupportedNestedPatchObjectKeysConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<String, Map<String, JsonNode>> localizations, ConstraintValidatorContext context) {

        if (localizations == null)
            return true;

        for (Map.Entry<String,Map<String,JsonNode>> localizationsPerLang : localizations.entrySet()) {
            for (String localizationKey : localizationsPerLang.getValue().keySet()) {
                if (localizationKey.contains("/"))
                    return false;
            }
        }

        return true;
    }

}
