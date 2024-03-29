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
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.VCardParam;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Custom JSON deserializer for the "vCardParams" map.
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class VCardParamsDeserializer extends JsonDeserializer<Map<String, VCardParam>> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, VCardParam> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Map<String, VCardParam> vCardParams = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String paramName = entry.getKey();
            VCardParam vCardParam;
            if (entry.getValue().isArray()) {
                List<String> array = mapper.treeToValue(entry.getValue(), List.class);
                vCardParam = VCardParam.builder().values(array.toArray(new String[0])).build();
            }
            else
                vCardParam = VCardParam.builder().value(entry.getValue().asText()).build();
            vCardParams.put(paramName,vCardParam);
        }
        return vCardParams;
    }
}
