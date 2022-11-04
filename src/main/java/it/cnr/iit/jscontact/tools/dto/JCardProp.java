package it.cnr.iit.jscontact.tools.dto;

import ezvcard.VCardDataType;
import ezvcard.parameter.VCardParameters;
import it.cnr.iit.jscontact.tools.dto.utils.VCardUtils;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
/**
 * Class mapping the JCardProp type as defined in section 2.15.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.15.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JCardProp {

    @NonNull
    String name;

    @Builder.Default
    Map<String,Object> parameters = new HashMap<>();

    VCardDataType type;

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
