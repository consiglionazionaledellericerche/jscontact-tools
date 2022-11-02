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
import it.cnr.iit.jscontact.tools.dto.JCardProp;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

/**
 * Custom JSON serializer for the JCardProp array.
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class JCardPropsSerializer extends JsonSerializer<JCardProp[]> {

    @Override
    public void serialize(
            JCardProp[] jCardProps, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (jCardProps == null)
            return;

        if (jCardProps.length == 0)
            return;

        jgen.writeStartArray();
        for (JCardProp jCardProp : jCardProps) {
            jgen.writeStartArray();
            jgen.writeString(jCardProp.getName());
            jgen.writeStartObject();
            for(Map.Entry<String,Object> entry : jCardProp.getParameters().entrySet()) {
                jgen.writeFieldName(entry.getKey().toLowerCase());
                jgen.writeObject(entry.getValue());
            }
            jgen.writeEndObject();
            jgen.writeString((jCardProp.getType() == null) ? "unknown" : jCardProp.getType().getName());
            jgen.writeObject(jCardProp.getValue());
            jgen.writeEndArray();
        }
        jgen.writeEndArray();
    }
}
