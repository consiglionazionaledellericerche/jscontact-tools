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

/**
 * Class mapping the relation types as defined in section 2.1.7 of [RFC9553]. The relation types map those presented for the vCard 4.0 RELATED property as defined in section 6.6.4 of [RFC6350]
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.7">RFC9553</a>
 * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.6.4">RFC6350</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RelationType extends ExtensibleEnumType<RelationEnum> implements Serializable {

    private boolean isRfcRelation(RelationEnum value) { return rfcValue != null && rfcValue == value; }
    /**
     * Tests if the type of this relation is "contact".
     *
     * @return true if the type of this relation is "contact", false otherwise
     */
    @JsonIgnore
    public boolean isContact() { return isRfcRelation(RelationEnum.CONTACT); }
    /**
     * Tests if the type of this relation is "acquaintance".
     *
     * @return true if the type of this relation is "acquaintance", false otherwise
     */
    @JsonIgnore
    public boolean isAcquaintance() { return isRfcRelation(RelationEnum.ACQUAINTANCE); }
    /**
     * Tests if the type of this relation is "agent".
     *
     * @return true if the type of this relation is "agent", false otherwise
     */
    @JsonIgnore
    public boolean isAgent() { return isRfcRelation(RelationEnum.AGENT); }
    /**
     * Tests if the type of this relation is "child".
     *
     * @return true if the type of this relation is "child", false otherwise
     */
    @JsonIgnore
    public boolean isChild() { return isRfcRelation(RelationEnum.CHILD); }
    /**
     * Tests if the type of this relation is "co-resident".
     *
     * @return true if the type of this relation is "co-resident", false otherwise
     */
    @JsonIgnore
    public boolean isCoResident() { return isRfcRelation(RelationEnum.CO_RESIDENT); }
    /**
     * Tests if the type of this relation is "co-worker".
     *
     * @return true if the type of this relation is "co-worker", false otherwise
     */
    @JsonIgnore
    public boolean isCoWorker() { return isRfcRelation(RelationEnum.CO_WORKER); }
    /**
     * Tests if the type of this relation is "colleague".
     *
     * @return true if the type of this relation is "colleague", false otherwise
     */
    @JsonIgnore
    public boolean isColleague() { return isRfcRelation(RelationEnum.COLLEAGUE); }
    /**
     * Tests if the type of this relation is "crush".
     *
     * @return true if the type of this relation is "crush", false otherwise
     */
    @JsonIgnore
    public boolean isCrush() { return isRfcRelation(RelationEnum.CRUSH); }
    /**
     * Tests if the type of this relation is "date".
     *
     * @return true if the type of this relation is "date", false otherwise
     */
    @JsonIgnore
    public boolean isDate() { return isRfcRelation(RelationEnum.DATE); }
    /**
     * Tests if the type of this relation is "emergency".
     *
     * @return true if the type of this relation is "emergency", false otherwise
     */
    @JsonIgnore
    public boolean isEmergency() { return isRfcRelation(RelationEnum.EMERGENCY); }
    /**
     * Tests if the type of this relation is "friend".
     *
     * @return true if the type of this relation is "friend", false otherwise
     */
    @JsonIgnore
    public boolean isFriend() { return isRfcRelation(RelationEnum.FRIEND); }
    /**
     * Tests if the type of this relation is "kin".
     *
     * @return true if the type of this relation is "kin", false otherwise
     */
    @JsonIgnore
    public boolean isKin() { return isRfcRelation(RelationEnum.KIN); }
    /**
     * Tests if the type of this relation is "me".
     *
     * @return true if the type of this relation is "me", false otherwise
     */
    @JsonIgnore
    public boolean isMe() { return isRfcRelation(RelationEnum.ME); }
    /**
     * Tests if the type of this relation is "met".
     *
     * @return true if the type of this relation is "met", false otherwise
     */
    @JsonIgnore
    public boolean isMet() { return isRfcRelation(RelationEnum.MET); }
    /**
     * Tests if the type of this relation is "muse".
     *
     * @return true if the type of this relation is "muse", false otherwise
     */
    @JsonIgnore
    public boolean isMuse() { return isRfcRelation(RelationEnum.MUSE); }
    /**
     * Tests if the type of this relation is "neighbor".
     *
     * @return true if the type of this relation is "neighbor", false otherwise
     */
    @JsonIgnore
    public boolean isNeighbor() { return isRfcRelation(RelationEnum.NEIGHBOR); }

    /**
     * Tests if the type of this relation is "parent".
     *
     * @return true if the type of this relation is "parent", false otherwise
     */
    @JsonIgnore
    public boolean isParent() { return isRfcRelation(RelationEnum.PARENT); }

    /**
     * Tests if the type of this relation is "sibling".
     *
     * @return true if the type of this relation is "sinling", false otherwise
     */
    @JsonIgnore
    public boolean isSibling() { return isRfcRelation(RelationEnum.SIBLING); }
    /**
     * Tests if the type of this relation is "spouse".
     *
     * @return true if the type of this relation is "spouse", false otherwise
     */
    @JsonIgnore
    public boolean isSpouse() { return isRfcRelation(RelationEnum.SPOUSE); }
    /**
     * Tests if the type of this relation is "sweetheart".
     *
     * @return true if the type of this relation is "sweetheart", false otherwise
     */
    @JsonIgnore
    public boolean isSweetheart() { return isRfcRelation(RelationEnum.SWEETHEART); }
    /**
     * Tests if the type of this relation is a custom type.
     *
     * @return true if the type of this relation is a custom type, false otherwise
     */
    @JsonIgnore
    public boolean isExtRelation() { return extValue != null; }

    /**
     * Returns a relation type whose enum value is pre-defined.
     *
     * @param rfcValue the pre-defined relation type
     * @return a pre-defined relation type
     */
    public static RelationType rfcRelation(RelationEnum rfcValue) { return RelationType.builder().rfcValue(rfcValue).build();}
    /**
     * Returns an "acquaintance" relation type.
     *
     * @return an "acquaintance" relation type
     */
    public static RelationType acquaintance() { return rfcRelation(RelationEnum.ACQUAINTANCE);}
    /**
     * Returns an "agent" relation type.
     *
     * @return an "agent" relation type
     */
    public static RelationType agent() { return rfcRelation(RelationEnum.AGENT);}
    /**
     * Returns a "child" relation type.
     *
     * @return a "child" relation type
     */
    public static RelationType child() { return rfcRelation(RelationEnum.CHILD);}
    /**
     * Returns a "colleague" relation type.
     *
     * @return a "colleague" relation type
     */
    public static RelationType colleague() { return rfcRelation(RelationEnum.COLLEAGUE);}
    /**
     * Returns a "contact" relation type.
     *
     * @return a "contact" relation type
     */
    public static RelationType contact() { return rfcRelation(RelationEnum.CONTACT);}
    /**
     * Returns a "co-resident" relation type.
     *
     * @return a "co-resident" relation type
     */
    public static RelationType coResident() { return rfcRelation(RelationEnum.CO_RESIDENT);}
    /**
     * Returns a "co-worker" relation type.
     *
     * @return a "co-worker" relation type
     */
    public static RelationType coWorker() { return rfcRelation(RelationEnum.CO_WORKER);}
    /**
     * Returns a "crush" relation type.
     *
     * @return a "crush" relation type
     */
    public static RelationType crush() { return rfcRelation(RelationEnum.CRUSH);}
    /**
     * Returns a "date" relation type.
     *
     * @return a "date" relation type
     */
    public static RelationType date() { return rfcRelation(RelationEnum.DATE);}
    /**
     * Returns "emergency" as relation type.
     *
     * @return "emergency" as relation type
     */
    public static RelationType emergency() { return rfcRelation(RelationEnum.EMERGENCY);}
    /**
     * Returns "friend" as relation type.
     *
     * @return "friend" as relation type
     */
    public static RelationType friend() { return rfcRelation(RelationEnum.FRIEND);}
    /**
     * Returns a "kin" relation type.
     *
     * @return a "kin" relation type
     */
    public static RelationType kin() { return rfcRelation(RelationEnum.KIN);}
    /**
     * Returns a "me" relation type.
     *
     * @return a "me" relation type
     */
    public static RelationType me() { return rfcRelation(RelationEnum.ME);}
    /**
     * Returns a "met" relation type.
     *
     * @return a "met" relation type
     */
    public static RelationType met() { return rfcRelation(RelationEnum.MET);}
    /**
     * Returns a "muse" relation type.
     *
     * @return a "muse" relation type
     */
    public static RelationType muse() { return rfcRelation(RelationEnum.MUSE);}
    /**
     * Returns a "neighbor" relation type.
     *
     * @return a "neighbor" relation type
     */
    public static RelationType neighbor() { return rfcRelation(RelationEnum.NEIGHBOR);}
    /**
     * Returns a "parent" relation type.
     *
     * @return a "parent" relation type
     */
    public static RelationType parent() { return rfcRelation(RelationEnum.PARENT);}
    /**
     * Returns a "sibling" relation type.
     *
     * @return a "sibling" relation type
     */
    public static RelationType sibling() { return rfcRelation(RelationEnum.SIBLING);}
    /**
     * Returns a "spouse" relation type.
     *
     * @return a "spouse" relation type
     */
    public static RelationType spouse() { return rfcRelation(RelationEnum.SPOUSE);}
    /**
     * Returns a "sweetheart" relation type.
     *
     * @return a "sweetheart" relation type
     */
    public static RelationType sweetheart() { return rfcRelation(RelationEnum.SWEETHEART);}
    /**
     * Returns a custom relation type.
     *
     * @param extValue the custom relation type
     * @return a custom relation type
     */
    public static RelationType extRelation(String extValue) { return RelationType.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }

}
