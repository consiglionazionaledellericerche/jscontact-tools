package it.cnr.iit.jscontact.tools.dto.utils;

import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        return thisClassName.replace(".utils.ClassUtils", StringUtils.EMPTY);
    }

    /**
     * Returns the class corresponding to the class name.
     * @param className the class name
     * @return the class corresponding to the class name
     * @throws ClassNotFoundException if the class name is wrong
     */
    public static Class forName(String className) throws ClassNotFoundException {
        return Class.forName(getDtoPackageName()+"."+className);
    }


    /**
     * Returns the list of class names corresponding to JSContact IANA registered types.
     * @return list of class names corresponding to JSContact IANA registered types
     * @throws ClassNotFoundException thrown by forName method
     * @throws IOException thrown by readLine method
     */
    public static List<String> getIANATypes() throws ClassNotFoundException, IOException {

        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(getDtoPackageName().replaceAll("[.]", "/"));

        List<String> IANATypes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while (reader.ready()) {
            String line = reader.readLine();
            if (line.endsWith(".class")) {
                Class classs = forName(line.replace(".class", StringUtils.EMPTY));
                if (Arrays.asList(classs.getInterfaces()).contains(IsIANAType.class))
                    IANATypes.add(classs.getSimpleName());
            }
        }

        return (IANATypes.isEmpty()) ? null : IANATypes;
    }

}
