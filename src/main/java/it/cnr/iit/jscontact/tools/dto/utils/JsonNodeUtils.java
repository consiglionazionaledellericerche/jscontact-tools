package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonNodeUtils {

    private static final JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;

    public static TextNode textNode(String text) {
        return JSON_NODE_FACTORY.textNode(text);
    }

    public static ArrayNode textArrayNode(String[] array) {
        ArrayNode arrayNode = JSON_NODE_FACTORY.arrayNode(array.length);
        for (int i=0;i < array.length ; i++)
          arrayNode.set(i, textNode(array[i]));
        return arrayNode;
    }
}
