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

import ezvcard.util.DataUri;
import it.cnr.iit.jscontact.tools.constraints.UriConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URI;

public class UriValidator implements ConstraintValidator<UriConstraint, String> {

    public void initialize(UriConstraint constraintAnnotation) {
    }

    public boolean isValid(String uri, ConstraintValidatorContext context) {

        if (uri == null)
            return true;

        if (uri.startsWith("data:")) {
            try {
                DataUri dataUri = DataUri.parse(uri);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        try {
            URI.create(uri);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
