package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonNodeUtils {

    private static final JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;

    public static TextNode textNode(String text) {
        return JSON_NODE_FACTORY.textNode(text);
    }
}
