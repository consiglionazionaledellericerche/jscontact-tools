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

import it.cnr.iit.jscontact.tools.constraints.CardGroupKindConstraint;
import it.cnr.iit.jscontact.tools.dto.CardGroup;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CardGroupKindValidator implements ConstraintValidator<CardGroupKindConstraint, CardGroup> {

    public void initialize(CardGroupKindConstraint constraintAnnotation) {
    }

    public boolean isValid(CardGroup group, ConstraintValidatorContext context) {

        return group.getCard() == null || group.getCard().getKind() == null || group.getCard().getKind().isGroup();

    }

}
