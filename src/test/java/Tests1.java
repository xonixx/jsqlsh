import info.xonix.sqlsh.*;
import org.junit.Test;

import java.util.Arrays;

/**
 * User: xonix
 * Date: 4/25/14
 * Time: 4:36 PM
 */
public class Tests1 {
    @Test
    public void test1() throws CommandParseException {
        System.out.println(Engine.processArgs(Arrays.asList()));
        System.out.println(Engine.processArgs(Arrays.asList("eee", "fff", "qqq")));
        System.out.println(Engine.processArgs(Arrays.asList("-aaa", "bbb", "ccc", "-d", "eee", "-f", "fff", "qqq", "zzz")));
        System.out.println(Engine.processArgs(Arrays.asList("-a", "-b", "-c")));
        try {
            System.out.println(Engine.processArgs(Arrays.asList("rrr", "-a", "-b", "-c")));
        } catch (CommandParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testInvokeCmd() throws CommandExecutionException {
        Engine engine = new Engine();

        exec(engine, "help");
        exec(engine, "help add");
        exec(engine, "add -a 5 -b 2");
    }

    private void exec(Engine engine, String cmd) throws CommandExecutionException {
        System.out.println("Executing " + cmd);
        ICommandParseResult commandParseResult = engine.parseCommand(cmd);
        if (commandParseResult.isValid()) {
            ICommandResult result = commandParseResult.getCommand().execute(null);
            System.out.println(result.getTextResult());
        } else {
            System.out.println(commandParseResult.getErrors());
        }
        System.out.println();
        System.out.println();
    }
}
