package info.xonix.sqlsh.store;

/**
 * User: xonix
 * Date: 5/1/14
 * Time: 10:07 PM
 */
public class StoreElement {
    public final String key;
    public final Object value;

    public StoreElement(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
