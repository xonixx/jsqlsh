package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 4/20/14
 * Time: 12:15 AM
 */
public interface ICommandParseResult {
    String getErrors();

    boolean isValid();

    ICommand getCommand();

    static class CommandParseResult implements ICommandParseResult {
        String error;
        ICommand command;

        public CommandParseResult(String error, ICommand command) {
            this.error = error;
            this.command = command;
        }

        @Override
        public String getErrors() {
            return error;
        }

        @Override
        public ICommand getCommand() {
            return command;
        }

        public boolean isValid() {
            return getCommand() != null;
        }
    }

    public static ICommandParseResult error(String error) {
        return new CommandParseResult(error,null);
    }

    public static ICommandParseResult command(ICommand command) {
        return new CommandParseResult(null,command);
    }
}
