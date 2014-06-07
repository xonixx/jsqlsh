package info.xonix.sqlsh;

import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: xonix
 * Date: 5/25/14
 * Time: 7:22 PM
 */
public class TableResult implements ITableResult, ICommandResult {
    private final List<String> columnNames;
    private final List<List<String>> data;

    public static class Builder {
        private final List<String> columnNames = new ArrayList<>();
        private final List<List<String>> data = new ArrayList<>();

        public Builder columns(String... cols) {
            columnNames.addAll(Arrays.asList(cols));
            return this;
        }

        public Builder row(String... row) {
            for (int i = 0; i < row.length; i++) {
                row[i] = StringUtils.defaultString(row[i]);
            }
            data.add(Arrays.asList(row));
            return this;
        }

        public TableResult build() {
            return new TableResult(columnNames, data);
        }
    }

    public TableResult(List<String> columnNames, List<List<String>> data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    public static TableResult create(ResultSet resultSet) {
        int limit = 100;
        List<String> cols;
        List<List<String>> res;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            cols = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                cols.add(metaData.getColumnLabel(i));
            }

            res = new ArrayList<>();

            int i = 0;
            while (resultSet.next() && i <= limit) {
                i++;
                ArrayList<String> row = new ArrayList<>();
                for (int j = 1; j <= columnCount; j++) {
                    String val = StringUtils.defaultString(resultSet.getString(j), "<NULL>");
                    if (val.length() > 25) {
                        val = val.substring(0, 24) + "...";
                    }
                    row.add(val);
                }
                res.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new TableResult(cols, res);
    }

    public static TableResult create(String[] columns, Object[]... lines) {
        List<String> cols = Arrays.asList(columns);
        List<List<String>> rows = new ArrayList<>();
        for (Object[] line : lines) {
            String[] row = new String[line.length];
            for (int i = 0; i < line.length; i++) {
                Object o = line[i];
                row[i] = o == null ? "" : o.toString();
            }
            rows.add(Arrays.asList(row));
        }
        return new TableResult(cols, rows);
    }

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public List<List<String>> getData() {
        return data;
    }

    @Override
    public CommandResultType getResultType() {
        return CommandResultType.TABLE;
    }

    @Override
    public ITableResult getTableResult() {
        return this;
    }

    @Override
    public String getTextResult() {
        throw new UnsupportedOperationException("This result type not supported");
    }
}
