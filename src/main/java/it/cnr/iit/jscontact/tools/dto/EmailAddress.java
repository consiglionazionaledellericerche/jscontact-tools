package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAddress implements IdMapValue, Serializable {

    @NotNull(message = "email is missing in EmailAddress")
    @NonNull
    @Email(message = "invalid email in Email")
    String email;

    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in EmailAddress - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context,Boolean> contexts;

    @Min(value=1, message = "invalid pref in Email - min value must be 1")
    @Max(value=100, message = "invalid pref in Email - max value must be 100")
    Integer pref;

}
