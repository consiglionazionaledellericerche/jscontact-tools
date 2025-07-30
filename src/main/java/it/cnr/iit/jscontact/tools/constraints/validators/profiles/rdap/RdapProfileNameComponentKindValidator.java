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

import it.cnr.iit.jscontact.tools.constraints.profiles.rdap.RdapProfileNameComponentKindConstraint;
import it.cnr.iit.jscontact.tools.dto.NameComponentEnum;
import it.cnr.iit.jscontact.tools.dto.NameComponentKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RdapProfileNameComponentKindValidator implements ConstraintValidator<RdapProfileNameComponentKindConstraint, NameComponentKind> {

    public void initialize(RdapProfileNameComponentKindConstraint constraintAnnotation) {
    }

    public boolean isValid(NameComponentKind kind, ConstraintValidatorContext context) {

        if (kind == null)
            return true;

        return kind.isRfc(NameComponentEnum.GIVEN) || kind.isRfc(NameComponentEnum.SURNAME);
    }

}
