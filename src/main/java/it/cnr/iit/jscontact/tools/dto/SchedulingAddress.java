package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.constraints.UriConstraint;
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the SchedulingAddress type as defined in section 2.4.2 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.4.2">Section 2.4.2 of RFC9553</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type", "uri", "contexts", "pref", "label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SchedulingAddress extends AbstractJSContactType implements HasLabel, IdMapValue, IsIANAType, HasContexts, Serializable {

    @Pattern(regexp = "SchedulingAddress", message="invalid @type value in SchedulingAddress")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "SchedulingAddress";

    @NotNull(message = "uri is missing in SchedulingAddress")
    @NonNull
    @UriConstraint
    @JsonProperty("uri")
    String uri;

    @JsonSerialize(using = ContextsSerializer.class)
    @JsonDeserialize(using = ContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Resource - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ContainsExtensibleEnum(enumClass = ContextEnum.class, getMethod = "getContexts")
    Map<Context,Boolean> contexts;

    @Min(value=1, message = "invalid pref in Resource - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Resource - value must be less or equal than 100")
    Integer pref;

    String label;
}
