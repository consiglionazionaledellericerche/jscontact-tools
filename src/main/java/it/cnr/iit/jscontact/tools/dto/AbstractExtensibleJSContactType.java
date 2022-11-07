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
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class mapping the vCard extensions in section 1.5.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-1.5.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractExtensibleJSContactType {

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


    public void buildAllExtensionsMap(Map<String,Object> map, String jsonPointer) {

        if (extensions != null) {
            for (Map.Entry<String,Object> extension : extensions.entrySet())
                map.put(String.format("%s%s", jsonPointer, extension.getKey()), extension.getValue());
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getDeclaringClass().isPrimitive())
                continue;
            else if (field.getType().isArray()) {
                try {
                    AbstractExtensibleJSContactType[] subarray = (AbstractExtensibleJSContactType[]) field.get(this);
                    if (subarray != null) {
                        int i = 0;
                        for (AbstractExtensibleJSContactType o : subarray)
                            o.buildAllExtensionsMap(map, String.format("%s%s/%d/", jsonPointer, field.getName(), i++));
                    }
                } catch(Exception e) {}
            } else if (Map.class.isAssignableFrom(field.getType())) {
                try {
                    Map<String, AbstractExtensibleJSContactType> submap = (Map<String, AbstractExtensibleJSContactType>) field.get(this);
                    if (submap != null) {
                        for (Map.Entry<String, AbstractExtensibleJSContactType> entry : submap.entrySet())
                            entry.getValue().buildAllExtensionsMap(map, String.format("%s%s/%s/", jsonPointer, field.getName(), entry.getKey()));
                    }
                } catch(Exception e) {
                    try {
                        Map<String, AbstractExtensibleJSContactType[]> submap2 = (Map<String, AbstractExtensibleJSContactType[]>) field.get(this);
                        if (submap2 != null) {
                            for (Map.Entry<String, AbstractExtensibleJSContactType[]> entry2 : submap2.entrySet()) {
                                AbstractExtensibleJSContactType[] subarray2 = entry2.getValue();
                                if (subarray2 != null) {
                                    int i = 0;
                                    for (AbstractExtensibleJSContactType o : subarray2)
                                        o.buildAllExtensionsMap(map, String.format("%s%s/%s/%d/", jsonPointer, field.getName(), entry2.getKey(), i++));
                                }
                            }
                        }
                    } catch (Exception e2) {}
                }
            } else {
                try {
                    AbstractExtensibleJSContactType o = ((AbstractExtensibleJSContactType) field.get(this));
                    o.buildAllExtensionsMap(map, String.format("%s%s/", jsonPointer, field.getName()));
                } catch (Exception e) {}
            }
        }
    }



    public void addExtension(List<String> pathItems, String extension, Object value) {

        if (pathItems.isEmpty()) {
            addExtension(extension,value);
            return;
        }

        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (!pathItems.get(0).equals(field.getName()))
                    continue;

                if (field.getType().isArray()) {
                    int index = Integer.parseInt(pathItems.get(1));
                    AbstractExtensibleJSContactType[] subarray = (AbstractExtensibleJSContactType[]) field.get(this);
                    if (subarray != null)
                        subarray[index].addExtension(pathItems.subList(2, pathItems.size()), extension, value);
                }
                else if (Map.class.isAssignableFrom(field.getType())) {
                    try {
                        Map<String, AbstractExtensibleJSContactType> submap = (Map<String, AbstractExtensibleJSContactType>) field.get(this);
                        if (submap != null)
                            submap.get(pathItems.get(1)).addExtension(pathItems.subList(2, pathItems.size()), extension, value);
                    } catch(Exception e) {
                        try {
                            Map<String, AbstractExtensibleJSContactType[]> submap2 = (Map<String, AbstractExtensibleJSContactType[]>) field.get(this);
                            if (submap2 != null) {
                                    AbstractExtensibleJSContactType[] subarray2 = submap2.get(pathItems.get(1));
                                    int index = Integer.parseInt(pathItems.get(2));
                                    if (subarray2 != null)
                                        subarray2[index].addExtension(pathItems.subList(3, pathItems.size()), extension, value);
                            }
                        } catch (Exception e2) {}
                    }
                } else {
                    try {
                        AbstractExtensibleJSContactType o = ((AbstractExtensibleJSContactType) field.get(this));
                        o.addExtension(pathItems.subList(1,pathItems.size()),extension, value);
                    } catch (Exception e) {}
                }

            }
        } catch(Exception e) {


        }
    }
}
