package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;

/**
 * User: xonix
 * Date: 4/27/14
 * Time: 9:22 PM
 */
@Command(
        name = "exit",
        description = "exit jsqlsh"
)
public class ExitCommand implements ICommand {
    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        return null;
    }
}
