package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the File type as defined in section 2.3.4 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.3.4">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File implements IdMapValue, Serializable {

    @NotNull
    @Pattern(regexp = "File", message="invalid @type value in File")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "File";

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
