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

import it.cnr.iit.jscontact.tools.constraints.groups.profiles.Profile_RDAP;
import it.cnr.iit.jscontact.tools.constraints.profiles.ProfilePropertiesConstraint;
import it.cnr.iit.jscontact.tools.dto.AbstractExtensibleJSContactType;
import it.cnr.iit.jscontact.tools.dto.utils.ProfileUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ProfilePropertiesValidator implements ConstraintValidator<ProfilePropertiesConstraint, AbstractExtensibleJSContactType> {

    public void initialize(ProfilePropertiesConstraint constraintAnnotation) {
    }

    public boolean isValid(AbstractExtensibleJSContactType type, ConstraintValidatorContext context) {

        String className =  type.getClass().getName();
        String profileClassName = ProfileUtils.getProfileName();
        Map<String, List<String>> profileProperties = ProfileUtils.getProfileProperties().get(profileClassName);
        for(Field field : type.getClass().getFields()) {
            if (field.getName().equals("_type") || (className.equals("Card") && field.getName().equals("version")))
                continue;

            if (!profileProperties.get(className).contains(field.getName())) {
                context.buildConstraintViolationWithTemplate(String.format("the property %s of type %s is not included in the profile %s", field.getName(), className, ProfileUtils.getProfileNames().get(profileClassName))).addConstraintViolation();
                return false;
            }
        }

        if (type.getExtensions()!= null) {
            for (String extension : type.getExtensions().keySet()) {
                if (type.getExtensions() != null && !profileProperties.get(className).contains(extension)) {
                    context.buildConstraintViolationWithTemplate(String.format("the extension %s of type %s is not included in the profile %s", extension, className, ProfileUtils.getProfileNames().get(profileClassName))).addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }

}
