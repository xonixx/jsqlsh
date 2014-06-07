package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;

import java.sql.SQLException;

/**
 * User: xonix
 * Date: 5/31/14
 * Time: 5:21 PM
 */
@Command(
        name = "cd",
        description = "change db/table"
)
public class CdCommand implements ICommand {
    @CommandArgument(
            name = "path",
            description = "cd target"
    )
    private String path;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        ISession session = context.getSession();
        IDbObject currentObject = session.getCurrentObject();

        if (currentObject == null) {
            throw new CommandExecutionException("Not connected");
        }

        if (path == null) {
            path = "../../../.."; // resolve to root
        }
        IDbObject targetDbObject = currentObject.resolve(path);
        if (targetDbObject == null) {
            throw new CommandExecutionException("not found: " + path);
        } else {
            ((Session) session).setCurrentObject(targetDbObject);
            if (targetDbObject.getType() == DbObjectType.DATABASE) {
                Db.executeUpdate(session.getConnection(), "use " + targetDbObject.getName());
            }
        }
        return new TextResult("");
    }
}
