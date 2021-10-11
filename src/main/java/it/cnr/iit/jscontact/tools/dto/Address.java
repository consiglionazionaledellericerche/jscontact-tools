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
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.AddressContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.serializers.AddressContextsSerializer;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;
import java.util.StringJoiner;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"fullAddress"})
public class Address extends GroupableObject implements HasAltid, IdMapValue, Serializable, Comparable<Address> {

    private static final String STREET_DETAILS_DELIMITER = " ";

    @NotNull
    @Pattern(regexp = "Address", message="invalid @type value in Address")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Address";

    String fullAddress;

    StreetComponent[] street;

    String locality;

    String region;

    String country;

    String postcode;

    @Pattern(regexp="[a-zA-Z]{2}", message = "invalid countryCode in Address")
    String countryCode;

    @Pattern(regexp="geo:([\\-0-9.]+),([\\-0-9.]+)(?:,([\\-0-9.]+))?(?:\\?(.*))?$", message = "invalid coordinates in Address")
    String coordinates;

    String timeZone;

    @JsonSerialize(using = AddressContextsSerializer.class)
    @JsonDeserialize(using = AddressContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<AddressContext,Boolean> contexts in Address - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<AddressContext,Boolean> contexts;

    String label;

    @Min(value=1, message = "invalid pref in Address - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Address - value must be less or equal than 100")
    Integer pref;

    @JsonIgnore
    String altid;

    @JsonIgnore
    String language;

    @JsonIgnore
    Boolean isDefaultLanguage;

    private boolean asContext(AddressContext context) { return contexts != null && contexts.containsKey(context); }
    public boolean asWork() { return asContext(AddressContext.work()); }
    public boolean asPrivate() { return asContext(AddressContext.private_()); }
    public boolean asBilling() { return asContext(AddressContext.billing()); }
    public boolean asPostal() { return asContext(AddressContext.postal()); }
    public boolean asOtherContext() { return asContext(AddressContext.other()); }
    public boolean asExtContext(String extValue) { return asContext(AddressContext.ext(extValue)); }
    public boolean hasNoContext() { return contexts == null || contexts.size() ==  0; }

    private String getStreetDetail(StreetComponentEnum detail) {

        if (street == null)
            return null;

        for (StreetComponent pair : street) {
            if (pair.isExt())
                continue;
            else if (pair.getType().getRfcValue() == detail)
                return pair.getValue();
        }

        return null;
    }

    @JsonIgnore
    public String getPostOfficeBox() {
        return getStreetDetail(StreetComponentEnum.POST_OFFICE_BOX);
    }
    @JsonIgnore
    public String getStreetDetails() {
        String separator = getStreetDetail(StreetComponentEnum.SEPARATOR);
        StringJoiner joiner = new StringJoiner( (separator != null) ? separator : STREET_DETAILS_DELIMITER);
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.NAME))) joiner.add(getStreetDetail(StreetComponentEnum.NAME));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.NUMBER))) joiner.add(getStreetDetail(StreetComponentEnum.NUMBER));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.DIRECTION))) joiner.add(getStreetDetail(StreetComponentEnum.DIRECTION));
        String streetDetails = joiner.toString();
        return StringUtils.defaultIfEmpty(streetDetails, null);
    }
    @JsonIgnore
    public String getStreetExtensions() {
        String separator = getStreetDetail(StreetComponentEnum.SEPARATOR);
        StringJoiner joiner = new StringJoiner( (separator != null) ? separator : STREET_DETAILS_DELIMITER);
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.BUILDING))) joiner.add("Building: " + getStreetDetail(StreetComponentEnum.BUILDING));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.FLOOR))) joiner.add("Floor: " + getStreetDetail(StreetComponentEnum.FLOOR));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.APARTMENT))) joiner.add("Apartment: " + getStreetDetail(StreetComponentEnum.APARTMENT));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.ROOM))) joiner.add("Room: " + getStreetDetail(StreetComponentEnum.ROOM));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.EXTENSION))) joiner.add(getStreetDetail(StreetComponentEnum.EXTENSION));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetComponentEnum.UNKNOWN))) joiner.add(getStreetDetail(StreetComponentEnum.EXTENSION));
        String streetExtensions = joiner.toString();
        return StringUtils.defaultIfEmpty(streetExtensions, null);
    }

    @Override
    public int compareTo(Address o) {

        if (altid == null) {
            if (o.getAltid() == null)
                return 0;
            else
                return -1;
        }
        else {
            if (o.getAltid() == null)
                return 1;
        }

        if (altid.equals(o.getAltid())) {
            if (isDefaultLanguage)
                return -1;
            else
                return 0;
        }

        return StringUtils.compare(altid,o.getAltid());

    }

}
