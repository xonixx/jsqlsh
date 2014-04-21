package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;

/**
 * User: xonix
 * Date: 4/21/14
 * Time: 9:53 PM
 */
@Command(
        name = "help",
        description = "lists all commands and provides help for each")
public class HelpCommand implements ICommand {
    @Override
    public ICommandResult execute(ISession session, String arg) throws CommandExecutionException {
        return null;
    }
}
