package it.cnr.iit.jscontact.tools.test.validation;

import it.cnr.iit.jscontact.tools.dto.Title;
import org.junit.Test;

public class TitleTest {


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


}
