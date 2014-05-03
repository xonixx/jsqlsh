package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 5/3/14
 * Time: 4:42 PM
 */
public interface IDbObject {
    String getName();

    DbObjectType getType();
}
