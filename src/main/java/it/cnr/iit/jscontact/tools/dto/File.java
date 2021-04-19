package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File implements IdMapValue, Serializable {

    @NotNull(message = "href is missing in File")
    @NonNull
    String href;

    String mediaType;

    @Min(value=0, message = "invalid size in File - value must be greater or equal than 0")
    int size;

    @Min(value=1, message = "invalid pref in File - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in File - value must be less or equal than 100")
    Integer pref;

}
