package info.xonix.sqlsh;

import java.util.List;

/**
 * TODO: scrolling ability
 * User: xonix
 * Date: 4/19/14
 * Time: 11:49 PM
 */
public interface ITableResult {
    List<String> getColumnNames();

    List<List<String>> getData();
}
