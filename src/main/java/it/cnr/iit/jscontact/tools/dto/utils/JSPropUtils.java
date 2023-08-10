package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Calendar;

/**
 * Utility class for handling vCard JSPROP properties.
 *
 * @author Mario Loffredo
 */
public class JSPropUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a JSContact object into the vCard JSPROP property value
     *
     * @param o the JSContact object
     * @return the vCard JSPROP property value
     */
    public static String toJSPropValue(Object o) throws JsonProcessingException {

        if (o == null)
            return null;

        if (o instanceof String)
            return String.format("\"%s\"", o);
        else if (o instanceof Calendar)
            return String.format("\"%s\"", DateUtils.toString((Calendar) o));
        else if (o instanceof Boolean || o instanceof Integer)
            return String.format("%s", o);
        else {
            return String.format("%s", mapper.writeValueAsString(o));
        }
    }


    /**
     * Converts vCard JSPROP property value into a JSContact object
     *
     * @param s the vCard JSPROP property value
     * @return the JSContact object
     */
    public static Object toJSContactObject(String s) throws JsonProcessingException {

        if (s == null)
            return null;

        JsonNode node = mapper.readTree(s);
        if (node.isObject() || node.isArray()) {
            return mapper.readValue(s,Object.class);
        } else {
            if (node.isTextual()) {
                try {
                       return DateUtils.toCalendar(node.asText());
                } catch (Exception e) {
                    return node.asText();
                }
            }
            else if (node.isBoolean())
                return node.asBoolean();
            else if (node.isLong())
                return node.asLong();
            else if (node.isInt())
                return node.asInt();
            else
                return node.asDouble();
        }
    }

}
