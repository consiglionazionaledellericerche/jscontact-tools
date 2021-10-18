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
package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2xcard;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.JSContact;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard.JSContact2EZVCard;
import lombok.Builder;

import java.util.List;

/**
 * Utility class for converting a JSContact object into an xCard [RFC6351].
 * @see <a href="https://tools.ietf.org/html/rfc6351">RFC6351</a>
 *
 * @author Mario Loffredo
 */
public class JSContact2XCard extends JSContact2EZVCard {

    @Builder
    public JSContact2XCard(JSContact2VCardConfig config) {
        super();
        this.config = config;
    }

    /**
     * Converts one or more JSContact objects into a complete vCard v4.0 in XML format, namely xCard [RFC6351]
     * @param jsContacts a list of JSContact objects
     * @return an xCard as an XML string
     * @throws CardException if one of the JSContact objects is not valid
     * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/">draft-ietf-jmap-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/">draft-ietf-jmap-jscontact</a>
     */
    public String convertToXml(JSContact... jsContacts) throws CardException {

        List<VCard> vcards = convert(jsContacts);
        return Ezvcard.writeXml(vcards).go();
    }


}
