package info.xonix.sqlsh;

import java.sql.Connection;

/**
 * User: xonix
 * Date: 5/3/14
 * Time: 4:52 PM
 */
public class Session implements ISession {
    private Connection connection;
    private IDbObject currentObject;

    @Override
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public IDbObject getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(IDbObject currentObject) {
        this.currentObject = currentObject;
    }
}
