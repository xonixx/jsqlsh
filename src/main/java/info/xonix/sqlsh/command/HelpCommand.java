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

    private String arg;

    @Override
    public void setValue(String value) {
        arg = value;
    }

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
                return ICommandResult.text("Command " + arg + " doesn't exist");
            }
            Command command = cmd.command;
            sb.append(command.name())
                    .append("\n\t")
                    .append(command.description());
            List<IPrm> prms = Engine.listPrms(cmd.klass, null);
            if (!prms.isEmpty()) {
                prms.sort((a, b) -> a.getParam().name().compareTo(b.getParam().name()));
                sb.append("\n\n\tParameters:");
                for (IPrm prm : prms) {
                    sb.append("\n\t\t")
                            .append(prm.getParam().name())
                            .append(" (")
                            .append(prm.getParamType())
                            .append(") - ")
                            .append(prm.getParam().description());
                }
            }
        }

        return ICommandResult.text(sb.toString());
    }
}
