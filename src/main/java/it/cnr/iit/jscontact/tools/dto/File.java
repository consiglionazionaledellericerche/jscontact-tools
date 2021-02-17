package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @NotNull(message = "href is missing in File")
    @NonNull
    String href;

    String mediaType;

    @Min(value=0, message = "invalid size in File")
    int size;

    Boolean isPreferred;

}
