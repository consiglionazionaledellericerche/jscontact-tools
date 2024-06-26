package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the Pronouns type as defined in section 2.2.4 of [RFC9553].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.4">Section 2.2.4 of RFC9553</a>
 */
@JsonPropertyOrder({"@type","pronouns","contexts","pref"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Pronouns extends AbstractJSContactType implements IdMapValue, IsIANAType, Serializable, HasContexts {

    @Pattern(regexp = "Pronouns", message="invalid @type value in Pronouns")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Pronouns";
    
    @NotNull(message = "pronouns is missing in Pronouns")
    @NonNull
    String pronouns;

    @JsonSerialize(using = ContextsSerializer.class)
    @JsonDeserialize(using = ContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Pronouns - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ContainsExtensibleEnum(enumClass = ContextEnum.class, getMethod = "getContexts")
    Map<Context,Boolean> contexts;

    @Min(value=1, message = "invalid pref in Pronouns - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Pronouns - value must be less or equal than 100")
    Integer pref;

}
