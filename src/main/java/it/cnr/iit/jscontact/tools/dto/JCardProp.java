package it.cnr.iit.jscontact.tools.dto;

import ezvcard.VCardDataType;
import ezvcard.parameter.VCardParameters;
import it.cnr.iit.jscontact.tools.dto.utils.VCardUtils;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
/**
 * Class mapping the JCardProp type as defined in section 2.15.1 of [draft-ietf-calext-jscontact-vcard].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.15.1">draft-ietf-calext-jscontact-vcard</a>
 * @author Mario Loffredo
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JCardProp {

    @NotNull(message = "name is missing in JCardProp")
    @NonNull
    V_Extension name;

    @Builder.Default
    Map<String,Object> parameters = new HashMap<>();

    VCardDataType type;

    @NotNull(message = "value is missing in JCardProp")
    @NonNull
    Object value;

    /**
     * Adds a parameter to this object.
     *
     * @param paramName the parameter name
     * @param paramValue the parameter value
     */
    public void addParameter(String paramName, String paramValue) {

        parameters.putIfAbsent(paramName,paramValue);
    }


    /**
     * Gets the Ezvcard VCardParameters object corresponding to the parameters map.
     *
     * @return the Ezvcard VCardParameters object corresponding to the parameters map
     */
    public VCardParameters getVCardParameters() {
        return VCardUtils.getVCardParameters(this.parameters);
    }



}
