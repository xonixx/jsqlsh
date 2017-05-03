package info.xonix.sqlsh.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlMetadataAccessorImproved implements MetadataAccessor {

    private final Connection connection;
    private final DatabaseMetaData databaseMetaData;

    public MysqlMetadataAccessorImproved(Connection connection) {
        this.connection = connection;
        try {
            databaseMetaData = connection.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getVersion() {
        try {
            return databaseMetaData.getDatabaseProductVersion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasDb(String dbName) {
        try {
            ResultSet catalogs = databaseMetaData.getCatalogs();
            while (catalogs.next()) {
                if (dbName.equals(catalogs.getString("TABLE_CAT")))
                    return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasTable(String dbName, String tableName) {
        return hasDbObject(dbName, tableName, "TABLE");
    }

    @Override
    public boolean hasView(String dbName, String viewName) {
        return hasDbObject(dbName, viewName, "VIEW");
    }

    private boolean hasDbObject(String dbName, String tableName, String type) {
        try {
            return databaseMetaData.getTables(dbName, null, tableName, new String[]{type}).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listDatabases() {
        List<String> res = new ArrayList<>();
        try {
            ResultSet catalogs = databaseMetaData.getCatalogs();
            while (catalogs.next()) {
                res.add(catalogs.getString("TABLE_CAT"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<String> listTables(String dbName) {
        return listDbObjects(dbName, "TABLE");
    }

    @Override
    public List<String> listViews(String dbName) {
        return listDbObjects(dbName, "VIEW");
    }

    private List<String> listDbObjects(String dbName, String type) {
        List<String> res = new ArrayList<>();
        try {
            ResultSet objects = databaseMetaData.getTables(dbName, null, null, new String[]{type});
            while (objects.next()) {
                res.add(objects.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<ColumnDescriptor> listColumns(String dbName, String tblName) {
        List<ColumnDescriptor> res = new ArrayList<>();

        try {
            ResultSet columns = databaseMetaData.getColumns(dbName, null, tblName, null);
            while (columns.next()) {
                res.add(new ColumnDescriptor(
                        columns.getString("COLUMN_NAME"),
                        columns.getString("COLUMN_DEF"),
                        columns.getInt("NULLABLE") == 1 ? "NULL" : "",
                        "TBD",
                        "TBD",
                        columns.getString("REMARKS")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
