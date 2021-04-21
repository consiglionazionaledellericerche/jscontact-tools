package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContext;
import lombok.*;

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
public class Phone implements IdMapValue, Serializable, HasContext {

    @NotNull(message = "phone is missing in Phone")
    @NonNull
    String phone;

    @BooleanMapConstraint(message = "invalid Map<PhoneType,Boolean> features in Phone - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<PhoneFeature,Boolean> features;

    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Phone - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context,Boolean> contexts;

    String label;

    @Min(value=1, message = "invalid pref in Phone - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Phone - value must be less or equal than 100")
    Integer pref;

    private boolean asFeature(PhoneFeature feature) { return features != null && features.containsKey(feature); }
    public boolean asVoice() { return asFeature(PhoneFeature.VOICE); }
    public boolean asFax() { return asFeature(PhoneFeature.FAX); }
    public boolean asPager() { return asFeature(PhoneFeature.PAGER); }
    public boolean asOtherFeature() { return asFeature(PhoneFeature.OTHER); }

}
