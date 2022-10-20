package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the Organization type as defined in section 2.2.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@NotNullAnyConstraint(fieldNames={"name","units"}, message = "at least one not null member other than @type is missing in Organization")
@JsonPropertyOrder({"@type","name","units"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends GroupableObject implements IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "Organization", message="invalid @type value in Organization")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Organization";

    String name;

    String[] units;
}
