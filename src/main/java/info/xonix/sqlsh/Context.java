package info.xonix.sqlsh;

import info.xonix.sqlsh.store.IStore;

/**
 * User: xonix
 * Date: 5/3/14
 * Time: 5:46 PM
 */
public class Context implements IContext {
    private IStore store;
    private ISession session;

    public Context(IStore store, ISession session) {
        this.store = store;
        this.session = session;
    }

    @Override
    public IStore getStore() {
        return store;
    }

    @Override
    public ISession getSession() {
        return session;
    }
}
