package it.cnr.iit.jscontact.tools.dto;

import ezvcard.VCardDataType;
import ezvcard.parameter.VCardParameters;
import it.cnr.iit.jscontact.tools.dto.utils.VCardUtils;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
/**
 * Class mapping the VCardProp type as defined in section 2.15.1 of [RFC9555].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.15.1">Section 2.15.1 of RFC9555</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VCardProp {

    @NotNull(message = "name is missing in VCardProp")
    @NonNull
    V_Extension name;

    @Builder.Default
    Map<String,Object> parameters = new HashMap<>();

    VCardDataType type;

    @NotNull(message = "value is missing in VCardProp")
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
        return VCardUtils.getVCardParameters(this.parameters, true);
    }

    /**
     * Gets the value of the group parameter.
     *
     * @return the value of the group parameter if any
     */
    public String getGroupParameterValue() {
        return VCardUtils.getVCardParamValue(VCardUtils.getVCardParameters(this.parameters, false), VCardParamEnum.GROUP);
    }


}
