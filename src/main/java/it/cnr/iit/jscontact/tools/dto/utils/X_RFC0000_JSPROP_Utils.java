package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class X_RFC0000_JSPROP_Utils {

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch(Exception e) {
            return null;
        }
    }

    private static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch(Exception e) {
            return null;
        }
    }


    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toX_RFC0000_JSPROPValue(Object o) throws JsonProcessingException {

        if (o == null)
            return null;

        if (o instanceof String)
            return String.format("data:application/json;%%22%s%%22", encodeValue((String) o));
        else if (o instanceof Calendar)
            return String.format("data:application/json;%%22%s%%22", encodeValue(DateUtils.toString((Calendar) o)));
        else if (o instanceof Boolean || o instanceof Integer)
            return String.format("data:application/json;%s", o);
        else {
            return String.format("data:application/json;base64,%s", Base64Utils.encode(mapper.writeValueAsString(o)));
        }
    }

    private static String removeQuotesEscape(String val) {

        String value = val.replaceFirst("%22", "");
        String reverse = new StringBuffer(value).reverse().toString();
        reverse = reverse.replaceFirst("22%", "");
        value = new StringBuffer(reverse).reverse().toString();
        return value;
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
            if (val.charAt(0) == '%') {
                String value = decodeValue(removeQuotesEscape(val));
                try {
                       return DateUtils.toCalendar(value);
                } catch (Exception e) {
                    return value;
                }
            }
            else if (val.equals("true") || val.equals("false"))
                return Boolean.parseBoolean(val);
            else {
                try {
                    return Integer.parseInt(val);
                } catch (Exception e) { }
                try {
                    return Float.parseFloat(val);
                } catch (Exception e) { }
            }
        }

        return null;
    }

}
