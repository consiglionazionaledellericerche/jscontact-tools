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
import org.apache.commons.lang3.SerializationUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"fullAddress"})
public class Address extends GroupableObject implements HasAltid, IdMapValue, Serializable {

    LocalizedString fullAddress;

    String street;

    String extension;

    String locality;

    String region;

    String country;

    String postOfficeBox;

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

    @Min(value=1, message = "invalid pref in Address - min value must be 1")
    @Max(value=100, message = "invalid pref in Address - max value must be 100")
    Integer pref;

    @JsonIgnore
    String altid;

    public Address clone() {
        return (Address) SerializationUtils.clone(this);
    }

}
