package info.xonix.sqlsh;

import org.reflections.Reflections;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: xonix
 * Date: 4/25/14
 * Time: 3:14 PM
 */
public class Core {
    public static final String COMMANDS_FILE = "jsqlsh/commands.txt";

    private static List<Cmd> listAllCmds() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> resources;
        try {
            resources = cl.getResources(COMMANDS_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Cmd> res = new LinkedList<>();

        while (resources.hasMoreElements()) {
            URL commandsFileUrl = resources.nextElement();

            res.addAll(processCommandsFile(commandsFileUrl));
        }

        return res;
    }

    private static Map<String,Cmd> buildNameToCmd() {
        Map<String,Cmd> res = new HashMap<>();

        for (Cmd cmd : listAllCmds()) {
            res.put(cmd.command.name(), cmd);
        }

        return res;
    }

    public static Cmd resolveCommand(String name) {
        return buildNameToCmd().get(name);
    }

    /**
     * @return list of all available commands sorted by name
     */
    public static List<Cmd> listAllCommands() {
        List<Cmd> res = listAllCmds();

        res.sort((a, b) -> a.command.name().compareTo(b.command.name()));

        return res;
    }

    private static Collection<Cmd> processCommandsFile(URL commandsFileUrl) {
        List<String> strings;
        try {
            strings = Files.readAllLines(Paths.get(commandsFileUrl.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        LinkedList<Cmd> res = new LinkedList<>();

        for (String string : strings) {
            if (!string.startsWith("#")) {
                string = string.trim();

                Reflections packageRefl = new Reflections(string);
                Set<Class<?>> commandClasses = packageRefl.getTypesAnnotatedWith(Command.class);

                for (Class<?> commandClass : commandClasses) {
                    res.add(new Cmd(commandClass.getAnnotation(Command.class), commandClass));
                }
            }
        }

        return res;
    }
}
