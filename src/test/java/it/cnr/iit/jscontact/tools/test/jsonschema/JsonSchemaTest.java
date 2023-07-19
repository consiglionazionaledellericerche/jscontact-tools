package it.cnr.iit.jscontact.tools.test.jsonschema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonSchemaTest {

    //@Test
    public void testJsonSchema1() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper);
        JsonSchema jsonSchema = generator.generateSchema(Card.class);
        assertNotNull(jsonSchema);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema));

    }



}