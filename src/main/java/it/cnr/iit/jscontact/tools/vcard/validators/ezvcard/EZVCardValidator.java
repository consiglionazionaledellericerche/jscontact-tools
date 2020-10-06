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
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;

import java.util.ArrayList;
import java.util.List;

public class EZVCardValidator {

    /**
     * Validates a complete vCard v4.0 [RFC6350]
     * @param vCards a vCard as a list of instances of the ez-vcard library VCard class
     * @throws CardException if the vCard is not v4.0 compliant
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
     */
    public void validate(List<VCard> vCards) throws CardException {

        List<JSCard> jsCards = new ArrayList<JSCard>();

        for (VCard vCard : vCards) {
            ValidationWarnings warnings = vCard.validate(VCardVersion.V4_0);
            if (!warnings.isEmpty())
                throw new CardException(warnings.toString());
        }
    }

}