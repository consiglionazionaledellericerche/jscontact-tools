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
import it.cnr.iit.jscontact.tools.dto.deserializers.StreetDetailTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.StreetDetailTypeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreetDetailPair implements Serializable {

    @NotNull
    @NonNull
    @JsonSerialize(using = StreetDetailTypeSerializer.class)
    @JsonDeserialize(using = StreetDetailTypeDeserializer.class)
    StreetDetailType type;

    @NotNull
    @NonNull
    String value;

    private boolean isRfcStreetDetail(StreetDetail rfcType) { return (type.getRfcValue()!= null && type.getRfcValue() == rfcType);};
    @JsonIgnore
    public boolean isName() { return isRfcStreetDetail(StreetDetail.NAME); }
    @JsonIgnore
    public boolean isNumber() { return isRfcStreetDetail(StreetDetail.NUMBER); }
    @JsonIgnore
    public boolean isApartment() { return isRfcStreetDetail(StreetDetail.APARTMENT); }
    @JsonIgnore
    public boolean isBuilding() { return isRfcStreetDetail(StreetDetail.BUILDING); }
    @JsonIgnore
    public boolean isDirection() { return isRfcStreetDetail(StreetDetail.DIRECTION); }
    @JsonIgnore
    public boolean isExtension() { return isRfcStreetDetail(StreetDetail.EXTENSION); }
    @JsonIgnore
    public boolean isFloor() { return isRfcStreetDetail(StreetDetail.FLOOR); }
    @JsonIgnore
    public boolean isLandmark() { return isRfcStreetDetail(StreetDetail.LANDMARK); }
    @JsonIgnore
    public boolean isPostOfficeBox() { return isRfcStreetDetail(StreetDetail.POST_OFFICE_BOX); }
    @JsonIgnore
    public boolean isRoom() { return isRfcStreetDetail(StreetDetail.ROOM); }
    @JsonIgnore
    public boolean isExtStreetDetail() { return type.getExtValue() != null; }

}
