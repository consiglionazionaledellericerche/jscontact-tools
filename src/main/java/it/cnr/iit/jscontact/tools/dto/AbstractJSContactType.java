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
package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.VCardParamsDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.VCardParamsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class mapping the vCard counterparts of JSContact extensions  as defined in section 2.15.1 of [draft-ietf-calext-jscontact-vcard].
 * The class contains two other properties shared by all JSContact types.
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.15.1">draft-ietf-calext-jscontact-vcard</a>
 * @author Mario Loffredo
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractJSContactType extends AbstractExtensibleJSContactType {

    @JsonIgnore
    @Getter
    @Setter
    String propId;

    @JsonSerialize(using = VCardParamsSerializer.class)
    @JsonDeserialize(using = VCardParamsDeserializer.class)
    @Valid
    Map<String, VCardParam> vCardParams;


    /**
     * Adds a VCardParam object to this object.
     *
     * @param id the link resource identifier
     * @param vCardParam the VCardParam object
     */
    public void addLinkResource(String id, VCardParam vCardParam) {

        if (vCardParams == null)
            vCardParams = new HashMap<>();

        vCardParams.putIfAbsent(id, vCardParam);
    }

}
