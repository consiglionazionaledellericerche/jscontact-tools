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

import it.cnr.iit.jscontact.tools.constraints.TitleOrganizationConstraint;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.dto.Kind;
import it.cnr.iit.jscontact.tools.dto.Title;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class TitleOrganizationValidator implements ConstraintValidator<TitleOrganizationConstraint, JSContact> {

    public void initialize(TitleOrganizationConstraint constraintAnnotation) {
    }

    public boolean isValid(JSContact jsContact, ConstraintValidatorContext context) {

        if (jsContact.getJobTitles() == null)
            return true;

        for (Title title : jsContact.getJobTitles().values()) {

            if (title.getOrganization() != null) {

                if (jsContact.getOrganizations() == null)
                    return false;

                if (jsContact.getOrganizations().get(title.getOrganization()) == null)
                    return false;

            }
        }

        return true;
    }

}
