package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.Organization;
import it.cnr.iit.jscontact.tools.dto.Title;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TitleTest extends AbstractTest {

    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidTitleBuild1() {

        // title missing
        Title.builder().build();
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidTitleBuild2() {

        // title missing
        Title.builder().organizationId("organization").build();
    }

    @Test
    public void testInvalidTitleOrganization1() {

        // missing title organization id due to missing organization
        Map<String,Title> jobTiles = new HashMap<>();
        jobTiles.put("title-1", Title.builder().name("Researcher").organizationId("IIT CNR").build());

        Card jsCard = Card.builder()
                .uid(getUUID())
                .titles(jobTiles)
                .build();

        assertFalse("testInvalidTitleOrganization1-1", jsCard.isValid());
        assertEquals("testInvalidTitleOrganization1-2", "title organization id is missing in organizations", jsCard.getValidationMessage());
    }

    @Test
    public void testInvalidTitleOrganization2() {

        // missing title organization id among organization ids
        Map<String,Title> jobTiles = new HashMap<>();
        jobTiles.put("title-1", Title.builder().name("Researcher").organizationId("IIT CNR").build());
        Map<String,Organization> organizations = new HashMap<>();
        organizations.put("organization-1", Organization.builder().name("an organization").build());
        Card jsCard = Card.builder()
                .uid(getUUID())
                .titles(jobTiles)
                .organizations(organizations)
                .build();

        assertFalse("testInvalidTitleOrganization2-1", jsCard.isValid());
        assertEquals("testInvalidTitleOrganization2-2", "title organization id is missing in organizations", jsCard.getValidationMessage());
    }


}
