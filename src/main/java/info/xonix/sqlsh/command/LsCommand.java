package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;

import java.sql.*;

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
        Connection connection = session.getConnection();

        IDbObject currentObject = session.getCurrentObject();
        try {
            return list(connection, currentObject);
        } catch (SQLException e) {
            throw new CommandExecutionException("Can't ls", e);
        }
    }

    private ICommandResult list(Connection connection, IDbObject currentObject) throws SQLException, CommandExecutionException {
        if (currentObject == null) {
            // top level -> list databases
            if (path == null) {
                return listDatabases(connection);
            } else {
                return listDbTables(connection, path);
            }
        } else if (currentObject.getType() == DbObjectType.DATABASE) {
            if (path == null) {
                return listDbTables(connection, currentObject.getName());
            } else {
                return listTableColumns(connection, path);
            }
        } else if (currentObject.getType() == DbObjectType.TABLE) {
            if (path == null) {
                return listTableColumns(connection, currentObject.getName());
            } else {
                throw new CommandExecutionException("doesn't exist");
            }
        }
        return null;
    }

    private ICommandResult listTableColumns(Connection connection, String tblName) {
        return null;
    }

    private ICommandResult listDbTables(Connection connection, String dbName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "select table_name as name, table_type as type " +
                        "from information_schema.tables " +
                        "where table_schema=?");
        statement.setString(1, dbName);
        ResultSet resultSet = statement.executeQuery();
        ICommandResult result = ICommandResult.table(resultSet);

        statement.close();

        return result;
    }

    private ICommandResult listDatabases(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("show databases;");
        ICommandResult result = ICommandResult.table(resultSet);

        statement.close();

        return result;
    }
}
