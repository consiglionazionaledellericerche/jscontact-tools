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
 * Class mapping the keys of "contexts" map of the Address type as defined in section 2.4.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.4.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddressContext extends ExtensibleEnum<AddressContextEnum> implements Serializable {

    private boolean isRfc(AddressContextEnum value) { return isRfcValue() && rfcValue == value; }
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
    public boolean isWork() { return isRfc(AddressContextEnum.WORK); }
    /**
     * Tests if this is a "postal" address context.
     *
     * @return true if this is a "postal" address context, false otherwise
     */
    @JsonIgnore
    public boolean isPostal() { return isRfc(AddressContextEnum.POSTAL); }
    /**
     * Tests if this is a "billing" address context.
     *
     * @return true if this is a "billing" address context, false otherwise
     */
    @JsonIgnore
    public boolean isBilling() { return isRfc(AddressContextEnum.BILLING); }
    /**
     * Tests if this is a custom address context.
     *
     * @return true if this is a custom address context, false otherwise
     */
    @JsonIgnore
    public boolean isExt() { return isExtValue(); }
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
    public static AddressContext work() { return rfc(AddressContextEnum.WORK);}
    /**
     * Returns a "postal" address context.
     *
     * @return a "postal" address context
     */
    public static AddressContext postal() { return rfc(AddressContextEnum.POSTAL);}
    /**
     * Returns a "billing" address context.
     *
     * @return a "billing" address context
     */
    public static AddressContext billing() { return rfc(AddressContextEnum.BILLING);}
    /**
     * Returns a custom address context.
     *
     * @param extValue the custom address context in text format
     * @return a custom address context
     */
    public static AddressContext ext(String extValue) { return AddressContext.builder().extValue(extValue).build(); }

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
