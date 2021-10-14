package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization implements IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "Organization", message="invalid @type value in Organization")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Organization";

    @NotNull(message = "name is missing in Organization")
    @NonNull
    String name;

    String[] units;
}
