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
package it.cnr.iit.jscontact.tools.dto.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.cnr.iit.jscontact.tools.dto.PhoneFeature;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

/**
 * Custom JSON serializer for the PhoneFeature map.
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class PhoneFeaturesSerializer extends JsonSerializer<Map<PhoneFeature,Boolean>> {

    @Override
    public void serialize(
            Map<PhoneFeature,Boolean> features, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (features.isEmpty()) {
            jgen.writeNull();
            return;
        }

        jgen.writeStartObject();
        for (Map.Entry<PhoneFeature,Boolean> entry : features.entrySet())
            jgen.writeBooleanField(entry.getKey().toJson(), entry.getValue());
        jgen.writeEndObject();

    }
}
