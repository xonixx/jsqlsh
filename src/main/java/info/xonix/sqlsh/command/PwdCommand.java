package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;

/**
 * User: xonix
 * Date: 5/31/14
 * Time: 5:21 PM
 */
@Command(
        name = "pwd",
        description = "print current db object"
)
public class PwdCommand implements ICommand {
    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        ISession session = context.getSession();
        IDbObject currentObject = session.getCurrentObject();
        return new TextResult(currentObject.pwd());
    }
}
