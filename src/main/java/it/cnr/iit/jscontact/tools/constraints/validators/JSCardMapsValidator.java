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

import it.cnr.iit.jscontact.tools.constraints.JSCardMapsConstraint;
import it.cnr.iit.jscontact.tools.dto.ContactLanguage;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.Relation;
import it.cnr.iit.jscontact.tools.constraints.validators.builder.ValidatorBuilder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.util.Set;

public class JSCardMapsValidator implements ConstraintValidator<JSCardMapsConstraint, JSCard> {

    public void initialize(JSCardMapsConstraint constraintAnnotation) {
    }

    public boolean isValid(JSCard jsCard, ConstraintValidatorContext context) {

        if (jsCard == null)
            return true;

        if (jsCard.getPreferredContactLanguages() != null) {

            for(ContactLanguage[] cls : jsCard.getPreferredContactLanguages().values()) {

                if (cls == null)
                    return false;

                for (ContactLanguage cl : cls) {

                    if (cl == null)
                        return false;

                    Set<ConstraintViolation<ContactLanguage>> constraintViolations = ValidatorBuilder.getValidator().validate(cl);
                    if (constraintViolations.size() > 0)
                        return false;
                }
            }
        }

        if (jsCard.getRelatedTo() != null) {

            for(Relation rel : jsCard.getRelatedTo().values()) {

                if (rel == null)
                    return false;

                Set<ConstraintViolation<Relation>> constraintViolations = ValidatorBuilder.getValidator().validate(rel);
                if (constraintViolations.size() > 0)
                    return false;

            }
        }

        return true;
    }

}
