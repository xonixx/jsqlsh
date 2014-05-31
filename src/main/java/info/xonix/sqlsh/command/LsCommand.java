package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;

import java.sql.SQLException;
import java.util.List;

/**
 * User: xonix
 * Date: 5/3/14
 * Time: 9:27 PM
 */
@Command(
        name = "ls",
        description = "list elements in current location"
)
public class LsCommand implements ICommand {
    @CommandArgument(
            description = "path to list"
    )
    private String path;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        ISession session = context.getSession();
        IDbObject currentObject = session.getCurrentObject();

        IDbObject targetDbObject = currentObject.resolve(path);

        return list(targetDbObject);
    }

    private ICommandResult list(IDbObject target) throws CommandExecutionException {
        if (target == null) {
            throw new CommandExecutionException("Not found.");
        } else if (target.getType() == DbObjectType.ROOT) {
            return listDatabases(target.getMetadataAccessor());
        } else if (target.getType() == DbObjectType.DATABASE) {
            return listDbTables(target.getMetadataAccessor(), target.getName());
        } else if (target.getType() == DbObjectType.TABLE ||
                target.getType() == DbObjectType.VIEW) {
            return listTableColumns(target.getMetadataAccessor(), target.getParent().getName(), target.getName());
        }
        return null;
    }

    private ICommandResult listTableColumns(MetadataAccessor metadataAccessor, String dbName, String tblName) {
        TableResult.Builder builder = new TableResult.Builder().columns(
                "field",
                "type",
                "null",
                "key",
                "default",
                "extra");

        for (ColumnDescriptor column : metadataAccessor.listColumns(dbName, tblName)) {
            builder.row(
                    column.field,
                    column.type,
                    column._null,
                    column.key,
                    column._default,
                    column.extra);
        }

        return builder.build();
    }

    private ICommandResult listDbTables(MetadataAccessor metadataAccessor, String dbName) {
        List<String> tables = metadataAccessor.listTables(dbName);
        List<String> views = metadataAccessor.listViewes(dbName);

        TableResult.Builder builder = new TableResult.Builder().columns("name", "type");

        for (String table : tables) {
            builder.row(table, "table");
        }

        for (String view : views) {
            builder.row(view, "view");
        }

        return builder.build();
    }

    private ICommandResult listDatabases(MetadataAccessor metadataAccessor) {
        TableResult.Builder builder = new TableResult.Builder().columns("name");

        for (String db : metadataAccessor.listDatabases()) {
            builder.row(db);
        }

        return builder.build();
    }
}
