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
package it.cnr.iit.jscontact.tools.constraints.validators.profiles.rdap;

import it.cnr.iit.jscontact.tools.constraints.profiles.rdap.RdapProfilePhoneFeaturesConstraint;
import it.cnr.iit.jscontact.tools.dto.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class RdapProfilePhoneFeaturesValidator implements ConstraintValidator<RdapProfilePhoneFeaturesConstraint, Map<PhoneFeature,Boolean>> {

    public void initialize(RdapProfilePhoneFeaturesConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<PhoneFeature,Boolean> features, ConstraintValidatorContext context) {

        if (features == null)
            return true;

        for (PhoneFeature feature : features.keySet()) {
            if (!feature.isVoice() && !feature.isFax())
                return false;
        }

        return true;
    }

}
