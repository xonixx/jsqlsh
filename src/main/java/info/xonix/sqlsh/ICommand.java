package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:39 PM
 */
public interface ICommand {
    ICommandResult execute(ISession session, String arg) throws CommandExecutionException;
}
