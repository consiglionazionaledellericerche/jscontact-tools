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
package it.cnr.iit.jscontact.tools.dto.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import it.cnr.iit.jscontact.tools.dto.PhoneFeature;
import it.cnr.iit.jscontact.tools.dto.PhoneFeatureEnum;
import it.cnr.iit.jscontact.tools.dto.V_Extension;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Custom JSON deserializer for the PhoneFeature map.
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class PhoneFeaturesDeserializer extends JsonDeserializer<Map<PhoneFeature,Boolean>> {

    @Override
    public Map<PhoneFeature,Boolean> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Map<PhoneFeature,Boolean> contexts = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String type = entry.getKey();
            Boolean value = entry.getValue().asBoolean();
            PhoneFeature feature;
            try {
                feature = PhoneFeature.builder().rfcValue(PhoneFeatureEnum.getEnum(type)).build();
            } catch (IllegalArgumentException e) {
                feature = PhoneFeature.builder().extValue(V_Extension.toV_Extension(type)).build();
            }
            contexts.put(feature,value);
        }
        return contexts;
    }
}
