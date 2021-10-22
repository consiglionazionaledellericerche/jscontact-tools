package it.cnr.iit.jscontact.tools.dto.utils;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class for handling messages about constraint violations.
 *
 * @author Mario Loffredo
 */
public class ConstraintViolationUtils {

    /**
     * Returns a text message including all the constraint violations returned by a validation process and separated by newline.
     *
     * @param <T> a generic type a constraint is set on
     * @param constraintViolations the set of constraint violations
     * @return the text message
     */
    public static <T> String getMessage(Set<ConstraintViolation<T>> constraintViolations) {

        List<String> messages = new ArrayList<>();
        for(ConstraintViolation<T> constraintViolation : constraintViolations)
            messages.add(constraintViolation.getMessage());

        return String.join(DelimiterUtils.NEWLINE_DELIMITER, messages);
    }

}
