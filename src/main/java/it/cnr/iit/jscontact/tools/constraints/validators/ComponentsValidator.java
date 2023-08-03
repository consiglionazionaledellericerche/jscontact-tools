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
            context.buildConstraintViolationWithTemplate("defaultSeparator must not be set if the isOrdered property value is false").addConstraintViolation();
            return false;
        }

        if (object.getComponents() == null)
            return true;

        if (object.getComponents().length == 0) {
            context.buildConstraintViolationWithTemplate("components array must not be empty").addConstraintViolation();
            return false;
        }

        boolean previousComponentIsSeparator = false;
        for (HasPhonetic component : object.getComponents()) {
            if (component.getPhonetic()!=null && object.getPhoneticScript()==null && object.getPhoneticSystem()==null) {
                context.buildConstraintViolationWithTemplate("if phonetic is set, at least one of the parent object phoneticSystem or phoneticScript properties must be set").addConstraintViolation();
                return false;
            }

            if (object.getIsOrdered()!=null && object.getIsOrdered() == Boolean.FALSE && component.getKind().toJson().equals("separator")) {
                context.buildConstraintViolationWithTemplate("if a separator component is set, the isOrdered member of the parent object must be true").addConstraintViolation();
                return false;
            }

            if (component.getKind().toJson().equals("separator")) {
                if (previousComponentIsSeparator) {
                    context.buildConstraintViolationWithTemplate("two consecutive separator components must not be set").addConstraintViolation();
                    return false;
                }
                previousComponentIsSeparator = true;
            } else
                previousComponentIsSeparator = false;
        }

        return true;

    }

}
