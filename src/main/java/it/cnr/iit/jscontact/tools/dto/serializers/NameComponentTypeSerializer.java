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
import it.cnr.iit.jscontact.tools.dto.NameComponentType;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class NameComponentTypeSerializer extends JsonSerializer<NameComponentType> {

    @Override
    public void serialize(
            NameComponentType type, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeString((type.getRfcValue()!=null) ? type.getRfcValue().getValue() : type.getExtValue());

    }
}
