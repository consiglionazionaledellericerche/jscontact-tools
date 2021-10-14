package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.Organization;
import it.cnr.iit.jscontact.tools.dto.Title;
import it.cnr.iit.jscontact.tools.test.AbstractTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TitleTest extends AbstractTest {

    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidTitleBuild1() {

        // title missing
        Title.builder().build();
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidTitleBuild2() {

        // title missing
        Title.builder().organization("organization").build();
    }

    @Test
    public void testInvalidTitleOrganization1() {

        // missing title organization id due to missing organization
        Map jobTiles = new HashMap<String,Title>();
        jobTiles.put("title-1", Title.builder().title("Researcher").organization("IIT CNR").build());

        Card jsCard = Card.builder()
                .uid(getUUID())
                .titles(jobTiles)
                .build();

        assertTrue("testInvalidTitleOrganization1-1", !jsCard.isValid());
        assertTrue("testInvalidTitleOrganization1-2", jsCard.getValidationMessage().equals("title organization id is missing in organizations"));
    }

    @Test
    public void testInvalidTitleOrganization2() {

        // missing title organization id among organization ids
        Map jobTiles = new HashMap<String,Title>();
        jobTiles.put("title-1", Title.builder().title("Researcher").organization("IIT CNR").build());
        Map organizations = new HashMap<String,Organization>();
        organizations.put("organization-1", Organization.builder().name("an organization").build());
        Card jsCard = Card.builder()
                .uid(getUUID())
                .titles(jobTiles)
                .organizations(organizations)
                .build();

        assertTrue("testInvalidTitleOrganization2-1", !jsCard.isValid());
        assertTrue("testInvalidTitleOrganization2-2", jsCard.getValidationMessage().equals("title organization id is missing in organizations"));
    }


}
