package it.cnr.iit.jscontact.tools.dto.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for handling labels in both JSContact ("label" properties) and vCard (LABEL parameter).
 *
 * @author Mario Loffredo
 */
public class LabelUtils {

    public static final String LABEL_DELIMITER = ",";

    /**
     * Tests if a label contains an item.
     * @param label the label
     * @param item the item
     * @return true if the label contains the item, false otherwise
     */
    public static boolean labelIncludesItem(String label,String item) {

        if (label == null)
            return false;

        List<String> labelItems = Arrays.asList(label.split(LABEL_DELIMITER));
        return labelItems.contains(item);

    }

    /**
     * Tests if a label contains any item in a list.
     * @param label the label
     * @param items the list of items
     * @return true if the label contains any of the item in the list, false otherwise
     */
    public static boolean labelIncludesAnyItem(String label,List<String> items) {

        if (label == null)
            return false;

        for (String item : items)
            if (labelIncludesItem(label, item))
                return true;

        return false;
    }

}
