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

import it.cnr.iit.jscontact.tools.constraints.PreferredContactChannelsConstraint;
import it.cnr.iit.jscontact.tools.constraints.validators.builder.ValidatorBuilder;
import it.cnr.iit.jscontact.tools.dto.ChannelType;
import it.cnr.iit.jscontact.tools.dto.ContactChannelPreference;
import it.cnr.iit.jscontact.tools.dto.utils.ConstraintViolationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;


public class PreferredContactChannelsValidator implements ConstraintValidator<PreferredContactChannelsConstraint, Map<ChannelType, ContactChannelPreference[]>> {

    public void initialize(PreferredContactChannelsConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<ChannelType, ContactChannelPreference[]> clMap, ConstraintValidatorContext context) {

        if (clMap == null)
            return true;

        for(Map.Entry<ChannelType, ContactChannelPreference[]> entry : clMap.entrySet()) {

            if (entry.getValue() == null) {
                context.buildConstraintViolationWithTemplate("null ContactChannelPreference in preferredContactedChannels").addConstraintViolation();
                return false;
            }

            for (ContactChannelPreference cl : entry.getValue()) {

                Set<ConstraintViolation<ContactChannelPreference>> constraintViolations = ValidatorBuilder.getValidator().validate(cl);
                if (constraintViolations.size() > 0) {
                    context.buildConstraintViolationWithTemplate(ConstraintViolationUtils.getMessage(constraintViolations)).addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }

}
