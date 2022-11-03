package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class X_RFC0000_JSPROP_Utils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toX_RFC0000_JSPROPValue(Object o) throws JsonProcessingException {

        if (o == null)
            return null;

        if (o instanceof String)
            return String.format("data:application/json;%%22%s%%22", o);
        else if (o instanceof Boolean || o instanceof Integer)
            return String.format("data:application/json;%s", o.toString());
        else {
            return String.format("data:application/json;base64,%s", Base64Utils.encode(mapper.writeValueAsString(o)));
        }
    }

    private static boolean isBase64Value(String s) {
        return s.startsWith("data:application/json;base64,");
    }

    public static Object toJsonValue(String s) throws JsonProcessingException {

        if (s == null)
            return null;

        if (isBase64Value(s)) {
            String val = s.replace("data:application/json;base64,","");
            return mapper.readValue(Base64Utils.decode(val),Object.class);
        } else {
            String val = s.replace("data:application/json;","");
            if (val.charAt(0) == '%')
                return val.replaceAll("%22", "");
            else {
                try {
                    return Integer.parseInt(val);
                } catch (Exception e) { }
                try {
                    return Float.parseFloat(val);
                } catch (Exception e) { }
                try {
                    return Boolean.parseBoolean(val);
                } catch (Exception e) { }
            }
        }

        return null;
    }

}
