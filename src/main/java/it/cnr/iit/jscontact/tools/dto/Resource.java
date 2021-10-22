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
import it.cnr.iit.jscontact.tools.dto.utils.LabelUtils;
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

    @Builder.Default
    ResourceType type = ResourceType.OTHER;

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
     * Tests if this is resource is identified by an URI.
     *
     * @return true if this resource is identified by an URI, false otherwise
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
    /**
     * Tests if this is resource is identified by an identifier not covered by any of the known types.
     *
     * @return true if this resource is identified by a user name, false otherwise
     */
    @JsonIgnore
    public boolean isOther() { return type == ResourceType.OTHER; }

    @JsonIgnore
    private boolean isResource(OnlineLabelKey labelKey) { return LabelUtils.labelIncludesItem(label,labelKey.getValue()); }
    /**
     * Tests if this resource maps a vCard 4.0 CALADRURI property as defined in section 6.9.2 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 CALADRURI property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.2">RFC6350</a>
     */
    @JsonIgnore
    public boolean isCaladruri() { return isResource(OnlineLabelKey.CALADRURI); }
    /**
     * Tests if this resource maps a vCard 4.0 CALURI property as defined in section 6.9.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 CALURI property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isCaluri() { return isResource(OnlineLabelKey.CALURI); }
    /**
     * Tests if this resource maps a vCard 4.0 CONTACT-URI property as defined in section 2.1 of [RFC8605].
     *
     * @return true if this resource maps a vCard 4.0 CONTACT-URI property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc8605#section-2.1">RFC6350</a>
     */
    @JsonIgnore
    public boolean isContactUri() { return isResource(OnlineLabelKey.CONTACT_URI); }
    /**
     * Tests if this resource maps a vCard 4.0 FBURL property as defined in section 6.9.1 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 FBURL property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.1">RFC6350</a>
     */
    @JsonIgnore
    public boolean isFburl() { return isResource(OnlineLabelKey.FBURL); }
    /**
     * Tests if this resource maps a vCard 4.0 KEY property as defined in section 6.8.1 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 KEY property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.8.1">RFC6350</a>
     */
    @JsonIgnore
    public boolean isKey() { return isResource(OnlineLabelKey.KEY); }
    /**
     * Tests if this resource maps a vCard 4.0 IMPP property as defined in section 6.4.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 IMPP property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.4.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isImpp() { return isResource(OnlineLabelKey.IMPP); }
    /**
     * Tests if this resource maps a vCard 4.0 LOGO property as defined in section 6.6.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 LOGO property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.6.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isLogo() { return isResource(OnlineLabelKey.LOGO); }
    /**
     * Tests if this resource maps a vCard 4.0 ORG-DIRECTORY property as defined in section 6.2.4 of [RFC6715].
     *
     * @return true if this resource maps a vCard 4.0 ORG-DIRECTORY property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6715.html#section-2.4">RFC6715</a>
     */
    @JsonIgnore
    public boolean isOrgDirectory() { return isResource(OnlineLabelKey.ORG_DIRECTORY); }
    /**
     * Tests if this resource maps a vCard 4.0 SOUND property as defined in section 6.7.5 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 SOUND property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.7.5">RFC6350</a>
     */
    @JsonIgnore
    public boolean isSound() { return isResource(OnlineLabelKey.SOUND); }
    /**
     * Tests if this resource maps a vCard 4.0 SOURCE property as defined in section 6.1.3 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 SOURCE property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.1.3">RFC6350</a>
     */
    @JsonIgnore
    public boolean isSource() { return isResource(OnlineLabelKey.SOURCE); }
    /**
     * Tests if this resource maps a vCard 4.0 URL property as defined in section 6.7.8 of [RFC6350].
     *
     * @return true if this resource maps a vCard 4.0 URL property, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.7.8">RFC6350</a>
     */
    @JsonIgnore
    public boolean isUrl() { return isResource(OnlineLabelKey.URL); }


    private static Resource resource(OnlineLabelKey labelKey, String resource) {
        return Resource.builder()
                       .resource(resource)
                       .type((labelKey == OnlineLabelKey.IMPP) ? ResourceType.USERNAME : ResourceType.URI)
                       .label(labelKey.getValue())
                       .build();
    }
    /**
     * Tests a resource mapping a vCard 4.0 CALADRURI property as defined in section 6.9.2 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.2">RFC6350</a>
     */
    public static Resource caladruri(String resource) { return resource(OnlineLabelKey.CALADRURI, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 CALURI property as defined in section 6.9.3 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.3">RFC6350</a>
     */
    public static Resource caluri(String resource) { return resource(OnlineLabelKey.CALURI, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 CONTACT-URI property as defined in section 2.1 of [RFC8605].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc8605#section-2.1">RFC6350</a>
     */
    public static Resource contactUri(String resource) { return resource(OnlineLabelKey.CONTACT_URI, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 FBURL property as defined in section 6.9.1 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.9.1">RFC6350</a>
     */
    public static Resource fburl(String resource) { return resource(OnlineLabelKey.FBURL, resource);}

    /**
     * Returns a resource mapping a vCard 4.0 IMPP property as defined in section 6.4.3 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.4.3">RFC6350</a>
     */
    public static Resource impp(String resource) { return resource(OnlineLabelKey.IMPP, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 KEY property as defined in section 6.8.1 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.8.1">RFC6350</a>
     */
    public static Resource key(String resource) { return resource(OnlineLabelKey.KEY, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 LOGO property as defined in section 6.6.3 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.6.3">RFC6350</a>
     */
    public static Resource logo(String resource) { return resource(OnlineLabelKey.LOGO, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 ORG-DIRECTORY property as defined in section 6.2.4 of [RFC6715].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6715.html#section-2.4">RFC6715</a>
     */
    public static Resource orgDirectory(String resource) { return resource(OnlineLabelKey.ORG_DIRECTORY, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 SOUND property as defined in section 6.7.5 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.7.5">RFC6350</a>
     */
    public static Resource sound(String resource) { return resource(OnlineLabelKey.SOUND, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 SOURCE property as defined in section 6.1.3 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.1.3">RFC6350</a>
     */
    public static Resource source(String resource) { return resource(OnlineLabelKey.SOURCE, resource);}
    /**
     * Returns a resource mapping a vCard 4.0 URL property as defined in section 6.7.8 of [RFC6350].
     *
     * @return the resource
     * @see <a href="https://datatracker.ietf.org/doc/rfc6350#section-6.7.8">RFC6350</a>
     */
    public static Resource url(String resource) { return resource(OnlineLabelKey.URL, resource);}

}
