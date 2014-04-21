package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/20/14
 * Time: 12:01 AM
 */
public @interface CommandParam {
    String name(); // defaults for field name
    String description();
    boolean optional() default true; // todo: maybe false?
}
