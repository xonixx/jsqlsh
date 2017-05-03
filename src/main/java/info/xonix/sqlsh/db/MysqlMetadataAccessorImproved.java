package info.xonix.sqlsh.db;

import java.sql.*;
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

//            ResultSetMetaData metaData = columns.getMetaData();
            while (columns.next()) {
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < metaData.getColumnCount(); i++) {
//                    sb.append(columns.getObject(i + 1)).append(", ");
//                }
//                sb.setLength(sb.length() - 2);
//                System.out.println("COL: " + sb);

                String typeName = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String typeNameLc = typeName.toLowerCase();
                boolean typeInt = typeNameLc.endsWith("int");
                if (typeInt)
                    columnSize += 1; // hmmm
                if (typeNameLc.endsWith("char") || typeInt)
                    typeName += "(" + columnSize + ")";
                res.add(new ColumnDescriptor(
                        columns.getString("COLUMN_NAME"),
                        typeName,
                        columns.getString("IS_NULLABLE"),
                        "",
                        columns.getString("COLUMN_DEF"),
                        "YES".equals(columns.getString("IS_AUTOINCREMENT")) ? "auto_increment" : ""
                ));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
