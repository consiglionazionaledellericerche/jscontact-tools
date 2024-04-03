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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.RelationDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import it.cnr.iit.jscontact.tools.dto.serializers.RelationSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the Relation type as defined in section 2.1.8 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.8">Section 2.1.8 of RFC9553</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","relation"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Relation extends AbstractJSContactType implements IsIANAType, Serializable {

    @Pattern(regexp = "Relation", message="invalid @type value in Relation")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Relation";

    @JsonSerialize(using = RelationSerializer.class)
    @JsonDeserialize(using = RelationDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<RelationType,Boolean> relation in Relation - Only Boolean.TRUE allowed")
    @Singular(value="relationType", ignoreNullCollections = true)
    @ContainsExtensibleEnum(enumClass = RelationEnum.class, getMethod = "getRelation")
    Map<RelationType,Boolean> relation;

    private boolean asRelation(RelationType type) { return relation!= null && relation.containsKey(type); }
    /**
     * Tests if the collection of relations includes the "acquaintance" relation type.
     *
     * @return true if the "relation" map includes "acquaintance", false otherwise
     */
    public boolean asAcquaintance() {return asRelation(RelationType.acquaintance()); }
    /**
     * Tests if the collection of relations includes the "agent" relation type.
     *
     * @return true if the "relation" map includes "agent", false otherwise
     */
    public boolean asAgent() {return asRelation(RelationType.agent()); }
    /**
     * Tests if the collection of relations includes the "child" relation type.
     *
     * @return true if the "relation" map includes "child", false otherwise
     */
    public boolean asChild() {return asRelation(RelationType.child()); }
    /**
     * Tests if the collection of relations includes the "colleague" relation type.
     *
     * @return true if the "relation" map includes "colleague", false otherwise
     */
    public boolean asColleague() {return asRelation(RelationType.colleague()); }
    /**
     * Tests if the collection of relations includes the "contact" relation type.
     *
     * @return true if the "relation" map includes "contact", false otherwise
     */
    public boolean asContact() {return asRelation(RelationType.contact()); }
    /**
     * Tests if the collection of relations includes the "co-resident" relation type.
     *
     * @return true if the "relation" map includes "co-resident", false otherwise
     */
    public boolean asCoResident() {return asRelation(RelationType.coResident()); }
    /**
     * Tests if the collection of relations includes the "co-worker" relation type.
     *
     * @return true if the "relation" map includes "co-worker", false otherwise
     */
    public boolean asCoWorker() {return asRelation(RelationType.coWorker()); }
    /**
     * Tests if the collection of relations includes the "crush" relation type.
     *
     * @return true if the "relation" map includes "crush", false otherwise
     */
    public boolean asCrush() {return asRelation(RelationType.crush()); }
    /**
     * Tests if the collection of relations includes the "date" relation type.
     *
     * @return true if the "relation" map includes "date", false otherwise
     */
    public boolean asDate() {return asRelation(RelationType.date()); }
    /**
     * Tests if the collection of relations includes the "emergency" relation type.
     *
     * @return true if the "relation" map includes "emergency", false otherwise
     */
    public boolean asEmergency() {return asRelation(RelationType.emergency()); }
    /**
     * Tests if the collection of relations includes the "friend" relation type.
     *
     * @return true if the "relation" map includes "friend", false otherwise
     */
    public boolean asFriend() {return asRelation(RelationType.friend()); }
    /**
     * Tests if the collection of relations includes the "kin" relation type.
     *
     * @return true if the "relation" map includes "kin", false otherwise
     */
    public boolean asKin() {return asRelation(RelationType.kin()); }
    /**
     * Tests if the collection of relations includes the "me" relation type.
     *
     * @return true if the "relation" map includes "me", false otherwise
     */
    public boolean asMe() {return asRelation(RelationType.me()); }
    /**
     * Tests if the collection of relations includes the "met" relation type.
     *
     * @return true if the "relation" map includes "met", false otherwise
     */
    public boolean asMet() {return asRelation(RelationType.met()); }
    /**
     * Tests if the collection of relations includes the "muse" relation type.
     *
     * @return true if the "relation" map includes "muse", false otherwise
     */
    public boolean asMuse() {return asRelation(RelationType.muse()); }
    /**
     * Tests if the collection of relations includes the "neighbor" relation type.
     *
     * @return true if the "relation" map includes "neighbor", false otherwise
     */
    public boolean asNeighbor() {return asRelation(RelationType.neighbor()); }
    /**
     * Tests if the collection of relations includes the "parent" relation type.
     *
     * @return true if the "relation" map includes "parent", false otherwise
     */
    public boolean asParent() {return asRelation(RelationType.parent()); }
    /**
     * Tests if the collection of relations includes the "sibling" relation type.
     *
     * @return true if the "relation" map includes "sibling", false otherwise
     */
    public boolean asSibling() {return asRelation(RelationType.sibling()); }
    /**
     * Tests if the collection of relations includes the "spouse" relation type.
     *
     * @return true if the "relation" map includes "spouse", false otherwise
     */
    public boolean asSpouse() {return asRelation(RelationType.spouse()); }
    /**
     * Tests if the collection of relations includes the "sweetheart" relation type.
     *
     * @return true if the "relation" map includes "sweetheart", false otherwise
     */
    public boolean asSweetheart() {return asRelation(RelationType.sweetheart()); }
    /**
     * Tests if the collection of relations includes a custom relation type.
     *
     * @param extValue the custom relation type
     * @return true if the "relation" map includes the custom relation type, false otherwise
     */
    public boolean asExtRelation(String extValue) { return asRelation(RelationType.extRelation(extValue)); }

}
