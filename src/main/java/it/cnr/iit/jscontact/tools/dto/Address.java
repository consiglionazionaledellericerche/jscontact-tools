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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.annotations.JSContactCollection;
import it.cnr.iit.jscontact.tools.dto.deserializers.AddressContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.serializers.AddressContextsSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

/**
 * Class mapping the Address type as defined in section 2.5.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.5.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","full","components","locality","region","country",
                     "postcode","countryCode","coordinates","timeZone",
                     "contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"hash"}, callSuper = false)
public class Address extends AbstractJSContactType implements IdMapValue, Serializable {

    @Pattern(regexp = "Address", message="invalid @type value in Address")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Address";

    String full;

    @JSContactCollection(addMethod = "addComponent", itemClass = AddressComponent.class)
    AddressComponent[] components;

    String locality;

    String region;

    String country;

    String postcode;

    @Pattern(regexp="[a-zA-Z]{2}", message = "invalid countryCode in Address")
    String countryCode;

    @Pattern(regexp="geo:([\\-0-9.]+),([\\-0-9.]+)(?:,([\\-0-9.]+))?(?:\\?(.*))?$", message = "invalid coordinates in Address")
    String coordinates;

    String timeZone;

    String defaultSeparator;

    @JsonSerialize(using = AddressContextsSerializer.class)
    @JsonDeserialize(using = AddressContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<AddressContext,Boolean> contexts in Address - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<AddressContext,Boolean> contexts;

    @Min(value=1, message = "invalid pref in Address - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Address - value must be less or equal than 100")
    Integer pref;

    @JsonIgnore
    String altid;

    @JsonIgnore
    String language;

    @JsonIgnore
    String group;

    @JsonIgnore
    String hash;

    private boolean asContext(AddressContext context) { return contexts != null && contexts.containsKey(context); }
    /**
     * Tests if this address is a work address.
     *
     * @return true if the context map includes the "work" context, false otherwise
     */
    public boolean asWork() { return asContext(AddressContext.work()); }
    /**
     * Tests if this address is a private address.
     *
     * @return true if the context map includes the "private" context, false otherwise
     */
    public boolean asPrivate() { return asContext(AddressContext.private_()); }
    /**
     * Tests if this address is a billing address.
     *
     * @return true if the context map includes the "billing" context, false otherwise
     */
    public boolean asBilling() {
        return asContext(AddressContext.billing());
    }

    /**
     * Tests if this address is a delivery address.
     *
     * @return true if the context map includes the "delivery" context, false otherwise
     */
    public boolean asDelivery() {
        return asContext(AddressContext.delivery());
    }

    /**
     * Tests if this address is used in a custom context.
     *
     * @param extValue the custom context in text format
     * @return true if the context map includes the given custom context, false otherwise
     */
    public boolean asExtContext(String extValue) {
        return asContext(AddressContext.ext(extValue));
    }
    /**
     * Tests if the context of this address is undefined.
     *
     * @return true if the context map is empty, false otherwise
     */
    public boolean hasNoContext() { return contexts == null || contexts.size() ==  0; }

    private String getStreetDetail(AddressComponentEnum detail) {

        if (components == null)
            return null;

        for (AddressComponent pair : components) {
            if (!pair.isExt() && pair.getKind().getRfcValue() == detail)
                return pair.getValue();
        }

        return null;
    }

    /**
     * Returns the P.O. box of this object.
     *
     * @return the value of AddressComponent item in the "components" array tagged as POST_OFFICE_BOX
     */
    @JsonIgnore
    public String getPostOfficeBox() {
        return getStreetDetail(AddressComponentEnum.POST_OFFICE_BOX);
    }


    private String getStreetAddressDetails(List<AddressComponentEnum> componentsToCheck) {
        if (components == null)
            return null;

        List<String> addressComponents = new ArrayList<>();
        boolean applySeparator = false;
        for (AddressComponent pair : components) {
            if (pair.getKind().isRfcValue()) {
                if (componentsToCheck.contains(pair.getKind().getRfcValue())) {
                    applySeparator = true;
                    addressComponents.add(pair.getValue());
                    if (defaultSeparator != null)
                        addressComponents.add(defaultSeparator);
                    else
                        addressComponents.add(DelimiterUtils.COMMA_ARRAY_DELIMITER);
                } else if (pair.isSeparator()) {
                    if (applySeparator)
                        addressComponents.set(addressComponents.size() - 2, pair.getValue());
                }
            }
        }

        return (addressComponents.isEmpty()) ? null : String.join("",addressComponents.subList(0,addressComponents.size()-1));
    }

    /**
     * Returns the street details of this object.
     *
     * @return a text obtained by concatenating the values of AddressComponent items in the "components" array tagged as NAME, NUMBER or DIRECTION. The items are separated by the value of the item tagged as SEPARATOR if it isn't empty, space otherwise
     */
    @JsonIgnore
    public String getStreetAddress() {

        return getStreetAddressDetails(Arrays.asList(AddressComponentEnum.DISTRICT,
                                                     AddressComponentEnum.BLOCK,
                                                     AddressComponentEnum.NAME,
                                                     AddressComponentEnum.NUMBER,
                                                     AddressComponentEnum.DIRECTION));
    }

    /**
     * Returns the street extensions of this object.
     *
     * @return a text obtained by concatenating the values of the AddressComponent items in the "components" array tagged as BUILDING, FLOOR, APARTMENT, ROOM, EXTENSION or UNKNOWN. The items are separated by the item tagged as SEPARATOR if it isn't empty, space otherwise
     */
    @JsonIgnore
    public String getStreetExtendedAddress() {

        return getStreetAddressDetails(Arrays.asList(AddressComponentEnum.BUILDING,
                AddressComponentEnum.FLOOR,
                AddressComponentEnum.APARTMENT,
                AddressComponentEnum.ROOM,
                AddressComponentEnum.LANDMARK,
                AddressComponentEnum.EXTENSION));
    }


    /**
     * Adds a street component to this object.
     *
     * @param sc the street component
     * @param components the street components
     * @return the street components in input plus the sc component
     */
    public static AddressComponent[] addComponent(AddressComponent[] components, AddressComponent sc) {
        return ArrayUtils.add(components, sc);
    }

    /**
     * Adds a street component to this object.
     *
     * @param sc the street component
     */
    public void addComponent(AddressComponent sc) {
        components = ArrayUtils.add(components, sc);
    }

    /**
     * Adds a context to this object.
     *
     * @param context the context
     */
    public void addContext(AddressContext context) {
        Map<AddressContext, Boolean> clone = new HashMap<>(getContexts());
        clone.put(context,Boolean.TRUE);
        setContexts(clone);
    }

    /**
     * This method will be used to get the extended contexts in the "contexts" property.
     *
     * @return the extended contexts in the "contexts" property
     */
    @JsonIgnore
    public AddressContext[] getExtContexts() {
        if (getContexts() == null)
            return null;
        AddressContext[] extended = null;
        for(AddressContext context : getContexts().keySet()){
            if (context.isExtValue())
                extended = ArrayUtils.add(extended, context);
        }

        return extended;
    }

}
