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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.AddressComponentKindDeserializer;

import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsComponent;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the AddressComponent type as defined in section 2.5.1.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.5.1.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","kind", "value", "phonetic"})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AddressComponent extends AbstractJSContactType implements HasKind, IsComponent, IsIANAType, Serializable {

    @Pattern(regexp = "AddressComponent", message="invalid @type value in AddressComponent")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "AddressComponent";

    @NotNull(message = "kind is missing in AddressComponent")
    @NonNull
    @JsonDeserialize(using = AddressComponentKindDeserializer.class)
    @ContainsExtensibleEnum(enumClass = AddressComponentEnum.class, getMethod = "getKind")
    AddressComponentKind kind;

    @NotNull(message = "value is missing in AddressComponent")
    @NonNull
    String value;

    String phonetic;

    private boolean isRfc(AddressComponentEnum value) { return (kind.getRfcValue()!= null && kind.getRfcValue() == value);}

    /**
     * Tests if this is the address locality.
     *
     * @return true if this is the address locality, false otherwise
     */
    @JsonIgnore
    public boolean isLocality() { return isRfc(AddressComponentEnum.LOCALITY); }

    /**
     * Tests if this is the address region.
     *
     * @return true if this is the address region, false otherwise
     */
    @JsonIgnore
    public boolean isRegion() { return isRfc(AddressComponentEnum.REGION); }

    /**
     * Tests if this is the address country.
     *
     * @return true if this is the address country, false otherwise
     */
    @JsonIgnore
    public boolean isCountry() { return isRfc(AddressComponentEnum.COUNTRY); }

    /**
     * Tests if this is the postcode country.
     *
     * @return true if this is the postcode country, false otherwise
     */
    @JsonIgnore
    public boolean isPostcode() { return isRfc(AddressComponentEnum.POSTCODE); }

    /**
     * Tests if this is the address district.
     *
     * @return true if this is the address district, false otherwise
     */
    @JsonIgnore
    public boolean isDistrict() { return isRfc(AddressComponentEnum.DISTRICT); }

    /**
     * Tests if this is the address subdistrict.
     *
     * @return true if this is the address subdistrict, false otherwise
     */
    @JsonIgnore
    public boolean isSubdistrict() { return isRfc(AddressComponentEnum.SUBDISTRICT); }

    /**
     * Tests if this is the address block.
     *
     * @return true if this is the address block, false otherwise
     */
    @JsonIgnore
    public boolean isBlock() { return isRfc(AddressComponentEnum.BLOCK); }
    /**
     * Tests if this is the street name.
     *
     * @return true if this is the street name, false otherwise
     */
    @JsonIgnore
    public boolean isName() { return isRfc(AddressComponentEnum.NAME); }

    /**
     * Tests if this is the street number.
     *
     * @return true if this is the street number, false otherwise
     */
    @JsonIgnore
    public boolean isNumber() { return isRfc(AddressComponentEnum.NUMBER); }

    /**
     * Tests if this is the cardinal direction.
     *
     * @return true if this is the cardinal direction, false otherwise
     */
    @JsonIgnore
    public boolean isDirection() { return isRfc(AddressComponentEnum.DIRECTION); }

    /**
     * Tests if this is the building or building part.
     *
     * @return true if this is the building or building part, false otherwise
     */
    @JsonIgnore
    public boolean isBuilding() { return isRfc(AddressComponentEnum.BUILDING); }

    /**
     * Tests if this is the floor number.
     *
     * @return true if this is the floor number, false otherwise
     */
    @JsonIgnore
    public boolean isFloor() { return isRfc(AddressComponentEnum.FLOOR); }

    /**
     * Tests if this is the apartment number or identifier.
     *
     * @return true if this is the apartment number or identifier, false otherwise
     */
    @JsonIgnore
    public boolean isApartment() { return isRfc(AddressComponentEnum.APARTMENT); }

    /**
     * Tests if this is room number or identifier.
     *
     * @return true if this is the room number or identifier, false otherwise
     */
    @JsonIgnore
    public boolean isRoom() { return isRfc(AddressComponentEnum.ROOM); }

    /**
     * Tests if this is the landmarkr.
     *
     * @return true if this is the landmark, false otherwise
     */
    @JsonIgnore
    public boolean isLandmark() { return isRfc(AddressComponentEnum.LANDMARK); }

    /**
     * Tests if this is the P.O. box number or identifier.
     *
     * @return true if this is the P.O. box number or identifier, false otherwise
     */
    @JsonIgnore
    public boolean isPostOfficeBox() { return isRfc(AddressComponentEnum.POST_OFFICE_BOX); }

    /**
     * Tests if this is the separator for street components used to build the full address.
     *
     * @return true if this is the separator, false otherwise
     */
    @JsonIgnore
    public boolean isSeparator() { return isRfc(AddressComponentEnum.SEPARATOR); }

    /**
     * Tests if this is a custom address component.
     *
     * @return true if this is a custom address component, false otherwise
     */
    @JsonIgnore
    public boolean isExt() { return kind.isExtValue(); }

    private static AddressComponent rfc(AddressComponentEnum rfcValue, String value, String phonetic) {
        return AddressComponent.builder()
                .value(value)
                .phonetic(phonetic)
                .kind(AddressComponentKind.builder().rfcValue(rfcValue).build())
                .build();
    }

    /**
     * Returns a locality component of an address with phonetic.
     *
     * @param value the address locality
     * @param phonetic the phonetic
     * @return the locality component
     */
    public static AddressComponent locality(String value, String phonetic) { return rfc(AddressComponentEnum.LOCALITY, value, phonetic);}
    /**
     * Returns a locality component of an address.
     *
     * @param value the address locality
     * @return the locality component
     */
    public static AddressComponent locality(String value) { return AddressComponent.locality(value, null);}
    /**
     * Returns a region component of an address with phonetic.
     *
     * @param value the address region
     * @param phonetic the phonetic
     * @return the region component
     */
    public static AddressComponent region(String value, String phonetic) { return rfc(AddressComponentEnum.REGION, value, phonetic);}
    /**
     * Returns a region component of an address.
     *
     * @param value the address region
     * @return the region component
     */
    public static AddressComponent region(String value) { return AddressComponent.region(value, null);}
    /**
     * Returns a country component of an address with phonetic.
     *
     * @param value the address country
     * @param phonetic the phonetic
     * @return the country component
     */
    public static AddressComponent country(String value, String phonetic) { return rfc(AddressComponentEnum.COUNTRY, value, phonetic);}
    /**
     * Returns a country component of an address.
     *
     * @param value the address country
     * @return the country component
     */
    public static AddressComponent country(String value) { return AddressComponent.country(value, null);}
    /**
     * Returns a postcode component of an address.
     *
     * @param value the address postcode
     * @return the postcode component
     */
    public static AddressComponent postcode(String value) { return rfc(AddressComponentEnum.POSTCODE, value, null);}
    /**
     * Returns a district component of an address with phonetic.
     *
     * @param value the address district
     * @param phonetic the phonetic
     * @return the district component
     */
    public static AddressComponent district(String value, String phonetic) { return rfc(AddressComponentEnum.DISTRICT, value, phonetic);}
    /**
     * Returns a district component of an address.
     *
     * @param value the address district
     * @return the district component
     */
    public static AddressComponent district(String value) { return AddressComponent.district(value, null);}
    /**
     * Returns a subdistrict component of an address with phonetic.
     *
     * @param value the address subdistrict
     * @param phonetic the phonetic
     * @return the subdistrict component
     */
    public static AddressComponent subdistrict(String value, String phonetic) { return rfc(AddressComponentEnum.SUBDISTRICT, value, phonetic);}
    /**
     * Returns a subdistrict component of an address.
     *
     * @param value the address subdistrict
     * @return the subdistrict component
     */
    public static AddressComponent subdistrict(String value) { return AddressComponent.subdistrict(value, null);}
    /**
     * Returns a block component of an address with phonetic.
     *
     * @param value the address block
     * @param phonetic the phonetic
     * @return the block component
     */
    public static AddressComponent block(String value, String phonetic) { return rfc(AddressComponentEnum.BLOCK, value, phonetic);}
    /**
     * Returns a block component of an address.
     *
     * @param value the address block
     * @return the block component
     */
    public static AddressComponent block(String value) { return AddressComponent.block(value, null);}
    /**
     * Returns a name component of an address with phonetic.
     *
     * @param value the address name
     * @param phonetic the phonetic
     * @return the name component
     */
    public static AddressComponent name(String value, String phonetic) { return rfc(AddressComponentEnum.NAME, value, phonetic);}
    /**
     * Returns a name component of an address.
     *
     * @param value the address name
     * @return the name component
     */
    public static AddressComponent name(String value) { return AddressComponent.name(value, null);}
    /**
     * Returns a number component of an address.
     *
     * @param value the address number
     * @return the number component
     */
    public static AddressComponent number(String value) { return rfc(AddressComponentEnum.NUMBER, value, null);}
    /**
     * Returns a direction component of an address.
     *
     * @param value the address direction
     * @return the direction component
     */
    public static AddressComponent direction(String value) { return rfc(AddressComponentEnum.DIRECTION, value, null);}
    /**
     * Returns a building component of an address with phonetic.
     *
     * @param value the building number
     * @param phonetic the phonetic
     * @return the building component
     */
    public static AddressComponent building(String value, String phonetic) { return rfc(AddressComponentEnum.BUILDING, value, phonetic);}
    /**
     * Returns a building component of an address.
     *
     * @param value the building number
     * @return the building component
     */
    public static AddressComponent building(String value) { return AddressComponent.building(value, null);}
    /**
     * Returns a floor component of an address.
     *
     * @param value the floor number
     * @return the floor component
     */
    public static AddressComponent floor(String value) { return rfc(AddressComponentEnum.FLOOR, value, null);}
    /**
     * Returns an apartment component of an address.
     *
     * @param value the apartment number
     * @return the apartment component
     */
    public static AddressComponent apartment(String value) { return rfc(AddressComponentEnum.APARTMENT, value, null);}
    /**
     * Returns a room component of an address.
     *
     * @param value the room number
     * @return the room component
     */
    public static AddressComponent room(String value) { return rfc(AddressComponentEnum.ROOM, value, null);}
    /**
     * Returns a landmark component of an address with phonetic.
     *
     * @param value the landmark
     * @param phonetic the phonetic
     * @return the landmark component
     */
    public static AddressComponent landmark(String value, String phonetic) { return rfc(AddressComponentEnum.LANDMARK, value, phonetic);}
    /**
     * Returns a landmark component of an address.
     *
     * @param value the landmark
     * @return the landmark component
     */
    public static AddressComponent landmark(String value) { return AddressComponent.landmark(value, null);}
    /**
     * Returns a P.O. box component of an address.
     *
     * @param value the P.O. box number
     * @return the P.O. box component
     */
    public static AddressComponent postOfficeBox(String value) { return rfc(AddressComponentEnum.POST_OFFICE_BOX, value, null);}
    /**
     * Returns a separator component of an address.
     *
     * @param value the separator
     * @return the separator component
     */
    public static AddressComponent separator(String value) { return rfc(AddressComponentEnum.SEPARATOR, value, null);}
    /**
     * Returns a custom component of an address with phonetic.
     *
     * @param extValue the custom address component
     * @param value the value for the custom address component
     * @param phonetic the phonetic
     * @return the custom component
     */
    public static AddressComponent ext(String extValue, String value, String phonetic) {
        return AddressComponent.builder()
                .value(value)
                .phonetic(phonetic)
                .kind(AddressComponentKind.builder().extValue(V_Extension.toV_Extension(extValue)).build())
                .build();
    }
    /**
     * Returns a custom component of an address.
     *
     * @param extValue the custom address component
     * @param value the value for the custom address component
     * @return the custom component
     */
    public static AddressComponent ext(String extValue, String value) {
        return AddressComponent.ext(extValue, value, null);
    }
}
