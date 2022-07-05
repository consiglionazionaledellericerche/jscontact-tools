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
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.serializers.AnniversaryDateSerializer;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the Anniversary type as defined in section 2.6.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","type","date","place","label"})
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

    AnniversaryType type;

    @NotNull(message = "date is missing in Anniversary")
    @NonNull
    @JsonSerialize(using = AnniversaryDateSerializer.class)
    @JsonDeserialize(using = AnniversaryDateDeserializer.class)
    AnniversaryDate date;

    @Valid
    Address place;

    String label;

    /**
     * Tests if this anniversary is a birthday. See vCard 4.0 BDAY property as defined in section 6.2.5 of [RFC6350].
     *
     * @return true if this anniversary is a birthday, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.5">RFC6350</a>
     */
    @JsonIgnore
    public boolean isBirth() { return type == AnniversaryType.BIRTH; }

    /**
     * Tests if this anniversary is a date of death. See vCard 4.0 DEATHDATE property as defined in section 6.2.5 of [RFC6474].
     *
     * @return true if this anniversary is a date of death, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6474#section-2.3">RFC6474</a>
     */
    @JsonIgnore
    public boolean isDeath() { return type == AnniversaryType.DEATH; }

    /**
     * Tests if this anniversary is a date of marriage, or equivalent. See vCard 4.0 ANNIVERSARY property [as defined in section 6.2.6 of [RFC6350].
     *
     * @return true if this anniversary is a date of marriage, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.6">RFC6350</a>
     */
    @JsonIgnore
    public boolean isMarriage() { return type == null && label.equals(ANNIVERSAY_MARRIAGE_LABEL); }

    /**
     * Tests if this is an undefined anniversary specified by the value of the "label" property.
     *
     * @return true if this is an undefined anniversary, false otherwise
     */
    @JsonIgnore
    public boolean isOtherAnniversary() { return type == null; }

    private static Anniversary anniversary(AnniversaryType type, AnniversaryDate date, String label) {
        return Anniversary.builder().type(type).date(date).label(label).build();
    }

    /**
     * Returns a birthday anniversary. See vCard 4.0 BDAY property as defined in section 6.2.5 of [RFC6350].
     *
     * @param date the birthday in text format
     * @return a birthday anniversary
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.5">RFC6350</a>
     */
    public static Anniversary birth(String date) { return anniversary(AnniversaryType.BIRTH, AnniversaryDate.parse(date), null);}

    /**
     * Returns a date of death anniversary. See vCard 4.0 DEATHDATE property as defined in section 2.3 of [RFC6474].
     *
     * @param date the death date in text format
     * @return a date of death anniversary
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6474#section-2.3">RFC6474</a>
     */
    public static Anniversary death(String date) { return anniversary(AnniversaryType.DEATH, AnniversaryDate.parse(date), null);}

    /**
     * Returns a date of marriage, or equivalent, anniversary. See vCard 4.0 ANNIVERSARY property as defined in section 6.2.6 of [RFC6350].
     *
     * @param date the marriage date in text format
     * @return a date of marriage anniversary
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.6">RFC6350</a>
     */
    public static Anniversary marriage(String date) { return anniversary(null, AnniversaryDate.parse(date), ANNIVERSAY_MARRIAGE_LABEL);}

    /**
     * Returns an anniversary other than birthday, date of death, date of marriage.
     *
     * @param date the anniversary date in text format
     * @param label a text specifying the anniversary
     * @return an anniversary other than birthday, date of death, date of marriage
     */
    public static Anniversary otherAnniversary(String date, String label) { return anniversary(null, AnniversaryDate.parse(date), label);}

}
