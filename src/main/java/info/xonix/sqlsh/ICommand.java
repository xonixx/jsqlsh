package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:39 PM
 */
public interface ICommand {
    ICommandResult execute(IContext context) throws CommandExecutionException;

    void setValue(String value);
}
