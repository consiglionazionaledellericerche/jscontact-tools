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
public class Title implements IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "Title", message="invalid @type value in Title")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Title";

    @NotNull(message = "title is missing in Title")
    @NonNull
    String title;

    String organization;
}
