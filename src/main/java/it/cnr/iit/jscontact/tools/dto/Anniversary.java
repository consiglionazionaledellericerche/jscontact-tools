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
import it.cnr.iit.jscontact.tools.dto.deserializers.AnniversaryDateDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.AnniversaryDateSerializer;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anniversary extends GroupableObject implements Serializable {

    public static final String ANNIVERSAY_MARRIAGE_LABEL = "marriage date";

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

    @JsonIgnore
    public boolean isBirth() { return type == AnniversaryType.BIRTH; }
    @JsonIgnore
    public boolean isDeath() { return type == AnniversaryType.DEATH; }
    @JsonIgnore
    public boolean isMarriage() { return type == AnniversaryType.OTHER && label.equals(ANNIVERSAY_MARRIAGE_LABEL); }
    @JsonIgnore
    public boolean isOtherAnniversary() { return type == AnniversaryType.OTHER; }

    private static Anniversary anniversary(AnniversaryType type, AnniversaryDate date, String label) {
        return Anniversary.builder().type(type).date(date).label(label).build();
    }
    public static Anniversary birth(String date) { return anniversary(AnniversaryType.BIRTH, AnniversaryDate.parse(date), null);}
    public static Anniversary death(String date) { return anniversary(AnniversaryType.DEATH, AnniversaryDate.parse(date), null);}
    public static Anniversary marriage(String date) { return anniversary(AnniversaryType.OTHER, AnniversaryDate.parse(date), ANNIVERSAY_MARRIAGE_LABEL);}
    public static Anniversary otherAnniversary(String date, String label) { return anniversary(AnniversaryType.OTHER, AnniversaryDate.parse(date), label);}

}
