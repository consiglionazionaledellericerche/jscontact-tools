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

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.constraints.NotNullDependencyConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class NotNullDependencyValidator implements ConstraintValidator<NotNullDependencyConstraint, Object> {

    private static final ObjectMapper mapper = new ObjectMapper();

    private String fieldName;
    private String[] dependingFieldNames;

    public void initialize(NotNullDependencyConstraint constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.dependingFieldNames = constraintAnnotation.dependingFieldNames();
    }

    public boolean isValid(Object object, ConstraintValidatorContext context) {

        if (object == null)
            return true;

        try {

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);

            for (String dependingFieldName : dependingFieldNames) {
                Field dependingField = object.getClass().getDeclaredField(dependingFieldName);
                dependingField.setAccessible(true);
                Object dependingValue = dependingField.get(object);

                if (dependingValue!=null && value == null) {
                    context.buildConstraintViolationWithTemplate(String.format("if %s is set, %s must be set", dependingFieldName, fieldName)).addConstraintViolation();
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

}
