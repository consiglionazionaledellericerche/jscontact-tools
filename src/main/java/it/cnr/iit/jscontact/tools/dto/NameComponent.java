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
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.NameComponentKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsComponent;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the NameComponent type as defined in section 2.2.1.2 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.1.2">draft-ietf-calext-jscontact</a>
 */
@JsonPropertyOrder({"@type", "kind", "value", "phonetic"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NameComponent extends AbstractJSContactType implements HasKind, IsComponent, IsIANAType, Serializable {

    @Pattern(regexp = "NameComponent", message = "invalid @type value in NameComponent")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "NameComponent";

    @NotNull(message = "kind is missing in NameComponent")
    @NonNull
    @JsonDeserialize(using = NameComponentKindDeserializer.class)
    @ContainsExtensibleEnum(enumClass = NameComponentEnum.class, getMethod = "getKind")
    NameComponentKind kind;

    @NotNull(message = "value is missing in NameComponent")
    @NonNull
    String value;

    String phonetic;

    @JsonIgnore
    int indexPerKind;

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

    private static NameComponent rfc(NameComponentEnum rfcValue, String value, String phonetic) {
        return NameComponent.builder()
                .value(value)
                .phonetic(phonetic)
                .kind(NameComponentKind.builder().rfcValue(rfcValue).build())
                .build();
    }
    /**
     * Returns a title component of a name with phonetic.
     *
     * @param value the title
     * @param phonetic the phonetic
     * @return the title component
     */
    public static NameComponent title(String value, String phonetic) {return rfc(NameComponentEnum.TITLE, value, phonetic);}
    /**
     * Returns a title component of a name.
     *
     * @param value the title
     * @return the title component
     */
    public static NameComponent title(String value) {return NameComponent.title(value, null);}

    /**
     * Returns a given name component of a name with phonetic.
     *
     * @param value the given name
     * @param phonetic the phonetic
     * @return the given name  component
     */
    public static NameComponent given(String value, String phonetic) {return rfc(NameComponentEnum.GIVEN, value, phonetic);}
    /**
     * Returns a given name component of a name.
     *
     * @param value the given name
     * @return the given name  component
     */
    public static NameComponent given(String value) {return NameComponent.given(value, null);}
    /**
     * Returns a surname component of a name with phonetic.
     *
     * @param value the surname
     * @param phonetic the phonetic
     * @return the surname  component
     */
    public static NameComponent surname(String value, String phonetic) {return rfc(NameComponentEnum.SURNAME, value, phonetic);}
    /**
     * Returns a surname component of a name.
     *
     * @param value the surname
     * @return the surname  component
     */
    public static NameComponent surname(String value) {return NameComponent.surname(value, null);}
    /**
     * Returns a second surname component of a name with phonetic.
     *
     * @param value the second surname
     * @param phonetic the phonetic
     * @return the second surname component
     */
    public static NameComponent surname2(String value, String phonetic) {return rfc(NameComponentEnum.SURNAME2, value, phonetic);}
    /**
     * Returns a second surname component of a name.
     *
     * @param value the second surname
     * @return the second surname component
     */
    public static NameComponent surname2(String value) {return NameComponent.surname2(value, null);}
    /**
     * Returns a second given name component of a name with phonetic.
     *
     * @param value the second given name
     * @param phonetic the phonetic
     * @return the second given name component
     */
    public static NameComponent given2(String value, String phonetic) {return rfc(NameComponentEnum.GIVEN2, value, phonetic);}
    /**
     * Returns a second given name component of a name.
     *
     * @param value the second given name
     * @return the second given name component
     */
    public static NameComponent given2(String value) {return NameComponent.given2(value, null);}
    /**
     * Returns a credential name component of a name with phonetic.
     *
     * @param value the credential
     * @param phonetic the phonetic
     * @return the credential name  component
     */
    public static NameComponent credential(String value, String phonetic) {return rfc(NameComponentEnum.CREDENTIAL, value, phonetic);}
    /**
     * Returns a credential name component of a name.
     *
     * @param value the credential
     * @return the credential name  component
     */
    public static NameComponent credential(String value) {return NameComponent.credential(value, null);}
    /**
     * Returns a generation name component of a name with phonetic.
     *
     * @param value the generation
     * @param phonetic the phonetic
     * @return the generation name  component
     */
    public static NameComponent generation(String value, String phonetic) {return rfc(NameComponentEnum.GENERATION, value, phonetic);}
    /**
     * Returns a generation name component of a name.
     *
     * @param value the generation
     * @return the generation name  component
     */
    public static NameComponent generation(String value) {return NameComponent.generation(value, null);}
    /**
     * Returns a separator name component of a name.
     *
     * @param value the separator
     * @return the separator name  component
     */
    public static NameComponent separator(String value) {return rfc(NameComponentEnum.SEPARATOR, value, null);}
    /**
     * Returns a custom component of a name with phonetic.
     *
     * @param extValue the custom name component
     * @param value the value for the custom name component
     * @param phonetic the phonetic
     * @return the custom component
     */
    public static NameComponent ext(String extValue, String value, String phonetic) {
        return NameComponent.builder()
                .value(value)
                .phonetic(phonetic)
                .kind(NameComponentKind.builder().extValue(V_Extension.toV_Extension(extValue)).build())
                .build();
    }

    /**
     * Returns a custom component of a name.
     *
     * @param extValue the custom name component
     * @param value the value for the custom name component
     * @return the custom component
     */
    public static NameComponent ext(String extValue, String value) {
        return NameComponent.ext(extValue,value, null);
    }

}
