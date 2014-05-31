package info.xonix.sqlsh;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: xonix
 * Date: 5/25/14
 * Time: 6:14 PM
 */
public class DbObject implements IDbObject {
    private final String name;
    private final DbObjectType type;
    private final DbObject parent;
    private final MetadataAccessor metadataAccessor;

    public static DbObject root(MetadataAccessor metadataAccessor) {
        return new DbObject("", DbObjectType.ROOT, null, metadataAccessor);
    }

    public DbObject child(String name, DbObjectType type) {
        return new DbObject(name, type, this, this.metadataAccessor);
    }

    public DbObject(String name, DbObjectType type, DbObject parent, MetadataAccessor metadataAccessor) {
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.metadataAccessor = metadataAccessor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DbObject getParent() {
        return parent;
    }

    @Override
    public DbObjectType getType() {
        return type;
    }

    private DbObject resolve0(String[] pathParts) {
        if (pathParts.length == 0) {
            return this;
        } else {
            DbObject dbObject = resolve0(pathParts[0]);
            if (dbObject == null) {
                return null;
            } else {
                return dbObject.resolve0(
                        (String[]) ArrayUtils.subarray(pathParts, 1, pathParts.length));
            }
        }
    }

    private DbObject resolve0(String part) {
        if (".".equals(part)) {
            return this;
        } else if ("..".equals(part)) {
            return parent != null ? parent : this;
        }

        if (type == DbObjectType.ROOT) {
            if (metadataAccessor.hasDb(part)) {
                return child(part, DbObjectType.DATABASE);
            }
        } else if (type == DbObjectType.DATABASE) {
            if (metadataAccessor.hasTable(name, part)) {
                return child(part, DbObjectType.TABLE);
            } else if (metadataAccessor.hasView(name, part)) {
                return child(part, DbObjectType.VIEW);
            }
        }
        return null;
    }

    @Override
    public IDbObject resolve(String path) {
        return resolve0(path != null ? path.split("/") : new String[0]);
    }

    @Override
    public MetadataAccessor getMetadataAccessor() {
        return metadataAccessor;
    }

    @Override
    public String pwd() {
        List<String> parts = new ArrayList<>();
        DbObject currentObject = this;
        do {
            DbObjectType type = currentObject.getType();
            parts.add(type == DbObjectType.ROOT ? currentObject.getName()
                    : (
                    type == DbObjectType.DATABASE ? "" :
                            type == DbObjectType.TABLE ? "" :
                                    type == DbObjectType.VIEW ? "v:" : "???") + currentObject.getName());
            currentObject = currentObject.getParent();
        } while (currentObject != null);
        Collections.reverse(parts);
        return StringUtils.join(parts, "/");
    }
}
