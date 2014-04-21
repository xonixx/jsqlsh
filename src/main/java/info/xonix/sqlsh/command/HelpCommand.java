package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

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
        List<Command> commands = Engine.listAllCommands();

        StringBuilder sb = new StringBuilder();

        for (Command command : commands) {
            if (StringUtils.isEmpty(arg) || arg.equals(command.name())) {
                sb.append(command.name());
                sb.append("\n\t");
                sb.append(command.description());
                sb.append("\n\n");
            }
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 2);

        return ICommandResult.text(sb.toString());
    }
}
