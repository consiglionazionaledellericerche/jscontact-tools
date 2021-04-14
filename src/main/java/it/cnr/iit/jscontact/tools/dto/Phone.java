package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone implements IdMapValue {

    @NotNull(message = "phone is missing in Phone")
    @NonNull
    String phone;

    @BooleanMapConstraint(message = "invalid Map<PhoneType,Boolean> features in Phone - Only Boolean.TRUE allowed")
    Map<PhoneFeature,Boolean> features;

    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Phone - Only Boolean.TRUE allowed")
    Map<Context,Boolean> contexts;

    String label;

    @Min(value=1, message = "invalid pref in Phone - min value must be 1")
    @Max(value=100, message = "invalid pref in Phone - max value must be 100")
    Integer pref;

}
