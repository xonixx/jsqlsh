package info.xonix.sqlsh;

import info.xonix.sqlsh.db.MetadataAccessor;
import info.xonix.sqlsh.db.MysqlMetadataAccessor;
import info.xonix.sqlsh.db.MysqlMetadataAccessorImproved;
import jline.console.ConsoleReader;

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

    @Override
    public MetadataAccessor getMetadataAccessor() {
//        return new MysqlMetadataAccessor(getConnection());
        return new MysqlMetadataAccessorImproved(getConnection());
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

    /**
     * changes a prompt according to current db object
     * @param console console
     */
    public void changePrompt(ConsoleReader console) {
        console.setPrompt((currentObject != null ? currentObject.pwd() : "") + "> ");
    }
}
