package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:41 PM
 */
public interface ICommandResult {
    CommandResultType getResultType();

    default String getTextResult() {
        throw new UnsupportedOperationException("This result type not supported");
    }

    default ITableResult getTableResult() {
        throw new UnsupportedOperationException("This result type not supported");
    }
}
