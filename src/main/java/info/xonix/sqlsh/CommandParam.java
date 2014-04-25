package info.xonix.sqlsh;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: xonix
 * Date: 4/20/14
 * Time: 12:01 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CommandParam {
    String name(); // defaults for field name

    String description();

    boolean optional() default true; // todo: maybe false?
}
