package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @NotNull(message = "name is missing in Organization")
    @NonNull
    @Valid
    LocalizedString name;

    @Valid
    LocalizedString[] units;
}
