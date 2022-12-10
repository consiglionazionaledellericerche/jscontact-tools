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
import it.cnr.iit.jscontact.tools.dto.PersonalInfoLevelEnum;
import it.cnr.iit.jscontact.tools.dto.PersonalInfoLevelType;
import it.cnr.iit.jscontact.tools.dto.V_Extension;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Custom JSON deserializer for the PersonalInfoLevelType value.
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class PersonalInfoLevelTypeDeserializer extends JsonDeserializer<PersonalInfoLevelType> {

    @Override
    public PersonalInfoLevelType deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String value = node.asText();
        try {
            return PersonalInfoLevelType.builder().rfcValue(PersonalInfoLevelEnum.getEnum(value)).build();
        } catch (IllegalArgumentException e) {
            return PersonalInfoLevelType.builder().extValue(V_Extension.toV_Extension(value)).build();
        }
    }
}
