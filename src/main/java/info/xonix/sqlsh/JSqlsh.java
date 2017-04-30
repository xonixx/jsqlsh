package info.xonix.sqlsh;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import info.xonix.sqlsh.command.ExitCommand;
import info.xonix.sqlsh.store.IStore;
import info.xonix.sqlsh.store.XmlStore;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * User: xonix
 * Date: 4/27/14
 * Time: 8:56 PM
 */
public class JSqlsh {
    public static void main(String[] args) {
        try {
            File settingsFolder = new File(".jsqlsh");
            if (!settingsFolder.exists()) {
                if (!settingsFolder.mkdirs()) {
                    throw new RuntimeException("Unable to create settings folder");
                }
            } else if (!settingsFolder.isDirectory()) {
                throw new RuntimeException("Not a folder: " + settingsFolder.getName());
            }
            ConsoleReader console = new ConsoleReader();
            FileHistory jlineHistory = new FileHistory(new File(settingsFolder, ".history"));
            console.setHistory(jlineHistory);
            PrintWriter out = new PrintWriter(console.getOutput());
            console.print("This is JSQLSH, print help (or help cmd) for information\n\n");
            String line;
            Engine engine = new Engine();
            Session session = new Session();
            console.addCompleter(new DbObjectsCompleter(session));
            session.setCurrentObject(DbObject.ROOT);
            IStore store = new XmlStore(new File(settingsFolder, ".jsqlsh.xml"));
            Engine.setJsqlshStore(store);
            session.changePrompt(console);
            Context context = new Context(store, session,
                    new IConsole() {
                        @Override
                        public String getString(String prompt) {
                            console.setPrompt(prompt);
                            String result = "";
                            try {
                                result = console.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            session.changePrompt(console);
                            return result;
                        }

                        @Override
                        public String getPassword(String prompt) {
                            throw new UnsupportedOperationException("tbd");
                        }
                    });
            Engine.setContext(context);
            while ((line = console.readLine()) != null) {
                ICommandParseResult commandParseResult = engine.parseCommand(line);
                String err = null;
                Throwable throwable = null;
                if (commandParseResult.isValid()) {
                    ICommandResult result = null;
                    ICommand command = commandParseResult.getCommand();
                    if (command instanceof ExitCommand) {
                        out.println("Bye!");
                        out.flush();
                        jlineHistory.flush();
                        break;
                    }
                    try {
                        result = command.execute(context);
                        session.changePrompt(console);
                    } catch (Throwable e) {
                        err = e.getMessage();
                        throwable = e;
                    }
                    if (result != null) {
                        if (result.getResultType() == CommandResultType.TEXT) {
                            out.print(result.getTextResult());
                        } else {
                            ITableResult tableResult = result.getTableResult();

                            if (tableResult.getData().size() > 0) {
                                AsciiTable asciiTable = new AsciiTable();
                                asciiTable.getRenderer().setCWC(new CWC_LongestLine());

                                List<String> columnNames = tableResult.getColumnNames();

                                asciiTable.addRule();
                                asciiTable.addRow(columnNames);
                                asciiTable.addRule();

                                List<List<String>> tableData = tableResult.getData();

                                for (int i = 0; i < tableData.size(); i++) {
                                    List<String> rows = tableData.get(i);

                                    asciiTable.addRow(rows);
                                }
                                asciiTable.addRule();
                                out.print(asciiTable.render());
                            }
                        }
                        out.print(result.getResultType() == CommandResultType.TABLE
                                || "".equals(result.getTextResult()) ? "\n" : "\n\n");
                    }
                } else {
                    err = commandParseResult.getErrors();
                }

                if (err != null) {
                    out.print("\u001B[33m");// red
                    out.print(err);
                    out.println("\u001B[0m");
                }
                if (throwable != null) {
                    out.print("\u001B[33m");// red
                    if (!(throwable instanceof CommandExecutionException)) {
                        throwable.printStackTrace();
                    }
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
