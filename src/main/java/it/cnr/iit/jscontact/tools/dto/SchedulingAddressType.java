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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Class mapping the values of the "type" property of the SchedulingAddress type as defined in section 2.4.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.4.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class SchedulingAddressType extends ExtensibleEnumType<SchedulingAddressEnum> implements Serializable {

    /**
     * Tests if this scheduling address type is "imip".
     *
     * @return true if scheduling address type is "imip", false otherwise
     */
    @JsonIgnore
    public boolean isImip() { return isRfc(SchedulingAddressEnum.IMIP); }


    private static SchedulingAddressType rfc(SchedulingAddressEnum rfcValue) { return SchedulingAddressType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "imip" scheduling address type.
     *
     * @return a "imip" scheduling address type
     */
    public static SchedulingAddressType imip() { return rfc(SchedulingAddressEnum.IMIP);}


    /**
     * Returns a custom calendar resource type.
     *
     * @return a custom calendar resource type
     */
    private static SchedulingAddressType ext(String extValue) { return SchedulingAddressType.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}
