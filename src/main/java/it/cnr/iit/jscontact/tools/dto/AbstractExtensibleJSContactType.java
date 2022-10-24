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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class mapping the vCard 4.0 related properties that can be grouped as defined in section 3.3 of [RFC6350].
 *
 * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-3.3">RFC6350</a>
 * @author Mario Loffredo
 */
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractExtensibleJSContactType {

    @JsonPropertyOrder(alphabetic = true)
    Map<String,Object> extensions;

    @JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @JsonAnySetter
    public void setExtension(String name, Object value) {

        if (extensions == null)
            extensions = new HashMap<>();

        extensions.putIfAbsent(name, value);
    }

    /**
     * Adds an extension to this object.
     *
     * @param key the extension identifier
     * @param value the extension as an object
     */
    public void addExtension(String key, Object value) {
        if(extensions == null)
            extensions = new HashMap<>();

        extensions.putIfAbsent(key,value);
    }

}
