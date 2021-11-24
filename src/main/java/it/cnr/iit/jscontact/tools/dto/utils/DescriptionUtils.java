package it.cnr.iit.jscontact.tools.dto.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for handling descriptions in both JSContact ("description" properties) and vCard (LABEL parameter).
 *
 * @author Mario Loffredo
 */
public class DescriptionUtils {

    public static final String LABEL_DELIMITER = ",";

    /**
     * Tests if a description contains an item.
     * @param description the description
     * @param item the item
     * @return true if the description contains the item, false otherwise
     */
    public static boolean descriptionIncludesItem(String description,String item) {

        if (description == null)
            return false;

        List<String> descriptionItems = Arrays.asList(description.split(LABEL_DELIMITER));
        return descriptionItems.contains(item);

    }

    /**
     * Tests if a description contains any item in a list.
     * @param description the description
     * @param items the list of items
     * @return true if the description contains any of the item in the list, false otherwise
     */
    public static boolean descriptionIncludesAnyItem(String description,List<String> items) {

        if (description == null)
            return false;

        for (String item : items)
            if (descriptionIncludesItem(description, item))
                return true;

        return false;
    }

}
