package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the OrgUnit type as defined in section 2.2.3 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.3">Section 2.2.3 of RFC9553</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","name","sortAs"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrgUnit extends AbstractJSContactType implements IdMapValue, IsIANAType, Serializable {

    @Pattern(regexp = "OrgUnit", message="invalid @type value in OrgUnit")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "OrgUnit";

    @NotEmpty(message = "name is missing or empty in OrgUnit")
    @NonNull
    String name;

    String sortAs;
}
