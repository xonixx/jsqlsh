package bug1;

import info.xonix.sqlsh.Command;
import org.reflections.Reflections;

import java.util.Set;

/**
 * User: xonix
 * Date: 4/26/14
 * Time: 1:00 AM
 */
public class TestBug1 {
    public static void main(String[] args) {
        Set<Class<?>> classes = new Reflections("bug1").getTypesAnnotatedWith(Command.class);
        for (Class<?> aClass : classes) {
            System.out.println(aClass);
        }
    }
}
