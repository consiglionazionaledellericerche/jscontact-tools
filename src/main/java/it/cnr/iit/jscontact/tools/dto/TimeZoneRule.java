package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import it.cnr.iit.jscontact.tools.dto.serializers.LocalDateTimeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Calendar;

/**
 * Class mapping the TimeZoneRule type as defined in section 4.7.2 of [RFC8984].
 *
 * @see <a href="https://datatracker.ietf.org/doc/rfc8984#section-4.7.2">RFC8984</a>
 * @author Mario Loffredo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneRule {

    Calendar start;

    String offsetFrom;

    String offsetTo;

}
