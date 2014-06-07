package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandArgument;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: xonix
 * Date: 6/7/14
 * Time: 6:37 PM
 */
@Command(
        name = "select",
        description = "performs sql select"
)
public class SelectCommand implements ICommand{
    @CommandArgument(
            name = "command",
            description = "command"
    )
    private String command;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        ISession session = context.getSession();
        IDbObject currentObject = session.getCurrentObject();

        if (currentObject == DbObject.ROOT) {
            throw new CommandExecutionException("not connected");
        }

        Connection connection = session.getConnection();

        TableResult tableResult;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select " + command);
            tableResult = TableResult.create(resultSet);
            resultSet.close();
        } catch (SQLException e) {
            throw new CommandExecutionException(e.getMessage());
        }

        return tableResult;
    }
}
