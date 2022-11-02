
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.cnr.iit.jscontact.tools.constraints.GroupKeyConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.JCardPropsDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.JCardPropsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Abstract class mapping a JSContact topmost object, namely Card and CardGroup.
 *
 * @author Mario Loffredo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of={"uid"}, callSuper = false)
@SuperBuilder
public abstract class JSContact extends ValidableObject implements Serializable {

    @NotNull(message = "uid is missing in JSContact")
    @NonNull
    String uid;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @GroupKeyConstraint(message = "invalid group key in Map<String,PropertyGroup>")
    Map<String,PropertyGroup> propertyGroups;

    @JsonProperty("ietf.org:rfc0000:props")
    @JsonSerialize(using = JCardPropsSerializer.class)
    @JsonDeserialize(using = JCardPropsDeserializer.class)
    JCardProp[] jCardExtensions;

    /**
     * Adds a property JSONPointer to the members of a group identfied by a group id.
     *
     * @param key the group key
     * @param label the group label
     * @param propertyJSONPointer the JSONPointer of the property included in the group
     */
    public void addPropertyGroup(String key, String label, String propertyJSONPointer) {

        if (propertyGroups == null)
            propertyGroups = new HashMap<>();

        PropertyGroup propertyGroupPerKey = propertyGroups.get(key);
        if (propertyGroupPerKey == null) {
            propertyGroups.put(key, PropertyGroup.builder()
                    .members(new HashMap<String, Boolean>() {{
                        put(propertyJSONPointer, Boolean.TRUE);
                    }})
                    .label(label)
                    .build());
        }
        else {
            Map<String, Boolean> map = propertyGroupPerKey.getMembers();
            map.put(propertyJSONPointer, Boolean.TRUE);
            propertyGroups.replace(key, PropertyGroup.builder()
                    .members(map)
                    .label(label)
                    .build());
        }
    }

    /**
     * Adds a property JSONPointer to the members of a group identfied by a group id.
     *
     * @param key the group key
     * @param propertyJSONPointer the JSONPointer of the property included in the group
     */
    public void addPropertyGroup(String key, String propertyJSONPointer) {
        addPropertyGroup(key, null, propertyJSONPointer);
    }

    /**
     * Deserialize a single JSContact object or an array of JSContact objects
     *
     * @param json the single JSContact object or the array of JSContact objects in JSON
     * @return an array of JSContact objects
     */
    public static JSContact[] toJSContacts(String json) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        objectMapper.registerModule(module);
        try {
            return objectMapper.readValue(json, JSContact[].class);
        } catch(Exception e) {
            return new JSContact[]{objectMapper.readValue(json, JSContact.class)};
        }
    }

    /**
     * Serialize an array of JSContact objects
     *
     * @param jsContacts the array of JSContact objects
     * @return the array of JSContact objects in JSON
     */
    public static String toJson(JSContact[] jsContacts) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(jsContacts);

    }

    /**
     * Adds a JCardProp object to this object.
     *
     * @param o the JCardProp object
     */
    public void addJCardProp(JCardProp o) {

        jCardExtensions = ArrayUtils.add(jCardExtensions, o);
    }


    /**
     * Adds a JCardProp object to this object.
     *
     * @param o the JCardProp object
     */
    @JsonIgnore
    public Map<String,String> getJCardExtensionsAsMap() {

        Map<String,String> map = new HashMap<>();
        if (this.getJCardExtensions() == null)
            return map;

        for (JCardProp jCardExtension : this.getJCardExtensions())
            map.put(jCardExtension.getName(),jCardExtension.getValue().toString());

        return map;
    }
}
