package info.xonix.sqlsh.db;

import info.xonix.sqlsh.Db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * User: xonix
 * Date: 5/25/14
 * Time: 6:20 PM
 */
public class MysqlMetadataAccessor implements MetadataAccessor {
    public static final String TABLE = "BASE TABLE";
    public static final String VIEW = "VIEW";
    private Connection connection;

    private static final String VERSION_QRY = "select VERSION()";
    private final static String LIMIT_1 = " LIMIT 1";

    private final static String TBL_QRY =
            "select table_name, table_type " +
                    "from information_schema.tables " +
                    "where table_schema=? and table_type=?";
    private final static String TBL_QRY_NAME = TBL_QRY + " and table_name=?";

    private final static String DB_QRY = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA";
    private final static String DB_QRY_NAME = DB_QRY + " WHERE SCHEMA_NAME = ?";

    private final static String SHOW_COLUMNS = "SHOW COLUMNS FROM ";

    public MysqlMetadataAccessor(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getVersion() {
        return (String) Db.single(connection, VERSION_QRY);
    }

    @Override
    public boolean hasDb(String dbName) {
        return Db.exists(connection, DB_QRY_NAME + LIMIT_1, dbName);
    }

    @Override
    public boolean hasTable(String dbName, String tableName) {
        return Db.exists(connection, TBL_QRY_NAME + LIMIT_1, dbName, TABLE, tableName);
    }

    @Override
    public boolean hasView(String dbName, String viewName) {
        return Db.exists(connection, TBL_QRY_NAME + LIMIT_1, dbName, VIEW, viewName);
    }

    @Override
    public List<String> listDatabases() {
        return Db.listStrings(connection, DB_QRY);
    }

    @Override
    public List<String> listTables(String dbName) {
        return Db.listStrings(connection, TBL_QRY, dbName, TABLE);
    }

    @Override
    public List<String> listViews(String dbName) {
        return Db.listStrings(connection, TBL_QRY, dbName, VIEW);
    }

    @Override
    public List<ColumnDescriptor> listColumns(String dbName, String tblName) {
        List<List<Object>> list = Db.list(connection, SHOW_COLUMNS + dbName + "." + tblName);
        List<ColumnDescriptor> res = new ArrayList<>(list.size());
        for (List<Object> row : list) {
            res.add(new ColumnDescriptor(
                    (String) row.get(0),
                    (String) row.get(1),
                    (String) row.get(2),
                    (String) row.get(3),
                    (String) row.get(4),
                    (String) row.get(5)
            ));
        }
        return res;
    }
}
