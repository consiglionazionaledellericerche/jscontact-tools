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
import it.cnr.iit.jscontact.tools.dto.deserializers.NameComponentTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.NameComponentTypeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the NameComponent type as defined in section 2.2.1 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.2.1">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","type","value"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameComponent extends GroupableObject implements Serializable {

    @NotNull
    @Pattern(regexp = "NameComponent", message="invalid @type value in NameComponent")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "NameComponent";

    @NotNull(message = "type is missing in NameComponent")
    @NonNull
    @JsonSerialize(using = NameComponentTypeSerializer.class)
    @JsonDeserialize(using = NameComponentTypeDeserializer.class)
    NameComponentType type;

    @NotNull(message = "value is missing in NameComponent")
    @NonNull
    String value;

    private boolean isRfc(NameComponentEnum value) { return (type.getRfcValue()!= null && type.getRfcValue() == value);}

    /**
     * Tests if this is a prefix.
     *
     * @return true if this is a prefix, false otherwise
     */
    @JsonIgnore
    public boolean isPrefix() { return isRfc(NameComponentEnum.PREFIX); }
    /**
     * Tests if this is a personal name.
     *
     * @return true if this is a personal name, false otherwise
     */
    @JsonIgnore
    public boolean isPersonal() { return isRfc(NameComponentEnum.PERSONAL); }
    /**
     * Tests if this is the surname.
     *
     * @return true if this is the surname, false otherwise
     */
    @JsonIgnore
    public boolean isSurname() { return isRfc(NameComponentEnum.SURNAME); }
    /**
     * Tests if this is an additional name.
     *
     * @return true if this is an additional name, false otherwise
     */
    @JsonIgnore
    public boolean isAdditional() { return isRfc(NameComponentEnum.ADDITIONAL); }
    /**
     * Tests if this is a suffix.
     *
     * @return true if this is a suffix, false otherwise
     */
    @JsonIgnore
    public boolean isSuffix() { return isRfc(NameComponentEnum.SUFFIX); }
    /**
     * Tests if this is the separator for name components used to build the full name.
     *
     * @return true if this is the separator, false otherwise
     */
    @JsonIgnore
    public boolean isSeparator() { return isRfc(NameComponentEnum.SEPARATOR); }
    /**
     * Tests if this is a custom name component.
     *
     * @return true if this is a custom name component, false otherwise
     */
    @JsonIgnore
    public boolean isExt() { return type.isExtValue(); }

    private static NameComponent rfc(NameComponentEnum rfcValue, String value) {
        return NameComponent.builder()
                .value(value)
                .type(NameComponentType.builder().rfcValue(rfcValue).build())
                .build();
    }
    /**
     * Returns a prefix component of a name.
     *
     * @param value the prefix
     * @return the prefix component
     */
    public static NameComponent prefix(String value) {return rfc(NameComponentEnum.PREFIX, value);}
    /**
     * Returns a personal name component of a name.
     *
     * @param value the personal name
     * @return the personal name  component
     */
    public static NameComponent personal(String value) {return rfc(NameComponentEnum.PERSONAL, value);}
    /**
     * Returns a surname component of a name.
     *
     * @param value the surname
     * @return the surname  component
     */
    public static NameComponent surname(String value) {return rfc(NameComponentEnum.SURNAME, value);}
    /**
     * Returns an additional name component of a name.
     *
     * @param value the additional name
     * @return the additional name  component
     */
    public static NameComponent additional(String value) {return rfc(NameComponentEnum.ADDITIONAL, value);}
    /**
     * Returns a suffix name component of a name.
     *
     * @param value the suffix
     * @return the suffix name  component
     */
    public static NameComponent suffix(String value) {return rfc(NameComponentEnum.SUFFIX, value);}
    /**
     * Returns a separator name component of a name.
     *
     * @param value the separator
     * @return the separator name  component
     */
    public static NameComponent separator(String value) {return rfc(NameComponentEnum.SEPARATOR, value);}
    /**
     * Returns a custom component of a name.
     *
     * @param extValue the custom name component
     * @param value the value for the custom name component
     * @return the custom component
     */
    public static NameComponent ext(String extValue, String value) {
        return NameComponent.builder()
                .value(value)
                .type(NameComponentType.builder().extValue(extValue).build())
                .build();
    }


}
