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

import it.cnr.iit.jscontact.tools.constraints.PreferredLanguagesConstraint;
import it.cnr.iit.jscontact.tools.constraints.validators.builder.ValidatorBuilder;
import it.cnr.iit.jscontact.tools.dto.LanguagePreference;
import it.cnr.iit.jscontact.tools.dto.utils.ConstraintViolationUtils;
import sun.util.locale.LanguageTag;
import sun.util.locale.ParseStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;


public class PreferredLanguagesValidator implements ConstraintValidator<PreferredLanguagesConstraint, Map<String, LanguagePreference[]>> {

    public void initialize(PreferredLanguagesConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<String, LanguagePreference[]> clMap, ConstraintValidatorContext context) {

        if (clMap == null)
            return true;

        for(Map.Entry<String, LanguagePreference[]> entry : clMap.entrySet()) {

            ParseStatus parseStatus = new ParseStatus();
            LanguageTag.parse(entry.getKey(), parseStatus);
            if (parseStatus.getErrorMessage() != null) {
                context.buildConstraintViolationWithTemplate("invalid language tag in preferredLanguages").addConstraintViolation();
                return false;
            }

            if (entry.getValue() == null) {
                context.buildConstraintViolationWithTemplate("null LanguagePreference in preferredLanguages").addConstraintViolation();
                return false;
            }

            for (LanguagePreference cl : entry.getValue()) {

                Set<ConstraintViolation<LanguagePreference>> constraintViolations = ValidatorBuilder.getValidator().validate(cl);
                if (constraintViolations.size() > 0) {
                    context.buildConstraintViolationWithTemplate(ConstraintViolationUtils.getMessage(constraintViolations)).addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }

}
