package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            String result = pwd(currentObject);
            return ICommandResult.text(result);
        }
    }

    private String pwd(IDbObject currentObject) {
        List<String> parts = new ArrayList<>();
        do {
            DbObjectType type = currentObject.getType();
            parts.add(type == DbObjectType.ROOT ? "Connection"
                    : (
                    type == DbObjectType.DATABASE ? "Database" :
                            type == DbObjectType.TABLE ? "Table" :
                                    type == DbObjectType.VIEW ? "View" : "???") + ": " + currentObject.getName());
            currentObject = currentObject.getParent();
        } while (currentObject != null);
        Collections.reverse(parts);
        return StringUtils.join(parts, "/");
    }
}
