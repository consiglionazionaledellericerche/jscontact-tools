package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.TitleTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasType;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the Title type as defined in section 2.2.5 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.5">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","name","type","organization"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Title extends AbstractJSContactType implements HasType, IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "Title", message="invalid @type value in Title")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Title";

    @NotNull(message = "name is missing in Title")
    @NonNull
    String name;

    @JsonDeserialize(using = TitleTypeDeserializer.class)
    TitleType type;

    String organization;
}
