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
package it.cnr.iit.jscontact.tools.vcard.validators.xcard;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.validators.ezvcard.EZVCardValidator;
import lombok.Builder;

import java.util.List;

/**
 * Utility class for validating an xCard [RFC6351].
 * @see <a href="https://tools.ietf.org/html/rfc6351">RFC6351</a>
 *
 * @author Mario Loffredo
 */
@Builder
public class XCardValidator extends EZVCardValidator {

    /**
     * Validates a complete vCard v4.0 in XML format, namely xCard [RFC6351].
     *
     * @param xCard an xCard as an XML string
     * @throws CardException if the xCard is not v4.0 compliant
     * @see <a href="https://tools.ietf.org/html/rfc6351">RFC6351</a>
     */
    public void validate(String xCard) throws CardException {

        List<VCard> vcards = Ezvcard.parseXml(xCard).all();
        validate(vcards);
    }

}
