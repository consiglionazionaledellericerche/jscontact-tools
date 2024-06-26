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
 * Class mapping the keys of "contexts" map of the Address type as defined in section 2.5.1 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.5.1">Section 2.5.1 of RFC9553</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddressContext extends ExtensibleEnumType<AddressContextEnum> implements Serializable {

    /**
     * Tests if this is a "private" address context.
     *
     * @return true if this is a "private" address context, false otherwise
     */
    @JsonIgnore
    public boolean isPrivate() { return isRfc(AddressContextEnum.PRIVATE); }
    /**
     * Tests if this is a "work" address context.
     *
     * @return true if this is a "work" address context, false otherwise
     */
    @JsonIgnore
    public boolean isWork() {
        return isRfc(AddressContextEnum.WORK);
    }

    /**
     * Tests if this is a "delivery" address context.
     *
     * @return true if this is a "delivery" address context, false otherwise
     */
    @JsonIgnore
    public boolean isDelivery() {
        return isRfc(AddressContextEnum.DELIVERY);
    }

    /**
     * Tests if this is a "billing" address context.
     *
     * @return true if this is a "billing" address context, false otherwise
     */
    @JsonIgnore
    public boolean isBilling() {
        return isRfc(AddressContextEnum.BILLING);
    }

    /**
     * Returns an address context whose enum value is pre-defined.
     *
     * @param rfcValue the pre-defined address context
     * @return a pre-defined address context
     */
    public static AddressContext rfc(AddressContextEnum rfcValue) { return AddressContext.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "private" address context.
     *
     * @return a "private" address context
     */
    public static AddressContext private_() { return rfc(AddressContextEnum.PRIVATE);}
    /**
     * Returns a "work" address context.
     *
     * @return a "work" address context
     */
    public static AddressContext work() {
        return rfc(AddressContextEnum.WORK);
    }

    /**
     * Returns a "delivery" address context.
     *
     * @return a "delivery" address context
     */
    public static AddressContext delivery() {
        return rfc(AddressContextEnum.DELIVERY);
    }

    /**
     * Returns a "billing" address context.
     *
     * @return a "billing" address context
     */
    public static AddressContext billing() {
        return rfc(AddressContextEnum.BILLING);
    }
    /**
     * Returns a custom address context.
     *
     * @param extValue the custom address context in text format
     * @return a custom address context
     */
    public static AddressContext ext(String extValue) { return AddressContext.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }

    /**
     * Returns the list of enum values corresponding to those whose type is known in a given collection of address contexts.
     *
     * @param contexts the list of address contexts
     * @return list of enum values corresponding to those address contexts whose type is known
     */
    public static List<AddressContextEnum> toEnumValues(Collection<AddressContext> contexts) {

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
