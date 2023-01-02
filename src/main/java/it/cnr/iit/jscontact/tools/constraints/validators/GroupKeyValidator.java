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

import it.cnr.iit.jscontact.tools.constraints.GroupKeyConstraint;
import it.cnr.iit.jscontact.tools.dto.PropertyGroup;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupKeyValidator implements ConstraintValidator<GroupKeyConstraint, Map<String, PropertyGroup>> {

    private static final Pattern KEY_PATTERN = Pattern.compile("[A-Za-z0-9\\-]+");

    public void initialize(GroupKeyConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<String,PropertyGroup> map, ConstraintValidatorContext context) {

        if (map == null)
            return true;

        for (String id : map.keySet()){

            Matcher matcher = KEY_PATTERN.matcher(id);
            if (!matcher.matches())
                return false;
        }

        Map<String, PropertyGroup> ciMap = new CaseInsensitiveMap();
        ciMap.putAll(map);
        return ciMap.size() == map.size();
    }

}
