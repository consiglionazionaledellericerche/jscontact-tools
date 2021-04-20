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

    public boolean hasContact() {return relation!= null && relation.containsKey(RelationType.CONTACT); }
    public boolean hasAcquaintance() {return relation!= null && relation.containsKey(RelationType.ACQUAINTANCE); }
    public boolean hasFriend() {return relation!= null && relation.containsKey(RelationType.FRIEND); }
    public boolean hasMet() {return relation!= null && relation.containsKey(RelationType.MET); }
    public boolean hasCoWorker() {return relation!= null && relation.containsKey(RelationType.CO_WORKER); }
    public boolean hasColleague() {return relation!= null && relation.containsKey(RelationType.COLLEAGUE); }
    public boolean hasCoResident() {return relation!= null && relation.containsKey(RelationType.CO_RESIDENT); }
    public boolean hasNeighbor() {return relation!= null && relation.containsKey(RelationType.NEIGHBOR); }
    public boolean hasChild() {return relation!= null && relation.containsKey(RelationType.CHILD); }
    public boolean hasParent() {return relation!= null && relation.containsKey(RelationType.PARENT); }
    public boolean hasSibling() {return relation!= null && relation.containsKey(RelationType.SIBLING); }
    public boolean hasSpouse() {return relation!= null && relation.containsKey(RelationType.SPOUSE); }
    public boolean hasKin() {return relation!= null && relation.containsKey(RelationType.KIN); }
    public boolean hasMuse() {return relation!= null && relation.containsKey(RelationType.MUSE); }
    public boolean hasCrush() {return relation!= null && relation.containsKey(RelationType.CRUSH); }
    public boolean hasDate() {return relation!= null && relation.containsKey(RelationType.DATE); }
    public boolean hasSweetheart() {return relation!= null && relation.containsKey(RelationType.SWEETHEART); }
    public boolean hasMe() {return relation!= null && relation.containsKey(RelationType.ME); }
    public boolean hasAgent() {return relation!= null && relation.containsKey(RelationType.AGENT); }
    public boolean hasEmergency() {return relation!= null && relation.containsKey(RelationType.EMERGENCY); }

}
