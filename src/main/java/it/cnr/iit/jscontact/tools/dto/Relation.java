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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.RelationDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.RelationSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the Relation type as defined in section 2.1.7 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.1.7">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation extends GroupableObject implements Serializable {

    @NotNull
    @Pattern(regexp = "Relation", message="invalid @type value in Relation")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Relation";

    @JsonSerialize(using = RelationSerializer.class)
    @JsonDeserialize(using = RelationDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<RelationType,Boolean> relation in Relation - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(value="relationType", ignoreNullCollections = true)
    Map<RelationType,Boolean> relation;

    private boolean asRelation(RelationType type) { return relation!= null && relation.containsKey(type); }
    public boolean asAcquaintance() {return asRelation(RelationType.acquaintance()); }
    public boolean asAgent() {return asRelation(RelationType.agent()); }
    public boolean asChild() {return asRelation(RelationType.child()); }
    public boolean asColleague() {return asRelation(RelationType.colleague()); }
    public boolean asContact() {return asRelation(RelationType.contact()); }
    public boolean asCoResident() {return asRelation(RelationType.coResident()); }
    public boolean asCoWorker() {return asRelation(RelationType.coWorker()); }
    public boolean asCrush() {return asRelation(RelationType.crush()); }
    public boolean asDate() {return asRelation(RelationType.date()); }
    public boolean asEmergency() {return asRelation(RelationType.emergency()); }
    public boolean asFriend() {return asRelation(RelationType.friend()); }
    public boolean asKin() {return asRelation(RelationType.kin()); }
    public boolean asMe() {return asRelation(RelationType.me()); }
    public boolean asMet() {return asRelation(RelationType.met()); }
    public boolean asMuse() {return asRelation(RelationType.muse()); }
    public boolean asNeighbor() {return asRelation(RelationType.neighbor()); }
    public boolean asParent() {return asRelation(RelationType.parent()); }
    public boolean asSibling() {return asRelation(RelationType.sibling()); }
    public boolean asSpouse() {return asRelation(RelationType.spouse()); }
    public boolean asSweetheart() {return asRelation(RelationType.sweetheart()); }
    public boolean asExtRelation(String extValue) { return asRelation(RelationType.extRelation(extValue)); }

}
