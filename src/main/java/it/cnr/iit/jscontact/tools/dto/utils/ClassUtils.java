package it.cnr.iit.jscontact.tools.dto.utils;

public class ClassUtils {

    public static String getDtoPackageName() {

        String thisClassName = ClassUtils.class.getCanonicalName();
        return thisClassName.replace(".utils.ClassUtils","");
    }

}
