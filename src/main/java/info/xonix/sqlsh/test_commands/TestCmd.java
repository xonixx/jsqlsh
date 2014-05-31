package info.xonix.sqlsh.test_commands;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;
import info.xonix.sqlsh.annotations.CommandParam;

/**
 * User: xonix
 * Date: 5/10/14
 * Time: 2:25 AM
 */
@Command(
        name = "testcmd",
        description = "fake command to test command API. Prints its args."
)
public class TestCmd implements ICommand {
    @CommandParam(
            name = "prm111",
            description = "param 1"
    )
    String param1;

    @CommandParam(
            description = "int param 2"
    )
    Integer param2;

    @CommandParam(
            description = "mandatory param",
            optional = false
    )
    int param3;

    @CommandArgument(
            name = "argument",
            description = "command argument"
    )
    String arg;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        return TableResult.create(
                new String[]{"PARAM", "VALUE"},
                new Object[]{"-param1", param1},
                new Object[]{"-param2", param2},
                new Object[]{"-param3", param3},
                new Object[]{"arg", arg}
        );
    }
}
