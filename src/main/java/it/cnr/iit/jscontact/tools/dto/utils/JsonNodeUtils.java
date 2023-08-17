package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Utility class for handling Jackson JsonNode objects.
 *
 * @author Mario Loffredo
 */
public class JsonNodeUtils {

    private static final JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a text into a Jackson TextNode object.
     * @param text the text
     * @return the TextNode object
     */
    public static TextNode textNode(String text) {
        return JSON_NODE_FACTORY.textNode(text);
    }

    /**
     * Converts a text array into a Jackson ArrayNode object.
     * @param array the text array
     * @return the ArrayNode object
     */
    public static ArrayNode textArrayNode(String[] array) {
        ArrayNode arrayNode = JSON_NODE_FACTORY.arrayNode(array.length);
        for (String s : array) arrayNode.add(textNode(s));
        return arrayNode;
    }

    /**
     * Converts a Jackson JsonNode object representing an array into a text array.
     * @param node the JsonNode object
     * @return the text array
     */
    public static String[] asTextArray(JsonNode node) {

        if (node == null)
            return null;

        if (!node.isArray())
            return null;

        int length = node.size();
        String[] array = new String[length];
        for (int i=0;i < array.length ; i++)
            array[i] = node.get(i).asText();
        return array;
    }

    /**
     * Converts a Jackson JsonNode object into a Java object.
     * @param node the JsonNode object
     * @return the object
     */
    public static Object toObject(JsonNode node) {

        if (node.isBoolean())
            return node.asBoolean();
        else if (node.isInt())
            return node.asInt();
        else if (node.isDouble())
            return node.asDouble();
        else if (node.isTextual())
            return node.asText();

        return null;
    }

    /**
     * Converts a Java object into a Jackson JsonNode object.
     * @param object the object
     * @return the JsonNode object
     */
    public static JsonNode toJsonNode(Object object) {

        if (object == null)
            return null;

        try {
            String json = mapper.writeValueAsString(object);
            return mapper.readTree(json);
        } catch (JsonMappingException e) {
            return null;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
