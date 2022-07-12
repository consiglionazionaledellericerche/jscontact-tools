package it.cnr.iit.jscontact.tools.test.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class TypeMemberTest {

    @Test
    public void testInvalidTypeMemberValue() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-InvalidTypeMemberValue.json"), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        assertFalse("testInvalidTypeMemberValue-1", jsCard.isValid());
        assertEquals("testInvalidTypeMemberValue-2", "invalid @type value in Address", jsCard.getValidationMessage());

    }
}
