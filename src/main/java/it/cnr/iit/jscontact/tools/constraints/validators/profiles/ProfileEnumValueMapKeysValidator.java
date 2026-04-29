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

import it.cnr.iit.jscontact.tools.constraints.profiles.ProfileEnumValueMapKeysConstraint;
import it.cnr.iit.jscontact.tools.dto.ExtensibleEnumType;
import it.cnr.iit.jscontact.tools.dto.utils.ProfileUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;

public class ProfileEnumValueMapKeysValidator implements ConstraintValidator<ProfileEnumValueMapKeysConstraint, Map<? extends ExtensibleEnumType, Boolean>> {

    public void initialize(ProfileEnumValueMapKeysConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<? extends ExtensibleEnumType, Boolean> map, ConstraintValidatorContext context) {

        if (map == null || map.isEmpty())
            return true;

        String className =  map.keySet().stream().iterator().next().getClass().getName();
        String profileClassName = ProfileUtils.getProfileName();
        Map<String, List<String>> profileEnumValues = ProfileUtils.getProfileEnumValues().get(profileClassName);

        if (profileEnumValues == null || profileEnumValues.get(className) == null)
            return true;

        for (ExtensibleEnumType type : map.keySet()) {
            String enumValue = (type.isRfcValue()) ? type.getRfcValue().getValue() : type.getExtValue().toString();
            if (!profileEnumValues.get(className).contains(enumValue)) {
                context.buildConstraintViolationWithTemplate(String.format("the enum value %s of type %s is not included in the %s profile", enumValue, className, ProfileUtils.getProfileNames().get(profileClassName))).addConstraintViolation();
                return false;
            }
        }

        return true;
    }

}
