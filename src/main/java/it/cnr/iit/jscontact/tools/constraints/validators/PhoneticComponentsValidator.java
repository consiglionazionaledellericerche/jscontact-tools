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
import it.cnr.iit.jscontact.tools.constraints.PhoneticComponentsConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPhonetic;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPhoneticComponents;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Map;

public class PhoneticComponentsValidator implements ConstraintValidator<PhoneticComponentsConstraint, HasPhoneticComponents> {

    private static final ObjectMapper mapper = new ObjectMapper();


    public void initialize(PhoneticComponentsConstraint constraintAnnotation) {
    }

    public boolean isValid(HasPhoneticComponents object, ConstraintValidatorContext context) {

        if (object == null)
            return true;

        if (object.getComponents() == null)
            return true;

            for (HasPhonetic component : object.getComponents()) {
                if (component.getPhonetic()!=null && object.getPhoneticScript()==null && object.getPhoneticSystem()==null) {
                    context.buildConstraintViolationWithTemplate("component includes the phonetic property but parent object doesn't include either the phoneticSystem or the phoneticScript properties").addConstraintViolation();
                    return false;
                }
            }

        return true;

    }

}
