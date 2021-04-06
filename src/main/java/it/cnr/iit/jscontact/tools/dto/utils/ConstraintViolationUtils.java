package it.cnr.iit.jscontact.tools.dto.utils;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConstraintViolationUtils {

    public static <T> String getMessage(Set<ConstraintViolation<T>> constraintViolations) {

        List<String> messages = new ArrayList<String>();
        for(ConstraintViolation<T> constraintViolation : constraintViolations)
            messages.add(constraintViolation.getMessage());

        return String.join("\n", messages);
    }

}
