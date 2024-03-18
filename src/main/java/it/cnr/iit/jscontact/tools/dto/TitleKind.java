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
 * Class mapping the "kind" property as defined in section 2.2.6 of [RFC9553].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.6">RFC9553</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class TitleKind extends ExtensibleEnumType<TitleEnum> implements Serializable {

    /**
     * Tests if this type of title is "title".
     *
     * @return true if this type of title is "title", false otherwise
     */
    @JsonIgnore
    public boolean isTitle() { return isRfc(TitleEnum.TITLE); }

    /**
     * Tests if this type of title is "role".
     *
     * @return true if this type of title is "role", false otherwise
     */
    @JsonIgnore
    public boolean isRole() { return isRfc(TitleEnum.ROLE); }


    private static TitleKind rfc(TitleEnum rfcValue) { return TitleKind.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "title" type of title.
     *
     * @return a "title" type of title
     */
    public static TitleKind title() { return rfc(TitleEnum.TITLE);}

    /**
     * Returns a "role" type of title.
     *
     * @return a "role" type of title
     */
    public static TitleKind role() { return rfc(TitleEnum.ROLE);}

    /**
     * Returns a custom kind of contact card.
     *
     * @return a custom kind of contact card
     */
    private static TitleKind ext(String extValue) { return TitleKind.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}
