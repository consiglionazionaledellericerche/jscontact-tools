package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.JSContact;

/**
 * Utility class for handling Jackson JSONPointer expressions.
 *
 * @author Mario Loffredo
 */
public class JsonPointerUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a relative JSONPointer expression in an absolute JSONPointer expression so that it can be processed by Jackson library
     * @param jsonPointer a JSONPointer expression
     * @return jsonPointer prepended with the '/' character, jsonPointer as is otherwise
     */
    public static String toAbsolute(String jsonPointer) {

        if (jsonPointer.charAt(0) != JsonPointer.SEPARATOR)
            return JsonPointer.SEPARATOR + jsonPointer;

        return jsonPointer;
    }

    public static JsonNode getPointedJsonNode(JSContact jsContact, String jsonPointerExpr) {

        JsonNode root = mapper.valueToTree(jsContact);

        JsonPointer jsonPointer =  JsonPointer.compile(toAbsolute(jsonPointerExpr));
        return root.at(jsonPointer);
    }


    public static JsonNode getPointedJsonNode(JsonNode root, String jsonPointerExpr) {

        JsonPointer jsonPointer =  JsonPointer.compile(toAbsolute(jsonPointerExpr));
        return root.at(jsonPointer);
    }


}
