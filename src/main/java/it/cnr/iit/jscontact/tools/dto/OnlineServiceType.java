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
 * Class mapping the values of the "type" property of the OnlineService type as defined in section 2.3.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class OnlineServiceType extends ExtensibleEnumType<OnlineServiceEnum> implements Serializable {

    /**
     * Tests if this online service type is "impp".
     *
     * @return true if this online service type is "impp", false otherwise
     */
    @JsonIgnore
    public boolean isImpp() { return isRfc(OnlineServiceEnum.IMPP); }

    /**
     * Tests if this online service type is "uri".
     *
     * @return true if this online service type is "uri", false otherwise
     */
    @JsonIgnore
    public boolean isUri() { return isRfc(OnlineServiceEnum.URI); }

    /**
     * Tests if this online service type is "username".
     *
     * @return true if this online service type is "username", false otherwise
     */
    @JsonIgnore
    public boolean isUsername() { return isRfc(OnlineServiceEnum.USERNAME); }

    private static OnlineServiceType rfc(OnlineServiceEnum rfcValue) { return OnlineServiceType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "impp" online service type.
     *
     * @return a "impp" online service type
     */
    public static OnlineServiceType impp() { return rfc(OnlineServiceEnum.IMPP);}

    /**
     * Returns a "uri" online service type.
     *
     * @return a "uri" online service type
     */
    public static OnlineServiceType uri() { return rfc(OnlineServiceEnum.URI);}

    /**
     * Returns a "username" online service type.
     *
     * @return a "username" online service type
     */
    public static OnlineServiceType username() { return rfc(OnlineServiceEnum.USERNAME);}

    /**
     * Returns a custom online service type.
     *
     * @return a custom online service type
     */
    private static OnlineServiceType ext(String extValue) { return OnlineServiceType.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}
