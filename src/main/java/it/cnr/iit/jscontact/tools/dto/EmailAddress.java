package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the EmailAddress type as defined in section 2.3.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","email","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAddress extends AbstractJSContactType implements HasLabel, IdMapValue, Serializable, HasContexts {

    @NotNull
    @Pattern(regexp = "EmailAddress", message="invalid @type value in EmailAddress")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "EmailAddress";

    @NotNull(message = "email is missing in EmailAddress")
    @NonNull
    @Email(message = "invalid email in EmailAddress")
    String email;

    @JsonSerialize(using = ContextsSerializer.class)
    @JsonDeserialize(using = ContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in EmailAddress - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context,Boolean> contexts;

    @Min(value=1, message = "invalid pref in EmailAddress - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in EmailAddress - value must be less or equal than 100")
    Integer pref;

    String label;

}
