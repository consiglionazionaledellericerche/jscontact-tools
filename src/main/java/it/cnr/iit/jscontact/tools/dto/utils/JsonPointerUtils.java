package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Utility class for handling Jackson JSONPointer expressions.
 *
 * @author Mario Loffredo
 */
public class JsonPointerUtils {

    /**
     * Converts a relative JSONPointer expression in an absolute JSONPointer expression so that it can be processed by Jackson library
     * @param jsonPointer a JSONPointer expression
     * @return jsonPointer prepended with the '/' character, jsonPointer as is otherwise
     */
    public static String toAbsolute(String jsonPointer) {

        if (jsonPointer.charAt(0) != JsonPointer.SEPARATOR)
            return Character.toString(JsonPointer.SEPARATOR) + jsonPointer;

        return jsonPointer;
    }
}
