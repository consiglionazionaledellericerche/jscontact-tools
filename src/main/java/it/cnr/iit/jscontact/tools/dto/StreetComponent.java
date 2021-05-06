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

    private boolean isRfcStreetComponent(StreetComponentEnum value) { return (type.getRfcValue()!= null && type.getRfcValue() == value);};
    @JsonIgnore
    public boolean isName() { return isRfcStreetComponent(StreetComponentEnum.NAME); }
    @JsonIgnore
    public boolean isNumber() { return isRfcStreetComponent(StreetComponentEnum.NUMBER); }
    @JsonIgnore
    public boolean isDirection() { return isRfcStreetComponent(StreetComponentEnum.DIRECTION); }
    @JsonIgnore
    public boolean isBuilding() { return isRfcStreetComponent(StreetComponentEnum.BUILDING); }
    @JsonIgnore
    public boolean isFloor() { return isRfcStreetComponent(StreetComponentEnum.FLOOR); }
    @JsonIgnore
    public boolean isApartment() { return isRfcStreetComponent(StreetComponentEnum.APARTMENT); }
    @JsonIgnore
    public boolean isRoom() { return isRfcStreetComponent(StreetComponentEnum.ROOM); }
    @JsonIgnore
    public boolean isExtension() { return isRfcStreetComponent(StreetComponentEnum.EXTENSION); }
    @JsonIgnore
    public boolean isPostOfficeBox() { return isRfcStreetComponent(StreetComponentEnum.POST_OFFICE_BOX); }
    @JsonIgnore
    public boolean isLandmark() { return isRfcStreetComponent(StreetComponentEnum.LANDMARK); }
    @JsonIgnore
    public boolean isSeparator() { return isRfcStreetComponent(StreetComponentEnum.SEPARATOR); }
    @JsonIgnore
    public boolean isUnknown() { return isRfcStreetComponent(StreetComponentEnum.UNKNOWN); }
    @JsonIgnore
    public boolean isExtStreetComponent() { return type.getExtValue() != null; }

    private static StreetComponent streetComponent(StreetComponentEnum rfcEnum, String value) {
        return StreetComponent.builder()
                .value(value)
                .type(StreetComponentType.builder().rfcValue(rfcEnum).build())
                .build();
    }
    public static StreetComponent name(String value) { return streetComponent(StreetComponentEnum.NAME, value);}
    public static StreetComponent number(String value) { return streetComponent(StreetComponentEnum.NUMBER, value);}
    public static StreetComponent direction(String value) { return streetComponent(StreetComponentEnum.DIRECTION, value);}
    public static StreetComponent building(String value) { return streetComponent(StreetComponentEnum.BUILDING, value);}
    public static StreetComponent floor(String value) { return streetComponent(StreetComponentEnum.FLOOR, value);}
    public static StreetComponent apartment(String value) { return streetComponent(StreetComponentEnum.APARTMENT, value);}
    public static StreetComponent room(String value) { return streetComponent(StreetComponentEnum.ROOM, value);}
    public static StreetComponent extension(String value) { return streetComponent(StreetComponentEnum.EXTENSION, value);}
    public static StreetComponent postOfficeBox(String value) { return streetComponent(StreetComponentEnum.POST_OFFICE_BOX, value);}
    public static StreetComponent landmark(String value) { return streetComponent(StreetComponentEnum.LANDMARK, value);}
    public static StreetComponent separator(String value) { return streetComponent(StreetComponentEnum.SEPARATOR, value);}
    public static StreetComponent unknown(String value) { return streetComponent(StreetComponentEnum.UNKNOWN, value);}
    public static StreetComponent extStreetComponent(String extEnum, String value) {
        return StreetComponent.builder()
                .value(value)
                .type(StreetComponentType.builder().extValue(extEnum).build())
                .build();
    }

}
