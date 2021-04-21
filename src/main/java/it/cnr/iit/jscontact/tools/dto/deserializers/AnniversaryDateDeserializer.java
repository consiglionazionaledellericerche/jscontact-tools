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
import ezvcard.util.PartialDate;
import it.cnr.iit.jscontact.tools.dto.AnniversaryDate;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Calendar;

@NoArgsConstructor
public class AnniversaryDateDeserializer extends JsonDeserializer<AnniversaryDate> {


    @Override
    public AnniversaryDate deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        try {
            Calendar date = DateUtils.toCalendar(node.asText());
            return AnniversaryDate.builder().date(date).build();
        } catch (Exception e) {
            try {
                PartialDate partialDate = PartialDate.parse(DateUtils.toVCardPartialDateText(node.asText()));
                return AnniversaryDate.builder().partialDate(partialDate).build();
            } catch (Exception e1) {
                return null;
            }
        }
    }
}
