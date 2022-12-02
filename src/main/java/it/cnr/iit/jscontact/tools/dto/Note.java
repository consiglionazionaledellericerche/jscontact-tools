package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.UTCDateTimeSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

/**
 * Class mapping the Note type as defined in section 2.8.3 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.3">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","note","created","author"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note extends AbstractJSContactType implements HasLabel, HasContexts, IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "Note", message="invalid @type value in Note")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Note";

    @NotNull(message = "note is missing in Note")
    @NonNull
    String note;

    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar created;

    @Valid
    Author author;

    @JsonSerialize(using = ContextsSerializer.class)
    @JsonDeserialize(using = ContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Note - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context,Boolean> contexts;

    @Min(value=1, message = "invalid pref in Note - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Note - value must be less or equal than 100")
    Integer pref;

    String label;

}
