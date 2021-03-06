package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/19/14
 * Time: 11:55 PM
 */
public class CommandExecutionException extends Exception {
    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        if (getCause() == null)
            return super.getMessage();
        else
            return super.getMessage() + ": " + getCause().getMessage();
    }
}
