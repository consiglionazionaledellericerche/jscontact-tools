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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.AnniversaryDateDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.serializers.AnniversaryDateSerializer;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anniversary extends GroupableObject implements IdMapValue, Serializable {

    public static final String ANNIVERSAY_MARRIAGE_LABEL = "marriage date";

    @NotNull
    @Pattern(regexp = "Anniversary", message="invalid @type value in Anniversary")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Anniversary";

    @NotNull(message = "type is missing in Anniversary")
    @NonNull
    AnniversaryType type;

    String label;

    @NotNull(message = "date is missing in Anniversary")
    @NonNull
    @JsonSerialize(using = AnniversaryDateSerializer.class)
    @JsonDeserialize(using = AnniversaryDateDeserializer.class)
    AnniversaryDate date;

    @Valid
    Address place;

    /**
     * Tests if this anniversary is a birthday. See vCard BDAY property [RFC6350].
     * @return true if this anniversary is a birthday
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.5">Section 6.2.5 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isBirth() { return type == AnniversaryType.BIRTH; }

    /**
     * Tests if this anniversary is a date of death. See vCard DEATHDATE property [RFC6474].
     * @return true if this anniversary is a date of death
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6474#section-2.3">Section 2.3 of RFC6474</a>
     */
    @JsonIgnore
    public boolean isDeath() { return type == AnniversaryType.DEATH; }

    /**
     * Tests if this anniversary is a date of marriage, or equivalent. See vCard ANNIVERSARY property [RFC6350].
     * @return true if this anniversary is a date of marriage
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.6">Section 6.2.6 of RFC6350</a>
     */
    @JsonIgnore
    public boolean isMarriage() { return type == AnniversaryType.OTHER && label.equals(ANNIVERSAY_MARRIAGE_LABEL); }

    /**
     * Tests if this is an undefined anniversary specified by the value of the "label" property.
     * @return true if this is an undefined anniversary
     */
    @JsonIgnore
    public boolean isOtherAnniversary() { return type == AnniversaryType.OTHER; }

    private static Anniversary anniversary(AnniversaryType type, AnniversaryDate date, String label) {
        return Anniversary.builder().type(type).date(date).label(label).build();
    }

    /**
     * Creates a birthday anniversary. See vCard BDAY property [RFC6350].
     * @return an object representing a birthday
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.5">Section 6.2.5 of RFC6350</a>
     */
    public static Anniversary birth(String date) { return anniversary(AnniversaryType.BIRTH, AnniversaryDate.parse(date), null);}

    /**
     * Creates a date of death anniversary. See vCard DEATHDATE property [RFC6474].
     * @return an object representing a date of death
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6474#section-2.3">Section 2.3 of RFC6474</a>
     */
    public static Anniversary death(String date) { return anniversary(AnniversaryType.DEATH, AnniversaryDate.parse(date), null);}

    /**
     * Creates a date of marriage, or equivalent, anniversary. See vCard ANNIVERSARY property [RFC6350].
     * @return an object representing a date of marriage
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.6">Section 6.2.6 of RFC6350</a>
     */
    public static Anniversary marriage(String date) { return anniversary(AnniversaryType.OTHER, AnniversaryDate.parse(date), ANNIVERSAY_MARRIAGE_LABEL);}

    /**
     * Creates an anniversary other than birthday, date of death, date of marriage.
     * @return an object representing an anniversary is other than birthday, date of death, date of marriage
     */
    public static Anniversary otherAnniversary(String date, String label) { return anniversary(AnniversaryType.OTHER, AnniversaryDate.parse(date), label);}

}
