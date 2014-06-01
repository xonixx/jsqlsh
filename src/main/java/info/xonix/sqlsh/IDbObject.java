package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 5/3/14
 * Time: 4:42 PM
 */
public interface IDbObject {
    String getName();

    IDbObject getParent();

    DbObjectType getType();

    /**
     * @param path like "dbname/tblname", null - resolves to same object
     * @return db object
     */
    IDbObject resolve(String path);

    MetadataAccessor getMetadataAccessor();

    /**
     * @return open command for this db object's connection
     */
    ICommand getOpenCommand();

    /**
     * @return path of this db object
     */
    String pwd();
}
