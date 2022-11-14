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
import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.dto.JCardProp;
import it.cnr.iit.jscontact.tools.dto.V_Extension;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

/**
 * Custom JSON deserializer for the JCardProp array.
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class JCardPropsDeserializer extends JsonDeserializer<JCardProp[]> {

    private static Map<String,Object> getParameters(JsonNode node) {

        Map<String,Object> parameters = new HashMap<>();
        if (node == null || !node.isObject())
            return parameters;

        Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            parameters.put(entry.getKey(),JsonNodeUtils.getValue(entry.getValue()));
        }
        return parameters;
    }

    @Override
    public JCardProp[] deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node == null || !node.isArray())
            return null;

        List<JCardProp> list = new ArrayList<>();
        for (JsonNode subnode : node) {
            if (subnode == null || !subnode.isArray())
                return null;
            Map<String,String> parameters = new HashMap<>();
            list.add(JCardProp.builder()
                              .name(V_Extension.toV_Extension(subnode.get(0).asText()))
                              .parameters(getParameters(subnode.get(1)))
                              .type((subnode.get(2).asText().equals("unknown")) ? null : VCardDataType.get(subnode.get(2).asText()))
                              .value(JsonNodeUtils.getValue(subnode.get(3)))
                              .build());
        }

        return list.toArray(new JCardProp[0]);
    }
}
