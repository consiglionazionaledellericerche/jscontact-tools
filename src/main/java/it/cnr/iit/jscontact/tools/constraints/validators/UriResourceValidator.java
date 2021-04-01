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

import it.cnr.iit.jscontact.tools.constraints.UriResourceConstraint;
import it.cnr.iit.jscontact.tools.dto.Resource;
import it.cnr.iit.jscontact.tools.dto.ResourceType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URI;

public class UriResourceValidator implements ConstraintValidator<UriResourceConstraint, Resource> {

    public void initialize(UriResourceConstraint constraintAnnotation) {
    }

    public boolean isValid(Resource resource, ConstraintValidatorContext context) {

        if (resource == null)
            return true;

        if (resource.getType() != ResourceType.URI)
            return true;

        try {
            URI uri = URI.create(resource.getResource());
            return true;
        } catch(Exception e) {
            return false;
        }
    }

}
