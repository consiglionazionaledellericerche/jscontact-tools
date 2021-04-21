package it.cnr.iit.jscontact.tools.dto.utils;

import java.util.Arrays;
import java.util.List;

public class LabelUtils {

    public static final String LABEL_DELIMITER = ",";

    public static boolean labelIncludesItem(String label,String item) {

        if (label == null)
            return false;

        List<String> labelItems = Arrays.asList(label.split(LABEL_DELIMITER));
        return labelItems.contains(item);

    }

    public static boolean labelIncludesAnyItem(String label,List<String> items) {

        if (label == null)
            return false;

        for (String item : items)
            if (labelIncludesItem(label, item))
                return true;

        return false;
    }

}
