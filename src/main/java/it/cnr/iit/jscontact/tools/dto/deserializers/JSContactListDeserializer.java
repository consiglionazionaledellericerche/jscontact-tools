package it.cnr.iit.jscontact.tools.dto.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.JSCard;
import it.cnr.iit.jscontact.tools.dto.CardGroup;
import it.cnr.iit.jscontact.tools.dto.JSContact;

import java.io.IOException;

public class JSContactListDeserializer extends JsonDeserializer<JSContact> {

    @Override
    public JSContact deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        final ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        final JsonNode node = mapper.readTree(jp);

        if (node.get("members") != null)
            return mapper.treeToValue(node, CardGroup.class);
        else
            return mapper.treeToValue(node, JSCard.class);
    }

}
