package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandParam;

import java.sql.*;

/**
 * User: xonix
 * Date: 5/3/14
 * Time: 4:47 PM
 */
@Command(
        name = "open",
        description = "opens connection to database"
)
public class OpenCommand implements ICommand {
    @CommandParam(
            name = "host",
            description = "database host"
    )
    String host = "localhost";

    @CommandParam(
            name = "port",
            description = "database port"
    )
    int port = 3306; // mysql

    @CommandParam(
            name = "user",
            description = "user name",
            optional = false
    )
    String user;

    @CommandParam(
            name = "pass",
            description = "user password",
            optional = true
    )
    String pass;

    @Override
    public ICommandResult execute(IContext context) throws CommandExecutionException {
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/?user=" + user + "&password=" + pass);
        } catch (ClassNotFoundException | SQLException e) {
            throw new CommandExecutionException("Can't connect to DB", e);
        }

        Session session = (Session) context.getSession();
        session.setConnection(connection);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select VERSION()");
            if (resultSet.next()) {
                return ICommandResult.text(resultSet.getString(1));
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new CommandExecutionException("Can't fetch DB version", e);
        }
    }
}
