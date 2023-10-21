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
package it.cnr.iit.jscontact.tools.vcard.converters.jcard2jsontact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.vcard.converters.ezvcard2jscontact.EZVCard2JSContact;
import it.cnr.iit.jscontact.tools.vcard.converters.config.VCard2JSContactConfig;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardReader;
import lombok.Builder;

import java.util.*;

/**
 * Utility class for converting a jCard [RFC7095] into a JSContact object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
 * @author Mario Loffredo
 */
public class JCard2JSContact extends EZVCard2JSContact {

    @Builder
    public JCard2JSContact(VCard2JSContactConfig config) {
        super();
        this.config = config;
    }

    /**
     * Converts a complete vCard v4.0 in JSON format, namely jCard [RFC7095], into a list of Card objects
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     *
     * @param jCard a jCard as a JSON string
     * @return a list of Card objects
     * @throws CardException if the jCard is not v4.0 compliant
     * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<Card> convert(String jCard) throws CardException {

        List<VCard> vcards = VCardReader.parseJson(jCard);
        if (vcards.size() == 0)
            throw new CardException("Bad jCard format");
        return convert(vcards.toArray(new VCard[0]));
    }


    /**
     * Converts a complete vCard v4.0 in JSON format, namely jCard [RFC7095], into a list of Card objects.
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     * @param jCard a jCard as an istance of Jackson library JsonNode class
     * @return a list of Card objects
     * @throws CardException if the jCard is not v4.0 compliant
     * @throws JsonProcessingException if the jCard cannot be serialized
     * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project Home</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<Card> convert(JsonNode jCard) throws CardException, JsonProcessingException {

        return convert(mapper.writeValueAsString(jCard));
    }

}
