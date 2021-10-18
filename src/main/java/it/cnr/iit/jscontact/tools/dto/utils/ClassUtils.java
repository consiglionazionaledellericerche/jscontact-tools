package it.cnr.iit.jscontact.tools.dto.utils;

/**
 * Utility class for supporting class loading of dto classes.
 *
 * @author Mario Loffredo
 */
public class ClassUtils {

    /**
     * Returns the dto package full name.
     * @return the dto package full name
     */
    public static String getDtoPackageName() {

        String thisClassName = ClassUtils.class.getCanonicalName();
        return thisClassName.replace(".utils.ClassUtils","");
    }

}
