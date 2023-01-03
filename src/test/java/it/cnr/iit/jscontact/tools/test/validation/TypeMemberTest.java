package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Card;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.*;

public class TypeMemberTest {

    @Test
    public void testInvalidTypeMemberValue() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-InvalidTypeMemberValue.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toJSCard(json);
        assertFalse("testInvalidTypeMemberValue-1", jsCard.isValid());
        assertEquals("testInvalidTypeMemberValue-2", "invalid @type value in Address", jsCard.getValidationMessage());

    }
}
