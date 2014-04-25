package info.xonix.sqlsh;

/**
 * TODO: good name
 * User: xonix
 * Date: 4/25/14
 * Time: 3:37 PM
 */
public class Cmd {
    public final Command command;
    public final Class klass;

    Cmd(Command command, Class klass) {
        this.command = command;
        this.klass = klass;
    }
}
