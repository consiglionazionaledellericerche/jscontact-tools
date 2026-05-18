package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ezvcard.VCardDataType;
import it.cnr.iit.jscontact.tools.dto.deserializers.VCardDataTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.VCardParamsDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.VCardDataTypeSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.VCardParamsSerializer;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Class mapping the VCardProperty type as defined in section 4.1.1 of [RFC9555bis].
 *
 * @author Mario Loffredo
 * @see <a href="https://www.ietf.org/archive/id/draft-stepanek-rfc9555bis-00.html#name-vcard">Section 4.1.1 of RFC9555bis</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VCardProperty {

    @NotNull(message = "name is missing in VCardProperty")
    @NonNull
    String name;

    @JsonSerialize(using = VCardDataTypeSerializer.class)
    @JsonDeserialize(using = VCardDataTypeDeserializer.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    VCardDataType valueType;

    /**
     * @see <a href="https://www.ietf.org/archive/id/draft-stepanek-rfc9555bis-00.html#name-vcard">Section 4.1.1 of RFC9555bis</a>
     */
    @JsonProperty("parameters")
    @JsonSerialize(using = VCardParamsSerializer.class)
    @JsonDeserialize(using = VCardParamsDeserializer.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Valid
    Map<String, VCardParam> parameters;

}
