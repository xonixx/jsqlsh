package info.xonix.sqlsh;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:41 PM
 */
public interface ICommandResult {
    CommandResultType getResultType();

    abstract String getTextResult();

    abstract ITableResult getTableResult();

    /*public static ICommandResult exception(Throwable throwable) {
        return text(throwable.getMessage());
    }*/

    public static ICommandResult text(String result) {
        return new ICommandResult() {
            @Override
            public CommandResultType getResultType() {
                return CommandResultType.TEXT;
            }

            @Override
            public String getTextResult() {
                return result;
            }

            @Override
            public ITableResult getTableResult() {
                throw new UnsupportedOperationException("This result type not supported");
            }
        };
    }

    public static ICommandResult table(ResultSet resultSet) {
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
                    row.add(resultSet.getString(j));
                }
                res.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new TableResult(cols, res);
    }

    public static ICommandResult table(String[] columns, Object[]... lines) {
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
}
