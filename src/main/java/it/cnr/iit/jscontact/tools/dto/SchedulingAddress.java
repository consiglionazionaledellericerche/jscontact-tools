package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.SchedulingAddressTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasType;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Class mapping the SchedulingAddress type as defined in section 2.4.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.4.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","uri","type","mediaType","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingAddress extends Resource implements HasType, IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "SchedulingAddress", message="invalid @type value in SchedulingAddress")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "SchedulingAddress";

    @JsonDeserialize(using = SchedulingAddressTypeDeserializer.class)
    SchedulingAddressType type;

    @JsonIgnore
    private boolean isSchedulingAddress(SchedulingAddressType type) { return this.type.equals(type); }

    /**
     * Tests if this scheduling address is an imip.
     *
     * @return true if this scheduling address is an imip, false otherwise
     */
    @JsonIgnore
    public boolean isImip() { return isSchedulingAddress(SchedulingAddressType.imip()); }

    private static SchedulingAddress resource(SchedulingAddressType type, String uri) {
        return SchedulingAddress.builder()
                .uri(uri)
                .type(type)
                .build();
    }

    /**
     * Returns an imip
     *
     * @param uri imip uri
     * @return the imip
     */
    public static SchedulingAddress imip(String uri) { return resource(SchedulingAddressType.imip(), uri);}


}
