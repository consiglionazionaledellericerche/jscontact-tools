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
package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2vcard;

import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard.JSContact2EZVCard;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardWriter;
import lombok.Builder;

import java.util.List;

/**
 * Utility class for converting into a Card object into a vCard 4.0 [RFC6350].
 *
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 * @author Mario Loffredo
 */
public class JSContact2VCard extends JSContact2EZVCard {

    @Builder
    public JSContact2VCard(JSContact2VCardConfig config) {
        super();
        this.config = config;
    }

    /**
     * Converts one or more Card objects into a complete vCard v4.0 [RFC6350].
     * JSContact is defined in [RFC9553].
     * JSContact extensions to vCard are defined in [RFC9554]
     * Conversion rules are defined in [RFC9555].
     *
     * @param jsCards a list of Card objects
     * @return a vCard as a text
     * @throws CardException if one of the Card objects is not valid
     * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553/">RFC9553</a>
     * @see <a href="https://datatracker.ietf.org/doc/RFC9554/">RFC9554</a>
     * @see <a href="https://datatracker.ietf.org/doc/RFC9555/">RFC9555</a>
     */
    public String convertToText(Card... jsCards) throws CardException {

        List<VCard> vcards = convert(jsCards);
        return VCardWriter.write(vcards);
    }


}
