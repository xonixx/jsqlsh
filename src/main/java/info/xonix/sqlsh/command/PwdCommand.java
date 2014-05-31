package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;

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
        if (currentObject == null) {
            throw new CommandExecutionException("Not connected");
        } else {
            DbObjectType type = currentObject.getType();
            return ICommandResult.text(type == DbObjectType.ROOT ? "Connection"
                    : (
                    type == DbObjectType.DATABASE ? "Database" :
                            type == DbObjectType.TABLE ? "Table" :
                                    type == DbObjectType.VIEW ? "View" : "???") + ": " + currentObject.getName());
        }
    }
}
