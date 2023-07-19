package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.DirectoryResourceTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.PronounceSystemDeserializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the Pronounce type as defined in section 1.5.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-1.5.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","phonetics","script","system"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Pronounce extends AbstractJSContactType implements Serializable {

    @Pattern(regexp = "Pronounce", message="invalid @type value in Pronounce")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Pronounce";

    @NotNull(message = "phonetics is missing in Pronounce")
    @NonNull
    String phonetics;

    String script;

    @JsonDeserialize(using = PronounceSystemDeserializer.class)
    PronounceSystem system;

}
