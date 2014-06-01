package info.xonix.sqlsh;

import info.xonix.sqlsh.store.IStore;

/**
 * User: xonix
 * Date: 5/3/14
 * Time: 5:46 PM
 */
public class Context implements IContext {
    private final IStore store;
    private final ISession session;
    private final IConsole console;

    public Context(IStore store, ISession session, IConsole console) {
        this.store = store;
        this.session = session;
        this.console = console;
    }

    @Override
    public IStore getStore() {
        return store;
    }

    @Override
    public ISession getSession() {
        return session;
    }

    @Override
    public IConsole getConsole() {
        return console;
    }
}
