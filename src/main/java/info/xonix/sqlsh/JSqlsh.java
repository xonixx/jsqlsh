package info.xonix.sqlsh;

import info.xonix.sqlsh.command.ExitCommand;
import info.xonix.sqlsh.store.XmlStore;
import jline.TerminalFactory;
import jline.console.ConsoleReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: xonix
 * Date: 4/27/14
 * Time: 8:56 PM
 */
public class JSqlsh {
    public static void main(String[] args) {
        try {
            ConsoleReader console = new ConsoleReader();
            PrintWriter out = new PrintWriter(console.getOutput());
            console.print("This is JSQLSH, print help (or help cmd) for information\n\n");
            console.setPrompt("> ");
            String line;
            Engine engine = new Engine();
            Context context = new Context(new XmlStore(new File(".jsqlsh.xml")), new Session());
            while ((line = console.readLine()) != null) {
                ICommandParseResult commandParseResult = engine.parseCommand(line);
                String err = null;
                if (commandParseResult.isValid()) {
                    ICommandResult result = null;
                    ICommand command = commandParseResult.getCommand();
                    if (command instanceof ExitCommand) {
                        out.println("Bye!");
                        out.flush();
                        break;
                    }
                    try {
                        result = command.execute(context);
                    } catch (CommandExecutionException e) {
                        err = e.getMessage();
                    }
                    if (result != null) {
                        if (result.getResultType() == CommandResultType.TEXT) {
                            out.print(result.getTextResult());
                        } else {
                            out.print("TBD: table result");
                        }
                        out.print("\n");
                    }
                } else {
                   err = commandParseResult.getErrors();
                }

                if (err != null) {
                    out.print("\u001B[33m");// red
                    out.print(err);
                    out.println("\u001B[0m");
                }

                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                TerminalFactory.get().restore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
