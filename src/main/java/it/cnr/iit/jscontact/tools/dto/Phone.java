package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.PhoneFeaturesDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContext;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.PhoneFeaturesSerializer;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone implements IdMapValue, Serializable, HasContext {

    @NotNull
    @Pattern(regexp = "Phone", message="Invalid @type value in Phone")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Phone";
    
    @NotNull(message = "phone is missing in Phone")
    @NonNull
    String phone;

    @JsonSerialize(using = PhoneFeaturesSerializer.class)
    @JsonDeserialize(using = PhoneFeaturesDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<PhoneFeature,Boolean> features in Phone - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<PhoneFeature,Boolean> features;

    @JsonSerialize(using = ContextsSerializer.class)
    @JsonDeserialize(using = ContextsDeserializer.class)
    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Phone - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context,Boolean> contexts;

    String label;

    @Min(value=1, message = "invalid pref in Phone - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Phone - value must be less or equal than 100")
    Integer pref;

    private boolean asFeature(PhoneFeature feature) { return features != null && features.containsKey(feature); }
    public boolean asVoice() { return asFeature(PhoneFeature.voice()); }
    public boolean asFax() { return asFeature(PhoneFeature.fax()); }
    public boolean asPager() { return asFeature(PhoneFeature.pager()); }
    public boolean asVideo() { return asFeature(PhoneFeature.video()); }
    public boolean asCell() { return asFeature(PhoneFeature.cell()); }
    public boolean asText() { return asFeature(PhoneFeature.text()); }
    public boolean asTextphone() { return asFeature(PhoneFeature.textphone()); }
    public boolean asExt(String extValue) { return asFeature(PhoneFeature.ext(extValue)); }
    public boolean asOtherFeature() { return asFeature(PhoneFeature.other()); }

}
