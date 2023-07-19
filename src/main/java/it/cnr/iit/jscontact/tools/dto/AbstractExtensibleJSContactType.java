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
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.annotations.JSContactCollection;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasPronounce;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JSContactPropUtils;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Abstract class mapping the vCard extensions in section 1.8.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-1.8.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractExtensibleJSContactType {

    private static final ObjectMapper mapper = new ObjectMapper();

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


    private String getSafeJsonPointerFieldName(String fieldName) {

        return fieldName.replaceAll(DelimiterUtils.SLASH_DELIMITER,DelimiterUtils.SLASH_DELIMITER_IN_JSON_POINTER);

    }

    private  static String removeLastChar(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }
    public void buildAllExtensionsMap(Map<String,Object> map, String jsonPointer) {

        if (this instanceof HasKind) { //The kind property must be considered, if any
            HasKind o = (HasKind) this;
            if (o.getKind()!=null && o.getKind().isExtValue()) // extended value for a type
                map.put(String.format("%s", removeLastChar(jsonPointer)), this);
            else if (o.getKind()==null) // unspecified value for a type
                map.put(String.format("%s", removeLastChar(jsonPointer)), this);
        }

        if (this instanceof HasPronounce && ((HasPronounce) this).getPronounce() != null)  { //The pronounce property must be considered, if any
            map.put(String.format("%s/pronounce", removeLastChar(jsonPointer)), ((HasPronounce) this).getPronounce());
        }
        if (this instanceof HasContexts && ((HasContexts) this).getExtContexts() != null ) { //The extended contexts must be considered, if any
            for (Context context : ((HasContexts) this).getExtContexts())
                map.put(String.format("%scontexts/%s", jsonPointer,context.getExtValue().toString()), true);
        }

        if (this instanceof Address && ((Address) this).getExtContexts() != null ) { //The extended contexts must be considered, if any
            for (AddressContext context : ((Address) this).getExtContexts())
                map.put(String.format("%scontexts/%s", jsonPointer,context.getExtValue().toString()), true);
        }

        if (this instanceof Phone && ((Phone) this).getExtPhoneFeatures() != null ) { //The extended contexts must be considered, if any
            for (PhoneFeature feature : ((Phone) this).getExtPhoneFeatures())
                map.put(String.format("%sfeatures/%s", jsonPointer,feature.getExtValue().toString()), true);
        }

        if (extensions != null) {
            for (Map.Entry<String,Object> extension : extensions.entrySet())
                map.put(String.format("%s%s", jsonPointer, getSafeJsonPointerFieldName(extension.getKey())), extension.getValue());
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().isPrimitive())
                continue;
            else if (field.getType().isArray()) {
                try {
                    AbstractExtensibleJSContactType[] subarray = (AbstractExtensibleJSContactType[]) field.get(this);
                    if (subarray != null) {
                        int i = 0;
                        for (AbstractExtensibleJSContactType o : subarray)
                            o.buildAllExtensionsMap(map, String.format("%s%s/%d/", jsonPointer, getSafeJsonPointerFieldName(field.getName()), i++));
                    }
                } catch(Exception e) {}
            } else if (Map.class.isAssignableFrom(field.getType())) {
                try {
                    Map<String, AbstractExtensibleJSContactType> submap = (Map<String, AbstractExtensibleJSContactType>) field.get(this);
                    if (submap != null) {
                        for (Map.Entry<String, AbstractExtensibleJSContactType> entry : submap.entrySet())
                            entry.getValue().buildAllExtensionsMap(map, String.format("%s%s/%s/", jsonPointer, getSafeJsonPointerFieldName(field.getName()), entry.getKey()));
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
                                        o.buildAllExtensionsMap(map, String.format("%s%s/%s/%d/", jsonPointer, getSafeJsonPointerFieldName(field.getName()), entry2.getKey(), i++));
                                }
                            }
                        }
                    } catch (Exception e2) {}
                }
            } else {
                try {
                    AbstractExtensibleJSContactType o = ((AbstractExtensibleJSContactType) field.get(this));
                    o.buildAllExtensionsMap(map, String.format("%s%s/", jsonPointer, getSafeJsonPointerFieldName(field.getName())));
                } catch (Exception e) {}
            }
        }
    }

    public static Object convertToJSContactType(Class classs, Object value) {

        try {
            String json = mapper.writeValueAsString(value);
            return mapper.readValue(json, classs);
        } catch (Exception e) {
            return null;
        }
    }

    private static void addObjectOnMap(Field field, Class classs, Object o, String key, Object value) {

        if (field.isAnnotationPresent(JSContactCollection.class)) {
            JSContactCollection annotation = field.getAnnotation(JSContactCollection.class);
            try {
                Object typedValue = convertToJSContactType(annotation.itemClass(), value);
                classs.getDeclaredMethod(annotation.addMethod(), String.class, Objects.requireNonNull(typedValue).getClass()).invoke(o, key, typedValue);
            } catch (Exception e) {
                throw new InternalErrorException(String.format("Internal Error: addObjectOnMap - message=%s", e.getMessage()));
            }
        }
    }

    private static void addObjectOnArray(Field field, Object o, Object value) {

        if (field.isAnnotationPresent(JSContactCollection.class)) {
            JSContactCollection annotation = field.getAnnotation(JSContactCollection.class);
            try {
                Object typedValue = convertToJSContactType(annotation.itemClass(), value);
                o.getClass().getDeclaredMethod(annotation.addMethod(), Objects.requireNonNull(typedValue).getClass()).invoke(o, typedValue);
            } catch (Exception e) {
                throw new InternalErrorException(String.format("Internal Error: addObjectOnArray - message=%s", e.getMessage()));
            }
        }
    }

    public void addExtension(List<String> pathItems, String extension, Object value) {
        try {
            if (pathItems.isEmpty()) {
                if (this instanceof HasPronounce && extension.equals("pronounce"))
                    ((HasPronounce) this).setPronounce((Pronounce) convertToJSContactType(Pronounce.class, value));
                else
                    addExtension(extension, value);
                return;
            } else if (pathItems.size() == 1) {

                String pathItem = pathItems.get(0);

                if (this instanceof Phone && pathItem.equals("features")) {
                    ((Phone) this).addFeature(PhoneFeature.ext(extension));
                    return;
                }
                else if (this instanceof Address && pathItem.equals("contexts")) {
                    ((Address) this).addContext(AddressContext.ext(extension));
                    return;
                }
                else if (this instanceof HasContexts && pathItem.equals("contexts")) {
                    ((HasContexts) this).addContext(Context.ext(extension));
                    return;
                }
                else if (this instanceof HasPronounce && extension.equals("pronounce")) {
                    ((HasPronounce) this).setPronounce((Pronounce) value);
                    return;
                }
            }

            for (Field field : this.getClass().getDeclaredFields()) {
                if (!pathItems.get(0).equals(field.getName()))
                    continue;

                if (field.getType().isArray()) {
                    AbstractExtensibleJSContactType[] subarray = (AbstractExtensibleJSContactType[]) field.get(this);
                    if (pathItems.size() == 1)
                        addObjectOnArray(field, this, value);
                    else if (subarray != null) {
                        int index = Integer.parseInt(pathItems.get(1));
                        subarray[index].addExtension(pathItems.subList(2, pathItems.size()), extension, value);
                    }
                }
                else if (Map.class.isAssignableFrom(field.getType())) {
                    try {
                        Map<String, AbstractExtensibleJSContactType> submap = (Map<String, AbstractExtensibleJSContactType>) field.get(this);
                        if (pathItems.size() == 1)
                            addObjectOnMap(field, this.getClass(), this, extension, value);
                        else if (submap != null)
                            submap.get(pathItems.get(1)).addExtension(pathItems.subList(2, pathItems.size()), extension, value);
                    } catch(Exception e) {
                        try {
                            Map<String, AbstractExtensibleJSContactType[]> submap2 = (Map<String, AbstractExtensibleJSContactType[]>) field.get(this);
                            if (submap2 != null) {
                                    AbstractExtensibleJSContactType[] subarray2 = submap2.get(pathItems.get(1));
                                    if (pathItems.size() == 2)
                                        addObjectOnMap(field, this.getClass(), this, extension, value);
                                    else if (subarray2 != null) {
                                        int index = Integer.parseInt(pathItems.get(2));
                                        subarray2[index].addExtension(pathItems.subList(3, pathItems.size()), extension, value);
                                    }
                            }
                        } catch (Exception e2) {
                            throw new InternalErrorException(String.format("Internal Error: addExtension - field=%s message=%s",field.getName(), e.getMessage()));
                        }
                    }
                } else {
                    try {
                        AbstractExtensibleJSContactType o = ((AbstractExtensibleJSContactType) field.get(this));
                        o.addExtension(pathItems.subList(1,pathItems.size()),extension, value);
                    } catch (Exception e) {
                        throw new InternalErrorException(String.format("Internal Error: addExtension - field=%s message=%s",field.getName(), e.getMessage()));
                    }
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new InternalErrorException(String.format("Internal Error: addExtension - message=%s", e.getMessage()));
        }
    }
}
