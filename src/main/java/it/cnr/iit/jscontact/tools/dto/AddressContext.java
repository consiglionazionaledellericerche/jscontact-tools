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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class mapping the keys of "contexts" map of the Address type as defined in section 2.4.1 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.4.1">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddressContext extends ExtensibleEnum<AddressContextEnum> implements Serializable {

    private boolean isRfc(AddressContextEnum value) { return isRfcValue() && rfcValue == value; }
    @JsonIgnore
    public boolean isPrivate() { return isRfc(AddressContextEnum.PRIVATE); }
    @JsonIgnore
    public boolean isWork() { return isRfc(AddressContextEnum.WORK); }
    @JsonIgnore
    public boolean isPostal() { return isRfc(AddressContextEnum.POSTAL); }
    @JsonIgnore
    public boolean isBilling() { return isRfc(AddressContextEnum.BILLING); }
    @JsonIgnore
    public boolean isOther() { return isRfc(AddressContextEnum.OTHER); }

    public static AddressContext rfc(AddressContextEnum rfcValue) { return AddressContext.builder().rfcValue(rfcValue).build();}
    public static AddressContext private_() { return rfc(AddressContextEnum.PRIVATE);}
    public static AddressContext work() { return rfc(AddressContextEnum.WORK);}
    public static AddressContext postal() { return rfc(AddressContextEnum.POSTAL);}
    public static AddressContext billing() { return rfc(AddressContextEnum.BILLING);}
    public static AddressContext other() { return rfc(AddressContextEnum.OTHER);}
    public static AddressContext ext(String extValue) { return AddressContext.builder().extValue(extValue).build(); }

    public static List<AddressContextEnum> getAddressContextEnumValues(Collection<AddressContext> contexts) {

        if (contexts == null)
            return null;

        List<AddressContextEnum> enumValues = new ArrayList<>();
        for (AddressContext context : contexts) {
            if (context.rfcValue != null)
                enumValues.add(context.getRfcValue());
        }

        return enumValues;
    }
}
