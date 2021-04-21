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
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation extends GroupableObject implements Serializable {

    @BooleanMapConstraint(message = "invalid Map<RelationType,Boolean> relation in Relation - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(value="relationType", ignoreNullCollections = true)
    Map<RelationType,Boolean> relation;

    private boolean asRelation(RelationType type) { return relation!= null && relation.containsKey(type); }
    public boolean asContact() {return asRelation(RelationType.CONTACT); }
    public boolean asAcquaintance() {return asRelation(RelationType.ACQUAINTANCE); }
    public boolean asFriend() {return asRelation(RelationType.FRIEND); }
    public boolean asMet() {return asRelation(RelationType.MET); }
    public boolean asCoWorker() {return asRelation(RelationType.CO_WORKER); }
    public boolean asColleague() {return asRelation(RelationType.COLLEAGUE); }
    public boolean asCoResident() {return asRelation(RelationType.CO_RESIDENT); }
    public boolean asNeighbor() {return asRelation(RelationType.NEIGHBOR); }
    public boolean asChild() {return asRelation(RelationType.CHILD); }
    public boolean asParent() {return asRelation(RelationType.PARENT); }
    public boolean asSibling() {return asRelation(RelationType.SIBLING); }
    public boolean asSpouse() {return asRelation(RelationType.SPOUSE); }
    public boolean asKin() {return asRelation(RelationType.KIN); }
    public boolean asMuse() {return asRelation(RelationType.MUSE); }
    public boolean asCrush() {return asRelation(RelationType.CRUSH); }
    public boolean asDate() {return asRelation(RelationType.DATE); }
    public boolean asSweetheart() {return asRelation(RelationType.SWEETHEART); }
    public boolean asMe() {return asRelation(RelationType.ME); }
    public boolean asAgent() {return asRelation(RelationType.AGENT); }
    public boolean asEmergency() {return asRelation(RelationType.EMERGENCY); }

}
