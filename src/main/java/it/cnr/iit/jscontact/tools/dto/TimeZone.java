package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import it.cnr.iit.jscontact.tools.dto.serializers.LocalDateTimeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Calendar;
import java.util.List;

/**
 * Class mapping the TimeZone type as defined in section 4.7.2 of RFC8984].
 *
 * @see <a href="https://datatracker.ietf.org/doc/rfc8984#section-4.7.2">RFC8984</a>
 * @author Mario Loffredo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeZone {

    @NotNull(message = "@type is missing in TimeZone")
    @JsonProperty(value="@type")
    @Pattern(regexp = "TimeZone", message = "invalid @type in TimeZone")
    @Builder.Default
    String type = "TimeZone";

    @NotNull(message = "tzId is missing in TimeZone")
    @NonNull
    String tzId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar updated;

    @Singular(value = "standardItem", ignoreNullCollections = true)
    List<TimeZoneRule> standard;

}
