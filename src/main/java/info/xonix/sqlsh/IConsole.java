package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 6/1/14
 * Time: 5:18 PM
 */
public interface IConsole {
    String getString(String prompt);

    String getPassword(String prompt);
}
