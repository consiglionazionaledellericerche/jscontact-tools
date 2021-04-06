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
package it.cnr.iit.jscontact.tools.constraints.validators;

import it.cnr.iit.jscontact.tools.constraints.IdMapConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdMapValidator implements ConstraintValidator<IdMapConstraint, Map<String,? extends IdMapValue>> {

    private static final Pattern ID_PATTERN = Pattern.compile("[A-Za-z0-9_\\-]+");

    public void initialize(IdMapConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<String,? extends IdMapValue> map, ConstraintValidatorContext context) {

        if (map == null)
            return true;

        for (String id : map.keySet()){

            Matcher matcher = ID_PATTERN.matcher(id);
            if (!matcher.matches())
                return false;
        }

        return true;
    }

}
