package info.xonix.sqlsh;

/**
* User: xonix
* Date: 5/31/14
* Time: 10:08 PM
*/
public class TextResult implements ICommandResult {
    private final String result;

    public TextResult(String result) {
        this.result = result;
    }

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
}
