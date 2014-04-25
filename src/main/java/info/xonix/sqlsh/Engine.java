package info.xonix.sqlsh;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * User: xonix
 * Date: 4/21/14
 * Time: 10:19 PM
 */
public class Engine implements IEngine {
    private static interface IPrm {
        CommandParam getParam();

        Class getParamType();

        boolean isValid(String value);

        void set(String value);
    }

    private static class KeyVal {
        final String key;
        String val;

        private KeyVal(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    public static abstract class Args {
        abstract List<KeyVal> getArgs();

        abstract String getValue();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (KeyVal keyVal : getArgs()) {
                sb.append('"')
                        .append(keyVal.key)
                        .append("\"=\"")
                        .append(StringUtils.defaultString(keyVal.val))
                        .append("\", ");
            }
            String value = getValue();
            if (value != null) {
                sb.append("\"value\"=\"")
                        .append(value)
                        .append("\", ");
            }
            if (sb.length() > 0)
                sb.setLength(sb.length() - 2);
            return sb.toString();
        }
    }

    @Override
    public ICommandParseResult parseCommand(String commandLine) {
        List<String> args;
        try {
            args = Utils.translateCommandline(commandLine);
            ICommand command = parseArgs(args);
            return ICommandParseResult.command(command);
        } catch (CommandParseException e) {
            return ICommandParseResult.error(e.getMessage());
        }
    }

    private ICommand parseArgs(List<String> args) throws CommandParseException {
        if (args == null || args.isEmpty()) {
            throw new CommandParseException("empty command");
        }

        String cmdName = args.get(0);

        Cmd cmd = Core.resolveCommand(cmdName);

        if (cmd == null) {
            throw new CommandParseException("command " + cmdName + " doesn't exit");
        }

        Class cls = cmd.klass;
        List<IPrm> prms = listPrms(cls);
        Args cmdArgs = processArgs(args.subList(1, args.size()));

        return bind(prms, cmdArgs);
    }

    private ICommand bind(List<IPrm> prms, Args cmdArgs) throws CommandParseException {
        Map<String, String> params = new HashMap<>();
        Set<String> knownParams = new HashSet<>();

        for (IPrm prm : prms) {
            knownParams.add(prm.getParam().name());
        }

        for (KeyVal keyVal : cmdArgs.getArgs()) {
            params.put(keyVal.key, keyVal.val);
        }

        List<String> errors = new LinkedList<>();

        for (IPrm prm : prms) {
            String pName = prm.getParam().name();

            if (!prm.getParam().optional() && !params.containsKey(pName)) {
                errors.add("Param " + pName + " is mandatory");
            }

            if (params.containsKey(pName)) {
                String pVal = params.get(pName);

                if (prm.isValid(pVal)) {
                    prm.set(pVal);
                } else {
                    errors.add("Param value '" + pVal + "' is invalid for param '" + pName + "' with type " + prm.getParamType());
                }
            }
        }

        for (String p : params.keySet()) {
            if (!knownParams.contains(p)) {
                errors.add("Param '" + p + "' is unknown");
            }
        }

        if (errors.isEmpty())
            return null;
        else {
            StringBuilder sb = new StringBuilder();
            for (String error : errors) {
                sb.append(error);
                sb.append('\n');
            }
            sb.setLength(sb.length() - 1);
            throw new CommandParseException(sb.toString());
        }

    }

    public static Args processArgs(List<String> args) throws CommandParseException {
        LinkedList<KeyVal> keyVals = new LinkedList<>();
        String value = null;

        int lastArgIdx = -1;
        for (int i = args.size() - 1; i >= 0; i--) {
            String arg = args.get(i);
            if (arg.startsWith("-")) {
                lastArgIdx = i;
                break;
            }
        }

        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);

            if (arg.startsWith("-")) {
                if (value != null) {
                    throw new CommandParseException("Arg " + arg + " can't follow value");
                }
                KeyVal keyVal = new KeyVal(arg.substring(1), null);
                keyVals.add(keyVal);
            } else {
                if (keyVals.isEmpty() || i > lastArgIdx + 1) {
                    if (value == null) {
                        value = arg;
                    } else {
                        value += " " + arg;
                    }
                } else {
                    KeyVal keyVal = keyVals.getLast();
                    if (keyVal.val == null) {
                        keyVal.val = arg;
                    } else {
                        keyVal.val += " " + arg;
                    }
                }
            }
        }
        final String finalValue = value;
        return new Args() {
            @Override
            public List<KeyVal> getArgs() {
                return keyVals;
            }

            @Override
            public String getValue() {
                return finalValue;
            }
        };
    }

    private List<IPrm> listPrms(Class cls) {
        return null;
    }
}
