package it.cnr.iit.jscontact.tools.dto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JSContactCollection {
    String addMethod() default "";
    Class itemClass() default Object.class;
}
