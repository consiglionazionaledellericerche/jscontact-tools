package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class mapping the SchedulingAddress type as defined in section 2.4.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.4.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","sendTo","pref"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingAddress extends GroupableObject implements IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "SchedulingAddress", message="invalid @type value in SchedulingAddress")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "SchedulingAddress";

    @NotNull
    @Size(min=1)
    @JsonProperty(required = true)
    @JsonPropertyOrder(alphabetic = true)
    Map<String,String> sendTo;

    @Min(value=1, message = "invalid pref in SchedulingAddress - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in SchedulingAddress - value must be less or equal than 100")
    Integer pref;


    /**
     * Adds a member to this object.
     *
     * @param key the key of the new entry in the "sendTo" map
     * @param value the value of the new entry in the "sendTo" map
     */
    public void addSendTo(String key, String value) {

        if(sendTo == null)
            sendTo = new LinkedHashMap<>();

        sendTo.putIfAbsent(key,value);
    }

}
