package it.cnr.iit.jscontact.tools.dto.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for serializing objects into formatted JSON.
 *
 * @author Mario Loffredo
 */
public class PrettyPrintSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * Returns the formatted JSON text of a given object
     *
     * @param o the object to serialize
     * @return the formatted JSON text
     * @throws JsonProcessingException if the jCard cannot be serialized
     */
    public static String print(Object o) throws JsonProcessingException {

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);

    }
}
