package it.cnr.iit.jscontact.tools.dto;

import ezvcard.VCardDataType;
import ezvcard.parameter.VCardParameters;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

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


    public VCardParameters getVCardParameters() {

        VCardParameters parameters = new VCardParameters();

        for (Map.Entry<String,Object> entry : this.parameters.entrySet())
            parameters.put(entry.getKey(), entry.getValue().toString());

        return parameters;
    }

}
