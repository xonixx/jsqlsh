package info.xonix.sqlsh;

import java.util.List;

/**
 * User: xonix
 * Date: 4/20/14
 * Time: 12:15 AM
 */
public interface ICommandParseResult {
    List<String> getErrors();

    default boolean isValid() {
        return getCommand() != null;
    }

    ICommand getCommand();
}
