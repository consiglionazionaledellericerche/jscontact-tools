package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import it.cnr.iit.jscontact.tools.constraints.UriConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the Author type as defined in section 2.8.3 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.3">Section 2.8.3 of RFC9553</a>
 * @author Mario Loffredo
 */
@NotNullAnyConstraint(fieldNames = {"name","uri"}, message = "at least one not null member between name and uri is required in Author")
@JsonPropertyOrder({"@type","name","uri"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Author extends AbstractJSContactType implements IdMapValue, IsIANAType, Serializable {

    @Pattern(regexp = "Author", message="invalid @type value in Author")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Author";

    String name;

    @UriConstraint
    String uri;
}
