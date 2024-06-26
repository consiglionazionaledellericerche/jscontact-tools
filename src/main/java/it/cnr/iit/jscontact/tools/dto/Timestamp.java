package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import it.cnr.iit.jscontact.tools.dto.serializers.UTCDateTimeSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.util.Calendar;

/**
 * Class mapping the Timestamp type as defined in section 2.8.1 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.1">Section 2.8.1 of RFC9553</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","utc"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Timestamp extends AbstractJSContactType implements IsIANAType {

    @NotNull
    @Pattern(regexp = "Timestamp", message="invalid @type value in Timestamp")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Timestamp";

    @NotNull(message = "utc is missing in Timestamp")
    @NonNull
    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar utc;

}
