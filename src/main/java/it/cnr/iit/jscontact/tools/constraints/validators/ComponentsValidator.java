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
import it.cnr.iit.jscontact.tools.constraints.ComponentsConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPhonetic;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasComponents;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ComponentsValidator implements ConstraintValidator<ComponentsConstraint, HasComponents> {

    private static final ObjectMapper mapper = new ObjectMapper();


    public void initialize(ComponentsConstraint constraintAnnotation) {
    }

    public boolean isValid(HasComponents object, ConstraintValidatorContext context) {

        if (object == null)
            return true;

        if (object.getIsOrdered()!=null && object.getIsOrdered()==Boolean.FALSE && object.getDefaultSeparator()!=null) {
            context.buildConstraintViolationWithTemplate("isOrdered is false but defaultSeparator is not null").addConstraintViolation();
            return false;
        }

        if (object.getComponents() == null)
            return true;

        if (object.getComponents().length == 0) {
            context.buildConstraintViolationWithTemplate("components array is empty").addConstraintViolation();
            return false;
        }

        for (HasPhonetic component : object.getComponents()) {
            if (component.getPhonetic()!=null && object.getPhoneticScript()==null && object.getPhoneticSystem()==null) {
                context.buildConstraintViolationWithTemplate("component includes the phonetic property but parent object doesn't include either the phoneticSystem or the phoneticScript properties").addConstraintViolation();
                return false;
            }
        }

        return true;

    }

}
