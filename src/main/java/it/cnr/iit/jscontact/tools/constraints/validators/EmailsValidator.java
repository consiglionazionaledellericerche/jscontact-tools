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

import it.cnr.iit.jscontact.tools.constraints.EmailsConstraint;
import it.cnr.iit.jscontact.tools.dto.EmailResourceType;
import it.cnr.iit.jscontact.tools.dto.Resource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailsValidator implements ConstraintValidator<EmailsConstraint, Resource[]> {

    public void initialize(EmailsConstraint constraintAnnotation) {
    }

    public boolean isValid(Resource[] emails, ConstraintValidatorContext context) {

        if (emails == null)
            return true;

        try {

            for (Resource email : emails){
                if (email.getType() == null)
                    continue;
                EmailResourceType type = EmailResourceType.getEnum(email.getType());
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
