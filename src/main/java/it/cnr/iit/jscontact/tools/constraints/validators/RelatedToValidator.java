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

import it.cnr.iit.jscontact.tools.constraints.RelatedToConstraint;
import it.cnr.iit.jscontact.tools.constraints.validators.builder.ValidatorBuilder;
import it.cnr.iit.jscontact.tools.dto.Relation;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;

public class RelatedToValidator implements ConstraintValidator<RelatedToConstraint, Map<String,Relation>> {

    public void initialize(RelatedToConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<String,Relation> relMap, ConstraintValidatorContext context) {

        if (relMap == null)
            return true;

        for(Relation rel : relMap.values()) {

            if (rel == null)
                return false;

            Set<ConstraintViolation<Relation>> constraintViolations = ValidatorBuilder.getValidator().validate(rel);
            if (constraintViolations.size() > 0) {
                context.buildConstraintViolationWithTemplate(StringUtils.EMPTY).addConstraintViolation();
                return false;
            }

        }

        return true;
    }

}
