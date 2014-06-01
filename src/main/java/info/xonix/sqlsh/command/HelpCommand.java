package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;
import info.xonix.sqlsh.annotations.CommandParam;
import info.xonix.sqlsh.prm.IPrm;
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

    @CommandArgument(
            name = "command",
            description = "command to provide help for",
            optional = true
    )
    private String arg;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {

        StringBuilder sb = new StringBuilder();

        boolean emptyArg = StringUtils.isEmpty(arg);

        if (emptyArg) {
            List<Cmd> commands = Core.listAllCommands();

            for (Cmd cmd : commands) {
                Command command = cmd.command;
                sb.append(command.name())
                        .append(" - ")
                        .append(command.description())
                        .append('\n');
            }
            if (sb.length() > 0)
                sb.setLength(sb.length() - 2);

        } else {
            Cmd cmd = Core.resolveCommand(arg);
            if (cmd == null) {
                return new TextResult("Command " + arg + " doesn't exist");
            }
            Command command = cmd.command;
            sb.append(command.name())
                    .append("\n\t")
                    .append(command.description());
            List<IPrm<CommandParam>> prms = Engine.listPrms(cmd.klass, null, CommandParam.class);
            List<IPrm<CommandArgument>> arg = Engine.listPrms(cmd.klass, null, CommandArgument.class);
            if (!prms.isEmpty() || !arg.isEmpty()) {
                prms.sort((a, b) -> a.getParam().name().compareTo(b.getParam().name()));
                sb.append("\n\n\tParameters:");
                for (IPrm<CommandParam> prm : prms) {
                    sb.append("\n\t\t")
                            .append(formParamLine(
                                    '-' + prm.getName(),
                                    prm.getParamType(),
                                    prm.getParam().description(),
                                    prm.getParam().optional()));
                }
                for (IPrm<CommandArgument> prm : arg) {
                    sb.append("\n\t\t")
                            .append(formParamLine(
                            prm.getName(),
                            prm.getParamType(),
                            prm.getParam().description(),
                            prm.getParam().optional()));
                    break; // only 1
                }
            }
        }

        return new TextResult(sb.toString());
    }

    private String formParamLine(String name, Class type, String description, boolean optional) {
        if (optional)
            name = '[' + name + ']';
        return name + " (" + type.getSimpleName().toLowerCase() + ") - " + description;
    }
}
