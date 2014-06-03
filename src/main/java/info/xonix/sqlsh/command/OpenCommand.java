package info.xonix.sqlsh.command;

import info.xonix.sqlsh.*;
import info.xonix.sqlsh.annotations.Command;
import info.xonix.sqlsh.annotations.CommandParam;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
        Connection connection = openConnection();
        Session session = (Session) context.getSession();
        session.setConnection(connection);
        MysqlMetadataAccessor metadataAccessor = new MysqlMetadataAccessor(connection);

        session.setCurrentObject(DbObject.connection(user + "@" + host, metadataAccessor, this));

        return new TextResult(metadataAccessor.getVersion());
    }

    public Connection openConnection() throws CommandExecutionException {
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/?user=" + user + "&password=" + pass);
        } catch (ClassNotFoundException | SQLException e) {
            throw new CommandExecutionException("Can't connect to DB", e);
        }
        return connection;
    }

    // TODO: rewrite general purpose object <-> map
    public Map<String,Object> asMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("host", host);
        res.put("port", port);
        res.put("user", user);
        res.put("pass", pass);
        return res;
    }

    public OpenCommand fromMap(Map map) {
        host = (String) map.get("host");
        port = (int) map.get("port");
        user = (String) map.get("user");
        pass = (String) map.get("pass");
        return this;
    }
}
