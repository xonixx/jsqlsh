package info.xonix.sqlsh.test_commands;

import info.xonix.sqlsh.*;

/**
 * User: xonix
 * Date: 4/26/14
 * Time: 12:10 AM
 */
@Command(
        name = "add",
        description = "Performs addition of two integers"
)
public class AddCmd implements ICommand {
    @CommandParam(
            name = "a",
            description = "first num",
            optional = false
    )
    int a;

    @CommandParam(
            name = "b",
            description = "second num",
            optional = false
    )
    int b;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        return ICommandResult.text(String.valueOf(a+b));
    }

    @Override
    public void setValue(String value) {
    }
}
