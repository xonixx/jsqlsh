package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;
import info.xonix.sqlsh.annotations.CommandParam;
import info.xonix.sqlsh.store.IStore;
import org.apache.commons.beanutils.BeanMap;

/**
 * User: xonix
 * Date: 6/1/14
 * Time: 4:57 PM
 */
@Command(
        name = "con",
        description = "save/delete connections in store"
)
public class ConnectionOpreations implements ICommand {
    public static final String BUCKET_CONNECTION = "connection";
    @CommandParam(
            name = "save",
            description = "save current connection in store"
    )
    boolean save;

    @CommandParam(
            name = "delete",
            description = "delete connection from store"
    )
    boolean delete;

    @CommandArgument(
            name = "name",
            description = "name of connection to save/delete"
    )
    String name;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        if (save && delete) {
            throw new CommandExecutionException("only save or delete, not both");
        }

        if (!save && !delete) {
            throw new CommandExecutionException("provide -save/-delete");
        }

        if (name == null) {
            throw new CommandExecutionException("provide name");
        }

        IStore store = context.getStore();
        IConsole console = context.getConsole();
        IDbObject currentObject = context.getSession().getCurrentObject();

        if (currentObject == null) {
            throw new CommandExecutionException("Not connected");
        }

        save: if (save) {
            if (store.exists(BUCKET_CONNECTION, name)) {
                String reply = console.getString("Connection with this name already exists. Overwrite? [y/n]: ");
                if ("n".equals(reply)) {
                    break save;
                }
            }

            store.put(BUCKET_CONNECTION, name, ((OpenCommand) currentObject.getOpenCommand()).asMap());
        } else if (delete) {
            if (store.exists(BUCKET_CONNECTION, name)) {
                store.delete(BUCKET_CONNECTION, name);
            } else {
                throw new CommandExecutionException(name + " doesn't exist");
            }
        }

        store.flush();

        return new TextResult("Done.");
    }
}
