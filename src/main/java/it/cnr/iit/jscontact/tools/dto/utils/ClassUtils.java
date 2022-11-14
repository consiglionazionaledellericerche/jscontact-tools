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
    private static String getDtoPackageName() {

        String thisClassName = ClassUtils.class.getCanonicalName();
        return thisClassName.replace(".utils.ClassUtils","");
    }

    public static Class forName(String className) throws ClassNotFoundException {
        return Class.forName(getDtoPackageName()+"."+className);
    }

}
