package info.xonix.sqlsh;

import info.xonix.sqlsh.db.ColumnDescriptor;
import info.xonix.sqlsh.db.MetadataAccessor;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;

import java.util.*;

/**
 * User: xonix
 * Date: 6/7/14
 * Time: 5:48 PM
 */
public class DbObjectsCompleter implements Completer {
    private ISession session;
    private Map<String, Completer> dbCompletersCache = new HashMap<>();

    public DbObjectsCompleter(ISession session) {
        this.session = session;
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {

        IDbObject currentObject = session.getCurrentObject();
        DbObjectType type = currentObject.getType();

        if (type == null || type.ordinal() < DbObjectType.DATABASE.ordinal()) {
            return -1;
        }

        String descriptor = currentObject.getOpenCommand().getDescriptor();

        Completer completer = dbCompletersCache.get(descriptor);
        if (completer == null) {
//            System.out.println("building comleter: " + descriptor);
            ArgumentCompleter argumentCompleter = new ArgumentCompleter(
                    new ArgumentCompleter.AbstractArgumentDelimiter() {
                        @Override
                        public boolean isDelimiterChar(CharSequence buffer, int pos) {
                            char ch = buffer.charAt(pos);
                            return '/' == ch || Character.isWhitespace(ch);
                        }
                    },
                    new StringsCompleter(listCompletionsForDb(currentObject)));
            argumentCompleter.setStrict(false);
            completer = argumentCompleter;
            dbCompletersCache.put(descriptor, completer);
        }

        return completer.complete(buffer, cursor, candidates);
    }

    private Collection<String> listCompletionsForDb(IDbObject currentObject) {
        List<String> result = new LinkedList<>();

        String dbName = currentObject.getParent(DbObjectType.DATABASE).getName();

        MetadataAccessor metadataAccessor = currentObject.getMetadataAccessor();

        for (String tbl : metadataAccessor.listTables(dbName)) {
//            System.out.println("tbl " + tbl);
            result.add(tbl);
            for (ColumnDescriptor clmn : metadataAccessor.listColumns(dbName, tbl)) {
//                System.out.println("clm " + clmn.field);
                result.add(clmn.field);
            }
        }

        return result;
    }
}
