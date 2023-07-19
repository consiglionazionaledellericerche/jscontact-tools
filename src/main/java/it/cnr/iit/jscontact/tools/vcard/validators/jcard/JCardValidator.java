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
package it.cnr.iit.jscontact.tools.vcard.validators.jcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedAddressScribe;
import it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe.ExtendedStructuredNameScribe;
import it.cnr.iit.jscontact.tools.vcard.validators.ezvcard.EZVCardValidator;
import lombok.Builder;

import java.util.List;

/**
 * Utility class for validating a jCard [RFC7095].
 * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
 *
 * @author Mario Loffredo
 */
@Builder
public class JCardValidator extends EZVCardValidator {

    private static ObjectMapper mapper = new ObjectMapper();
    /**
     * Validates a complete vCard v4.0 in JSON format, namely jCard [RFC7095].
     *
     * @param jCard a jCard as a JSON string
     * @throws CardException if the jCard is not v4.0 compliant
     * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
     */
    public void validate(String jCard) throws CardException {

        List<VCard> vcards = Ezvcard.parseJson(jCard)
                                    .register(new ExtendedAddressScribe())
                                    .register(new ExtendedStructuredNameScribe())
                                    .all();
        validate(vcards);
    }

    /**
     * Validates a complete vCard v4.0 in JSON format, namely jCard [RFC7095].
     *
     * @param jCard a jCard as an istance of Jackson library JsonNode class
     * @throws CardException if the jCard is not v4.0 compliant
     * @throws JsonProcessingException if the JsonNode object cannot be serialized
     * @see <a href="https://tools.ietf.org/html/rfc7095">RFC7095</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project Home</a>
     */
    public void validate(JsonNode jCard) throws CardException, JsonProcessingException {

        validate(mapper.writeValueAsString(jCard));
    }
}
