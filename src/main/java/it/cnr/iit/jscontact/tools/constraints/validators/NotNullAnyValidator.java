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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import org.apache.commons.beanutils.PropertyUtils;

public class NotNullAnyValidator implements ConstraintValidator<NotNullAnyConstraint, Object> {

    private String[] fieldNames;

    public void initialize(NotNullAnyConstraint constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fieldNames();
    }

    public boolean isValid(Object object, ConstraintValidatorContext context) {

        if (object == null)
            return true;

        try {

            for (String fieldName:fieldNames){
                Object property = PropertyUtils.getProperty(object, fieldName);

                if (property!=null) return true;
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

}
