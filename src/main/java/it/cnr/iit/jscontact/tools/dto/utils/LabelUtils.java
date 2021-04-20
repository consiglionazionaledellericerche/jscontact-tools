package it.cnr.iit.jscontact.tools.dto.utils;

import java.util.Arrays;
import java.util.List;

public class LabelUtils {

    public static final String LABEL_DELIMITER = ",";

    public static boolean labelIncludesItem(String label,String labelItem) {

        if (label == null)
            return false;

        List<String> labelItems = Arrays.asList(label.split(LABEL_DELIMITER));
        return labelItems.contains(labelItem);

    }

}
