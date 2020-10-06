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
package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.iit.jscontact.tools.constraints.validators.builder.ValidatorBuilder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ToString
public class ValidableObject {

    @JsonIgnore
    @Getter
    private List<String> validationMessages = new ArrayList<String>();

    @JsonIgnore
    public boolean isValid() {

        validationMessages.clear();

        Set<ConstraintViolation<ValidableObject>> constraintViolations = ValidatorBuilder.getValidator().validate(this);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<ValidableObject> constraintViolation : constraintViolations)
                validationMessages.add(constraintViolation.getMessage());
            return false;
        }

        return true;
    }

}
