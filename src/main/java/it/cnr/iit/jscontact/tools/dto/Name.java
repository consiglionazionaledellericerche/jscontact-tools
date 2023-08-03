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
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import it.cnr.iit.jscontact.tools.constraints.ComponentsConstraint;
import it.cnr.iit.jscontact.tools.constraints.NotNullDependencyConstraint;
import it.cnr.iit.jscontact.tools.dto.annotations.JSContactCollection;
import it.cnr.iit.jscontact.tools.dto.deserializers.NameSortAsDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.PronounceSystemDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasComponents;
import it.cnr.iit.jscontact.tools.dto.serializers.NameSortAsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the Name type as defined in section 2.2.1 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.1">draft-ietf-calext-jscontact</a>
 */
@NotNullAnyConstraint(fieldNames = {"full", "components"}, message = "at least one not null member between full and components is required in Name")
@NotNullDependencyConstraint(fieldName="components", dependingFieldNames = {"sortAs"})
@ComponentsConstraint
@JsonPropertyOrder({"@type", "full", "components", "isOrdered", "pronounce", "sortAs", "phoneticSystem", "phoneticScript"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Name extends AbstractJSContactType implements HasComponents, Serializable {

    @Pattern(regexp = "Name", message="invalid @type value in Name")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Name";

    String full;

    @JSContactCollection(addMethod = "addComponent", itemClass = NameComponent.class)
    @Valid
    NameComponent[] components;

    Boolean isOrdered = Boolean.FALSE;

    String defaultSeparator;

    @JsonSerialize(using = NameSortAsSerializer.class)
    @JsonDeserialize(using = NameSortAsDeserializer.class)
    Map<NameComponentKind, String> sortAs;

    String phoneticScript;

    @JsonDeserialize(using = PronounceSystemDeserializer.class)
    PhoneticSystem phoneticSystem;

    /**
     * Adds a name component to this object.
     *
     * @param nc         the name component
     * @param components the name components
     * @return the name components in input plus the nc component
     */
    public static NameComponent[] addComponent(NameComponent[] components, NameComponent nc) {
        return ArrayUtils.add(components, nc);
    }

    /**
     * Adds a name component to this object.
     *
     * @param nc the name component
     */
    public void addComponent(NameComponent nc) {
        components = ArrayUtils.add(components, nc);
    }

    private String getComponentValue(NameComponentKind componentKind) {

        if (components == null)
            return null;

        for (NameComponent component : components) {
            if (component.getKind().equals(componentKind))
                return component.getValue();
        }

        return null;
    }

    /**
     * Returns the given name of this object.
     *
     * @return the value of NameComponent item in the "components" array tagged as "given"
     */
    @JsonIgnore
    public String getGiven() {
        return getComponentValue(NameComponentKind.given());
    }

    /**
     * Returns the secondary given name of this object.
     *
     * @return the value of NameComponent item in the "components" array tagged as "given2"
     */
    @JsonIgnore
    public String getGiven2() {
        return getComponentValue(NameComponentKind.given2());
    }

    /**
     * Returns the surname of this object.
     *
     * @return the value of NameComponent item in the "components" array tagged as "surname"
     */
    @JsonIgnore
    public String getSurname() {
        return getComponentValue(NameComponentKind.surname());
    }

    /**
     * Returns the secondary surname of this object.
     *
     * @return the value of NameComponent item in the "components" array tagged as "surname2"
     */
    @JsonIgnore
    public String getSurname2() {
        return getComponentValue(NameComponentKind.surname2());
    }

    /**
     * Returns the generation of this object.
     *
     * @return the value of NameComponent item in the "components" array tagged as "generation"
     */
    @JsonIgnore
    public String getGeneration() {
        return getComponentValue(NameComponentKind.generation());
    }

}
