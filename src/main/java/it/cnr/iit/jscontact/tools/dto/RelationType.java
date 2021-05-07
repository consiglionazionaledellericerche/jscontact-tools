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
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RelationType extends ExtensibleType<RelationEnum> implements Serializable {

    private boolean isRfcRelation(RelationEnum value) { return rfcValue != null && rfcValue == value; }
    @JsonIgnore
    public boolean isContact() { return isRfcRelation(RelationEnum.CONTACT); }
    @JsonIgnore
    public boolean isAcquaintance() { return isRfcRelation(RelationEnum.ACQUAINTANCE); }
    @JsonIgnore
    public boolean isAgent() { return isRfcRelation(RelationEnum.AGENT); }
    @JsonIgnore
    public boolean isChild() { return isRfcRelation(RelationEnum.CHILD); }
    @JsonIgnore
    public boolean isCoResident() { return isRfcRelation(RelationEnum.CO_RESIDENT); }
    @JsonIgnore
    public boolean isCoWorker() { return isRfcRelation(RelationEnum.CO_WORKER); }
    @JsonIgnore
    public boolean isColleague() { return isRfcRelation(RelationEnum.COLLEAGUE); }
    @JsonIgnore
    public boolean isCrush() { return isRfcRelation(RelationEnum.CRUSH); }
    @JsonIgnore
    public boolean isDate() { return isRfcRelation(RelationEnum.DATE); }
    @JsonIgnore
    public boolean isEmergency() { return isRfcRelation(RelationEnum.EMERGENCY); }
    @JsonIgnore
    public boolean isFriend() { return isRfcRelation(RelationEnum.FRIEND); }
    @JsonIgnore
    public boolean isKin() { return isRfcRelation(RelationEnum.KIN); }
    @JsonIgnore
    public boolean isMe() { return isRfcRelation(RelationEnum.ME); }
    @JsonIgnore
    public boolean isMet() { return isRfcRelation(RelationEnum.MET); }
    @JsonIgnore
    public boolean isMuse() { return isRfcRelation(RelationEnum.MUSE); }
    @JsonIgnore
    public boolean isNeighbor() { return isRfcRelation(RelationEnum.NEIGHBOR); }
    @JsonIgnore
    public boolean isParent() { return isRfcRelation(RelationEnum.PARENT); }
    @JsonIgnore
    public boolean isSibling() { return isRfcRelation(RelationEnum.SIBLING); }
    @JsonIgnore
    public boolean isSpouse() { return isRfcRelation(RelationEnum.SPOUSE); }
    @JsonIgnore
    public boolean isSweetheart() { return isRfcRelation(RelationEnum.SWEETHEART); }
    @JsonIgnore
    public boolean isExtRelation() { return extValue != null; }

    public static RelationType rfcRelation(RelationEnum rfcValue) { return RelationType.builder().rfcValue(rfcValue).build();}
    public static RelationType acquaintance() { return rfcRelation(RelationEnum.ACQUAINTANCE);}
    public static RelationType agent() { return rfcRelation(RelationEnum.AGENT);}
    public static RelationType child() { return rfcRelation(RelationEnum.CHILD);}
    public static RelationType colleague() { return rfcRelation(RelationEnum.COLLEAGUE);}
    public static RelationType contact() { return rfcRelation(RelationEnum.CONTACT);}
    public static RelationType coResident() { return rfcRelation(RelationEnum.CO_RESIDENT);}
    public static RelationType coWorker() { return rfcRelation(RelationEnum.CO_WORKER);}
    public static RelationType crush() { return rfcRelation(RelationEnum.CRUSH);}
    public static RelationType date() { return rfcRelation(RelationEnum.DATE);}
    public static RelationType emergency() { return rfcRelation(RelationEnum.EMERGENCY);}
    public static RelationType friend() { return rfcRelation(RelationEnum.FRIEND);}
    public static RelationType kin() { return rfcRelation(RelationEnum.KIN);}
    public static RelationType me() { return rfcRelation(RelationEnum.ME);}
    public static RelationType met() { return rfcRelation(RelationEnum.MET);}
    public static RelationType muse() { return rfcRelation(RelationEnum.MUSE);}
    public static RelationType neighbor() { return rfcRelation(RelationEnum.NEIGHBOR);}
    public static RelationType parent() { return rfcRelation(RelationEnum.PARENT);}
    public static RelationType sibling() { return rfcRelation(RelationEnum.SIBLING);}
    public static RelationType spouse() { return rfcRelation(RelationEnum.SPOUSE);}
    public static RelationType sweetheart() { return rfcRelation(RelationEnum.SWEETHEART);}
    public static RelationType extRelation(String extValue) { return RelationType.builder().extValue(extValue).build(); }

}
