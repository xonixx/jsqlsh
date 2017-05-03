package info.xonix.sqlsh.db;

import java.util.List;

/**
 * User: xonix
 * Date: 5/25/14
 * Time: 6:20 PM
 */
public interface MetadataAccessor {
    String getVersion();

    boolean hasDb(String dbName);

    boolean hasTable(String dbName, String tableName);

    boolean hasView(String dbName, String viewName);

    List<String> listDatabases();

    List<String> listTables(String dbName);

    List<String> listViews(String dbName);

    List<ColumnDescriptor> listColumns(String dbName, String tblName);
}
