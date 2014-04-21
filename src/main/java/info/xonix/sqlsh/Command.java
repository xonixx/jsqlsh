package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/20/14
 * Time: 12:07 AM
 */
public @interface Command {
    String name(); // defaults for cls name
    String description();
}
