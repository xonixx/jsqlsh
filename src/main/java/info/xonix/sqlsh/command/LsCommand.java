package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
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

    private MetadataAccessor metadataAccessor;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        ISession session = context.getSession();
        Connection connection = session.getConnection();
        metadataAccessor = new MysqlMetadataAccessor(connection);
        IDbObject currentObject = session.getCurrentObject();

        try {
            String[] pathParts = path != null ? path.split("/") : new String[0];
            IDbObject targetDbObject = resolve(currentObject, pathParts);
            return list(targetDbObject);
        } catch (SQLException e) {
            throw new CommandExecutionException("Can't ls", e);
        }
    }

    private IDbObject resolve(IDbObject relObject, String[] pathParts) {
        if (pathParts.length == 0) {
            return relObject;
        } else {
            return resolve(resolve(relObject, pathParts[0]),
                    (String[]) ArrayUtils.subarray(pathParts, 1, pathParts.length));
        }
    }

    private IDbObject resolve(IDbObject relObject, String part) {
        if (relObject.getType() == DbObjectType.ROOT) {
            if (metadataAccessor.hasDb(part)) {
                return DbObject.db(part);
            }
        } else if (relObject.getType() == DbObjectType.DATABASE) {
            if (metadataAccessor.hasTable(relObject.getName(), part)) {
                return new DbObject(part, DbObjectType.TABLE, relObject);
            } else if (metadataAccessor.hasView(relObject.getName(), part)) {
                return new DbObject(part, DbObjectType.VIEW, relObject);
            }
        }
        return null;
    }

    private ICommandResult list(IDbObject target) throws SQLException, CommandExecutionException {
        if (target == null) {
            throw new CommandExecutionException("Not found.");
        } else if (target.getType() == DbObjectType.ROOT) {
            return listDatabases();
        } else if (target.getType() == DbObjectType.DATABASE) {
            return listDbTables(target.getName());
        } else if (target.getType() == DbObjectType.TABLE ||
                target.getType() == DbObjectType.VIEW) {
            return listTableColumns(target.getParent().getName(), target.getName());
        }
        return null;
    }

    private ICommandResult listTableColumns(String dbName, String tblName) {
        TableResult.Builder builder = new TableResult.Builder().columns(
                "field",
                "type",
                "null",
                "key",
                "default",
                "extra");

        for (ColumnDescriptor column : metadataAccessor.listColumns(dbName, tblName)) {
            builder.row(
                    StringUtils.defaultString(column.field),
                    StringUtils.defaultString(column.type),
                    StringUtils.defaultString(column._null),
                    StringUtils.defaultString(column.key),
                    StringUtils.defaultString(column._default),
                    StringUtils.defaultString(column.extra));
        }

        return builder.build();
    }

    private ICommandResult listDbTables(String dbName) throws SQLException {
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

    private ICommandResult listDatabases() throws SQLException {
        TableResult.Builder builder = new TableResult.Builder().columns("name");

        for (String db : metadataAccessor.listDatabases()) {
            builder.row(db);
        }

        return builder.build();
    }
}
