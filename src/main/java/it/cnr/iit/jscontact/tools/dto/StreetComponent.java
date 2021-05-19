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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.StreetComponentTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.StreetComponentTypeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreetComponent implements Serializable {

    @NotNull
    @NonNull
    @JsonSerialize(using = StreetComponentTypeSerializer.class)
    @JsonDeserialize(using = StreetComponentTypeDeserializer.class)
    StreetComponentType type;

    @NotNull
    @NonNull
    String value;

    private boolean isRfc(StreetComponentEnum value) { return (type.getRfcValue()!= null && type.getRfcValue() == value);};
    @JsonIgnore
    public boolean isName() { return isRfc(StreetComponentEnum.NAME); }
    @JsonIgnore
    public boolean isNumber() { return isRfc(StreetComponentEnum.NUMBER); }
    @JsonIgnore
    public boolean isDirection() { return isRfc(StreetComponentEnum.DIRECTION); }
    @JsonIgnore
    public boolean isBuilding() { return isRfc(StreetComponentEnum.BUILDING); }
    @JsonIgnore
    public boolean isFloor() { return isRfc(StreetComponentEnum.FLOOR); }
    @JsonIgnore
    public boolean isApartment() { return isRfc(StreetComponentEnum.APARTMENT); }
    @JsonIgnore
    public boolean isRoom() { return isRfc(StreetComponentEnum.ROOM); }
    @JsonIgnore
    public boolean isExtension() { return isRfc(StreetComponentEnum.EXTENSION); }
    @JsonIgnore
    public boolean isPostOfficeBox() { return isRfc(StreetComponentEnum.POST_OFFICE_BOX); }
    @JsonIgnore
    public boolean isSeparator() { return isRfc(StreetComponentEnum.SEPARATOR); }
    @JsonIgnore
    public boolean isUnknown() { return isRfc(StreetComponentEnum.UNKNOWN); }
    @JsonIgnore
    public boolean isExt() { return type.isExtValue(); }

    private static StreetComponent rfc(StreetComponentEnum rfcValue, String value) {
        return StreetComponent.builder()
                .value(value)
                .type(StreetComponentType.builder().rfcValue(rfcValue).build())
                .build();
    }
    public static StreetComponent name(String value) { return rfc(StreetComponentEnum.NAME, value);}
    public static StreetComponent number(String value) { return rfc(StreetComponentEnum.NUMBER, value);}
    public static StreetComponent direction(String value) { return rfc(StreetComponentEnum.DIRECTION, value);}
    public static StreetComponent building(String value) { return rfc(StreetComponentEnum.BUILDING, value);}
    public static StreetComponent floor(String value) { return rfc(StreetComponentEnum.FLOOR, value);}
    public static StreetComponent apartment(String value) { return rfc(StreetComponentEnum.APARTMENT, value);}
    public static StreetComponent room(String value) { return rfc(StreetComponentEnum.ROOM, value);}
    public static StreetComponent extension(String value) { return rfc(StreetComponentEnum.EXTENSION, value);}
    public static StreetComponent postOfficeBox(String value) { return rfc(StreetComponentEnum.POST_OFFICE_BOX, value);}
    public static StreetComponent separator(String value) { return rfc(StreetComponentEnum.SEPARATOR, value);}
    public static StreetComponent unknown(String value) { return rfc(StreetComponentEnum.UNKNOWN, value);}
    public static StreetComponent ext(String extValue, String value) {
        return StreetComponent.builder()
                .value(value)
                .type(StreetComponentType.builder().extValue(extValue).build())
                .build();
    }

}
