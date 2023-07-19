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
import it.cnr.iit.jscontact.tools.dto.deserializers.NameComponentTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPronounce;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the NameComponent type as defined in section 2.2.2 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.2">draft-ietf-calext-jscontact</a>
 */
@JsonPropertyOrder({"@type", "value", "kind", "pronounce"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NameComponent extends AbstractJSContactType implements HasKind, HasPronounce, Serializable {

    @Pattern(regexp = "NameComponent", message = "invalid @type value in NameComponent")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "NameComponent";

    @NotNull(message = "value is missing in NameComponent")
    @NonNull
    String value;

    @NotNull(message = "kind is missing in NameComponent")
    @NonNull
    @JsonDeserialize(using = NameComponentTypeDeserializer.class)
    NameComponentKind kind;

    @Valid
    Pronounce pronounce;

    private boolean isRfc(NameComponentEnum value) {
        return (kind.getRfcValue() != null && kind.getRfcValue() == value);
    }

    /**
     * Tests if this is a title.
     *
     * @return true if this is a title, false otherwise
     */
    @JsonIgnore
    public boolean isTitle() { return isRfc(NameComponentEnum.TITLE); }
    /**
     * Tests if this is a given name.
     *
     * @return true if this is a given name, false otherwise
     */
    @JsonIgnore
    public boolean isGiven() { return isRfc(NameComponentEnum.GIVEN); }
    /**
     * Tests if this is the surname.
     *
     * @return true if this is the surname, false otherwise
     */
    @JsonIgnore
    public boolean isSurname() { return isRfc(NameComponentEnum.SURNAME); }
    /**
     * Tests if this is the second surname.
     *
     * @return true if this is the second surname, false otherwise
     */
    @JsonIgnore
    public boolean isSurname2() { return isRfc(NameComponentEnum.SURNAME2); }
    /**
     * Tests if this is a second name.
     *
     * @return true if this is a second name, false otherwise
     */
    @JsonIgnore
    public boolean isGiven2() { return isRfc(NameComponentEnum.GIVEN2); }
    /**
     * Tests if this is a credential.
     *
     * @return true if this is a credential, false otherwise
     */
    @JsonIgnore
    public boolean isCredential() { return isRfc(NameComponentEnum.CREDENTIAL); }
    /**
     * Tests if this is a generation.
     *
     * @return true if this is a generation, false otherwise
     */
    @JsonIgnore
    public boolean isGeneration() { return isRfc(NameComponentEnum.GENERATION); }
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
    public boolean isExt() { return kind.isExtValue(); }

    private static NameComponent rfc(NameComponentEnum rfcValue, String value) {
        return NameComponent.builder()
                .value(value)
                .kind(NameComponentKind.builder().rfcValue(rfcValue).build())
                .build();
    }
    /**
     * Returns a title component of a name.
     *
     * @param value the title
     * @return the title component
     */
    public static NameComponent title(String value) {return rfc(NameComponentEnum.TITLE, value);}
    /**
     * Returns a given name component of a name.
     *
     * @param value the given name
     * @return the given name  component
     */
    public static NameComponent given(String value) {return rfc(NameComponentEnum.GIVEN, value);}
    /**
     * Returns a surname component of a name.
     *
     * @param value the surname
     * @return the surname  component
     */
    public static NameComponent surname(String value) {return rfc(NameComponentEnum.SURNAME, value);}
    /**
     * Returns a second surname component of a name.
     *
     * @param value the second surname
     * @return the second surname component
     */
    public static NameComponent surname2(String value) {return rfc(NameComponentEnum.SURNAME2, value);}
    /**
     * Returns a second given name component of a name.
     *
     * @param value the second given name
     * @return the second given name component
     */
    public static NameComponent given2(String value) {return rfc(NameComponentEnum.GIVEN2, value);}
    /**
     * Returns a credential name component of a name.
     *
     * @param value the credential
     * @return the credential name  component
     */
    public static NameComponent credential(String value) {return rfc(NameComponentEnum.CREDENTIAL, value);}
    /**
     * Returns a generation name component of a name.
     *
     * @param value the generation
     * @return the generation name  component
     */
    public static NameComponent generation(String value) {return rfc(NameComponentEnum.GENERATION, value);}
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
                .kind(NameComponentKind.builder().extValue(V_Extension.toV_Extension(extValue)).build())
                .build();
    }


}
