package info.xonix.sqlsh;

import java.sql.Connection;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:39 PM
 */
public interface ISession {
    Connection getConnection();

    IDbObject getCurrentObject();
}
