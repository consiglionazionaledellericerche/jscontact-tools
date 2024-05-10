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
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.AnniversaryDateDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.AnniversaryKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import it.cnr.iit.jscontact.tools.dto.serializers.AnniversaryDateSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


/**
 * Class mapping the Anniversary type as defined in section 2.8.1 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.1">Section 2.8.1 of RFC9553</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type", "kind", "date", "place"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Anniversary extends AbstractJSContactType implements HasKind, IdMapValue, IsIANAType, Serializable {

    @Pattern(regexp = "Anniversary", message = "invalid @type value in Anniversary")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Anniversary";

    @NotNull(message = "kind is missing in Anniversary")
    @JsonDeserialize(using = AnniversaryKindDeserializer.class)
    @ContainsExtensibleEnum(enumClass = AnniversaryEnum.class, getMethod = "getKind")
    AnniversaryKind kind;

    @NotNull(message = "date is missing in Anniversary")
    @NonNull
    @JsonSerialize(using = AnniversaryDateSerializer.class)
    @JsonDeserialize(using = AnniversaryDateDeserializer.class)
    @Valid
    AnniversaryDate date;

    @Valid
    Address place;

    /**
     * Tests if this anniversary is a birthday. See vCard 4.0 BDAY property as defined in section 6.2.5 of [RFC6350].
     *
     * @return true if this anniversary is a birthday, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.5">Section 6.2.5 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isBirth() { return kind.isBirth(); }

    /**
     * Tests if this anniversary is a date of death. See vCard 4.0 DEATHDATE property as defined in section 2.3 of [RFC6474].
     *
     * @return true if this anniversary is a date of death, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6474#section-2.3">Section 2.3 of RFC6474</a>
     */
    @JsonIgnore
    public boolean isDeath() { return kind.isDeath(); }

    /**
     * Tests if this anniversary is a date of wedding, or equivalent. See vCard 4.0 ANNIVERSARY property as defined in section 6.2.6 of [RFC6350].
     *
     * @return true if this anniversary is a date of wedding, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.6">Section 2.6.2 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isWedding() { return kind.isWedding(); }

    /**
     * Tests if this is an undefined anniversary specified by the value of the "label" property.
     *
     * @return true if this is an undefined anniversary, false otherwise
     */
    @JsonIgnore
    public boolean isOtherAnniversary() { return kind.isExtValue(); }

    private static Anniversary anniversary(AnniversaryKind type, AnniversaryDate date, String label) {
        return Anniversary.builder().kind(type).date(date).build();
    }

}
