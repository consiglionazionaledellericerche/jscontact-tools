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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.constraints.UriConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the OnlineService type as defined in section 2.3.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */

@JsonPropertyOrder({"@type", "service", "user", "kind", "contexts", "pref", "label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OnlineService extends AbstractJSContactType implements HasLabel, IdMapValue, Serializable, HasContexts {

    @Pattern(regexp = "OnlineService", message = "invalid @type value in OnlineService")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "OnlineService";

    String service;

    @UriConstraint
    String uri;

    String user;

    @JsonSerialize(using = ContextsSerializer.class)
    @JsonDeserialize(using = ContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in OnlineService - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context, Boolean> contexts;

    @Min(value = 1, message = "invalid pref in OnlineService - value must be greater or equal than 1")
    @Max(value = 100, message = "invalid pref in OnlineService - value must be less or equal than 100")
    Integer pref;

    String label;

}
