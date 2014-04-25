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
    public ICommandResult execute(ISession session) throws CommandExecutionException {
        List<Cmd> commands = Core.listAllCommands();

        StringBuilder sb = new StringBuilder();

        for (Cmd cmd : commands) {
            Command command = cmd.command;
            boolean emptyArg = StringUtils.isEmpty(arg);
            boolean isArg = !emptyArg && arg.equals(command.name());
            if (emptyArg || isArg) {
                sb.append(command.name());
                sb.append("\n\t");
                sb.append(command.description());
                if (isArg) {
                    List<IPrm> prms = Engine.listPrms(cmd.klass, null);
                    // TODO: hmmmm this line makes command not found. WHY???
//                    prms.sort((a,b)->a.getParam().name().compareTo(b.getParam().name()));
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
                sb.append("\n\n");
            }
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 2);

        return ICommandResult.text(sb.toString());
    }
}
