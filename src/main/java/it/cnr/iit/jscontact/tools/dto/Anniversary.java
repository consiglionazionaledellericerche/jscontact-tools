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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.AnniversaryDateDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.AnniversaryTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasType;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.serializers.AnniversaryDateSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the Anniversary type as defined in section 2.8.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","type","date","place"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anniversary extends AbstractJSContactType implements HasType, IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "Anniversary", message="invalid @type value in Anniversary")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Anniversary";

    @JsonDeserialize(using = AnniversaryTypeDeserializer.class)
    AnniversaryType type;

    @NotNull(message = "date is missing in Anniversary")
    @NonNull
    @JsonSerialize(using = AnniversaryDateSerializer.class)
    @JsonDeserialize(using = AnniversaryDateDeserializer.class)
    AnniversaryDate date;

    @Valid
    Address place;

    /**
     * Tests if this anniversary is a birthday. See vCard 4.0 BDAY property as defined in section 6.2.5 of [RFC6350].
     *
     * @return true if this anniversary is a birthday, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.5">RFC6350</a>
     */
    @JsonIgnore
    public boolean isBirth() { return type.isBirth(); }

    /**
     * Tests if this anniversary is a date of death. See vCard 4.0 DEATHDATE property as defined in section 6.2.5 of [RFC6474].
     *
     * @return true if this anniversary is a date of death, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6474#section-2.3">RFC6474</a>
     */
    @JsonIgnore
    public boolean isDeath() { return type.isDeath(); }

    /**
     * Tests if this anniversary is a date of marriage, or equivalent. See vCard 4.0 ANNIVERSARY property as defined in section 6.2.6 of [RFC6350].
     *
     * @return true if this anniversary is a date of marriage, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.6">RFC6350</a>
     */
    @JsonIgnore
    public boolean isMarriage() { return type.isMarriage(); }

    /**
     * Tests if this is an undefined anniversary specified by the value of the "label" property.
     *
     * @return true if this is an undefined anniversary, false otherwise
     */
    @JsonIgnore
    public boolean isOtherAnniversary() { return type == null || type.isExtValue(); }

    private static Anniversary anniversary(AnniversaryType type, AnniversaryDate date, String label) {
        return Anniversary.builder().type(type).date(date).build();
    }
}
