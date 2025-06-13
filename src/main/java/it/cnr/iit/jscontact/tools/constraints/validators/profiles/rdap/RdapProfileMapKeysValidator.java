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

import it.cnr.iit.jscontact.tools.constraints.profiles.rdap.RdapProfileMapKeysConstraint;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.rdap.JSContactForRdapMapId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class RdapProfileMapKeysValidator implements ConstraintValidator<RdapProfileMapKeysConstraint, Map<String,? extends IdMapValue>> {

    public void initialize(RdapProfileMapKeysConstraint constraintAnnotation) {
    }

    public boolean isValid(Map<String,? extends IdMapValue> map, ConstraintValidatorContext context) {

        if (map == null)
            return true;

        IdMapValue idMapValue = (IdMapValue) map.values().toArray()[0];

        if (idMapValue instanceof EmailAddress)
            return map.containsKey(JSContactForRdapMapId.EMAIL_ID.getValue());

        if (idMapValue instanceof Address)
            return map.containsKey(JSContactForRdapMapId.ADDRESS_ID.getValue());

        if (idMapValue instanceof Phone)
            return map.containsKey(JSContactForRdapMapId.VOICE_ID.getValue()) ||
                    map.containsKey(JSContactForRdapMapId.FAX_ID.getValue());

        if (idMapValue instanceof Organization)
            return map.containsKey(JSContactForRdapMapId.ORG_ID.getValue());

        if (idMapValue instanceof Link)
            return map.containsKey(JSContactForRdapMapId.URL_ID.getValue()) ||
                    map.containsKey(JSContactForRdapMapId.CONTACT_URI_ID.getValue());

        return true;
    }

}
