package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 5/25/14
 * Time: 7:48 PM
 */
public class ColumnDescriptor {
    public final String field;
    public final String type;
    public final String _null;
    public final String key;
    public final String _default;
    public final String extra;

    public ColumnDescriptor(String field, String type, String _null, String key, String _default, String extra) {
        this.field = field;
        this.type = type;
        this._null = _null;
        this.key = key;
        this._default = _default;
        this.extra = extra;
    }
}
