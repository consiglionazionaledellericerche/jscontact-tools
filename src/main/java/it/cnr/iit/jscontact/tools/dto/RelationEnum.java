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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensible;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

/**
 * Enum class mapping the "relation" map keys of the Relation type as defined in section 2.1.7 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.1.7">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum RelationEnum implements IsExtensible {

    CONTACT("contact"),
    ACQUAINTANCE("acquaintance"),
    FRIEND("friend"),
    MET("met"),
    CO_WORKER("co-worker"),
    COLLEAGUE("colleague"),
    CO_RESIDENT("co-resident"),
    NEIGHBOR("neighbor"),
    CHILD("child"),
    PARENT("parent"),
    SIBLING("sibling"),
    SPOUSE("spouse"),
    KIN("kin"),
    MUSE("muse"),
    CRUSH("crush"),
    DATE("date"),
    SWEETHEART("sweetheart"),
    ME("me"),
    AGENT("agent"),
    EMERGENCY("emergency");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RelationEnum getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(RelationEnum.class, value);
    }

    @Override
    public String toString() {
        return value;
    }

}
