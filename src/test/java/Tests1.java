import info.xonix.sqlsh.CommandParseException;
import info.xonix.sqlsh.Engine;
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
        System.out.println(Engine.processArgs(Arrays.asList("eee", "fff", "qqq")));
        System.out.println(Engine.processArgs(Arrays.asList("-aaa", "bbb", "ccc", "-d", "eee", "-f", "fff", "qqq", "zzz")));
        System.out.println(Engine.processArgs(Arrays.asList("-a", "-b", "-c")));
        try {
            System.out.println(Engine.processArgs(Arrays.asList("rrr", "-a", "-b", "-c")));
        } catch (CommandParseException e) {
            System.out.println(e.getMessage());
        }
    }
}
