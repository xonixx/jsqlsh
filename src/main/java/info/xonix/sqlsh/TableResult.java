package info.xonix.sqlsh;

import org.apache.commons.lang.StringUtils;

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
