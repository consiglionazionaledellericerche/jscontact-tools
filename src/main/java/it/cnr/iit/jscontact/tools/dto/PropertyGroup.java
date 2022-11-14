package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the PropertyGroup type as defined in section 2.x.x of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.x.x">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","label","members"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyGroup implements Serializable {

    @NotNull
    @Pattern(regexp = "PropertyGroup", message="invalid @type value in PropertyGroup")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "PropertyGroup";

    String label;

    @NotNull(message = "members is missing in PropertyGroup")
    @NonNull
    @BooleanMapConstraint(message = "invalid Map<String,Boolean> members in PropertyGroup - Only Boolean.TRUE allowed")
    Map<String,Boolean> members;

}
