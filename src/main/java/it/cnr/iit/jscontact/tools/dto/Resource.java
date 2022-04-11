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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.constraints.UriResourceConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasIndex;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContext;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.HasIndexUtils;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the StreetComponent type as defined in section 2.3.3 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.3.3">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@UriResourceConstraint
@JsonPropertyOrder({"@type","resource","type","mediaType","contexts","label","pref"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends GroupableObject implements HasIndex, Comparable<Resource>, IdMapValue, Serializable, HasContext {

    @NotNull
    @Pattern(regexp = "Resource", message="invalid @type value in Resource")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Resource";

    @NotNull(message = "resource is missing in Resource")
    @NonNull
    String resource;

    ResourceType type;

    String mediaType;

    @JsonSerialize(using = ContextsSerializer.class)
    @JsonDeserialize(using = ContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Resource - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context,Boolean> contexts;

    String label;

    @Min(value=1, message = "invalid pref in Resource - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Resource - value must be less or equal than 100")
    Integer pref;

    @JsonIgnore
    Integer index;

    /**
     * Compares this resource with another based on the value of the "index" property.
     *
     * @param o the object this object must be compared with
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the given object.
     */
    @Override
    public int compareTo(Resource o) {

        return HasIndexUtils.compareTo(this, o);
    }

    /**
     * Tests if this is resource is a uri.
     *
     * @return true if this resource is a uri, false otherwise
     */
    @JsonIgnore
    public boolean isUri() { return type == ResourceType.URI; }

    /**
     * Tests if this is resource is identified by a user name.
     *
     * @return true if this resource is identified by a user name, false otherwise
     */
    @JsonIgnore
    public boolean isUsername() { return type == ResourceType.USERNAME; }

    @JsonIgnore
    private boolean isResource(ResourceType type) { return this.type == type; }

    @JsonIgnore
    private boolean isResource(ResourceType type, String label) { return this.type == type && this.label.equals(label);}

    /**
     * Tests if this resource maps a vCard 4.0 CALURI property as defined in section 6.9.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 CALURI property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isCalendar() { return isResource(ResourceType.CALURI); }
    /**
     * Tests if this resource maps a vCard 4.0 CONTACT-URI property as defined in section 2.1 of [RFC8605].
     *
     * @return true if this resource maps a vCard 4.0 CONTACT-URI property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc8605#section-2.1">RFC6350</a>
     */
    @JsonIgnore
    public boolean isContactUri() { return isResource(ResourceType.CONTACT_URI); }
    /**
     * Tests if this resource maps a vCard 4.0 FBURL property as defined in section 6.9.1 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 FBURL property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.1">RFC6350</a>
     */
    @JsonIgnore
    public boolean isFreeBusy() { return isResource(ResourceType.FBURL); }
    /**
     * Tests if this resource maps a vCard 4.0 KEY property as defined in section 6.8.1 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 KEY property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.8.1">RFC6350</a>
     */
    @JsonIgnore
    public boolean isPublicKey() { return isResource(ResourceType.KEY); }
    /**
     * Tests if this resource maps a vCard 4.0 IMPP property as defined in section 6.4.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 IMPP property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.4.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isImpp() { return isResource(ResourceType.USERNAME, "XMPP"); }
    /**
     * Tests if this resource maps a vCard 4.0 LOGO property as defined in section 6.6.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 LOGO property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.6.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isLogo() { return isResource(ResourceType.LOGO); }
    /**
     * Tests if this resource maps a vCard 4.0 ORG-DIRECTORY property as defined in section 6.2.4 of [RFC6715].
     *
     * @return true if this resource maps a vCard 4.0 ORG-DIRECTORY property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6715.html#section-2.4">RFC6715</a>
     */
    @JsonIgnore
    public boolean isDirectory() { return isResource(ResourceType.ORG_DIRECTORY); }
    /**
     * Tests if this resource maps a vCard 4.0 SOUND property as defined in section 6.7.5 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 SOUND property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.7.5">RFC6350</a>
     */
    @JsonIgnore
    public boolean isAudio() { return isResource(ResourceType.SOUND); }
    /**
     * Tests if this resource maps a vCard 4.0 SOURCE property as defined in section 6.1.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 SOURCE property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.1.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isDirectorySource() { return isResource(ResourceType.SOURCE); }


    private static Resource resource(ResourceType type,  String resource) {

        return resource(type, null, resource);

    }

    private static Resource resource(ResourceType type, String label, String resource) {
        return Resource.builder()
                       .resource(resource)
                       .type(type)
                       .label(label)
                       .build();
    }
    /**
     * Returns a resource mapping a vCard 4.0 CALURI property as defined in section 6.9.3 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.3">RFC6350</a>
     */
    public static Resource caluri(String resource) { return resource(ResourceType.CALURI, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 CONTACT-URI property as defined in section 2.1 of [RFC8605].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc8605#section-2.1">RFC6350</a>
     */
    public static Resource contactUri(String resource) { return resource(ResourceType.CONTACT_URI, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 FBURL property as defined in section 6.9.1 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.1">RFC6350</a>
     */
    public static Resource fburl(String resource) { return resource(ResourceType.FBURL, resource);}

    /**
     * Returns a resource mapping a vCard 4.0 IMPP property as defined in section 6.4.3 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.4.3">RFC6350</a>
     */
    public static Resource impp(String resource) { return resource(ResourceType.USERNAME,  "XMPP", resource);}
    /**
     * Returns a resource mapping a vCard 4.0 KEY property as defined in section 6.8.1 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.8.1">RFC6350</a>
     */
    public static Resource key(String resource) { return resource(ResourceType.KEY, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 LOGO property as defined in section 6.6.3 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.6.3">RFC6350</a>
     */
    public static Resource logo(String resource) { return resource(ResourceType.LOGO, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 ORG-DIRECTORY property as defined in section 6.2.4 of [RFC6715].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6715.html#section-2.4">RFC6715</a>
     */
    public static Resource orgDirectory(String resource) { return resource(ResourceType.ORG_DIRECTORY, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 SOUND property as defined in section 6.7.5 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.7.5">RFC6350</a>
     */
    public static Resource sound(String resource) { return resource(ResourceType.SOUND, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 SOURCE property as defined in section 6.1.3 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.1.3">RFC6350</a>
     */
    public static Resource source(String resource) { return resource(ResourceType.SOURCE, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 URL property as defined in section 6.7.8 of [RFC6350].
     *
     * @param resource resource identifier
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.7.8">RFC6350</a>
     */
    public static Resource url(String resource) { return resource(ResourceType.URI, resource);}

}
