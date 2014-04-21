package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/20/14
 * Time: 12:10 AM
 */
public interface IEngine {
    ICommandParseResult parseCommand(String commandLine);
}
