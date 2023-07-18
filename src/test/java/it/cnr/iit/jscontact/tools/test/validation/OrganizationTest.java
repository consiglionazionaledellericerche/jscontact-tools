package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.Organization;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OrganizationTest extends AbstractTest {

    @Test
    public void testInvalidOrganization1() {

        Map<String, Organization> organizations = new HashMap<String,Organization>() {{ put("ORG-1", Organization.builder().build()); }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .organizations(organizations)
                .build();
        assertFalse("testInvalidOrganization1-1", jsCard.isValid());
        assertEquals("testInvalidOrganization1-2", "at least one not null between name and units is required in Organization", jsCard.getValidationMessage());
    }
}
