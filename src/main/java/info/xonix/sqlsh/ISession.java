package info.xonix.sqlsh;

import info.xonix.sqlsh.db.MetadataAccessor;

import java.sql.Connection;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:39 PM
 */
public interface ISession {
    Connection getConnection();

    MetadataAccessor getMetadataAccessor();

    IDbObject getCurrentObject();
}
