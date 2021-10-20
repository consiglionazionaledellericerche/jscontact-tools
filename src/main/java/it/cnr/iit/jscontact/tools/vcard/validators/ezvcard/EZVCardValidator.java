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
package it.cnr.iit.jscontact.tools.vcard.validators.ezvcard;

import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.ValidationWarnings;
import it.cnr.iit.jscontact.tools.exceptions.CardException;

import java.util.List;

/**
 * Abstract utility class for validating a list of Ezvcard VCard objects.
 *
 * @author Mario Loffredo
 */
public abstract class EZVCardValidator {

    protected void validate(List<VCard> vCards) throws CardException {

        for (VCard vCard : vCards) {
            ValidationWarnings warnings = vCard.validate(VCardVersion.V4_0);
            if (!warnings.isEmpty())
                throw new CardException(warnings.toString());
        }
    }

}