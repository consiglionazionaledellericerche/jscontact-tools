package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContextsDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.PhoneFeaturesDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContext;
import it.cnr.iit.jscontact.tools.dto.serializers.ContextsSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.PhoneFeaturesSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Class mapping the Phone type as defined in section 2.3.3 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.3">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","phone","features","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone extends AbstractJSContactType implements HasLabel, IdMapValue, Serializable, HasContext {

    @NotNull
    @Pattern(regexp = "Phone", message="invalid @type value in Phone")
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

    @Min(value=1, message = "invalid pref in Phone - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Phone - value must be less or equal than 100")
    Integer pref;

    String label;

    /**
     * Tests if the pheature of this phone is undefined.
     *
     * @return true if the features map is empty, false otherwise
     */
    public boolean hasNoFeature() { return features == null || features.size() ==  0; }


    private boolean asFeature(PhoneFeature feature) { return features != null && features.containsKey(feature); }
    /**
     * Tests if this phone number is for calling by voice.
     *
     * @return true if this phone number isfor calling by voice, false otherwise
     */
    public boolean asVoice() { return asFeature(PhoneFeature.voice()); }
    /**
     * Tests if this phone number is for sending a fax.
     *
     * @return true if this phone number is for sending a fax, false otherwise
     */
    public boolean asFax() { return asFeature(PhoneFeature.fax()); }
    /**
     * Tests if this phone number is for a pager or a beeper.
     *
     * @return true if phone number is for a pager or a beeper, false otherwise
     */
    public boolean asPager() { return asFeature(PhoneFeature.pager()); }
    /**
     * Tests if this phone number supports video conferencing.
     *
     * @return true if this phone number supports video conferencing, false otherwise
     */
    public boolean asVideo() { return asFeature(PhoneFeature.video()); }
    /**
     * Tests if this phone number is for a cell phone.
     *
     * @return true if this phone number is for a cell phone, false otherwise
     */
    public boolean asCell() { return asFeature(PhoneFeature.cell()); }
    /**
     * Tests if this phone number supports text messages (SMS).
     *
     * @return true if this phone number supports text messages, false otherwise
     */
    public boolean asText() { return asFeature(PhoneFeature.text()); }
    /**
     * Tests if this phone number is for a device for people with hearing or speech difficulties.
     *
     * @return true if this phone number is for a device for people with hearing or speech difficulties, false otherwise
     */
    public boolean asTextphone() { return asFeature(PhoneFeature.textphone()); }
    /**
     * Tests if this phone number supports a custom feature.
     *
     * @param extValue the custom feature
     * @return true if this phone number supports a custom purpose, false otherwise
     */
    public boolean asExt(String extValue) { return asFeature(PhoneFeature.ext(extValue)); }

}
