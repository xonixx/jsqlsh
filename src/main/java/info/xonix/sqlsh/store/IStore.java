package info.xonix.sqlsh.store;

import java.util.List;

/**
 * User: xonix
 * Date: 5/1/14
 * Time: 10:02 PM
 */
public interface IStore {
    void put(String path, String key, Object value);

    boolean delete(String path, String key);

    Object get(String path, String key);

    void flush();

    boolean exists(String path, String key);

    List<String> listKeys(String path);

    List<StoreElement> list(String path);
}
