package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class X_RFC0000_JSPROP_Utils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toValue(Object o) throws JsonProcessingException {

        if (o == null)
            return null;

        if (o instanceof String)
            return String.format("data:application/json;&quot;%s&quot;", o);
        else if (o instanceof Boolean || o instanceof Integer)
            return String.format("data:application/json;%s", o.toString());
        else {
            return String.format("data:application/json;base64,%s", Base64Utils.encode(mapper.writeValueAsString(o)));
        }
    }

}
