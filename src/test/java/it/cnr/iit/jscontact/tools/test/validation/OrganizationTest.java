package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.LocalizedString;
import it.cnr.iit.jscontact.tools.dto.Organization;
import org.junit.Test;

public class OrganizationTest {


    @Test(expected = NullPointerException.class)
    public void testInvalidOrganizationBuild1() {

        // name missing
        Organization.builder().build();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidOrganizationBuild2() {

        // name missing
        Organization.builder().units(new String[]{"unit1"}).build();
    }

}
