package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:41 PM
 */
public interface ICommandResult {
    CommandResultType getResultType();

    String getTextResult();

    ITableResult getTableResult();
}
