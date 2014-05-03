package info.xonix.sqlsh;

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

    public static ICommandResult table(ITableResult result) {
        return new ICommandResult() {
            @Override
            public CommandResultType getResultType() {
                return CommandResultType.TABLE;
            }

            @Override
            public ITableResult getTableResult() {
                return result;
            }

            @Override
            public String getTextResult() {
                throw new UnsupportedOperationException("This result type not supported");
            }
        };
    }
}
