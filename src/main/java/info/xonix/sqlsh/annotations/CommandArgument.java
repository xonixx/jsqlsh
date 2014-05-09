package info.xonix.sqlsh.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: xonix
 * Date: 5/9/14
 * Time: 11:55 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CommandArgument {
    String name() default "";

    String description();

    boolean optional() default true; // todo: maybe false?
}
