package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.VCardUnconvertedPropsDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.VCardUnconvertedPropsSerializer;
import lombok.*;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 * Class mapping the VCard type as defined in section 4.1.1 of [RFC9555bis].
 *
 * @author Mario Loffredo
 * @see <a href="https://www.ietf.org/archive/id/draft-stepanek-rfc9555bis-00.html#name-vcard">Section 4.1.1 of RFC9555bis</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VCardData {

    @Pattern(regexp = "VCard", message="invalid @type value in VCard")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "VCard";

    @JsonProperty("convertedProperties")
    @Valid
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String,VCardProperty> convertedProperties;

    @JsonProperty("properties")
    @JsonSerialize(using = VCardUnconvertedPropsSerializer.class)
    @JsonDeserialize(using = VCardUnconvertedPropsDeserializer.class)
    @Valid
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    VCardUnconvertedProperty[] properties;

    /**
     * Adds a converted property to this convertedProperties map.
     *
     * @param path the converted property path
     * @param o the object representing the converted property
     */
    public void addConvertedProperty(String path, VCardProperty o) {

        if(convertedProperties == null)
            convertedProperties = new HashMap<>();

        convertedProperties.putIfAbsent(path,o);
    }

    /**
     * Adds a VCardUnconvertedProperty object to the properties array.
     *
     * @param o the VCardUnconvertedProperty object
     */
    public void addUnconvertedProperty(VCardUnconvertedProperty o) {
        properties = ArrayUtils.add(properties, o);
    }

    /**
     * Convert the unconverted propeties array into a map
     * where the keys are the extnsion names and
     * the values are the extension values in text format
     *
     * @return unconverted properties array converted into a map
     */
    @JsonIgnore
    public Map<String,String> getVCardUnconvertedPropertiesAsMap() {

        Map<String,String> map = new HashMap<>();
        if (this.getProperties() == null)
            return map;

        for (VCardUnconvertedProperty jCardExtension : this.getProperties())
            map.put(jCardExtension.getName().toString(),jCardExtension.getValue().toString());

        return map;
    }

}
