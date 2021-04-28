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
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasAltid;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class Address extends GroupableObject implements HasAltid, IdMapValue, Serializable {

    private static final String STREET_DETAILS_DELIMITER = " ";

    LocalizedString fullAddress;

    StreetDetailPair[] street;

    String locality;

    String region;

    String country;

    String postcode;

    @Pattern(regexp="[a-zA-Z]{2}", message = "invalid countryCode in Address")
    String countryCode;

    @Pattern(regexp="geo:([\\-0-9.]+),([\\-0-9.]+)(?:,([\\-0-9.]+))?(?:\\?(.*))?$", message = "invalid coordinates in Address")
    String coordinates;

    String timeZone;

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

    private boolean asContext(AddressContext context) { return contexts != null && contexts.containsKey(context); }
    public boolean asWork() { return asContext(AddressContext.WORK); }
    public boolean asPrivate() { return asContext(AddressContext.PRIVATE); }
    public boolean asBilling() { return asContext(AddressContext.BILLING); }
    public boolean asPostal() { return asContext(AddressContext.POSTAL); }
    public boolean asOtherContext() { return asContext(AddressContext.OTHER); }
    public boolean hasNoContext() { return contexts == null || contexts.size() ==  0; }

    private String getStreetDetail(StreetDetail detail) {

        if (street == null)
            return null;

        for (StreetDetailPair pair : street) {
            if (pair.isExtStreetDetail())
                continue;
            else if (pair.getType().getRfcValue() == detail)
                return pair.getValue();
        }

        return null;
    }

    @JsonIgnore
    public String getPostOfficeBox() {
        return getStreetDetail(StreetDetail.POST_OFFICE_BOX);
    }
    @JsonIgnore
    public String getStreetDetails() {
        StringJoiner joiner = new StringJoiner(STREET_DETAILS_DELIMITER);
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.NAME))) joiner.add(getStreetDetail(StreetDetail.NAME));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.NUMBER))) joiner.add(getStreetDetail(StreetDetail.NUMBER));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.DIRECTION))) joiner.add(getStreetDetail(StreetDetail.DIRECTION));
        String streetDetails = joiner.toString();
        return StringUtils.defaultIfEmpty(streetDetails, null);
    }
    @JsonIgnore
    public String getStreetExtensions() {
        StringJoiner joiner = new StringJoiner(STREET_DETAILS_DELIMITER);
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.BUILDING))) joiner.add("Building: " + getStreetDetail(StreetDetail.BUILDING));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.FLOOR))) joiner.add("Floor: " + getStreetDetail(StreetDetail.FLOOR));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.APARTMENT))) joiner.add("Apartment: " + getStreetDetail(StreetDetail.APARTMENT));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.ROOM))) joiner.add("Room: " + getStreetDetail(StreetDetail.ROOM));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.LANDMARK))) joiner.add("Landmark: " + getStreetDetail(StreetDetail.LANDMARK));
        if (StringUtils.isNotEmpty(getStreetDetail(StreetDetail.EXTENSION))) joiner.add(getStreetDetail(StreetDetail.EXTENSION));
        String streetExtensions = joiner.toString();
        return StringUtils.defaultIfEmpty(streetExtensions, null);
    }

}
