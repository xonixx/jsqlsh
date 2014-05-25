package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 5/25/14
 * Time: 6:14 PM
 */
public class DbObject implements IDbObject {
    private final String name;
    private final DbObjectType type;
    private final IDbObject parent;

    public static DbObject db(String name) {
        return new DbObject(name, DbObjectType.DATABASE, null);
    }

    public DbObject(String name, DbObjectType type, IDbObject parent) {
        this.name = name;
        this.type = type;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IDbObject getParent() {
        return parent;
    }

    @Override
    public DbObjectType getType() {
        return type;
    }
}
