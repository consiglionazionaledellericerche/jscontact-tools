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
package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2jcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard.JSContact2EZVCard;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedAddressScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;
import lombok.Builder;

import java.util.List;

/**
 * Utility class for converting a Card object into a jCard [RFC7095].
 *
 * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
 * @author Mario Loffredo
 */
public class JSContact2JCard extends JSContact2EZVCard {

    @Builder
    public JSContact2JCard(JSContact2VCardConfig config) {
        super();
        this.config = config;
    }

    /**
     * Converts a list of Card objects into a complete vCard v4.0 in JSON format, namely jCard [RFC7095]
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     *
     * @param jsCards a list of Card objects
     * @return a jCard as a JSON string
     * @throws CardException if one of the Card objects is not valid
     * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public String convertToJson(Card... jsCards) throws CardException {

        List<VCard> vcards = convert(jsCards);
        return Ezvcard.writeJson(vcards)
                      .register(new ExtendedAddressScribe())
                      .register(new ExtendedStructuredNameScribe())
                      .go();
    }


    /**
     * Converts one or more objects into a complete vCard v4.0 in JSON format, namely jCard [RFC7095].
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     *
     * @param jsCards a list of Card objects
     * @return a jCard as an istance of Jackson library JsonNode class
     * @throws CardException if one of the Card objects is not valid
     * @throws JsonProcessingException if the jCard cannot be deserialized
     * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project Home</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public JsonNode convertToJsonNode(Card... jsCards) throws CardException, JsonProcessingException {

        String json = convertToJson(jsCards);
        return mapper.readTree(json);
    }


}
