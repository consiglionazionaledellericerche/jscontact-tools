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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class mapping the keys of the "contexts" map as defined in section 1.5.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-1.5.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class Context extends ExtensibleEnum<ContextEnum> implements Serializable {

    /**
     * Tests if this is a "private" context.
     *
     * @return true if this is a "private" context, false otherwise
     */
    @JsonIgnore
    public boolean isPrivate() { return isRfc(ContextEnum.PRIVATE); }
    /**
     * Tests if this is a "work" context.
     *
     * @return true if this is a "work" context, false otherwise
     */
    @JsonIgnore
    public boolean isWork() { return isRfc(ContextEnum.WORK); }

    /**
     * Returns a context whose enum value is pre-defined.
     *
     * @param rfcValue the pre-defined context
     * @return a pre-defined context
     */
    public static Context rfc(ContextEnum rfcValue) { return Context.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "private" context.
     *
     * @return a "private" context
     */
    public static Context private_() { return rfc(ContextEnum.PRIVATE);}
    /**
     * Returns a "work" context.
     *
     * @return a "work" context
     */
    public static Context work() { return rfc(ContextEnum.WORK);}
    /**
     * Returns a custom context.
     *
     * @param extValue the custom context in text format
     * @return a custom context
     */
    public static Context ext(String extValue) { return Context.builder().extValue(extValue).build(); }

    /**
     * Returns the list of enum values corresponding to those whose type is known in a given collection of contexts.
     *
     * @param contexts the list of contexts
     * @return list of enum values corresponding to those contexts whose type is known
     */
    public static List<ContextEnum> toEnumValues(Collection<Context> contexts) {

        if (contexts == null)
            return null;

        List<ContextEnum> enumValues = new ArrayList<>();
        for (Context context : contexts) {
            if (context.rfcValue != null)
                enumValues.add(context.getRfcValue());
        }

        return enumValues;
    }

}
