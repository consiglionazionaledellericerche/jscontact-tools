package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization implements IdMapValue, Serializable {

    @NotNull(message = "name is missing in Organization")
    @NonNull
    @Valid
    LocalizedString name;

    @Valid
    LocalizedString[] units;
}
