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

import it.cnr.iit.jscontact.tools.dto.RelationType;
import it.cnr.iit.jscontact.tools.constraints.RelationTypeConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class RelationTypeValidator implements ConstraintValidator<RelationTypeConstraint, Map<String,Boolean>> {

    public void initialize(RelationTypeConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<String,Boolean> relations, ConstraintValidatorContext context) {

        if (relations == null)
            return true;

        try {

            for (String key : relations.keySet()){
                RelationType type = RelationType.getEnum(key);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
