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

    public boolean asContact() {return relation!= null && relation.containsKey(RelationType.CONTACT); }
    public boolean asAcquaintance() {return relation!= null && relation.containsKey(RelationType.ACQUAINTANCE); }
    public boolean asFriend() {return relation!= null && relation.containsKey(RelationType.FRIEND); }
    public boolean asMet() {return relation!= null && relation.containsKey(RelationType.MET); }
    public boolean asCoWorker() {return relation!= null && relation.containsKey(RelationType.CO_WORKER); }
    public boolean asColleague() {return relation!= null && relation.containsKey(RelationType.COLLEAGUE); }
    public boolean asCoResident() {return relation!= null && relation.containsKey(RelationType.CO_RESIDENT); }
    public boolean asNeighbor() {return relation!= null && relation.containsKey(RelationType.NEIGHBOR); }
    public boolean asChild() {return relation!= null && relation.containsKey(RelationType.CHILD); }
    public boolean asParent() {return relation!= null && relation.containsKey(RelationType.PARENT); }
    public boolean asSibling() {return relation!= null && relation.containsKey(RelationType.SIBLING); }
    public boolean asSpouse() {return relation!= null && relation.containsKey(RelationType.SPOUSE); }
    public boolean asKin() {return relation!= null && relation.containsKey(RelationType.KIN); }
    public boolean asMuse() {return relation!= null && relation.containsKey(RelationType.MUSE); }
    public boolean asCrush() {return relation!= null && relation.containsKey(RelationType.CRUSH); }
    public boolean asDate() {return relation!= null && relation.containsKey(RelationType.DATE); }
    public boolean asSweetheart() {return relation!= null && relation.containsKey(RelationType.SWEETHEART); }
    public boolean asMe() {return relation!= null && relation.containsKey(RelationType.ME); }
    public boolean asAgent() {return relation!= null && relation.containsKey(RelationType.AGENT); }
    public boolean asEmergency() {return relation!= null && relation.containsKey(RelationType.EMERGENCY); }

}
