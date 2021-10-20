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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.StreetComponentTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.StreetComponentTypeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the StreetComponent type as defined in section 2.4.1 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.4.1">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreetComponent implements Serializable {

    @NotNull
    @Pattern(regexp = "StreetComponent", message="invalid @type value in StreetComponent")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "StreetComponent";

    @NotNull
    @NonNull
    @JsonSerialize(using = StreetComponentTypeSerializer.class)
    @JsonDeserialize(using = StreetComponentTypeDeserializer.class)
    StreetComponentType type;

    @NotNull
    @NonNull
    String value;

    private boolean isRfc(StreetComponentEnum value) { return (type.getRfcValue()!= null && type.getRfcValue() == value);}

    /**
     * Tests if this is the street name.
     *
     * @return true if this is the street name, false otherwise
     */
    @JsonIgnore
    public boolean isName() { return isRfc(StreetComponentEnum.NAME); }

    /**
     * Tests if this is the street number.
     *
     * @return true if this is the street number, false otherwise
     */
    @JsonIgnore
    public boolean isNumber() { return isRfc(StreetComponentEnum.NUMBER); }

    /**
     * Tests if this is the cardinal direction.
     *
     * @return true if this is the cardinal direction, false otherwise
     */
    @JsonIgnore
    public boolean isDirection() { return isRfc(StreetComponentEnum.DIRECTION); }

    /**
     * Tests if this is the building or building part.
     *
     * @return true if this is the building or building part, false otherwise
     */
    @JsonIgnore
    public boolean isBuilding() { return isRfc(StreetComponentEnum.BUILDING); }

    /**
     * Tests if this is the floor number.
     *
     * @return true if this is the floor number, false otherwise
     */
    @JsonIgnore
    public boolean isFloor() { return isRfc(StreetComponentEnum.FLOOR); }

    /**
     * Tests if this is the apartment number or identifier.
     *
     * @return true if this is the apartment number or identifier, false otherwise
     */
    @JsonIgnore
    public boolean isApartment() { return isRfc(StreetComponentEnum.APARTMENT); }

    /**
     * Tests if this is room number or identifier.
     *
     * @return true if this is the room number or identifier, false otherwise
     */
    @JsonIgnore
    public boolean isRoom() { return isRfc(StreetComponentEnum.ROOM); }

    /**
     * Tests if this is an extension designation or box number.
     *
     * @return true if this is an extension, false otherwise
     */
    @JsonIgnore
    public boolean isExtension() { return isRfc(StreetComponentEnum.EXTENSION); }

    /**
     * Tests if this is the post office box number or identifier.
     *
     * @return true if this is the the post office box number or identifier, false otherwise
     */
    @JsonIgnore
    public boolean isPostOfficeBox() { return isRfc(StreetComponentEnum.POST_OFFICE_BOX); }

    /**
     * Tests if this is the separator for two street components used to build the full address.
     *
     * @return true if this is the separator, false otherwise
     */
    @JsonIgnore
    public boolean isSeparator() { return isRfc(StreetComponentEnum.SEPARATOR); }

    /**
     * Tests if this is a street component whose type is unknown.
     *
     * @return true if this is an unknown street component, false otherwise
     */
    @JsonIgnore
    public boolean isUnknown() { return isRfc(StreetComponentEnum.UNKNOWN); }

    /**
     * Tests if this is a custom street component.
     *
     * @return true if this is a custom street component, false otherwise
     */
    @JsonIgnore
    public boolean isExt() { return type.isExtValue(); }

    private static StreetComponent rfc(StreetComponentEnum rfcValue, String value) {
        return StreetComponent.builder()
                .value(value)
                .type(StreetComponentType.builder().rfcValue(rfcValue).build())
                .build();
    }

    /**
     * Creates a name component of a street address.
     *
     * @return the name component
     */
    public static StreetComponent name(String value) { return rfc(StreetComponentEnum.NAME, value);}
    /**
     * Creates a number component of a street address.
     *
     * @return the number component
     */
    public static StreetComponent number(String value) { return rfc(StreetComponentEnum.NUMBER, value);}
    /**
     * Creates a direction component of a street address.
     *
     * @return the direction component
     */
    public static StreetComponent direction(String value) { return rfc(StreetComponentEnum.DIRECTION, value);}
    /**
     * Creates a building component of a street address.
     *
     * @return the building component
     */
    public static StreetComponent building(String value) { return rfc(StreetComponentEnum.BUILDING, value);}
    /**
     * Creates a floor component of a street address.
     *
     * @return the floor component
     */
    public static StreetComponent floor(String value) { return rfc(StreetComponentEnum.FLOOR, value);}
    /**
     * Creates an apartment component of a street address.
     *
     * @return the apartment component
     */
    public static StreetComponent apartment(String value) { return rfc(StreetComponentEnum.APARTMENT, value);}
    /**
     * Creates a room component of a street address.
     *
     * @return the room component
     */
    public static StreetComponent room(String value) { return rfc(StreetComponentEnum.ROOM, value);}
    /**
     * Creates an extension component of a street address.
     *
     * @return the extension component
     */
    public static StreetComponent extension(String value) { return rfc(StreetComponentEnum.EXTENSION, value);}
    /**
     * Creates a P.O. component of a street address.
     *
     * @return the P.O. component
     */
    public static StreetComponent postOfficeBox(String value) { return rfc(StreetComponentEnum.POST_OFFICE_BOX, value);}
    /**
     * Creates a separator component of a street address.
     * @return the separator component
     */
    public static StreetComponent separator(String value) { return rfc(StreetComponentEnum.SEPARATOR, value);}
    /**
     * Creates an unknown component of a street address.
     *
     * @return the unknown component
     */
    public static StreetComponent unknown(String value) { return rfc(StreetComponentEnum.UNKNOWN, value);}
    /**
     * Creates a custom component of a street address.
     *
     * @return the custom component
     */
    public static StreetComponent ext(String extValue, String value) {
        return StreetComponent.builder()
                .value(value)
                .type(StreetComponentType.builder().extValue(extValue).build())
                .build();
    }

}
