package info.xonix.sqlsh;

import org.reflections.Reflections;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * User: xonix
 * Date: 4/21/14
 * Time: 10:19 PM
 */
public class Engine implements IEngine {
    public static final String COMMANDS_FILE = "/jsqlsh/commands.txt";

    @Override
    public ICommandParseResult parseCommand(String commandLine) {
        return null;
    }

    /**
     * @return list of all available commands sorted by name
     */
    public static List<Command> listAllCommands() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> resources;
        try {
            resources = cl.getResources(COMMANDS_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Command> res = new LinkedList<>();

        while (resources.hasMoreElements()) {
            URL commandsFileUrl = resources.nextElement();

            res.addAll(processCommandsFile(commandsFileUrl));
        }

        res.sort((a, b) -> a.name().compareTo(b.name()));

        return res;
    }

    private static Collection<? extends Command> processCommandsFile(URL commandsFileUrl) {
        List<String> strings;
        try {
            strings = Files.readAllLines(Paths.get(commandsFileUrl.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        LinkedList<Command> res = new LinkedList<>();

        for (String string : strings) {
            if (!string.startsWith("#")) {
                string = string.trim();

                Reflections packageRefl = new Reflections(string);
                Set<Class<?>> commandClasses = packageRefl.getTypesAnnotatedWith(Command.class);

                for (Class<?> commandClass : commandClasses) {
                    res.add(commandClass.getAnnotation(Command.class));
                }
            }
        }

        return res;
    }
}
