package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.LocalizedString;
import it.cnr.iit.jscontact.tools.dto.Organization;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OrganizationTest {


    @Test(expected = NullPointerException.class)
    public void testInvalidOrganizationBuild1() {

        // name missing
        Organization.builder().build();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidOrganizationBuild2() {

        LocalizedString[] units = { LocalizedString.builder().value("unit1").language("en").build()};
        // name missing
        Organization.builder().units(units).build();
    }

}
