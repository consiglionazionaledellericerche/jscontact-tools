package it.cnr.iit.jscontact.tools.test.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class TypeMemberTest {

    @Test
    public void testInvalidTypeMemberValue() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-InvalidTypeMemberValue.json"), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        assertTrue("testInvalidTypeMemberValue-1", !jsCard.isValid());
        assertTrue("testInvalidTypeMemberValue-2", jsCard.getValidationMessage().equals("invalid @type value in Address"));

    }
}
