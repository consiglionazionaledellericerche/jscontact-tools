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
 * Class mapping the values of the "kind" property of the Link type as defined in section 2.6.43 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.3">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class LinkResourceKind extends ExtensibleEnumType<LinkResourceEnum> implements Serializable {

    /**
     * Tests if this link resource type is "contact".
     *
     * @return true if link resource type is "photo", false otherwise
     */
    @JsonIgnore
    public boolean isContact() { return isRfc(LinkResourceEnum.CONTACT); }

    private static LinkResourceKind rfc(LinkResourceEnum rfcValue) { return LinkResourceKind.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "contact" link resource type.
     *
     * @return a "contact" link resource type
     */
    public static LinkResourceKind contact() { return rfc(LinkResourceEnum.CONTACT);}

    /**
     * Returns a custom link resource type.
     *
     * @return a custom link resource type
     */
    private static LinkResourceKind ext(String extValue) { return LinkResourceKind.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}
