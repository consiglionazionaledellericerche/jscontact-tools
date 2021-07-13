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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneRule {

    @NotNull(message = "@type is missing in TimeZoneRule")
    @JsonProperty(value="@type")
    @Pattern(regexp = "TimeZoneRule", message = "invalid @type in TimeZoneRule")
    @Builder.Default
    String type = "TimeZoneRule";

    @NotNull(message = "start is missing in TimeZoneRule")
    @NonNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar start;

    @NotNull(message = "offsetFrom is missing in TimeZoneRule")
    @NonNull
    String offsetFrom;

    @NotNull(message = "offsetTo is missing in TimeZoneRule")
    @NonNull
    String offsetTo;

}
