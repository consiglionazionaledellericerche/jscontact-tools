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
 * Class mapping the values of the "kind" property of the Link type as defined in section 2.6.3 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.6.3">Section 2.6.3 of RFC9553</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class LinkKind extends ExtensibleEnumType<LinkEnum> implements Serializable {

    /**
     * Tests if this link resource type is "contact".
     *
     * @return true if link resource type is "photo", false otherwise
     */
    @JsonIgnore
    public boolean isContact() { return isRfc(LinkEnum.CONTACT); }

    private static LinkKind rfc(LinkEnum rfcValue) { return LinkKind.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "contact" link resource type.
     *
     * @return a "contact" link resource type
     */
    public static LinkKind contact() { return rfc(LinkEnum.CONTACT);}

    /**
     * Returns a custom link resource type.
     *
     * @return a custom link resource type
     */
    private static LinkKind ext(String extValue) { return LinkKind.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}
