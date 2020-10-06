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
package it.cnr.iit.jscontact.tools.vcard.converters.xcard2jscontact;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.interfaces.JSContact;
import it.cnr.iit.jscontact.tools.vcard.converters.ezvcard2jscontact.EZVCard2JSContact;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import lombok.Builder;

import java.util.List;

public class XCard2JSContact extends EZVCard2JSContact {

    @Builder
    public XCard2JSContact(VCard2JSContactConfig config) {
        super();
        this.config = config;
    }

    /**
     * Converts a complete vCard v4.0 in XML format, namely xCard [RFC6351], into a list of JSContact objects.
     * JSContact is defined in draft-ietf-jmap-jscontact.
     * Conversion rules are defined in draft-ietf-jmap-jscontact-vcard.
     * @param xCard an xCard as an XML string
     * @return the list of JSContact objects
     * @throws CardException if the xCard is not v4.0 compliant
     * @see <a href="https://tools.ietf.org/html/rfc6351">RFC6351</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    public List<JSContact> convert(String xCard) throws CardException {

        List<VCard> vcards = Ezvcard.parseXml(xCard).all();
        if (vcards.size() == 0)
            throw new CardException("Bad xCard format");
        return convert(vcards);
    }

}
