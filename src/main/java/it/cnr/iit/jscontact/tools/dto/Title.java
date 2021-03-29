package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.dto.LocalizedString;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Title {

    @NotNull(message = "title is missing in Title")
    @NonNull
    @Valid
    LocalizedString title;

    String organization;
}